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

import net.devsv.orders.interfaces.ICategoriaService;
import net.devsv.orders.models.entities.Categoria;

@RestController
@CrossOrigin(origins = "http://localhost:4200/")
@RequestMapping("/api")
public class CategoriaController {
	
	@Autowired
	private ICategoriaService catService; 

	@GetMapping("/categorias")
	public List<Categoria> getAll(){
		return catService.findAll();
	}
	
	@GetMapping("/categorias/{id}")
	public ResponseEntity<?> getById(@PathVariable Integer id){
		Categoria categoria = null;
		Map<String, Object> response = new HashMap<>();
		try {
			categoria = catService.findById(id);
		}catch(DataAccessException e){
			response.put("message","Error al realizar la consulta a la base de datos");
			response.put("error",e.getMessage());
		}
		if(categoria == null) {
			response.put("message", "La categoria con ID: ".concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Categoria>(categoria,HttpStatus.OK);
	}
	
	//metodo para guardar una categoria
	@PostMapping("/categorias")
	public ResponseEntity<?> save(@RequestBody Categoria categoria){
		Map<String, Object> response = new HashMap<>();
		try {
			
			if(catService.findByNombre(categoria.getNombre()).size() > 0 && categoria.getId() == null) {
				response.put("message", "Ya existe una categoria con ese nombre en la base de datos");
				return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CONFLICT);
			}else {
				catService.save(categoria);
			}
		}catch(DataAccessException e) {
			response.put("message","Error al insertar el registro, intente de nuevo");
			response.put("error",e.getMessage());
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("message", "Categoria registrada con exito...!");
		response.put("categoria", categoria);
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CREATED);
	}
	//metodo para actualizar una categoria
	
	@PutMapping("/categorias/{id}")
	public ResponseEntity<?> update(@RequestBody Categoria cat, @PathVariable Integer id){
		Categoria catActual = catService.findById(id);
		Categoria catUpdate = null;
		Map<String, Object> response = new HashMap<>();
		if(catActual == null) {
			response.put("message", "Error: no se puede editar la categoria con id: ".concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.NOT_FOUND);
		}
		try {
			catActual.setNombre(cat.getNombre());
			catUpdate = catService.save(catActual);
		}catch(DataAccessException e) {
			response.put("message", "Error al actualizar el registro, intente de nuevo");
			response.put("error", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("message", "Categoria actualizada con exito...!");
		response.put("categoria", catUpdate);
		return new ResponseEntity<Map<String,Object>>(response, HttpStatus.CREATED);	
	}
	
	@DeleteMapping("/categorias/{id}")
	public ResponseEntity<?> delete(@PathVariable Integer id){
		Map<String, Object> response = new HashMap<>();
		Categoria catActual = catService.findById(id);
		if(catActual == null) {
			response.put("message", "Error: no se puede eliminar la categoria con id: ".concat(id.toString().concat(" no existe en la bae de datos")));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.NOT_FOUND);
		}
		try {
			catService.delete(id);
	    }catch(DataAccessException e) {
	    	response.put("message", "No se puede eliminar la categoria, ya tiene productos registrados");
			response.put("error", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("message", "Categoria eliminada con exito...!");
		return new ResponseEntity<Map<String,Object>>(response, HttpStatus.OK);
	}
}