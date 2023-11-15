package com.softlond.base.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.InformacionUsuario;
import com.softlond.base.entity.Organizacion;
import com.softlond.base.entity.Usuario;
import com.softlond.base.repository.MaestroValorDao;
import com.softlond.base.repository.OrganizacionDao;
import com.softlond.base.repository.PerfilDao;
import com.softlond.base.repository.PlanDao;
import com.softlond.base.repository.UsuarioDao;
import com.softlond.base.repository.UsuarioInformacionDao;
import com.softlond.base.request.CambioContrasenaReq;
import com.softlond.base.request.UsuarioAuxReq;
import com.softlond.base.response.UserDetailResponse;


@Service
public class UsuarioService {

	private static final Logger logger = Logger.getLogger(UsuarioService.class);

	@Autowired
	UsuarioDao usuarioDao;

	@Autowired
	UsuarioInformacionDao usuarioInformacionDao;

	@Autowired
	PerfilDao perfilDao;

	@Autowired
	MaestroValorDao maestroValorDao;
	
	@Autowired
	PlanDao planDao;
	
	@Autowired
	OrganizacionDao organizacionDao;

	@Autowired
	private PasswordEncoder cifradoContrasena;

	
	/* Listar todos los usuarios siendo el SUPER_USER */
	@PreAuthorize("hasAuthority('SUPER')")
	public ResponseEntity<Object> listarTodos() {
		
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		
		try {		
			ArrayList<InformacionUsuario> users = this.usuarioInformacionDao.obtenerTodos();
			
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito listando los administradores");
			respuestaDto.setObjetoRespuesta(users);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		}
		catch (Exception e) {
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error listando los administradores como super " + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return respuesta;
	}
	
	
	/* Metodo para crear usuarios en el sistema siendo un super */
	@PreAuthorize("hasAuthority('SUPER')")
	public ResponseEntity<Object> crearUsuario(@Valid @RequestBody UsuarioAuxReq nuevoUsuarioAux, HttpServletRequest request, HttpServletResponse response) {
		
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		
		Authentication authenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) authenticacion.getPrincipal();
		Usuario nuevoUsuario = nuevoUsuarioAux.getUsuario();
		
		
		
		try {
			
			if (!existePorNombreUsuario(nuevoUsuario.getUsername())) {
				
				nuevoUsuario.setPassword(cifradoContrasena.encode(nuevoUsuario.getPassword()));
				Usuario savedUser = usuarioDao.save(nuevoUsuario);
				
				InformacionUsuario nuevoUsuarioInformacion = nuevoUsuarioAux.getUsuarioInformacion();
				nuevoUsuarioInformacion.setIdUsuario(savedUser);
				nuevoUsuarioInformacion.setIdCreador(usuarioAutenticado);
				nuevoUsuarioInformacion.setFechaCreacion(new Date(new java.util.Date().getTime()));
				nuevoUsuarioInformacion.setIdPerfilActivado(perfilDao.findById(nuevoUsuarioAux.getUsuarioInformacion().getIdPerfilActivado().getId()).get());
				nuevoUsuarioInformacion.setIdPlanActivado(planDao.findById(nuevoUsuarioAux.getUsuarioInformacion().getIdPlanActivado().getId()).get());
				nuevoUsuarioInformacion.setIdOrganizacion(this.organizacionDao.findById(nuevoUsuarioAux.getUsuarioInformacion().getIdOrganizacion().getId()).get());
				this.usuarioInformacionDao.save(nuevoUsuarioInformacion);

				if(nuevoUsuarioInformacion.getIdRol().getId() == 11) {
					this.usuarioDao.guardarAutoridad(nuevoUsuario.getId(), 1);
				}
				if(nuevoUsuarioInformacion.getIdRol().getId() == 12) {
					this.usuarioDao.guardarAutoridad(nuevoUsuario.getId(), 2);
				}
				if(nuevoUsuarioInformacion.getIdRol().getId() == 13) {
					this.usuarioDao.guardarAutoridad(nuevoUsuario.getId(), 3);
				}
				if(nuevoUsuarioInformacion.getIdRol().getId() == 22) {
					this.usuarioDao.guardarAutoridad(nuevoUsuario.getId(), 4);
				}
				
				if(nuevoUsuarioInformacion.getIdRol().getId() == 25) {
					this.usuarioDao.guardarAutoridad(nuevoUsuario.getId(), 5);
				}
				
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Creación del usuario exitosa");
				respuestaDto.setObjetoRespuesta(nuevoUsuarioInformacion);
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);	
			} 
			else {
				logger.error("Error creando el usuario, ya existe uno con el mismo email");
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, "Error creando el usuario, ya existe uno con el mismo email");
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.BAD_REQUEST);
			}
		} 
		catch (Exception e) {
			logger.error("Error al crear el usuario " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error creando el usuario");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return respuesta;
	}

	
	/* Metodo para actualizar usuarios en el sistema */
	@PreAuthorize("hasAuthority('SUPER')")
	public @ResponseBody ResponseEntity<Object> actualizarUsuario(@RequestBody InformacionUsuario usuarioActualizar, HttpServletRequest request, HttpServletResponse response) {
		
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		
		InformacionUsuario nuevoActualizar = this.usuarioInformacionDao.findById(usuarioActualizar.getId()).get();
		
		try {
			
			if (!(usuarioActualizar.getCorreo().equals(nuevoActualizar.getCorreo()))) {
				
				if (this.existePorEmail(usuarioActualizar.getCorreo())) {
					logger.error("Error creando el usuario, ya existe uno con el mismo email");
					RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, "Error creando el usuario, ya existe uno con el mismo email");
					respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.BAD_REQUEST);
				} 
				else {
					usuarioActualizar.setFechaActualizacion(new Date(new java.util.Date().getTime()));
					usuarioActualizar.setIdUltimoUsuarioModifico(usuarioAutenticado);
					
					this.usuarioInformacionDao.save(usuarioActualizar);
					
					RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Actualización del usuario exitosa");
					respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
				}
			}
			
			usuarioActualizar.setFechaActualizacion(new Date(new java.util.Date().getTime()));
			usuarioActualizar.setIdUltimoUsuarioModifico(usuarioAutenticado);
			
			this.usuarioInformacionDao.save(usuarioActualizar);
			
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Actualización del usuario exitosa");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} 
		catch (Exception e) {
			logger.error("Error actualizando usuario");
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error creando el usuario");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return respuesta;
	}
	
	
	/* SE RECIBE UN correo de un USUARIO PARA EL BORRADO DEL MISMO */
	@PreAuthorize("hasAuthority('SUPER')")
	public @ResponseBody ResponseEntity<Object> borrarUsuario(@RequestParam String nombreUsuario) {
		
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		
		Usuario userAux = this.usuarioDao.buscarPorNombreUsuario(nombreUsuario);
		InformacionUsuario userInfoAux = this.usuarioInformacionDao.buscarPorCorreo(nombreUsuario);
		
		try {
			
			this.usuarioInformacionDao.delete(userInfoAux);
			this.usuarioDao.borrarAutoridad(userAux.getId());
			this.usuarioDao.delete(userAux);
			
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito borrando el usuario");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		}
		catch (Exception e) {
			
			
				logger.error("Error en el borrado del usuario");
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error en el borrado del usuario");
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		
		
		return respuesta;
	}

	
	/* Metodo para cambiar la contraseña de un usuario del sistema DESDE SUPER */
	@PreAuthorize("hasAuthority('SUPER')")
	public ResponseEntity<Object> cambiarContrasena(@Valid @RequestBody CambioContrasenaReq cambiarContrasenaAux, HttpServletRequest request, HttpServletResponse response) {
		
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		
		try {
			Usuario usuarioCambiarClave = this.usuarioDao.findById(this.usuarioInformacionDao.findById(cambiarContrasenaAux.getIdUsuario()).get().getIdUsuario().getId()).get();
			usuarioCambiarClave.setPassword(cifradoContrasena.encode(cambiarContrasenaAux.getContrasenaNueva()));
			
			this.usuarioDao.save(usuarioCambiarClave);
			
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "La contraseña fue actualizada con exito");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} 
		catch (Exception e) {
			logger.error("Error cambiando la contraseña del usuario");
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error cambiando la contraseña del usuario");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return respuesta;
	}
	
	
	/*
	 * Método para obtener la información relacionada a un usuario en el sistema que se encuentra autenticado y realiza esta petición
	 */
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerInformacionUsuarioAutenticado() {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = usuarioInformacionDao.findByIdUsuario(this.usuarioDao.findById(usuarioAutenticado.getId()).get());
		
		try {
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "La contraseña fue actualizada con exito");
			respuestaDto.setObjetoRespuesta(usuarioInformacion);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} 
		catch (Exception e) {
			logger.error("Error obteniendo informacion del usuario ", e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error obteniendo informacion del usuario");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return respuesta;
	}
	
	
	/* Metodo para cambiar la contraseña del usuario autenticado en el sistema */
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('SOCIO') or hasAuthority ('VALIDADOR') or hasAuthority('VALORADOR_RESO') or hasAuthority('GESTION_DOCUMENTAL')")
	public ResponseEntity<Object> cambiarContrasenaUsuarioAutenticado(@Valid @RequestBody CambioContrasenaReq cambiarContrasenaAux, HttpServletRequest request, HttpServletResponse response) {
		
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		
		try {
			Usuario usuarioCambiarClave = this.usuarioDao.findById(this.usuarioInformacionDao.findById(cambiarContrasenaAux.getIdUsuario()).get().getIdUsuario().getId()).get();
			
			if (cifradoContrasena.matches(cambiarContrasenaAux.getContrasenaAntigua(), usuarioCambiarClave.getPassword())) {
				usuarioCambiarClave.setPassword(cifradoContrasena.encode(cambiarContrasenaAux.getContrasenaNueva()));
				this.usuarioDao.save(usuarioCambiarClave);
				
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "La contraseña fue actualizada con exito");
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
			} 
			else {
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, "Error cambiando la contraseña, la anterior que ingreso no coincide con la que se encuentra almacenada en nuestro sistema");
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.BAD_REQUEST);
			}
		} 
		catch (Exception e) {
			logger.error("Error cambiando la contraseña del usuario");
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error cambiando la contraseña del usuario");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return respuesta;
	}
	

	/* Metodo que verifica si exista un correo ya en el sistema */
	public boolean existePorEmail(String email) {
		
		if (this.usuarioInformacionDao.buscarPorCorreo(email) != null) {
			return true;
		} 
		else {
			return false;
		}
	}

	/* Metodo que recibe un nombreUsuario y responde con un boolean si el nombre de usuario ya esta almacenado en la BD */
	public boolean existePorNombreUsuario(String nombreUsuario) {
		
		if (this.usuarioDao.buscarPorNombreUsuario(nombreUsuario) != null) {
			return true;
		} 
		else {
			return false;
		}
	}
	
	// Adicionado para retornar instancia usuario
	public Usuario getNewUser() {
		return new Usuario();
	}

	// Adicionado para retornar instancia infoUser
	public InformacionUsuario getNewInfoUser() {
		return new InformacionUsuario();
	}

	public UserDetailResponse getNewRestUserDetail() {
		return new UserDetailResponse();
	}
	
	// listar
				@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
				public ResponseEntity<Object> listarInformacionUsuario() {

					ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

					try {

						List<InformacionUsuario> organizacion = this.usuarioInformacionDao.listarInformacionUsuario();

						RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de organización exitoso");
						respuestaDto.setObjetoRespuesta(organizacion);
						respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
					} catch (Exception e) {
						logger.error("Error obteniendo organizaciones " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
						RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error obteniendo organizaciones");
						respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
					}
					return respuesta;
				}
}
