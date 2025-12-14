package br.edu.ifsp.biblioteca.service;

import br.edu.ifsp.biblioteca.model.CategoriaUsuario;
import br.edu.ifsp.biblioteca.repository.CategoriaUsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class CategoriaUsuarioServiceTest {

    private CategoriaUsuarioRepository categoriaUsuarioRepository;
    private CategoriaUsuarioService categoriaUsuarioService;

    @BeforeEach
    void setUp() {
        categoriaUsuarioRepository = Mockito.mock(CategoriaUsuarioRepository.class);
        categoriaUsuarioService = new CategoriaUsuarioService(categoriaUsuarioRepository);
    }

    @Test
    void testCadastrarCategoriaUsuarioSucesso() {
        CategoriaUsuario categoria = new CategoriaUsuario(null, "aluno");
        CategoriaUsuario salvo = new CategoriaUsuario(1, "aluno");

        Mockito.when(categoriaUsuarioRepository.existsByNomeCategoriaUsuario("aluno")).thenReturn(false);
        Mockito.when(categoriaUsuarioRepository.save(any(CategoriaUsuario.class))).thenReturn(salvo);

        CategoriaUsuario resultado = categoriaUsuarioService.cadastrarCategoriaUsuario(categoria);

        assertNotNull(resultado);
        assertEquals(1, resultado.getIdCategoriaUsuario());
        assertEquals("aluno", resultado.getNomeCategoriaUsuario());
    }

    @Test
    void testCadastrarCategoriaUsuarioDuplicado() {
        CategoriaUsuario categoria = new CategoriaUsuario(null, "aluno");
        Mockito.when(categoriaUsuarioRepository.existsByNomeCategoriaUsuario("aluno")).thenReturn(true);
        ResponseStatusException excecao = assertThrows(ResponseStatusException.class, () -> categoriaUsuarioService.cadastrarCategoriaUsuario(categoria));
        assertEquals(HttpStatus.CONFLICT, excecao.getStatusCode());
        assertNotNull(excecao.getReason());
        assertTrue(excecao.getReason().toLowerCase().contains("categoria"));
    }

    @Test
    void testConsultarPorNomeSucesso() {
        CategoriaUsuario categoria = new CategoriaUsuario(1, "professor");
        Mockito.when(categoriaUsuarioRepository.findByNomeCategoriaUsuario("professor")).thenReturn(Optional.of(categoria));
        CategoriaUsuario resultado = categoriaUsuarioService.consultarPorNome("professor");
        assertEquals(categoria, resultado);
    }

    @Test
    void testConsultarPorNomeNaoEncontrado() {
        Mockito.when(categoriaUsuarioRepository.findByNomeCategoriaUsuario("inexistente")).thenReturn(Optional.empty());
        ResponseStatusException excecao = assertThrows(ResponseStatusException.class, () -> categoriaUsuarioService.consultarPorNome("inexistente"));
        assertEquals(HttpStatus.NOT_FOUND, excecao.getStatusCode());
        assertNotNull(excecao.getReason());
        assertTrue(excecao.getReason().toLowerCase().contains("categoria"));
    }

    @Test
    void testConsultarPorIdSucesso() {
        CategoriaUsuario categoria = new CategoriaUsuario(2, "aluno");
        Mockito.when(categoriaUsuarioRepository.findById(2)).thenReturn(Optional.of(categoria));
        CategoriaUsuario resultado = categoriaUsuarioService.consultarPorId(2);
        assertEquals(categoria, resultado);
    }

    @Test
    void testConsultarPorIdNaoEncontrado() {
        Mockito.when(categoriaUsuarioRepository.findById(99)).thenReturn(Optional.empty());
        ResponseStatusException excecao = assertThrows(ResponseStatusException.class, () -> categoriaUsuarioService.consultarPorId(99));
        assertEquals(HttpStatus.NOT_FOUND, excecao.getStatusCode());
        assertNotNull(excecao.getReason());
        assertTrue(excecao.getReason().toLowerCase().contains("categoria"));
    }

    @Test
    void testListarTodas() {
        CategoriaUsuario categoria1 = new CategoriaUsuario(1, "aluno");
        CategoriaUsuario categoria2 = new CategoriaUsuario(2, "professor");
        Mockito.when(categoriaUsuarioRepository.findAll()).thenReturn(List.of(categoria1, categoria2));
        List<CategoriaUsuario> categorias = categoriaUsuarioService.listarTodas();
        assertEquals(2, categorias.size());
        assertTrue(categorias.contains(categoria1));
        assertTrue(categorias.contains(categoria2));
    }
}
