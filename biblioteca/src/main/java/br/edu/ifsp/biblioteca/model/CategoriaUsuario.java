package br.edu.ifsp.biblioteca.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;

@Entity
public class CategoriaUsuario {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCategoriaUsuario;

    @NotBlank
    private String nomeCategoriaUsuario;
    
    public CategoriaUsuario() {}
    
    public CategoriaUsuario(Integer idCategoriaUsuario, String nomeCategoriaUsuario) {
    	this.idCategoriaUsuario = idCategoriaUsuario;
		this.nomeCategoriaUsuario = nomeCategoriaUsuario;
	}
    
    // ====================================================
    //                   getters e setters
    // ====================================================
    

	public Integer getIdCategoriaUsuario() {
        return idCategoriaUsuario;
    }

    public void setIdCategoriaUsuario(int idCategoriaUsuario) {
        this.idCategoriaUsuario = idCategoriaUsuario;
    }

    public String getNomeCategoriaUsuario() {
        return this.nomeCategoriaUsuario;
    }

    public void setNomeCategoriaUsuario(String nomeCategoriaUsuario) {
        this.nomeCategoriaUsuario = nomeCategoriaUsuario;
    }
}
