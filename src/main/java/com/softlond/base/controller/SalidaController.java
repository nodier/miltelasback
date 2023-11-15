package com.softlond.base.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import org.apache.log4j.Logger;

import com.softlond.base.constant.ApiConstant;
import com.softlond.base.entity.SalidaMercancia;
import com.softlond.base.service.SalidaService;

@Controller
@RequestMapping(ApiConstant.SALIDAS_MERCANCIA_API)
public class SalidaController {

	private final Logger logger = Logger.getLogger(getClass());

	@Autowired
	private SalidaService salidaMercanciaService;

	@PostMapping(value = ApiConstant.SALIDAS_MERCANCIA_API_CREAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> crearTraslado(@RequestBody SalidaMercancia salida) {
		logger.info("SALIDAS_MERCANCIA_API_CREAR");
		// logger.info(salida.getSalidas());
		return salidaMercanciaService.crearSalidaMercancia(salida);
	}

	@GetMapping(value = ApiConstant.SALIDAS_MERCANCIA_API_LISTAR_CONSULTA, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> listadoConsultaTraslados(String fechaInicial, String fechaFinal,
			String numeroTraslado, Integer estado, @PathVariable Integer page) {
		return salidaMercanciaService.obtenerSalidaConsulta(fechaInicial, fechaFinal, numeroTraslado, estado, page);
	}

	@PostMapping(value = ApiConstant.SALIDAS_MERCANCIA_API_ACTUALIZAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> actualizarTraslado(@RequestBody SalidaMercancia salida) {
		return salidaMercanciaService.actualizarSalidaMercancia(salida);
	}

	@GetMapping(value = ApiConstant.SALIDAS_MERCANCIA_API_LISTAR_MES, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> listadoConsultaMes(@PathVariable Integer page) {
		return salidaMercanciaService.salidaMercanciaMes(page);
	}

	@GetMapping(value = ApiConstant.SALIDAS_MERCANCIA_API_OBTENER_NUMERO, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerSalidasNumero(String numeroTraslado) {
		return salidaMercanciaService.salidaMercanciaNumero(numeroTraslado);
	}
}
