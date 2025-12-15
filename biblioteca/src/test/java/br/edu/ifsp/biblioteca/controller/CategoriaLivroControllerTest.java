package br.edu.ifsp.biblioteca.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.edu.ifsp.biblioteca.dto.CategoriaLivroDto;
import br.edu.ifsp.biblioteca.model.CategoriaLivro;
import br.edu.ifsp.biblioteca.service.CategoriaLivroService;

public class CategoriaLivroControllerTest {

    private CategoriaLivroService categoriaLivroService;
    private CategoriaLivroController categoriaLivroController;

    @BeforeEach
    void setUp() {
        categoriaLivroService = Mockito.mock(CategoriaLivroService.class);
        categoriaLivroController = new CategoriaLivroController(categoriaLivroService);
    }

    @Test
    void testCriarCategoriaLivroSucesso() {

        String nomeCategoria = "Ficção Científica";
        CategoriaLivro categoriaSalva = new CategoriaLivro(1, nomeCategoria);
        Mockito.when(categoriaLivroService.cadastrar(any(CategoriaLivro.class))).thenReturn(categoriaSalva);
        ResponseEntity<CategoriaLivroDto> response = categoriaLivroController.criar(nomeCategoria);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(categoriaSalva.getIdCategoriaLivro(), response.getBody().getIdCategoriaLivro());
        assertEquals(categoriaSalva.getNomeCategoriaLivro(), response.getBody().getNomeCategoriaLivro());
    }

    @Test
    void testListarCategoriasLivrosSucesso() {

        Mockito.when(categoriaLivroService.listarTodas())
                .thenReturn(List.of(new CategoriaLivro(1, "Terror"), new CategoriaLivro(2, "Romance")));
        ResponseEntity<List<CategoriaLivroDto>> response = categoriaLivroController.listarTodos();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("Terror", response.getBody().get(0).getNomeCategoriaLivro());
    }

    @Test
    void testConsultarCategoriaLivroSucesso() {
        String nomeBusca = "Aventura";
        CategoriaLivro categoriaEncontrada = new CategoriaLivro(3, "Aventura");
        Mockito.when(categoriaLivroService.consultarPorNome(nomeBusca)).thenReturn(categoriaEncontrada);
        ResponseEntity<CategoriaLivroDto> response = categoriaLivroController.consultarPorNome(nomeBusca);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(categoriaEncontrada.getIdCategoriaLivro(), response.getBody().getIdCategoriaLivro());
        assertEquals(categoriaEncontrada.getNomeCategoriaLivro(), response.getBody().getNomeCategoriaLivro());
    }

    @Test
    void deveTestarConstrutorVazioESetters() {
        CategoriaLivro categoria = new CategoriaLivro();
        categoria.setIdCategoriaLivro(10);
        categoria.setNomeCategoriaLivro("Fantasia");

        assertEquals(10, categoria.getIdCategoriaLivro());
        assertEquals("Fantasia", categoria.getNomeCategoriaLivro());
    }
}