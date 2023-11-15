package com.softlond.base.service;

import java.sql.Date;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.InformacionUsuario;
import com.softlond.base.entity.Organizacion;
import com.softlond.base.entity.PeriodosContables;
import com.softlond.base.entity.Prefijo;
import com.softlond.base.entity.Usuario;
import com.softlond.base.repository.OrganizacionDao;
import com.softlond.base.repository.PeriodosContablesDao;
import com.softlond.base.repository.PrefijoDao;
import com.softlond.base.repository.UsuarioInformacionDao;

@Service
public class PeriodosContablesService {

	private static final Logger logger = Logger.getLogger(PerfilService.class);
	
	@Autowired
	private PeriodosContablesDao periodosContablesDao;
	
	@Autowired
	OrganizacionDao organizacionDao;
	
	@Autowired
	UsuarioInformacionDao usuarioInformacionDao;
	
	//Obtener los periodos contables
			@PreAuthorize("hasAuthority('USER') or hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
			public ResponseEntity<Object> obtenerPeriodo(){
				
				ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
			
				
				Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
				Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
				
				try {
					Organizacion sede = organizacionDao.obtenerSede(usuarioAutenticado.getId()); 					
					ArrayList<PeriodosContables> periodo = this.periodosContablesDao.findPeriodoSede(sede.getId());
										
					RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion periodos contables  exitosa");
					respuestaDto.setObjetoRespuesta(periodo);
					respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
				} catch (Exception e) {
					logger.error("Error obteniendo periodos contables " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
					RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error obteniendo periodos contables");
					respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
				}
				return respuesta;
			}
			
			//Obtener los periodos contables
			@PreAuthorize("hasAuthority('USER') or hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
			public ResponseEntity<Object> obtenerPeriodoSede(String idSedeRequest){
				
				ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
			
				int idSede;
				if (idSedeRequest.equals("null")) {
					Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
					Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
					InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
							.buscarPorIdUsuario(usuarioAutenticado.getId());
					idSede = usuarioInformacion.getIdOrganizacion().getId();
				} else {
					idSede = Integer.parseInt(idSedeRequest);
				}
				
				try {					
					ArrayList<PeriodosContables> periodo = this.periodosContablesDao.findPeriodoSede(idSede);
										
					RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion periodos contables  exitosa");
					respuestaDto.setObjetoRespuesta(periodo);
					respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
				} catch (Exception e) {
					logger.error("Error obteniendo periodos contables " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
					RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error obteniendo periodos contables");
					respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
				}
				return respuesta;
			}
}
