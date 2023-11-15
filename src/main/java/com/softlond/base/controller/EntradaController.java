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

import com.softlond.base.constant.ApiConstant;
import com.softlond.base.entity.Entrada;
import com.softlond.base.entity.SalidaMercancia;
import com.softlond.base.request.EntradaRequest;
import com.softlond.base.service.EntradaService;

import org.apache.log4j.Logger;

@Controller
@RequestMapping(ApiConstant.ENTRADAS_MERCANCIA_API)
public class EntradaController {

	private final Logger logger = Logger.getLogger(getClass());

	@Autowired
	private EntradaService entradaService;

	@PostMapping(value = ApiConstant.ENTRADAS_MERCANCIA_API_CREAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> crerTralado(@RequestBody Entrada entradaRequest) {
		logger.info(entradaRequest.getEntradas());
		return entradaService.crearEntradaMercancia(entradaRequest);
	}

	@GetMapping(value = ApiConstant.ENTRADAS_MERCANCIA_API_LISTAR_CONSULTA, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> listadoConsultaTraslados(String fechaInicial, String fechaFinal,
			String numeroTraslado, Integer estado, @PathVariable Integer page) {
		return entradaService.obtenerEntradasConsulta(fechaInicial, fechaFinal, numeroTraslado, estado, page);
	}

	@PostMapping(value = ApiConstant.ENTRADAS_MERCANCIA_API_ACTUALIZAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> ActualizarTralado(@RequestBody Entrada entradaRequest) {
		return entradaService.actualizarEntradaMercancia(entradaRequest);
	}

	@GetMapping(value = ApiConstant.ENTRADAS_MERCANCIA_API_LISTAR_MES, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> listadoEntradasMes(@PathVariable Integer page) {
		return entradaService.entradaMes(page);
	}

	@GetMapping(value = ApiConstant.ENTRADAS_MERCANCIA_API_OBTENER_NUMERO, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> EntradasNumero(String numero) {
		return entradaService.entradasNumero(numero);
	}
}
