package web.controlevacinacao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import web.controlevacinacao.model.Saida;
import web.controlevacinacao.model.Status;
import web.controlevacinacao.repository.queries.aplicacao.AplicacaoQueries;

public interface AplicacaoRepository extends JpaRepository<Saida, Long>, AplicacaoQueries {

    Saida findByCodigoAndStatus(Long codigo, Status status);
}
