package net.devsv.orders.interfaces;

import java.util.List;


import net.devsv.orders.models.entities.Marca;

public interface IMarcaService {
	
	public List<Marca> findAll();
	public Marca finById(Integer id);
	public Marca save(Marca marca);
	public void delete(Integer id);
	public List<Marca> findByNombre(String nombre);
	
}
