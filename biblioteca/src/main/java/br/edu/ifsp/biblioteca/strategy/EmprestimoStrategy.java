package br.edu.ifsp.biblioteca.strategy;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import br.edu.ifsp.biblioteca.model.Usuario;
import br.edu.ifsp.biblioteca.model.Estoque;


@Component
public class EmprestimoStrategy implements IEmprestimoStrategy {

    private static final int DEFAULT_DUE_DAYS = 40;
    private static final int DEFAULT_MAX_ACTIVE_LOANS = 5;

    @Override
    public LocalDate calculateDueDate(Usuario usuario, Estoque estoque, LocalDate loanDate) {
        if (loanDate == null) {
            throw new IllegalArgumentException("Loan date must not be null");
        }
        return loanDate.plusDays(DEFAULT_DUE_DAYS);
    }

    @Override
    public int maxAllowedActiveLoans(Usuario usuario) {
        return DEFAULT_MAX_ACTIVE_LOANS;
    }
}
