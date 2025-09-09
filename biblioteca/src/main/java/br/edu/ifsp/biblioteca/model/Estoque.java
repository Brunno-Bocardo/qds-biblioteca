package br.edu.ifsp.biblioteca.model;

import jakarta.persistence.*;

@Entity
@Table(name = "estoque")
public class Estoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idEstoque;

    @Column(nullable = false, unique = true)
    private String codigoExemplar;

    @ManyToOne
    @JoinColumn(name = "livro_id", nullable = false)
    private Livro livro;

    @Column(nullable = false)
    private boolean disponivel = true;

    // Getters e Setters
    public int getId() {
        return idEstoque;
    }

    public void setId(int id) {
        this.idEstoque = id;
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
