package web.controlevacinacao.service;

import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.stereotype.Service;
import web.controlevacinacao.report.JaspersoftUtil;

@Service
public class RelatoriosService {

    private DataSource dataSource;
    private JaspersoftUtil jaspersoftUtil;

    public RelatoriosService(DataSource dataSource, JaspersoftUtil jaspersoftUtil) {
        this.dataSource = dataSource;
        this.jaspersoftUtil = jaspersoftUtil;
    }

    public byte[] gerarRelatorioSaidasPorFuncionario() {
        String arquivoJasper = "/relatorios/funcionarios_saidas.jasper";
        // Não precisamos de parâmetros aqui, pois a query principal busca todos
        return jaspersoftUtil.gerarRelatorio(arquivoJasper, null, dataSource);
    }
}