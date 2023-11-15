package com.softlond.base.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.softlond.base.constant.ApiConstant;
import com.softlond.base.service.PerfilService;
import com.softlond.base.service.PeriodosContablesService;

@Controller
@RequestMapping(ApiConstant.PERIODO_CONTABLE_CONTROL_API)
public class PeriodosContablesController {
	
	@Autowired
	private PeriodosContablesService periodoContableService;

	/* Obtener el periodo contable */
	@GetMapping(value = ApiConstant.PERIODO_CONTABLE_CONTROL_API_GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerPeriodo() {
		return this.periodoContableService.obtenerPeriodo();
	}
	
	/* Obtener el periodo contable */
	@GetMapping(value = ApiConstant.PERIODO_CONTABLE_CONTROL_API_GET_SEDE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerPeriodoSede(String idSede) {
		return this.periodoContableService.obtenerPeriodoSede(idSede);
	}
	
}
