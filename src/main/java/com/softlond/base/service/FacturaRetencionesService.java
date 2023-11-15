package com.softlond.base.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.FacturaArticulos;
import com.softlond.base.entity.FacturaRetenciones;
import com.softlond.base.repository.FacturaRetencionesDao;

@Service
public class FacturaRetencionesService {

private static final Logger logger = Logger.getLogger(FacturaRetencionesService.class);
	
	@Autowired
	private FacturaRetencionesDao facturaRetencionDao;
	
	
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> crearRetenciones(@RequestBody List<FacturaRetenciones> facturas){
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		try {
			facturaRetencionDao.saveAll(facturas);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "retenciones creadas exitosamente");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error al guardar la retencion");
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error al guardar las retenciones"+e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}
	
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> listarRetenciones(Integer idFactura){
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		try {
			List<FacturaRetenciones> retenciones = facturaRetencionDao.obtenerRetencionesFactura(idFactura);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "retenciones creadas exitosamente");
			respuestaDto.setObjetoRespuesta(retenciones);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error al guardar la retencion");
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error al guardar las retenciones"+e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}
}
