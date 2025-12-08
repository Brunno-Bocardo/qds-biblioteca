package br.edu.ifsp.biblioteca.builder;

import org.springframework.stereotype.Component;

import br.edu.ifsp.biblioteca.handler.NomeHandler;
import br.edu.ifsp.biblioteca.handler.ValidationHandler;

@Component
public class CursoValidationChainBuilder {
	public ValidationHandler<String> buildNomeChain() {
		return new NomeHandler<>(s->s);
	}
}
