package br.edu.ifsp.biblioteca.dto;

import br.edu.ifsp.biblioteca.model.CategoriaUsuario;
import br.edu.ifsp.biblioteca.model.Curso;

public class UsuarioResponseDto extends UsuarioBaseDto {
	private int idUsuario;
	public String nome;
	public String cpf;
	public String email;
	private CategoriaUsuario categoria;
	private Curso curso;
	public UsuarioResponseDto(int idUsuario, String nome, String cpf, String email, CategoriaUsuario categoria,
			Curso curso) {
		super(nome, cpf, email);
		this.idUsuario = idUsuario;
		this.categoria = categoria;
		this.curso = curso;
	}
	public UsuarioResponseDto() {
		super();
	}
	public int getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
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

