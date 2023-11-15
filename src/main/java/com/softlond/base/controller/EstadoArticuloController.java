package com.softlond.base.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.softlond.base.constant.ApiConstant;
import com.softlond.base.service.EstadoArticuloService;

@RestController
@RequestMapping(ApiConstant.ESTADO_ARTICULOS_CONTROL_API)
public class EstadoArticuloController {
	

	@Autowired
	public EstadoArticuloService estadoArticuloService;
	
    @GetMapping(value = ApiConstant.ESTADO_ARTICULOS_CONTROL_API_LISTAR_ESTADOS, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> listarEstadosArticulos() {
		return estadoArticuloService.listarEstados();
	}

}
