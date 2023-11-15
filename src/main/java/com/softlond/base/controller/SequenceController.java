package com.softlond.base.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.apache.log4j.Logger;

import com.softlond.base.constant.ApiConstant;
import com.softlond.base.service.SequenceService;

@RestController
@RequestMapping(ApiConstant.SEQUENCIA_API)
public class SequenceController {

	@Autowired
	public SequenceService sequenceService;
	// private static final Logger logger = Logger.getLogger(SequenceService.class);

	private final Logger logger = Logger.getLogger(getClass());

	@GetMapping(value = ApiConstant.SEQUENCIA_API_SEDE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerSequencia(@RequestParam Integer idSede, Integer idPrefijo) {
		return sequenceService.obtenerSequencia(idSede, idPrefijo);
	}

	@GetMapping(value = ApiConstant.SEQUENCIA_API_SEDE2, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerSequencia2(String prefijo, Integer idSede, Integer idPrefijo) {
		return sequenceService.obtenerSequencia2(prefijo, idSede, idPrefijo);
	}

	@GetMapping(value = ApiConstant.SEQUENCIA_API_SEDE3, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerSequencia3(String prefijo, Integer idSede, Integer idPrefijo) {
		return sequenceService.obtenerSequencia3(prefijo, idSede, idPrefijo);
	}

	@GetMapping(value = ApiConstant.SEQUENCIA_API_SEDE4, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerSequencia4(String prefijo, Integer idSede, Integer idPrefijo) {
		return sequenceService.obtenerSequencia4(prefijo, idSede, idPrefijo);
	}

}
