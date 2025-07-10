package web.controlevacinacao.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    public void remover(Long codigo) {
        Veiculo veiculo = veiculoRepository.findById(codigo)
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado para exclusão!"));

        if (veiculo.getSaidas() != null && !veiculo.getSaidas().isEmpty()) {
            // Se o veículo tiver saídas, inativa em vez de excluir
            veiculo.setStatus(web.controlevacinacao.model.Status.INATIVO);
            veiculoRepository.save(veiculo);
            throw new IllegalStateException("Veículo não pode ser excluído pois possui saídas registradas. Status alterado para INATIVO.");
        } else {
            // Se não houver saídas, procede com a exclusão
            veiculoRepository.deleteById(codigo);
        }
    }
}