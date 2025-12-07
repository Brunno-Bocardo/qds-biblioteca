package br.edu.ifsp.biblioteca.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.function.Function;

public class IsbnHandler<T> extends BaseHandler<T> {

    private Function<T, String> isbnExtractor;

    public IsbnHandler(Function<T, String> isbnExtractor) {
        this.isbnExtractor = isbnExtractor;
    }

    @Override
    public void handle(T data) {
        String isbn;
        
        if (data instanceof String s) {
            isbn = s;
        } else {
            isbn = isbnExtractor.apply(data);
        }

        if (isbn == null || isbn.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O ISBN é obrigatório");
        }
        
        super.handle(data);
    }

}
