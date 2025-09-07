package br.edu.ifsp.biblioteca.service;

import org.springframework.stereotype.Service;

import br.edu.ifsp.biblioteca.model.Estoque;
import br.edu.ifsp.biblioteca.repository.EmprestimoRepository;
import br.edu.ifsp.biblioteca.model.Livro;
import br.edu.ifsp.biblioteca.repository.EstoqueRepository;
import br.edu.ifsp.biblioteca.repository.LivroRepository;

import java.util.*;

@Service
public class EstoqueService {

    private final EstoqueRepository estoqueRepository;
    private final LivroRepository livroRepository;

    public EstoqueService(EstoqueRepository estoqueRepository, LivroRepository livroRepository) {
        this.estoqueRepository = estoqueRepository;
        this.livroRepository = livroRepository;
    }

    public Estoque cadastrarExemplar(String isbn, String codigoExemplar) {
        Livro livro = livroRepository.findByIsbn(isbn)
                .orElseThrow(() -> new RuntimeException("Livro não encontrado"));

        Estoque estoque = new Estoque();
        estoque.setLivro(livro);
        estoque.setCodigoExemplar(codigoExemplar);
        estoque.setDisponivel(true);

        return estoqueRepository.save(estoque);
    }

    public List<Estoque> listarExemplares() {
        return estoqueRepository.findAll();
    }

    public Optional<Estoque> buscarPorCodigo(String codigoExemplar) {
        return estoqueRepository.findByCodigoExemplar(codigoExemplar);
    }

    public Estoque atualizarDisponibilidade(String codigoExemplar, boolean disponivel) {
        Estoque estoque = estoqueRepository.findByCodigoExemplar(codigoExemplar)
                .orElseThrow(() -> new RuntimeException("Exemplar não encontrado"));

        estoque.setDisponivel(disponivel);
        return estoqueRepository.save(estoque);
    }

    public void removerExemplar(String codigoExemplar) {
        Estoque estoque = estoqueRepository.findByCodigoExemplar(codigoExemplar)
                .orElseThrow(() -> new RuntimeException("Exemplar não encontrado"));

        if (!estoque.isDisponivel()) {
            throw new RuntimeException("Exemplar não pode ser removido, está emprestado");
        }

        estoqueRepository.delete(estoque);
    }
}

