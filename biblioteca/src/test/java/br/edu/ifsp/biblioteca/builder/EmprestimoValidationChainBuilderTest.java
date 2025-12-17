package br.edu.ifsp.biblioteca.builder;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import br.edu.ifsp.biblioteca.dto.EmprestimoCreateDto;
import br.edu.ifsp.biblioteca.handler.CpfHandler;
import br.edu.ifsp.biblioteca.handler.ExemplarHandler;
import br.edu.ifsp.biblioteca.handler.SequenceCpfHandler;
import br.edu.ifsp.biblioteca.handler.ValidationHandler;

public class EmprestimoValidationChainBuilderTest {
	@Test
	void deveCriarCadeiaDeValidacao() {
		EmprestimoValidationChainBuilder builder = new EmprestimoValidationChainBuilder();
		
	    ValidationHandler<EmprestimoCreateDto> handler =
	                builder.buildEmprestimoChain();
	       
	    assertNotNull(handler);
	    assertTrue(handler instanceof ExemplarHandler);
	       
	    ValidationHandler<EmprestimoCreateDto> nextCpfHandler =
	                ((ExemplarHandler<EmprestimoCreateDto>) handler).getNext();

	    assertNotNull(nextCpfHandler);
	    assertTrue(nextCpfHandler instanceof CpfHandler);
	    
	    ValidationHandler<EmprestimoCreateDto> nextSequenceCpfHandler =
                ((CpfHandler<EmprestimoCreateDto>) nextCpfHandler).getNext();
	    
	    assertNotNull(nextSequenceCpfHandler);
	    assertTrue(nextSequenceCpfHandler instanceof SequenceCpfHandler);

	}
}
