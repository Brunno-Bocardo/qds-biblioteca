package br.edu.ifsp.biblioteca.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import br.edu.ifsp.biblioteca.model.Livro;
import br.edu.ifsp.biblioteca.service.LivroService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;



@RestController
@Validated
@RequestMapping("/library/livros")
public class LivroController {
	
	private final LivroService service;
	
	public LivroController(LivroService service) {
		this.service = service;
	}
	
	public record LivroCreateDTO(
			@NotBlank String isbn,
			@NotBlank String titulo,
			@NotBlank String autor,
			@NotBlank String editora,
			@NotBlank String edicao,
			@NotNull(message = "O ID da categoria é obrigatório.") Integer categoriaId
	) {}
	
	public record LivroUpdateDTO(
			@NotBlank String titulo,
			@NotBlank String autor,
			@NotBlank String editora,
			@NotBlank String edicao,
			@NotNull(message = "O ID da categoria é obrigatório.") Integer categoriaId
	) {}
	
	@PostMapping
	public ResponseEntity<Livro> criar(@Valid @RequestBody LivroCreateDTO livroDto ){
		Livro livroSalvo = service.cadastrar(
				livroDto.isbn(), 
				livroDto.titulo(),
				livroDto.autor(), 
				livroDto.editora(), 
				livroDto.edicao(), 
				livroDto.categoriaId()
			);
		return ResponseEntity.status(HttpStatus.CREATED).body(livroSalvo);
	
}

	@GetMapping(value = "/{isbn}")
	public ResponseEntity<Livro> buscarPorIsbn(@PathVariable String isbn){
		return ResponseEntity.status(HttpStatus.OK).body(service.procurarPorIsbn(isbn));
	}

	@GetMapping
	public ResponseEntity<List<Livro>> exibirTodos(){
		return ResponseEntity.status(HttpStatus.OK).body(service.listarLivros());
	}

	@PutMapping(value = "/{isbn}")
	public ResponseEntity<Livro> atualizar(@PathVariable String isbn, @RequestBody Livro livro){
	    Livro livroAtualizado = service.atualizar(isbn, livro);
	    return ResponseEntity.status(HttpStatus.OK).body(livroAtualizado);
	}
	
	@DeleteMapping(value = "/{isbn}")
	public ResponseEntity<Void> delete(@PathVariable String isbn){
		service.deletar(isbn);
		return ResponseEntity.noContent().build();
}
}