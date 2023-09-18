package net.devsv.orders.controllers;

import java.io.File;


import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import net.devsv.orders.interfaces.IProductoService;
import net.devsv.orders.interfaces.IUploadFileService;
import net.devsv.orders.models.entities.Producto;

@CrossOrigin(origins = "http://localhost:4200/")
@RestController
@RequestMapping("/api")
public class ProductoController {

	@Autowired
	private IProductoService productoService;
	
	@Autowired
	private IUploadFileService uploadService;
	
	private Logger logger = LoggerFactory.getLogger(ProductoController.class);
	
	@GetMapping("/productos")
	public List<Producto> getAllActivos(){
		return productoService.findAllActivos();
	}
	
	@GetMapping("/productos/inactivos")
	public List<Producto> getAllInactivos(){
		return productoService.findAllInactivos();
	}
	
	@GetMapping("/productos/{id}")
	public ResponseEntity<?> getById(@PathVariable Integer id){
		Producto producto = null;
		Map<String, Object> response = new HashMap<>();
		try {
			producto = productoService.findById(id);			
		}catch(DataAccessException e) {
			response.put("message","Error al realizar la consulta a la base de datos");
			response.put("error", e.getMessage());
		}
		if(producto == null) {
			response.put("message","El producto con ID ".concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Producto>(producto, HttpStatus.OK);
	}
	
	//metodo para guardar un producto
	@PostMapping("/productos")
	public ResponseEntity<?> save(@RequestPart Producto producto, @RequestPart(name = "imagen", required = false) MultipartFile imagen) throws IOException{
		String imageNewName = null;
		Map<String, Object> response = new HashMap<>();
		try {
			if(productoService.isExist(producto).size() > 0 && producto.getId() == null) {
			response.put("message","Ya existe un producto con este nombre y descripcion");
			return new ResponseEntity<Map<String,Object>>(response, HttpStatus.CONFLICT);
			}else {
				//hacemos persistente el producto de la bd
				if(imagen != null) imageNewName = uploadService.copyFile(imagen);
				producto.setImagen(imageNewName);
				//guardamos el objeto
				productoService.save(producto);
			}
		}catch(DataAccessException e){
			response.put("message", "Error al insertar el registro a la base de datos");
			logger.error("ERROR: ".concat(e.getMessage()));
			uploadService.deleteFile(imageNewName);
			return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("message", "Producto registrado exitosamente...!");
		response.put("producto", producto);
		return new ResponseEntity<Map<String,Object>>(response, HttpStatus.CREATED);
	}
	
	//metodo para actualizar un producto
	@PutMapping("/productos/{id}")
	public ResponseEntity<?> update(@RequestPart Producto producto, @RequestPart(name = "imagen", required = false) MultipartFile imagen, @PathVariable Integer id) throws IOException{
		String imageNewName = null;
		Producto productoActual = productoService.findById(id);
		Producto productoUpdate = null;
		Map<String, Object> response = new HashMap<>();
		if(productoActual == null) {
			response.put("message", "Error: no se puede editar el producto con ID: ".concat(id.toString().concat(" no existe en la bae de datos")));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.NOT_FOUND);
		}
		try {
			productoActual.setNombre(producto.getNombre());
			productoActual.setDescripcion(producto.getDescripcion());
			productoActual.setPrecio(producto.getPrecio());
			productoActual.setExistencia(producto.getExistencia());
			productoActual.setModelo(producto.getModelo());
			productoActual.setCategoria(producto.getCategoria());
			productoActual.setMarca(producto.getMarca());
			if(imagen !=null) {
				//evaluamos el producto a modificar ya tiene una imagen
				//si es el caso se elimina y se sube la nueva
				if(productoActual.getImagen() != null && productoActual.getImagen().length() >0 ) {
					String imgAnterior = productoActual.getImagen();
					Path rutaImgAnterior = uploadService.getPath(imgAnterior);
					File archivoImgAnterior = rutaImgAnterior.toFile();
					if(archivoImgAnterior.exists() && archivoImgAnterior.canRead()){
							archivoImgAnterior.delete();
					}
				}
				imageNewName = uploadService.copyFile(imagen);
				productoActual.setImagen(imageNewName);
			}
			productoUpdate = productoService.save(productoActual);
		}catch(DataAccessException e) {
			response.put("message", "Error al actualizar el registro, intente de nuevo");
			response.put("error", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("message", "Producto actualizado con exito...!");
		response.put("producto", productoUpdate);
		return new ResponseEntity<Map<String,Object>>(response, HttpStatus.CREATED);	
	}
	//metodo para cambiar el estado del producto
	
	@PutMapping("/productos/change-state")
	public ResponseEntity<?> changeState(@RequestBody Producto producto,
			@RequestParam(name = "estado")String estado){
		Map<String, Object> response = new HashMap<>();
		try {
		   producto.setEstado(estado);
		  productoService.save(producto);
		}catch(DataAccessException e) {
			response.put("message", "Error al actualizar el estado intente de nuevo");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		String strEstado = null;
		if(estado.toString().equals("D"))
			strEstado = "Disponible";
		else
			strEstado = "Inactivo";
		response.put("message","El Estado del producto ha sido cambiado: " +strEstado );
		return new ResponseEntity<Map<String,Object>>(response, HttpStatus.CREATED);
	}
	
	
}