package br.edu.ifsp.biblioteca.dto;

public abstract class LivroBaseDto {
	private String titulo;
	private String autor;
	private String editora;
	private String edicao;
	
	public LivroBaseDto() {}
	
	public LivroBaseDto(String titulo, String autor, String editora, String edicao) {
		this.titulo = titulo;
		this.autor = autor;
		this.editora = editora;
		this.edicao = edicao;
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
}
