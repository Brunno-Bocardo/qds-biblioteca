package br.edu.ifsp.biblioteca.controller;

import br.edu.ifsp.biblioteca.dto.CategoriaLivroDto;
import br.edu.ifsp.biblioteca.model.CategoriaLivro;
import br.edu.ifsp.biblioteca.service.CategoriaLivroService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/library/catalogos/categorias-livro")
public class CategoriaLivroController {

    private final CategoriaLivroService categoriaLivroService;

    public CategoriaLivroController(CategoriaLivroService categoriaLivroService) {
        this.categoriaLivroService = categoriaLivroService;
    }

    @PostMapping
    public ResponseEntity<CategoriaLivroDto> criar(String nome) {
        CategoriaLivro novaCategoria = new CategoriaLivro(null, nome);
        CategoriaLivro categoriaSalva = categoriaLivroService.cadastrar(novaCategoria);
        CategoriaLivroDto respostaDto = new CategoriaLivroDto(categoriaSalva.getIdCategoriaLivro(),
                categoriaSalva.getNomeCategoriaLivro());
        return ResponseEntity.status(HttpStatus.CREATED).body(respostaDto);
    }

    @GetMapping
    public ResponseEntity<List<CategoriaLivroDto>> listarTodos() {
        List<CategoriaLivro> categorias = categoriaLivroService.listarTodas();
        List<CategoriaLivroDto> categoriasDto = categorias.stream()
                .map(categoria -> new CategoriaLivroDto(categoria.getIdCategoriaLivro(),
                        categoria.getNomeCategoriaLivro()))
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(categoriasDto);
    }

    @GetMapping("/{nome}")
    public ResponseEntity<CategoriaLivroDto> consultarPorNome(@PathVariable String nome) {
        CategoriaLivro categoriaEncontrada = categoriaLivroService.consultarPorNome(nome);
        CategoriaLivroDto categoriaDto = new CategoriaLivroDto(categoriaEncontrada.getIdCategoriaLivro(),
                categoriaEncontrada.getNomeCategoriaLivro());
        return ResponseEntity.status(HttpStatus.OK).body(categoriaDto);
    }
}