package br.edu.ifsp.biblioteca.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import br.edu.ifsp.biblioteca.model.Livro;

public interface LivroRepository extends JpaRepository<Livro,String>{
	 Optional<Livro> findByIsbn(String isbn);
	 boolean existsByIsbn(String isbn);
	 void deleteByIsbn(String isbn);
	 boolean existsByAutorAndEditoraAndEdicao(String autor, String editora, String edicao);   
}
