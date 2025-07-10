package web.controlevacinacao.service;

import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.stereotype.Service;
import web.controlevacinacao.report.JaspersoftUtil;

@Service
public class RelatoriosService {

  private final DataSource dataSource;
  private final JaspersoftUtil jaspersoftUtil;

  public RelatoriosService(DataSource dataSource, JaspersoftUtil jaspersoftUtil) {
    this.dataSource = dataSource;
    this.jaspersoftUtil = jaspersoftUtil;
  }

  public byte[] gerarRelatorioTodasSaidas() {
    // o path deve bater com o local em src/main/resources/relatorios
    return jaspersoftUtil.gerarRelatorio(
        "/relatorios/TodasSaidas.jasper",
        null,
        dataSource);
  }

  public byte[] gerarRelatorioTodosVeiculos() {
    return jaspersoftUtil.gerarRelatorio(
        "/relatorios/TodosVeiculos.jasper",
        null,
        dataSource);
  }

  public byte[] gerarRelatorioTodosFuncionarios() {
    return jaspersoftUtil.gerarRelatorio(
        "/relatorios/TodosFuncionarios.jasper",
        null,
        dataSource);
  }
}
