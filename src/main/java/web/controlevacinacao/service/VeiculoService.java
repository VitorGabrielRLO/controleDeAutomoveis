package web.controlevacinacao.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.controlevacinacao.model.Status;
import web.controlevacinacao.model.Veiculo;
import web.controlevacinacao.repository.VeiculoRepository;

@Service
@Transactional
public class VeiculoService {

    private VeiculoRepository veiculoRepository;

    public VeiculoService(VeiculoRepository veiculoRepository) {
        this.veiculoRepository = veiculoRepository;
    }

    public void salvar(Veiculo veiculo) {
        veiculoRepository.save(veiculo);
    }

    public void alterar(Veiculo veiculo) {
        veiculoRepository.save(veiculo);
    }

    public void desativar(Long codigo) {
        Veiculo veiculo = veiculoRepository.findById(codigo).get();
        veiculo.setStatus(Status.INATIVO);
        veiculoRepository.save(veiculo);
    }
}