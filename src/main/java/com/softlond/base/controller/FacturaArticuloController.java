package com.softlond.base.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.softlond.base.constant.ApiConstant;
import com.softlond.base.entity.FacturaArticulos;
import com.softlond.base.service.FacturaArticuloService;

import ch.qos.logback.classic.Logger;

@Controller
@RequestMapping(ApiConstant.FACTURA_ARTICULO_API)
public class FacturaArticuloController {
	@Autowired
	private FacturaArticuloService facturaArticuloService;
	
	@GetMapping(value = ApiConstant.FACTURA_ARTICULO_API_LISTAR_TODOS_SEDE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerFacturasSede(@RequestParam Integer idSede) {
		return facturaArticuloService.listarFacturasArticuloSede(idSede);
	}
	
	@PostMapping(value = ApiConstant.FACTURA_ARTICULO_API_CREAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> crerOrganizacion(@RequestBody FacturaArticulos factura) {
		
		return facturaArticuloService.crearFacturaArticulo(factura);
	}
	
	@PostMapping(value = ApiConstant.FACTURA_ARTICULO_API_EDITAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> editar(@RequestBody FacturaArticulos factura) {
		return facturaArticuloService.editarFacturaArticulo(factura);
	}
}
