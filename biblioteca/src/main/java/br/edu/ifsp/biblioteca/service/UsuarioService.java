package br.edu.ifsp.biblioteca.service;

import br.edu.ifsp.biblioteca.builder.UsuarioValidationChainBuilder;
import br.edu.ifsp.biblioteca.dto.UsuarioCreateDto;
import br.edu.ifsp.biblioteca.model.CategoriaUsuario;
import br.edu.ifsp.biblioteca.model.Curso;
import br.edu.ifsp.biblioteca.model.Usuario;
import br.edu.ifsp.biblioteca.model.Usuario.StatusUsuario;
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
	private final UsuarioValidationChainBuilder usuarioValidation;
    private final UsuarioRepository usuarioRepository;
    private final CategoriaUsuarioService categoriaService;
    private final CursoRepository cursoRepository;
    
    public UsuarioService(UsuarioRepository usuarioRepository, CategoriaUsuarioService categoriaService, CursoRepository cursoRepository, UsuarioValidationChainBuilder usuarioValidationChain) {
        this.usuarioValidation = usuarioValidationChain;
    	this.usuarioRepository = usuarioRepository;
        this.categoriaService = categoriaService;
        this.cursoRepository = cursoRepository;
    }  

    public Usuario criarUsuario(UsuarioCreateDto usuario) {
    	usuarioValidation.buildUsuarioChain().handle(usuario);

        if (usuarioRepository.existsByCpf(usuario.getCpf())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "CPF já cadastrado");
        }
        
        if (usuarioRepository.existsByEmail(usuario.getEmail()) ) {
        	throw new ResponseStatusException(HttpStatus.NOT_FOUND, "E-mail já cadastrado");
        }

        CategoriaUsuario categoriaUsuario = categoriaService.consultarPorId(usuario.getCategoriaId());
        if (categoriaUsuario == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria Usuario não encontrada");
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
    
    public boolean consultarPorCurso(Curso curso) {
    	return usuarioRepository.existsByCurso(curso);
    }
    
    public void validarDadosDuplicados(Integer idAtual, String novoCpf, String novoEmail) {
    	if (usuarioRepository.existsByCpfAndIdUsuarioNot(novoCpf, idAtual)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe outro usuário com este CPF.");
        }
    	if (usuarioRepository.existsByEmailAndIdUsuarioNot(novoEmail.trim(), idAtual)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe outro usuário com este e-mail.");
        }
    }
    
    @Transactional
    public Usuario atualizarUsuario(String cpf, UsuarioCreateDto novosDadosUsuario) {
    	usuarioValidation.buildCpfChain().handle(cpf);
    	usuarioValidation.buildUsuarioChain().handle(novosDadosUsuario);;
        Usuario usuarioAtual = procurarPorCpf(cpf);
        String novoCpf   = novosDadosUsuario.getCpf();
        String novoEmail = novosDadosUsuario.getEmail();
        
        validarDadosDuplicados(usuarioAtual.getIdUsuario(), novoCpf, novoEmail);
        
        usuarioAtual.setNomeUsuario(novosDadosUsuario.getNome());
        usuarioAtual.setEmail(novoEmail.trim());
        usuarioAtual.setCpf(novoCpf);

        if (novosDadosUsuario.getCategoriaId() > 0) {
        	var categoriaUsuario = categoriaService.consultarPorId(novosDadosUsuario.getCategoriaId());
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
