package br.edu.ifsp.biblioteca.builder;

import br.edu.ifsp.biblioteca.dto.EstoqueCreateDto;
import br.edu.ifsp.biblioteca.handler.ExemplarHandler;
import br.edu.ifsp.biblioteca.handler.IsbnHandler;
import br.edu.ifsp.biblioteca.handler.ValidationHandler;

public class EstoqueValidationChainBuilder {
    public ValidationHandler<EstoqueCreateDto> buildEstoqueChain() {     
        ExemplarHandler<EstoqueCreateDto> handleCodigoExemplar = new ExemplarHandler<>(EstoqueCreateDto::getCodigoExemplar);
        IsbnHandler<EstoqueCreateDto> handleIsbn = new IsbnHandler<>(EstoqueCreateDto::getIsbn);
        
        handleCodigoExemplar.setNext(handleIsbn);
        return handleCodigoExemplar;
    }
}
