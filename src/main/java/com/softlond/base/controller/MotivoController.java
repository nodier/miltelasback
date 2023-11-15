package com.softlond.base.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.softlond.base.constant.ApiConstant;
import com.softlond.base.service.MotivoService;

@RestController
@RequestMapping(ApiConstant.MOTIVOS_API)
public class MotivoController {

	@Autowired
	private MotivoService motivoService;
	
	@GetMapping(value = ApiConstant.MOTIVOS_API_LISTAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerTodos() {
		return motivoService.listarMotivos();
	}
}
