package web.controlevacinacao.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort; // <-- IMPORT ADICIONADO AQUI
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import jakarta.validation.Valid;
import web.controlevacinacao.model.Saida;
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
    public String abrirRegistrar(Saida saida, Model model) {
        model.addAttribute("funcionarios", funcionarioRepository.findAll());
        model.addAttribute("veiculos", veiculoRepository.findAll());
        return "saidas/registrar";
    }

    @PostMapping("/registrar")
    public String registrarSaida(@Valid Saida saida, BindingResult resultado, RedirectAttributes redirectAttributes, Model model) {
        if (resultado.hasErrors()) {
            model.addAttribute("funcionarios", funcionarioRepository.findAll());
            model.addAttribute("veiculos", veiculoRepository.findAll());
            return "saidas/registrar";
        }
        
        saidaService.registrarSaida(saida);
        
        redirectAttributes.addFlashAttribute("notificacao", new NotificacaoSweetAlert2(
                "Saída registrada com sucesso!", TipoNotificaoSweetAlert2.SUCCESS, 4000));
        return "redirect:/saidas/registrar";
    }

    @GetMapping("/pesquisar")
    public String abrirPesquisar(Model model) {
        // Agora o 'Sort.by' será reconhecido
        model.addAttribute("saidas", saidaRepository.findAll(Sort.by("dataHoraSaida").descending()));
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
    public String processarRetorno(@PathVariable Long id, 
                                   @RequestParam("kmRetorno") double kmRetorno, 
                                   RedirectAttributes redirectAttributes, 
                                   Model model) {
        try {
            saidaService.registrarRetorno(id, kmRetorno);
            redirectAttributes.addFlashAttribute("notificacao", new NotificacaoSweetAlert2(
                "Retorno do veículo registrado com sucesso!", TipoNotificaoSweetAlert2.SUCCESS, 4000));
            return "redirect:/saidas/pesquisar"; 
        } catch (IllegalArgumentException e) {
            Saida saida = saidaRepository.findById(id).get();
            model.addAttribute("saida", saida);
            model.addAttribute("erro_km", e.getMessage());
            return "saidas/retornar :: modal";
        }
    }
}