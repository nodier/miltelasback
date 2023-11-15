package com.softlond.base.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.softlond.base.constant.ApiConstant;
import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.AsignacionComprobante;
import com.softlond.base.response.Paginacion;
import com.softlond.base.service.AsignacionPendienteReciboService;
import com.softlond.base.service.AsignacionesPendientesService;

@Controller
@RequestMapping(ApiConstant.ASIGNACION_PENDIENTE_RECIBO_API)
public class AsignacionPendientesReciboController {

private final Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private AsignacionPendienteReciboService asignaciones;
	
	@GetMapping(value = ApiConstant.ASIGNACION_PENDIENTE_RECIBO_API_LISTAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerAsignacionesPendientes(Integer idCliente, int pagina){
		RespuestaDto respuestaDto;
		try {
			Paginacion pag = asignaciones.obtenerAsignacionesPorProveedorPaginado(idCliente, pagina);
			respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
            respuestaDto.setObjetoRespuesta(pag);
            return new ResponseEntity(respuestaDto, HttpStatus.OK);
		}catch (Exception ex) {
			ex.printStackTrace();
            String msj = "Error buscando asignaciones pendientes por proveedor... " + ex.getMessage();
            logger.error(ex);
            respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
            return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
		}		
	}
	
	@PostMapping(value = ApiConstant.ASIGNACION_PENDIENTE_RECIBO_API_CREAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> crearAsignacion(@RequestBody AsignacionComprobante asignacion){
		return asignaciones.crearAsignacion(asignacion);
	}
	
	@GetMapping(value = ApiConstant.ASIGNACION_PENDIENTE_API_LISTAR_UPDATE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerAsignacionesPendientesActualizar(Integer idCliente, int pagina){
		RespuestaDto respuestaDto;
		try {
			Paginacion pag = asignaciones.obtenerAsignacionesPorProveedorPaginadoActualizar(idCliente, pagina);
			respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
            respuestaDto.setObjetoRespuesta(pag);
            return new ResponseEntity(respuestaDto, HttpStatus.OK);
		}catch (Exception ex) {
			ex.printStackTrace();
            String msj = "Error buscando asignaciones pendientes por proveedor... " + ex.getMessage();
            logger.error(ex);
            respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
            return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
		}
	}
}
