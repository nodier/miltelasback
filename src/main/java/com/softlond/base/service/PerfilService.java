package com.softlond.base.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.springframework.web.bind.annotation.ResponseBody;

import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.Modulo;
import com.softlond.base.entity.ModulosPorPerfil;
import com.softlond.base.entity.Perfil;
import com.softlond.base.entity.Usuario;
import com.softlond.base.repository.ModuloDao;
import com.softlond.base.repository.ModulosPorPerfilDao;
import com.softlond.base.repository.PerfilDao;
import com.softlond.base.repository.UsuarioDao;
import com.softlond.base.repository.UsuarioInformacionDao;

@Service
public class PerfilService {
	
	private static final Logger logger = Logger.getLogger(PerfilService.class);

	@Autowired
	public PerfilDao perfilDao;

	@Autowired
	public UsuarioInformacionDao usuarioInformacionDao;

	@Autowired
	UsuarioDao usuarioDao;

	@Autowired
	public ModulosPorPerfilDao modulosPorPerfilDao;

	@Autowired
	public ModuloDao moduloDao;

	
	
	/* SE RECIBE UN PERFIL PARA LA CREACION DEL MISMO */
	@PreAuthorize("hasAuthority('SUPER')")
	public ResponseEntity<Object> crearPerfil(Perfil newProfile) {
		
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		
		Authentication autentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = (Usuario) autentication.getPrincipal();
		
		try {
			if (!existePorNombre(newProfile.getNombre())) {
				
				newProfile.setFechaCreacion(new Date());
				newProfile.setFechaActualizacion(new Date());
				newProfile.setIdCreador(usuario);
				newProfile.setIdUltimoUsuarioModifico(usuario);
				this.perfilDao.save(newProfile);
				
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito listando los perfiles");
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
			} 
			else {
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, "Error, ya existe un perfil con ese nombre");
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.BAD_REQUEST);
			}
		} 
		catch (Exception e) {
			logger.error("Error en la creacion del perfil " + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error en la creacion del perfil");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return respuesta;
	}


	/* MUESTRA TODOS LOS PERFILES DEL SISTEMA */
	@PreAuthorize("hasAuthority('SUPER')")
	public ResponseEntity<Object> obtenerTodos() {
		
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		
		try {
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito listando los perfiles");
			respuestaDto.setObjetoRespuesta(this.perfilDao.findAllByOrderByFechaCreacionDesc());
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} 
		catch (Exception e) {
			logger.error("Error en la visualización de los perfiles");
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error en la visualización de los perfiles");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return respuesta;
	}

	
	/* SE RECIBE UN PERFIL PARA LA EDICIÓN DEL MISMO */
	@PreAuthorize("hasAuthority('SUPER')")
	public @ResponseBody ResponseEntity<Object> actualizarPerfil(@RequestBody Perfil perfilNuevo, HttpServletRequest request, HttpServletResponse response) {
		
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		
		try {
			Perfil perfilBd = this.perfilDao.findById(perfilNuevo.getId()).get();
			
			if (!perfilBd.getNombre().equals(perfilNuevo.getNombre())) {
				
				if (existePorNombre(perfilNuevo.getNombre())) {
					RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, "Error, ya existe un perfil con ese nombre");
					respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.BAD_REQUEST);
				} 
				else {
					perfilNuevo.setFechaActualizacion(new Date());
					perfilNuevo.setIdUltimoUsuarioModifico(usuarioAutenticado);
					perfilDao.save(perfilNuevo);
					
					RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito");
					respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
				}
			}
			else {
				perfilNuevo.setFechaActualizacion(new Date());
				perfilNuevo.setIdUltimoUsuarioModifico(usuarioAutenticado);
				perfilDao.save(perfilNuevo);
				
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito");
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
			}
		} 
		catch (Exception e) {
			logger.error("Error al actualizar el perfil" + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error al actualizar el perfil");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return respuesta;
	}
	
	
	/* ELIMINA UN PERFIL POR ID */
	@PreAuthorize("hasAuthority('SUPER')")
	public @ResponseBody ResponseEntity<Object> borrarPerfil(Integer id) {
		
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		
		try {			
			Perfil auxProfile = this.perfilDao.findById(id).get();
			this.perfilDao.delete(auxProfile);
			
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} 
		catch (Exception e) {
			Throwable t = e.getCause();
			
			if (t instanceof ConstraintViolationException) {
				logger.error("El perfil se encuentra asociado en el sistema");
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, "El perfil se encuentra asociado en el sistema");
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.BAD_REQUEST);
			} 
			else {
				logger.error("Error en la eliminación de perfiles");
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "El perfil se encuentra asociado en el sistema");
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		return respuesta;
	}
	
	
	/* SE RECIBE UN ID DE PERFIL PARA OBTENER LOS MODULOS DEL MISMO */
	@PreAuthorize("hasAuthority('SUPER')")
	public ResponseEntity<Object> listarModulosPorPerfil(Long idPerfil) {
		
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		
		try {
			List<Integer> indiceModulos = moduloDao.findModuleByProfileOrder(idPerfil);
			ArrayList<Modulo> modulos = new ArrayList<Modulo>();
			
			for (Integer module : indiceModulos) {
				modulos.add(moduloDao.findById(module).get());
			}
			
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito");
			respuestaDto.setObjetoRespuesta(modulos);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} 
		catch (Exception e) {
			logger.error("Error se presentan problemas al cargar los modulos por perfil" + e.getMessage());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error se presentan problemas al cargar los modulos por perfil");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return respuesta;
	}

	
	/* Eliminar emparejado de un modulo a un perfil */
	@PreAuthorize("hasAuthority('SUPER')")
	public @ResponseBody ResponseEntity<Object> desemparejarModuloPerfil(@RequestBody ModulosPorPerfil moduloPerfil, HttpServletRequest request, HttpServletResponse response) {
		
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		
		try {
			ModulosPorPerfil auxPerfilModulo = getNewProfileModules();
			auxPerfilModulo = this.modulosPorPerfilDao.buscarModuloDesenparejar(moduloPerfil.getIdPerfil().getId(), moduloPerfil.getIdModulo().getId());
			
			this.modulosPorPerfilDao.delete(auxPerfilModulo);
			
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} 
		catch (Exception e) {
			logger.error("Error en el desemparejado del modulo y el perfil", e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error en el desemparejado del modulo y el perfil");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return respuesta;
	}
	
	
	/* Empareja un modulo a un perfil */
	@PreAuthorize("hasAuthority('SUPER')")
	public @ResponseBody ResponseEntity<Object> emparejarModuloPerfil(@RequestBody ModulosPorPerfil moduloPerfil, HttpServletRequest request, HttpServletResponse response) {
		
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		
		if (!existeRelacion(moduloPerfil.getIdPerfil().getId(), moduloPerfil.getIdModulo().getId())) {
			try {
				moduloPerfil.setFechaCreacion(new Date());
				this.modulosPorPerfilDao.save(moduloPerfil);
				
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito");
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
			} 
			catch (Exception e) {
				logger.error("Error en la creacion del emparejamiento del perfil y el modulo", e);
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error en la creacion del emparejamiento del perfil y el modulo");
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} 
		else {
			logger.error("Error en la creacion del emparejamiento del perfil y el modulo");
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, "Error en la creacion del emparejamiento del perfil y el modulo");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.BAD_REQUEST);
		}
		
		return respuesta;
	}
	
	
	/* ACTUALIZA ORDENAMIENTO DE LA RELACIÓN DE MODULOS CON PERFILES */
	@PreAuthorize("hasAuthority('SUPER')")
	public @ResponseBody ResponseEntity<Object> guardarPosicion(@RequestBody ModulosPorPerfil profileModules, HttpServletRequest request, HttpServletResponse response) {
		
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		
		try {
			Integer moduloPerfilId = modulosPorPerfilDao.existeModuloEnPerfil(profileModules.getIdPerfil().getId(), profileModules.getIdModulo().getId());
			ModulosPorPerfil aux = modulosPorPerfilDao.findById(moduloPerfilId).get();
			aux.setIndice(profileModules.getIndice());
			
			this.modulosPorPerfilDao.save(aux);
			
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} 
		catch (Exception e) {
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error reordenando los modulos");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return respuesta;
	}
	
	
	
	/* VIEW ALL PROFILES BY COMPANY */
	/* MUESTRA TODOS LOS PERFILES DEL SISTEMA POR COMPAÑIA 
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER')")
	public ResponseEntity<GeneralRest> takeAllMyProfilesByCompany(HttpServletRequest request,
			HttpServletResponse response) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario customUser = (Usuario) authentication.getPrincipal();
		InformacionUsuario userAuthenticatedInfo = this.usuarioInformacionDao.findByIdUsuario(this.usuarioDao.findById(customUser.getId()).get());
		try {
			// GeneralRest generalRest = new GeneralRest(
			// this.profileDao.findProfileByAdminIdOrCompanyOrderByCreatedDateDesc(customUser.getId(),
			// userAuthenticatedInfo.getCompanyId().getId()),
			// "Exito", 200);
			GeneralRest generalRest = new GeneralRest(this.perfilDao.findAllByOrderByFechaCreacionDesc(), "Exito", 200);
			return new ResponseEntity<GeneralRest>(generalRest, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			this.logger.error("Error en la visualizacón de perfiles por compañia ");
			return new ResponseEntity<GeneralRest>(exceptionHandler.exceptionResponse(e, "PERFIL"),
					HttpStatus.BAD_REQUEST);
		}
	} */
	
	
	/* Verifica si existe una relación entre un perfil y un modulo */
	public boolean existeRelacion(Integer idPerfil, Integer idModule) {
		
		boolean existe = false;
		
		if (this.modulosPorPerfilDao.existeModuloEnPerfil(idPerfil, idModule) != null) {
			existe = true;
		}
		
		return existe;
	}

	// Adicionado para retornar instancia ModulesByProfile
	public ModulosPorPerfil getNewProfileModules() {
		return new ModulosPorPerfil();
	}

	// Adicionado para retornar instancia Profile
	public Perfil getNewProfile() {
		return new Perfil();
	}

	// metodo que recibe un nombre para perfil y responde con un boolean si el perfil ya esta almacenado en la BD
	public boolean existePorNombre(String nombrePerfil) {
		
		boolean existe = false;
		
		if (perfilDao.buscarPorNombre(nombrePerfil) != null) {
			existe = true;
		} 
		
		return existe;
	}

}
