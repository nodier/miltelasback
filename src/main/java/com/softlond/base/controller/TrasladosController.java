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
import com.softlond.base.entity.Factura;
import com.softlond.base.entity.Traslado;
import com.softlond.base.service.TrasladosService;

@Controller
@RequestMapping(ApiConstant.TRASLADOS_API)
public class TrasladosController {

	@Autowired
	private TrasladosService trasladoService;

	@PostMapping(value = ApiConstant.TRASLADOS_API_CREAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> crearTraslados(@RequestBody Traslado traslado) {
			return trasladoService.crearTrasladosArticulos(traslado);
	}

	@GetMapping(value = ApiConstant.TRASLADOS_API_LISTAR_CONSULTA, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> listadoConsultaTraslados(String fechaInicial, String fechaFinal,
			String numeroTraslado, Integer estado, @PathVariable Integer page) {
		return trasladoService.obtenerTrasladosConsulta(fechaInicial, fechaFinal, numeroTraslado, estado, page);
	}

	@PostMapping(value = ApiConstant.TRASLADOS_API_ACTUALIZAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> actualizarTralado(@RequestBody Traslado traslado) {
		return trasladoService.actualizarTraslados(traslado);
	}

	@GetMapping(value = ApiConstant.TRASLADOS_API_LISTAR_MES, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> listadoTrasladosMes(@PathVariable Integer page) {
		return trasladoService.trasladosMes(page);
	}

	@GetMapping(value = ApiConstant.TRASLADOS_API_OBTENER_NUMERO, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerTraslado(String numeroTraslado) {
		return trasladoService.obtenerTrasladoNumero(numeroTraslado);
	}

}
