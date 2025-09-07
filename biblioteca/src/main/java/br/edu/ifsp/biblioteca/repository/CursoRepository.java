package br.edu.ifsp.biblioteca.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import br.edu.ifsp.biblioteca.model.Curso;

public interface CursoRepository extends JpaRepository<Curso, Integer> {
    boolean existsByNomeCursoIgnoreCase(String nomeCurso);
    Optional<Curso> findByNomeCursoIgnoreCase(String nomeCurso);
    boolean existsByNomeCursoIgnoreCaseAndIdCursoNot(String nomeCurso, Integer idCurso);
}