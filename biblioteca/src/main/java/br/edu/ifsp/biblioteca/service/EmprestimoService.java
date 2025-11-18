package br.edu.ifsp.biblioteca.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import br.edu.ifsp.biblioteca.dto.EmprestimoCreateDto;
import br.edu.ifsp.biblioteca.factory.EmprestimoValidationChainFactory;
import br.edu.ifsp.biblioteca.handler.ValidationHandler;
import br.edu.ifsp.biblioteca.model.Emprestimo;
import br.edu.ifsp.biblioteca.model.Emprestimo.StatusEmprestimo;
import br.edu.ifsp.biblioteca.model.Estoque;
import br.edu.ifsp.biblioteca.model.Usuario;
import br.edu.ifsp.biblioteca.model.Usuario.StatusUsuario;
import br.edu.ifsp.biblioteca.repository.EmprestimoRepository;
import br.edu.ifsp.biblioteca.strategy.EmprestimoStrategy;

@Service
@Transactional
public class EmprestimoService {
	private final ValidationHandler<EmprestimoCreateDto> handlerChain;
	private final EmprestimoRepository emprestimoRepository;
	private final UsuarioService usuarioService;
	private final EstoqueService estoqueService;
	private final EmprestimoStrategy emprestimoStrategy;
	
	public EmprestimoService(EmprestimoRepository emprestimoRepository, UsuarioService usuarioService, EstoqueService estoqueService, EmprestimoStrategy emprestimoStrategy) {
		this.handlerChain = EmprestimoValidationChainFactory.createEmprestimoChain();
        this.emprestimoRepository = emprestimoRepository;
        this.usuarioService = usuarioService;
        this.estoqueService = estoqueService;
        this.emprestimoStrategy = emprestimoStrategy;
	}
	
	public Emprestimo registrarEmprestimo(EmprestimoCreateDto emprestimoDto) {
        handlerChain.handle(emprestimoDto);

        Usuario usuario = this.usuarioService.procurarPorCpf(emprestimoDto.getCpf());
        StatusUsuario status = usuario.getStatus();

        if (status != StatusUsuario.ATIVO) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Only active users can borrow books");
        }

        Estoque estoque = this.estoqueService.buscarPorCodigo(emprestimoDto.getCodigoExemplar());
        if (!estoque.isDisponivel()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Only available items can be borrowed");
        }

        int activeLoans = emprestimoRepository.countByUsuarioAndStatus(usuario, StatusEmprestimo.ATIVO);
        int maxLoans = emprestimoStrategy.maxAllowedActiveLoans(usuario);

        if (activeLoans >= maxLoans) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário atingiu o número máximo de empréstimos");
        }

        LocalDate loanDate = LocalDate.now();
        LocalDate dueDate = emprestimoStrategy.calculateDueDate(usuario, estoque, loanDate);

        Emprestimo novoEmprestimo = new Emprestimo();
        novoEmprestimo.setUsuario(usuario);
        novoEmprestimo.setEstoque(estoque);
        novoEmprestimo.setStatus(StatusEmprestimo.ATIVO);
        novoEmprestimo.setDataEmprestimo(loanDate);
        novoEmprestimo.setDataPrevistaDevolucao(dueDate);

        return emprestimoRepository.save(novoEmprestimo);
    }
	
	public List<Emprestimo>listarEmprestimos(){
		return emprestimoRepository.findAll();
	}
	
	public Emprestimo procurarEmprestimoPorId(int idEmprestimo) {
        return emprestimoRepository.findById(idEmprestimo)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empréstimo não encontrado"));
    }
	
	public Emprestimo registrarDevolucaoEmprestimo(int idEmprestimo) {

        Emprestimo emprestimo = emprestimoRepository.getReferenceById(idEmprestimo);
        LocalDate dataDevolucao = LocalDate.now();
        LocalDate dataPrevistaDevolucao = emprestimo.getDataPrevistaDevolucao();

        emprestimo.setDataDevolucao(dataDevolucao);
        emprestimo.setStatus(StatusEmprestimo.DEVOLVIDO);

        if (dataDevolucao.isAfter(dataPrevistaDevolucao)) {

            emprestimo.setStatus(StatusEmprestimo.ATRASADO);
            long diasDeAtraso = ChronoUnit.DAYS.between(dataPrevistaDevolucao, dataDevolucao);

            if (diasDeAtraso > 60) {
                emprestimo.setStatus(StatusEmprestimo.SUSPENSO);
            } else {
                LocalDate dataDeSuspensao = dataDevolucao.plusDays(diasDeAtraso * 3);
                emprestimo.setDataSuspensao(dataDeSuspensao);
            }
        }

        return emprestimo;
    }
}
