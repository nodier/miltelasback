package com.softlond.base.controller;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.softlond.base.entity.Impuesto;
import com.softlond.base.constant.ApiConstant;
import com.softlond.base.entity.Menu;
import com.softlond.base.service.ImpuestoService;
import com.softlond.base.entity.Factura;
import org.apache.log4j.Logger;

@Controller
@RequestMapping(ApiConstant.IMPUESTOS_API)
public class impuestoController {
	
	private static final Logger logger = Logger.getLogger(ImpuestoService.class);
	@Autowired
	private ImpuestoService impuestoService;

	/* Obtener impuestos */
	@GetMapping(value = ApiConstant.IMPUESTOS_API_LISTAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerTodosLosImpuestos() {
		return this.impuestoService.listarTodos();
	}
	
	/* Obtener impuestos */
	@GetMapping(value = ApiConstant.IMPUESTOS_API_LISTAR_RECIBO, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerTodosLosImpuestosRecibo() {
		return this.impuestoService.listarTodosRecibo();
	}

	/* Obtener impuestos Factura*/
	@GetMapping(value = ApiConstant.IMPUESTOS_API_FACTURA, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerImpuestoFactura(Integer factura) {
		return this.impuestoService.ObtenerImpuestoFactura(factura);
	}
	/*crear impuestos*/
	@PostMapping(value = ApiConstant.IMPUESTOS_API_CREAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> crearImpuesto(@RequestBody Impuesto newImpuesto) {
		return this.impuestoService.crearImpuesto(newImpuesto);
	}
	
	/*crear impuestos*/
	@PostMapping(value = ApiConstant.IMPUESTOS_API_ACTUALIZAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> actualizarImpuesto(@RequestBody Impuesto newImpuesto) {
		return this.impuestoService.actualizarImpuesto(newImpuesto);
	}
	
	/* Metodo para elminar los impuestos en el sistema */
	@DeleteMapping(value = ApiConstant.IMPUESTOS_API_ELIMINAR)
	public ResponseEntity<Object> borrarMenu(Integer id) {
		return this.impuestoService.borrarImpuesto(id);
	}
	
	/* Obtener impuestos */
	@GetMapping(value = ApiConstant.IMPUESTOS_API_CONEXION, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> obtenerImpuestosConexion(String tipoImpuesto) {
		return this.impuestoService.ObtenerImpuestosConexion(tipoImpuesto);
	}
	
	@PostMapping(value = ApiConstant.IMPUESTOS_API_ENLACE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> enlaceImpuesto(@RequestBody Impuesto newImpuesto) {
		return this.impuestoService.enlazarImpuesto(newImpuesto);
	}
	
	@PostMapping(value = ApiConstant.IMPUESTOS_API_ELIMINARENLACE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> enlaceEliminar(@RequestBody Impuesto eliminarEnlace) {
		return this.impuestoService.eliminarEnlace(eliminarEnlace);
	}
	
}


