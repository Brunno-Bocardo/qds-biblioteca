package br.edu.ifsp.biblioteca.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifsp.biblioteca.model.Emprestimo;
import br.edu.ifsp.biblioteca.model.Usuario;
import br.edu.ifsp.biblioteca.model.Emprestimo.StatusEmprestimo;

public interface EmprestimoRepository extends JpaRepository<Emprestimo,Integer>{
	int countByUsuarioAndStatus(Usuario usuario, StatusEmprestimo status);
	Optional<Emprestimo> findById(Integer idEmprestimo);
}
