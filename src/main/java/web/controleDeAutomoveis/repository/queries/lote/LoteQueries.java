package web.controleDeAutomoveis.repository.queries.lote;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import web.controleDeAutomoveis.filter.LoteFilter;
import web.controleDeAutomoveis.model.Saida;

public interface LoteQueries {

	Page<Saida> pesquisar(LoteFilter filtro, Pageable pageable);
	
}
