package com.softlond.base.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.softlond.base.constant.ApiConstant;
import com.softlond.base.entity.ConceptosReciboCaja;
import com.softlond.base.entity.Prefijo;
import com.softlond.base.service.ConceptosReciboCajaService;

@RestController
@RequestMapping(ApiConstant.CONCEPTOS_RECIBO_CAJA)
public class ConceptosReciboCajaController {
	
	@Autowired
	public ConceptosReciboCajaService conceptosReciboCajaService;
		
		// Actualizar concepto cuando realizan un abono
		@PostMapping(value = ApiConstant.CONCEPTOS_RECIBO_CAJA_ABONO, produces = MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody ResponseEntity<Object> actualizarAbono(@RequestBody ConceptosReciboCaja abonoActualizado) {
			return conceptosReciboCajaService.actualizarAbono(abonoActualizado);
		}
}
