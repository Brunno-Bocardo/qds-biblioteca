package br.edu.ifsp.biblioteca.dto;

import lombok.experimental.SuperBuilder;

@SuperBuilder
public abstract class UsuarioBaseDto {
	public String nome;
	public String cpf;
	public String email;
	
	public UsuarioBaseDto() {}

	public UsuarioBaseDto(String nome, String cpf, String email) {
		this.nome = nome;
		this.cpf = cpf;
		this.email = email;
	}
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
}
