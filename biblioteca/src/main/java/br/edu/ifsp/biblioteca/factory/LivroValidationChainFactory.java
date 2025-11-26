package br.edu.ifsp.biblioteca.factory;

import br.edu.ifsp.biblioteca.dto.LivroCreateDto;
import br.edu.ifsp.biblioteca.handler.IsbnHandler;
import br.edu.ifsp.biblioteca.handler.LivroHandler;
import br.edu.ifsp.biblioteca.handler.ValidationHandler;

public class LivroValidationChainFactory {
    public static ValidationHandler<LivroCreateDto> createLivroChain() {
        LivroHandler handleLivro = new LivroHandler();
        IsbnHandler<LivroCreateDto> handleIsbn = new IsbnHandler<>(LivroCreateDto::getIsbn);
        handleLivro.setNext(handleLivro);
        handleIsbn.setNext(handleIsbn);
        return handleLivro;
    }

    public static ValidationHandler<String> createIsbnChain() {
        IsbnHandler<String> handleIsbn = new IsbnHandler<>(s -> s);
        handleIsbn.setNext(handleIsbn);
        return handleIsbn;
    }
}
