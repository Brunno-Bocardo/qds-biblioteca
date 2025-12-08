package br.edu.ifsp.biblioteca.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import br.edu.ifsp.biblioteca.dto.UsuarioCreateDto;

public class UsuarioHandler extends BaseHandler<UsuarioCreateDto> {
	@Override
	public void handle(UsuarioCreateDto usuarioDto) {
        if (usuarioDto.getEmail() == null || usuarioDto.getEmail().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "E-mail é obrigatório");
        }
        
        if (usuarioDto.getCategoriaId() == null || usuarioDto.getCategoriaId() == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoria é obrigatório");
        }
        
        if (usuarioDto.getCursoId() == null || usuarioDto.getCursoId() == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Curso é obrigatório");
        }
        
        super.handle(usuarioDto);
	}
}
