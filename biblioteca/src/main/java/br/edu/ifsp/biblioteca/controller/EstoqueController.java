package br.edu.ifsp.biblioteca.controller;

import org.springframework.web.bind.annotation.*;

import br.edu.ifsp.biblioteca.model.Estoque;
import br.edu.ifsp.biblioteca.service.EstoqueService;
import org.springframework.http.ResponseEntity;
import java.util.*;

@RestController
@RequestMapping("/api/estoque")

public class EstoqueController {

    private final EstoqueService estoqueService;

    public EstoqueController(EstoqueService estoqueService) {
        this.estoqueService = estoqueService;
    }

    @PostMapping
    public ResponseEntity<Estoque> cadastrarExemplar(@RequestParam String isbn,
                                                     @RequestParam String codigoExemplar) {
        return ResponseEntity.ok(estoqueService.cadastrarExemplar(isbn, codigoExemplar));
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
    public ResponseEntity<Estoque> atualizarDisponibilidade(@PathVariable String codigoExemplar,
                                                            @RequestParam boolean disponivel) {
        return ResponseEntity.ok(estoqueService.atualizarDisponibilidade(codigoExemplar, disponivel));
    }

    @DeleteMapping("/{codigoExemplar}")
    public ResponseEntity<Void> removerExemplar(@PathVariable String codigoExemplar) {
        estoqueService.removerExemplar(codigoExemplar);
        return ResponseEntity.noContent().build();
    }
}
