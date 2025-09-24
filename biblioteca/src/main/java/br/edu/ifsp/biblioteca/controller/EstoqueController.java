package br.edu.ifsp.biblioteca.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import br.edu.ifsp.biblioteca.model.Estoque;
import br.edu.ifsp.biblioteca.service.EstoqueService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@RestController
@Validated
@RequestMapping("/library/estoque")
public class EstoqueController {

    private final EstoqueService estoqueService;

    public EstoqueController(EstoqueService estoqueService) {
        this.estoqueService = estoqueService;
    }

    // DTO para criação de exemplar
    public record EstoqueCreateDTO(
            @NotBlank String isbn,
            @NotBlank String codigoExemplar
    ) {}

    // DTO para atualização de disponibilidade
    public record EstoqueDisponibilidadeDTO(
            @NotNull Boolean disponivel
    ) {}

    @PostMapping
    public ResponseEntity<Estoque> cadastrarExemplar(@Valid @RequestBody EstoqueCreateDTO dto) {
        Estoque estoque = estoqueService.cadastrarExemplar(dto.isbn(), dto.codigoExemplar());
        return ResponseEntity.status(HttpStatus.CREATED).body(estoque);
    }

    @GetMapping
    public ResponseEntity<List<Estoque>> listarExemplares() {
        return ResponseEntity.ok(estoqueService.listarExemplares());
    }

    @GetMapping("/{codigoExemplar}")
    public ResponseEntity<Estoque> buscarPorCodigo(@PathVariable String codigoExemplar) {
        return ResponseEntity.ok(estoqueService.buscarPorCodigo(codigoExemplar));
    }

    @PutMapping("/{codigoExemplar}")
    public ResponseEntity<Estoque> atualizarDisponibilidade(
            @PathVariable String codigoExemplar,
            @Valid @RequestBody EstoqueDisponibilidadeDTO dto
    ) {
        Estoque atualizado = estoqueService.atualizarDisponibilidade(codigoExemplar, dto.disponivel());
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{codigoExemplar}")
    public ResponseEntity<Void> removerExemplar(@PathVariable String codigoExemplar) {
        estoqueService.removerExemplar(codigoExemplar);
        return ResponseEntity.noContent().build();
    }
}
