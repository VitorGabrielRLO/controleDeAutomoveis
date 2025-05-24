package web.controleDeAutomoveis.repository.queries.lote;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import web.controleDeAutomoveis.filter.LoteFilter;
import web.controleDeAutomoveis.model.Lote;
import web.controleDeAutomoveis.repository.pagination.PaginacaoUtil;

public class LoteQueriesImpl implements LoteQueries {

	private static final Logger logger = LoggerFactory.getLogger(LoteQueriesImpl.class);

	@PersistenceContext
	private EntityManager em;

	@Override
	public Page<Saida> pesquisar(LoteFilter filtro, Pageable pageable) {

		StringBuilder queryLotes = new StringBuilder("select distinct l from Lote l inner join fetch l.vacina");
		StringBuilder condicoes = new StringBuilder();

		criarCondicoes(filtro, condicoes);
		if (condicoes.toString().isEmpty()) {
			condicoes.append(" where ");
		} else {
			condicoes.append(" and ");
		}
		condicoes.append("l.status = 'ATIVO'");

		queryLotes.append(condicoes);
		PaginacaoUtil.prepararOrdemJPQL(queryLotes, "l", pageable);
		TypedQuery<Saida> typedQuery = em.createQuery(queryLotes.toString(), Saida.class);
		PaginacaoUtil.prepararIntervalo(typedQuery, pageable);
		typedQuery.setHint("hibernate.query.passDistinctThrough", false);

		preencherParametros(filtro, typedQuery);

		List<Saida> lotes = typedQuery.getResultList();

		logger.info("Calculando o total de registros que o filtro retornará.");
		StringBuilder queryTotal = new StringBuilder("select count(l) from Lote l");
		queryTotal.append(condicoes);
		TypedQuery<Long> typedQueryTotal = em.createQuery(queryTotal.toString(), Long.class);

		preencherParametros(filtro, typedQueryTotal);

		long totalLotes = typedQueryTotal.getSingleResult();
		logger.info("O filtro retornará {} registros.", totalLotes);

		Page<Saida> page = new PageImpl<>(lotes, pageable, totalLotes);

		return page;
	}

	private void criarCondicoes(LoteFilter filtro, StringBuilder condicoes) {
		boolean condicao = false;

		if (filtro.getCodigo() != null) {
			fazerLigacaoCondicoes(condicoes, condicao);
			condicoes.append("l.codigo = :codigo");
			condicao = true;
		}
		if (filtro.getInicioValidade() != null) {
			fazerLigacaoCondicoes(condicoes, condicao);
			condicoes.append("l.validade >= :inicioValidade");
			condicao = true;
		}
		if (filtro.getFimValidade() != null) {
			fazerLigacaoCondicoes(condicoes, condicao);
			condicoes.append("l.validade <= :fimValidade");
			condicao = true;
		}
		if (filtro.getMinimoDosesLote() != null) {
			fazerLigacaoCondicoes(condicoes, condicao);
			condicoes.append("l.nroDosesDoLote >= :minimoDosesLote");
			condicao = true;
		}
		if (filtro.getMaximoDosesLote() != null) {
			fazerLigacaoCondicoes(condicoes, condicao);
			condicoes.append("l.nroDosesDoLote <= :maximoDosesLote");
			condicao = true;
		}
		if (filtro.getMinimoDosesAtual() != null) {
			fazerLigacaoCondicoes(condicoes, condicao);
			condicoes.append("l.nroDosesAtual >= :minimoDosesAtual");
			condicao = true;
		}
		if (filtro.getMaximoDosesAtual() != null) {
			fazerLigacaoCondicoes(condicoes, condicao);
			condicoes.append("l.nroDosesAtual <= :maximoDosesAtual");
			condicao = true;
		}
		if (StringUtils.hasText(filtro.getNomeVacina())) {
			fazerLigacaoCondicoes(condicoes, condicao);
			condicoes.append("lower(l.vacina.nome) like :vacina");
			condicao = true;
		}
	}

	private void preencherParametros(LoteFilter filtro, TypedQuery<?> typedQuery) {
		if (filtro.getCodigo() != null) {
			typedQuery.setParameter("codigo", filtro.getCodigo());
		}
		if (filtro.getInicioValidade() != null) {
			typedQuery.setParameter("inicioValidade", filtro.getInicioValidade());
		}
		if (filtro.getFimValidade() != null) {
			typedQuery.setParameter("fimValidade", filtro.getFimValidade());
		}
		if (filtro.getMinimoDosesLote() != null) {
			typedQuery.setParameter("minimoDosesLote", filtro.getMinimoDosesLote());
		}
		if (filtro.getMaximoDosesLote() != null) {
			typedQuery.setParameter("maximoDosesLote", filtro.getMaximoDosesLote());
		}
		if (filtro.getMinimoDosesAtual() != null) {
			typedQuery.setParameter("minimoDosesAtual", filtro.getMinimoDosesAtual());
		}
		if (filtro.getMaximoDosesAtual() != null) {
			typedQuery.setParameter("maximoDosesAtual", filtro.getMaximoDosesAtual());
		}
		if (StringUtils.hasText(filtro.getNomeVacina())) {
			typedQuery.setParameter("vacina", "%" + filtro.getNomeVacina() + "%");
		}
	}

	private void fazerLigacaoCondicoes(StringBuilder condicoes, boolean condicao) {
		if (!condicao) {
			condicoes.append(" where ");
		} else {
			condicoes.append(" and ");
		}
	}

}
