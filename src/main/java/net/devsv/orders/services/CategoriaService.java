package net.devsv.orders.services;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import net.devsv.orders.interfaces.ICategoriaService;
import net.devsv.orders.models.dao.ICategoriaDAO;
import net.devsv.orders.models.entities.Categoria;

@Service
public class CategoriaService implements ICategoriaService{
	
	@Autowired
	private ICategoriaDAO catDAO;

	@Override
	public List<Categoria> findAll() {
		return (List<Categoria>)catDAO.findAll();
	}

	@Override
	public Categoria findById(Integer id) {
		return catDAO.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public Categoria save(Categoria categoria) {
		return catDAO.save(categoria);
	}

	@Override
	public void delete(Integer id) {
		catDAO.deleteById(id);
		
	}

	@Override
	public List<Categoria> findByNombre(String nombre) {
	
		return catDAO.findByNombreIgnoreCase(nombre);
	}

}
