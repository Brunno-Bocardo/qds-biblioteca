package br.edu.ifsp.biblioteca.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import br.edu.ifsp.biblioteca.dto.UsuarioCreateDto;

public class UsuarioHandler extends BaseHandler<UsuarioCreateDto> {
	@Override
	public void handle(UsuarioCreateDto usuarioDto) {
		if (usuarioDto.getNome() == null || usuarioDto.getNome().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome do usuário é obrigatório");
        }
        
        if (usuarioDto.getEmail() == null || usuarioDto.getEmail().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "E-mail é obrigatório");
        }
        
        if (usuarioDto.getCategoriaId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoria é obrigatória");
        }
        
        if (usuarioDto.getCursoId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Curso é obrigatório");
        }
        
        super.handle(usuarioDto);
	}
}
