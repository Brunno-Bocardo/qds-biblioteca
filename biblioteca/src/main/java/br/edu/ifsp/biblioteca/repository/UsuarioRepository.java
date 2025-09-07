package br.edu.ifsp.biblioteca.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import br.edu.ifsp.biblioteca.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
	
    Optional<Usuario> findByCpf(String cpf);
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findById(Integer idUsuario);
    boolean existsByCpfAndIdUsuarioNot(String cpf, Integer idUsuario);
    boolean existsByEmailAndIdUsuarioNot(String email, Integer idUsuario);
    boolean existsByCpf(String cpf);
    void deleteByCpf(String cpf);
    
}
