package web.controlevacinacao.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "saida")
public class Saida implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name="gerador_saida", sequenceName="saida_codigo_seq", allocationSize=1)
    @GeneratedValue(generator="gerador_saida", strategy=GenerationType.SEQUENCE)
    private Long codigo;

    @NotNull(message = "O funcionário é obrigatório")
    @ManyToOne
    @JoinColumn(name = "codigo_funcionario")
    private Funcionario funcionario;

    @NotNull(message = "O veículo é obrigatório")
    @ManyToOne
    @JoinColumn(name = "codigo_veiculo")
    private Veiculo veiculo;

    private LocalDateTime dataHoraSaida;

    private LocalDateTime dataHoraRetorno;

    private double kmSaida;

    private double kmRetorno;

    @NotBlank(message = "O destino é obrigatório")
    private String destino;

    @Enumerated(EnumType.STRING)
    private Status status = Status.ATIVO;

    // Getters e Setters
    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public LocalDateTime getDataHoraSaida() {
        return dataHoraSaida;
    }

    public void setDataHoraSaida(LocalDateTime dataHoraSaida) {
        this.dataHoraSaida = dataHoraSaida;
    }

    public LocalDateTime getDataHoraRetorno() {
        return dataHoraRetorno;
    }

    public void setDataHoraRetorno(LocalDateTime dataHoraRetorno) {
        this.dataHoraRetorno = dataHoraRetorno;
    }

    public double getKmSaida() {
        return kmSaida;
    }

    public void setKmSaida(double kmSaida) {
        this.kmSaida = kmSaida;
    }

    public double getKmRetorno() {
        return kmRetorno;
    }

    public void setKmRetorno(double kmRetorno) {
        this.kmRetorno = kmRetorno;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Saida other = (Saida) obj;
        return Objects.equals(codigo, other.codigo);
    }
}