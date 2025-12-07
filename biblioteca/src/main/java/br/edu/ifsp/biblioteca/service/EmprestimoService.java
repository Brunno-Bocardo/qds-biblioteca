package br.edu.ifsp.biblioteca.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import br.edu.ifsp.biblioteca.dto.EmprestimoCreateDto;
import br.edu.ifsp.biblioteca.model.Emprestimo;
import br.edu.ifsp.biblioteca.model.Emprestimo.StatusEmprestimo;
import br.edu.ifsp.biblioteca.model.Estoque;
import br.edu.ifsp.biblioteca.model.Usuario;
import br.edu.ifsp.biblioteca.model.Usuario.StatusUsuario;
import br.edu.ifsp.biblioteca.repository.EmprestimoRepository;
import br.edu.ifsp.biblioteca.strategy.EmprestimoStrategyResolver;
import br.edu.ifsp.biblioteca.strategy.EmprestimoValidationChainStrategy;
import br.edu.ifsp.biblioteca.strategy.IEmprestimoStrategy;

@Service
@Transactional
public class EmprestimoService {

    private final EmprestimoRepository emprestimoRepository;
    private final UsuarioService usuarioService;
    private final EstoqueService estoqueService;
    private final EmprestimoStrategyResolver strategyResolver;
    private final EmprestimoValidationChainStrategy emprestimoValidation;

    public EmprestimoService(EmprestimoRepository emprestimoRepository, UsuarioService usuarioService, EstoqueService estoqueService, EmprestimoStrategyResolver strategyResolver, EmprestimoValidationChainStrategy emprestimoValidationChain) {
        this.emprestimoRepository = emprestimoRepository;
        this.usuarioService = usuarioService;
        this.estoqueService = estoqueService;
        this.strategyResolver = strategyResolver;
        this.emprestimoValidation = emprestimoValidationChain;
    }

    public Emprestimo registrarEmprestimo(EmprestimoCreateDto emprestimoDto) {
    	emprestimoValidation.createEmprestimoChain().handle(emprestimoDto);
        Usuario usuario = this.usuarioService.procurarPorCpf(emprestimoDto.getCpf());
        StatusUsuario status = usuario.getStatus();

        if (status != StatusUsuario.ATIVO) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Apenas usuários ativos podem alugar livros");
        }

        Estoque estoque = this.estoqueService.buscarPorCodigo(emprestimoDto.getCodigoExemplar());
        if (!estoque.isDisponivel()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Esse livro não está disponível");
        }

        // --- STRATEGY ---
        IEmprestimoStrategy strategy = strategyResolver.resolve(usuario);
        int activeLoans = emprestimoRepository.countByUsuarioAndStatus(usuario, StatusEmprestimo.ATIVO);
        int maxLoans = strategy.maxAllowedActiveLoans(usuario);

        if (activeLoans >= maxLoans) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário atingiu o limite de empréstimos");
        }

        LocalDate loanDate = LocalDate.now();
        LocalDate dueDate = strategy.calculateDueDate(usuario, estoque, loanDate);

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

		Emprestimo emprestimo = emprestimoRepository.findById(idEmprestimo)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empréstimo não encontrado"));
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
