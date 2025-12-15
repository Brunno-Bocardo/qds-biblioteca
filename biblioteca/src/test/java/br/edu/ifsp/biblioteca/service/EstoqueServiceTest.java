package br.edu.ifsp.biblioteca.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import br.edu.ifsp.biblioteca.builder.EstoqueValidationChainBuilder;
import br.edu.ifsp.biblioteca.dto.EstoqueCreateDto;
import br.edu.ifsp.biblioteca.handler.ValidationHandler;
import br.edu.ifsp.biblioteca.model.Estoque;
import br.edu.ifsp.biblioteca.model.Livro;
import br.edu.ifsp.biblioteca.repository.EstoqueRepository;
import br.edu.ifsp.biblioteca.repository.LivroRepository;

class EstoqueServiceTest {

    private EstoqueRepository estoqueRepository;
    private LivroRepository livroRepository;
    private EstoqueValidationChainBuilder estoqueValidation;
    private ValidationHandler<EstoqueCreateDto> validationHandler;
    private EstoqueService estoqueService;

    @SuppressWarnings("unchecked")
    @BeforeEach
    void setUp() {
        estoqueRepository = Mockito.mock(EstoqueRepository.class);
        livroRepository = Mockito.mock(LivroRepository.class);
        estoqueValidation = Mockito.mock(EstoqueValidationChainBuilder.class);
        validationHandler = Mockito.mock(ValidationHandler.class);

        when(estoqueValidation.buildEstoqueChain()).thenReturn(validationHandler);
        doNothing().when(validationHandler).handle(any());

        estoqueService = new EstoqueService(estoqueRepository, livroRepository, estoqueValidation);
    }

    @Test
    void testCadastrarExemplarSucesso() {
        EstoqueCreateDto dto = new EstoqueCreateDto("123456", "EX-001");
        Livro livro = new Livro();
        livro.setIsbn("123456");

        Estoque estoqueSalvo = new Estoque();
        estoqueSalvo.setCodigoExemplar("EX-001");
        estoqueSalvo.setLivro(livro);
        estoqueSalvo.setDisponivel(true);

        when(livroRepository.findByIsbn("123456")).thenReturn(Optional.of(livro));
        when(estoqueRepository.findByCodigoExemplar("EX-001")).thenReturn(Optional.empty());
        when(estoqueRepository.save(any(Estoque.class))).thenReturn(estoqueSalvo);

        Estoque resultado = estoqueService.cadastrarExemplar(dto);

        assertNotNull(resultado);
        assertEquals("EX-001", resultado.getCodigoExemplar());
        assertTrue(resultado.isDisponivel());
        verify(validationHandler).handle(dto);
        verify(estoqueRepository).save(any(Estoque.class));
    }

    @Test
    void testCadastrarExemplarLivroNaoEncontrado() {
        EstoqueCreateDto dto = new EstoqueCreateDto("999999", "EX-002");

        when(livroRepository.findByIsbn("999999")).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> 
            estoqueService.cadastrarExemplar(dto)
        );

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertEquals("Livro não encontrado para o ISBN informado", ex.getReason());
    }

    @Test
    void testCadastrarExemplarCodigoDuplicado() {
        EstoqueCreateDto dto = new EstoqueCreateDto("123456", "EX-001");
        Livro livro = new Livro();

        when(livroRepository.findByIsbn("123456")).thenReturn(Optional.of(livro));
        when(estoqueRepository.findByCodigoExemplar("EX-001")).thenReturn(Optional.of(new Estoque()));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> 
            estoqueService.cadastrarExemplar(dto)
        );

        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
        assertEquals("Já existe um exemplar com este código", ex.getReason());
    }

    @Test
    void testListarExemplares() {
        when(estoqueRepository.findAll()).thenReturn(List.of(new Estoque(), new Estoque()));

        List<Estoque> lista = estoqueService.listarExemplares();

        assertEquals(2, lista.size());
        verify(estoqueRepository).findAll();
    }

    @Test
    void testBuscarPorCodigoSucesso() {
        Estoque estoque = new Estoque();
        estoque.setCodigoExemplar("EX-001");

        when(estoqueRepository.findByCodigoExemplar("EX-001")).thenReturn(Optional.of(estoque));

        Estoque resultado = estoqueService.buscarPorCodigo("EX-001");

        assertEquals(estoque, resultado);
    }

    @Test
    void testBuscarPorCodigoNaoEncontrado() {
        when(estoqueRepository.findByCodigoExemplar("INEXISTENTE")).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> 
            estoqueService.buscarPorCodigo("INEXISTENTE")
        );

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }

    @Test
    void testAtualizarDisponibilidadeSucesso() {
        Estoque estoque = new Estoque();
        estoque.setCodigoExemplar("EX-001");
        estoque.setDisponivel(true);

        when(estoqueRepository.findByCodigoExemplar("EX-001")).thenReturn(Optional.of(estoque));
        when(estoqueRepository.save(any(Estoque.class))).thenAnswer(i -> i.getArguments()[0]);

        Estoque resultado = estoqueService.atualizarDisponibilidade("EX-001", false);

        assertFalse(resultado.isDisponivel());
        verify(estoqueRepository).save(estoque);
    }

    @Test
    void testAtualizarDisponibilidadeNaoEncontrado() {
        when(estoqueRepository.findByCodigoExemplar("INEXISTENTE")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> 
            estoqueService.atualizarDisponibilidade("INEXISTENTE", false)
        );

        assertEquals("Exemplar não encontrado", ex.getMessage());
    }

    @Test
    void testRemoverExemplarSucesso() {
        Estoque estoque = new Estoque();
        estoque.setCodigoExemplar("EX-001");
        estoque.setDisponivel(true);

        when(estoqueRepository.findByCodigoExemplar("EX-001")).thenReturn(Optional.of(estoque));

        estoqueService.removerExemplar("EX-001");

        verify(estoqueRepository).delete(estoque);
    }

    @Test
    void testRemoverExemplarNaoEncontrado() {
        when(estoqueRepository.findByCodigoExemplar("INEXISTENTE")).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> 
            estoqueService.removerExemplar("INEXISTENTE")
        );

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }

    @Test
    void testRemoverExemplarEmprestado() {
        Estoque estoque = new Estoque();
        estoque.setCodigoExemplar("EX-001");
        estoque.setDisponivel(false); // Emprestado

        when(estoqueRepository.findByCodigoExemplar("EX-001")).thenReturn(Optional.of(estoque));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> 
            estoqueService.removerExemplar("EX-001")
        );

        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
        assertEquals("Exemplar não pode ser removido pois está emprestado", ex.getReason());
        verify(estoqueRepository, never()).delete(any());
    }
}