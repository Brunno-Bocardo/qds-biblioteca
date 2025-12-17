package br.edu.ifsp.biblioteca.handler;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import br.edu.ifsp.biblioteca.dto.EstoqueCreateDto;

public class IsbnHandlerTest {
	@Test
    void deveAceitarObjetoComIsbnValido() {

		IsbnHandler<EstoqueCreateDto> handler =
                new IsbnHandler<>(EstoqueCreateDto::getIsbn);

		EstoqueCreateDto dto = EstoqueCreateDto.builder()
				.isbn("1234-8765")
                .build();

        assertDoesNotThrow(() -> handler.handle(dto));
    }
	
	@Test
    void deveAceitarIsbnValido() {
		
		String isbn = "1234-8765";
		
		IsbnHandler<String> handler =
                new IsbnHandler<>(s -> s);

        assertDoesNotThrow(() -> handler.handle(isbn));
    }
	
	@Test
    void deveLancarExcecaoQuandoNulo() {
		
		IsbnHandler<String> handler =
                new IsbnHandler<>(s -> s);

		ResponseStatusException exception =
                assertThrows(ResponseStatusException.class,
                        () -> handler.handle(null));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("O ISBN é obrigatório", exception.getReason());
    }
	
	@Test
    void deveLancarExcecaoQuandoVazio() {
		
		IsbnHandler<String> handler =
                new IsbnHandler<>(s -> s);

		ResponseStatusException exception =
                assertThrows(ResponseStatusException.class,
                        () -> handler.handle(""));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("O ISBN é obrigatório", exception.getReason());
    }
	
}
