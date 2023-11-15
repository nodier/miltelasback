package com.softlond.base.service;

import java.util.Optional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.ReciboCajaVenta;
import com.softlond.base.entity.Sequence;
import com.softlond.base.entity.Prefijo;
import com.softlond.base.repository.PrefijoDao;
import com.softlond.base.repository.SequenceDao;

@Service
public class SequenceService {

	private static final Logger logger = Logger.getLogger(SequenceService.class);
	@Autowired
	private SequenceDao sequenceDao;

	@Autowired
	private PrefijoDao prefijoDao;

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerSequencia(Integer idSede, Integer idPrefijo) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		try {
			logger.info(idSede);
			logger.info(idPrefijo);
			Integer sequence = sequenceDao.sequencia(idSede, idPrefijo);
			logger.info(sequence);
			// Optional<Prefijo> p = prefijoDao.findById(idPrefijo);
			// Integer sequence1 = sequenceDao.sequenciaNotaCredito("NTC1",idSede,
			// idPrefijo);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito obteniendo sequencia");
			respuestaDto.setObjetoRespuesta(sequence);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error en obtener la sequencia " + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "error obteniendo sequencia " + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerSequencia2(String prefijo, Integer idSede, Integer idPrefijo) {
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		try {
			logger.info(idSede);
			logger.info(idPrefijo);
			Integer sequence = sequenceDao.sequenciaNueva(prefijo, idSede, idPrefijo);
			logger.info(sequence);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito obteniendo sequencia");
			respuestaDto.setObjetoRespuesta(sequence);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error en obtener la sequencia " + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "error obteniendo sequencia " + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerSequencia3(String prefijo, Integer idSede, Integer idPrefijo) {
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		try {

			Integer sequence = sequenceDao.obtenerUltimaSecuencia(idSede, idPrefijo);

			sequence = sequence + 1;
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito obteniendo sequencia");
			respuestaDto.setObjetoRespuesta(sequence);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error en obtener la sequencia " + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "error obteniendo sequencia " + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerSequencia4(String prefijo, Integer idSede, Integer idPrefijo) {
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		try {

			Integer sequence = sequenceDao.obtenerUltimaSecuencia(idSede, idPrefijo);

			sequence = sequence + 1;
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito obteniendo sequencia");
			respuestaDto.setObjetoRespuesta(sequence);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error en obtener la sequencia " + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "error obteniendo sequencia " + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

}
