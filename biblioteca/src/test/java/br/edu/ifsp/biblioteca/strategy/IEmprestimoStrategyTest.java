package br.edu.ifsp.biblioteca.strategy;

import br.edu.ifsp.biblioteca.model.CategoriaUsuario;
import br.edu.ifsp.biblioteca.model.Estoque;
import br.edu.ifsp.biblioteca.model.Usuario;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class IEmprestimoStrategyTest {

    @Test
    void testInterfacePodeSerImplementada() {
        DummyStrategy strategy = new DummyStrategy();

        CategoriaUsuario categoria = new CategoriaUsuario();
        categoria.setNomeCategoriaUsuario("ANY");
        Usuario usuario = new Usuario();
        Estoque estoque = new Estoque();
        LocalDate loanDate = LocalDate.of(2024, 1, 1);

        assertTrue(strategy.supports(categoria));
        assertEquals(loanDate.plusDays(1), strategy.calculateDueDate(usuario, estoque, loanDate));
        assertEquals(1, strategy.maxAllowedActiveLoans(usuario));
    }

    private static class DummyStrategy implements IEmprestimoStrategy {
        @Override
        public boolean supports(CategoriaUsuario categoriaUsuario) {
            return categoriaUsuario != null;
        }

        @Override
        public LocalDate calculateDueDate(Usuario usuario, Estoque estoque, LocalDate loanDate) {
            return loanDate.plusDays(1);
        }

        @Override
        public int maxAllowedActiveLoans(Usuario usuario) {
            return 1;
        }
    }
}
