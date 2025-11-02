package br.edu.ifsp.biblioteca.dto;

public class EstoqueDisponibilidadeDto {
	Boolean disponivel;
	
	public EstoqueDisponibilidadeDto(Boolean disponivel) {
		this.disponivel = disponivel;
	}

	public Boolean getDisponivel() {
		return disponivel;
	}

	public void setDisponivel(Boolean disponivel) {
		this.disponivel = disponivel;
	}
	
}
