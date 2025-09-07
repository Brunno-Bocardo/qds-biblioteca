package br.edu.ifsp.biblioteca.repository;

import br.edu.ifsp.biblioteca.model.Estoque;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstoqueRepository extends JpaRepository<Estoque, Integer> {
    Optional<Estoque> findByCodigoExemplar(String codigoExemplar);
}
