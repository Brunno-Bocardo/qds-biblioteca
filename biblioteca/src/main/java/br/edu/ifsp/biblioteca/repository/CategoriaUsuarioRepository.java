package br.edu.ifsp.biblioteca.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifsp.biblioteca.model.CategoriaUsuario;

public interface CategoriaUsuarioRepository extends JpaRepository<CategoriaUsuario, Integer> {
	boolean existsByNomeCategoriaUsuario(String nome);
}