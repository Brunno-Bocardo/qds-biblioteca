package br.edu.ifsp.biblioteca.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import br.edu.ifsp.biblioteca.dto.LivroCreateDto;
import br.edu.ifsp.biblioteca.dto.LivroUpdateDto;
import br.edu.ifsp.biblioteca.model.Livro;
import br.edu.ifsp.biblioteca.service.LivroService;
import jakarta.validation.Valid;



@RestController
@Validated
@RequestMapping("/library/livros")
public class LivroController {
	
	private final LivroService service;
	
	public LivroController(LivroService service) {
		this.service = service;
	}
	
	@PostMapping
	public ResponseEntity<Livro> criar(@Valid @RequestBody LivroCreateDto livroDto ){
		Livro livroSalvo = service.cadastrarLivro(livroDto);
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
	public ResponseEntity<Livro> atualizar(@PathVariable String isbn, @RequestBody LivroUpdateDto livro){
	    Livro livroAtualizado = service.atualizarLivro(isbn, livro);
	    return ResponseEntity.status(HttpStatus.OK).body(livroAtualizado);
	}
	
	@DeleteMapping(value = "/{isbn}")
	public ResponseEntity<Void> delete(@PathVariable String isbn){
		service.deletar(isbn);
		return ResponseEntity.noContent().build();
}
}