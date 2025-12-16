package br.edu.ifsp.biblioteca.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import br.edu.ifsp.biblioteca.builder.EmprestimoValidationChainBuilder;

import br.edu.ifsp.biblioteca.dto.EmprestimoCreateDto;
import br.edu.ifsp.biblioteca.handler.CpfHandler;
import br.edu.ifsp.biblioteca.handler.ExemplarHandler;
import br.edu.ifsp.biblioteca.handler.SequenceCpfHandler;
import br.edu.ifsp.biblioteca.handler.ValidationHandler;
import br.edu.ifsp.biblioteca.model.Estoque;
import br.edu.ifsp.biblioteca.model.Usuario;
import br.edu.ifsp.biblioteca.model.Usuario.StatusUsuario;
import br.edu.ifsp.biblioteca.repository.EmprestimoRepository;
import br.edu.ifsp.biblioteca.strategy.EmprestimoStrategyResolver;
import br.edu.ifsp.biblioteca.strategy.IEmprestimoStrategy;
import br.edu.ifsp.biblioteca.model.CategoriaUsuario;
import br.edu.ifsp.biblioteca.model.Emprestimo;
import br.edu.ifsp.biblioteca.model.Emprestimo.StatusEmprestimo;

@ExtendWith(MockitoExtension.class)
public class EmprestimoServiceTest {
	
	@InjectMocks
	private EmprestimoService emprestimoService;
	
	@Mock
    private EmprestimoRepository emprestimoRepository;
	
	@Mock
    private UsuarioService usuarioService;
	
	@Mock
    private EstoqueService estoqueService;

    @Mock
    private EmprestimoStrategyResolver strategyResolver;
    
    @Mock
    private EmprestimoValidationChainBuilder emprestimoValidation;

    @Mock
    private IEmprestimoStrategy strategy;
    
    @Test
    void deveRegistrarEmprestimoComSucesso() {
        
        EmprestimoCreateDto dto = EmprestimoCreateDto.builder()
                .cpf("49667797074")
                .codigoExemplar("EX123")
                .build();

        Usuario usuario = new Usuario();
        usuario.setStatus(Usuario.StatusUsuario.ATIVO);

        Estoque estoque = new Estoque();
        estoque.setDisponivel(true);

        ValidationHandler<EmprestimoCreateDto> handleExemplar = new ExemplarHandler<>(
				EmprestimoCreateDto::getCodigoExemplar);
        ValidationHandler<EmprestimoCreateDto> handleFormatCpf = new CpfHandler<>(EmprestimoCreateDto::getCpf);
        ValidationHandler<EmprestimoCreateDto> handleSequenceCpf = new SequenceCpfHandler<>(
				EmprestimoCreateDto::getCpf);
        
        handleExemplar.setNext(handleFormatCpf);
		handleFormatCpf.setNext(handleSequenceCpf);
		
        when(emprestimoValidation.buildEmprestimoChain())
        .thenReturn(handleExemplar);

        when(usuarioService.procurarPorCpf(dto.getCpf()))
                .thenReturn(usuario);

        when(estoqueService.buscarPorCodigo(dto.getCodigoExemplar()))
                .thenReturn(estoque);

        when(strategyResolver.resolve(usuario))
                .thenReturn(strategy);

        when(emprestimoRepository.countByUsuarioAndStatus(usuario, StatusEmprestimo.ATIVO))
                .thenReturn(0);

        when(strategy.maxAllowedActiveLoans(usuario))
                .thenReturn(3);

        when(strategy.calculateDueDate(any(), any(), any()))
                .thenReturn(LocalDate.now().plusDays(7));

        when(emprestimoRepository.save(any(Emprestimo.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Emprestimo emprestimo = emprestimoService.registrarEmprestimo(dto);
        
        assertNotNull(emprestimo);
        assertFalse(estoque.isDisponivel());
    }
    
    @Test
    void deveRegistrarStatusException() {
        
        EmprestimoCreateDto dto = EmprestimoCreateDto.builder()
                .cpf("49667797074")
                .codigoExemplar("EX123")
                .build();

        Usuario usuario = new Usuario();
        usuario.setStatus(Usuario.StatusUsuario.SUSPENSO);

        Estoque estoque = new Estoque();
        estoque.setDisponivel(true);

        ValidationHandler<EmprestimoCreateDto> handleExemplar = new ExemplarHandler<>(
				EmprestimoCreateDto::getCodigoExemplar);
		
        when(emprestimoValidation.buildEmprestimoChain())
        .thenReturn(handleExemplar);

        when(usuarioService.procurarPorCpf(dto.getCpf()))
                .thenReturn(usuario);

        ResponseStatusException exception =
                assertThrows(ResponseStatusException.class,
                    () -> emprestimoService.registrarEmprestimo(dto));

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
        assertEquals("Apenas usuários ativos podem alugar livros", exception.getReason());
    }
    
    @Test
    void deveRegistrarLoansException() {
        
        EmprestimoCreateDto dto = EmprestimoCreateDto.builder()
                .cpf("49667797074")
                .codigoExemplar("EX123")
                .build();

        Usuario usuario = new Usuario();
        CategoriaUsuario categoriaUsuario = new CategoriaUsuario();
        
        categoriaUsuario.setNomeCategoriaUsuario("Aluno");
        usuario.setCategoria(categoriaUsuario);
        usuario.setStatus(Usuario.StatusUsuario.ATIVO);

        Estoque estoque = new Estoque();
        estoque.setDisponivel(true);

        ValidationHandler<EmprestimoCreateDto> handleExemplar = new ExemplarHandler<>(
				EmprestimoCreateDto::getCodigoExemplar);
		
        when(emprestimoValidation.buildEmprestimoChain())
        .thenReturn(handleExemplar);

        when(usuarioService.procurarPorCpf(dto.getCpf()))
                .thenReturn(usuario);
        
        when(estoqueService.buscarPorCodigo(dto.getCodigoExemplar()))
        .thenReturn(estoque);

		when(strategyResolver.resolve(usuario))
		        .thenReturn(strategy);

		when(emprestimoRepository.countByUsuarioAndStatus(usuario, StatusEmprestimo.ATIVO))
		        .thenReturn(3);
		
		when(strategy.maxAllowedActiveLoans(usuario))
		        .thenReturn(3);

        ResponseStatusException exception =
                assertThrows(ResponseStatusException.class,
                    () -> emprestimoService.registrarEmprestimo(dto));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Aluno já possui 3 empréstimos ativos. Limite permitido: 3", exception.getReason());
    }
    
    @Test
    void deveRegistrarLoansExceptionUsuario() {
        
        EmprestimoCreateDto dto = EmprestimoCreateDto.builder()
                .cpf("49667797074")
                .codigoExemplar("EX123")
                .build();

        Usuario usuario = new Usuario();

        usuario.setStatus(Usuario.StatusUsuario.ATIVO);

        Estoque estoque = new Estoque();
        estoque.setDisponivel(true);

        ValidationHandler<EmprestimoCreateDto> handleExemplar = new ExemplarHandler<>(
				EmprestimoCreateDto::getCodigoExemplar);
		
        when(emprestimoValidation.buildEmprestimoChain())
        .thenReturn(handleExemplar);

        when(usuarioService.procurarPorCpf(dto.getCpf()))
                .thenReturn(usuario);
        
        when(estoqueService.buscarPorCodigo(dto.getCodigoExemplar()))
        .thenReturn(estoque);

		when(strategyResolver.resolve(usuario))
		        .thenReturn(strategy);

		when(emprestimoRepository.countByUsuarioAndStatus(usuario, StatusEmprestimo.ATIVO))
		        .thenReturn(3);
		
		when(strategy.maxAllowedActiveLoans(usuario))
		        .thenReturn(3);

        ResponseStatusException exception =
                assertThrows(ResponseStatusException.class,
                    () -> emprestimoService.registrarEmprestimo(dto));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Usuário já possui 3 empréstimos ativos. Limite permitido: 3", exception.getReason());
    }
    
    @Test
    void deveRegistrarEstoqueException() {
        
    	EmprestimoCreateDto dto = EmprestimoCreateDto.builder()
                .cpf("49667797074")
                .codigoExemplar("EX123")
                .build();
    	
    	Estoque estoque = new Estoque();
        estoque.setDisponivel(false);
        
        Usuario usuario = new Usuario();
        usuario.setStatus(StatusUsuario.ATIVO);
        usuario.setCpf(dto.getCpf());
    	
    	ValidationHandler<EmprestimoCreateDto> handleExemplar = new ExemplarHandler<>(
				EmprestimoCreateDto::getCodigoExemplar);
    	
        when(emprestimoValidation.buildEmprestimoChain())
		.thenReturn(handleExemplar);
		
        when(usuarioService.procurarPorCpf(dto.getCpf()))
        .thenReturn(usuario);

		when(estoqueService.buscarPorCodigo(dto.getCodigoExemplar()))
		        .thenReturn(estoque);
	        
        ResponseStatusException exception =
                assertThrows(ResponseStatusException.class,
                    () -> emprestimoService.registrarEmprestimo(dto));
        
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
        assertEquals("Esse livro não está disponível", exception.getReason());
    }
    
    @Test
    void deveRegistrarDevolucaoComSucesso() {
    	
        Estoque estoque = new Estoque();
        estoque.setDisponivel(true);
        
        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setEstoque(estoque);
        emprestimo.setStatus(StatusEmprestimo.ATIVO);
        emprestimo.setDataEmprestimo(LocalDate.now().minusDays(5));
        emprestimo.setDataPrevistaDevolucao(LocalDate.now());
		
		when(emprestimoRepository.findById(1))
		        .thenReturn(Optional.of(emprestimo));
	        
        Emprestimo emprestimoReturn = emprestimoService.registrarDevolucaoEmprestimo(1);

        assertNotNull(emprestimoReturn);
        assertTrue(estoque.isDisponivel());
        assertNotNull(emprestimoReturn.getDataDevolucao());
        assertEquals(StatusEmprestimo.DEVOLVIDO, emprestimoReturn.getStatus());
    }
    
    @Test
    void deveRegistrarEmprestimoException() {
		
		when(emprestimoRepository.findById(1))
		        .thenReturn(Optional.empty());
	        
        ResponseStatusException exception =
                assertThrows(ResponseStatusException.class,
                    () -> emprestimoService.registrarDevolucaoEmprestimo(1));
        
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Empréstimo não encontrado", exception.getReason());
    }
    
    @Test
    void deveRegistrarEmprestimoSuspenso() {
   
        Emprestimo emprestimo = new Emprestimo();
       
        emprestimo.setStatus(StatusEmprestimo.ATIVO);
        emprestimo.setDataEmprestimo(LocalDate.now().minusDays(90));
        emprestimo.setDataPrevistaDevolucao(LocalDate.now().minusDays(61));
		
		when(emprestimoRepository.findById(1))
		        .thenReturn(Optional.of(emprestimo));
	        
        Emprestimo emprestimoReturn = emprestimoService.registrarDevolucaoEmprestimo(1);

        assertNotNull(emprestimoReturn);
        assertNotNull(emprestimoReturn.getDataDevolucao());
        assertEquals(StatusEmprestimo.SUSPENSO, emprestimoReturn.getStatus());
    }
    
    @Test
    void deveRegistrarEmprestimoAtrasado() {
   
        Emprestimo emprestimo = new Emprestimo();
       
        emprestimo.setStatus(StatusEmprestimo.ATIVO);
        emprestimo.setDataEmprestimo(LocalDate.now().minusDays(90));
        emprestimo.setDataPrevistaDevolucao(LocalDate.now().minusDays(60));
		
		when(emprestimoRepository.findById(1))
		        .thenReturn(Optional.of(emprestimo));
	        
        Emprestimo emprestimoReturn = emprestimoService.registrarDevolucaoEmprestimo(1);

        assertNotNull(emprestimoReturn);
        assertNotNull(emprestimoReturn.getDataDevolucao());
        assertEquals(StatusEmprestimo.ATRASADO, emprestimoReturn.getStatus());
    }
    
    @Test
    void deveRetornarListaDeEmprestimo() {
   
        Emprestimo emprestimo = new Emprestimo();
       
		when(emprestimoRepository.findById(1))
		        .thenReturn(Optional.of(emprestimo));
	        
        Emprestimo emprestimoReturn = emprestimoService.procurarEmprestimoPorId(1);

        assertNotNull(emprestimoReturn);
    }
    
    @Test
    void deveListarEmprestimosComSucesso() {
        
        Emprestimo emprestimo = new Emprestimo();
		
		when(emprestimoRepository.findAll())
		        .thenReturn(List.of(emprestimo));
	        
        List<Emprestimo> emprestimos = emprestimoService.listarEmprestimos();

        assertNotNull(emprestimos);
    }
}
