// br.edu.ifsp.biblioteca.controller.UsuarioController
package br.edu.ifsp.biblioteca.controller;

import br.edu.ifsp.biblioteca.model.Usuario;
import br.edu.ifsp.biblioteca.service.UsuarioService;
import jakarta.validation.Valid;
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
    public ResponseEntity<Usuario> criar(@Valid @RequestBody UsuarioCreateDTO in) {
        Usuario salvo = usuarioService.criarUsuario(in.nomeUsuario(), in.cpf(), in.email(), in.categoriaId(), in.cursoId());
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }
}
