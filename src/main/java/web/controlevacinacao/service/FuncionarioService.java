package web.controlevacinacao.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import web.controlevacinacao.model.Funcionario;
import web.controlevacinacao.model.Status;
import web.controlevacinacao.repository.FuncionarioRepository;

@Service
@Transactional
public class FuncionarioService {

    private FuncionarioRepository funcionarioRepository;

    public FuncionarioService(FuncionarioRepository funcionarioRepository) {
        this.funcionarioRepository = funcionarioRepository;
    }

    public void salvar(Funcionario funcionario) {
        funcionarioRepository.save(funcionario);
    }

    public void alterar(Funcionario funcionario) {
        funcionarioRepository.save(funcionario);
    }

    public void remover(Long codigo) {
        Funcionario funcionario = funcionarioRepository.findByCodigoAndStatus(codigo, Status.ATIVO);
        if (funcionario == null) {
            throw new RuntimeException("Remoção de Funcionário com código inválido");
        } else {
            funcionario.setStatus(Status.INATIVO);
            funcionarioRepository.save(funcionario);
        }
    }
}