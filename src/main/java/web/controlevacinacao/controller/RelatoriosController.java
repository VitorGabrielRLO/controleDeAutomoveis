package web.controlevacinacao.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import web.controlevacinacao.service.RelatoriosService;

@Controller
public class RelatoriosController {

    private static final Logger logger = LoggerFactory.getLogger(RelatoriosController.class);

    @Autowired
    private RelatoriosService relatorioService;

    @GetMapping("/relatorios/saidas")
    public ResponseEntity<byte[]> gerarRelatorioTodasSaidas() {
        logger.debug("Gerando relatório de todas as saídas");
        byte[] pdf = relatorioService.gerarRelatorioTodasSaidas();
        logger.debug("Relatório de todas as saídas gerado");
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=TodasSaidas.pdf")
                .body(pdf);
    }

    @GetMapping("/relatorios/veiculos")
    public ResponseEntity<byte[]> gerarRelatorioTodosVeiculos() {
        logger.debug("Gerando relatório de todos os veículos");
        byte[] pdf = relatorioService.gerarRelatorioTodosVeiculos();
        logger.debug("Relatório de todos os veículos gerado");
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=TodosVeiculos.pdf")
                .body(pdf);
    }

    @GetMapping("/relatorios/funcionarios")
    public ResponseEntity<byte[]> gerarRelatorioTodosFuncionarios() {
        logger.debug("Gerando relatório de todos os funcionários");
        byte[] pdf = relatorioService.gerarRelatorioTodosFuncionarios();
        logger.debug("Relatório de todos os funcionários gerado");
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=TodosFuncionarios.pdf")
                .body(pdf);
    }
}