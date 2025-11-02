package br.edu.ifsp.biblioteca.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifsp.biblioteca.model.CategoriaUsuario;

public interface CategoriaUsuarioRepository extends JpaRepository<CategoriaUsuario, Integer> {
	Optional<CategoriaUsuario> findByNomeCategoriaUsuario(String nome);
	boolean existsByNomeCategoriaUsuario(String nome);
}