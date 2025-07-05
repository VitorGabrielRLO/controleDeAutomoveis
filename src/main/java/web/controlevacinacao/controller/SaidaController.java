package web.controlevacinacao.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import web.controlevacinacao.model.Saida;
import web.controlevacinacao.model.Status;
import web.controlevacinacao.notificacao.NotificacaoSweetAlert2;
import web.controlevacinacao.notificacao.TipoNotificaoSweetAlert2;
import web.controlevacinacao.repository.FuncionarioRepository;
import web.controlevacinacao.repository.SaidaRepository;
import web.controlevacinacao.repository.VeiculoRepository;
import web.controlevacinacao.service.SaidaService;

@Controller
@RequestMapping("/saidas")
public class SaidaController {

    private static final Logger logger = LoggerFactory.getLogger(SaidaController.class);

    @Autowired private FuncionarioRepository funcionarioRepository;
    @Autowired private VeiculoRepository veiculoRepository;
    @Autowired private SaidaRepository saidaRepository;
    @Autowired private SaidaService saidaService;

    @GetMapping("/registrar")
    public String abrirRegistrar(Saida saida, Model model, HttpServletRequest request) {
        model.addAttribute("funcionarios", funcionarioRepository.findAll(Sort.by("nome")));
        model.addAttribute("veiculos", veiculoRepository.findAllByStatus(Status.ATIVO, Sort.by("modelo")));
        model.addAttribute("saida", saida);

        if ("true".equals(request.getHeader("HX-Request"))) {
            return "saidas/registrar :: conteudo";
        }
        return "saidas/registrar";
    }

    @PostMapping("/registrar")
    @HxRequest
    public String registrarSaida(@Valid Saida saida, BindingResult resultado, Model model) {
        try {
            if (resultado.hasErrors()) {
                throw new Exception("Erro de validação.");
            }
            saidaService.registrarSaida(saida);
            model.addAttribute("notificacao", new NotificacaoSweetAlert2("Saída registrada com sucesso!", TipoNotificaoSweetAlert2.SUCCESS, 4000));
            return abrirRegistrar(new Saida(), model, null);
        } catch (Exception e) {
            if (e instanceof IllegalStateException) {
                model.addAttribute("notificacao", new NotificacaoSweetAlert2(e.getMessage(), TipoNotificaoSweetAlert2.ERROR, 4000));
            }
            model.addAttribute("funcionarios", funcionarioRepository.findAll(Sort.by("nome")));
            model.addAttribute("veiculos", veiculoRepository.findAllByStatus(Status.ATIVO, Sort.by("modelo")));
            model.addAttribute("saida", saida);
            return "saidas/registrar :: conteudo";
        }
    }

    @GetMapping("/pesquisar")
    public String abrirPesquisar(Model model, HttpServletRequest request) {
        model.addAttribute("saidas", saidaRepository.findAll(Sort.by("dataHoraSaida").descending()));
        if ("true".equals(request.getHeader("HX-Request"))) {
            return "saidas/listar :: conteudo"; // CORRETO
        }
        return "saidas/listar";
    }

    @GetMapping("/retornar/{id}")
    @HxRequest
    public String abrirRetorno(@PathVariable Long id, Model model) {
        Saida saida = saidaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Registro de Saída não encontrado: " + id));
        model.addAttribute("saida", saida);
        return "saidas/retornar :: modal";
    }

    @PostMapping("/retornar/{id}")
    @HxRequest
    public String processarRetorno(@PathVariable Long id, 
                                   @RequestParam("kmRetorno") double kmRetorno, 
                                   Model model) { 
        try {
            saidaService.registrarRetorno(id, kmRetorno);
            
            model.addAttribute("notificacao", new NotificacaoSweetAlert2(
                "Retorno do veículo registrado com sucesso!", TipoNotificaoSweetAlert2.SUCCESS, 4000));
            
            model.addAttribute("saidas", saidaRepository.findAll(Sort.by("dataHoraSaida").descending()));

            // Retorna o fragmento correto para substituir #main
            return "saidas/listar :: conteudo";

        } catch (IllegalArgumentException | IllegalStateException e) {
            Saida saida = saidaRepository.findById(id).get();
            model.addAttribute("saida", saida);
            model.addAttribute("erro_km", e.getMessage());
            return "saidas/retornar :: modal"; 
        }
    }
     @GetMapping("/cancelar-retorno")
    @HxRequest
    @ResponseBody // Retorna o corpo da resposta diretamente, sem procurar um template
    public String cancelarRetorno() {
        return ""; // Retorna vazio para limpar o #modal-placeholder
    }
}