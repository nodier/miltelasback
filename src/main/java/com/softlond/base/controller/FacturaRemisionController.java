package com.softlond.base.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.softlond.base.constant.ApiConstant;
import com.softlond.base.entity.FacturaImpuestos;
import com.softlond.base.service.FacturaImpuestosService;
import com.softlond.base.service.FacturaRemisionService;

@Controller
@RequestMapping(ApiConstant.FACTURA_REMISION_API)
public class FacturaRemisionController {

	@Autowired
	private FacturaRemisionService facturaRemisionService;
	
	@GetMapping(value = ApiConstant.FACTURA_REMISION_API_CANTIDAD, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerCantidad(Integer idFactura) {
		return facturaRemisionService.obtenerCantidadRemisiones(idFactura);
	}
	
	@GetMapping(value = ApiConstant.FACTURA_REMISION_API_LISTAR_FACTURA, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerRemisiones(Integer idFactura) {
		return facturaRemisionService.obtenerRemisiones(idFactura);
	}
}
