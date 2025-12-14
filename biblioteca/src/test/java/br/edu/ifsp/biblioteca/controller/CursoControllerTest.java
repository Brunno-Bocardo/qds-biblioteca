package br.edu.ifsp.biblioteca.controller;

import br.edu.ifsp.biblioteca.dto.CursoCreateDto;
import br.edu.ifsp.biblioteca.model.Curso;
import br.edu.ifsp.biblioteca.service.CursoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CursoControllerTest {

    private CursoService cursoService;
    private CursoController cursoController;

    @BeforeEach
    void setUp() {
        cursoService = Mockito.mock(CursoService.class);
        cursoController = new CursoController(cursoService);
    }

    @Test
    void testCriarCursoSucesso() {
        CursoCreateDto inputDto = new CursoCreateDto("ADS");
        Curso cursoCriado = new Curso();
        cursoCriado.setIdCurso(1);
        cursoCriado.setNomeCurso("ADS");

        Mockito.when(cursoService.criarCurso("ADS")).thenReturn(cursoCriado);

        ResponseEntity<Curso> response = cursoController.criarCurso(inputDto);

        Mockito.verify(cursoService).criarCurso("ADS");
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(cursoCriado, response.getBody());
    }

    @Test
    void testExibirCursos() {
        Curso curso1 = new Curso();
        curso1.setIdCurso(1);
        curso1.setNomeCurso("ADS");

        Curso curso2 = new Curso();
        curso2.setIdCurso(2);
        curso2.setNomeCurso("Astronomia");

        Mockito.when(cursoService.listarCursos()).thenReturn(List.of(curso1, curso2));

        ResponseEntity<List<Curso>> response = cursoController.exibirCursos();

        Mockito.verify(cursoService).listarCursos();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("ADS", response.getBody().get(0).getNomeCurso());
        assertEquals("Astronomia", response.getBody().get(1).getNomeCurso());
    }

    @Test
    void testExibirCursoPorId() {
        Curso curso = new Curso();
        curso.setIdCurso(5);
        curso.setNomeCurso("Engenharia de Alimentos");

        Mockito.when(cursoService.procurarCursoPorId(5)).thenReturn(curso);

        ResponseEntity<Curso> response = cursoController.exibirCursoPorId(5);

        Mockito.verify(cursoService).procurarCursoPorId(5);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(5, response.getBody().getIdCurso());
        assertEquals("Engenharia de Alimentos", response.getBody().getNomeCurso());
    }

    @Test
    void testAtualizarCurso() {
        Curso cursoAtualizado = new Curso();
        cursoAtualizado.setIdCurso(3);
        cursoAtualizado.setNomeCurso("Pedagogia");

        CursoCreateDto inputDto = new CursoCreateDto("Pedagogia");

        Mockito.when(cursoService.atualizarCursoPorId(3, "Pedagogia")).thenReturn(cursoAtualizado);

        ResponseEntity<Curso> response = cursoController.atualizarCurso(3, inputDto);

        Mockito.verify(cursoService).atualizarCursoPorId(3, "Pedagogia");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(cursoAtualizado, response.getBody());
    }

    @Test
    void testDeletarCurso() {
        ResponseEntity<Map<String, String>> response = cursoController.deletarCurso(7);

        Mockito.verify(cursoService).deletarCursoPorId(7);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Curso deletado!", response.getBody().get("message"));
    }
}
