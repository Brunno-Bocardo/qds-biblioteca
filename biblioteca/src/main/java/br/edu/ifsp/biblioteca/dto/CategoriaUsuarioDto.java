package br.edu.ifsp.biblioteca.model;

public class CategoriaUsuarioDto {
	private String nome;
	
	public CategoriaUsuarioDto(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
}
