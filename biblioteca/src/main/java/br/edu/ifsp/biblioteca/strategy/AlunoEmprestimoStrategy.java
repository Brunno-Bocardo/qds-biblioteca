package br.edu.ifsp.biblioteca.strategy;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import br.edu.ifsp.biblioteca.model.CategoriaUsuario;
import br.edu.ifsp.biblioteca.model.Estoque;
import br.edu.ifsp.biblioteca.model.Usuario;

@Component
public class AlunoEmprestimoStrategy implements IEmprestimoStrategy {

    private static final int DEFAULT_DUE_DAYS = 15;
    private static final int SAME_AREA_DUE_DAYS = 30;
    private static final int MAX_ACTIVE_LOANS = 3;
    private static final String CATEGORY_NAME = "ALUNO";

    @Override
    public boolean supports(CategoriaUsuario categoriaUsuario) {
        return categoriaUsuario != null
            && CATEGORY_NAME.equalsIgnoreCase(categoriaUsuario.getNomeCategoriaUsuario());
    }

    @Override
    public LocalDate calculateDueDate(Usuario usuario, Estoque estoque, LocalDate loanDate) {
        if (loanDate == null) {
            throw new IllegalArgumentException("Data de empréstimo não pode ser vazia");
        }
        if (isBookFromUserCourseArea(usuario, estoque)) {
            return loanDate.plusDays(SAME_AREA_DUE_DAYS);
        }
        return loanDate.plusDays(DEFAULT_DUE_DAYS);
    }

    @Override
    public int maxAllowedActiveLoans(Usuario usuario) {
        return MAX_ACTIVE_LOANS;
    }

    private boolean isBookFromUserCourseArea(Usuario usuario, Estoque estoque) {
        if (usuario == null || estoque == null || estoque.getLivro() == null) {
            return false;
        }

        String cursoArea = usuario.getCurso() != null ? usuario.getCurso().getNomeCurso() : null;
        String livroArea = estoque.getLivro().getCategoria().getNomeCategoriaLivro();

        if (cursoArea == null || livroArea == null) {
            return false;
        }

        return cursoArea.equalsIgnoreCase(livroArea);
    }
}

