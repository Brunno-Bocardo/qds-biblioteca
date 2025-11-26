package br.edu.ifsp.biblioteca.strategy;

import java.util.List;

import org.springframework.stereotype.Service;

import br.edu.ifsp.biblioteca.model.CategoriaUsuario;
import br.edu.ifsp.biblioteca.model.Usuario;

@Service
public class EmprestimoStrategyResolver {

    private final List<IEmprestimoStrategy> strategies;

    public EmprestimoStrategyResolver(List<IEmprestimoStrategy> strategies) {
        this.strategies = strategies;
    }

    public IEmprestimoStrategy resolve(Usuario usuario) {
        CategoriaUsuario categoria = usuario.getCategoria();
        if (categoria == null) {
            throw new IllegalArgumentException("Categoria do usuário não pode ser nula");
        }

        return strategies.stream()
                .filter(strategy -> strategy.supports(categoria))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Estratégia não encontrada: " + categoria.getNomeCategoriaUsuario()
                ));
    }
}

