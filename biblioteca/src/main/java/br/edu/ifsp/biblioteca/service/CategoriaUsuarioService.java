package br.edu.ifsp.biblioteca.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import br.edu.ifsp.biblioteca.model.CategoriaUsuario;
import br.edu.ifsp.biblioteca.repository.CategoriaUsuarioRepository;
import jakarta.transaction.Transactional;

@Service
public class CategoriaUsuarioService {
	
	private final CategoriaUsuarioRepository categoriaUsuarioRepository;
	
	public CategoriaUsuarioService(CategoriaUsuarioRepository categoriaRepository) {
		this.categoriaUsuarioRepository = categoriaRepository;
	}
	
	@Transactional
	public CategoriaUsuario cadastrarCategoriaUsuario(CategoriaUsuario categoriaUsuario) {
		if(categoriaUsuarioRepository.existsByNomeCategoriaUsuario(categoriaUsuario.getNomeCategoriaUsuario())) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe uma categoria usuário com este nome");
		}
		return categoriaUsuarioRepository.save(categoriaUsuario);
	}
	public CategoriaUsuario consultarPorNome(String categoria) {
		return categoriaUsuarioRepository.findByNomeCategoriaUsuario(categoria).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "A categoria informada não consta no sistema"));
	}
	
	public CategoriaUsuario consultarPorId(Integer categoria) {
		return categoriaUsuarioRepository.findById(categoria).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "A categoria informada não consta no sistema"));
	}
	
	public List<CategoriaUsuario> listarTodas() {
		return categoriaUsuarioRepository.findAll();
	}

}
