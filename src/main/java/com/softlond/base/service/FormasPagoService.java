package com.softlond.base.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.FormasPago;
import com.softlond.base.repository.FormasPagoDao;

@Service
public class FormasPagoService {
	
	private static final Logger logger = Logger.getLogger(FormasPagoService.class);
	
	@Autowired
	private FormasPagoDao formasPagoDao;

	
	//Obtener las formas de pago
	@PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('SUPER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerFormasPago(){
		
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		
		
		try {
								
			ArrayList<FormasPago> formasPagos = this.formasPagoDao.findAll();
								
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de formas de pago exitosa");
			respuestaDto.setObjetoRespuesta(formasPagos);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo formas de pago " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error obteniendo formas de pago");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}
	
	@PreAuthorize("hasAuthority('USER') or hasAuthority('SUPER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerFormaPago(Integer id){
		
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		
		
		try {
								
			FormasPago formaPagos = this.formasPagoDao.findById(id).orElse(null);
								
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de forma de pago exitosa");
			respuestaDto.setObjetoRespuesta(formaPagos);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo formas de pago " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error obteniendo forma de pago");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}
	

}
