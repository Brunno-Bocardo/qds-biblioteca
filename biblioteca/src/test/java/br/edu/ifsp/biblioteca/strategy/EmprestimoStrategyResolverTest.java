package br.edu.ifsp.biblioteca.strategy;

import br.edu.ifsp.biblioteca.model.CategoriaUsuario;
import br.edu.ifsp.biblioteca.model.Usuario;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;

class EmprestimoStrategyResolverTest {

    @Test
    void testResolveRetornaPrimeiraEstrategiaQueSuporta() {
        IEmprestimoStrategy strategyAluno = Mockito.mock(IEmprestimoStrategy.class);
        IEmprestimoStrategy strategyProfessor = Mockito.mock(IEmprestimoStrategy.class);

        CategoriaUsuario categoriaAluno = new CategoriaUsuario();
        categoriaAluno.setNomeCategoriaUsuario("ALUNO");
        Usuario usuario = new Usuario();
        usuario.setCategoria(categoriaAluno);

        Mockito.when(strategyAluno.supports(categoriaAluno)).thenReturn(true);
        Mockito.when(strategyProfessor.supports(any())).thenReturn(false);

        EmprestimoStrategyResolver resolver = new EmprestimoStrategyResolver(List.of(strategyAluno, strategyProfessor));

        IEmprestimoStrategy resultado = resolver.resolve(usuario);

        assertSame(strategyAluno, resultado);
        Mockito.verify(strategyAluno).supports(categoriaAluno);
        Mockito.verify(strategyProfessor, never()).supports(categoriaAluno);
    }

    @Test
    void testResolveLancaExcecaoQuandoCategoriaNula() {
        Usuario usuario = new Usuario();
        usuario.setCategoria(null);

        EmprestimoStrategyResolver resolver = new EmprestimoStrategyResolver(List.of());

        IllegalArgumentException excecao = assertThrows(IllegalArgumentException.class, () -> resolver.resolve(usuario));
        assertTrue(excecao.getMessage().toLowerCase().contains("categoria"));
    }

    @Test
    void testResolveLancaExcecaoQuandoNenhumaEstrategiaEncontrada() {
        IEmprestimoStrategy strategyAluno = Mockito.mock(IEmprestimoStrategy.class);
        CategoriaUsuario categoriaProfessor = new CategoriaUsuario();
        categoriaProfessor.setNomeCategoriaUsuario("PROFESSOR");

        Usuario usuario = new Usuario();
        usuario.setCategoria(categoriaProfessor);

        Mockito.when(strategyAluno.supports(categoriaProfessor)).thenReturn(false);

        EmprestimoStrategyResolver resolver = new EmprestimoStrategyResolver(List.of(strategyAluno));

        IllegalArgumentException excecao = assertThrows(IllegalArgumentException.class, () -> resolver.resolve(usuario));
        assertTrue(excecao.getMessage().toLowerCase().contains("estrat"));
    }
}
