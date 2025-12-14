package br.edu.ifsp.biblioteca.controller;

import br.edu.ifsp.biblioteca.controller.CategoriaUsuarioController.CategoriaTipo;
import br.edu.ifsp.biblioteca.dto.CategoriaUsuarioDto;
import br.edu.ifsp.biblioteca.model.CategoriaUsuario;
import br.edu.ifsp.biblioteca.service.CategoriaUsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CategoriaUsuarioControllerTest {

    private CategoriaUsuarioService categoriaUsuarioService;
    private CategoriaUsuarioController categoriaUsuarioController;

    @BeforeEach
    void setUp() {
        categoriaUsuarioService = Mockito.mock(CategoriaUsuarioService.class);
        categoriaUsuarioController = new CategoriaUsuarioController(categoriaUsuarioService);
    }

    @Test
    void testCriarCategoriaUsuarioSucesso() {
        CategoriaUsuario categoriaSalva = new CategoriaUsuario(1, "aluno");
        Mockito.when(categoriaUsuarioService.cadastrarCategoriaUsuario(Mockito.any(CategoriaUsuario.class))).thenReturn(categoriaSalva);
        ResponseEntity<CategoriaUsuarioDto> response = categoriaUsuarioController.criarCategoriaUsuario(CategoriaTipo.aluno);

        ArgumentCaptor<CategoriaUsuario> captor = ArgumentCaptor.forClass(CategoriaUsuario.class);
        Mockito.verify(categoriaUsuarioService).cadastrarCategoriaUsuario(captor.capture());
        assertEquals("aluno", captor.getValue().getNomeCategoriaUsuario());

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getIdCategoriaUsuario());
        assertEquals("aluno", response.getBody().getNomeCategoriaUsuarioo());
    }

    @Test
    void testListarTodos() {
        List<CategoriaUsuario> categorias = List.of(
            new CategoriaUsuario(1, "aluno"),
            new CategoriaUsuario(2, "professor")
        );
        Mockito.when(categoriaUsuarioService.listarTodas()).thenReturn(categorias);

        ResponseEntity<List<CategoriaUsuarioDto>> response = categoriaUsuarioController.listarTodos();

        Mockito.verify(categoriaUsuarioService).listarTodas();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("aluno", response.getBody().get(0).getNomeCategoriaUsuarioo());
        assertEquals("professor", response.getBody().get(1).getNomeCategoriaUsuarioo());
    }

    @Test
    void testConsultarPorNome() {
        CategoriaUsuario categoria = new CategoriaUsuario(5, "aluno");
        Mockito.when(categoriaUsuarioService.consultarPorNome("aluno")).thenReturn(categoria);

        ResponseEntity<CategoriaUsuarioDto> response = categoriaUsuarioController.consultarPorNome("aluno");

        Mockito.verify(categoriaUsuarioService).consultarPorNome("aluno");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(5, response.getBody().getIdCategoriaUsuario());
        assertEquals("aluno", response.getBody().getNomeCategoriaUsuarioo());
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> testes-unitarios
