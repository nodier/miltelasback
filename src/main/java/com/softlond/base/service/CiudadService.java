package com.softlond.base.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.repository.CiudadDao;

@Service
public class CiudadService {

	private static final Logger logger = Logger.getLogger(CiudadService.class);

	@Autowired
	CiudadDao ciudadDao;
	
	
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('SOCIO') or hasAuthority('VALIDADOR') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerTodas() {
		
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		
		try {
			
			
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito listando todas las ciudades");
			respuestaDto.setObjetoRespuesta(this.ciudadDao.listarOrdenadasNombre());
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} 
		catch (Exception e) {
			logger.error("Error se presentan problemas al cargar las ciudades");
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error se presentan problemas al cargar las ciudades");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return respuesta;
	}
	
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('SOCIO') or hasAuthority('VALIDADOR') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerPorDepartamento(Integer idDepartamento) {
		
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		
		try {
			
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito listando todas las ciudades");
			respuestaDto.setObjetoRespuesta(this.ciudadDao.buscarPorDepartamento(idDepartamento));
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} 
		catch (Exception e) {
			logger.error("Error se presentan problemas al cargar las ciudades por departamento");
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error se presentan problemas al cargar las ciudades por departamento");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return respuesta;
	}
	
}