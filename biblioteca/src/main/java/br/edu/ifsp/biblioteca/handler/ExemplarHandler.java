package br.edu.ifsp.biblioteca.handler;

import java.util.function.Function;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ExemplarHandler<T> extends BaseHandler<T> {
	private Function<T, String> codigoExtractor;
	
	public ExemplarHandler(Function<T, String> codigoExtractor) {
		this.codigoExtractor = codigoExtractor;
	}
	
	@Override
	public void handle(T data) {
		String codigoExemplar;

        if (data instanceof String s) {
        	codigoExemplar = s;
        } else {
        	codigoExemplar = codigoExtractor.apply(data);
        }
		
		if(codigoExemplar == null || codigoExemplar.isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Codigo do Exemplar é obrigatorio");
		}
		
		super.handle(data);
	}
}
