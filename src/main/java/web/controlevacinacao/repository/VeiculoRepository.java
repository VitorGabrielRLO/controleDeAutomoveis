package web.controlevacinacao.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import web.controlevacinacao.model.Status;
import web.controlevacinacao.model.Veiculo;
import web.controlevacinacao.repository.queries.veiculo.VeiculoQueries;

public interface VeiculoRepository extends JpaRepository<Veiculo, Long>, VeiculoQueries {

    /**
     * Encontra um veículo pelo código e pelo status.
     * Útil para buscar um veículo específico que esteja ativo ou inativo.
     */
    Veiculo findByCodigoAndStatus(Long codigo, Status status);

    /**
     * Encontra todos os veículos que correspondem a um determinado status.
     * Essencial para listar apenas os veículos ativos na tela de registro de saída.
     */
    List<Veiculo> findByStatus(Status status);

}