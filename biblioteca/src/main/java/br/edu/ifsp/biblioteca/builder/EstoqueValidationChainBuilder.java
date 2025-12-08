package br.edu.ifsp.biblioteca.builder;

import org.springframework.stereotype.Component;

import br.edu.ifsp.biblioteca.dto.EstoqueCreateDto;
import br.edu.ifsp.biblioteca.handler.ExemplarHandler;
import br.edu.ifsp.biblioteca.handler.IsbnHandler;
import br.edu.ifsp.biblioteca.handler.ValidationHandler;

@Component
public class EstoqueValidationChainBuilder {
    public ValidationHandler<EstoqueCreateDto> buildEstoqueChain() {     
        ExemplarHandler<EstoqueCreateDto> handleCodigoExemplar = new ExemplarHandler<>(EstoqueCreateDto::getCodigoExemplar);
        IsbnHandler<EstoqueCreateDto> handleIsbn = new IsbnHandler<>(EstoqueCreateDto::getIsbn);
        
        handleCodigoExemplar.setNext(handleIsbn);
        return handleCodigoExemplar;
    }
}
