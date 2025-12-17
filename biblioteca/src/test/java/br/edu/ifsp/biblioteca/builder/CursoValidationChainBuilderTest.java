package br.edu.ifsp.biblioteca.builder;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import br.edu.ifsp.biblioteca.handler.NomeHandler;
import br.edu.ifsp.biblioteca.handler.ValidationHandler;

public class CursoValidationChainBuilderTest {
	@Test
	void deveCriarCadeiaDeValidacao() {
		CursoValidationChainBuilder builder = new CursoValidationChainBuilder();
	       ValidationHandler<String> handler =
	                builder.buildNomeChain();
	       
	    assertNotNull(handler);
	    assertTrue(handler instanceof NomeHandler);
	}
}
