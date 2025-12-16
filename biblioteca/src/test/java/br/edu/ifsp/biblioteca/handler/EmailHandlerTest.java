package br.edu.ifsp.biblioteca.handler;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import br.edu.ifsp.biblioteca.dto.UsuarioCreateDto;

public class EmailHandlerTest {
	@Test
    void deveAceitarEmailValido() {

		EmailHandler<UsuarioCreateDto> handler =
                new EmailHandler<>(UsuarioCreateDto::getEmail);

		UsuarioCreateDto dto = UsuarioCreateDto.builder()
				.email("flavia@gmail.com")
                .build();

        assertDoesNotThrow(() -> handler.handle(dto));
    }
	
	@Test
    void deveLancarExcecaoQuandoNull() {

		EmailHandler<UsuarioCreateDto> handler =
                new EmailHandler<>(UsuarioCreateDto::getEmail);

		UsuarioCreateDto dto = UsuarioCreateDto.builder()
				.email(null)
                .build();

		ResponseStatusException exception =
                assertThrows(ResponseStatusException.class,
                        () -> handler.handle(dto));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("E-mail inválido", exception.getReason());
    }
	
	@Test
    void deveLancarExcecaoQuandoVazio() {

		EmailHandler<UsuarioCreateDto> handler =
                new EmailHandler<>(UsuarioCreateDto::getEmail);

		UsuarioCreateDto dto = UsuarioCreateDto.builder()
				.email("")
                .build();

		ResponseStatusException exception =
                assertThrows(ResponseStatusException.class,
                        () -> handler.handle(dto));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("E-mail inválido", exception.getReason());
    }
}

