package br.edu.ifsp.biblioteca.dto;

import lombok.experimental.SuperBuilder;

@SuperBuilder
public class LivroCreateDto extends LivroBaseDto {
	private String isbn;
	private Integer categoriaId;

	public LivroCreateDto() {
		super();
	}

	public LivroCreateDto(String isbn, String titulo, String autor, String editora, String edicao,
			Integer categoriaId) {
		super(titulo, autor, editora, edicao);
		this.isbn = isbn;
		this.categoriaId = categoriaId;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public Integer getCategoriaId() {
		return categoriaId;
	}

	public void setCategoriaId(Integer categoriaId) {
		this.categoriaId = categoriaId;
	}

}
