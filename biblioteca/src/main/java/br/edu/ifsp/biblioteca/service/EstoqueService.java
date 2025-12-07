package br.edu.ifsp.biblioteca.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.edu.ifsp.biblioteca.dto.EstoqueCreateDto;
import br.edu.ifsp.biblioteca.factory.EstoqueValidationChainFactory; // Import Novo
import br.edu.ifsp.biblioteca.handler.ValidationHandler; // Import Novo
import br.edu.ifsp.biblioteca.model.Estoque;
import br.edu.ifsp.biblioteca.model.Livro;
import br.edu.ifsp.biblioteca.repository.EstoqueRepository;
import br.edu.ifsp.biblioteca.repository.LivroRepository;

@Service
public class EstoqueService {

    private final EstoqueRepository estoqueRepository;
    private final LivroRepository livroRepository;
    private final EstoqueValidationChainFactory estoqueValidation; 

    public EstoqueService(EstoqueRepository estoqueRepository, LivroRepository livroRepository, EstoqueValidationChainFactory estoqueValidation) {
        this.estoqueRepository = estoqueRepository;
        this.livroRepository = livroRepository;
        this.estoqueValidation = estoqueValidation;
    }

    public Estoque cadastrarExemplar(EstoqueCreateDto createDto) {
        estoqueValidation.createEstoqueChain().handle(createDto);

        Livro livro = livroRepository.findByIsbn(createDto.getIsbn())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Livro não encontrado para o ISBN informado"));
        
        if (estoqueRepository.findByCodigoExemplar(createDto.getCodigoExemplar()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um exemplar com este código");
        }

        Estoque estoque = new Estoque();
        estoque.setLivro(livro);
        estoque.setCodigoExemplar(createDto.getCodigoExemplar());
        estoque.setDisponivel(true);

        return estoqueRepository.save(estoque);
    }
    
    public List<Estoque> listarExemplares() {
        return estoqueRepository.findAll();
    }

    public Estoque buscarPorCodigo(String codigoExemplar) {
        return estoqueRepository.findByCodigoExemplar(codigoExemplar).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Não foi possível localizar exemplar."));
    }

    public Estoque atualizarDisponibilidade(String codigoExemplar, boolean disponivel) {
        Estoque estoque = estoqueRepository.findByCodigoExemplar(codigoExemplar)
                .orElseThrow(() -> new RuntimeException("Exemplar não encontrado"));

        estoque.setDisponivel(disponivel);
        return estoqueRepository.save(estoque);
    }
    
    public void removerExemplar(String codigoExemplar) {
        Estoque estoque = estoqueRepository.findByCodigoExemplar(codigoExemplar)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Exemplar não encontrado"));

        if (!estoque.isDisponivel()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Exemplar não pode ser removido pois está emprestado");
        }

        estoqueRepository.delete(estoque);
    }
}