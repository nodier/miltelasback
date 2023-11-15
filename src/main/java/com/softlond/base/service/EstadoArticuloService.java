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
import com.softlond.base.entity.EstadoArticulo;
import com.softlond.base.repository.EstadoArticuloDao;

@Service
public class EstadoArticuloService {
	 private final Logger logger = Logger.getLogger(EstadoArticuloService.class);
	 
	 @Autowired
	    private EstadoArticuloDao estadoArticuloDao;
	 
    @PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> listarEstados() {
		
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		try {
			List<EstadoArticulo> estado = this.estadoArticuloDao.listarEstados();
			
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito listando los estados");
			respuestaDto.setObjetoRespuesta(estado);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} 
		catch (Exception e) {
			logger.error("Error en el listar los estados");
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error en el listar los estados");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return respuesta;
	}
}
