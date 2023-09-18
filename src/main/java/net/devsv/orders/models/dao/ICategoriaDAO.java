package net.devsv.orders.models.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import net.devsv.orders.models.entities.Categoria;

public interface ICategoriaDAO extends CrudRepository<Categoria, Integer>{
	List<Categoria> findByNombreIgnoreCase(String nombre);
}
