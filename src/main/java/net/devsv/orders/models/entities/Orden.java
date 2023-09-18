package net.devsv.orders.models.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "ordenes", schema = "public", catalog = "Orders")
public class Orden implements Serializable{

	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "correlativo", nullable = false, length = 10)
	private String correlativo;
	
	@Column(name = "fecha_orden", nullable = false)
	private Date fechaOrden;
	
	@Column(name = "fecha_despacho", nullable = true)
	private Date fechaDespacho;
	
	@Column(name = "monto", nullable = false, precision = 2)
	private double monto;
	
	@Column(name = "impuesto", nullable = false, precision = 2)
	private double impuesto;
	
	@Column(name = "total_envio", nullable = true, precision = 2)
	private double totalEnvio;
	
	@Column(name = "estado", nullable = false, length = 1)
	private String estado;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
	@JoinColumn(name = "usuario_id", referencedColumnName = "id", nullable = false)
	private Usuario usuario;
	
	//prendiente de crear relacion de 1:n con DetalleOrden
	@OneToMany(mappedBy = "orden", fetch=FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<DetalleOrden> detalleOrden;

}
