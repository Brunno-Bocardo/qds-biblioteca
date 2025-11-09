package br.edu.ifsp.biblioteca.dto;

import br.edu.ifsp.biblioteca.model.CategoriaLivro;

public class LivroResponseDto extends LivroBaseDto {
	private String isbn;
	private CategoriaLivro categoria;
	
	public LivroResponseDto() {}
	
	public LivroResponseDto(String isbn, String titulo, String autor, String editora, String edicao, CategoriaLivro categoria) {
		super(titulo, autor, editora, edicao);
		this.isbn = isbn;
		this.categoria = categoria;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public CategoriaLivro getCategoria() {
		return categoria;
	}

	public void setCategoria(CategoriaLivro categoria) {
		this.categoria = categoria;
	}
}
