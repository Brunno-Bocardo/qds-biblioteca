package br.edu.ifsp.biblioteca.factory;

import org.springframework.stereotype.Component;

import br.edu.ifsp.biblioteca.dto.EstoqueCreateDto;
import br.edu.ifsp.biblioteca.handler.EstoqueHandler;
import br.edu.ifsp.biblioteca.handler.IsbnHandler;
import br.edu.ifsp.biblioteca.handler.ValidationHandler;

@Component
public class EstoqueValidationChainFactory {

    public ValidationHandler<EstoqueCreateDto> createEstoqueChain() {
        EstoqueHandler handleAtributos = new EstoqueHandler();
        
        // Reaproveitando o handler de ISBN existente, passando o getter do DTO
        IsbnHandler<EstoqueCreateDto> handleIsbn = new IsbnHandler<>(EstoqueCreateDto::getIsbn);
        
        handleAtributos.setNext(handleIsbn);
        return handleAtributos;
    }
}