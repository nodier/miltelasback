package com.softlond.base.controller;

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
import com.softlond.base.entity.Barrio;
import com.softlond.base.service.BarrioService;

@Controller
@RequestMapping(ApiConstant.BARRIO_CONTROLADOR_API)
public class BarrioController {
	
	@Autowired
	private BarrioService barrioService;

	@GetMapping(value = ApiConstant.BARRIO_CONTROLADOR_API_OBTENER_POR_CIUDAD, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerBarrioPorCiudad(Integer idCiudad) {
		return barrioService.obtenerBarrios(idCiudad);
	}
	
	@PostMapping(value = ApiConstant.BARRIO_CONTROLADOR_API_CREAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> crearBarrio(@RequestBody Barrio barrio) {
		return barrioService.agregarBarrio(barrio);
	}
}
