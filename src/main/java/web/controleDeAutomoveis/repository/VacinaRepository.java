package web.controleDeAutomoveis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import web.controleDeAutomoveis.model.Status;
import web.controleDeAutomoveis.model.Carro;
import web.controleDeAutomoveis.repository.queries.vacina.VacinaQueries;

public interface VacinaRepository extends JpaRepository<Carro, Long>, VacinaQueries {

    List<Carro> findByStatus(Status status);

    Carro findByCodigoAndStatus(Long codigo, Status status);

}
