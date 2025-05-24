package web.controleDeAutomoveis.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Column;
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
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import web.controleDeAutomoveis.validation.IntegerAttributesRelation;
import web.controleDeAutomoveis.validation.util.AttributesRelation;

@Entity
@Table(name = "saida")
@DynamicUpdate
@IntegerAttributesRelation(attribute1 = "kmSaida", attribute2 = "kmVolta", attribute3 ="nroDeUsos", relation = AttributesRelation.GREATEROREQUAL)
public class Saida implements Serializable {

	private static final long serialVersionUID = -3935828642122652510L;

	@Id
	@SequenceGenerator(name = "gerador4", sequenceName = "saida_codigo_seq", allocationSize = 1)
	@GeneratedValue(generator = "gerador4", strategy = GenerationType.SEQUENCE)
	private Long codigo;
	@NotNull(message = "A hora que saiu é obrigatória")
	private LocalDateTime saida;
	@NotNull(message = "A hora que voltou é obrigatória")
	private LocalDateTime volta;
	@Min(value = 0, message = "O km de saída não pode ser negativo")
	@NotNull(message = "O km de saída é obrigatório")
	@Column(name = "km_saida")
	private Integer kmSaida;
	@Min(value = 0, message = "O km de volta não pode ser negativo")
	@NotNull(message = "O km de volta é obrigatório")
	@Column(name = "km_volta")
	private Integer kmVolta;
	@NotNull(message = "O número de doses do lote é obrigatório")
	@Column(name = "nro_doses_do_lote")
	private Integer nroDeUsos;
	@NotNull(message = "O carro é obrigatório")
	@ManyToOne
	@JoinColumn(name = "carro_id")
	private Carro carro;
	@Enumerated(EnumType.STRING)
	private Status status = Status.ATIVO;

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public LocalDateTime getSaida() {
		return saida;
	}

	public void setSaida(LocalDateTime saida) {
		this.saida = saida;
	}
	public LocalDateTime getVolta() {
		return volta;
	}
	public void setVolta(LocalDateTime volta) {
		this.volta = volta;
	}
	public Integer getKmSaida() {
		return kmSaida;
	}
	public void setKmSaida(Integer kmSaida) {
		this.kmSaida = kmSaida;
	}
	public Integer getKmVolta() {
		return kmVolta;
	}
	public void setKmVolta(Integer kmVolta) {
		this.kmVolta = kmVolta;
	}
	public Integer getNroDeUsos() {
		return nroDeUsos;
	}
	public void setNroDeUsos(Integer nroDeUsos) {
		this.nroDeUsos = nroDeUsos;
	}
	public Carro getCarro() {
		return carro;
	}
	public void setCarro(Carro carro) {
		this.carro = carro;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		return result;
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
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Saida [codigo=" + codigo + ", saida=" + saida + ", volta=" + volta + ", kmSaida=" + kmSaida
				+ ", kmVolta=" + kmVolta + ", carro=" + carro + ", nroDeUsos=" + nroDeUsos
				+ ", status=" + status + "]";
	}

}
