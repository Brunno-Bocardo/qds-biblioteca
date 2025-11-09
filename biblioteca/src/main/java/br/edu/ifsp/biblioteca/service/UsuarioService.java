package br.edu.ifsp.biblioteca.service;

import br.edu.ifsp.biblioteca.dto.UsuarioCreateDto;
import br.edu.ifsp.biblioteca.factory.UsuarioValidationChainFactory;
import br.edu.ifsp.biblioteca.handler.ValidationHandler;
import br.edu.ifsp.biblioteca.model.CategoriaUsuario;
import br.edu.ifsp.biblioteca.model.Curso;
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
	private final ValidationHandler<UsuarioCreateDto> handlerChain;
    private final UsuarioRepository usuarioRepository;
    private final CategoriaUsuarioRepository categoriaRepository;
    private final CursoRepository cursoRepository;
    
    public UsuarioService(UsuarioRepository usuarioRepository, CategoriaUsuarioRepository categoriaRepository, CursoRepository cursoRepository) {
        this.handlerChain = UsuarioValidationChainFactory.createUsuarioChain();
    	this.usuarioRepository = usuarioRepository;
        this.categoriaRepository = categoriaRepository;
        this.cursoRepository = cursoRepository;
    }  

    public Usuario criarUsuario(UsuarioCreateDto usuario) {
    	handlerChain.handle(usuario);

        if (usuarioRepository.existsByCpf(usuario.getCpf())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "CPF já cadastrado");
        }
        
        if (usuarioRepository.existsByEmail(usuario.getEmail()) ) {
        	throw new ResponseStatusException(HttpStatus.NOT_FOUND, "E-mail já cadastrado");
        }

        CategoriaUsuario categoriaUsuario = categoriaRepository.findById(usuario.getCategoriaId()).orElse(null);
        if (categoriaUsuario == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "CategoriaUsuario não encontrada");
        }

        Curso curso = cursoRepository.findById(usuario.getCursoId()).orElse(null);
        if (curso == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso não encontrado");
        }
        
        Usuario novoUsuario = new Usuario();
        novoUsuario.setNomeUsuario(usuario.getNome());
        novoUsuario.setCpf(usuario.getCpf());
        novoUsuario.setEmail(usuario.getEmail());
        novoUsuario.setCategoria(categoriaUsuario);
        novoUsuario.setCurso(curso);
        novoUsuario.setStatus(StatusUsuario.ATIVO);

        return usuarioRepository.save(novoUsuario);
    }
    
    public List<Usuario>listarUsuarios(){
		return usuarioRepository.findAll();
	}
    
    public Usuario procurarPorCpf(String cpf){
		return usuarioRepository.findByCpf(cpf).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Não foi localizado nenhum usuário com este CPF."));
	}
    
    public void validarDadosDuplicados(Integer idAtual, String novoCpf, String novoEmail) {
        if (novoCpf != null && !novoCpf.isBlank()) {
            if (usuarioRepository.existsByCpfAndIdUsuarioNot(novoCpf, idAtual)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe outro usuário com este CPF.");
            }
        }
        if (novoEmail != null && !novoEmail.isBlank()) {
            if (usuarioRepository.existsByEmailAndIdUsuarioNot(novoEmail.trim(), idAtual)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe outro usuário com este e-mail.");
            }
        }
    }
    
    @Transactional
    public Usuario atualizarUsuario(String cpf, UsuarioCreateDto novosDadosUsuario) {
    	handlerChain.handle(novosDadosUsuario);
        Usuario usuarioAtual = procurarPorCpf(cpf);
        String novoCpf   = novosDadosUsuario.getCpf();
        String novoEmail = novosDadosUsuario.getEmail();
        
        validarDadosDuplicados(usuarioAtual.getIdUsuario(), novoCpf, novoEmail);
        
        usuarioAtual.setNomeUsuario(novosDadosUsuario.getNome());
        usuarioAtual.setEmail(novoEmail.trim());
        usuarioAtual.setCpf(novoCpf);

        if (novosDadosUsuario.getCategoriaId() > 0) {
            var categoriaUsuario = categoriaRepository.findById(novosDadosUsuario.getCategoriaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "CategoriaUsuario não encontrada."));
            usuarioAtual.setCategoria(categoriaUsuario);
        }

        if (novosDadosUsuario.getCursoId() > 0) {
            var curso = cursoRepository.findById(novosDadosUsuario.getCursoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso não encontrado."));
            usuarioAtual.setCurso(curso);
        }

        return usuarioRepository.save(usuarioAtual);
    }
    
    @Transactional
	public void deletarUsuario(String cpf) {
    	procurarPorCpf(cpf);
    	usuarioRepository.deleteByCpf(cpf);
	}
    
    @Transactional
    public Usuario alterarStatusUsuario(Integer idUsuario, Usuario.StatusUsuario novoStatus) {
        Usuario usuario = usuarioRepository.findById(idUsuario).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado."));
        usuario.setStatus(novoStatus);
        return usuarioRepository.save(usuario);
    }

}
