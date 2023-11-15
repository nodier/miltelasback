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
import com.softlond.base.service.TipoTarjetaService;

@Controller
@RequestMapping(ApiConstant.TIPOS_TARJETAS_API)
public class TipoTarjetasController {
	
	@Autowired
	public TipoTarjetaService tiposTarjetasService;
	
	/* Obtener tipos de tarjetas */
	@GetMapping(value = ApiConstant.TIPOS_TARJETAS_API_LISTAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerFormasPago() {
		return this.tiposTarjetasService.obtenerTiposTarjetas();
	}
}
