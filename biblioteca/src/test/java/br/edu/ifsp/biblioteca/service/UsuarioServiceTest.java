package br.edu.ifsp.biblioteca.service;

import br.edu.ifsp.biblioteca.builder.UsuarioValidationChainBuilder;
import br.edu.ifsp.biblioteca.dto.UsuarioCreateDto;
import br.edu.ifsp.biblioteca.handler.ValidationHandler;
import br.edu.ifsp.biblioteca.model.CategoriaUsuario;
import br.edu.ifsp.biblioteca.model.Curso;
import br.edu.ifsp.biblioteca.model.Usuario;
import br.edu.ifsp.biblioteca.repository.CursoRepository;
import br.edu.ifsp.biblioteca.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

public class UsuarioServiceTest {

    private UsuarioRepository usuarioRepository;
    private CategoriaUsuarioService categoriaService;
    private CursoRepository cursoRepository;
    private UsuarioValidationChainBuilder usuarioValidation;
    private ValidationHandler<UsuarioCreateDto> usuarioValidationChain;
    private ValidationHandler<String> cpfValidationChain;
    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        usuarioRepository = Mockito.mock(UsuarioRepository.class);
        categoriaService = Mockito.mock(CategoriaUsuarioService.class);
        cursoRepository = Mockito.mock(CursoRepository.class);
        usuarioValidation = Mockito.mock(UsuarioValidationChainBuilder.class);
        usuarioValidationChain = Mockito.mock(ValidationHandler.class);
        cpfValidationChain = Mockito.mock(ValidationHandler.class);

        Mockito.when(usuarioValidation.buildUsuarioChain()).thenReturn(usuarioValidationChain);
        Mockito.when(usuarioValidation.buildCpfChain()).thenReturn(cpfValidationChain);
        Mockito.doNothing().when(usuarioValidationChain).handle(any());
        Mockito.doNothing().when(cpfValidationChain).handle(any());

        usuarioService = new UsuarioService(usuarioRepository, categoriaService, cursoRepository, usuarioValidation);
    }

    @Test
    void testCriarUsuarioSucesso() {
        UsuarioCreateDto usuarioCreateDto = new UsuarioCreateDto();
        usuarioCreateDto.setNome("Naruto");
        usuarioCreateDto.setCpf("12345678900");
        usuarioCreateDto.setEmail("naruto@aluno.ifsp.edu.br");
        usuarioCreateDto.setCategoriaId(1);
        usuarioCreateDto.setCursoId(10);

        Mockito.when(usuarioRepository.existsByCpf(usuarioCreateDto.getCpf())).thenReturn(false);
        Mockito.when(usuarioRepository.existsByEmail(usuarioCreateDto.getEmail())).thenReturn(false);

        CategoriaUsuario categoria = new CategoriaUsuario();
        Mockito.when(categoriaService.consultarPorId(usuarioCreateDto.getCategoriaId())).thenReturn(categoria);

        Curso curso = new Curso();
        Mockito.when(cursoRepository.findById(usuarioCreateDto.getCursoId())).thenReturn(Optional.of(curso));

        Usuario usuarioSalvo = new Usuario();
        usuarioSalvo.setIdUsuario(99);
        usuarioSalvo.setNomeUsuario("Naruto");
        usuarioSalvo.setCpf(usuarioCreateDto.getCpf());
        usuarioSalvo.setEmail(usuarioCreateDto.getEmail());
        usuarioSalvo.setCategoria(categoria);
        usuarioSalvo.setCurso(curso);
        usuarioSalvo.setStatus(Usuario.StatusUsuario.ATIVO);

        Mockito.when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioSalvo);
        Usuario resultado = usuarioService.criarUsuario(usuarioCreateDto);

        assertNotNull(resultado);
        assertEquals(99, resultado.getIdUsuario());
        assertEquals("Naruto", resultado.getNomeUsuario());
        assertEquals("12345678900", resultado.getCpf());
        assertEquals("naruto@aluno.ifsp.edu.br", resultado.getEmail());
        assertEquals(categoria, resultado.getCategoria());
        assertEquals(curso, resultado.getCurso());
        assertEquals(Usuario.StatusUsuario.ATIVO, resultado.getStatus());

        Mockito.verify(usuarioRepository).save(any(Usuario.class));
        Mockito.verify(usuarioValidationChain).handle(usuarioCreateDto);
    }

    @Test
    void testCriarUsuarioCpfDuplicado() {
        UsuarioCreateDto usuarioCreateDto = new UsuarioCreateDto();
        usuarioCreateDto.setCpf("12345678900");
        usuarioCreateDto.setEmail("naruto@aluno.ifsp.edu.br");

        Mockito.when(usuarioRepository.existsByCpf(usuarioCreateDto.getCpf())).thenReturn(true);
        ResponseStatusException resultado = assertThrows(ResponseStatusException.class, () -> usuarioService.criarUsuario(usuarioCreateDto));

        assertEquals(HttpStatus.CONFLICT, resultado.getStatusCode());
        assertTrue(resultado.getReason().contains("CPF já cadastrado"));
        Mockito.verify(usuarioValidationChain).handle(usuarioCreateDto);
    }

    @Test
    void testCriarUsuarioEmailDuplicado() {
        UsuarioCreateDto usuarioCreateDto = new UsuarioCreateDto();
        usuarioCreateDto.setCpf("12345678900");
        usuarioCreateDto.setEmail("naruto@aluno.ifsp.edu.br");

        Mockito.when(usuarioRepository.existsByEmail(usuarioCreateDto.getEmail())).thenReturn(true);
        ResponseStatusException resultado = assertThrows(ResponseStatusException.class, () -> usuarioService.criarUsuario(usuarioCreateDto));

        assertEquals(HttpStatus.NOT_FOUND, resultado.getStatusCode());
        assertTrue(resultado.getReason().contains("E-mail já cadastrado"));
        Mockito.verify(usuarioValidationChain).handle(usuarioCreateDto);
    }

    @Test
    void testCategoriaUsuarioNaoExiste() {
        UsuarioCreateDto usuarioCreateDto = new UsuarioCreateDto();
        usuarioCreateDto.setNome("Naruto");
        usuarioCreateDto.setCpf("12345678900");
        usuarioCreateDto.setEmail("naruto@aluno.ifsp.edu.br");
        usuarioCreateDto.setCategoriaId(1);
        usuarioCreateDto.setCursoId(10);

        Mockito.when(usuarioRepository.existsByCpf(usuarioCreateDto.getCpf())).thenReturn(false);
        Mockito.when(usuarioRepository.existsByEmail(usuarioCreateDto.getEmail())).thenReturn(false);
        Mockito.when(categoriaService.consultarPorId(usuarioCreateDto.getCategoriaId())).thenReturn(null);

        ResponseStatusException resultado = assertThrows(ResponseStatusException.class, () -> usuarioService.criarUsuario(usuarioCreateDto));

        assertEquals(HttpStatus.NOT_FOUND, resultado.getStatusCode());
        assertTrue(resultado.getReason().contains("Categoria Usuario não encontrada"));
        Mockito.verify(usuarioValidationChain).handle(usuarioCreateDto);
    }

    @Test
    void testCursoNaoEncontradoAoCriarUsuario() {
        UsuarioCreateDto usuarioCreateDto = new UsuarioCreateDto();
        usuarioCreateDto.setNome("Naruto");
        usuarioCreateDto.setCpf("12345678900");
        usuarioCreateDto.setEmail("naruto@aluno.ifsp.edu.br");
        usuarioCreateDto.setCategoriaId(1);
        usuarioCreateDto.setCursoId(10);

        Mockito.when(usuarioRepository.existsByCpf(usuarioCreateDto.getCpf())).thenReturn(false);
        Mockito.when(usuarioRepository.existsByEmail(usuarioCreateDto.getEmail())).thenReturn(false);

        CategoriaUsuario categoria = new CategoriaUsuario();
        Mockito.when(categoriaService.consultarPorId(usuarioCreateDto.getCategoriaId())).thenReturn(categoria);
        Mockito.when(cursoRepository.findById(usuarioCreateDto.getCursoId())).thenReturn(Optional.empty());

        ResponseStatusException resultado = assertThrows(ResponseStatusException.class, () -> usuarioService.criarUsuario(usuarioCreateDto));

        assertEquals(HttpStatus.NOT_FOUND, resultado.getStatusCode());
        assertTrue(resultado.getReason().contains("Curso não encontrado"));
        Mockito.verify(usuarioValidationChain).handle(usuarioCreateDto);
    }

    @Test
    void testListarUsuarios() {
        Usuario usuario1 = new Usuario();
        Usuario usuario2 = new Usuario();
        Mockito.when(usuarioRepository.findAll()).thenReturn(List.of(usuario1, usuario2));
        List<Usuario> usuarios = usuarioService.listarUsuarios();

        assertEquals(2, usuarios.size());
        assertTrue(usuarios.contains(usuario1));
        assertTrue(usuarios.contains(usuario2));
    }

    @Test
    void testProcurarPorCpfSucesso() {
        String cpf = "12345678900";
        Usuario usuario = new Usuario();
        Mockito.when(usuarioRepository.findByCpf(cpf)).thenReturn(Optional.of(usuario));
        Usuario resultado = usuarioService.procurarPorCpf(cpf);
        assertEquals(usuario, resultado);
    }

    @Test
    void testProcurarPorCpfNaoEncontrado() {
        String cpf = "12345678900";
        Mockito.when(usuarioRepository.findByCpf(cpf)).thenReturn(Optional.empty());
        ResponseStatusException resultado = assertThrows(ResponseStatusException.class, () -> usuarioService.procurarPorCpf(cpf));
        assertEquals(HttpStatus.NOT_FOUND, resultado.getStatusCode());
        assertTrue(resultado.getReason().contains("Não foi localizado nenhum usuário com este CPF."));
    }

    @Test
    void testConsultarPorCurso() {
        Curso curso = new Curso();
        Mockito.when(usuarioRepository.existsByCurso(curso)).thenReturn(true);
        assertTrue(usuarioService.consultarPorCurso(curso));
    }

    @Test
    void testAtualizarUsuarioSucesso() {
        String cpf = "12345678900";
        Usuario usuarioAtual = new Usuario();
        usuarioAtual.setIdUsuario(1);
        usuarioAtual.setCpf(cpf);
        usuarioAtual.setEmail("old@ifsp.edu.br");
        usuarioAtual.setNomeUsuario("Old Name");
        usuarioAtual.setCategoria(new CategoriaUsuario());
        usuarioAtual.setCurso(new Curso());

        UsuarioCreateDto novosDados = new UsuarioCreateDto();
        novosDados.setNome("Novo Nome");
        novosDados.setCpf("98765432100");
        novosDados.setEmail("novo@ifsp.edu.br");
        novosDados.setCategoriaId(2);
        novosDados.setCursoId(5);

        CategoriaUsuario novaCategoria = new CategoriaUsuario();
        Curso novoCurso = new Curso();

        Mockito.when(usuarioRepository.findByCpf(cpf)).thenReturn(Optional.of(usuarioAtual));
        Mockito.when(usuarioRepository.existsByCpfAndIdUsuarioNot(novosDados.getCpf(), usuarioAtual.getIdUsuario())).thenReturn(false);
        Mockito.when(usuarioRepository.existsByEmailAndIdUsuarioNot(novosDados.getEmail().trim(), usuarioAtual.getIdUsuario())).thenReturn(false);
        Mockito.when(categoriaService.consultarPorId(novosDados.getCategoriaId())).thenReturn(novaCategoria);
        Mockito.when(cursoRepository.findById(novosDados.getCursoId())).thenReturn(Optional.of(novoCurso));
        Mockito.when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Usuario resultado = usuarioService.atualizarUsuario(cpf, novosDados);

        Mockito.verify(usuarioValidationChain).handle(novosDados);
        Mockito.verify(cpfValidationChain).handle(cpf);
        assertEquals("Novo Nome", resultado.getNomeUsuario());
        assertEquals("98765432100", resultado.getCpf());
        assertEquals("novo@ifsp.edu.br", resultado.getEmail());
        assertEquals(novaCategoria, resultado.getCategoria());
        assertEquals(novoCurso, resultado.getCurso());
    }

    @Test
    void testAtualizarUsuarioCpfDuplicado() {
        String cpf = "12345678900";
        Usuario usuarioAtual = new Usuario();
        usuarioAtual.setIdUsuario(1);
        usuarioAtual.setCpf(cpf);
        usuarioAtual.setEmail("old@ifsp.edu.br");

        UsuarioCreateDto novosDados = new UsuarioCreateDto();
        novosDados.setCpf("98765432100");
        novosDados.setEmail("novo@ifsp.edu.br");

        Mockito.when(usuarioRepository.findByCpf(cpf)).thenReturn(Optional.of(usuarioAtual));
        Mockito.when(usuarioRepository.existsByCpfAndIdUsuarioNot(novosDados.getCpf(), usuarioAtual.getIdUsuario())).thenReturn(true);

        ResponseStatusException resultado = assertThrows(ResponseStatusException.class, () -> usuarioService.atualizarUsuario(cpf, novosDados));

        assertEquals(HttpStatus.CONFLICT, resultado.getStatusCode());
        assertTrue(resultado.getReason().contains("Já existe outro usuário com este CPF."));
        Mockito.verify(cpfValidationChain).handle(cpf);
        Mockito.verify(usuarioValidationChain).handle(novosDados);
    }

    @Test
    void testAtualizarUsuarioEmailDuplicado() {
        String cpf = "12345678900";
        Usuario usuarioAtual = new Usuario();
        usuarioAtual.setIdUsuario(1);
        usuarioAtual.setCpf(cpf);
        usuarioAtual.setEmail("old@ifsp.edu.br");

        UsuarioCreateDto novosDados = new UsuarioCreateDto();
        novosDados.setCpf("98765432100");
        novosDados.setEmail("novo@ifsp.edu.br");

        Mockito.when(usuarioRepository.findByCpf(cpf)).thenReturn(Optional.of(usuarioAtual));
        Mockito.when(usuarioRepository.existsByCpfAndIdUsuarioNot(novosDados.getCpf(), usuarioAtual.getIdUsuario())).thenReturn(false);
        Mockito.when(usuarioRepository.existsByEmailAndIdUsuarioNot(novosDados.getEmail().trim(), usuarioAtual.getIdUsuario())).thenReturn(true);

        ResponseStatusException resultado = assertThrows(ResponseStatusException.class, () -> usuarioService.atualizarUsuario(cpf, novosDados));

        assertEquals(HttpStatus.CONFLICT, resultado.getStatusCode());
        assertTrue(resultado.getReason().contains("Já existe outro usuário com este e-mail."));
        Mockito.verify(cpfValidationChain).handle(cpf);
        Mockito.verify(usuarioValidationChain).handle(novosDados);
    }

    @Test
    void testAtualizarUsuarioCursoNaoEncontrado() {
        String cpf = "12345678900";
        Usuario usuarioAtual = new Usuario();
        usuarioAtual.setIdUsuario(1);
        usuarioAtual.setCpf(cpf);

        UsuarioCreateDto novosDados = new UsuarioCreateDto();
        novosDados.setCpf("98765432100");
        novosDados.setEmail("novo@ifsp.edu.br");
        novosDados.setCategoriaId(0);
        novosDados.setCursoId(9);

        Mockito.when(usuarioRepository.findByCpf(cpf)).thenReturn(Optional.of(usuarioAtual));
        Mockito.when(usuarioRepository.existsByCpfAndIdUsuarioNot(novosDados.getCpf(), usuarioAtual.getIdUsuario())).thenReturn(false);
        Mockito.when(usuarioRepository.existsByEmailAndIdUsuarioNot(novosDados.getEmail().trim(), usuarioAtual.getIdUsuario())).thenReturn(false);
        Mockito.when(cursoRepository.findById(novosDados.getCursoId())).thenReturn(Optional.empty());

        ResponseStatusException resultado = assertThrows(ResponseStatusException.class, () -> usuarioService.atualizarUsuario(cpf, novosDados));

        assertEquals(HttpStatus.NOT_FOUND, resultado.getStatusCode());
        assertTrue(resultado.getReason().contains("Curso não encontrado."));
    }

    @Test
    void testDeletarUsuarioSucesso() {
        String cpf = "12345678900";
        Usuario usuario = new Usuario();
        Mockito.when(usuarioRepository.findByCpf(cpf)).thenReturn(Optional.of(usuario));
        usuarioService.deletarUsuario(cpf);
        Mockito.verify(usuarioRepository).deleteByCpf(cpf);
    }

    @Test
    void testAlterarStatusUsuarioSucesso() {
        Integer id = 1;
        Usuario usuario = new Usuario();
        usuario.setStatus(Usuario.StatusUsuario.ATIVO);
        Mockito.when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));
        Mockito.when(usuarioRepository.save(usuario)).thenReturn(usuario);
        Usuario resultado = usuarioService.alterarStatusUsuario(id, Usuario.StatusUsuario.SUSPENSO);
        assertEquals(Usuario.StatusUsuario.SUSPENSO, resultado.getStatus());
        Mockito.verify(usuarioRepository).save(usuario);
    }

    @Test
    void testAlterarStatusUsuarioNaoEncontrado() {
        Integer id = 1;
        Mockito.when(usuarioRepository.findById(id)).thenReturn(Optional.empty());
        ResponseStatusException resultado = assertThrows(ResponseStatusException.class, () -> usuarioService.alterarStatusUsuario(id, Usuario.StatusUsuario.SUSPENSO));
        assertEquals(HttpStatus.NOT_FOUND, resultado.getStatusCode());
        assertTrue(resultado.getReason().contains("Usuário não encontrado."));
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> testes-unitarios
