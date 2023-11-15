package com.softlond.base.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.softlond.base.constant.ApiConstant;
import com.softlond.base.service.FormasPagoService;

@Controller
@RequestMapping(ApiConstant.FORMAS_PAGO_API)
public class FormasPagoController {
	
	@Autowired
	public FormasPagoService formasPagoService;
	
	/* Obtener formas de pago */
	@GetMapping(value = ApiConstant.FORMAS_PAGO_API_LISTAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerFormasPago() {
		return this.formasPagoService.obtenerFormasPago();
	}
	
	@GetMapping(value = ApiConstant.FORMAS_PAGO_API_GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerFormaPago(Integer id) {
		return this.formasPagoService.obtenerFormaPago(id);
	}
}
