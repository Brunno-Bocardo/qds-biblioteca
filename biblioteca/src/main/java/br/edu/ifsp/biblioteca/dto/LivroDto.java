package br.edu.ifsp.biblioteca.dto;

import br.edu.ifsp.biblioteca.model.CategoriaLivro;

public class LivroDto {
	private String isbn;
	private String titulo;
	private String autor;
	private String editora;
	private String edicao;
	private CategoriaLivro categoria;
	
	public LivroDto() {
		
	}
	
	public LivroDto(String isbn, String titulo, String autor, String editora, String edicao, CategoriaLivro categoria) {
		this.isbn = isbn;
		this.titulo = titulo;
		this.autor = autor;
		this.editora = editora;
		this.edicao = edicao;
		this.categoria = categoria;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public String getEditora() {
		return editora;
	}

	public void setEditora(String editora) {
		this.editora = editora;
	}

	public String getEdicao() {
		return edicao;
	}

	public void setEdicao(String edicao) {
		this.edicao = edicao;
	}

	public CategoriaLivro getCategoria() {
		return categoria;
	}

	public void setCategoria(CategoriaLivro categoria) {
		this.categoria = categoria;
	}
	
}
