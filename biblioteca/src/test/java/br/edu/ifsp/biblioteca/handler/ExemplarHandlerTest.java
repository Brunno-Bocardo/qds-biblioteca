package br.edu.ifsp.biblioteca.handler;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ExemplarHandlerTest {
	@Test
    void deveAceitarTipoString() {
		ExemplarHandler<String> handleExemplar = new ExemplarHandler<>(s -> s);
        assertDoesNotThrow(() -> handleExemplar.handle("EX12345"));
    }
    
	@Test
    void deveLancarExcecaoQuandoNulo() {
		ExemplarHandler<String> handleExemplar = new ExemplarHandler<>(s -> s);
		
		ResponseStatusException exception =
                assertThrows(ResponseStatusException.class,
                        () -> handleExemplar.handle(null));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Codigo do Exemplar é obrigatório", exception.getReason());
    }
	
	@Test
    void deveLancarExcecaoQuandoVazio() {
		ExemplarHandler<String> handleExemplar = new ExemplarHandler<>(s -> s);
		
		ResponseStatusException exception =
                assertThrows(ResponseStatusException.class,
                        () -> handleExemplar.handle(""));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Codigo do Exemplar é obrigatório", exception.getReason());
    }
}
