package br.edu.ifsp.biblioteca.dto;

import lombok.experimental.SuperBuilder;

@SuperBuilder
public class UsuarioCreateDto extends UsuarioBaseDto {
	private Integer categoriaId;
	private Integer cursoId;
	
	public UsuarioCreateDto() {
		super();
	}

	public UsuarioCreateDto(String nome, String cpf, String email, Integer categoriaId, Integer cursoId) {
		super(nome, cpf, email);
		this.categoriaId = categoriaId;
		this.cursoId = cursoId;
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
