package com.softlond.base.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.softlond.base.constant.ApiConstant;
import com.softlond.base.service.PrdDescuentosService;
import com.softlond.base.entity.Clientes;
import com.softlond.base.entity.PrdDescuentos;


@RestController
@RequestMapping(ApiConstant.DESCUENTOS_CONTROLADOR_API)

public class PrdDescuentosController {
	
	@Autowired
	private PrdDescuentosService prdDescuentosService;
	
	/* Listar todos los descuentos */
	@GetMapping(value = ApiConstant.DESCUENTOS_CONTROLADOR_API_LISTAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> obtenerDescuentos() {
		return prdDescuentosService.obtenerDescuentos();
	}
	
	@GetMapping(value = ApiConstant.DESCUENTOS_CONTROLADOR_API_LISTAR_TODOS, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> obtenerTodosDescuentos(@PathVariable Integer page){
		return this.prdDescuentosService.obtenerTodosDescuentos(page);
	}
	
	/*Crear Descuento*/
	@PostMapping(value = ApiConstant.DESCUENTOS_CONTROLADOR_API_CREAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> crearDescuento(@RequestBody PrdDescuentos descuentos){
		return this.prdDescuentosService.crearDescuento(descuentos);
	}
	
	//Actualizar Descuento
	@PutMapping(value = ApiConstant.DESCUENTOS_CONTROLADOR_API_ACTUALIZAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> actualizarDescuento(@RequestBody PrdDescuentos descuentos){
		return this.prdDescuentosService.actualizarDescuento(descuentos);
	}
	
	//Eliminar Decuento
	@DeleteMapping(value = ApiConstant.DESCUENTOS_CONTROLADOR_API_ELIMINAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> eliminarDescuento(Integer idDescuento){
		return this.prdDescuentosService.eliminarDescuento(idDescuento);
	}
	
	@GetMapping(value = ApiConstant.DESCUENTOS_CONTROLADOR_API_LISTAR_TODOS_CONSULTA, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> obtenerDescuentoConsulta(@PathVariable Integer page, Integer cliente, String activo){
		return this.prdDescuentosService.obtenerDescuentoConsulta(cliente, activo, page);
	}
	
	@GetMapping(value = ApiConstant.DESCUENTOS_CONTROLADOR_API_LISTAR_TODOS_CONSULTAS, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> obtenerDescuentoConsultas(@PathVariable Integer page, Integer clasificacion, String activo, Integer tipo, Integer referencia){
		return this.prdDescuentosService.obtenerDescuentosConsultas(page, clasificacion, activo, tipo, referencia);
	}
	
	@GetMapping(value = ApiConstant.DESCUENTOS_CONTROLADOR_API_LISTAR_TODOS_PRODUCTO, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> obtenerTodosDescuentosProducto(@PathVariable Integer page, Integer producto){
		return this.prdDescuentosService.obtenerTodosDescuentosProducto(page, producto);
	}
	
	@GetMapping(value = ApiConstant.DESCUENTOS_CONTROLADOR_API_LISTAR_TODOS_CONSULTAS_PRODUCTO, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> obtenerDescuentoConsultas(@PathVariable Integer page, Integer clasificacion, String activo, 
			Integer tipo, Integer referencia, Integer producto){
		return this.prdDescuentosService.obtenerDescuentosConsultasProducto(page, clasificacion, activo, tipo, referencia, producto);
	}
	
	@GetMapping(value = ApiConstant.DESCUENTOS_CONTROLADOR_API_OBTENER_POR_CLIENTE_CLASIFICACION, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> obtenerPorClienteClasificacion(Integer idClasificacion, Integer idTipo, Integer idReferencia, Integer idPresentacion){
		return this.prdDescuentosService.obtenerPorClienteClasificacion(idClasificacion, idTipo,idReferencia,idPresentacion);
	}

}
