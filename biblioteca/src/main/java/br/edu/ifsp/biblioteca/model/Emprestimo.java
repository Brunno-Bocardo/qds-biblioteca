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
	
	public Estoque getEstoque() {
		return estoque;
	}
	
	public LocalDate getDataEmprestimo() {
		return dataEmprestimo;
	}
	
	public LocalDate getDataPrevistaDevolucao() {
		return dataPrevistaDevolucao;
	}
	
	public LocalDate getDataDevolucao() {
		return dataDevolucao;
	}
	
	public void setDataDevolucao(LocalDate dataDevolucao) {
		this.dataDevolucao = dataDevolucao;
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
