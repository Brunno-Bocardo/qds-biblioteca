package br.edu.ifsp.biblioteca.handler;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class SequenceCpfHandlerTest {
	@Test
    void deveAceitarCpfValido() {

		SequenceCpfHandler<String> handler =
                new SequenceCpfHandler<>(s -> s);

        assertDoesNotThrow(() -> handler.handle("49667797074"));
    }
	
	@Test
    void deveLancarExcecao() {

		SequenceCpfHandler<String> handler =
                new SequenceCpfHandler<>(s -> s);

		ResponseStatusException exception =
                assertThrows(ResponseStatusException.class,
                        () -> handler.handle("49667797075"));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("CPF inválido", exception.getReason());
    }
	
	@Test
    void deveLancarExcecaoVerificador() {

		SequenceCpfHandler<String> handler =
                new SequenceCpfHandler<>(s -> s);

		ResponseStatusException exception =
                assertThrows(ResponseStatusException.class,
                        () -> handler.handle("48301297071"));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("CPF inválido", exception.getReason());
    }
	
	@Test
    void deveLancarExcecaoRestoSoma() {

		SequenceCpfHandler<String> handler =
                new SequenceCpfHandler<>(s -> s);

		ResponseStatusException exception =
                assertThrows(ResponseStatusException.class,
                        () -> handler.handle("16899535008"));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("CPF inválido", exception.getReason());
    }
	
}
