package web.controlevacinacao.repository.queries.funcionario;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import web.controlevacinacao.filter.FuncionarioFilter;
import web.controlevacinacao.model.Funcionario;
import web.controlevacinacao.pagination.PaginacaoUtil;

public class FuncionarioQueriesImpl implements FuncionarioQueries {

	@PersistenceContext
	private EntityManager em;

    @Override
	public Page<Funcionario> pesquisar(FuncionarioFilter filtro, Pageable pageable) {
		StringBuilder queryFuncionarios = new StringBuilder("select f from Funcionario f");
		StringBuilder condicoes = new StringBuilder();
		Map<String, Object> parametros = new HashMap<>();

		preencherCondicoesEParametros(filtro, condicoes, parametros);

		if (condicoes.isEmpty()) {
			condicoes.append(" where f.status = 'ATIVO'");
		} else {
			condicoes.append(" and f.status = 'ATIVO'");
		}

		queryFuncionarios.append(condicoes);
		PaginacaoUtil.prepararOrdemJPQL(queryFuncionarios, "f", pageable);
		TypedQuery<Funcionario> typedQuery = em.createQuery(queryFuncionarios.toString(), Funcionario.class);
		PaginacaoUtil.prepararIntervalo(typedQuery, pageable);
		PaginacaoUtil.preencherParametros(parametros, typedQuery);
		List<Funcionario> funcionarios = typedQuery.getResultList();

		long totalFuncionarios = PaginacaoUtil.getTotalRegistros("Funcionario", "f", condicoes, parametros, em);

		return new PageImpl<>(funcionarios, pageable, totalFuncionarios);
	}

	private void preencherCondicoesEParametros(FuncionarioFilter filtro, StringBuilder condicoes, Map<String, Object> parametros) {
		boolean condicao = false;

        if (filtro != null) {
            if (filtro.getCodigo() != null) {
                PaginacaoUtil.fazerLigacaoCondicoes(condicoes, condicao);
                condicoes.append("f.codigo = :codigo");
                parametros.put("codigo", filtro.getCodigo());
                condicao = true;
            }
            if (StringUtils.hasText(filtro.getNome())) {
                PaginacaoUtil.fazerLigacaoCondicoes(condicoes, condicao);		
                condicoes.append("lower(f.nome) like :nome");
                parametros.put("nome", "%" + filtro.getNome().toLowerCase() + "%");
                condicao = true;
            }
            if (StringUtils.hasText(filtro.getCpf())) {
                PaginacaoUtil.fazerLigacaoCondicoes(condicoes, condicao);
                condicoes.append("f.cpf like :cpf");
                parametros.put("cpf", "%" + filtro.getCpf() + "%");
                condicao = true;
            }
            if (StringUtils.hasText(filtro.getMatricula())) {
                PaginacaoUtil.fazerLigacaoCondicoes(condicoes, condicao);
                condicoes.append("f.matricula like :matricula");
                parametros.put("matricula", "%" + filtro.getMatricula() + "%");
                condicao = true;
            }
        }
	}
}