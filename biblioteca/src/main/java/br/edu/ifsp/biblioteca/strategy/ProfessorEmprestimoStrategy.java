package br.edu.ifsp.biblioteca.strategy;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import br.edu.ifsp.biblioteca.model.CategoriaUsuario;
import br.edu.ifsp.biblioteca.model.Estoque;
import br.edu.ifsp.biblioteca.model.Usuario;

@Component
public class ProfessorEmprestimoStrategy implements IEmprestimoStrategy {

    private static final int DUE_DAYS = 40;
    private static final int MAX_ACTIVE_LOANS = 5;
    private static final String CATEGORY_NAME = "PROFESSOR";

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
        return loanDate.plusDays(DUE_DAYS);
    }

    @Override
    public int maxAllowedActiveLoans(Usuario usuario) {
        return MAX_ACTIVE_LOANS;
    }
}

