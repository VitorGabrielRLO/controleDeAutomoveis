package web.controlevacinacao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.controlevacinacao.model.Funcionario;
import web.controlevacinacao.model.Status;
import web.controlevacinacao.repository.queries.funcionario.FuncionarioQueries;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long>, FuncionarioQueries {

    Funcionario findByCodigoAndStatus(Long codigo, Status status);

}