package com.softlond.base.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.softlond.base.constant.ApiConstant;
import com.softlond.base.entity.DevolucionCompras;
import com.softlond.base.request.DevolucionRequest;
import com.softlond.base.service.DevolucionCompraService;

@Controller
@RequestMapping(ApiConstant.FACTURA_DEVOLUCION_API)
public class FacturaDevolucionController {

	@Autowired
	private DevolucionCompraService devolucionService;
	
	@PostMapping(value = ApiConstant.FACTURA_DEVOLUCION_API_CREAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> crerFactura(@RequestBody DevolucionRequest factura) {
		return devolucionService.crearFactura(factura);
	}

	@GetMapping(value = ApiConstant.FACTURA_DEVOLUCION_API_NUMERO, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> numeroFactura() {
		return devolucionService.obtenerNumero();
	}
	
	@GetMapping(value = ApiConstant.FACTURA_DEVOLUCION_API_BUSCAR_POR_SEDE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerDevolucion(Integer idSede ) {
		return devolucionService.obtenerDevolucion(idSede);
	}
	
	@GetMapping(value = ApiConstant.FACTURA_DEVOLUCION_API_BUSCAR_POR_SEDE_PROVEEDOR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerDevolucionSedeProveedor(Integer idSede, Integer idProveedor ) {
		return devolucionService.obtenerDevolucionSedeProveedor(idSede,idProveedor);
	}
	
	@GetMapping(value = ApiConstant.FACTURA_DEVOLUCION_API_BUSCAR_DEVOLUCIONES_ANULAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerDevolucionAnular(String numero) {
		return devolucionService.ObtenerDevolucionPorNumero(numero);
	}
	
	@PostMapping(value = ApiConstant.FACTURA_DEVOLUCION_API_ANULAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> anularFactura(@RequestBody DevolucionCompras factura) {
		return devolucionService.anularFactura(factura);
	}
}
