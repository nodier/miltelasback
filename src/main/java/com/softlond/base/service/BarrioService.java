package com.softlond.base.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.Barrio;
import com.softlond.base.repository.BarrioDao;

@Service
public class BarrioService {
	
	private static final Logger logger = Logger.getLogger(BarrioService.class);
	
	@Autowired
	private BarrioDao barrioDao;
	
	public ResponseEntity<Object> obtenerBarrios(Integer idCiudad){
		ResponseEntity<Object> respuesta;
		try {
			List<Barrio> barrios = this.barrioDao.obtenerBarriosCiudad(idCiudad);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de barrios exitosa");
			respuestaDto.setObjetoRespuesta(barrios);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error("Error al obtener barrios");
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error al obtener los barrios");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}
	
	
	public ResponseEntity<Object> agregarBarrio(Barrio barrio){
		ResponseEntity<Object> respuesta;
		try {
			Barrio barrioSave = this.barrioDao.save(barrio);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "barrio guardado con exitosa");
			respuestaDto.setObjetoRespuesta(barrioSave);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error("Error al guardar el barrio"+e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error al guardar el barrio");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}
	
	

}
