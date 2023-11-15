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
import com.softlond.base.entity.ConceptoNotaCredito;
import com.softlond.base.entity.ConceptoNotaDebito;
import com.softlond.base.service.ConceptoNotaDebitoService;
import com.softlond.base.service.NotaDebitoService;

@RestController
@RequestMapping(ApiConstant.CONCEPTOS_NOTA_DEBITO_API)
public class ConceptoNotaDebitoController {

	@Autowired
	public ConceptoNotaDebitoService conceptoNotaDebitoService;

	// Crear
	@PostMapping(value = ApiConstant.CONCEPTOS_NOTA_DEBITO_API_CREAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> crearConceptoND(@RequestBody ConceptoNotaDebito conceptoND) {
		return conceptoNotaDebitoService.crearConceptoND(conceptoND);
	}

	@GetMapping(value = ApiConstant.CONCEPTOS_NOTA_DEBITO_API_BUSCAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerNDConcepto(Integer idNotaDebito) throws Exception {
		return conceptoNotaDebitoService.obtenerNDConceptos(idNotaDebito);
	}

	@PostMapping(value = ApiConstant.CONCEPTOS_NOTA_DEBITO_API_ACTUALIZAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> actualizarConceptos(@RequestBody ConceptoNotaDebito conceptoND) {
		return conceptoNotaDebitoService.actualizarConceptoND(conceptoND);
	}

}
