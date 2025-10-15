package br.edu.ifsp.biblioteca.dto;

import br.edu.ifsp.biblioteca.model.Livro;

public class EstoqueDto {
	private int idEstoque;
	private String codigoExemplar;
	private Livro livro;
	private boolean disponivel = true;
	
	public EstoqueDto() {}
	
	
	
	public EstoqueDto(int idEstoque, String codigoExemplar, Livro livro, boolean disponivel) {
		this.idEstoque = idEstoque;
		this.codigoExemplar = codigoExemplar;
		this.livro = livro;
		this.disponivel = disponivel;
	}
	public int getIdEstoque() {
		return idEstoque;
	}
	public void setIdEstoque(int idEstoque) {
		this.idEstoque = idEstoque;
	}
	public String getCodigoExemplar() {
		return codigoExemplar;
	}
	public void setCodigoExemplar(String codigoExemplar) {
		this.codigoExemplar = codigoExemplar;
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
