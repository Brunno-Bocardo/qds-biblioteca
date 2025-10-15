package br.edu.ifsp.biblioteca.dto;

public class CategoriaUsuarioDto {
	private Integer idCategoriaUsuario;
    private String nomeCategoriaUsuario;

    public CategoriaUsuarioDto( Integer idCategoriaUsuario, String nomeCategoriaUsuario) {
    	this.idCategoriaUsuario = idCategoriaUsuario;
    	this.nomeCategoriaUsuario = nomeCategoriaUsuario;  
    }

    public CategoriaUsuarioDto() {
    };

    public Integer getIdCategoriaUsuario() {
		return idCategoriaUsuario;
	}

	public void setIdCategoriaUsuario(Integer idCategoriaUsuario) {
		this.idCategoriaUsuario = idCategoriaUsuario;
	}

	public String getNomeCategoriaUsuarioo() {
        return nomeCategoriaUsuario;
    }

    public void setNomeCategoriaUsuario(String nomeCategoriaUsuario) {
        this.nomeCategoriaUsuario = nomeCategoriaUsuario;
    }

}