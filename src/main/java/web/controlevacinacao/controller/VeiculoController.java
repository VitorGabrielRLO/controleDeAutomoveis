package web.controlevacinacao.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxLocation;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import web.controlevacinacao.filter.VeiculoFilter;
import web.controlevacinacao.model.Status;
import web.controlevacinacao.model.Veiculo;
import web.controlevacinacao.notificacao.NotificacaoSweetAlert2;
import web.controlevacinacao.notificacao.TipoNotificaoSweetAlert2;
import web.controlevacinacao.pagination.PageWrapper;
import web.controlevacinacao.repository.VeiculoRepository;
import web.controlevacinacao.service.VeiculoService;

@Controller
@RequestMapping("/veiculos")
public class VeiculoController {

    private static final Logger logger = LoggerFactory.getLogger(VeiculoController.class);

    private VeiculoRepository veiculoRepository;
    private VeiculoService veiculoService;

    public VeiculoController(VeiculoRepository veiculoRepository, VeiculoService veiculoService) {
        this.veiculoRepository = veiculoRepository;
        this.veiculoService = veiculoService;
    }

    @HxRequest
    @GetMapping("/abrirpesquisar")
    public String abrirPaginaPesquisaHTMX() {
        return "veiculos/pesquisar :: formulario";
    }

    @HxRequest
    @GetMapping("/pesquisar")
    public String pesquisarHTMX(VeiculoFilter filtro, Model model,
            @PageableDefault(size = 7) @SortDefault(sort = "codigo", direction = Sort.Direction.ASC) Pageable pageable,
            HttpServletRequest request) {
        Page<Veiculo> pagina = veiculoRepository.pesquisar(filtro, pageable);
        PageWrapper<Veiculo> paginaWrapper = new PageWrapper<>(pagina, request);
        model.addAttribute("pagina", paginaWrapper);
        return "veiculos/listar :: tabela";
    }

    @HxRequest
    @GetMapping("/cadastrar")
    public String abrirCadastroHTMX(Veiculo veiculo) {
        return "veiculos/cadastrar :: formulario";
    }

    @HxRequest
    @PostMapping("/cadastrar")
    public String cadastrarHTMX(@Valid Veiculo veiculo, BindingResult resultado,
            RedirectAttributes redirectAttributes) {
        if (resultado.hasErrors()) {
            // Log de erros
            return "veiculos/cadastrar :: formulario";
        }
        veiculoService.salvar(veiculo);
        redirectAttributes.addFlashAttribute("notificacao", new NotificacaoSweetAlert2(
                "Veículo cadastrado com sucesso!", TipoNotificaoSweetAlert2.SUCCESS, 4000));
        return "redirect:/veiculos/cadastrar";
    }

    @HxRequest
    @GetMapping("/alterar/{codigo}")
    public String abrirAlterarHTMX(@PathVariable("codigo") Long codigo, Model model) {
        Veiculo veiculo = veiculoRepository.findByCodigoAndStatus(codigo, Status.ATIVO);
        model.addAttribute("veiculo", veiculo);
        return "veiculos/alterar :: formulario";
    }

    @HxRequest
    @PostMapping("/alterar")
    public String alterarHTMX(@Valid Veiculo veiculo, BindingResult resultado, RedirectAttributes redirectAttributes) {
        if (resultado.hasErrors()) {
            return "veiculos/alterar :: formulario";
        }
        veiculoService.alterar(veiculo);
        redirectAttributes.addFlashAttribute("notificacao",
                new NotificacaoSweetAlert2("Veículo alterado com sucesso!", TipoNotificaoSweetAlert2.SUCCESS, 4000));
        return "redirect:/veiculos/abrirpesquisar";
    }

    @HxRequest
    // A anotação HxLocation redireciona o cliente para a página de pesquisa após a ação.
    @HxLocation(path = "/veiculos/abrirpesquisar", target = "#main", swap = "outerHTML")
    @GetMapping("/remover/{codigo}")
    public String removerHTMX(@PathVariable("codigo") Long codigo, RedirectAttributes attributes) {
        try {
            // Chama o novo método de exclusão
            veiculoService.remover(codigo);
            attributes.addFlashAttribute("notificacao",
                    new NotificacaoSweetAlert2("Veículo excluído com sucesso!", TipoNotificaoSweetAlert2.SUCCESS, 4000));
        } catch (IllegalStateException e) {
            // Captura a exceção específica e exibe a mensagem para o usuário
            attributes.addFlashAttribute("notificacao",
                    new NotificacaoSweetAlert2(e.getMessage(), TipoNotificaoSweetAlert2.ERROR, 5000));
        }
        // O HxLocation se encarrega de atualizar a tela
        return "redirect:/veiculos/abrirpesquisar";
    }


    
}