package com.softlond.base.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.softlond.base.constant.ApiConstant;
import com.softlond.base.entity.PlazoCredito;
import com.softlond.base.entity.Prefijo;
import com.softlond.base.service.PlazoCreditoService;

@RestController
@RequestMapping(ApiConstant.PLAZOS_CREDITO_CONTROLADOR_API)
public class PlazoCreditoController {

	@Autowired
	public PlazoCreditoService plazoCreditoService;
	
	// Listar todos los plazos de credito
			@GetMapping(value = ApiConstant.PLAZOS_CREDITO_CONTROLADOR_API_LISTAR, produces = MediaType.APPLICATION_JSON_VALUE)
			public @ResponseBody ResponseEntity<Object> obtenerTodos() {
				return plazoCreditoService.obtenerTodos();
			}
			
			// Crear plazos de credito siendo super 
			@PostMapping(value = ApiConstant.PLAZOS_CREDITO_CONTROLADOR_API_CREAR, produces = MediaType.APPLICATION_JSON_VALUE)
			public @ResponseBody ResponseEntity<Object> crearPlazoCredito(@RequestBody PlazoCredito plazoCreditoNuevo) {
				return plazoCreditoService.crearPlazoCredito(plazoCreditoNuevo);
			}
			
			// Actualizar plazos de credito
			@PostMapping(value = ApiConstant.PLAZOS_CREDITO_CONTROLADOR_API_ACTUALIZAR, produces = MediaType.APPLICATION_JSON_VALUE)
			public @ResponseBody ResponseEntity<Object> actualizarPlazoCredito(@RequestBody PlazoCredito plazoCreditoActualizado) {
				return plazoCreditoService.actualizarPlazoCredito(plazoCreditoActualizado);
			}
			
			// Borrar plazos de credito en el sistema
			@DeleteMapping(value = ApiConstant.PLAZOS_CREDITO_CONTROLADOR_API_ELIMINAR, produces = MediaType.APPLICATION_JSON_VALUE)
			public @ResponseBody ResponseEntity<Object> borrarPlazoCredito(@RequestParam Integer idPlazoCredito) {
				return plazoCreditoService. borrarPlazoCredito(idPlazoCredito);
			}
}
