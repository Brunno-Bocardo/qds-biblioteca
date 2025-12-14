package br.edu.ifsp.biblioteca.controller;

import br.edu.ifsp.biblioteca.dto.UsuarioCreateDto;
import br.edu.ifsp.biblioteca.model.Usuario;
import br.edu.ifsp.biblioteca.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

import java.util.List;
import java.util.Map;

public class UsuarioControllerTest {

    private UsuarioService usuarioService;
    private UsuarioController usuarioController;

    @BeforeEach
    void setUp() {
        usuarioService = Mockito.mock(UsuarioService.class);
        usuarioController = new UsuarioController(usuarioService);
    }

    @Test
    void testCriarUsuarioSucesso() {
        UsuarioCreateDto inputDto = new UsuarioCreateDto();
        Usuario usuarioSalvo = new Usuario();
        Mockito.when(usuarioService.criarUsuario(any(UsuarioCreateDto.class))).thenReturn(usuarioSalvo);
        ResponseEntity<Usuario> response = usuarioController.criarUsuario(inputDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(usuarioSalvo, response.getBody());
    }

    @Test
    void testExibirUsuariosSucesso() {
        Mockito.when(usuarioService.listarUsuarios()).thenReturn(List.of(new Usuario(), new Usuario()));
        ResponseEntity<List<Usuario>> response = usuarioController.exibirUsuarios();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void testDeletarUsuarioSucesso() {
        String cpf = "12345678900";
        ResponseEntity<Map<String, String>> response = usuarioController.deletarUsuario(cpf);

        Mockito.verify(usuarioService).deletarUsuario(cpf);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Usuário deletado!", response.getBody().get("message"));
    }

    @Test
    void testExibirUsuarioPorCpfSucesso() {
        String cpf = "12345678900";
        Usuario usuario = new Usuario();
        Mockito.when(usuarioService.procurarPorCpf(cpf)).thenReturn(usuario);
        ResponseEntity<Usuario> response = usuarioController.exibirUsuarioPorCpf(cpf);

        Mockito.verify(usuarioService).procurarPorCpf(cpf);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(usuario, response.getBody());
    }

    @Test
    void testAtualizarUsuarioSucesso() {
        String cpf = "12345678900";
        UsuarioCreateDto inputDto = new UsuarioCreateDto();
        Usuario usuarioAtualizado = new Usuario();
        Mockito.when(usuarioService.atualizarUsuario(cpf, inputDto)).thenReturn(usuarioAtualizado);
        ResponseEntity<Usuario> response = usuarioController.atualizarUsuario(cpf, inputDto);

        Mockito.verify(usuarioService).atualizarUsuario(cpf, inputDto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(usuarioAtualizado, response.getBody());
    }
}
