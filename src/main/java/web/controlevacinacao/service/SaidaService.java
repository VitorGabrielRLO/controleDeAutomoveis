package web.controlevacinacao.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import web.controlevacinacao.model.Saida;
import web.controlevacinacao.model.Status;
import web.controlevacinacao.model.Veiculo;
import web.controlevacinacao.repository.SaidaRepository;
import web.controlevacinacao.repository.VeiculoRepository;

@Service
@Transactional
public class SaidaService {

    @Autowired
    private SaidaRepository saidaRepository;

    @Autowired
    private VeiculoRepository veiculoRepository;

    public void registrarSaida(Saida saida) {
        // Define a data e hora da saída no momento do registro
        saida.setDataHoraSaida(LocalDateTime.now());

        // ============== LÓGICA ADICIONADA ==============
        Veiculo veiculo = saida.getVeiculo();
        if (veiculo != null) {
            // Atualiza a quilometragem do veículo com a informada na saída
            veiculo.setQuilometragem(saida.getKmSaida());
            veiculoRepository.save(veiculo); // Salva a alteração no veículo
        }
        // ===============================================

        // Salva o registro da saída em si
        saidaRepository.save(saida);
    }

    public void registrarRetorno(Saida saida) {
        // ============== LÓGICA PARA O RETORNO (A ser feita no futuro) ==============
        saida.setDataHoraRetorno(LocalDateTime.now());

        Veiculo veiculo = saida.getVeiculo();
        if (veiculo != null) {
            veiculo.setQuilometragem(saida.getKmRetorno());
            veiculoRepository.save(veiculo);
        }
        // =========================================================================

        saidaRepository.save(saida);
    }

    public void salvar(Saida saida) {
        saidaRepository.save(saida);
    }
}