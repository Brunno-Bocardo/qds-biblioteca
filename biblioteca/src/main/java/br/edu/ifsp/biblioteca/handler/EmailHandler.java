package br.edu.ifsp.biblioteca.handler;

import java.util.function.Function;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class EmailHandler<T> extends BaseHandler<T> {
	private Function<T, String> emailExtractor;
	
	public EmailHandler(Function<T, String> emailExtractor) {
		this.emailExtractor = emailExtractor;
	}
	
	@Override
	public void handle(T data) {
		String email = emailExtractor.apply(data);
		if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "E-mail inválido");
        }
		super.handle(data);
	}
}
