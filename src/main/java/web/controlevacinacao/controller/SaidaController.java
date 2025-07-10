package web.controlevacinacao.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import web.controlevacinacao.model.Funcionario;
import web.controlevacinacao.model.Saida;
import web.controlevacinacao.model.Veiculo;
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

    // --- MÉTODO CORRIGIDO ---
    // Agora este método carrega os dados necessários para a página
    @GetMapping("/registrar")
    public String abrirRegistrar(Saida saida, Model model) {
        model.addAttribute("funcionarios", funcionarioRepository.findAll());
        // Aqui você pode adicionar uma lógica para carregar apenas veículos disponíveis
        model.addAttribute("veiculos", veiculoRepository.findAll()); 
        return "saidas/registrar";
    }

    @PostMapping("/registrar")
    public String registrarSaida(@Valid Saida saida, BindingResult resultado, RedirectAttributes redirectAttributes, Model model) {
        // Se houver erros de validação, recarregue a página com as listas
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

    // Abre a página de consulta de saídas
    @GetMapping("/pesquisar")
    public String abrirPesquisar(Model model) {
        // Simplificado, idealmente seria paginado e com filtro
        model.addAttribute("saidas", saidaRepository.findAll()); 
        return "saidas/listar";
    }
}