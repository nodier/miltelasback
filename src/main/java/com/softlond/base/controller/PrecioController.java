package com.softlond.base.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.softlond.base.constant.ApiConstant;
import com.softlond.base.service.PrdPreciosService;

@Controller
@RequestMapping(ApiConstant.PRECIOS_API)

public class PrecioController {
	
	@Autowired
	private PrdPreciosService precioService;
	
	/* Obtener impuestos */
	@GetMapping(value = ApiConstant.PRECIOS_API_LISTAR_CONSULTA, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerpreciosConsulta(String texto, String idClasificacion, Integer page) {
		return this.precioService.obtenerPreciosConsulta(texto, idClasificacion, page);
	}
	
	@GetMapping(value = ApiConstant.PRECIOS_API_LISTAR_EXPORTAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerpreciosConsultaExport(String texto, String idClasificacion) {
		return this.precioService.obtenerPreciosConsultaExport(texto, idClasificacion);
	}

}
