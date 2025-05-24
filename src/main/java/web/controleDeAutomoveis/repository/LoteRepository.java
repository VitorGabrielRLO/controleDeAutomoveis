package web.controleDeAutomoveis.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import web.controleDeAutomoveis.model.Saida;
import web.controleDeAutomoveis.model.Status;
import web.controleDeAutomoveis.repository.queries.lote.LoteQueries;

public interface LoteRepository extends JpaRepository<Saida, Long>, LoteQueries {

    Saida findByCodigoAndStatus(Long codigo, Status status);
}
