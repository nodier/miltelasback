package com.softlond.base.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.softlond.base.constant.ApiConstant;
import com.softlond.base.entity.Promocion;
import com.softlond.base.service.PromocionService;

import ch.qos.logback.classic.Logger;

@RestController
@RequestMapping(ApiConstant.PROMOCION_API)
public class PromocionController {

	@Autowired
	private PromocionService promocionService;
	
	@PostMapping(value = ApiConstant.PROMOCION_API_CREAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> crearPromocion(@RequestBody Promocion promocion){
		return promocionService.crearPromocion(promocion);
	}
	
	@GetMapping(value = ApiConstant.PROMOCION_API_OBTENER_PROMOCION_TEXTO, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> obtenerPromocionTexto(String texto){
		return promocionService.obtenerPromocionTextoBusqueda(texto);
	}
	
	@GetMapping(value = ApiConstant.PROMOCION_API_CONSULTA, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> obtenerPromocionFiltros(String fechaInicial, String fechaFinal,
			String idPromocion, Integer page){
		return promocionService.obtenerPromocionFiltros(fechaInicial, fechaFinal, idPromocion, page);
	}
	
	@DeleteMapping(value = ApiConstant.PROMOCION_API_ELIMINAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> eliminarPromocion(Integer idPromocion){
		return promocionService.elimnarPromocion(idPromocion);
	}
	
	@GetMapping(value = ApiConstant.PROMOCION_API_CONSULTA_EXPORT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> obtenerPromocionFiltrosExport(String fechaInicial, String fechaFinal,
			String idPromocion){
		return promocionService.obtenerPromocionFiltrosExport(fechaInicial, fechaFinal, idPromocion);
	}
	
	@GetMapping(value = ApiConstant.PROMOCION_API_OBTENER_PROMOCION_DIA, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> obtenerPromocionDia(Integer idTipo, Integer idReferencia, Integer idPresentacion, String diaActual){       
		return promocionService.obtenerPromocionDia(idTipo, idReferencia, idPresentacion, diaActual);
	}
}
