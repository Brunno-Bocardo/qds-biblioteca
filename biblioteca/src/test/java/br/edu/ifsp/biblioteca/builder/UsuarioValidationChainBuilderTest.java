package br.edu.ifsp.biblioteca.builder;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import br.edu.ifsp.biblioteca.dto.UsuarioCreateDto;
import br.edu.ifsp.biblioteca.handler.CpfHandler;
import br.edu.ifsp.biblioteca.handler.EmailHandler;
import br.edu.ifsp.biblioteca.handler.NomeHandler;
import br.edu.ifsp.biblioteca.handler.SequenceCpfHandler;
import br.edu.ifsp.biblioteca.handler.UsuarioHandler;
import br.edu.ifsp.biblioteca.handler.ValidationHandler;

public class UsuarioValidationChainBuilderTest {
	@Test
	void deveCriarCadeiaDeValidacao() {
		UsuarioValidationChainBuilder builder = new UsuarioValidationChainBuilder();
		
	    ValidationHandler<UsuarioCreateDto> handler =
	                builder.buildUsuarioChain();
	       
	    assertNotNull(handler);
	    assertTrue(handler instanceof NomeHandler);
	       
	    ValidationHandler<UsuarioCreateDto> nextUsuarioHandler =
	                ((ValidationHandler<UsuarioCreateDto>) handler).getNext();

	    assertNotNull(nextUsuarioHandler);
	    assertTrue(nextUsuarioHandler instanceof UsuarioHandler);
	    
	    ValidationHandler<UsuarioCreateDto> nextCpfHandler =
                ((ValidationHandler<UsuarioCreateDto>) nextUsuarioHandler).getNext();
	    
	    assertNotNull(nextCpfHandler);
	    assertTrue(nextCpfHandler instanceof CpfHandler);
	    
	    ValidationHandler<UsuarioCreateDto> nextCpfSequenceHandler =
                ((ValidationHandler<UsuarioCreateDto>) nextCpfHandler).getNext();
	    
	    assertNotNull(nextCpfSequenceHandler);
	    assertTrue(nextCpfSequenceHandler instanceof SequenceCpfHandler);
	    
	    ValidationHandler<UsuarioCreateDto> nextEmailHandler =
                ((ValidationHandler<UsuarioCreateDto>) nextCpfSequenceHandler).getNext();
	    
	    assertNotNull(nextEmailHandler);
	    assertTrue(nextEmailHandler instanceof EmailHandler);
	}
	
	@Test
	void deveCriarCadeiaDeCpf() {
		UsuarioValidationChainBuilder builder = new UsuarioValidationChainBuilder();
		
	    ValidationHandler<String> handler =
	                builder.buildCpfChain();
	       
	    assertNotNull(handler);
	    assertTrue(handler instanceof CpfHandler);
	    
	    ValidationHandler<String> nextSequenceCpfHandler =
                ((ValidationHandler<String>) handler).getNext();
	    
	    assertNotNull(nextSequenceCpfHandler);
	    assertTrue(nextSequenceCpfHandler instanceof SequenceCpfHandler);
	}
}
