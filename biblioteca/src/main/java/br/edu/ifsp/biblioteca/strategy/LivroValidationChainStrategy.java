package br.edu.ifsp.biblioteca.strategy;

import org.springframework.stereotype.Component;

import br.edu.ifsp.biblioteca.dto.LivroCreateDto;
import br.edu.ifsp.biblioteca.handler.IsbnHandler;
import br.edu.ifsp.biblioteca.handler.LivroHandler;
import br.edu.ifsp.biblioteca.handler.ValidationHandler;

@Component
public class LivroValidationChainStrategy {
	
    public ValidationHandler<LivroCreateDto> createLivroChain() {
        LivroHandler handleLivro = new LivroHandler();
        IsbnHandler<LivroCreateDto> handleIsbn = new IsbnHandler<>(LivroCreateDto::getIsbn);
        
        handleLivro.setNext(handleIsbn);
        
        return handleLivro;
    }

    public ValidationHandler<String> createIsbnChain() {
        IsbnHandler<String> handleIsbn = new IsbnHandler<>(s -> s); 
        return handleIsbn;
    }
}
