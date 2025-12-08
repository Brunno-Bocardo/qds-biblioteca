package br.edu.ifsp.biblioteca.builder;

import org.springframework.stereotype.Component;

import br.edu.ifsp.biblioteca.dto.LivroCreateDto;
import br.edu.ifsp.biblioteca.handler.IsbnHandler;
import br.edu.ifsp.biblioteca.handler.LivroHandler;
import br.edu.ifsp.biblioteca.handler.ValidationHandler;

@Component
public class LivroValidationChainBuilder {
	public ValidationHandler<LivroCreateDto> buildLivroChain() {
		LivroHandler handleLivro = new LivroHandler();
        IsbnHandler<LivroCreateDto> handleIsbn = new IsbnHandler<>(LivroCreateDto::getIsbn);
        handleLivro.setNext(handleIsbn);
        return handleLivro;
	}
	
	public ValidationHandler<String> buildIsbnChain() {
        return new IsbnHandler<>(s -> s);
    }
}
