package com.softlond.base.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.softlond.base.constant.ApiConstant;
import com.softlond.base.service.ConceptoReciboEgresoService;

@RestController
@RequestMapping(ApiConstant.CONCEPTO_RECIBO_EGRESO_API)
public class ConceptoReciboEgresoController {
	
	@Autowired
	private ConceptoReciboEgresoService conceptoReciboEgreso;

	@GetMapping(value = ApiConstant.CONCEPTO_RECIBO_EGRESO_API_ABONO, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerNDConcepto( String numero) {
		return conceptoReciboEgreso.obtenerTotalAbonosConcepto(numero);
	}
}
