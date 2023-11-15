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
import com.softlond.base.entity.FacturaRetenciones;
import com.softlond.base.service.FacturaRetencionesService;

@Controller
@RequestMapping(ApiConstant.FACTURA_RETENCION_API)
public class FacturaRetencionController {

	@Autowired
	private FacturaRetencionesService facturaRetencionService;
	
	@PostMapping(value = ApiConstant.FACTURA_RETENCION_API_CREAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> crerFactura(@RequestBody List<FacturaRetenciones> retenciones) {
		return facturaRetencionService.crearRetenciones(retenciones);
	}
	
	@GetMapping(value = ApiConstant.FACTURA_RETENCION_API_LISTAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> listarRetenciones(Integer idFactura) {
		return facturaRetencionService.listarRetenciones(idFactura);
	}
}
