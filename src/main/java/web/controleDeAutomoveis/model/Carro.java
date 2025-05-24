package web.controleDeAutomoveis.model;

import java.io.Serializable;

import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name="Carro")
@DynamicUpdate
public class Carro implements Serializable {

	private static final long serialVersionUID = 7562368353372595992L;
	
	@Id
	@SequenceGenerator(name="gerador5", sequenceName="carro_id_codigo_seq", allocationSize=1)
	@GeneratedValue(generator="gerador5", strategy = GenerationType.SEQUENCE)
	private Long carro_id;
	@NotBlank(message = "O modelo é obrigatório!")
	private String modelo;
	@NotBlank(message = "A marca é obrigatório!")
	private String marca;
	@NotBlank(message = "A placa é obrigatória!")
	private String placa;
	@Enumerated(EnumType.STRING)
	private Status status = Status.ATIVO;

	public Long getCodigo() {
		return carro_id;
	}

	public void setCodigo(Long carro_id) {
		this.carro_id = carro_id;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
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
		result = prime * result + ((carro_id == null) ? 0 : carro_id.hashCode());
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
		Carro other = (Carro) obj;
		if (carro_id == null) {
			if (other.carro_id != null)
				return false;
		} else if (!carro_id.equals(other.carro_id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Id do carro: " + carro_id + "\n Modelo: " + modelo + "\nMarca: " + marca + "\nPlaca: " + placa
				+ "\nStatus: " + status;
	}

}
