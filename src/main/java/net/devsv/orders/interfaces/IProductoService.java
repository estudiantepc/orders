package net.devsv.orders.interfaces;

import java.util.List;

import net.devsv.orders.models.entities.Producto;

public interface IProductoService {

	public List<Producto> findAllActivos();
	public List<Producto> findAllInactivos();
	public Producto findById(Integer id);
	public Producto save(Producto producto);
	public Producto changeState(Producto producto);
	public List<Producto> isExist(Producto producto);
}