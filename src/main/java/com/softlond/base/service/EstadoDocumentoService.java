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
import com.softlond.base.entity.EstadoDocumento;
import com.softlond.base.repository.EstadoDocumentoDao;

@Service
public class EstadoDocumentoService {

	private static final Logger logger = Logger.getLogger(UsuarioService.class);

	@Autowired
	private EstadoDocumentoDao estadoDocumentoDao;

	// listar
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerEstadosDocumento() {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		try {

			ArrayList<EstadoDocumento> estadoDocumento = this.estadoDocumentoDao.obtenerEstadosDocumento();

			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de estado documento exitosa");
			respuestaDto.setObjetoRespuesta(estadoDocumento);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error(
					"Error obteniendo estado documento " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error obteniendo estado documento");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	// Listar nombres estados
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerNombresEstados() {
		ResponseEntity<Object> respuesta;
		try {
			List<String> estados = estadoDocumentoDao.obtenerNombresEstados();
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de estados exitosa");
			respuestaDto.setObjetoRespuesta(estados);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error("Error al obtener el estado" + e.getMessage());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"No se pudo obtener los estados");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerEstadosDocumentoTraslados() {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		try {
			ArrayList<EstadoDocumento> estadoDocumento = this.estadoDocumentoDao.obtenerEstadosDocumentoTraslado();
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de estado documento exitosa");
			respuestaDto.setObjetoRespuesta(estadoDocumento);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error(
					"Error obteniendo estado documento " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error obteniendo estado documento");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

}
