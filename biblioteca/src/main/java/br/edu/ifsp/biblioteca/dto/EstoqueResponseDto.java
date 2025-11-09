package br.edu.ifsp.biblioteca.dto;

import br.edu.ifsp.biblioteca.model.Livro;

public class EstoqueResponseDto extends EstoqueBaseDto {
	private int idEstoque;
	private Livro livro;
	private boolean disponivel = true;
	
	public EstoqueResponseDto() {}
	
	public EstoqueResponseDto(int idEstoque, String codigoExemplar, Livro livro, boolean disponivel) {
		super(codigoExemplar);
		this.idEstoque = idEstoque;
		this.livro = livro;
		this.disponivel = disponivel;
	}
	public int getIdEstoque() {
		return idEstoque;
	}
	
	public void setIdEstoque(int idEstoque) {
		this.idEstoque = idEstoque;
	}

	public Livro getLivro() {
		return livro;
	}
	
	public void setLivro(Livro livro) {
		this.livro = livro;
	}
	
	public boolean isDisponivel() {
		return disponivel;
	}
	
	public void setDisponivel(boolean disponivel) {
		this.disponivel = disponivel;
	}
}
