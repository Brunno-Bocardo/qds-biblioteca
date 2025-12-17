package br.edu.ifsp.biblioteca.handler;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import br.edu.ifsp.biblioteca.dto.UsuarioCreateDto;

public class NomeHandlerTest {
	@Test
    void deveAceitarObjetoComNomeValido() {

		NomeHandler<UsuarioCreateDto> handler =
                new NomeHandler<>(UsuarioCreateDto::getNome);

		UsuarioCreateDto dto = UsuarioCreateDto.builder()
				.nome("Flavia")
                .build();

        assertDoesNotThrow(() -> handler.handle(dto));
    }
	
	@Test
    void deveAceitarNomeValido() {
		String nome = "Flavia";

		NomeHandler<String> handler =
                new NomeHandler<>(s -> s);

        assertDoesNotThrow(() -> handler.handle(nome));
    }
	
	@Test
    void deveLancarExcecaoQuandoNulo() {
		String nome = null;

		NomeHandler<String> handler =
                new NomeHandler<>(s -> s);

		ResponseStatusException exception =
                assertThrows(ResponseStatusException.class,
                        () -> handler.handle(nome));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Nome é obrigatório", exception.getReason());
    }
	
	@Test
    void deveLancarExcecaoQuandoVazio() {
		String nome = "";

		NomeHandler<String> handler =
                new NomeHandler<>(s -> s);

		ResponseStatusException exception =
                assertThrows(ResponseStatusException.class,
                        () -> handler.handle(nome));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Nome é obrigatório", exception.getReason());
    }
}
