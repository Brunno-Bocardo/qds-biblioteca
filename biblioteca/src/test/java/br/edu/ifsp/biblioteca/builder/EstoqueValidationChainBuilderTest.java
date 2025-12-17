package br.edu.ifsp.biblioteca.builder;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import br.edu.ifsp.biblioteca.dto.EstoqueCreateDto;
import br.edu.ifsp.biblioteca.handler.ExemplarHandler;
import br.edu.ifsp.biblioteca.handler.IsbnHandler;
import br.edu.ifsp.biblioteca.handler.ValidationHandler;

public class EstoqueValidationChainBuilderTest {
	@Test
	void deveCriarCadeiaDeValidacao() {
		EstoqueValidationChainBuilder builder = new EstoqueValidationChainBuilder();
		
	    ValidationHandler<EstoqueCreateDto> handler =
	                builder.buildEstoqueChain();
	       
	    assertNotNull(handler);
	    assertTrue(handler instanceof ExemplarHandler);
	       
	    ValidationHandler<EstoqueCreateDto> next =
	                ((ExemplarHandler<EstoqueCreateDto>) handler).getNext();

	    assertNotNull(next);
	    assertTrue(next instanceof IsbnHandler);
	}
}
