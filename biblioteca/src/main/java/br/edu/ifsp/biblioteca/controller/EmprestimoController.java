package br.edu.ifsp.biblioteca.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import br.edu.ifsp.biblioteca.model.Emprestimo;
import br.edu.ifsp.biblioteca.service.EmprestimoService;


@RestController
@Validated
@RequestMapping("/library/emprestimo")

public class EmprestimoController {
	
	private final EmprestimoService emprestimoService;
	
	public EmprestimoController(EmprestimoService serviceEmprestimo) {
		this.emprestimoService = serviceEmprestimo;
	}
	
	public record EmprestimoCreateDTO(
			 String cpf,
			 String codigoExemplar
	) {}
	
	@PostMapping
	public ResponseEntity<Emprestimo> registrarEmprestimo(@RequestBody EmprestimoCreateDTO emprestimo) {
		Emprestimo emprestimoCriado = emprestimoService.registrarEmprestimo(emprestimo.cpf(), emprestimo.codigoExemplar());
		return ResponseEntity.status(HttpStatus.OK).body(emprestimoCriado);
	}
	
	@GetMapping
	public ResponseEntity<List<Emprestimo>> exibirEmprestimos() {
		return ResponseEntity.status(HttpStatus.OK).body(emprestimoService.listarEmprestimos());
	}
	
	@PutMapping(value = "/{id}/devolucao")
	public ResponseEntity<Emprestimo> registrarDevolucaoEmprestimo(@PathVariable int id){
		Emprestimo emprestimoAtualizado = emprestimoService.registrarDevolucaoEmprestimo(id);
		return ResponseEntity.status(HttpStatus.OK).body(emprestimoAtualizado);
	}
}
