package com.softlond.base.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.softlond.base.constant.ApiConstant;
import com.softlond.base.service.MaestroValorService;

@Controller
@RequestMapping(ApiConstant.MAESTRO_VALOR_CONTROLADOR_API)
public class MaestroValorController {

	@Autowired
	MaestroValorService documentTypeService;
	
	/*
	 * Metodo para buscar los datos de maestro valor en el bd por el objetivo dado
	 * registrado
	 */
	@GetMapping(value = ApiConstant.MAESTRO_VALOR_CONTROLADOR_API_VER_POR_OBJETIVO, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerPorObjetivo(@RequestParam String objetivo, HttpServletRequest request, HttpServletResponse response) {
		return this.documentTypeService.obtenerPorObjetivo(objetivo, request, response);
	}
	
	@GetMapping(value = ApiConstant.MAESTRO_VALOR_CONTROLADOR_API_VER_DOCUMENTOS, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerPorObjetivo() {
		return this.documentTypeService.obtenerDocumentos();
	}
	
	@GetMapping(value = ApiConstant.MAESTRO_VALOR_CONTROLADOR_API_VER_TIPOS_PROVEEDOR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerTipoProveedor() {
		return this.documentTypeService.obtenerTipoProveedor();
	}
	
	@GetMapping(value = ApiConstant.MAESTRO_VALOR_CONTROLADOR_API_VER_MOTIVOS_SALIDA, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerMotivosSalida() {
		return this.documentTypeService.obtenerMotivosSalida();
	}
	
}
