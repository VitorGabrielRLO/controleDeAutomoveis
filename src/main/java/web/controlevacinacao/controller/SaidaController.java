package web.controlevacinacao.controller;

import java.util.HashMap;
import java.util.Map;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import web.controlevacinacao.model.Status;
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

    // ... outros métodos permanecem iguais ...

    @GetMapping("/registrar")
    public String abrirRegistrar(Saida saida, Model model) {
        // CORREÇÃO: Passa para o modelo apenas os veículos com status ATIVO.
        model.addAttribute("veiculos", veiculoRepository.findByStatus(Status.ATIVO));
        
        model.addAttribute("funcionarios", funcionarioRepository.findAll());
        return "saidas/registrar :: formulario";
    }

    @PostMapping("/registrar")
    public String registrarSaida(@Valid Saida saida, BindingResult resultado, RedirectAttributes redirectAttributes, Model model) {
        if (resultado.hasErrors()) {
            // Garante que, em caso de erro, a lista de veículos na tela continue sendo apenas dos ativos.
            model.addAttribute("veiculos", veiculoRepository.findByStatus(Status.ATIVO));

            model.addAttribute("funcionarios", funcionarioRepository.findAll());
            return "saidas/registrar :: formulario";
        }
        
        try {
            saidaService.registrarSaida(saida);
        } catch (IllegalArgumentException e) {
            resultado.rejectValue("veiculo", "veiculo.invalido", e.getMessage());
            // Garante que a lista correta seja carregada em caso de erro de negócio.
            model.addAttribute("veiculos", veiculoRepository.findByStatus(Status.ATIVO));
            
            model.addAttribute("funcionarios", funcionarioRepository.findAll());
            return "saidas/registrar :: formulario";
        }
        
        redirectAttributes.addFlashAttribute("notificacao", new NotificacaoSweetAlert2(
                "Saída registrada com sucesso!", TipoNotificaoSweetAlert2.SUCCESS, 4000));
        return "redirect:/saidas/registrar";
    }

    @GetMapping("/pesquisar")
    public String abrirPesquisar(Model model) {
        model.addAttribute("saidas", saidaRepository.findAll(Sort.by("dataHoraSaida").descending()));
        return "saidas/listar";
    }
    
    @HxRequest
    @GetMapping("/pesquisar")
    public String pesquisarHTMX(Model model) {
        model.addAttribute("saidas", saidaRepository.findAll(Sort.by("dataHoraSaida").descending()));
        return "saidas/listar :: tabela";
    }

    @PostMapping("/retornar/{id}")
    @HxRequest
    public String processarRetorno(@PathVariable Long id, 
                                   @RequestParam(name = "kmRetorno", required = false) Double kmRetorno, 
                                   RedirectAttributes redirectAttributes) {
        
        if (kmRetorno == null) {
            Map<Long, String> errors = new HashMap<>();
            errors.put(id, "A quilometragem de retorno é obrigatória.");
            redirectAttributes.addFlashAttribute("retorno_errors", errors);
            return "redirect:/saidas/pesquisar";
        }
        
        try {
            saidaService.registrarRetorno(id, kmRetorno);
            redirectAttributes.addFlashAttribute("notificacao", new NotificacaoSweetAlert2(
                "Retorno do veículo registrado com sucesso!", TipoNotificaoSweetAlert2.SUCCESS, 4000));
        } catch (IllegalArgumentException | IllegalStateException e) {
            Map<Long, String> errors = new HashMap<>();
            errors.put(id, e.getMessage());
            redirectAttributes.addFlashAttribute("retorno_errors", errors);
        }

        return "redirect:/saidas/pesquisar";
    }
}