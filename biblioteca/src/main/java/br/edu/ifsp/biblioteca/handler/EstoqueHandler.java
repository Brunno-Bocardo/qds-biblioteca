package br.edu.ifsp.biblioteca.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import br.edu.ifsp.biblioteca.dto.EstoqueCreateDto;

public class EstoqueHandler extends BaseHandler<EstoqueCreateDto> {

    @Override
    public void handle(EstoqueCreateDto estoqueDto) {
        if (estoqueDto.getCodigoExemplar() == null || estoqueDto.getCodigoExemplar().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O código do exemplar é obrigatório");
        }
        
        if (estoqueDto.getIsbn() == null || estoqueDto.getIsbn().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O ISBN é obrigatório para vincular o exemplar");
        }

        super.handle(estoqueDto);
    }
}