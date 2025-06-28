package web.controlevacinacao.repository.queries.funcionario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import web.controlevacinacao.filter.FuncionarioFilter;
import web.controlevacinacao.model.Funcionario;

public interface FuncionarioQueries {

	Page<Funcionario> pesquisar(FuncionarioFilter filtro, Pageable pageable);
	
}