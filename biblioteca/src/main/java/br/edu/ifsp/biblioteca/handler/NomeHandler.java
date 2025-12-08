package br.edu.ifsp.biblioteca.handler;

import java.util.function.Function;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NomeHandler<T> extends BaseHandler<T> {
	private Function<T, String> nomeExtractor;
	
	public NomeHandler(Function<T, String> nomeExtractor) {
		this.nomeExtractor = nomeExtractor;
	}
	
	@Override
	public void handle(T data) {
		String nome;

        if (data instanceof String s) {
        	nome = s;
        } else {
        	nome = nomeExtractor.apply(data);
        }
		
		if(nome == null || nome.isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome é obrigatório");
		}
		
		super.handle(data);
	}
}
