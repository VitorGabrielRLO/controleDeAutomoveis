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
import web.controlevacinacao.filter.FuncionarioFilter;
import web.controlevacinacao.model.Funcionario;
import web.controlevacinacao.model.Status;
import web.controlevacinacao.notificacao.NotificacaoSweetAlert2;
import web.controlevacinacao.notificacao.TipoNotificaoSweetAlert2;
import web.controlevacinacao.pagination.PageWrapper;
import web.controlevacinacao.repository.FuncionarioRepository;
import web.controlevacinacao.service.FuncionarioService;

@Controller
@RequestMapping("/funcionarios")
public class FuncionarioController {

    private static final Logger logger = LoggerFactory.getLogger(FuncionarioController.class);
    private final FuncionarioRepository funcionarioRepository;
    private final FuncionarioService funcionarioService;

    public FuncionarioController(FuncionarioRepository funcionarioRepository, FuncionarioService funcionarioService) {
        this.funcionarioRepository = funcionarioRepository;
        this.funcionarioService = funcionarioService;
    }

    @HxRequest
    @GetMapping("/abrirpesquisar")
    public String abrirPesquisaHTMX() {
        return "funcionarios/pesquisar :: formulario";
    }

    @HxRequest
    @GetMapping("/pesquisar")
    public String pesquisarFuncionariosHTMX(FuncionarioFilter filtro, Model model,
            @PageableDefault(size = 8) @SortDefault(sort = "codigo", direction = Sort.Direction.ASC) Pageable pageable,
            HttpServletRequest request) {
        Page<Funcionario> pagina = funcionarioRepository.pesquisar(filtro, pageable);
        PageWrapper<Funcionario> paginaWrapper = new PageWrapper<>(pagina, request);
        model.addAttribute("pagina", paginaWrapper);
        return "funcionarios/listar :: tabela";
    }

    @HxRequest
    @GetMapping("/cadastrar")
    public String abrirCadastroHTMX(Funcionario funcionario) {
        return "funcionarios/cadastrar :: formulario";
    }

    @HxRequest
    @PostMapping("/cadastrar")
    public String cadastrarHTMX(@Valid Funcionario funcionario, BindingResult resultado, RedirectAttributes attributes) {
        if (resultado.hasErrors()) {
            return "funcionarios/cadastrar :: formulario";
        }
        funcionarioService.salvar(funcionario);
        attributes.addFlashAttribute("notificacao", new NotificacaoSweetAlert2("Funcionário cadastrado com sucesso!", TipoNotificaoSweetAlert2.SUCCESS, 4000));
        return "redirect:/funcionarios/cadastrar";
    }

    @HxRequest
    @GetMapping("/alterar/{codigo}")
    public String abrirAlterarHTMX(@PathVariable("codigo") Long codigo, Model model) {
        Funcionario funcionario = funcionarioRepository.findByCodigoAndStatus(codigo, Status.ATIVO);
        model.addAttribute("funcionario", funcionario);
        return "funcionarios/alterar :: formulario";
    }

    @HxRequest
    @PostMapping("/alterar")
    public String alterarHTMX(@Valid Funcionario funcionario, BindingResult resultado, RedirectAttributes redirectAttributes) {
        if (resultado.hasErrors()) {
            return "funcionarios/alterar :: formulario";
        }
        funcionarioService.alterar(funcionario);
        redirectAttributes.addFlashAttribute("notificacao", new NotificacaoSweetAlert2("Funcionário alterado com sucesso!", TipoNotificaoSweetAlert2.SUCCESS, 4000));
        return "redirect:/funcionarios/abrirpesquisar";
    }

    @HxRequest
    @HxLocation(path = "/mensagem", target = "#main", swap = "outerHTML")
    @GetMapping("/remover/{codigo}")
    public String removerHTMX(@PathVariable("codigo") Long codigo, RedirectAttributes attributes) {
        funcionarioService.remover(codigo);
        attributes.addFlashAttribute("notificacao", new NotificacaoSweetAlert2("Funcionário removido com sucesso!", TipoNotificaoSweetAlert2.SUCCESS, 4000));
        return "redirect:/funcionarios/abrirpesquisar";
    }
}