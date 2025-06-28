package web.controlevacinacao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.controlevacinacao.model.Saida;

public interface SaidaRepository extends JpaRepository<Saida, Long> {
    // Futuramente, podemos adicionar consultas customizadas aqui, como:
    // List<Saida> findByDataHoraRetornoIsNull(); // Para encontrar veículos que ainda não voltaram
}