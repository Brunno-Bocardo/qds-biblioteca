package br.edu.ifsp.biblioteca.dto;

import br.edu.ifsp.biblioteca.model.CategoriaUsuario;
import br.edu.ifsp.biblioteca.model.Curso;

public class UsuarioDto {
	private int idUsuario;
	private String nomeUsuario;
	private String cpf;
	private String email;
	private CategoriaUsuario categoria;
	private Curso curso;
	public UsuarioDto(int idUsuario, String nomeUsuario, String cpf, String email, CategoriaUsuario categoria,
			Curso curso) {
		this.idUsuario = idUsuario;
		this.nomeUsuario = nomeUsuario;
		this.cpf = cpf;
		this.email = email;
		this.categoria = categoria;
		this.curso = curso;
	}
	public UsuarioDto() {
	}
	public int getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}
	public String getNomeUsuario() {
		return nomeUsuario;
	}
	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
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
	public CategoriaUsuario getCategoria() {
		return categoria;
	}
	public void setCategoria(CategoriaUsuario categoria) {
		this.categoria = categoria;
	}
	public Curso getCurso() {
		return curso;
	}
	public void setCurso(Curso curso) {
		this.curso = curso;
	}
	
	
	
}

