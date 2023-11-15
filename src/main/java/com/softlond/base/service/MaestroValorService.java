package com.softlond.base.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.MaestroValor;
import com.softlond.base.repository.MaestroValorDao;

@Service
public class MaestroValorService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private MaestroValorDao maestroValorDao;

	public @ResponseBody ResponseEntity<Object> obtenerPorObjetivo(@RequestBody String objetivo,
			HttpServletRequest request, HttpServletResponse response) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		ArrayList<MaestroValor> listadoMaestros = new ArrayList<MaestroValor>();

		try {
			if (objetivo.toUpperCase() != null) {
				logger.info(objetivo.toUpperCase());
			}
			listadoMaestros = this.maestroValorDao.findByObjetivo(objetivo.toUpperCase());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito");
			if (listadoMaestros != null) {
				logger.info(listadoMaestros.toString());
			}
			respuestaDto.setObjetoRespuesta(listadoMaestros);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error se presentan problemas al cargar el elemento maestro valor ", e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error cargando el elemento maestro valor");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return respuesta;
	}

	public ResponseEntity<Object> obtenerDocumentos() {
		ResponseEntity<Object> respuesta;
		try {
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			List<MaestroValor> listadoDocumentos = this.maestroValorDao.obtenerClasificacionesLegales();
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito");
			respuestaDto.setObjetoRespuesta(listadoDocumentos);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error("Error se presentan problemas al cargar el elemento maestro valor ", e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error cargando el elemento maestro valor");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	public ResponseEntity<Object> obtenerTipoProveedor() {
		ResponseEntity<Object> respuesta;
		try {
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			List<MaestroValor> listadoDocumentos = this.maestroValorDao.obtenerTipoProveedor();
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito");
			respuestaDto.setObjetoRespuesta(listadoDocumentos);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error("Error se presentan problemas al cargar el elemento maestro valor ", e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error cargando el elemento maestro valor");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	public ResponseEntity<Object> obtenerMotivosSalida() {
		ResponseEntity<Object> respuesta;
		try {
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			List<MaestroValor> listadoDocumentos = this.maestroValorDao.obtenerMotivosSalida();
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito");
			respuestaDto.setObjetoRespuesta(listadoDocumentos);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error("Error se presentan problemas al cargar el elemento maestro valor ", e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error cargando el elemento maestro valor");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

}
