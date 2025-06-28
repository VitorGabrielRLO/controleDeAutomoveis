package web.controlevacinacao.repository.queries.veiculo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import web.controlevacinacao.filter.VeiculoFilter; // Será criado a seguir
import web.controlevacinacao.model.Veiculo;

public interface VeiculoQueries {
    Page<Veiculo> pesquisar(VeiculoFilter filtro, Pageable pageable);
}