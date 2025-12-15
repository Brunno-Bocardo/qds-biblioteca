package br.edu.ifsp.biblioteca.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import br.edu.ifsp.biblioteca.model.CategoriaLivro;
import br.edu.ifsp.biblioteca.service.CategoriaLivroService;
import br.edu.ifsp.biblioteca.model.CategoriaLivro;

import br.edu.ifsp.biblioteca.repository.CategoriaLivroRepository;

public class CategoriaLivroServiceTest {
    private CategoriaLivroRepository categoriaLivroRepository;
    private CategoriaLivroService categoriaLivroService;

    @BeforeEach
    void setUp() {
        categoriaLivroRepository = Mockito.mock(CategoriaLivroRepository.class);
        categoriaLivroService = new CategoriaLivroService(categoriaLivroRepository);
    }

    @Test
    void testCadastrarCategoriaLivroSucesso() {
        CategoriaLivro categoria = new CategoriaLivro(null, "Terror");
        CategoriaLivro salvo = new CategoriaLivro(1, "Terror");

        Mockito.when(categoriaLivroRepository.existsByNomeCategoriaLivro("Terror")).thenReturn(false);
        Mockito.when(categoriaLivroRepository.save(any(CategoriaLivro.class))).thenReturn(salvo);

        CategoriaLivro resultado = categoriaLivroService.cadastrar(categoria);

        assertNotNull(resultado);
        assertEquals(1, resultado.getIdCategoriaLivro());
        assertEquals("Terror", resultado.getNomeCategoriaLivro());
    }

    @Test
    void testCadastrarCategoriaLivroDuplicado() {
        CategoriaLivro categoria = new CategoriaLivro(null, "JavaScrip");
        Mockito.when(categoriaLivroRepository.existsByNomeCategoriaLivro("JavaScrip")).thenReturn(true);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> categoriaLivroService.cadastrar(categoria));
        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertNotNull(exception.getReason());
        assertTrue(exception.getReason().toLowerCase().contains("categoria"));
    }

    @Test
    void testConsultarCategoriaPorNomeSucesso() {
        CategoriaLivro categoria = new CategoriaLivro(1, "ADS");
        Mockito.when(categoriaLivroRepository.findByNomeCategoriaLivro("ADS")).thenReturn(Optional.of(categoria));
        CategoriaLivro resultado = categoriaLivroService.consultarPorNome("ADS");
        assertEquals(categoria, resultado);
    }

    @Test
    void testConsultarCategoriaPorNomeNaoEncontrado() {
        Mockito.when(categoriaLivroRepository.findByNomeCategoriaLivro("Engenharia de Software"))
                .thenReturn(Optional.empty());
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> categoriaLivroService.consultarPorNome("Engenharia de Software"));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertNotNull(exception.getReason());
        assertTrue(exception.getReason().toLowerCase().contains("categoria"));
    }

    @Test
    void testConsultarPorIdSucesso() {
        CategoriaLivro categoria = new CategoriaLivro(1, "ADS");
        Mockito.when(categoriaLivroRepository.findById(1)).thenReturn(Optional.of(categoria));
        CategoriaLivro resultado = categoriaLivroService.consultarPorId(1);
        assertEquals(categoria, resultado);
    }

    @Test
    void testConsultarPorIdNaoEncontrado() {
        Mockito.when(categoriaLivroRepository.findById(1)).thenReturn(Optional.empty());
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> categoriaLivroService.consultarPorId(1));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertNotNull(exception.getReason());
        assertTrue(exception.getReason().toLowerCase().contains("categoria"));
    }

    @Test
    void testListarTodasSucesso() {
        CategoriaLivro categoria1 = new CategoriaLivro(1, "ADS");
        CategoriaLivro categoria2 = new CategoriaLivro(2, "Engenharia de Software");
        Mockito.when(categoriaLivroRepository.findAll()).thenReturn(List.of(categoria1, categoria2));
        List<CategoriaLivro> categorias = categoriaLivroService.listarTodas();
        assertEquals(2, categorias.size());
        assertTrue(categorias.contains(categoria1));
        assertTrue(categorias.contains(categoria2));
    }
}
