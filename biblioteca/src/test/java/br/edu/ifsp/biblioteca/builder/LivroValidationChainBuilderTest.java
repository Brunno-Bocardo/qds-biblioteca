package br.edu.ifsp.biblioteca.builder;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import br.edu.ifsp.biblioteca.dto.LivroCreateDto;
import br.edu.ifsp.biblioteca.handler.IsbnHandler;
import br.edu.ifsp.biblioteca.handler.LivroHandler;
import br.edu.ifsp.biblioteca.handler.ValidationHandler;

public class LivroValidationChainBuilderTest {
	@Test
	void deveCriarCadeiaDeValidacao() {
		LivroValidationChainBuilder builder = new LivroValidationChainBuilder();
		
	    ValidationHandler<LivroCreateDto> handler =
	                builder.buildLivroChain();
	       
	       assertNotNull(handler);
	       assertTrue(handler instanceof LivroHandler);
	       
	       ValidationHandler<LivroCreateDto> nextHandlerIsbn =
	                ((ValidationHandler<LivroCreateDto>) handler).getNext();

	        assertNotNull(nextHandlerIsbn);
	        assertTrue(nextHandlerIsbn instanceof IsbnHandler);
	}
	
	@Test
	void deveCriarIsbnHandler() {
		LivroValidationChainBuilder builder = new LivroValidationChainBuilder();
		
	    ValidationHandler<String> handler =
	                builder.buildIsbnChain();
	       
	       assertNotNull(handler);
	       assertTrue(handler instanceof IsbnHandler);
	}
}
