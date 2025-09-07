// br.edu.ifsp.biblioteca.service.UsuarioService
package br.edu.ifsp.biblioteca.service;

import br.edu.ifsp.biblioteca.model.CategoriaUsuario;
import br.edu.ifsp.biblioteca.model.Curso;
import br.edu.ifsp.biblioteca.model.Livro;
import br.edu.ifsp.biblioteca.model.Usuario;
import br.edu.ifsp.biblioteca.model.Usuario.StatusUsuario;
import br.edu.ifsp.biblioteca.repository.CategoriaUsuarioRepository;
import br.edu.ifsp.biblioteca.repository.CursoRepository;
import br.edu.ifsp.biblioteca.repository.UsuarioRepository;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final CategoriaUsuarioRepository categoriaRepository;
    private final CursoRepository cursoRepository;

    
    public UsuarioService(UsuarioRepository usuarioRepository, CategoriaUsuarioRepository categoriaRepository, CursoRepository cursoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.categoriaRepository = categoriaRepository;
        this.cursoRepository = cursoRepository;
    }
    

    public Usuario criarUsuario(String nomeUsuario, String cpf, String email, Integer categoriaId, Integer cursoId) {
        validarCpfFormato(cpf);

        if (usuarioRepository.existsByCpf(cpf)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "CPF já cadastrado");
        }

        CategoriaUsuario categoriaUsuario = categoriaRepository.findById(categoriaId).orElse(null);
        if (categoriaUsuario == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "CategoriaUsuario não encontrada");
        }

        Curso curso = cursoRepository.findById(cursoId).orElse(null);
        if (curso == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso não encontrado");
        }
        
        
        // TODO: validar outros pontos aqui
        
        
        Usuario novoUsuario = new Usuario();
        novoUsuario.setNomeUsuario(nomeUsuario);
        novoUsuario.setCpf(cpf);
        novoUsuario.setEmail(email);
        novoUsuario.setCategoria(categoriaUsuario);
        novoUsuario.setCurso(curso);
        novoUsuario.setStatus(StatusUsuario.ATIVO); // por padrão

        // o método 'save' vem do JpaRepository - ele em si já tem o CRUD
        return usuarioRepository.save(novoUsuario);
    }

    // TODO: implementar o resto da validação do CPF
    private void validarCpfFormato(String cpf) {
        if (cpf == null || !cpf.matches("\\d{11}")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CPF deve conter 11 dígitos numéricos");
        }
    }
    
    public List<Usuario>listarUsuarios(){
		return usuarioRepository.findAll();
	}
    
    public Usuario procurarPorCpf(String cpf){
		return usuarioRepository.findByCpf(cpf).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Não foi localizado nenhum usuário com este CPF."));
	}
}
