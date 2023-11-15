package com.softlond.base.service;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.FormasPago;
import com.softlond.base.entity.RbcTipoTarjetas;
import com.softlond.base.repository.FormasPagoDao;
import com.softlond.base.repository.TipoTarjetasDao;

@Service
public class TipoTarjetaService {
	
	private static final Logger logger = Logger.getLogger(CiudadService.class);
	
	@Autowired
	private TipoTarjetasDao tipoTarjetasDao;

	
	//Obtener tipos de tarjeta
	@PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('SUPER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerTiposTarjetas(){
		
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		
		
		try {
								
			ArrayList<RbcTipoTarjetas> tiposTarjetas = this.tipoTarjetasDao.findAll();
								
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de formas de pago exitosa");
			respuestaDto.setObjetoRespuesta(tiposTarjetas);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo tipos de tarjeta " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error obteniendo tipos de tarjetas");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

}
