package br.edu.ifsp.biblioteca.dto;

public class EstoqueCreateDto {
	private String isbn;
	private String codigoExemplar;
	
	public EstoqueCreateDto(String isbn, String codigoExemplar) {
		this.isbn = isbn;
		this.codigoExemplar = codigoExemplar;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getCodigoExemplar() {
		return codigoExemplar;
	}

	public void setCodigoExemplar(String codigoExemplar) {
		this.codigoExemplar = codigoExemplar;
	}
	
	
}
