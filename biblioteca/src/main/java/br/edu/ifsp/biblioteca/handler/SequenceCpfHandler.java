package br.edu.ifsp.biblioteca.handler;

import java.util.function.Function;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class SequenceCpfHandler<T> extends BaseHandler<T> {
	
	private Function<T, String> cpfExtractor;
	
	public SequenceCpfHandler(Function<T, String> cpfExtractor) {
		this.cpfExtractor = cpfExtractor;
	}
	
	@Override
	public void handle(T data){	
		String cpf = cpfExtractor.apply(data);
		
        int somaDigitos = 0;
        int restoSoma = 0;
        int verificador = 0;
        
 
        for (int i = 0, peso = 10; i < 9; i++, peso--) {
            somaDigitos += (cpf.charAt(i) - '0') * peso;
        }
        
        restoSoma = somaDigitos % 11;
        if (restoSoma < 2) {
            verificador = 0;
        } else {
            verificador = (11 - restoSoma);
        }
        
        if (verificador != (cpf.charAt(9) - '0')) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CPF inválido");
        }
        

        somaDigitos = 0;
        for (int i = 0, peso = 11; i < 10; i++, peso--) {
            somaDigitos += (cpf.charAt(i) - '0') * peso;
        }
        
        restoSoma = somaDigitos % 11;
        if (restoSoma < 2) {
            verificador = 0;
        } else {
            verificador = (11 - restoSoma);
        }
        
        if (verificador != (cpf.charAt(10) - '0')) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CPF inválido");
        }
		
		super.handle(data);
	}
}
