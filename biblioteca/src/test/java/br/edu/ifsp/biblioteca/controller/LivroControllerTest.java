package br.edu.ifsp.biblioteca.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq; // Importante para o mock do atualizar

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.edu.ifsp.biblioteca.dto.LivroCreateDto;
import br.edu.ifsp.biblioteca.model.Livro;
import br.edu.ifsp.biblioteca.service.LivroService;

public class LivroControllerTest {
    private LivroController livroController;
    private LivroService livroService;

    @BeforeEach
    void setUp() {
        livroService = Mockito.mock(LivroService.class);
        livroController = new LivroController(livroService);
    }

    @Test
    void testCriarLivroSucesso() {
        LivroCreateDto inputDto = new LivroCreateDto();
        Livro livroSalvo = new Livro();
        Mockito.when(livroService.cadastrarLivro(any(LivroCreateDto.class))).thenReturn(livroSalvo);
        ResponseEntity<Livro> response = livroController.criar(inputDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(livroSalvo, response.getBody());
    }

    @Test
    void testListarLivrosSucesso() {
        Mockito.when(livroService.listarLivros()).thenReturn(List.of(new Livro(), new Livro()));
        ResponseEntity<List<Livro>> response = livroController.exibirTodos();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void testBuscarPorIsbnSucesso() {
        String isbn = "123456";
        Livro livro = new Livro();
        Mockito.when(livroService.procurarPorIsbn(isbn)).thenReturn(livro);
        ResponseEntity<Livro> response = livroController.buscarPorIsbn(isbn);

        Mockito.verify(livroService).procurarPorIsbn(isbn);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(livro, response.getBody());
    }

    @Test
    void testAtualizarLivroSucesso() {
        String isbn = "123456";
        LivroCreateDto inputDto = new LivroCreateDto();
        Livro livroAtualizado = new Livro();
        Mockito.when(livroService.atualizarLivro(eq(isbn), any(LivroCreateDto.class))).thenReturn(livroAtualizado);
        ResponseEntity<Livro> response = livroController.atualizarLivro(isbn, inputDto);
        Mockito.verify(livroService).atualizarLivro(eq(isbn), any(LivroCreateDto.class)); // Verifique com os mesmos
                                                                                          // matchers

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(livroAtualizado, response.getBody());
    }

    @Test
    void testDeletarLivroSucesso() {
        String isbn = "123456";
        ResponseEntity<Map<String, String>> response = livroController.deletarLivro(isbn);
        Mockito.verify(livroService).deletar(isbn);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Livro deletado!", response.getBody().get("message"));
    }
}