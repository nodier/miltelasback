package com.softlond.base.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.softlond.base.constant.ApiConstant;
import com.softlond.base.service.CiudadService;

@Controller
@RequestMapping(ApiConstant.CIUDAD_CONTROLADOR_API)
public class CiudadController {

	
	@Autowired
	private CiudadService ciudadService; 
	
	@GetMapping(value = ApiConstant.CIUDAD_CONTROLADOR_API_OBTENER_TODAS, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerTodas() {
		return ciudadService.obtenerTodas();
	}
	
	@GetMapping(value = ApiConstant.CIUDAD_CONTROLADOR_API_OBTENER_POR_DEPARTAMENTO, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerPorDepartamento(Integer idDepartamento) {
		return ciudadService.obtenerPorDepartamento(idDepartamento);
	}
}