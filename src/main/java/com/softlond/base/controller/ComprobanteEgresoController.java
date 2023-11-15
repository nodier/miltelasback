package com.softlond.base.controller;

import com.softlond.base.constant.ApiConstant;
import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.ComprobanteEgreso;
import com.softlond.base.entity.ContableM;
import com.softlond.base.entity.Organizacion;
import com.softlond.base.entity.Usuario;
import com.softlond.base.repository.ComprobanteEgresoDao;
import com.softlond.base.request.ComprobanteEgresoRequest;
import com.softlond.base.service.ComprobanteEgresoService;
import com.softlond.base.service.ConsecutivoEgresoSedeService;
import com.softlond.base.service.DatosSesionService;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.softlond.base.repository.UsuarioInformacionDao;
import com.softlond.base.entity.InformacionUsuario;

@RestController
@RequestMapping(ApiConstant.COMPROBANTE_EGRESO_CONTROL_API)
public class ComprobanteEgresoController {
	private final Logger logger = Logger.getLogger(getClass());
	@Autowired
	private ComprobanteEgresoService comprobanteService;
	@Autowired
	private ConsecutivoEgresoSedeService consecutivoService;
	@Autowired
	private DatosSesionService sesionService;
	@Autowired
	private ComprobanteEgresoDao comprobanteEgresoDao;
	@Autowired
	UsuarioInformacionDao usuarioInformacionDao;

	@GetMapping(value = ApiConstant.COMPROBANTE_EGRESO_CONTROL_API_SIGUIENTE_NUMERO, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity siguienteNumero(Integer idPrefijo) {
		logger.info(idPrefijo);
		RespuestaDto respuestaDto;
		try {
			Organizacion sede = sesionService.sedeUsuarioSesion();
			int next = consecutivoService.siguienteNumero(sede, idPrefijo);
			respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
			respuestaDto.setObjetoRespuesta(next);
			return new ResponseEntity(respuestaDto, HttpStatus.OK);
		} catch (Exception ex) {
			String msj = "Error buscando consecutivo egreso... " + ex.getMessage() + " Linea error: "
					+ ex.getStackTrace()[0].getLineNumber();
			logger.error(msj);
			respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
			return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping(value = ApiConstant.COMPROBANTE_EGRESO_CONTROL_API_CREAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity crearComprobante(@RequestBody ComprobanteEgresoRequest body) {
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao.buscarPorIdUsuario(usuarioAutenticado.getId());
		Integer idSede = usuarioInformacion.getIdOrganizacion().getId();
		logger.info(body.getListContable().size());
		logger.info(idSede);
		logger.info("ingresa a crear comprobante de egresso controller ");
		RespuestaDto respuestaDto;
		if (body.getComprobante() != null) {
			logger.info(body.getComprobante().getNumeroDocumento().toString());
			// !la primera vez el comprobante tiene el id nulo
			if (body.getComprobante().getId() == null
					&& comprobanteEgresoDao.obtenerComprobantesPorNumeroC(body.getComprobante().getNumeroDocumento(),
							idSede) <= 0) {
				logger.info("es un comprobante nuevo");
			} else {
				logger.info("el comprobante ya existe");
				logger.info(body.getComprobante().getId());
				logger.info(body.getComprobante().getNumeroDocumento());
			}
		} else {
			logger.info("no existe comprobante");
		}
		try {
			ComprobanteEgreso comprobante = comprobanteService.crearComprobante(body);
			if (comprobante != null) {
				respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
				respuestaDto.setObjetoRespuesta(comprobante);
				return new ResponseEntity(respuestaDto, HttpStatus.OK);
			} else {
				respuestaDto = new RespuestaDto(HttpStatus.OK, "editandose");
				respuestaDto.setObjetoRespuesta(comprobante);
				return new ResponseEntity(respuestaDto, HttpStatus.OK);
			}
		} catch (Exception ex) {
			String msj = "Error creando comprobante de egreso... " + ex.getMessage() + " Linea error: "
					+ ex.getStackTrace()[0].getLineNumber();
			logger.error(msj);
			respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
			return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(value = ApiConstant.COMPROBANTE_EGRESO_CONTROL_API_LISTAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity obtenerListadoComprobantes(String fechaInicial, String fechaFinal,
			Integer estado, String numeroComprobante, Integer page) {
		return comprobanteService.listarComprobantes(fechaInicial, fechaFinal, estado, numeroComprobante, page);
	}

	@DeleteMapping(value = ApiConstant.COMPROBANTE_EGRESO_CONTROL_API_ELIMINAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity eliminarComprobante(Integer idComprobante) {
		return comprobanteService.eliminarComprobante(idComprobante);
	}

	@PostMapping(value = ApiConstant.COMPROBANTE_EGRESO_CONTROL_API_ACTUALIZAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity actualizarComprobante(@RequestBody ComprobanteEgreso body) {
		return comprobanteService.actualizarComprobante(body);
	}

	@GetMapping(value = ApiConstant.COMPROBANTE_EGRESO_CONTROL_API_LISTAR_TODO, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity obtenerListadoComprobantesSinPaginar(String fechaInicial, String fechaFinal,
			Integer estado, String numeroComprobante) {
		return comprobanteService.listarComprobantesTodos(fechaInicial, fechaFinal, estado, numeroComprobante);
	}

	@GetMapping(value = ApiConstant.COMPROBANTE_EGRESO_CONTROL_API_LISTAR_MES, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity obtenerListadoComprobantesMes(Integer page) {
		return comprobanteService.listarComprobantesMes(page);
	}

	@PostMapping(value = ApiConstant.COMPROBANTE_EGRESO_CONTROL_API_RETENCION, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity obtenerRetencionComprobante(@RequestBody ComprobanteEgreso comprobante) {
		return comprobanteService.obtenerRetencion(comprobante);
	}

	@PutMapping(value = ApiConstant.COMPROBANTE_EGRESO_CONTROL_API_MODIFICAR_EDITABLE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity modificarEditableComprobante(@RequestBody Integer idComprobante) {
		return comprobanteService.modificarEditableComprobante(idComprobante);
	}

	// @PostMapping(value = ApiConstant.COMPROBANTE_EGRESO_CONTROL_API_CREAR_MEKANO,
	// produces = MediaType.APPLICATION_JSON_VALUE)
	// public @ResponseBody ResponseEntity<Object>
	// crearComprobanteMekano(@RequestBody ContableM contable,
	// Date fechaFactura) throws Exception {
	// logger.info(contable);
	// logger.info(fechaFactura);
	// return comprobanteService.crearComprobanteMekano(contable, fechaFactura);
	// }
}
