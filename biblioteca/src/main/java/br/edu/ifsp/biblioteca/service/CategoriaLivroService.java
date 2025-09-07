package br.edu.ifsp.biblioteca.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.edu.ifsp.biblioteca.model.CategoriaLivro;
import br.edu.ifsp.biblioteca.repository.CategoriaLivroRepository;
import jakarta.transaction.Transactional;

@Service
public class CategoriaLivroService {
	
	private final CategoriaLivroRepository categoriaLivroRepository;
	
	public CategoriaLivroService(CategoriaLivroRepository categoriaRepository) {
		this.categoriaLivroRepository = categoriaRepository;		
	}
	
	@Transactional 
	public CategoriaLivro cadastrar(CategoriaLivro categoria) {
		if(categoriaLivroRepository.existsByNomeCategoriaLivro(categoria.getNomeCategoriaLivro())) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe uma categoria com este nome");
		}
		return categoriaLivroRepository.save(categoria);
	}
	
	public CategoriaLivro consultar(String categoria) {
		return categoriaLivroRepository.findByNomeCategoriaLivro(categoria).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"A categoria informada não consta no sistema"));
	}
	
	public List<CategoriaLivro> listarTodas() {
        return categoriaLivroRepository.findAll();
    }
}
