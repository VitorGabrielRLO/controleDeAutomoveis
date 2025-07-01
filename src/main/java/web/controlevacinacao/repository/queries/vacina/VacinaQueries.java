package web.controlevacinacao.repository.queries.vacina;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import web.controlevacinacao.filter.VacinaFilter;
import web.controlevacinacao.model.Veiculo;

public interface VacinaQueries {

	Page<Veiculo> pesquisar(VacinaFilter filtro, Pageable pageable);
	
}
