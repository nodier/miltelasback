package com.softlond.base.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.softlond.base.constant.ApiConstant;
import com.softlond.base.entity.DevolucionCompras;
import com.softlond.base.request.DevolucionRequest;
import com.softlond.base.request.DevolucionesRequest;
import com.softlond.base.service.DevolucionCompraService;
import com.softlond.base.service.DevolucionVentaService;

@Controller
@RequestMapping(ApiConstant.FACTURAS_DEVOLUCION_API)
public class FacturasDevolucionController {

	@Autowired
	private DevolucionVentaService devolucionService;

	private final Logger logger = Logger.getLogger(getClass());

	@PostMapping(value = ApiConstant.FACTURAS_DEVOLUCION_API_CREAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> crerFactura(@RequestBody DevolucionesRequest factura) {
		return devolucionService.crearFactura(factura);
	}

	@GetMapping(value = ApiConstant.FACTURAS_DEVOLUCION_API_NUMERO, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> numeroFactura() {
		return devolucionService.obtenerNumero();
	}

	@GetMapping(value = ApiConstant.FACTURAS_DEVOLUCION_API_BUSCAR_POR_SEDE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerDevolucion(Integer idSede) {
		return devolucionService.obtenerDevolucion(idSede);
	}

	@GetMapping(value = ApiConstant.FACTURAS_DEVOLUCION_API_BUSCAR_POR_SEDE_CLIENTE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerDevolucionSedeCliente(Integer idSede, Integer idCliente) {
		return devolucionService.obtenerDevolucionSedeCliente(idSede, idCliente);
	}

	@GetMapping(value = ApiConstant.FACTURAS_DEVOLUCION_API_BUSCAR_ID, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerDevolucionFactura(Integer id) {
		return devolucionService.obtenerDevolucionFactura(id);
	}

	@GetMapping(value = ApiConstant.FACTURAS_DEVOLUCION_API_LISTAR_FILTRO, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerListadoDevolucionesCliente(String numeroDocumento,
			String fechaInicial,
			String fechaFinal, Integer estado, Integer page) {
		return devolucionService.obtenerListadoDevolucionesFiltros(numeroDocumento, fechaInicial, fechaFinal, estado, page);
	}

	@DeleteMapping(value = ApiConstant.FACTURAS_DEVOLUCION_API_ELIMINAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> eliminarDevolucion(Integer idDevolucion) {
		return devolucionService.eliminarDevolucion(idDevolucion);
	}

	@PostMapping(value = ApiConstant.FACTURAS_DEVOLUCION_API_ACTUALIZAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> actualizarDevolucion(@RequestBody DevolucionesRequest factura) {
		return devolucionService.actualizarFactura(factura);
	}

	@GetMapping(value = ApiConstant.FACTURAS_DEVOLUCION_API_LISTAR_POR_MES, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerListadoDevolucionesClienteMes(Integer page) {
		return devolucionService.devolucionesMes(page);
	}

	@GetMapping(value = ApiConstant.FACTURAS_DEVOLUCION_API_ESTADONOTACREDITO, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerEstadoNotacredito(String nroDevolucion) {
		return devolucionService.estadoNota(nroDevolucion);
	}
}
