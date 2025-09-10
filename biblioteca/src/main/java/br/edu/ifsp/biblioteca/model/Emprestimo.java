package br.edu.ifsp.biblioteca.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Emprestimo {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int idEmprestimo;
	
	@ManyToOne
	@JoinColumn(name = "idUsuario")
	private Usuario usuario;
	
	@ManyToOne
	@JoinColumn(name = "idEstoque")
	private Estoque estoque;
	
	private LocalDate dataEmprestimo;
	private LocalDate dataPrevistaDevolucao;
	private LocalDate dataDevolucao;
	private LocalDate dataSuspensao;
	
	@Enumerated(EnumType.STRING)
	private StatusEmprestimo status;
	
	public Emprestimo() {}
	
	public Emprestimo(Usuario usuario, Estoque estoque, LocalDate dataEmprestimo, LocalDate dataPrevistaDevolucao) {
		this.usuario = usuario;
		this.estoque = estoque;
		this.dataEmprestimo = dataEmprestimo;
		this.dataPrevistaDevolucao = dataPrevistaDevolucao;
		this.status = StatusEmprestimo.ATIVO;
	}
	
    // ====================================================
    //                   Getters e Setters
    // ====================================================
	
	public int getId() {
		return idEmprestimo;
	}
	
	public Usuario getUsuario() {
		return usuario;
	}
	
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public void setEstoque(Estoque estoque) {
		this.estoque = estoque;
	}
	
	public Estoque getEstoque() {
		return estoque;
	}
	
	public void setDataEmprestimo(LocalDate dataEmprestimo) {
		this.dataEmprestimo = dataEmprestimo;
	}
	
	public LocalDate getDataEmprestimo() {
		return dataEmprestimo;
	}
	
	public void setDataPrevistaDevolucao(LocalDate dataPrevistaDevolucao) {
		this.dataPrevistaDevolucao = dataPrevistaDevolucao;
	}
	
	public LocalDate getDataPrevistaDevolucao() {
		return dataPrevistaDevolucao;
	}
	
	public void setDataDevolucao(LocalDate dataDevolucao) {
		this.dataDevolucao = dataDevolucao;
	}
	
	public LocalDate getDataDevolucao() {
		return dataDevolucao;
	}
	
	public void setDataSuspensao(LocalDate dataSuspensao) {
		this.dataSuspensao = dataSuspensao;
	}
	
	public LocalDate getDataSuspensao() {
		return dataSuspensao;
	}
	
	public void setStatus(StatusEmprestimo status) {
		this.status = status;
	}
	
    // ====================================================
    //                     enum auxiliar
    // ====================================================
	
	public enum StatusEmprestimo {
	    ATIVO,  
	    DEVOLVIDO,  
	    ATRASADO,    
	    SUSPENSO     
	}
}
