package net.devsv.orders.interfaces;

import java.util.Date;


import java.util.List;

import net.devsv.orders.models.entities.Orden;



public interface IOrdenService {

	public List<Orden> findAll(Date fecha);
	
	public List<Orden> findAllDespachadas(Date fecha);
	
	public List<Orden> findAllAnuladas(Date fecha);
	
	public Orden saveOrUpdate(Orden orden);
	
	public Orden changeState(Orden orden);
	
	public Orden findById(Integer id);
}
 