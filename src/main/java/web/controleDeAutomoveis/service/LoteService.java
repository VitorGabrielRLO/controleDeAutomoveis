package web.controleDeAutomoveis.service;

import org.springframework.stereotype.Service;

import web.controleDeAutomoveis.model.Saida;
import web.controleDeAutomoveis.model.Status;
import web.controleDeAutomoveis.repository.LoteRepository;

@Service
public class LoteService {
    private LoteRepository loteRepository;

    public LoteService(LoteRepository loteRepository) {
        this.loteRepository = loteRepository;
    }

    public void salvar(Saida lote) {
        loteRepository.save(lote);
    }

    public void alterar(Saida lote) {
        loteRepository.save(lote);
    }

    public void remover(Long codigo) {
        loteRepository.deleteById(codigo);
    }

    public void desativar(Long codigo) {
        Saida lote = loteRepository.findById(codigo).get();
        lote.setStatus(Status.INATIVO);
        loteRepository.save(lote);
    }
}
