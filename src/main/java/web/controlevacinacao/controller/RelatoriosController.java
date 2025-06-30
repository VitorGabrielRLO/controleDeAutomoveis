package web.controlevacinacao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import web.controlevacinacao.service.RelatoriosService;

@Controller
@RequestMapping("/relatorios")
public class RelatoriosController {

    @Autowired
    private RelatoriosService relatorioService;

    @GetMapping("/saidas-por-funcionario")
    public ResponseEntity<byte[]> gerarRelatorioSaidasPorFuncionario() {
        byte[] relatorio = relatorioService.gerarRelatorioSaidasPorFuncionario();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=RelatorioSaidasPorFuncionario.pdf")
                .body(relatorio);
    }
}