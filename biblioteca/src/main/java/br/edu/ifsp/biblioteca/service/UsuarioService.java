package br.edu.ifsp.biblioteca.service;

import br.edu.ifsp.biblioteca.dto.UsuarioCreateDto;
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

    private final UsuarioRepository usuarioRepository;
    private final CategoriaUsuarioRepository categoriaRepository;
    private final CursoRepository cursoRepository;

    
    public UsuarioService(UsuarioRepository usuarioRepository, CategoriaUsuarioRepository categoriaRepository, CursoRepository cursoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.categoriaRepository = categoriaRepository;
        this.cursoRepository = cursoRepository;
    }
    

    public Usuario criarUsuario(UsuarioCreateDto usuario) {
    	
    	if (usuario.getNome() == null || usuario.getNome().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome do usuário é obrigatório");
        }
        if (usuario.getCpf() == null || usuario.getCpf().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CPF é obrigatório");
        }
        if (usuario.getEmail() == null || usuario.getEmail().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "E-mail é obrigatório");
        }
        if (usuario.getCategoriaId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoria é obrigatória");
        }
        if (usuario.getCursoId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Curso é obrigatório");
        }
        
        validarCpfFormato(usuario.getCpf());
        validarSequenciaCpf(usuario.getCpf());
        validarEmail(usuario.getEmail());

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

    public void validarCpfFormato(String cpf) {
        if (cpf == null || !cpf.matches("^(?!([0-9])\\1{10})\\d{11}$")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CPF inválido. Ele deve conter 11 dígitos e não pode ser uma repetição.");
        }
    }
    
    private void validarSequenciaCpf(String cpf) {
        int somaDigitos = 0;
        int restoSoma = 0;
        int verificador = 0;
        
        // -------- Primeiro Dígito --------
        for (int i = 0, peso = 10; i < 9; i++, peso--) {
            somaDigitos += (cpf.charAt(i) - '0') * peso;
        }
        
        restoSoma = somaDigitos % 11;
        if (restoSoma < 2) {
            verificador = 0;
        } else {
            verificador = (11 - restoSoma);
        }
        
        if (verificador != (cpf.charAt(9) - '0')) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CPF inválido");
        }
        
        // -------- Segundo Dígito --------
        somaDigitos = 0;
        for (int i = 0, peso = 11; i < 10; i++, peso--) {
            somaDigitos += (cpf.charAt(i) - '0') * peso;
        }
        
        restoSoma = somaDigitos % 11;
        if (restoSoma < 2) {
            verificador = 0;
        } else {
            verificador = (11 - restoSoma);
        }
        
        if (verificador != (cpf.charAt(10) - '0')) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CPF inválido");
        }
    }

    private void validarEmail(String email) {
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "E-mail inválido");
        }
    }
    
    public List<Usuario>listarUsuarios(){
		return usuarioRepository.findAll();
	}
    
    public Usuario procurarPorCpf(String cpf){
		return usuarioRepository.findByCpf(cpf).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Não foi localizado nenhum usuário com este CPF."));
	}
    
    @Transactional
    public Usuario atualizarUsuario(String cpf, Usuario novosDadosUsuario) {
        Usuario usuarioAtual = procurarPorCpf(cpf);
        String novoCpf   = novosDadosUsuario.getCpf();
        String novoEmail = novosDadosUsuario.getEmail();
        
        validarDadosDuplicados(usuarioAtual.getIdUsuario(), novoCpf, novoEmail);

        if (novosDadosUsuario.getNomeUsuario() != null && !novosDadosUsuario.getNomeUsuario().isBlank()) {
        	usuarioAtual.setNomeUsuario(novosDadosUsuario.getNomeUsuario());
        }

        if (novoEmail != null && !novoEmail.isBlank()) {
            validarEmail(novoEmail);
        	usuarioAtual.setEmail(novoEmail.trim());
        }

        if (novoCpf != null && !novoCpf.isBlank()) {
        	validarCpfFormato(novoCpf);
            validarSequenciaCpf(novoCpf);
            usuarioAtual.setCpf(novoCpf);
        }

        if (novosDadosUsuario.getCategoria() != null && novosDadosUsuario.getCategoria().getIdCategoriaUsuario() > 0) {
            var categoriaUsuario = categoriaRepository.findById(novosDadosUsuario.getCategoria().getIdCategoriaUsuario())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "CategoriaUsuario não encontrada."));
            usuarioAtual.setCategoria(categoriaUsuario);
        }

        if (novosDadosUsuario.getCurso() != null && novosDadosUsuario.getCurso().getIdCurso() > 0) {
            var curso = cursoRepository.findById(novosDadosUsuario.getCurso().getIdCurso())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso não encontrado."));
            usuarioAtual.setCurso(curso);
        }

        return usuarioRepository.save(usuarioAtual);
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
