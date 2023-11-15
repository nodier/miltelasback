package com.softlond.base.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.softlond.base.constant.ApiConstant;
import com.softlond.base.service.ClasificacionLegalService;

@Controller
@RequestMapping(ApiConstant.CLASIFICACION_LEGAL_API)
public class ClasificacionLegalController {

	@Autowired
	private ClasificacionLegalService clasificacionService;
	
	@GetMapping(value = ApiConstant.CLASIFICACION_LEGAL_API_LISTAR_TODOS, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object>obtenerClasificacion(){
		return this.clasificacionService.obtenerClasificacionLegal();
	}
}
