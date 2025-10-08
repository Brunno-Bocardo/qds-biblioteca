package br.edu.ifsp.biblioteca.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifsp.biblioteca.model.CategoriaUsuario;
import br.edu.ifsp.biblioteca.model.CategoriaUsuarioDto;
import br.edu.ifsp.biblioteca.service.CategoriaUsuarioService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("/library/catalogos/categorias-usuario")
public class CategoriaUsuarioController {
	
	private final CategoriaUsuarioService categoriaUsuarioService;
	
	public CategoriaUsuarioController(CategoriaUsuarioService categoriaUsuarioService) {
		this.categoriaUsuarioService = categoriaUsuarioService;
	}
	
	@PostMapping
	public ResponseEntity<CategoriaUsuario> criarCategoriaUsuario(CategoriaUsuarioDto categoriaDto) {
		CategoriaUsuario novaCategoria = new CategoriaUsuario(null, categoriaDto.getNome());
		return ResponseEntity.status(HttpStatus.CREATED).body(categoriaUsuarioService.cadastrarCategoriaUsuario(novaCategoria));
	}
	
	@GetMapping
	public ResponseEntity<List<CategoriaUsuario>> listarTodos() {
		List<CategoriaUsuario> categorias = categoriaUsuarioService.listarTodas();
		return ResponseEntity.status(HttpStatus.OK).body(categorias);
	}
	@GetMapping("/{nome}")
    public ResponseEntity<CategoriaUsuario> consultarPorNome(@PathVariable String nome) {
        return ResponseEntity.status(HttpStatus.OK).body(categoriaUsuarioService.consultar(nome));
    }
}
