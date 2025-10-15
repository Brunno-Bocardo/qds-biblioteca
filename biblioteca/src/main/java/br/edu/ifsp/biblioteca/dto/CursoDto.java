package br.edu.ifsp.biblioteca.dto;

public class CursoDto {
	private int idCurso;
    private String nomeCurso;
    		
	public CursoDto(int idCurso, String nomeCurso) {
		this.idCurso = idCurso;
		this.nomeCurso = nomeCurso;
	}
	
	public CursoDto() {}
	
	public int getIdCurso() {
		return idCurso;
	}

	public void setIdCurso(int idCurso) {
		this.idCurso = idCurso;
	}

	public String getNomeCurso() {
		return nomeCurso;
	}

	public void setNomeCurso(String nomeCurso) {
		this.nomeCurso = nomeCurso;
	}
	
	
    
    
}
