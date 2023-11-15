package com.softlond.base.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.softlond.base.constant.ApiConstant;
import com.softlond.base.service.DepartamentoService;

@Controller
@RequestMapping(ApiConstant.DEPARTAMENTO_CONTROLADOR_API)
public class DepartamentoController {

	@Autowired
	private DepartamentoService departamentoService;
	
	@GetMapping(value = ApiConstant.DEPARTAMENTO_CONTROLADOR_API_OBTENER_TODOS, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerTodos() {
		return departamentoService.obtenerTodos();
	}
}