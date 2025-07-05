package web.controlevacinacao.repository;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import web.controlevacinacao.model.Status;
import web.controlevacinacao.model.Veiculo;
import web.controlevacinacao.repository.queries.veiculo.VeiculoQueries;

public interface VeiculoRepository extends JpaRepository<Veiculo, Long>, VeiculoQueries {

    Veiculo findByCodigoAndStatus(Long codigo, Status status);

    // Novo método para buscar todos os veículos por um status específico, ordenado
    List<Veiculo> findAllByStatus(Status status, Sort sort);

}