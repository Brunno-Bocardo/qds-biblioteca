package br.edu.ifsp.biblioteca.service;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import br.edu.ifsp.biblioteca.model.Curso;
import br.edu.ifsp.biblioteca.repository.CursoRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class CursoService {

    private final CursoRepository cursoRepository;

    public CursoService(CursoRepository cursoRepository) {
        this.cursoRepository = cursoRepository;
    }

    public Curso criarCurso(String nomeCurso) {
        validarNome(nomeCurso);
        if (cursoRepository.existsByNomeCursoIgnoreCase(nomeCurso)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe outro curso com esse nome.");
        }
        Curso curso = new Curso();
        curso.setNomeCurso(nomeCurso.trim());
        return cursoRepository.save(curso);
    }

    public List<Curso> listarCursos() {
        return cursoRepository.findAll();
    }

    public Curso procurarCursoPorId(Integer idCurso) {
        return cursoRepository.findById(idCurso).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso não encontrado."));
    }

    public Curso atualizarCursoPorId(Integer idCurso, String novoNome) {
        Curso cursoAtual = procurarCursoPorId(idCurso);
        validarNome(novoNome);
        if (cursoRepository.existsByNomeCursoIgnoreCaseAndIdCursoNot(novoNome, idCurso)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe outro curso com esse nome.");
        }
        cursoAtual.setNomeCurso(novoNome.trim());
        return cursoRepository.save(cursoAtual);
    }

    public void deletarCursoPorId(Integer idCurso) {
        Curso cursoAtual = procurarCursoPorId(idCurso);
        // TODO: checar se existem users vinculados
        cursoRepository.delete(cursoAtual);
    }

    private void validarNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "nomeCurso é obrigatório");
        }
    }
}
