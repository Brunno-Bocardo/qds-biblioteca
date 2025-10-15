package br.edu.ifsp.biblioteca.dto;

public class CategoriaLivroDto {
	private Integer idCategoriaLivro;
    private String nomeCategoriaLivro;

    public CategoriaLivroDto( Integer idCategoriaLivro, String nomeCategoriaLivro) {
    	this.idCategoriaLivro = idCategoriaLivro;
    	this.nomeCategoriaLivro = nomeCategoriaLivro;  
    }

    public CategoriaLivroDto() {
    };

    public Integer getIdCategoriaLivro() {
		return idCategoriaLivro;
	}

	public void setIdCategoriaLivro(Integer idCategoriaLivro) {
		this.idCategoriaLivro = idCategoriaLivro;
	}

	public String getNomeCategoriaLivro() {
        return nomeCategoriaLivro;
    }

    public void setNomeCategoriaLivro(String nomeCategoriaLivro) {
        this.nomeCategoriaLivro = nomeCategoriaLivro;
    }

}
