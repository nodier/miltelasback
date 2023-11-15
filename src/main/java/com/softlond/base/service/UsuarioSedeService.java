package com.softlond.base.service;


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
import com.softlond.base.entity.InformacionUsuario;
import com.softlond.base.entity.Organizacion;
import com.softlond.base.entity.Usuario;
import com.softlond.base.repository.OrganizacionDao;
import com.softlond.base.repository.UsuarioSedeDao;
import com.softlond.base.request.UsuarioSedeRequest;
import com.softlond.base.response.UserSedeResponse;

@Service
public class UsuarioSedeService {

	private static final Logger logger = Logger.getLogger(UsuarioSedeService.class);
	
	@Autowired
	UsuarioSedeDao usuarioSedeDao;
	
	@Autowired
	OrganizacionDao organizacionDao;
	
	
	@PreAuthorize("hasAuthority('SUPER')")
	public ResponseEntity<Object> AsignarUsuarioASede(UsuarioSedeRequest usuarioSede){
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		try {
			usuarioSedeDao.AsignarSedeAUsuario(usuarioSede.getUserId(),usuarioSede.getSedeId());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Asignacion del usuario exitosa");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error al asignar el usuario " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error asignando el usuario");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return respuesta;
		
	}
	
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('VENDEDOR') or hasAuthority('ADMIN')")
	public ResponseEntity<Object> ObtenerUsuariosConSede(){
		List<UserSedeResponse>listUserSede = new ArrayList<UserSedeResponse>();
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		try {
			List<InformacionUsuario> usuarios = usuarioSedeDao.obtenerUsuarios();
			
			for (InformacionUsuario usuario : usuarios) {
				List<Organizacion> sedes = organizacionDao.obtenerSedes(usuario.getId());
				for (Organizacion sede : sedes) {
					UserSedeResponse userSede = new UserSedeResponse(usuario, sede);
					listUserSede.add(userSede);
				}
			}
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de usuarios exitosa");
			respuestaDto.setObjetoRespuesta(listUserSede);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo usuarios " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error obteniendo usuarios");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}
	
	@PreAuthorize("hasAuthority('SUPER')")
	public ResponseEntity<Object> deleteUsuarioSede(UsuarioSedeRequest usuarioSede){
		ResponseEntity<Object> respuesta;
		try {
			this.usuarioSedeDao.eliminarSedeAUsuario(usuarioSede.getUserId(), usuarioSede.getSedeId());
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "asignacion eliminada exitosamente");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
			return respuesta;
		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.INTERNAL_SERVER_ERROR);
			logger.error("Error eliminando la asignacion " + e.getMessage());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error eliminando la asignacion");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
			return respuesta;
		}
	}
	//obtener la sede asociada a un usuario
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> ObtenerSedeUsuario(){
		
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		
		try {
			Organizacion sede = organizacionDao.obtenerSede(usuarioAutenticado.getId()); 
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de usuarios exitosa");
			respuestaDto.setObjetoRespuesta(sede);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);			
		} catch (Exception e) {
			logger.error("Error obteniendo usuarios " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error obteniendo usuarios");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}
	
}
