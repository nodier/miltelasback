package com.softlond.base.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.Motivos;
import com.softlond.base.repository.MotivoDao;

@Service
public class MotivoService {

	private static final Logger logger = Logger.getLogger(MotivoService.class);

	@Autowired
	private MotivoDao motivoDao;

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> listarMotivos() {
		logger.info("listar motivos");
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		try {
			List<Motivos> motivos = (List<Motivos>) this.motivoDao.findAll();
			logger.info(motivos);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito listando los motivos");
			respuestaDto.setObjetoRespuesta(motivos);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error en el listar los estados");
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error en el listar los motivos");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return respuesta;
	}
}
