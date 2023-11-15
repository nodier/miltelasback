package com.softlond.base.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.softlond.base.constant.ApiConstant;
import com.softlond.base.entity.ContableM;
import com.softlond.base.service.InformeDiarioSedeService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.Date;
import org.apache.log4j.Logger;

@Controller
@RequestMapping(ApiConstant.INFORME_DIARIO_SEDE_API)
public class InformeDiarioSedeController {
	private final Logger logger = Logger.getLogger(getClass());
	@Autowired
	private InformeDiarioSedeService informeService;

	@GetMapping(value = ApiConstant.INFORME_DIARIO_SEDE_API_GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerInformeSedes(String fecha, String idSede) {
		return informeService.obtenerInformeDiario(fecha, idSede);
	}

	@PostMapping(value = ApiConstant.INFORME_DIARIO_SEDE_API_INFORME_MEKANO, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> crearInformeDiarioMekano(@RequestBody ContableM contable,
			String fechaFactura) {
		logger.info(contable);
		logger.info(fechaFactura);
		return informeService.crearInformeDiarioMekano(contable, fechaFactura);
	}

	// @PostMapping(value =
	// ApiConstant.INFORME_DIARIO_SEDE_API_INFORME_MEKANO_OBTENER, produces =
	// MediaType.APPLICATION_JSON_VALUE)
	// public @ResponseBody ResponseEntity<Object> enviarInformeContadoMekano() {
	// return informeService.enviarInformeContadoMekano();
	// }

	@GetMapping(value = ApiConstant.INFORME_DIARIO_SEDE_API_GET_IVA, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerInformeSedesIva(String fecha, String idSede) {
		return informeService.obtenerInformeDiarioIva(fecha, idSede);
	}

	@GetMapping(value = ApiConstant.INFORME_DIARIO_SEDE_API_RECAUDOS, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerInformeRecaudos(String fecha, String idSede) {
		return informeService.obtenerRecaudos(fecha, idSede);
	}
}
