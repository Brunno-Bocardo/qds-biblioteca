package br.edu.ifsp.biblioteca.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import br.edu.ifsp.biblioteca.model.Emprestimo;
import br.edu.ifsp.biblioteca.model.Emprestimo.StatusEmprestimo;
import br.edu.ifsp.biblioteca.model.Estoque;
import br.edu.ifsp.biblioteca.model.Usuario;
import br.edu.ifsp.biblioteca.model.Usuario.StatusUsuario;
import br.edu.ifsp.biblioteca.repository.EmprestimoRepository;

@Service
@Transactional
public class EmprestimoService {

	private final EmprestimoRepository emprestimoRepository;
	private final UsuarioService usuarioService;
	private final EstoqueService estoqueService;
	
	public EmprestimoService(EmprestimoRepository emprestimoRepository, UsuarioService usuarioService, EstoqueService estoqueService) {
		this.emprestimoRepository = emprestimoRepository;
		this.usuarioService = usuarioService;
		this.estoqueService = estoqueService;
	}
	
	public Emprestimo registrarEmprestimo(String cpf, String codigoExemplar) {
		if(cpf == null || cpf.isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CPF é obrigatório");
		}
		
		if(codigoExemplar == null || codigoExemplar.isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Codigo do Exemplar é obrigatorio");
		}
		
		this.usuarioService.validarCpfFormato(cpf);
		
		Usuario usuario = this.usuarioService.procurarPorCpf(cpf);
		StatusUsuario status = usuario.getStatus();
		
		if(status != StatusUsuario.ATIVO) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Apenas usuários ativos podem realizar emprestimos");
		}
		
		Estoque estoque = this.estoqueService.buscarPorCodigo(codigoExemplar);
		if(!estoque.isDisponivel()) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Apenas exemplares disponíveis podem ser emprestados");
		}
		
		LocalDate dataEmprestimo = LocalDate.now();
		// Validar o tipo do usuario pois o tempo vai variar
		LocalDate dataPrevistaDevolucao = dataEmprestimo.plusDays(40);
		Emprestimo novoEmprestimo = new Emprestimo();
		
		novoEmprestimo.setUsuario(usuario);
		novoEmprestimo.setEstoque(estoque);
		novoEmprestimo.setStatus(StatusEmprestimo.ATIVO);
		novoEmprestimo.setDataEmprestimo(dataEmprestimo);
		novoEmprestimo.setDataPrevistaDevolucao(dataPrevistaDevolucao);
		
		return emprestimoRepository.save(novoEmprestimo);
	}
	
	public List<Emprestimo>listarEmprestimos(){
		// Verificar se apenas os emprestimos ativos devem ser retornados 
		return emprestimoRepository.findAll();
	}
	
	public Emprestimo registrarDevolucaoEmprestimo(int idEmprestimo) {
		
		Emprestimo emprestimo = emprestimoRepository.getReferenceById(idEmprestimo);
		
		LocalDate dataDevolucao = LocalDate.now();
		LocalDate dataPrevistaDevolucao = emprestimo.getDataPrevistaDevolucao();
		
		emprestimo.setDataDevolucao(dataDevolucao);
		emprestimo.setStatus(StatusEmprestimo.DEVOLVIDO);
		
		if(dataDevolucao.isAfter(dataPrevistaDevolucao)) {
			
			emprestimo.setStatus(StatusEmprestimo.ATRASADO);
			long diasDeAtraso = ChronoUnit.DAYS.between(dataDevolucao, dataPrevistaDevolucao);
			
			if(diasDeAtraso > 60) {
				emprestimo.setStatus(StatusEmprestimo.SUSPENSO);
			} else {
				LocalDate dataDeSuspensao = dataDevolucao.plusDays(diasDeAtraso * 3);
				emprestimo.setDataSuspensao(dataDeSuspensao);
			}
		}
		
		return emprestimo;
	}
	
	
	
}
