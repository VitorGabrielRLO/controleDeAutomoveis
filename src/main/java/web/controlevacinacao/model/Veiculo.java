package web.controlevacinacao.model;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;


@Entity
@Table(name = "veiculo")
public class Veiculo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "gerador_veiculo", sequenceName = "veiculo_codigo_seq", allocationSize = 1)
    @GeneratedValue(generator = "gerador_veiculo", strategy = GenerationType.SEQUENCE)
    private Long codigo;

    @NotBlank(message = "A placa é obrigatória!")
    private String placa;

    @NotBlank(message = "O modelo é obrigatório!")
    private String modelo;
    
    @NotNull(message = "O ano é obrigatório")
    @Min(value = 1950, message = "O ano deve ser maior que 1950")
    private int ano;
    
    @NotNull(message = "A quilometragem é obrigatória")
    @Min(value = 0, message = "A quilometragem não pode ser negativa")
    private double quilometragem;

    @Enumerated(EnumType.STRING)
    private Status status = Status.ATIVO;

    @OneToMany(mappedBy = "veiculo")
    private List<Saida> saidas;

    // Getters e Setters
    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public double getQuilometragem() {
        return quilometragem;
    }

    public void setQuilometragem(double quilometragem) {
        this.quilometragem = quilometragem;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
    
    public List<Saida> getSaidas() {
        return saidas;
    }

    public void setSaidas(List<Saida> saidas) {
        this.saidas = saidas;
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
        Veiculo other = (Veiculo) obj;
        return Objects.equals(codigo, other.codigo);
    }
}