package net.devsv.orders.models.dao;

import java.util.List;


import org.springframework.data.repository.CrudRepository;

import net.devsv.orders.models.entities.Marca;

public interface IMarcaDAO extends CrudRepository<Marca, Integer>{
	List<Marca> findByNombreIgnoreCase(String nombre);
}
