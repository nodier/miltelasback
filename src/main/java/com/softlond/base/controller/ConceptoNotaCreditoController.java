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
import com.softlond.base.service.ConceptoNotaCreditoService;

@RestController
@RequestMapping(ApiConstant.CONCEPTOS_NOTA_CREDITO_API)
public class ConceptoNotaCreditoController {

	@Autowired
	public ConceptoNotaCreditoService conceptoNCService;
	
	// Crear 
	@PostMapping(value = ApiConstant.CONCEPTOS_NOTA_CREDITO_API_CREAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> crearConceptoNC(@RequestBody ConceptoNotaCredito conceptoNC) {
		return conceptoNCService.crearConceptoNC(conceptoNC);
	}
	
	@GetMapping(value = ApiConstant.CONCEPTOS_NOTA_CREDITO_API_LISTAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> listarNotasCredito() {
		return conceptoNCService.listarNC();
	}
	

	//busqueda por filtro
	@GetMapping(value = ApiConstant.CONCEPTOS_NOTA_CREDITO_API_BUSCAR_FILTROS, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerNCFiltros( String numeroDocumento, String fechaInicial, String fechaFinal,
			String estadoDocumento, Integer page) throws Exception {
		return conceptoNCService.obtenerNcFiltros(numeroDocumento, fechaInicial, fechaFinal, estadoDocumento, page);
	}
	
	@GetMapping(value = ApiConstant.CONCEPTOS_NOTA_CREDITO_API_BUSCAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerNConcepto( Integer idNotaCredito) throws Exception {
		return conceptoNCService.obtenerNcConceptos(idNotaCredito);
	}
}
