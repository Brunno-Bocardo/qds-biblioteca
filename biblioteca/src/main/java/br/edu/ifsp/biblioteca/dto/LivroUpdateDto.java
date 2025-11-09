package br.edu.ifsp.biblioteca.dto;

public class LivroUpdateDto extends LivroBaseDto {
	private Integer categoriaId;
	
	LivroUpdateDto(String titulo, String autor, String editora, String edicao, Integer categoriaId) {
		super(titulo, autor, editora, edicao);
		this.categoriaId = categoriaId;
	}

	public Integer getCategoriaId() {
		return categoriaId;
	}

	public void setCategoriaId(Integer categoriaId) {
		this.categoriaId = categoriaId;
	}
}
