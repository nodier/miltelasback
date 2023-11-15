package com.softlond.base.controller;

import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.softlond.base.constant.ApiConstant;
import com.softlond.base.entity.ArticulosRemisionCompra;
import com.softlond.base.entity.RemisionCompra;
import com.softlond.base.service.ArticulosRemisionCompraService;

@RestController
@RequestMapping(ApiConstant.ARTICULOS_REMISION_COMPRA_API)
public class ArticulosRemisionCompraController {

	@Autowired
	public ArticulosRemisionCompraService articulosRemisionCompraService;

	// Crear Articulos remisiones de compra
	@PostMapping(value = ApiConstant.ARTICULOS_REMISION_COMPRA_API_CREAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> crerArticulosRemisionCompra(
			@RequestBody List<ArticulosRemisionCompra> articulosRemision) {
		return articulosRemisionCompraService.crearArticulosRemision(articulosRemision);
	}

// obtener articulos remision
	@GetMapping(value = ApiConstant.ARTICULOS_REMISION_COMPRA_API_BUSCAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerArticulosRemision(Integer idRemisionCompra) {
		return articulosRemisionCompraService.obtenerArticulosRemision(idRemisionCompra);
	}

	// obtener articulos remision
	@GetMapping(value = ApiConstant.ARTICULOS_REMISION_COMPRA_API_BUSCAR_PENDIENTE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerArticulosRemisionPaginator(Integer idRemisionCompra) {
		return articulosRemisionCompraService.obtenerArticulosRemisionPaginado(idRemisionCompra);
	}

	// obtener articulos remision
	@GetMapping(value = ApiConstant.ARTICULOS_REMISION_COMPRA_API_UPDATE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerArticulosRemisionPaginatorUpdate(Integer idFacturaCompra) {
		return articulosRemisionCompraService.obtenerArticulosRemisionPaginadoUpdate(idFacturaCompra);
	}

	// obtener articulos remision por producto
	@GetMapping(value = ApiConstant.ARTICULOS_REMISION_COMPRA_API_LISTADO_PRODUCTO, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerArticulosRemisionPorProducto(String idProducto,@PathVariable Integer page) {
		return articulosRemisionCompraService.obtenerArticulosRemisionProductos(idProducto, page);
	}

}
