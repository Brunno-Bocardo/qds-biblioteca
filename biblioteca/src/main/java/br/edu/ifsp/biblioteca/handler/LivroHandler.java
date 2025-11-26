package br.edu.ifsp.biblioteca.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import br.edu.ifsp.biblioteca.dto.LivroCreateDto;

public class LivroHandler extends BaseHandler<LivroCreateDto> {

    @Override
    public void handle(LivroCreateDto livroDto) {
        if (livroDto.getTitulo() == null || livroDto.getTitulo().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O titulo é obrigatório");
        }
        if (livroDto.getAutor() == null || livroDto.getAutor().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O autor é obrigatório");
        }
        if (livroDto.getEditora() == null || livroDto.getEditora().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O editora é obrigatório");
        }
        if (livroDto.getEdicao() == null || livroDto.getEdicao().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O edição é obrigatório");
        }
        if (livroDto.getCategoriaId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O categoria é obrigatório");
        }
    }
}
