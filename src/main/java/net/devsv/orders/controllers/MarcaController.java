package net.devsv.orders.controllers;

import java.util.HashMap;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import net.devsv.orders.interfaces.IMarcaService;
import net.devsv.orders.models.entities.Marca;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200/")
public class MarcaController {
	
	@Autowired
	private IMarcaService marcaService; 

	@GetMapping("/marcas")
	public List<Marca> getAll(){
		return marcaService.findAll();
	}
	
	
	@GetMapping("/marcas/{id}")
	public ResponseEntity<?> getById(@PathVariable Integer id){
		Marca marca = null;
		Map<String, Object> response = new HashMap<>();
		try {
			marca = marcaService.finById(id);
		}catch(DataAccessException e){
			response.put("message","Error al realizar la consulta a la base de datos");
			response.put("error",e.getMessage());
		}
		if(marca == null) {
			response.put("message", "La categoria con ID: ".concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Marca>(marca,HttpStatus.OK);
	}
	
	//metodo para guardar una marca
	@PostMapping("/marcas")
	public ResponseEntity<?> save(@RequestBody Marca marca){
		Map<String, Object> response = new HashMap<>();
		try {
			
			if(marcaService.findByNombre(marca.getNombre()).size() > 0 && marca.getId() == null) {
				response.put("message", "Ya existe una marca con ese nombre en la base de datos");
				return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CONFLICT);
			}else {
				marcaService.save(marca);
			}
		}catch(DataAccessException e) {
			response.put("message","Error al insertar el registro, intente de nuevo");
			response.put("error",e.getMessage());
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("message", "Marca registrada con exito...!");
		response.put("marca", marca);
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CREATED);
	}
	//metodo para actualizar una marca
	
	@PutMapping("/marcas/{id}")
	public ResponseEntity<?> update(@RequestBody Marca marca, @PathVariable Integer id){
		Marca marcaActual = marcaService.finById(id);
		Marca marcaUpdate = null;
		Map<String, Object> response = new HashMap<>();
		if(marcaActual == null) {
			response.put("message", "Error: no se puede editar la categoria con id: ".concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.NOT_FOUND);
		}
		try {
			marcaActual.setNombre(marca.getNombre());
			marcaUpdate = marcaService.save(marcaActual);
		}catch(DataAccessException e) {
			response.put("message", "Error al actualizar el registro, intente de nuevo");
			response.put("error", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("message", "Marca actualizada con exito...!");
		response.put("marca", marcaUpdate);
		return new ResponseEntity<Map<String,Object>>(response, HttpStatus.CREATED);	
	}
	
	@DeleteMapping("/marcas/{id}")
	public ResponseEntity<?> delete(@PathVariable Integer id){
		Map<String, Object> response = new HashMap<>();
		Marca marcaActual = marcaService.finById(id);
		if(marcaActual == null) {
			response.put("message", "Error: no se puede eliminar la marca con id: ".concat(id.toString().concat(" no existe en la bae de datos")));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.NOT_FOUND);
		}
		try {
			marcaService.delete(id);
	    }catch(DataAccessException e) {
	    	response.put("message", "No se puede eliminar la marca, ya tiene productos registrados");
			response.put("error", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("message", "Marca eliminada con exito...!");
		return new ResponseEntity<Map<String,Object>>(response, HttpStatus.OK);
	}
}
