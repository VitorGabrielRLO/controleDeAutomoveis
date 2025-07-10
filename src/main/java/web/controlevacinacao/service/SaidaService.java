package web.controlevacinacao.service;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.controlevacinacao.model.Saida;
import web.controlevacinacao.model.Status; // Importe o Status
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
        Veiculo veiculo = veiculoRepository.findByCodigoAndStatus(saida.getVeiculo().getCodigo(), Status.ATIVO);
        
        if (veiculo == null) {
            throw new IllegalArgumentException("Veículo não encontrado ou já está em uso (inativo).");
        }

        // --- LÓGICA ADICIONADA ---
        // 1. Torna o veículo INATIVO, pois ele está saindo.
        veiculo.setStatus(Status.INATIVO);
        
        double kmAtual = veiculo.getQuilometragem();
        saida.setKmSaida(kmAtual);

        double novaQuilometragem = kmAtual + saida.getKmAndado(); 
        
        veiculo.setQuilometragem(novaQuilometragem);
        veiculoRepository.save(veiculo);

        saida.setDataHoraSaida(LocalDateTime.now());
        saida.setVeiculo(veiculo); 
        saidaRepository.save(saida);
    }
    
    @Transactional
    public void registrarRetorno(Long saidaId, double kmRetorno) {
        Saida saida = saidaRepository.findById(saidaId)
                .orElseThrow(() -> new IllegalArgumentException("Registro de Saída não encontrado!"));

        if (saida.getDataHoraRetorno() != null) {
            throw new IllegalStateException("Este veículo já teve seu retorno registrado.");
        }

        if (kmRetorno < saida.getKmSaida()) {
            throw new IllegalArgumentException("A quilometragem de retorno não pode ser menor que a quilometragem no momento da saída (" + saida.getKmSaida() + " km).");
        }

        saida.setDataHoraRetorno(LocalDateTime.now());
        saida.setKmRetorno(kmRetorno);
        
        Veiculo veiculo = saida.getVeiculo();
        veiculo.setQuilometragem(kmRetorno);

        // --- LÓGICA ADICIONADA ---
        // 2. Torna o veículo ATIVO novamente, pois ele retornou.
        veiculo.setStatus(Status.ATIVO);
        
        veiculoRepository.save(veiculo);
        saidaRepository.save(saida);
    }
    
    public void salvar(Saida saida) {
        saidaRepository.save(saida);
    }
}