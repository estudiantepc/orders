package net.devsv.orders.services;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.devsv.orders.interfaces.IOrdenService;
import net.devsv.orders.models.dao.IDetalleOrdenDAO;
import net.devsv.orders.models.dao.IOrdenDAO;
import net.devsv.orders.models.entities.DetalleOrden;
import net.devsv.orders.models.entities.Orden;

@Service
public class OrdenService implements IOrdenService {

	
	@Autowired
	private IOrdenDAO ordenDAO;
	
	@Autowired
	private IDetalleOrdenDAO detalleOrdenDAO;
	
	@Override
	@Transactional(readOnly = true)
	public List<Orden> findAll(Date fechaInicio) {
		Date fechaFinal = null;
		if(fechaInicio != null) {
			Calendar c = Calendar.getInstance();
			c.setTime(fechaInicio);
			c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
			fechaFinal = c.getTime();
			return ordenDAO.findAllRecibidasWithRangoFechas(fechaInicio, fechaFinal);
		}
		
		return ordenDAO.findAllRecibidas();
	}

	@Override
	@Transactional(readOnly = true)
	public List<Orden> findAllDespachadas(Date fechaInicio) {
		Date fechaFinal = null;
		if(fechaInicio != null) {
			Calendar c = Calendar.getInstance();
			c.setTime(fechaInicio);
			c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
			fechaFinal = c.getTime();
			return ordenDAO.findAllDespachadasWithRangoFechas(fechaInicio, fechaFinal);
		}
		
		return ordenDAO.findAllDespachadas();
	}

	@Override
	public List<Orden> findAllAnuladas(Date fechaInicio) {
		Date fechaFinal = null;
		if(fechaInicio != null) {
			Calendar c = Calendar.getInstance();
			c.setTime(fechaInicio);
			c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
			fechaFinal = c.getTime();
			return ordenDAO.findAllAnuladasWithRangoFechas(fechaInicio, fechaFinal);
		}
		
		return ordenDAO.findAllAnuladas();
	}

	@Override
	public Orden saveOrUpdate(Orden orden) {
		Orden ordenPersisted = null;
		try {
			if(orden.getId() == null) {
				//se va a crear un nuevo registro
				List<DetalleOrden> detalleOrden = orden.getDetalleOrden();
				orden.setDetalleOrden(new ArrayList<DetalleOrden>());
				//seteando el numero de orden
				orden.setCorrelativo(generarCorrelativo());
				//hacemos persistencia en orden
				ordenPersisted = ordenDAO.save(orden);
				//recorrer la coleccion para guardar en el detalle
				for(DetalleOrden detalle: detalleOrden) {
					detalle.setOrden(ordenPersisted);
				}
				//Guardamos todo el detalle 
				detalleOrdenDAO.saveAll(detalleOrden);
			}else {
				//se va a actualizar el registro
				for(DetalleOrden detalle: orden.getDetalleOrden()){
					detalle.setOrden(orden);
				}
				ordenPersisted = ordenDAO.save(orden);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return ordenPersisted;
	}

	@Override
	public Orden changeState(Orden orden) {
		return ordenDAO.save(orden);
	}

	@Override
	public Orden findById(Integer id) {     
		return ordenDAO.findById(id).orElse(null);
	}
	
	//metodo para generar un correlativo
	private String generarCorrelativo() {
	//obtener la fecha del sistema
		LocalDate currentDate = LocalDate.now();
		//obetener el ano y mes de la fecha 
		String yearMonth = currentDate.format(DateTimeFormatter.ofPattern("yyyyMM"));
		//obteniendo el maximo correlativo para el ano y mes
		Long maxCorrelativo = ordenDAO.findMaxCorrelativoByYearMonth(yearMonth);
		//calculando el proximo correlativo
		Long nextCorrelativo = (maxCorrelativo != null) ? maxCorrelativo + 1 : 1;
		//formateando el numero de orden 
		String numeroOrden = yearMonth + String.format("%04d", nextCorrelativo);
		
		return numeroOrden;
		
	}

}
