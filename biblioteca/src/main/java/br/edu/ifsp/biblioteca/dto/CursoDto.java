package br.edu.ifsp.biblioteca.dto;

public class CursoDto {
	private int id;
    private String nome;
    		
	public CursoDto(int id, String nome) {
		this.id = id;
		this.nome = nome;
	}
	
	public CursoDto() {}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
}
