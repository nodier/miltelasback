package com.softlond.base.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.softlond.base.constant.ApiConstant;
import com.softlond.base.service.EstadoDocumentoService;

@RestController
@RequestMapping(ApiConstant.ESTADODOCUMENTO_CONTROL_API)
public class EstadoDocumentoController {
	
	@Autowired
	private EstadoDocumentoService estadoDocumentoService;
	
	/* Listar todos los estados */
	@GetMapping(value = ApiConstant.ESTADODOCUMENTO_CONTROL_API_LISTAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> obtenerEstadosDocumento() {
		return estadoDocumentoService.obtenerEstadosDocumento();
	}
	
	@GetMapping(value = ApiConstant.ESTADODOCUMENTO_CONTROL_API_LISTAR_TODOS_NOMBRE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> obtenerNombresEstados(){
		return this.estadoDocumentoService.obtenerNombresEstados();
	}
	
	@GetMapping(value = ApiConstant.ESTADODOCUMENTO_CONTROL_API_LISTAR_TRASLADOS, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> obtenerEstadosTraslados(){
		return this.estadoDocumentoService.obtenerEstadosDocumentoTraslados();
	}

}
