package br.edu.ifsp.biblioteca.dto;

import java.time.LocalDate;

import br.edu.ifsp.biblioteca.model.Estoque;
import br.edu.ifsp.biblioteca.model.Usuario;

public class EmprestimoDto {
	public int idEmprestimo;
	private Usuario usuario;
	private Estoque estoque;
	private LocalDate dataEmprestimo;
	private LocalDate dataPrevistaDevolucao;
	private LocalDate dataDevolucao;
	private LocalDate dataSuspensao;
	
	public EmprestimoDto(int idEmprestimo, Usuario usuario, Estoque estoque, LocalDate dataEmprestimo,
			LocalDate dataPrevistaDevolucao, LocalDate dataDevolucao, LocalDate dataSuspensao) {
		this.idEmprestimo = idEmprestimo;
		this.usuario = usuario;
		this.estoque = estoque;
		this.dataEmprestimo = dataEmprestimo;
		this.dataPrevistaDevolucao = dataPrevistaDevolucao;
		this.dataDevolucao = dataDevolucao;
		this.dataSuspensao = dataSuspensao;
	}
	public EmprestimoDto() {
	}

	public int getIdEmprestimo() {
		return idEmprestimo;
	}
	public void setIdEmprestimo(int idEmprestimo) {
		this.idEmprestimo = idEmprestimo;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public Estoque getEstoque() {
		return estoque;
	}
	public void setEstoque(Estoque estoque) {
		this.estoque = estoque;
	}
	public LocalDate getDataEmprestimo() {
		return dataEmprestimo;
	}
	public void setDataEmprestimo(LocalDate dataEmprestimo) {
		this.dataEmprestimo = dataEmprestimo;
	}
	public LocalDate getDataPrevistaDevolucao() {
		return dataPrevistaDevolucao;
	}
	public void setDataPrevistaDevolucao(LocalDate dataPrevistaDevolucao) {
		this.dataPrevistaDevolucao = dataPrevistaDevolucao;
	}
	public LocalDate getDataDevolucao() {
		return dataDevolucao;
	}
	public void setDataDevolucao(LocalDate dataDevolucao) {
		this.dataDevolucao = dataDevolucao;
	}
	public LocalDate getDataSuspensao() {
		return dataSuspensao;
	}
	public void setDataSuspensao(LocalDate dataSuspensao) {
		this.dataSuspensao = dataSuspensao;
	}
	
	
}
