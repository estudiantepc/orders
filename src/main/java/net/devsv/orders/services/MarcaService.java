package net.devsv.orders.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import net.devsv.orders.interfaces.IMarcaService;
import net.devsv.orders.models.dao.IMarcaDAO;
import net.devsv.orders.models.entities.Marca;

@Service
public class MarcaService implements IMarcaService{
	
	@Autowired
	private IMarcaDAO marcaDAO;
	
	@Override
	public List<Marca> findAll() {
		return (List<Marca>)marcaDAO.findAll();
	}

	@Override
	public Marca finById(Integer id) {
		return marcaDAO.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public Marca save(Marca marca) {
		return marcaDAO.save(marca);
	}

	@Override
	public void delete(Integer id) {
		marcaDAO.deleteById(id);
		
	}

	@Override
	public List<Marca> findByNombre(String nombre) {
		return marcaDAO.findByNombreIgnoreCase(nombre);
	}

}
