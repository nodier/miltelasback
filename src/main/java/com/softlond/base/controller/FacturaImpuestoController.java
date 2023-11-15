package com.softlond.base.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.softlond.base.constant.ApiConstant;
import com.softlond.base.entity.FacturaImpuestos;
import com.softlond.base.entity.FacturaRetenciones;
import com.softlond.base.service.FacturaImpuestosService;
import com.softlond.base.service.FacturaRetencionesService;

@Controller
@RequestMapping(ApiConstant.FACTURA_IMPUESTO_API)
public class FacturaImpuestoController {

	@Autowired
	private FacturaImpuestosService facturaImpuestoService;
	
	@PostMapping(value = ApiConstant.FACTURA_IMPUESTO_API_CREAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> crerFactura(@RequestBody List<FacturaImpuestos> impuestos) {
		return facturaImpuestoService.crearImpuestos(impuestos);
	}
}
