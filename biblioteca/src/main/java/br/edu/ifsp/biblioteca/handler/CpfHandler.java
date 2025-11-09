package br.edu.ifsp.biblioteca.handler;

import java.util.function.Function;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CpfHandler<T> extends BaseHandler<T>{

	private Function<T, String> cpfExtractor;
	
	public CpfHandler(Function<T, String> cpfExtractor) {
		this.cpfExtractor = cpfExtractor;
	}
	
	@Override
	public void handle(T data){
		String cpf = cpfExtractor.apply(data);
				
		if (cpf == null || cpf.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CPF é obrigatório");
        }
		
		if (cpf == null || !cpf.matches("^(?!([0-9])\\1{10})\\d{11}$")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CPF inválido. Ele deve conter 11 dígitos e não pode ser uma repetição.");
        }
		
		super.handle(data);
	}
}
