package com.softlond.base.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.softlond.base.constant.ApiConstant;

import com.softlond.base.service.DescuentoClienteService;


@RestController
@RequestMapping(ApiConstant.DESCUENTOS_CLIENTE_CONTROLADOR_API)
public class DescuentoClienteController {
	
	@Autowired
	public DescuentoClienteService descuentoClienteService;


	
	@GetMapping(value = ApiConstant.DESCUENTOS_CLIENTE_CONTROLADOR_API_LISTAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> obtenerDescuentoClientes() {
		return descuentoClienteService.obtenerDescuentoClientes();
	}
	
	@GetMapping(value = ApiConstant.DESCUENTOS_CLIENTE_CONTROLADOR_API_LISTAR_TODOS, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> obtenerTodosDescuentosClientes(@PathVariable Integer page){
		return descuentoClienteService.obtenerTodosDescuentosClientes(page);
	}	
}
