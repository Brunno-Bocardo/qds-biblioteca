package br.edu.ifsp.biblioteca.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.edu.ifsp.biblioteca.model.CategoriaLivro;
import br.edu.ifsp.biblioteca.model.Livro;
import br.edu.ifsp.biblioteca.service.CategoriaLivroService;
import br.edu.ifsp.biblioteca.repository.LivroRepository;
import jakarta.transaction.Transactional;

import java.util.*;

@Service
public class LivroService {
	
	private final LivroRepository livroRepository;
	private final CategoriaLivroService categoriaLivroService;
	
	public LivroService(LivroRepository livroRepository, CategoriaLivroService categoriaService){
        this.livroRepository = livroRepository; 
        this.categoriaLivroService = categoriaService;
    }

	@Transactional
	public Livro cadastrar(String isbn, String titulo, String autor, String editora, String edicao, Integer categoriaId) {		
		
		if(isbn == null || isbn.isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O ISBN é obrigatório");
		}
		if(titulo == null || titulo.isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O titulo é obrigatório");
		}
		if(autor == null || autor.isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O autor é obrigatório");
		}
		if(editora == null || editora.isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O editora é obrigatório");
		}
		if(edicao == null || edicao.isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O edição é obrigatório");
		}
		if(categoriaId == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O categoria é obrigatório");
		}
		if(livroRepository.existsByIsbn(isbn)) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um livro com este ISBN");
		}
		if(livroRepository.existsByAutorAndEditoraAndEdicao(autor, editora, edicao)){
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um livro com essas atribuições");
		}
		
		CategoriaLivro categoriaExistente = categoriaLivroService.consultarPorId(categoriaId);
		
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
		// TODO: precisa verrificar se há emprestimos do livro.
		// procurarPorIsbn(isbn);
		livroRepository.deleteByIsbn(isbn);
	}
	
	
}
