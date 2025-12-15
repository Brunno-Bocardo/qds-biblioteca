package br.edu.ifsp.biblioteca.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.edu.ifsp.biblioteca.builder.LivroValidationChainBuilder;
import br.edu.ifsp.biblioteca.dto.LivroCreateDto;
import br.edu.ifsp.biblioteca.dto.LivroUpdateDto;
import br.edu.ifsp.biblioteca.model.CategoriaLivro;
import br.edu.ifsp.biblioteca.model.Livro;
import br.edu.ifsp.biblioteca.repository.LivroRepository;
import jakarta.transaction.Transactional;

import java.util.*;

@Service
public class LivroService {
	private final LivroValidationChainBuilder livroValidation;
	private final LivroRepository livroRepository;
	private final CategoriaLivroService categoriaLivroService;
	private final EstoqueService estoqueService;

	public LivroService(LivroRepository livroRepository, CategoriaLivroService categoriaService,
			LivroValidationChainBuilder livroValidationChain, EstoqueService estoqueService) {
		this.livroValidation = livroValidationChain;
		this.livroRepository = livroRepository;
		this.categoriaLivroService = categoriaService;
		this.estoqueService = estoqueService;
	}

	@Transactional
	public Livro cadastrarLivro(LivroCreateDto livro) {
		livroValidation.buildLivroChain().handle(livro);

		if (livroRepository.existsByIsbn(livro.getIsbn())) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um livro com este ISBN");
		}
		if (livroRepository.existsByAutorAndEditoraAndEdicao(livro.getAutor(), livro.getEditora(), livro.getEdicao())) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um livro com essas atribuições");
		}

		CategoriaLivro categoriaExistente = categoriaLivroService.consultarPorId(livro.getCategoriaId());

		Livro novoLivro = new Livro(livro.getIsbn(), livro.getTitulo(), livro.getAutor(), livro.getEditora(),
				livro.getEdicao(), categoriaExistente);

		return livroRepository.save(novoLivro);
	}

	public Livro procurarPorIsbn(String isbn) {
		livroValidation.buildIsbnChain().handle(isbn);
		return livroRepository.findByIsbn(isbn).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
				"Não foi localizado nenhum livro com este ISBN"));
	}

	public List<Livro> listarLivros() {
		return livroRepository.findAll();
	}

	@Transactional
	public Livro atualizarLivro(String isbn, LivroCreateDto livroAtualizado) {
		livroValidation.buildLivroChain().handle(livroAtualizado);
		Livro livro = procurarPorIsbn(isbn);

		if (livroRepository.existsByAutorAndEditoraAndEdicao(livroAtualizado.getAutor(), livroAtualizado.getEditora(),
				livroAtualizado.getEdicao())) {
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
		this.procurarPorIsbn(isbn);
		if (estoqueService.existeExemplarParaOIsbn(isbn)) {
			throw new ResponseStatusException(HttpStatus.CONFLICT,
					"Não é possível deletar um livro que possui exemplares");
		}
		livroRepository.deleteByIsbn(isbn);
	}

}
