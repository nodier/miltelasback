package com.softlond.base.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.Caja;
import com.softlond.base.entity.InformacionUsuario;
import com.softlond.base.entity.Organizacion;
import com.softlond.base.entity.Prefijo;
import com.softlond.base.entity.Usuario;
import com.softlond.base.repository.CajaDao;
import com.softlond.base.repository.OrganizacionDao;
import com.softlond.base.repository.UsuarioInformacionDao;

@Service
public class CajaService {
	private static final Logger logger = Logger.getLogger(CajaService.class);
	@Autowired
	public CajaDao cajaDao;

	@Autowired
	OrganizacionDao organizacionDao;
	
	@Autowired
	UsuarioInformacionDao usuarioInformacionDao;

	@PreAuthorize("hasAuthority('SUPER')")
	public ResponseEntity<Object> postCajas(ArrayList<Caja> cajas) {
		ResponseEntity<Object> respuesta;
		try {
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			for (Caja caja : cajas) {
				Caja cajaAux = this.cajaDao.findNombreAndSede(caja.getNombre(), caja.getSede().getId()).orElse(null);
				if (cajaAux != null) {
					RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, "Caja existente");
					respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.BAD_REQUEST);
					return respuesta;
				}
			}
			this.cajaDao.saveAll(cajas);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Caja creada exitosamente");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
			return respuesta;
		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.INTERNAL_SERVER_ERROR);
			logger.error("Error creando caja " + e.getMessage());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error creando la caja");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
			return respuesta;
		}
	}

	@PreAuthorize("hasAuthority('SUPER')")
	public ResponseEntity<Object> deleteCajas(Integer id) {
		ResponseEntity<Object> respuesta;
		try {
			this.cajaDao.deleteById(id);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Caja eliminada exitosamente");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
			return respuesta;
		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.INTERNAL_SERVER_ERROR);
			logger.error("Error eliminando caja " + e.getMessage());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error eliminando la caja");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
			return respuesta;
		}
	}

	@PreAuthorize("hasAuthority('SUPER')")
	public ResponseEntity<Object> updateCajas(Caja caja, Integer id) {

		Caja cajaActual = this.cajaDao.findById(id).orElse(null);
		Caja cajaUpdate = null;
		ResponseEntity<Object> respuesta;
		if (cajaActual == null) {
			respuesta = ResponseEntity.ok(HttpStatus.INTERNAL_SERVER_ERROR);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error actualizando la caja");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
			return respuesta;
		}
		try {
			cajaActual.setNombre(caja.getNombre());
			cajaActual.setDescripcion(caja.getDescripcion());
			cajaActual.setSede(caja.getSede());
			cajaUpdate = this.cajaDao.save(cajaActual);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Caja actualizada exitosamente");
			respuestaDto.setObjetoRespuesta(cajaUpdate);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
			return respuesta;
		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.INTERNAL_SERVER_ERROR);
			logger.error("Error eliminando caja " + e.getMessage());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al actualizar la caja");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
			return respuesta;
		}
	}

	@PreAuthorize("hasAuthority('SUPER')")
	public ResponseEntity<Object> getMyCajas(Integer id) {
		ResponseEntity<Object> respuesta;
		try {
			ArrayList<Caja> cajas = (ArrayList<Caja>) this.cajaDao.findMyCajas(id);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "cajas recuperadas exitosamente");
			respuestaDto.setObjetoRespuesta(cajas);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
			return respuesta;
		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.INTERNAL_SERVER_ERROR);
			logger.error("Error obteniendo cajas " + e.getMessage());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error obteniendo cajas");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
			return respuesta;
		}
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> getMyCajas() {
		ResponseEntity<Object> respuesta;
		try {
			ArrayList<Caja> cajas = (ArrayList<Caja>) this.cajaDao.findMyCajas();
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "cajas recuperadas exitosamente");
			respuestaDto.setObjetoRespuesta(cajas);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
			return respuesta;
		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.INTERNAL_SERVER_ERROR);
			logger.error("Error obteniendo cajas " + e.getMessage());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error obteniendo cajas");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
			return respuesta;
		}
	}

	// Obtener las cajas por sede
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> getCajasSede() {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();

		try {
			Organizacion sede = organizacionDao.obtenerSede(usuarioAutenticado.getId());
			ArrayList<Caja> cajas = this.cajaDao.obtenerCajasSede(sede.getId());

			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de cajas exitosa");
			respuestaDto.setObjetoRespuesta(cajas);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo usuarios " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error obteniendo usuarios");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	// Obtener las cajas por sede informes
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> getCajasSedeInforme(String idSedeRequest) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Integer idSede;
		if(idSedeRequest.equals("null")) {
			Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
			Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
			InformacionUsuario usuarioInformacion = this.usuarioInformacionDao.buscarPorIdUsuario(usuarioAutenticado.getId());
			idSede = usuarioInformacion.getIdOrganizacion().getId();
		}
		else {
			idSede = Integer.parseInt(idSedeRequest);
		}
		try {
			ArrayList<Caja> cajas = this.cajaDao.obtenerCajasSede(idSede);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de cajas exitosa");
			respuestaDto.setObjetoRespuesta(cajas);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo usuarios " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error obteniendo usuarios");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}
}
