package br.edu.ifsp.biblioteca.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifsp.biblioteca.dto.CategoriaUsuarioDto;
import br.edu.ifsp.biblioteca.model.CategoriaUsuario;
import br.edu.ifsp.biblioteca.service.CategoriaUsuarioService;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/library/catalogos/categorias-usuario")
public class CategoriaUsuarioController {
	
	public enum CategoriaTipo {
	    aluno,
	    professor
	}

	private final CategoriaUsuarioService categoriaUsuarioService;

	public CategoriaUsuarioController(CategoriaUsuarioService categoriaUsuarioService) {
		this.categoriaUsuarioService = categoriaUsuarioService;
	}

	@PostMapping
	public ResponseEntity<CategoriaUsuarioDto> criarCategoriaUsuario(@RequestParam CategoriaTipo tipo) {
	    CategoriaUsuario novaCategoria = new CategoriaUsuario(null, tipo.name());
	    CategoriaUsuario categoriaSalva = categoriaUsuarioService.cadastrarCategoriaUsuario(novaCategoria);

	    CategoriaUsuarioDto respostaDto = new CategoriaUsuarioDto(
	            categoriaSalva.getIdCategoriaUsuario(),
	            categoriaSalva.getNomeCategoriaUsuario()
	    );

	    return ResponseEntity.status(HttpStatus.CREATED).body(respostaDto);
	}

	@GetMapping
	public ResponseEntity<List<CategoriaUsuarioDto>> listarTodos() {
		List<CategoriaUsuario> categorias = categoriaUsuarioService.listarTodas();
		List<CategoriaUsuarioDto> categoriasDto = categorias.stream()
				.map(categoria -> new CategoriaUsuarioDto(categoria.getIdCategoriaUsuario(),
						categoria.getNomeCategoriaUsuario()))
				.collect(Collectors.toList());
		return ResponseEntity.status(HttpStatus.OK).body(categoriasDto);
	}

	@GetMapping("/{nome}")
	public ResponseEntity<CategoriaUsuarioDto> consultarPorNome(@PathVariable String nome) {
		CategoriaUsuario categoriaEncontrada = categoriaUsuarioService.consultar(nome);
		CategoriaUsuarioDto categoriaDto = new CategoriaUsuarioDto(categoriaEncontrada.getIdCategoriaUsuario(),
				categoriaEncontrada.getNomeCategoriaUsuario());
		return ResponseEntity.status(HttpStatus.OK).body(categoriaDto);
	}
}
