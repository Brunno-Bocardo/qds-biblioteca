package br.edu.ifsp.biblioteca.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import br.edu.ifsp.biblioteca.model.CategoriaLivro;

public interface CategoriaLivroRepository extends JpaRepository<CategoriaLivro, Integer> {
    Optional<CategoriaLivro> findByNomeCategoriaLivro(String nome);
    boolean existsByNomeCategoriaLivro(String nome);
}