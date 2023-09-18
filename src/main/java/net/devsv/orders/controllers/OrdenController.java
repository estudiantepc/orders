package net.devsv.orders.controllers;

import java.util.Date;



import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.devsv.orders.interfaces.IOrdenService;
import net.devsv.orders.models.entities.Orden;

@CrossOrigin(origins = "http://localhost:4200/")
@RestController
@RequestMapping("/api")
public class OrdenController {
	@Autowired
	private IOrdenService ordenService;
	
	@GetMapping("/ordenes")
	public List<Orden> getAllResibidas(@RequestParam(name = "fecha", required = false) Date fecha){
		return ordenService.findAll(fecha);
	}
	@GetMapping("/ordenes/despachadas")
	public List<Orden> getAllDespacahadas(@RequestParam(name = "fecha", required = false) Date fecha){
		return ordenService.findAllDespachadas(fecha);
	}
	@GetMapping("/ordenes/anuladas")
	public List<Orden> getAllAnuladas(@RequestParam(name = "fecha", required = false) Date fecha){
		return ordenService.findAllAnuladas(fecha);
	}
	
	@GetMapping("/ordenes/{id}")
	public ResponseEntity<?> getById(@PathVariable Integer id){
		Orden orden = null;
		Map<String, Object> response = new HashMap<>();
		try {
			orden = ordenService.findById(id);			
		}catch(DataAccessException e) {
			response.put("message","Error al realizar la consulta a la base de datos");
			response.put("error", e.getMessage());
		}
		if(orden == null) {
			response.put("message","La orden con ID ".concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Orden>(orden, HttpStatus.OK);
	}
	
	@PostMapping("/ordenes")
	public ResponseEntity<?> saveOrUpdate(@RequestBody Orden orden){
		Map<String, Object> response = new HashMap<>();
		Orden ordenReturn = null;
		try {
			ordenReturn = ordenService.saveOrUpdate(orden);
		}catch(DataAccessException e){
			response.put("error","Error al insertar la orden en la base de datos"+e.getMessage());
			return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}	
		response.put("message",("Orden registrada con exito...!"));
		response.put("orden", ordenReturn);
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CREATED);
	}
	
	//metodo para cambiar el estado de la orden 
	@PutMapping("/ordenes/change-state")
	public ResponseEntity<?> changeState(@RequestBody Orden orden, @RequestParam(name = "estado") String estado){
		Map<String, Object> response = new HashMap<>();
		try {
			
		    orden.setEstado(estado);
		    if(estado.equals("D")) {
		    	Date fechaDesp = new Date ();
		    	orden.setFechaDespacho(fechaDesp);
		    }
			ordenService.saveOrUpdate(orden);
		}catch(DataAccessException e) {
			response.put("message","Error al cambiar el estado de la orden");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		String strEstado = null;
		if(estado.toString().equals("D"))
		strEstado = "Despachada";
		else
			
			strEstado = "Anuladas";
		response.put("message", "El estado de la orden ha sido cambiado a: "+strEstado);
		return new ResponseEntity<Map<String,Object>>(response, HttpStatus.OK);
	}
	

}