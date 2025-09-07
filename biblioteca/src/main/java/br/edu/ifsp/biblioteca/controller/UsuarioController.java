// br.edu.ifsp.biblioteca.controller.UsuarioController
package br.edu.ifsp.biblioteca.controller;

import br.edu.ifsp.biblioteca.model.Livro;
import br.edu.ifsp.biblioteca.model.Usuario;
import br.edu.ifsp.biblioteca.service.UsuarioService;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping("/library/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService service) {
        this.usuarioService = service;
    }

    public record UsuarioCreateDTO(
            String nomeUsuario,
            String cpf,
            String email,
            Integer categoriaId,
            Integer cursoId
    ) {}

    @PostMapping
    public ResponseEntity<Usuario> criarUsuario(@Valid @RequestBody UsuarioCreateDTO in) {
        Usuario usuarioCriado = usuarioService.criarUsuario(in.nomeUsuario(), in.cpf(), in.email(), in.categoriaId(), in.cursoId());
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioCriado);
    }
    
    @GetMapping
	public ResponseEntity<List<Usuario>> exibirUsuarios(){
		return ResponseEntity.status(HttpStatus.OK).body(usuarioService.listarUsuarios());
	}
    
    @GetMapping(value = "/{cpf}")
	public ResponseEntity<Usuario> exibirUsuarioPorCpf(@PathVariable String cpf){
		return ResponseEntity.status(HttpStatus.OK).body(usuarioService.procurarPorCpf(cpf));
	}

    @PutMapping(value = "/{cpf}")
	public ResponseEntity<Usuario> atualizarUsuario(@PathVariable String cpf, @RequestBody Usuario usuario){
    	Usuario usuarioAtualizado = usuarioService.atualizarUsuario(cpf, usuario);
	    return ResponseEntity.status(HttpStatus.OK).body(usuarioAtualizado);
	}
    
}
