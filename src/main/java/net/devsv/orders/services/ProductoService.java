package net.devsv.orders.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.devsv.orders.interfaces.IProductoService;
import net.devsv.orders.models.dao.IProductoDAO;
import net.devsv.orders.models.entities.Producto;

@Service 

public class ProductoService implements IProductoService{
	
	@Autowired
	private IProductoDAO productoDAO;
	
	@Override
	public List<Producto> findAllActivos() {
		return productoDAO.findAll();
	}

	@Override
	public List<Producto> findAllInactivos() {
		
		return productoDAO.findAllInactivos();
	}

	@Override
	public Producto findById(Integer id) {
		return productoDAO.findById(id).orElse(null);
	}

	@Override
	public Producto save(Producto producto) {
		return productoDAO.save(producto);
	}

	@Override
	public Producto changeState(Producto producto) {
		return  productoDAO.save(producto);
	}

	@Override
	public List<Producto> isExist(Producto producto) {
		   
		return productoDAO.findByNombreDescripcion(producto);
	}
	
}

