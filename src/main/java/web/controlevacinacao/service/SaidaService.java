package web.controlevacinacao.service;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.controlevacinacao.model.Saida;
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
        // 1. Carrega a instância completa do veículo do banco de dados
        Veiculo veiculo = veiculoRepository.findById(saida.getVeiculo().getCodigo())
                .orElseThrow(() -> new IllegalArgumentException("Veículo não encontrado!"));

        // 2. Armazena a quilometragem atual do veículo ANTES de sair
        double kmAtual = veiculo.getQuilometragem();
        saida.setKmSaida(kmAtual); // Salva no registro da saída qual era a KM no momento da partida

        // 3. Calcula a nova quilometragem total do veículo
        //    (KM atual + KM que o usuário informou que vai rodar)
        double novaQuilometragem = kmAtual + saida.getKmAndado(); // Usaremos um campo temporário 'kmAndado'
        
        // 4. Atualiza a quilometragem principal do veículo no banco de dados
        veiculo.setQuilometragem(novaQuilometragem);
        veiculoRepository.save(veiculo);

        // 5. Define a data/hora da saída e salva o registro completo
        saida.setDataHoraSaida(LocalDateTime.now());
        saida.setVeiculo(veiculo); 
        saidaRepository.save(saida);
    }
    
    // ... (imports e outros métodos)

@Transactional
public void registrarRetorno(Long saidaId, double kmRetorno) {
    // 1. Carrega o registro de saída original do banco
    Saida saida = saidaRepository.findById(saidaId)
            .orElseThrow(() -> new IllegalArgumentException("Registro de Saída não encontrado!"));

    // 2. Validação: O retorno já foi registrado?
    if (saida.getDataHoraRetorno() != null) {
        throw new IllegalStateException("Este veículo já teve seu retorno registrado.");
    }

    // 3. Validação: A KM de retorno é válida? (Não pode ser menor que a KM de saída)
    if (kmRetorno < saida.getKmSaida()) {
        throw new IllegalArgumentException("A quilometragem de retorno não pode ser menor que a quilometragem no momento da saída (" + saida.getKmSaida() + " km).");
    }

    // 4. Atualiza o registro de Saída com os novos dados
    saida.setDataHoraRetorno(LocalDateTime.now());
    saida.setKmRetorno(kmRetorno);
    
    // 5. Atualiza a quilometragem principal do Veículo
    Veiculo veiculo = saida.getVeiculo();
    veiculo.setQuilometragem(kmRetorno);
    
    // 6. Salva as alterações no banco de dados
    veiculoRepository.save(veiculo);
    saidaRepository.save(saida);
}
//...
    
    public void salvar(Saida saida) {
        saidaRepository.save(saida);
    }
}