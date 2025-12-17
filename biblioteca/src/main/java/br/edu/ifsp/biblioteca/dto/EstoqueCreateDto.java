package br.edu.ifsp.biblioteca.dto;

import lombok.experimental.SuperBuilder;

@SuperBuilder
public class EstoqueCreateDto extends EstoqueBaseDto {
	private String isbn;
	
	public EstoqueCreateDto() {}
	
	public EstoqueCreateDto(String isbn, String codigoExemplar) {
		super(codigoExemplar);
		this.isbn = isbn;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
}
