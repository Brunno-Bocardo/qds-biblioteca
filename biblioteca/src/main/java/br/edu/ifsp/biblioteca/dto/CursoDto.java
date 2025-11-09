package br.edu.ifsp.biblioteca.dto;

public class CursoDto extends CursoCreateDto {
	private int id;
    		
	public CursoDto() {
		super();
	}
	
	public CursoDto(int id, String nome) {
		super(nome);
		this.id = id;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
