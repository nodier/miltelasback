package com.softlond.base.service;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.CajaCierre;
import com.softlond.base.entity.Usuario;
import com.softlond.base.repository.CajaCierreDao;

@Service
public class CajaCierreService {
	private static final Logger logger = Logger.getLogger(CajaService.class);

	@Autowired
	public CajaCierreDao cajaCierreDao;

	// Obtener el cierre de caja
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> cierreCaja(Integer idCaja) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		try {
			ArrayList<CajaCierre> cierre = this.cajaCierreDao.obtenerCierre(idCaja);
			if (cierre != null) {
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de cierre de caja");
				respuestaDto.setObjetoRespuesta(cierre);
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
			} else {
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
						"No se encuentra cierre de caja registrado");
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
			}

		} catch (Exception e) {
			logger.error(
					"Error obteniendo cierre de caja " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
		}
		return respuesta;
	}

	// Crear cierre de caja en el sistema
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> registrarCierre(@RequestBody CajaCierre cierre) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();

		try {
			cierre.setIdCreador(usuarioAutenticado);

			CajaCierre guardado = this.cajaCierreDao.save(cierre);

			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito registrando el ciere");
			respuestaDto.setObjetoRespuesta(guardado);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);

		} catch (Exception e) {
			logger.error("Error en el registro del cierre");
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error en el registro del cierre");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return respuesta;
	}
}
