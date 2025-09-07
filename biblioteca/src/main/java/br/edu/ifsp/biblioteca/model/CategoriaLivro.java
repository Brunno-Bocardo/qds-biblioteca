package br.edu.ifsp.biblioteca.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;

@Entity
public class CategoriaLivro {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idCategoriaLivro;

	@NotBlank
	private String nomeCategoriaLivro;
	
	@OneToMany(mappedBy = "categoria")
	private List<Livro> livrosRelacionados;

	public CategoriaLivro() {};
	
	public CategoriaLivro(Integer idCategoriaLivro, String nomeCategoriaLivro) {
		this.idCategoriaLivro = idCategoriaLivro;
		this.nomeCategoriaLivro = nomeCategoriaLivro;
	}
	
	// ====================================================
    //                   getters e setters
    // ====================================================
    
	
	public Integer getIdCategoriaLivro() {
		return idCategoriaLivro;
	}
	
	public void setIdCategoriaLivro(int idCategoriaLivro) {
		this.idCategoriaLivro = idCategoriaLivro;
	}
	
	public String getNomeCategoriaLivro() {
		return nomeCategoriaLivro;
	}
	
	public void setNomeCategoriaLivro(String nomeCategoriaLivro) {
		this.nomeCategoriaLivro = nomeCategoriaLivro;
	}
	
}

