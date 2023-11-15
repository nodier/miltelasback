package com.softlond.base.service;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.ConceptoNotaCredito;
import com.softlond.base.entity.ConceptoNotaDebito;
import com.softlond.base.repository.ConceptoNotaDebitoDao;

@Service
public class ConceptoNotaDebitoService {

private static final Logger logger = Logger.getLogger(ConceptoNotaCredito.class);
	
	@Autowired
	public ConceptoNotaDebitoDao conceptoNdDao;
	
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> crearConceptoND(@RequestBody ConceptoNotaDebito conceptoND) {
		
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		try {	
			
			    ConceptoNotaDebito guardado = this.conceptoNdDao.save(conceptoND);
				
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito creando conceptos ND");
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);	
			
		}
		catch (Exception e) {
			logger.error("Error en la creación de conceptos ND");
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error en la creación de conceptos ND");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return respuesta;
	}
	
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerNDConceptos(Integer idNotaDebito){
		
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		
		try {
				
			ArrayList<ConceptoNotaDebito> concepto = this.conceptoNdDao.obtenerConceptoIdNotaDebito(idNotaDebito);	
			
			
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de conceptos exitosa");
			respuestaDto.setObjetoRespuesta(concepto);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo conceptos " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error obteniendo conceptos");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}
	
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> actualizarConceptoND(@RequestBody ConceptoNotaDebito conceptoND) {
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		try {	
			    ConceptoNotaDebito guardado = this.conceptoNdDao.save(conceptoND);
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito creando conceptos ND");
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);	
			
		}
		catch (Exception e) {
			logger.error("Error en la creación de conceptos ND");
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error en la creación de conceptos ND");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return respuesta;
	}
}
