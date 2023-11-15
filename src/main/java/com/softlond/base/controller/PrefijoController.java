package com.softlond.base.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.softlond.base.constant.ApiConstant;
import com.softlond.base.entity.Prefijo;
import com.softlond.base.service.PrefijoService;

@RestController
@RequestMapping(ApiConstant.PREFIJO_CONTROLADOR_API)
public class PrefijoController {

	private static final Logger logger = Logger.getLogger(PrefijoService.class);

	@Autowired
	public PrefijoService prefijoService;

	// Listar todos los prefijos
	@GetMapping(value = ApiConstant.PREFIJO_CONTROLADOR_API_LISTAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerTodos() {
		return prefijoService.obtenerTodos();
	}

	// Crear prefijos siendo super
	@PostMapping(value = ApiConstant.PREFIJO_CONTROLADOR_API_CREAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> crerPrefijo(@RequestBody Prefijo prefijoNuevo) {
		return prefijoService.crearPrefijo(prefijoNuevo);
	}

	// Actualizar prefijo
	@PostMapping(value = ApiConstant.PREFIJO_CONTROLADOR_API_ACTUALIZAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> actualizarPrefijo(@RequestBody Prefijo prefijoActualizado) {
		return prefijoService.actualizarPrefijo(prefijoActualizado);
	}

	// Borrar prefijo en el sistema
	@DeleteMapping(value = ApiConstant.PREFIJO_CONTROLADOR_API_ELIMINAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> borrarPrefijo(@RequestParam Integer idPrefijo) {
		return prefijoService.borrarPrefijo(idPrefijo);
	}

	// Obtener prefijos por sede
	@GetMapping(value = ApiConstant.PREFIJO_CONTROLADOR_API_PREFIJO_POR_SEDE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerPrefijoSede() {
		return prefijoService.obtenerPrefijoSede();
	}

	// Obtener prefijos por sede
	@GetMapping(value = ApiConstant.PREFIJO_CONTROLADOR_API_PREFIJO_POR_SEDE_CONTADO, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerPrefijoSedeContado() {
		return prefijoService.obtenerPrefijoSedeContado();
	}
	
	@GetMapping(value = ApiConstant.PREFIJO_CONTROLADOR_API_PREFIJO_POR_SEDE_NOTA_DEVOLUCION, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerPrefijoSedeDevolucion() {
		return prefijoService.obtenerPrefijoDevolucion();
	}
	
	@GetMapping(value = ApiConstant.PREFIJO_CONTROLADOR_API_PREFIJO_POR_SEDE_NOTA_CREDITO, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerPrefijoSedeNotaCredito() {
		return prefijoService.obtenerPrefijoNotaCredito();
	}
	
	@GetMapping(value = ApiConstant.PREFIJO_CONTROLADOR_API_PREFIJO_POR_SEDE_NOTA_DEBITO, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerPrefijoSedeNotaDebito() {
		return prefijoService.obtenerPrefijoNotaDebito();
	}
	
	@GetMapping(value = ApiConstant.PREFIJO_CONTROLADOR_API_PREFIJO_POR_SEDE_REMISION_VENTA, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerPrefijoRemisionVenta() {
		return prefijoService.obtenerPrefijoRemisionVenta();
	}
	
	@GetMapping(value = ApiConstant.PREFIJO_CONTROLADOR_API_PREFIJO_POR_SEDE_NOTA_DEBITO_CLIENTE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerPrefijoSedeNotaDebitoCliente() {
		return prefijoService.obtenerPrefijoNotaDebitoCliente();
	}
	@GetMapping(value = ApiConstant.PREFIJO_CONTROLADOR_API_PREFIJO_POR_SEDE_NOTA_CREDITO_CLIENTE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerPrefijoSedeNotaCreditoCliente() {
		return prefijoService.obtenerPrefijoNotaCreditoCliente();
	}
	@GetMapping(value = ApiConstant.PREFIJO_CONTROLADOR_API_PREFIJO_FACTURA_ELECTRONICA, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerPrefijoSedeFacturaElectronica() {
		return prefijoService.obtenerPrefijoFacturaElectronica();
	}
	@GetMapping(value = ApiConstant.PREFIJO_CONTROLADOR_API_PREFIJO_RECIBO_CAJA, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerPrefijoSedeReciboCaja() {
		return prefijoService.obtenerPrefijoReciboCaja();
	}
	@GetMapping(value = ApiConstant.PREFIJO_CONTROLADOR_API_PREFIJO_INVENTARIO, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerPrefijoSedeInventario() {
		return prefijoService.obtenerPrefijoInventario();
	}
	
	@GetMapping(value = ApiConstant.PREFIJO_CONTROLADOR_API_PREFIJO_POR_SEDE_DEVOLUCION_CREDITO_CLIENTE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerPrefijoSedeDevolucionCliente() {
		return prefijoService.obtenerPrefijoDevolucionCliente();
	}
}
