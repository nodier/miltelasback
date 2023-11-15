package com.softlond.base.controller;

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
import com.softlond.base.entity.RemisionCompra;
import com.softlond.base.service.RemisionCompraService;

@RestController
@RequestMapping(ApiConstant.REMISION_COMPRA_API)
public class RemisionCompraController {

	@Autowired
	public RemisionCompraService remisionCompraService;
	
	// Crear remisiones de compra
			@PostMapping(value = ApiConstant.REMISION_COMPRA_API_CREAR, produces = MediaType.APPLICATION_JSON_VALUE)
			public @ResponseBody ResponseEntity<Object> crerRemisionCompra(@RequestBody RemisionCompra remision) {
				return remisionCompraService.crearRemision(remision);
			}
	//Obtener remision por codigo
			@GetMapping(value = ApiConstant.REMISION_COMPRA_API_BUSCAR_POR_CODIGO, produces = MediaType.APPLICATION_JSON_VALUE)
			public @ResponseBody ResponseEntity<Object> obtenerRemisionCodigo(String idRemision, Integer idProveedor) {
				return remisionCompraService.obtenerRemisionCodigo(idRemision, idProveedor);
			}
	//busqueda por filtro
			@GetMapping(value = ApiConstant.REMISION_COMPRA_API_BUSCAR_FILTRO, produces = MediaType.APPLICATION_JSON_VALUE)
			public @ResponseBody ResponseEntity<Object> obtenerRemisionFiltros(Integer idProveedor, String numeroRemision, String fechaInicial, String fechaFinal,
					String estado, Integer page) throws Exception {
				return remisionCompraService.obtenerRemisionesFiltros(idProveedor, numeroRemision, fechaInicial, fechaFinal,estado, page);
			}
			
	//busqueda inicial remisiones
			@GetMapping(value = ApiConstant.REMISION_COMPRA_API_BUSCAR_MES, produces = MediaType.APPLICATION_JSON_VALUE)
			public @ResponseBody ResponseEntity<Object> obtenerRemision(Integer page) throws Exception {
				return remisionCompraService.obtenerRemisionesMes(page);
			}
  //actualizar
			@PostMapping(value = ApiConstant.REMISION_COMPRA_API_ACTUALIZAR, produces = MediaType.APPLICATION_JSON_VALUE)
			public @ResponseBody ResponseEntity<Object> actualizarRemisionCompra(@RequestBody RemisionCompra remision) {
				return remisionCompraService.actualizarRemision(remision);
			}

			//Eliminar
			@DeleteMapping(value = ApiConstant.REMISION_COMPRA_API_ELIMINAR, produces = MediaType.APPLICATION_JSON_VALUE)
			public @ResponseBody ResponseEntity<Object> borrarRemision(@RequestParam Integer idRemision) {
				return remisionCompraService.borrarRemision(idRemision);
			}
			
			//Obtener remisiones
			@GetMapping(value = ApiConstant.REMISION_COMPRA_API_LISTAR_TODO, produces = MediaType.APPLICATION_JSON_VALUE)
			public @ResponseBody ResponseEntity<Object> obtenerRemisiones(Integer idProveedor) {
				return remisionCompraService.obtenerRemisiones(idProveedor);
			}
			
			//Obtener remisiones
			@GetMapping(value = ApiConstant.REMISION_COMPRA_API_LISTAR_FACTURA, produces = MediaType.APPLICATION_JSON_VALUE)
			public @ResponseBody ResponseEntity<Object> obtenerRemisionesFactura(Integer idFactura) {
				return remisionCompraService.obtenerRemisionesFactura(idFactura);
			}

			
			//listar por fecha actual
			@GetMapping(value = ApiConstant.REMISION_COMPRA_API_LISTAR_POR_FECHA_ACTUAL, produces = MediaType.APPLICATION_JSON_VALUE)
			public @ResponseBody ResponseEntity<Object> remisionFechaActual(Integer page) {
				return remisionCompraService.remisionFechaActual(page);
			}
			
			@GetMapping(value = ApiConstant.REMISION_COMPRA_API_BUSCAR_ENTRADA, produces = MediaType.APPLICATION_JSON_VALUE)
			public @ResponseBody ResponseEntity<Object> obtenerRemisionEntrada(Integer idProveedor, String numeroRemision, Integer page) throws Exception {
				return remisionCompraService.obtenerRemisionesEntrada(idProveedor, numeroRemision, page);
			}
}
