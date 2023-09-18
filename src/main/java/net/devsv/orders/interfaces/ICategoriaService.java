package net.devsv.orders.interfaces;

import java.util.List;

import net.devsv.orders.models.entities.Categoria;

public interface ICategoriaService {
	
	public List<Categoria> findAll();
	public Categoria findById(Integer id);
	public Categoria save(Categoria categoria);
	public void delete(Integer id);
	public List<Categoria> findByNombre(String nombre);
	

}
