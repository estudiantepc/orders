package net.devsv.orders.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import net.devsv.orders.models.entities.Producto;

public interface IProductoDAO extends CrudRepository<Producto,Integer>{
	
	@Query("FROM Producto p WHERE p.nombre=:#{#producto.nombre} and p.descripcion=:#{#producto.descripcion}")
	List<Producto> findByNombreDescripcion(Producto producto);
	
	@Query("FROM Producto p WHERE p.estado = 'D' ORDER BY p.id DESC")
	List<Producto> findAll();
	
	@Query("FROM Producto p WHERE p.estado = 'I' ORDER BY p.id DESC")
	List<Producto> findAllInactivos();
}