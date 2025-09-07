package br.edu.ifsp.biblioteca.controller;

import br.edu.ifsp.biblioteca.model.CategoriaLivro;
import br.edu.ifsp.biblioteca.service.CategoriaLivroService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/library/catalogos/categorias-livro")
public class CategoriaLivroController {

    private final CategoriaLivroService categoriaLivroService;

    public CategoriaLivroController(CategoriaLivroService categoriaLivroService) {
        this.categoriaLivroService = categoriaLivroService;
    }

    public record CategoriaCreateDTO(@NotBlank String nome) {}

    @PostMapping
    public ResponseEntity<CategoriaLivro> criar(@Valid @RequestBody CategoriaCreateDTO categoriaDto) {
        CategoriaLivro novaCategoria = new CategoriaLivro(null, categoriaDto.nome());
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaLivroService.cadastrar(novaCategoria));
    }

    @GetMapping
    public ResponseEntity<List<CategoriaLivro>> listarTodos() {
        List<CategoriaLivro> categorias = categoriaLivroService.listarTodas();
        return ResponseEntity.status(HttpStatus.OK).body(categorias);
    }

    @GetMapping("/{nome}")
    public ResponseEntity<CategoriaLivro> consultarPorNome(@PathVariable String nome) {
        return ResponseEntity.status(HttpStatus.OK).body(categoriaLivroService.consultar(nome));
    }
}