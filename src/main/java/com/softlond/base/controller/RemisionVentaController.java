package com.softlond.base.controller;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.softlond.base.constant.ApiConstant;
import com.softlond.base.entity.Articulo;
import com.softlond.base.entity.FacturaArticulos;
import com.softlond.base.entity.RemisionVenta;
import com.softlond.base.service.RemisionVentaService;

import ch.qos.logback.classic.Logger;



@RestController
@RequestMapping(ApiConstant.REMISION_VENTA_API)
public class RemisionVentaController {
	
	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	public RemisionVentaService remisionVentaService;

	// Crear remisiones de venta ,listaIdsArticulos,listaCantArticulos ,Number[] listaIdsArticulos, Number[] listaCantArticulos
	@PostMapping(value = ApiConstant.REMISION_VENTA_API_CREAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> crerRemisionVenta(@RequestBody RemisionVenta remision) {
		return remisionVentaService.crearRemision(remision);
	}

	// actualizar
	@PutMapping(value = ApiConstant.REMISION_VENTA_API_ACTUALIZAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> actualizarRemisionVenta(@RequestBody RemisionVenta remision) {
		return remisionVentaService.actualizarRemisionVenta(remision);
	}
	
	// ajustar remisiones obtenidas
		@PutMapping(value = ApiConstant.REMISION_VENTA_API_AJUSTAR_REMISIONES_VENTA, produces = MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody ResponseEntity<Object> ajustarRemisionVenta(@RequestBody FacturaArticulos[] factura) {	
			logger.info("ingresa a REMISION_VENTA_API_AJUSTAR_REMISIONES_VENTA "+factura);
			return remisionVentaService.ajustarRemisionVenta(factura);
		}

	// Listar remisiones de venta
	@GetMapping(value = ApiConstant.REMISION_VENTA_API_LISTAR_TODOS_SEDE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> listarRemisioVenta(Integer page, String ordenarPor) {
		return remisionVentaService.listarRemisioVenta(page, ordenarPor);
	}

	@GetMapping(value = ApiConstant.REMISION_VENTA_API_LISTAR_TODOS_CONSULTA, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerRemisionVentaConsulta(String fechaInicial, String fechaFinal,
			String numeroRemision, Integer estado, Integer cliente, Integer vendedor, Integer page) {
		return this.remisionVentaService.obtenerRemisionVentaConsulta(fechaInicial, fechaFinal, numeroRemision, estado,
				cliente, vendedor, page);
	}

	/* Borrar Remiision venta */
	@DeleteMapping(value = ApiConstant.REMISION_VENTA_API_ELIMINAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> borrarRemisionVenta(Integer id) {
		return remisionVentaService.borrarRemisionVenta(id);
	}

	// Obtener numero maximo
	@GetMapping(value = ApiConstant.REMISION_VENTA_API_OBTENER_NUMERO_MAXIMO, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerNumeroM() {
				return remisionVentaService.obtenerNumeroM();
	}

	// Listar remisiones de venta
	@GetMapping(value = ApiConstant.REMISION_VENTA_API_OBTENER, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerRemisionVenta(String numero) {	
		return remisionVentaService.obtenerRemision(numero);
	}
	
	
	// Listar remisiones de venta
		@GetMapping(value = ApiConstant.REMISION_VENTA_API_LISTAR_TODOS_SEDE2, produces = MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody ResponseEntity<Object> listarRemisioVenta2(Integer id, Integer page) {
			return remisionVentaService.listarRemisioVenta2(id, page);
		}
		/*
	// Listar remisiones de venta
		@GetMapping(value = ApiConstant.REMISION_VENTA_API_LISTAR_TODOS_SEDE2, produces = MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody ResponseEntity<Object> listarRemisioVenta2() {
			return remisionVentaService.listarRemisioVenta2();
		}*/
		
		@GetMapping(value = ApiConstant.REMISION_VENTA_API_LISTAR_TODOS_CONSULTA2, produces = MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody ResponseEntity<Object> obtenerRemisionVentaConsulta2(Integer vendedor, String fechaInicial, String fechaFinal, Integer page) {
			return this.remisionVentaService.obtenerRemisionVentaConsulta2(vendedor, fechaInicial, fechaFinal, page);
		}

	// Listar remisiones de venta
	@GetMapping(value = ApiConstant.REMISION_VENTA_API_LISTAR_PENDIENTES, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerRemisionesPendientes(Integer page) {
		return remisionVentaService.obtenerRemisionesPendientes(page);
	}
	
	// Listar remisiones de venta
		@GetMapping(value = ApiConstant.REMISION_VENTA_API_CEDULA_VENDEDOR, produces = MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody ResponseEntity<Object> listarPorCedulaVendedor(String nitocc) {
			return remisionVentaService.listarPorCedulaVendedor(nitocc);
		}
		
		@GetMapping(value = ApiConstant.REMISION_VENTA_API_OBTENER_IMPRESION, produces = MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody ResponseEntity<Object> obtenerRemisionImpresion(String numero) {		
			return remisionVentaService.obtenerRemisionImpresion(numero);	
		}
}


