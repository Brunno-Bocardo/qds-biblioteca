package br.edu.ifsp.biblioteca.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.edu.ifsp.biblioteca.model.CategoriaLivro;
import br.edu.ifsp.biblioteca.model.Livro;
import br.edu.ifsp.biblioteca.repository.CategoriaLivroRepository;
import br.edu.ifsp.biblioteca.repository.LivroRepository;
import jakarta.transaction.Transactional;

import java.util.*;

@Service
public class LivroService {
	
	private final LivroRepository livroRepository;
	private final CategoriaLivroRepository categoriaLivroRepository;
	
	public LivroService(LivroRepository livroRepository, CategoriaLivroRepository categoriaRepository){
        this.livroRepository = livroRepository; 
        this.categoriaLivroRepository = categoriaRepository;
    }

	@Transactional
	public Livro cadastrar(String isbn, String titulo, String autor, String editora, String edicao, Integer categoriaId) {		
		if(livroRepository.existsByIsbn(isbn)) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um livro com este ISBN");
		}
		
		if(livroRepository.existsByAutorAndEditoraAndEdicao(autor, editora, edicao)){
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um livro com essas atribuições");
		}
		
		CategoriaLivro categoriaExistente = categoriaLivroRepository.findById(categoriaId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "A categoria informada não consta no sistema"));
		
		Livro novoLivro = new Livro(isbn, titulo, autor, editora, edicao, categoriaExistente);
		
		return livroRepository.save(novoLivro);
	}
	
	public Livro procurarPorIsbn(String isbn){
		return livroRepository.findByIsbn(isbn).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Não foi localizado nenhum livro com este ISBN"));
	}
	
	public List<Livro>listarLivros(){
		return livroRepository.findAll();
	}
	
	@Transactional
	public Livro atualizar(String isbn, Livro livroAtualizado){
		Livro livro = procurarPorIsbn(isbn);
		
		if(livroRepository.existsByAutorAndEditoraAndEdicao(livroAtualizado.getAutor(), livroAtualizado.getEditora(), livroAtualizado.getEdicao())) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um livro com essas atribuições");
		}
		
		livro.setAutor(livroAtualizado.getAutor());
		livro.setCategoria(livroAtualizado.getCategoria());
		livro.setEditora(livroAtualizado.getEditora());
		livro.setEdicao(livroAtualizado.getEdicao());
		livro.setTitulo(livroAtualizado.getTitulo());
		
		return livroRepository.save(livro);
	}
	
	@Transactional
	public void deletar(String isbn) {
		//precisa verrificar se há emprestimos do livro.
		procurarPorIsbn(isbn);
		livroRepository.deleteByIsbn(isbn);
	}
	
	
}
