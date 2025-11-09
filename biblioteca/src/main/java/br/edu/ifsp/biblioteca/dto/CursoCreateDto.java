package br.edu.ifsp.biblioteca.dto;

public class CursoCreateDto {
	private String nome;
	
	public CursoCreateDto() {}
	
	public CursoCreateDto(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	
}
