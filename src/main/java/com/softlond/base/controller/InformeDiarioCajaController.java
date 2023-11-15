package com.softlond.base.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.softlond.base.constant.ApiConstant;
import com.softlond.base.service.InformeDiarioCajaService;
import com.softlond.base.service.ReporteDiarioService;

@Controller
@RequestMapping(ApiConstant.INFORME_DIARIO_CAJA_API)
public class InformeDiarioCajaController {

	@Autowired
	private InformeDiarioCajaService informeService;
	@GetMapping(value = ApiConstant.INFORME_DIARIO_CAJA_API_GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerInformeSedes(Integer idCaja, String fecha){
		return informeService.obtenerInformeDiario(idCaja, fecha);
	}
	
	@GetMapping(value = ApiConstant.INFORME_DIARIO_CAJA_API_GET_IVA, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerInformeSedesIva(Integer idCaja, String fecha){
		return informeService.obtenerInformeDiarioIva(idCaja, fecha);
	}
	
	@GetMapping(value = ApiConstant.INFORME_DIARIO_CAJA_API_RECAUDOS, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerInformeRecaudos(String fecha, String idCaja){
		return informeService.obtenerRecaudos(fecha, idCaja);
	}
}
