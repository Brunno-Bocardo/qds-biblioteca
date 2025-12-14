package br.edu.ifsp.biblioteca.strategy;

import br.edu.ifsp.biblioteca.model.CategoriaUsuario;
import br.edu.ifsp.biblioteca.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ProfessorEmprestimoStrategyTest {

    private ProfessorEmprestimoStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new ProfessorEmprestimoStrategy();
    }

    @Test
    void testSupportsCategoriaProfessor() {
        CategoriaUsuario categoria = new CategoriaUsuario();
        categoria.setNomeCategoriaUsuario("professor");

        assertTrue(strategy.supports(categoria));
    }

    @Test
    void testSupportsRetornaFalseParaOutrasCategoriasOuNull() {
        CategoriaUsuario outra = new CategoriaUsuario();
        outra.setNomeCategoriaUsuario("ALUNO");

        assertFalse(strategy.supports(outra));
        assertFalse(strategy.supports(null));
    }

    @Test
    void testCalculateDueDateAdiciona40Dias() {
        LocalDate loanDate = LocalDate.of(2024, 4, 10);
        LocalDate dueDate = strategy.calculateDueDate(new Usuario(), null, loanDate);

        assertEquals(loanDate.plusDays(40), dueDate);
    }

    @Test
    void testCalculateDueDateComDataNulaLancaExcecao() {
        IllegalArgumentException excecao = assertThrows(IllegalArgumentException.class,
                () -> strategy.calculateDueDate(new Usuario(), null, null));

        assertTrue(excecao.getMessage().contains("Data de empréstimo não pode ser vazia"));
    }

    @Test
    void testMaxAllowedActiveLoans() {
        assertEquals(5, strategy.maxAllowedActiveLoans(new Usuario()));
    }
}
