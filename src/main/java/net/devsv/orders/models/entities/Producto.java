package net.devsv.orders.models.entities;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "productos", schema = "public", catalog = "Orders")
public class Producto implements Serializable{

	private static final long serialVersionUID = 1L;
    
	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "nombre", nullable = false, length = 100)
	private String nombre;
	
	@Column(name = "descripcion", nullable = true, length = 200)
	private String descripcion;
	
	@Column(name = "precio", nullable = false, precision = 2)
	private double precio;
	
	@Column(name = "existencia", nullable = false)
	private int existencia;
	
	@Column(name = "modelo", nullable = true, length = 50)
	private String modelo;
	
	@Column(name = "imagen", nullable = true, length = 100)
	private String imagen;
	
	@Column(name = "estado", nullable = false, length = 1)
	private String estado;
	
	//relacion de muchos a uno
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
	@JoinColumn(name = "categoria_id", referencedColumnName = "id", nullable = false)
	private Categoria categoria;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
	@JoinColumn(name = "marca_id", referencedColumnName = "id", nullable = false)
	private Marca marca;
	
	//seteando el estado
	@PrePersist
	private void setEstado() {
		this.estado = "D";
	}
	
}