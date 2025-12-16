package br.edu.ifsp.biblioteca.handler;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import br.edu.ifsp.biblioteca.dto.EmprestimoCreateDto;

public class CpfHandlerTest {
	@Test
    void deveAceitarCpfValido() {

        CpfHandler<EmprestimoCreateDto> handler =
                new CpfHandler<>(EmprestimoCreateDto::getCpf);

        EmprestimoCreateDto dto = EmprestimoCreateDto.builder()
                .cpf("49667797074")
                .build();

        assertDoesNotThrow(() -> handler.handle(dto));
    }
	
    @Test
    void deveLancarExcecaoQuandoCpfNulo() {

        CpfHandler<EmprestimoCreateDto> handler =
                new CpfHandler<>(EmprestimoCreateDto::getCpf);
 
        EmprestimoCreateDto dto = EmprestimoCreateDto.builder()
                .cpf(null)
                .build();

        ResponseStatusException exception =
                assertThrows(ResponseStatusException.class,
                        () -> handler.handle(dto));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("CPF é obrigatório", exception.getReason());
    }
    
    @Test
    void deveLancarExcecaoQuandoCpfVazio() {

        CpfHandler<EmprestimoCreateDto> handler =
                new CpfHandler<>(EmprestimoCreateDto::getCpf);
 
        EmprestimoCreateDto dto = EmprestimoCreateDto.builder()
                .cpf("")
                .build();

        ResponseStatusException exception =
                assertThrows(ResponseStatusException.class,
                        () -> handler.handle(dto));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("CPF é obrigatório", exception.getReason());
    }

	
    @Test
    void deveAceitarTipoString() {
        CpfHandler<String> handleCpf = new CpfHandler<>(s -> s);
        assertDoesNotThrow(() -> handleCpf.handle("49667797074"));
    }
    
    @Test
    void deveLancarExcecaoComSequencia() {
        CpfHandler<String> handleCpf = new CpfHandler<>(s -> s);
        
        ResponseStatusException exception =
                assertThrows(ResponseStatusException.class,
                        () -> handleCpf.handle("12345"));
        
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("CPF inválido. Ele deve conter 11 dígitos e não pode ser uma repetição.", exception.getReason());
    }
}
