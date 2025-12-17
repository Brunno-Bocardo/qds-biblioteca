package br.edu.ifsp.biblioteca.handler;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import br.edu.ifsp.biblioteca.dto.LivroCreateDto;

public class LivroHandlerTest {
	@Test
    void deveAceitarEmailValido() {

		LivroHandler handler = new LivroHandler();

		LivroCreateDto dto = LivroCreateDto.builder()
				.titulo("Assassinato no Expresso Do Oriente")
				.autor("Agatha Christie")
				.categoriaId(1)
				.edicao("ED1")
				.editora("HarperCollins")
                .build();

        assertDoesNotThrow(() -> handler.handle(dto));
    }
	
	@Test
    void deveLancarExcecaoEmTitulo() {

		LivroHandler handler = new LivroHandler();

		LivroCreateDto dto = LivroCreateDto.builder()
				.titulo(null)
                .build();

		ResponseStatusException exception =
                assertThrows(ResponseStatusException.class,
                        () -> handler.handle(dto));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("O titulo é obrigatório", exception.getReason());
    }
	
	@Test
    void deveLancarExcecaoEmTituloVazio() {

		LivroHandler handler = new LivroHandler();

		LivroCreateDto dto = LivroCreateDto.builder()
				.titulo("")
                .build();

		ResponseStatusException exception =
                assertThrows(ResponseStatusException.class,
                        () -> handler.handle(dto));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("O titulo é obrigatório", exception.getReason());
    }
	
	@Test
    void deveLancarExcecaoEmAutor() {

		LivroHandler handler = new LivroHandler();

		LivroCreateDto dto = LivroCreateDto.builder()
				.titulo("Assassinato no Expresso Do Oriente")
				.autor(null)
                .build();

		ResponseStatusException exception =
                assertThrows(ResponseStatusException.class,
                        () -> handler.handle(dto));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("O autor é obrigatório", exception.getReason());
    }
	
	@Test
    void deveLancarExcecaoEmAutorVazio() {

		LivroHandler handler = new LivroHandler();

		LivroCreateDto dto = LivroCreateDto.builder()
				.titulo("Assassinato no Expresso Do Oriente")
				.autor("")
                .build();

		ResponseStatusException exception =
                assertThrows(ResponseStatusException.class,
                        () -> handler.handle(dto));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("O autor é obrigatório", exception.getReason());
    }
	
	@Test
    void deveLancarExcecaoEmEditora() {

		LivroHandler handler = new LivroHandler();

		LivroCreateDto dto = LivroCreateDto.builder()
				.titulo("Assassinato no Expresso Do Oriente")
				.autor("Agatha Christie")
				.editora(null)
                .build();

		ResponseStatusException exception =
                assertThrows(ResponseStatusException.class,
                        () -> handler.handle(dto));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("A editora é obrigatória", exception.getReason());
    }
	
	@Test
    void deveLancarExcecaoEmEditoraVazia() {

		LivroHandler handler = new LivroHandler();

		LivroCreateDto dto = LivroCreateDto.builder()
				.titulo("Assassinato no Expresso Do Oriente")
				.autor("Agatha Christie")
				.editora("")
                .build();

		ResponseStatusException exception =
                assertThrows(ResponseStatusException.class,
                        () -> handler.handle(dto));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("A editora é obrigatória", exception.getReason());
    }
	
	@Test
    void deveLancarExcecaoEmEdicao() {

		LivroHandler handler = new LivroHandler();

		LivroCreateDto dto = LivroCreateDto.builder()
				.titulo("Assassinato no Expresso Do Oriente")
				.autor("Agatha Christie")
				.editora("HarperCollins")
				.edicao(null)
                .build();

		ResponseStatusException exception =
                assertThrows(ResponseStatusException.class,
                        () -> handler.handle(dto));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("A edição é obrigatória", exception.getReason());
    }
	
	@Test
    void deveLancarExcecaoEmEdicaoVazia() {

		LivroHandler handler = new LivroHandler();

		LivroCreateDto dto = LivroCreateDto.builder()
				.titulo("Assassinato no Expresso Do Oriente")
				.autor("Agatha Christie")
				.editora("HarperCollins")
				.edicao("")
                .build();

		ResponseStatusException exception =
                assertThrows(ResponseStatusException.class,
                        () -> handler.handle(dto));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("A edição é obrigatória", exception.getReason());
    }
	
	@Test
    void deveLancarExcecaoEmCategoria() {

		LivroHandler handler = new LivroHandler();

		LivroCreateDto dto = LivroCreateDto.builder()
				.titulo("Assassinato no Expresso Do Oriente")
				.autor("Agatha Christie")
				.editora("HarperCollins")
				.edicao("ED1")
				.categoriaId(null)
                .build();

		ResponseStatusException exception =
                assertThrows(ResponseStatusException.class,
                        () -> handler.handle(dto));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("A categoria é obrigatória", exception.getReason());
    }
	
	@Test
    void deveLancarExcecaoEmCategoriaVazia() {

		LivroHandler handler = new LivroHandler();

		LivroCreateDto dto = LivroCreateDto.builder()
				.titulo("Assassinato no Expresso Do Oriente")
				.autor("Agatha Christie")
				.editora("HarperCollins")
				.edicao("ED1")
				.categoriaId(0)
                .build();

		ResponseStatusException exception =
                assertThrows(ResponseStatusException.class,
                        () -> handler.handle(dto));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("A categoria é obrigatória", exception.getReason());
    }
}
