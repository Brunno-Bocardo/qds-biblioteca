package br.edu.ifsp.biblioteca.strategy;

import br.edu.ifsp.biblioteca.model.CategoriaLivro;
import br.edu.ifsp.biblioteca.model.CategoriaUsuario;
import br.edu.ifsp.biblioteca.model.Curso;
import br.edu.ifsp.biblioteca.model.Estoque;
import br.edu.ifsp.biblioteca.model.Livro;
import br.edu.ifsp.biblioteca.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class AlunoEmprestimoStrategyTest {

    private AlunoEmprestimoStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new AlunoEmprestimoStrategy();
    }

    @Test
    void testSupportsCategoriaAluno() {
        CategoriaUsuario categoria = new CategoriaUsuario();
        categoria.setNomeCategoriaUsuario("aluno");

        assertTrue(strategy.supports(categoria));
    }

    @Test
    void testSupportsRetornaFalseParaOutrasCategoriasOuNull() {
        CategoriaUsuario outraCategoria = new CategoriaUsuario();
        outraCategoria.setNomeCategoriaUsuario("PROFESSOR");

        assertFalse(strategy.supports(outraCategoria));
        assertFalse(strategy.supports(null));
    }

    @Test
    void testCalculateDueDateMesmaAreaCurso() {
        Usuario usuario = criarUsuarioComCurso("Computacao");
        Estoque estoque = criarEstoqueComCategoriaLivro("computacao");
        LocalDate loanDate = LocalDate.of(2024, 1, 1);

        LocalDate dueDate = strategy.calculateDueDate(usuario, estoque, loanDate);

        assertEquals(loanDate.plusDays(30), dueDate);
    }

    @Test
    void testCalculateDueDateAreaDiferenteUsaPadrao() {
        Usuario usuario = criarUsuarioComCurso("Matematica");
        Estoque estoque = criarEstoqueComCategoriaLivro("Historia");
        LocalDate loanDate = LocalDate.of(2024, 2, 10);

        LocalDate dueDate = strategy.calculateDueDate(usuario, estoque, loanDate);

        assertEquals(loanDate.plusDays(15), dueDate);
    }

    @Test
    void testCalculateDueDateCursoSemInformacaoUsaPadrao() {
        Usuario usuarioSemCurso = new Usuario();
        Estoque estoque = criarEstoqueComCategoriaLivro("Historia");
        LocalDate loanDate = LocalDate.of(2024, 6, 1);

        LocalDate dueDate = strategy.calculateDueDate(usuarioSemCurso, estoque, loanDate);

        assertEquals(loanDate.plusDays(15), dueDate);
    }

    @Test
    void testCalculateDueDateCategoriaLivroSemNomeUsaPadrao() {
        Usuario usuario = criarUsuarioComCurso("Historia");
        Estoque estoque = criarEstoqueComCategoriaLivro(null);
        LocalDate loanDate = LocalDate.of(2024, 7, 1);

        LocalDate dueDate = strategy.calculateDueDate(usuario, estoque, loanDate);

        assertEquals(loanDate.plusDays(15), dueDate);
    }

    @Test
    void testCalculateDueDateDadosIncompletosUsaPadrao() {
        Estoque estoqueSemLivro = new Estoque();
        LocalDate loanDate = LocalDate.of(2024, 3, 5);

        LocalDate dueDate = strategy.calculateDueDate(null, estoqueSemLivro, loanDate);

        assertEquals(loanDate.plusDays(15), dueDate);
    }

    @Test
    void testCalculateDueDateComDataNulaLancaExcecao() {
        IllegalArgumentException excecao = assertThrows(IllegalArgumentException.class,
                () -> strategy.calculateDueDate(new Usuario(), new Estoque(), null));

        assertTrue(excecao.getMessage().contains("Data de empréstimo não pode ser vazia"));
    }

    @Test
    void testMaxAllowedActiveLoans() {
        assertEquals(3, strategy.maxAllowedActiveLoans(new Usuario()));
    }

    private Usuario criarUsuarioComCurso(String nomeCurso) {
        Curso curso = new Curso();
        curso.setNomeCurso(nomeCurso);

        Usuario usuario = new Usuario();
        usuario.setCurso(curso);
        return usuario;
    }

    private Estoque criarEstoqueComCategoriaLivro(String nomeCategoriaLivro) {
        CategoriaLivro categoriaLivro = new CategoriaLivro();
        categoriaLivro.setNomeCategoriaLivro(nomeCategoriaLivro);

        Livro livro = new Livro();
        livro.setCategoria(categoriaLivro);

        Estoque estoque = new Estoque();
        estoque.setLivro(livro);
        return estoque;
    }
}
