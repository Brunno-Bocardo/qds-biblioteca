package br.edu.ifsp.biblioteca.dto;

public class UsuarioCreateDto {
	private String nome;
	private String cpf;
	private String email;
	private Integer categoriaId;
	private Integer cursoId;
	
	public UsuarioCreateDto(String nome, String cpf, String email, Integer categoriaId, Integer cursoId) {
		this.nome = nome;
		this.cpf = cpf;
		this.email = email;
		this.categoriaId = categoriaId;
		this.cursoId = cursoId;
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

	public Integer getCategoriaId() {
		return categoriaId;
	}

	public void setCategoriaId(Integer categoriaId) {
		this.categoriaId = categoriaId;
	}

	public Integer getCursoId() {
		return cursoId;
	}

	public void setCursoId(Integer cursoId) {
		this.cursoId = cursoId;
	}
	
	
}
