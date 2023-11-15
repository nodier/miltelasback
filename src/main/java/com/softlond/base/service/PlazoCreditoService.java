package com.softlond.base.service;

import java.sql.Date;
import java.util.ArrayList;

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
import com.softlond.base.entity.PlazoCredito;
import com.softlond.base.entity.Prefijo;
import com.softlond.base.entity.Usuario;
import com.softlond.base.repository.PlazoCreditoDao;

@Service
public class PlazoCreditoService {

	private static final Logger logger = Logger.getLogger(PrefijoService.class);
	
	@Autowired
	private PlazoCreditoDao plazoCreditoDao; 
	
	// Listar todos los plazos de credito
			@PreAuthorize("hasAuthority('SUPER')")
			public ResponseEntity<Object> obtenerTodos() {
				
				ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
				
				try {
					ArrayList<PlazoCredito> plazo = this.plazoCreditoDao.findAllByOrderByFechaCreacionDesc();
					
					RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito listando los plazos de crédito");
					respuestaDto.setObjetoRespuesta(plazo);
					respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
				} 
				catch (Exception e) {
					logger.error("Error en el listar los plazos de Crédito");
					RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error en el listar los plazos de crédito");
					respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
				}
				
				return respuesta;
			}

			
			// Crear plazos de crédito en el sistema
			@PreAuthorize("hasAuthority('SUPER')")
			public ResponseEntity<Object> crearPlazoCredito(@RequestBody PlazoCredito plazoCreditoNuevo) {
				
				ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
				
				Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
				Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
				
				try {
					if (!this.existePlazoCredito(plazoCreditoNuevo)) {
						
						plazoCreditoNuevo.setFechaCreacion(new Date(new java.util.Date().getTime()));
						plazoCreditoNuevo.setIdCreador(usuarioAutenticado);
						
						PlazoCredito guardado = this.plazoCreditoDao.save(plazoCreditoNuevo);
						
						RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito creando el plazo de crédito");
						respuestaDto.setObjetoRespuesta(guardado);
						respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
					} 
					else {
						RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, "Error, ya existe");
						respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.BAD_REQUEST);
					}
				}
				catch (Exception e) {
					logger.error("Error en la creación del plazo de crédito");
					RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error en el listar los plazos de crédito");
					respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
				}
				
				return respuesta;
			}
			

			// Actualizar plazos de crédito
			@PreAuthorize("hasAuthority('SUPER')")
			public ResponseEntity<Object> actualizarPlazoCredito(@RequestBody PlazoCredito plazoCreditoActualizado) {
				
				ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
				
				Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
				Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
				
				PlazoCredito plazoCreditoEnBd = this.plazoCreditoDao.findById(plazoCreditoActualizado.getId()).get();
				
				try {
					
					if (!plazoCreditoEnBd.getId().equals(plazoCreditoActualizado.getId())) {
						
						if (!this.existePlazoCredito(plazoCreditoActualizado)) {
							
											
							PlazoCredito actualizada = this.plazoCreditoDao.save(plazoCreditoActualizado);
							
							RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito actualizando el plazo de Crédito");
							respuestaDto.setObjetoRespuesta(actualizada);
							respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
						} 
						else {
							RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, "Error, el plazo de crédito ya existe");
							respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.BAD_REQUEST);
						}
					}
					else {
						
						
						PlazoCredito actualizada = this.plazoCreditoDao.save(plazoCreditoActualizado);
						
						RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito actualizando el plazo de crédito");
						respuestaDto.setObjetoRespuesta(actualizada);
						respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
					}
				} 
				catch (Exception e) {
					logger.error("Error en la actualización del plazo de crédito");
					RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error en la actualización del plazo de Crédito");
					respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
				}
				
				return respuesta;
			}
			
			// Borrar plazo credito en el sistema sistema
			@PreAuthorize("hasAuthority('SUPER')")
			public ResponseEntity<Object> borrarPlazoCredito(@RequestParam Integer idPlazoCredito) {
				
				ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
				
				try {
					this.plazoCreditoDao.delete(this.plazoCreditoDao.findById(idPlazoCredito).get());
					
					RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito");
					respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
				} 
				catch (Exception e) {
					Throwable t = e.getCause();
					
					if (t instanceof ConstraintViolationException) {
						logger.error("El plazo de crédito se encuentra asociada en el sistema " + e.getCause());
						RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, "Error,  se encuentra asociada en el sistema");
						respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.BAD_REQUEST);
					} 
					else {
						logger.error("Error en el borrado del plazo de crédito");
						RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error en el borrado del plazo de crédito");
						respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
					}
				}
				
				return respuesta;
			}

			private boolean existePlazoCredito(PlazoCredito plazoCreditoBuscar) {
				
				boolean flag = false;
				
				if (this.plazoCreditoDao.findByNombre(plazoCreditoBuscar.getNombre()) != null) {
					flag = true;
				} 
				
				return flag;
			}
}
