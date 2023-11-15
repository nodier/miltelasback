package com.softlond.base.service;

import java.sql.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.InformacionUsuario;
import com.softlond.base.entity.Organizacion;
import com.softlond.base.entity.Usuario;
import com.softlond.base.repository.OrganizacionDao;
import com.softlond.base.repository.UsuarioDao;
import com.softlond.base.repository.UsuarioInformacionDao;

@Service
public class OrganizacionService {

	private static final Logger logger = Logger.getLogger(OrganizacionService.class);

	@Autowired
	private OrganizacionDao organizacionDao;

	@Autowired
	UsuarioDao usuarioDao;

	@Autowired
	UsuarioInformacionDao usuarioInformacionDao;

	
	// Listar todos las organizaciones
	@PreAuthorize("hasAuthority('SUPER')")
	public ResponseEntity<Object> obtenerTodas() {
		
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		
		try {
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito listando las organizaciones");
			respuestaDto.setObjetoRespuesta(this.organizacionDao.findAllByOrderByFechaCreacionDesc());
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} 
		catch (Exception e) {
			logger.error("Error en el listar de organizaciones");
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error en el listar de organizaciones");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return respuesta;
	}

	
	// Crear compania en el sistema
	@PreAuthorize("hasAuthority('SUPER')")
	public ResponseEntity<Object> crerOrganizacion(@RequestBody Organizacion organizacionNueva) {
		
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		
		try {
			if (!this.existeOrganizacion(organizacionNueva)) {
				
				organizacionNueva.setFechaCreacion(new Date(new java.util.Date().getTime()));
				organizacionNueva.setFechaActualizacion(new Date(new java.util.Date().getTime()));
				organizacionNueva.setIdUltimoUsuarioModifico(usuarioAutenticado);
				organizacionNueva.setIdCreador(usuarioAutenticado);
				
				Organizacion guardada = this.organizacionDao.save(organizacionNueva);
				
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito creando la organización");
				respuestaDto.setObjetoRespuesta(guardada);
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
			} 
			else {
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, "Error, la organización ya existe");
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.BAD_REQUEST);
			}
		}
		catch (Exception e) {
			logger.error("Error en la creación de la organizacion");
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error en el listar de organizaciones");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return respuesta;
	}

	
	// Actualizar organizacion
	@PreAuthorize("hasAuthority('SUPER')")
	public ResponseEntity<Object> actualizarOrganizacion(@RequestBody Organizacion organizacionActualizar) {
		
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		
		Organizacion organizacionEnBd = this.organizacionDao.findById(organizacionActualizar.getId()).get();
		
		try {
			
			if (!organizacionEnBd.getIdUnico().equals(organizacionActualizar.getIdUnico())) {
				
				if (!this.existeOrganizacion(organizacionActualizar)) {
					
					organizacionActualizar.setFechaActualizacion(new Date(new java.util.Date().getTime()));
					organizacionActualizar.setIdUltimoUsuarioModifico(usuarioAutenticado);
					
					Organizacion actualizada = this.organizacionDao.save(organizacionActualizar);
					
					RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito actualizando la organización");
					respuestaDto.setObjetoRespuesta(actualizada);
					respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
				} 
				else {
					RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, "Error, la organización ya existe");
					respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.BAD_REQUEST);
				}
			}
			else {
				organizacionActualizar.setFechaActualizacion(new Date(new java.util.Date().getTime()));
				organizacionActualizar.setIdUltimoUsuarioModifico(usuarioAutenticado);
				
				Organizacion actualizada = this.organizacionDao.save(organizacionActualizar);
				
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito actualizando la organización");
				respuestaDto.setObjetoRespuesta(actualizada);
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
			}
		} 
		catch (Exception e) {
			logger.error("Error en la actualización de la organizacion");
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error en la actualización de la organizacion");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return respuesta;
	}


	// Borrar organizacion en el sistema sistema
	@PreAuthorize("hasAuthority('SUPER')")
	public ResponseEntity<Object> borrarOrganizacion(@RequestParam Integer idOrganizacion) {
		
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		
		try {
			this.organizacionDao.delete(this.organizacionDao.findById(idOrganizacion).get());
			
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} 
		catch (Exception e) {
			Throwable t = e.getCause();
			
			if (t instanceof ConstraintViolationException) {
				logger.error("La organizción se encuentra asociada en el sistema " + e.getCause());
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, "Error, la organizción se encuentra asociada en el sistema");
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.BAD_REQUEST);
			} 
			else {
				logger.error("Error en el borrado de la organizacion");
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error en el borrado de la organizacion");
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		return respuesta;
	}

	// Obtener la información de compañia del usuario autenticado actualmente
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('SOCIO') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VALORADOR_RESO') "
			+ "or hasAuthority('GESTION_DOCUMENTAL') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> verMiOrganizacion() {
		
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao.buscarPorIdUsuario(usuarioAutenticado.getId());
		
		try {	
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito");
			respuestaDto.setObjetoRespuesta(usuarioInformacion.getIdOrganizacion());
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} 
		catch (Exception e) {
			logger.error("Error al consultar la organizacion del usuario");
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error al consultar la organizacion del usuario");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return respuesta;
	}
	

	private boolean existeOrganizacion(Organizacion organizacionBuscar) {
		
		boolean flag = false;
		
		if (this.organizacionDao.findByIdUnico(organizacionBuscar.getIdUnico()) != null) {
			flag = true;
		} 
		
		return flag;
	}

}
