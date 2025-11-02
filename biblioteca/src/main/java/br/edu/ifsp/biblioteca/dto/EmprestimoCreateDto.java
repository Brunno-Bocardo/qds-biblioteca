package br.edu.ifsp.biblioteca.dto;

public class EmprestimoCreateDto {
	private String cpf;
	private String codigoExemplar;
	
	EmprestimoCreateDto(String cpf, String codigoExemplar){
		this.cpf = cpf;
		this.codigoExemplar = codigoExemplar;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getCodigoExemplar() {
		return codigoExemplar;
	}

	public void setCodigoExemplar(String codigoExemplar) {
		this.codigoExemplar = codigoExemplar;
	}
}
