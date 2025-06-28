package web.controlevacinacao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.controlevacinacao.model.Status;
import web.controlevacinacao.model.Veiculo;
import web.controlevacinacao.repository.queries.veiculo.VeiculoQueries; // Será criado a seguir

public interface VeiculoRepository extends JpaRepository<Veiculo, Long>, VeiculoQueries {

    Veiculo findByCodigoAndStatus(Long codigo, Status status);

}