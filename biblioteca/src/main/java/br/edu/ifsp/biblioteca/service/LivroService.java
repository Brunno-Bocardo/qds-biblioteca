package br.edu.ifsp.biblioteca.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.edu.ifsp.biblioteca.dto.LivroCreateDto;
import br.edu.ifsp.biblioteca.dto.LivroUpdateDto;
import br.edu.ifsp.biblioteca.model.CategoriaLivro;
import br.edu.ifsp.biblioteca.model.Livro;
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
	public Livro cadastrarLivro(LivroCreateDto livro) {		
		
		if(livro.getIsbn() == null || livro.getIsbn().isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O ISBN é obrigatório");
		}
		if(livro.getTitulo() == null || livro.getTitulo().isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O titulo é obrigatório");
		}
		if(livro.getAutor() == null || livro.getAutor().isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O autor é obrigatório");
		}
		if(livro.getEditora() == null || livro.getEditora().isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O editora é obrigatório");
		}
		if(livro.getEdicao() == null || livro.getEdicao().isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O edição é obrigatório");
		}
		if(livro.getCategoriaId() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O categoria é obrigatório");
		}
		if(livroRepository.existsByIsbn(livro.getIsbn())) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um livro com este ISBN");
		}
		if(livroRepository.existsByAutorAndEditoraAndEdicao(livro.getAutor(), livro.getEditora(), livro.getEdicao())){
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um livro com essas atribuições");
		}
		
		CategoriaLivro categoriaExistente = categoriaLivroService.consultarPorId(livro.getCategoriaId());
		
		Livro novoLivro = new Livro(livro.getIsbn(), livro.getTitulo(), livro.getAutor(), livro.getEditora(), livro.getEdicao(), categoriaExistente);
		
		return livroRepository.save(novoLivro);
	}
	
	public Livro procurarPorIsbn(String isbn){
		return livroRepository.findByIsbn(isbn).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Não foi localizado nenhum livro com este ISBN"));
	}
	
	public List<Livro>listarLivros(){
		return livroRepository.findAll();
	}
	
	@Transactional
	public Livro atualizarLivro(String isbn, LivroUpdateDto livroAtualizado){
		Livro livro = procurarPorIsbn(isbn);
		
		if(livroRepository.existsByAutorAndEditoraAndEdicao(livroAtualizado.getAutor(), livroAtualizado.getEditora(), livroAtualizado.getEdicao())) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um livro com essas atribuições");
		}
		
		CategoriaLivro categoriaLivro = this.categoriaLivroService.consultarPorId(livroAtualizado.getCategoriaId());
		
		livro.setAutor(livroAtualizado.getAutor());
		livro.setCategoria(categoriaLivro);
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
