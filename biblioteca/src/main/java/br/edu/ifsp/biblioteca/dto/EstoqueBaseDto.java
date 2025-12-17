package br.edu.ifsp.biblioteca.dto;

import lombok.experimental.SuperBuilder;

@SuperBuilder
public abstract class EstoqueBaseDto {
	private String codigoExemplar;

	public EstoqueBaseDto() {}
	
	public EstoqueBaseDto(String codigoExemplar) {
		this.codigoExemplar = codigoExemplar;
	}

	public String getCodigoExemplar() {
		return codigoExemplar;
	}

	public void setCodigoExemplar(String codigoExemplar) {
		this.codigoExemplar = codigoExemplar;
	}
}
