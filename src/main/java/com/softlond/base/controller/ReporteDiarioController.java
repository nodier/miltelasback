package com.softlond.base.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.softlond.base.constant.ApiConstant;
import com.softlond.base.service.ReporteDiarioService;

@Controller
@RequestMapping(ApiConstant.INFORME_DIARIO_API)
public class ReporteDiarioController {

	@Autowired
	private ReporteDiarioService reporteService;
	
	@GetMapping(value = ApiConstant.INFORME_DIARIO_API_SEDE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerInformeSedes(Integer idSede, String fecha){
		return reporteService.obtenerInformeDiario(idSede, fecha);
	}
	
	@GetMapping(value = ApiConstant.INFORME_DIARIO_API_RECAUDO, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerInformeRecaudo(Integer idCaja, String fecha){
		return reporteService.obtenerInformeRecaudoDiario(idCaja, fecha);
	}
	
	@GetMapping(value = ApiConstant.INFORME_DIARIO_API_RECAUDO_SEDE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerInformeRecaudoSede(String fecha, String idSede){
		return reporteService.obtenerInformeRecaudoDiarioSede(fecha, idSede);
	}
	
	@GetMapping(value = ApiConstant.INFORME_DIARIO_API_RECAUDO_NO_EFECTIVO, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerInformeRecaudoMediosPagosNoEfectivos(Integer idCaja, String fecha){
		return reporteService.obtenerInformeRecaudoDiarioMediosPagosNoEfectivo(idCaja, fecha);
	}
	//informe recaudos
	@GetMapping(value = ApiConstant.INFORME_DIARIO_API_RECAUDO_NO_EFECTIVO1, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerInformeRecaudoMediosPagosNoEfectivosRecaudos(Integer idCaja, String fecha){
		return reporteService.obtenerInformeRecaudoDiarioMediosPagosNoEfectivoRecaudos(idCaja, fecha);
	}
	
	@GetMapping(value = ApiConstant.INFORME_DIARIO_API_RECAUDO_NO_EFECTIVO_SEDE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerInformeRecaudoMediosPagosNoEfectivos(String fecha, String idSede){
		return reporteService.obtenerInformeRecaudoDiarioMediosPagosNoEfectivoSede(fecha, idSede);
	}
	//informe recaudo
	@GetMapping(value = ApiConstant.INFORME_DIARIO_API_RECAUDO_NO_EFECTIVO_SEDE1, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerInformeRecaudoMediosPagosNoEfectivosRecuado(String fecha, String idSede){
		return reporteService.obtenerInformeRecaudoDiarioMediosPagosNoEfectivoSedeRecaudos(fecha, idSede);
	}
	
	
	/* Recibos de caja 
	 */
	
	@GetMapping(value = ApiConstant.INFORME_DIARIO_API_RECAUDO_RECIBO, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerInformeRecaudoRecibo(Integer idCaja, String fecha){
		return reporteService.obtenerInformeRecaudoDiarioRecibo(idCaja, fecha);
	}
	
	
	@GetMapping(value = ApiConstant.INFORME_DIARIO_API_RECAUDO_SEDE_RECIBO, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerInformeRecaudoSedeRecibo(String fecha, String idSede){
		return reporteService.obtenerInformeRecaudoDiarioSedeRecibo(fecha, idSede);
	}
	
	@GetMapping(value = ApiConstant.INFORME_DIARIO_API_RECAUDO_NO_EFECTIVO_RECIBO, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerInformeRecaudoMediosPagosNoEfectivosRecibo(Integer idCaja, String fecha){
		return reporteService.obtenerInformeRecaudoDiarioMediosPagosNoEfectivoRecibo(idCaja, fecha);
	}
	
	@GetMapping(value = ApiConstant.INFORME_DIARIO_API_RECAUDO_NO_EFECTIVO_SEDE_RECIBO, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerInformeRecaudoMediosPagosNoEfectivosRecibo(String fecha, String idSede){
		return reporteService.obtenerInformeRecaudoDiarioMediosPagosNoEfectivoSedeRecibo(fecha, idSede);
	}	
	
}



