package br.edu.ifsp.biblioteca.service;

import br.edu.ifsp.biblioteca.builder.CursoValidationChainBuilder;
import br.edu.ifsp.biblioteca.handler.ValidationHandler;
import br.edu.ifsp.biblioteca.model.Curso;
import br.edu.ifsp.biblioteca.repository.CursoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class CursoServiceTest {

    private CursoRepository cursoRepository;
    private UsuarioService usuarioService;
    private CursoValidationChainBuilder cursoValidation;
    @SuppressWarnings("unchecked")
    private ValidationHandler<String> nomeValidation;
    private CursoService cursoService;

    @BeforeEach
    void setUp() {
        cursoRepository = Mockito.mock(CursoRepository.class);
        usuarioService = Mockito.mock(UsuarioService.class);
        cursoValidation = Mockito.mock(CursoValidationChainBuilder.class);
        nomeValidation = Mockito.mock(ValidationHandler.class);

        Mockito.when(cursoValidation.buildNomeChain()).thenReturn(nomeValidation);
        Mockito.doNothing().when(nomeValidation).handle(any(String.class));

        cursoService = new CursoService(cursoRepository, usuarioService, cursoValidation);
    }

    @Test
    void testCriarCursoSucesso() {
        String nome = "  Engenharia ";
        Mockito.when(cursoRepository.existsByNomeCursoIgnoreCase("  Engenharia ")).thenReturn(false);
        Mockito.when(cursoRepository.save(any(Curso.class))).thenAnswer(invocation -> {
            Curso curso = invocation.getArgument(0);
            curso.setIdCurso(1);
            return curso;
        });

        Curso resultado = cursoService.criarCurso(nome);

        Mockito.verify(nomeValidation).handle(nome);
        Mockito.verify(cursoRepository).save(any(Curso.class));
        assertNotNull(resultado);
        assertEquals(1, resultado.getIdCurso());
        assertEquals("Engenharia", resultado.getNomeCurso());
    }

    @Test
    void testCriarCursoDuplicado() {
        Mockito.when(cursoRepository.existsByNomeCursoIgnoreCase("Direito")).thenReturn(true);

        ResponseStatusException excecao = assertThrows(ResponseStatusException.class, () -> cursoService.criarCurso("Direito"));

        assertEquals(HttpStatus.CONFLICT, excecao.getStatusCode());
        assertNotNull(excecao.getReason());
        assertTrue(excecao.getReason().toLowerCase().contains("curso"));
        Mockito.verify(nomeValidation).handle("Direito");
        Mockito.verify(cursoRepository, Mockito.never()).save(any(Curso.class));
    }

    @Test
    void testListarCursos() {
        Curso curso1 = new Curso();
        Curso curso2 = new Curso();
        Mockito.when(cursoRepository.findAll()).thenReturn(List.of(curso1, curso2));

        List<Curso> cursos = cursoService.listarCursos();

        Mockito.verify(cursoRepository).findAll();
        assertEquals(2, cursos.size());
        assertTrue(cursos.contains(curso1));
        assertTrue(cursos.contains(curso2));
    }

    @Test
    void testProcurarCursoPorIdSucesso() {
        Curso curso = new Curso();
        Mockito.when(cursoRepository.findById(10)).thenReturn(Optional.of(curso));

        Curso resultado = cursoService.procurarCursoPorId(10);

        Mockito.verify(cursoRepository).findById(10);
        assertEquals(curso, resultado);
    }

    @Test
    void testProcurarCursoPorIdNaoEncontrado() {
        Mockito.when(cursoRepository.findById(55)).thenReturn(Optional.empty());

        ResponseStatusException excecao = assertThrows(ResponseStatusException.class, () -> cursoService.procurarCursoPorId(55));

        assertEquals(HttpStatus.NOT_FOUND, excecao.getStatusCode());
        assertNotNull(excecao.getReason());
        assertTrue(excecao.getReason().toLowerCase().contains("curso"));
    }

    @Test
    void testAtualizarCursoPorIdSucesso() {
        Curso cursoAtual = new Curso();
        cursoAtual.setIdCurso(3);
        cursoAtual.setNomeCurso("Antigo Nome");

        Mockito.when(cursoRepository.findById(3)).thenReturn(Optional.of(cursoAtual));
        Mockito.when(cursoRepository.existsByNomeCursoIgnoreCaseAndIdCursoNot("Historia", 3)).thenReturn(false);
        Mockito.when(cursoRepository.save(any(Curso.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Curso resultado = cursoService.atualizarCursoPorId(3, " Historia ");

        Mockito.verify(nomeValidation).handle(" Historia ");
        Mockito.verify(cursoRepository).save(any(Curso.class));
        assertEquals(3, resultado.getIdCurso());
        assertEquals("Historia", resultado.getNomeCurso());
    }

    @Test
    void testAtualizarCursoPorIdDuplicado() {
        Curso cursoAtual = new Curso();
        cursoAtual.setIdCurso(4);
        cursoAtual.setNomeCurso("Antigo");

        Mockito.when(cursoRepository.findById(4)).thenReturn(Optional.of(cursoAtual));
        Mockito.when(cursoRepository.existsByNomeCursoIgnoreCaseAndIdCursoNot("Novo", 4)).thenReturn(true);

        ResponseStatusException excecao = assertThrows(ResponseStatusException.class, () -> cursoService.atualizarCursoPorId(4, "Novo"));

        assertEquals(HttpStatus.CONFLICT, excecao.getStatusCode());
        assertNotNull(excecao.getReason());
        assertTrue(excecao.getReason().toLowerCase().contains("curso"));
        Mockito.verify(cursoRepository, Mockito.never()).save(any(Curso.class));
    }

    @Test
    void testDeletarCursoSucesso() {
        Curso curso = new Curso();
        Mockito.when(cursoRepository.findById(2)).thenReturn(Optional.of(curso));
        Mockito.when(usuarioService.consultarPorCurso(curso)).thenReturn(false);

        cursoService.deletarCursoPorId(2);

        Mockito.verify(usuarioService).consultarPorCurso(curso);
        Mockito.verify(cursoRepository).delete(curso);
    }

    @Test
    void testDeletarCursoEmUso() {
        Curso curso = new Curso();
        Mockito.when(cursoRepository.findById(7)).thenReturn(Optional.of(curso));
        Mockito.when(usuarioService.consultarPorCurso(curso)).thenReturn(true);

        ResponseStatusException excecao = assertThrows(ResponseStatusException.class, () -> cursoService.deletarCursoPorId(7));

        assertEquals(HttpStatus.BAD_REQUEST, excecao.getStatusCode());
        assertNotNull(excecao.getReason());
        assertTrue(excecao.getReason().contains("Curso em Uso"));
        Mockito.verify(cursoRepository, Mockito.never()).delete(any(Curso.class));
    }
}
