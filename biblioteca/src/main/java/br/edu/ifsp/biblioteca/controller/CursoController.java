package br.edu.ifsp.biblioteca.controller;

import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.edu.ifsp.biblioteca.model.Curso;
import br.edu.ifsp.biblioteca.service.CursoService;
import jakarta.validation.Valid;

@RestController
@Validated
@RequestMapping("/library/curso")
public class CursoController {
	private final CursoService cursoService;
	
	public CursoController(CursoService cursoService) {
		this.cursoService = cursoService;
	}
	
	public record CursoCreateDTO(
            String nomeCurso
    ) {}
	
	@PostMapping
    public ResponseEntity<Curso> criarCurso(@Valid @RequestBody CursoCreateDTO curso) {
		Curso cursoCriado = cursoService.criarCurso(curso.nomeCurso());
        return ResponseEntity.status(HttpStatus.CREATED).body(cursoCriado);
    }
	
	@GetMapping
	public ResponseEntity<List<Curso>> exibirCursos(){
		return ResponseEntity.status(HttpStatus.OK).body(cursoService.listarCursos());
	}
	
	@GetMapping(value = "/{idCurso}")
	public ResponseEntity<Curso> exibirCursoPorId(@PathVariable Integer idCurso){
		return ResponseEntity.status(HttpStatus.OK).body(cursoService.procurarCursoPorId(idCurso));
	}
	
	@PutMapping("/{idCurso}")
	public ResponseEntity<Curso> atualizarCurso(@PathVariable Integer idCurso, @RequestBody CursoCreateDTO body) {
	    Curso cursoAtualizado = cursoService.atualizarCursoPorId(idCurso, body.nomeCurso());
	    return ResponseEntity.ok(cursoAtualizado);
	}
    
	@DeleteMapping("/{idCurso}")
    public ResponseEntity<Map<String, String>> deletarCurso(@PathVariable Integer idCurso) {
        cursoService.deletarCursoPorId(idCurso);
        return ResponseEntity.ok(Map.of("message", "Curso deletado!"));
    }
    
}
