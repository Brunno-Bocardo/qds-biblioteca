package br.edu.ifsp.biblioteca.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import br.edu.ifsp.biblioteca.builder.LivroValidationChainBuilder;
import br.edu.ifsp.biblioteca.dto.LivroCreateDto;
import br.edu.ifsp.biblioteca.handler.ValidationHandler;
import br.edu.ifsp.biblioteca.model.CategoriaLivro;
import br.edu.ifsp.biblioteca.model.Livro;
import br.edu.ifsp.biblioteca.repository.LivroRepository;

public class LivroServiceTest {
    private LivroRepository livroRepository;
    private CategoriaLivroService categoriaLivroService;
    private LivroValidationChainBuilder livroValidation;
    private ValidationHandler<LivroCreateDto> livroValidationChain;
    private ValidationHandler<String> isbnValidationChain;
    private EstoqueService estoqueService;
    private LivroService livroService;

    @BeforeEach
    void setUp() {
        livroRepository = Mockito.mock(LivroRepository.class);
        categoriaLivroService = Mockito.mock(CategoriaLivroService.class);
        livroValidation = Mockito.mock(LivroValidationChainBuilder.class);
        livroValidationChain = Mockito.mock(ValidationHandler.class);
        isbnValidationChain = Mockito.mock(ValidationHandler.class);
        estoqueService = Mockito.mock(EstoqueService.class);

        livroService = new LivroService(livroRepository, categoriaLivroService, livroValidation, estoqueService);

        Mockito.when(livroValidation.buildLivroChain()).thenReturn(livroValidationChain);
        Mockito.when(livroValidation.buildIsbnChain()).thenReturn(isbnValidationChain);
        Mockito.doNothing().when(livroValidationChain).handle(any());
        Mockito.doNothing().when(isbnValidationChain).handle(any());
    }

    @Test
    void testCadastrarLivroSucesso() {
        LivroCreateDto livroCreateDto = new LivroCreateDto();
        livroCreateDto.setIsbn("12345");
        livroCreateDto.setTitulo("UI ou UX?");
        livroCreateDto.setAutor("Autor Teste");
        livroCreateDto.setEditora("Editora Teste");
        livroCreateDto.setEdicao("Edição 1");
        livroCreateDto.setCategoriaId(2);

        CategoriaLivro categoria = new CategoriaLivro();
        categoria.setIdCategoriaLivro(2);

        Mockito.when(livroRepository.existsByIsbn(livroCreateDto.getIsbn())).thenReturn(false);
        Mockito.when(livroRepository.existsByAutorAndEditoraAndEdicao(livroCreateDto.getAutor(),
                livroCreateDto.getEditora(), livroCreateDto.getEdicao())).thenReturn(false);
        Mockito.when(categoriaLivroService.consultarPorId(livroCreateDto.getCategoriaId())).thenReturn(categoria);

        Livro livroSalvo = new Livro();
        livroSalvo.setIsbn(livroCreateDto.getIsbn());
        livroSalvo.setTitulo(livroCreateDto.getTitulo());
        livroSalvo.setAutor(livroCreateDto.getAutor());
        livroSalvo.setEditora(livroCreateDto.getEditora());
        livroSalvo.setEdicao(livroCreateDto.getEdicao());
        livroSalvo.setCategoria(categoria);

        Mockito.when(livroRepository.save(any(Livro.class))).thenReturn(livroSalvo);

        Livro resultado = livroService.cadastrarLivro(livroCreateDto);

        assertNotNull(resultado);
        assertEquals(livroCreateDto.getIsbn(), resultado.getIsbn());
        assertEquals(livroCreateDto.getTitulo(), resultado.getTitulo());
        assertEquals(categoria, resultado.getCategoria());

        Mockito.verify(livroRepository).save(any(Livro.class));
    }

    @Test
    void testCadastrarLivroComIsbnDuplicado() {
        LivroCreateDto livroCreateDto = new LivroCreateDto();
        livroCreateDto.setIsbn("12345");

        Mockito.when(livroRepository.existsByIsbn(livroCreateDto.getIsbn())).thenReturn(true);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> livroService.cadastrarLivro(livroCreateDto));

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertEquals("Já existe um livro com este ISBN", exception.getReason());
    }

    @Test
    void testCadastrarLivroComAtributosExistentes() {
        LivroCreateDto livroCreateDto = new LivroCreateDto();
        livroCreateDto.setIsbn("12345");
        livroCreateDto.setAutor("Autor");
        livroCreateDto.setEditora("Editora");
        livroCreateDto.setEdicao("1");

        Mockito.when(livroRepository.existsByIsbn(any())).thenReturn(false);
        Mockito.when(livroRepository.existsByAutorAndEditoraAndEdicao(livroCreateDto.getAutor(),
                livroCreateDto.getEditora(), livroCreateDto.getEdicao())).thenReturn(true);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> livroService.cadastrarLivro(livroCreateDto));

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertEquals("Já existe um livro com essas atribuições", exception.getReason());
    }

    @Test
    void testListarLivrosSucesso() {
        Livro livro1 = new Livro();
        Livro livro2 = new Livro();
        Mockito.when(livroRepository.findAll()).thenReturn(List.of(livro1, livro2));

        List<Livro> livros = livroService.listarLivros();

        assertEquals(2, livros.size());
    }

    @Test
    void testProcurarPorIsbnSucesso() {
        String isbn = "12345";
        Livro livro = new Livro();
        Mockito.when(livroRepository.findByIsbn(isbn)).thenReturn(Optional.of(livro));

        Livro resultado = livroService.procurarPorIsbn(isbn);

        assertEquals(livro, resultado);
    }

    @Test
    void testProcurarPorIsbnNaoEncontrado() {
        String isbn = "99999";
        Mockito.when(livroRepository.findByIsbn(isbn)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> livroService.procurarPorIsbn(isbn));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void testAtualizarLivroSucesso() {
        String isbn = "12345";
        Livro livroExistente = new Livro();
        livroExistente.setIsbn(isbn);
        livroExistente.setTitulo("Titulo Antigo");

        LivroCreateDto novosDados = new LivroCreateDto();
        novosDados.setTitulo("Titulo Novo");
        novosDados.setAutor("Autor");
        novosDados.setEditora("Editora");
        novosDados.setEdicao("2");
        novosDados.setCategoriaId(1);

        Mockito.when(livroRepository.findByIsbn(isbn)).thenReturn(Optional.of(livroExistente));
        Mockito.when(livroRepository.existsByAutorAndEditoraAndEdicao(novosDados.getAutor(), novosDados.getEditora(),
                novosDados.getEdicao())).thenReturn(false);
        Mockito.when(categoriaLivroService.consultarPorId(novosDados.getCategoriaId()))
                .thenReturn(new CategoriaLivro());
        Mockito.when(livroRepository.save(any(Livro.class))).thenReturn(livroExistente);

        Livro resultado = livroService.atualizarLivro(isbn, novosDados);

        assertNotNull(resultado);
        assertEquals("Titulo Novo", resultado.getTitulo());
        Mockito.verify(livroRepository).save(livroExistente);
    }

    @Test
    void testAtualizarLivroAtributosExistentes() {
        String isbn = "12345";
        LivroCreateDto novosDados = new LivroCreateDto();
        novosDados.setAutor("Autor X");
        novosDados.setEditora("Editora Y");
        novosDados.setEdicao("1");

        Mockito.when(livroRepository.findByIsbn(isbn)).thenReturn(Optional.of(new Livro()));
        Mockito.when(livroRepository.existsByAutorAndEditoraAndEdicao(novosDados.getAutor(),
                novosDados.getEditora(), novosDados.getEdicao())).thenReturn(true);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> livroService.atualizarLivro(isbn, novosDados));

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertEquals("Já existe um livro com essas atribuições", exception.getReason());
    }

    @Test
    void testDeletarLivroSucesso() {
        String isbn = "12345";
        Livro livro = new Livro();
        Mockito.when(livroRepository.findByIsbn(isbn)).thenReturn(Optional.of(livro));
        Mockito.when(estoqueService.existeExemplarParaOIsbn(isbn)).thenReturn(false);
        livroService.deletar(isbn);
        Mockito.verify(livroRepository).deleteByIsbn(isbn);
    }

    @Test
    void testDeletarLivroComExemplaresFalha() {
        String isbn = "12345";

        Mockito.when(livroRepository.findByIsbn(isbn)).thenReturn(Optional.of(new Livro()));
        Mockito.when(estoqueService.existeExemplarParaOIsbn(isbn)).thenReturn(true);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> livroService.deletar(isbn));
        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertEquals("Não é possível deletar um livro que possui exemplares", exception.getReason());

        Mockito.verify(livroRepository, never()).deleteByIsbn(anyString());
    }
}