package com.softlond.base.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.ClasificacionLegal;
import com.softlond.base.repository.ClasificacionLegalDao;

@Service
public class ClasificacionLegalService {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ClasificacionLegalDao clasificacionLegalDao;
	
	public ResponseEntity<Object> obtenerClasificacionLegal(){
		ResponseEntity<Object> respuesta;
		try {
			List<ClasificacionLegal> clasificaciones = this.clasificacionLegalDao.findByVisible(true);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito");
			respuestaDto.setObjetoRespuesta(clasificaciones);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error("Error se presentan problemas al cargar el elemento clasificacion legal ", e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error cargando el elemento clasificacion legal");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}
}
