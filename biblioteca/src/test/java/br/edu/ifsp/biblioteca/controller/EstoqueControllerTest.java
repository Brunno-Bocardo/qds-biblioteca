package br.edu.ifsp.biblioteca.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.edu.ifsp.biblioteca.dto.EstoqueCreateDto;
import br.edu.ifsp.biblioteca.dto.EstoqueDisponibilidadeDto;
import br.edu.ifsp.biblioteca.model.Estoque;
import br.edu.ifsp.biblioteca.service.EstoqueService;

class EstoqueControllerTest {

    private EstoqueService estoqueService;
    private EstoqueController estoqueController;

    @BeforeEach
    void setUp() {
        estoqueService = Mockito.mock(EstoqueService.class);
        estoqueController = new EstoqueController(estoqueService);
    }

    @Test
    void testCadastrarExemplarSucesso() {
        EstoqueCreateDto dto = new EstoqueCreateDto("123456", "EX-001");
        Estoque estoqueSalvo = new Estoque();
        estoqueSalvo.setCodigoExemplar("EX-001");

        when(estoqueService.cadastrarExemplar(any(EstoqueCreateDto.class))).thenReturn(estoqueSalvo);

        ResponseEntity<Estoque> response = estoqueController.cadastrarExemplar(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("EX-001", response.getBody().getCodigoExemplar());
        verify(estoqueService).cadastrarExemplar(dto);
    }

    @Test
    void testListarExemplares() {
        when(estoqueService.listarExemplares()).thenReturn(List.of(new Estoque(), new Estoque()));

        ResponseEntity<List<Estoque>> response = estoqueController.listarExemplares();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(estoqueService).listarExemplares();
    }

    @Test
    void testBuscarPorCodigo() {
        Estoque estoque = new Estoque();
        estoque.setCodigoExemplar("EX-001");

        when(estoqueService.buscarPorCodigo("EX-001")).thenReturn(estoque);

        ResponseEntity<Estoque> response = estoqueController.buscarPorCodigo("EX-001");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("EX-001", response.getBody().getCodigoExemplar());
    }

    @Test
    void testAtualizarDisponibilidade() {
        String codigo = "EX-001";
        EstoqueDisponibilidadeDto dto = new EstoqueDisponibilidadeDto(false);
        Estoque estoqueAtualizado = new Estoque();
        estoqueAtualizado.setCodigoExemplar(codigo);
        estoqueAtualizado.setDisponivel(false);

        when(estoqueService.atualizarDisponibilidade(eq(codigo), eq(false))).thenReturn(estoqueAtualizado);

        ResponseEntity<Estoque> response = estoqueController.atualizarDisponibilidade(codigo, dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isDisponivel());
        verify(estoqueService).atualizarDisponibilidade(codigo, false);
    }

    @Test
    void testRemoverExemplar() {
        String codigo = "EX-001";
        
        doNothing().when(estoqueService).removerExemplar(codigo);

        ResponseEntity<Void> response = estoqueController.removerExemplar(codigo);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(estoqueService).removerExemplar(codigo);
    }
}