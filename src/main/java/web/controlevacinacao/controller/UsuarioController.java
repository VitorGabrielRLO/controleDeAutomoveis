package web.controlevacinacao.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import jakarta.validation.Valid;
import web.controlevacinacao.model.Papel;
import web.controlevacinacao.model.Usuario;
import web.controlevacinacao.notificacao.NotificacaoSweetAlert2;
import web.controlevacinacao.notificacao.TipoNotificaoSweetAlert2;
import web.controlevacinacao.repository.PapelRepository;
import web.controlevacinacao.service.CadastroUsuarioService;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

	private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);
	
	@Autowired private PapelRepository papelRepository;
	@Autowired private CadastroUsuarioService cadastroUsuarioService;
	@Autowired private PasswordEncoder passwordEncoder;
	
	@GetMapping("/cadastrar")
	@HxRequest
	public String abrirCadastroUsuario(Usuario usuario, Model model) {
		List<Papel> papeis = papelRepository.findAll();
		model.addAttribute("todosPapeis", papeis);
		return "usuarios/cadastrar :: formulario";
	}
	
	@PostMapping("/cadastrar")
	@HxRequest
	public String cadastrarNovoUsuario(@Valid Usuario usuario, BindingResult resultado, Model model, RedirectAttributes redirectAttributes) {
		if (resultado.hasErrors()) {
			logger.info("O usuario recebido para cadastrar não é válido.");
			for (FieldError erro : resultado.getFieldErrors()) {
				logger.info("{}", erro);
			}
			// É importante recarregar os papéis em caso de erro para a tela não quebrar
			model.addAttribute("todosPapeis", papelRepository.findAll());
			return "usuarios/cadastrar :: formulario";
		} else {
			usuario.setAtivo(true);
			// Criptografa a senha antes de salvar
			usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
			cadastroUsuarioService.salvar(usuario);

			redirectAttributes.addFlashAttribute("notificacao", new NotificacaoSweetAlert2(
                    "Usuário cadastrado com sucesso!", TipoNotificaoSweetAlert2.SUCCESS, 4000));
			return "redirect:/usuarios/cadastrar";
		}
	}
}