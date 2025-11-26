package br.edu.ifsp.biblioteca.strategy;

import java.time.LocalDate;

import br.edu.ifsp.biblioteca.model.Usuario;
import br.edu.ifsp.biblioteca.model.CategoriaUsuario;
import br.edu.ifsp.biblioteca.model.Estoque;

public interface IEmprestimoStrategy {

    boolean supports(CategoriaUsuario categoriaUsuario);
    LocalDate calculateDueDate(Usuario usuario, Estoque estoque, LocalDate loanDate);
    int maxAllowedActiveLoans(Usuario usuario);
}
