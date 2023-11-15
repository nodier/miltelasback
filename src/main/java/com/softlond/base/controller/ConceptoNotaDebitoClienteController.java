package com.softlond.base.controller;

import java.util.List;

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
import com.softlond.base.entity.ConceptoNotaDebitoCliente;
import com.softlond.base.service.ConceptoNotaDebitoClienteService;
import com.softlond.base.service.ConceptoNotaDebitoService;
import com.softlond.base.service.NotaDebitoService;

@RestController
@RequestMapping(ApiConstant.CONCEPTOS_NOTA_DEBITO_CLIENTE_API)
public class ConceptoNotaDebitoClienteController {
	
	@Autowired
	public ConceptoNotaDebitoClienteService conceptoNotaDebitoClienteService;
	
	// Crear 
		@PostMapping(value = ApiConstant.CONCEPTOS_NOTA_DEBITO_CLIENTE_API_CREAR, produces = MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody ResponseEntity<Object> crearConceptoND(@RequestBody List<ConceptoNotaDebitoCliente> conceptosND) {
			return conceptoNotaDebitoClienteService.crearConceptoND(conceptosND);
		}
		
		@GetMapping(value = ApiConstant.CONCEPTOS_NOTA_DEBITO_CLIENTE_API_BUSCAR, produces = MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody ResponseEntity<Object> obtenerNDConcepto( Integer idNotaDebito) throws Exception {
			return conceptoNotaDebitoClienteService.obtenerNDConceptos(idNotaDebito);
		}

}
