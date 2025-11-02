package br.edu.ifsp.biblioteca.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import br.edu.ifsp.biblioteca.dto.EstoqueCreateDto;
import br.edu.ifsp.biblioteca.dto.EstoqueDisponibilidadeDto;
import br.edu.ifsp.biblioteca.model.Estoque;
import br.edu.ifsp.biblioteca.service.EstoqueService;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@Validated
@RequestMapping("/library/estoque")
public class EstoqueController {

    private final EstoqueService estoqueService;

    public EstoqueController(EstoqueService estoqueService) {
        this.estoqueService = estoqueService;
    }

    @PostMapping
    public ResponseEntity<Estoque> cadastrarExemplar(@Valid @RequestBody EstoqueCreateDto createDto) {
        Estoque estoque = estoqueService.cadastrarExemplar(createDto.getIsbn(), createDto.getCodigoExemplar());
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
            @Valid @RequestBody EstoqueDisponibilidadeDto disponibilidadeDto
    ) {
        Estoque atualizado = estoqueService.atualizarDisponibilidade(codigoExemplar, disponibilidadeDto.getDisponivel());
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{codigoExemplar}")
    public ResponseEntity<Void> removerExemplar(@PathVariable String codigoExemplar) {
        estoqueService.removerExemplar(codigoExemplar);
        return ResponseEntity.noContent().build();
    }
}
