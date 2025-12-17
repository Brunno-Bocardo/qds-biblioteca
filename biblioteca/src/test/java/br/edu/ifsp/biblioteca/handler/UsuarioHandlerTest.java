package br.edu.ifsp.biblioteca.handler;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import br.edu.ifsp.biblioteca.dto.UsuarioCreateDto;

public class UsuarioHandlerTest {
	@Test
	void deveAceitarUsuarioValido() {
		UsuarioHandler handler = new UsuarioHandler();
		
		UsuarioCreateDto dto = UsuarioCreateDto.builder()
				.email("flavia@gmail.com")
				.categoriaId(1)
				.cursoId(1)
                .build();
		
		assertDoesNotThrow(() -> handler.handle(dto));
	}
	
	@Test
	void deveLancarEmailExcecao() {
		UsuarioHandler handler = new UsuarioHandler();
		
		UsuarioCreateDto dto = UsuarioCreateDto.builder()
				.email(null)
                .build();
		
		ResponseStatusException exception =
                assertThrows(ResponseStatusException.class,
                        () -> handler.handle(dto));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("E-mail é obrigatório", exception.getReason());
	}
	
	@Test
	void deveLancarEmailExcecaoVazio() {
		UsuarioHandler handler = new UsuarioHandler();
		
		UsuarioCreateDto dto = UsuarioCreateDto.builder()
				.email("")
                .build();
		
		ResponseStatusException exception =
                assertThrows(ResponseStatusException.class,
                        () -> handler.handle(dto));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("E-mail é obrigatório", exception.getReason());
	}
	
	@Test
	void deveLancarCategoriaExcecao() {
		UsuarioHandler handler = new UsuarioHandler();
		
		UsuarioCreateDto dto = UsuarioCreateDto.builder()
				.email("flavia@gmail.com")
				.categoriaId(null)
                .build();
		
		ResponseStatusException exception =
                assertThrows(ResponseStatusException.class,
                        () -> handler.handle(dto));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Categoria é obrigatório", exception.getReason());
	}
	
	@Test
	void deveLancarCategoriaExcecaoVazio() {
		UsuarioHandler handler = new UsuarioHandler();
		
		UsuarioCreateDto dto = UsuarioCreateDto.builder()
				.email("flavia@gmail.com")
				.categoriaId(0)
                .build();
		
		ResponseStatusException exception =
                assertThrows(ResponseStatusException.class,
                        () -> handler.handle(dto));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Categoria é obrigatório", exception.getReason());
	}
	
	@Test
	void deveLancarCursoExcecao() {
		UsuarioHandler handler = new UsuarioHandler();
		
		UsuarioCreateDto dto = UsuarioCreateDto.builder()
				.email("flavia@gmail.com")
				.categoriaId(1)
				.cursoId(null)
                .build();
		
		ResponseStatusException exception =
                assertThrows(ResponseStatusException.class,
                        () -> handler.handle(dto));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Curso é obrigatório", exception.getReason());
	}
	
	@Test
	void deveLancarCursoExcecaoVazio() {
		UsuarioHandler handler = new UsuarioHandler();
		
		UsuarioCreateDto dto = UsuarioCreateDto.builder()
				.email("flavia@gmail.com")
				.categoriaId(1)
				.cursoId(0)
                .build();
		
		ResponseStatusException exception =
                assertThrows(ResponseStatusException.class,
                        () -> handler.handle(dto));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Curso é obrigatório", exception.getReason());
	}
}
