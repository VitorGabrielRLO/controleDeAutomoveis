package web.controlevacinacao.repository.queries.aplicacao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import web.controlevacinacao.filter.AplicacaoFilter;
import web.controlevacinacao.model.Saida;

public interface AplicacaoQueries {

	Page<Saida> pesquisar(AplicacaoFilter filtro, Pageable pageable);
	
	Saida buscar(Long codigo);

}
