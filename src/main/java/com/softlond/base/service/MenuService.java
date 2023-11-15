package com.softlond.base.service;

import java.util.Date;

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
import com.softlond.base.entity.Menu;
import com.softlond.base.entity.Usuario;
import com.softlond.base.repository.MenuDao;
import com.softlond.base.repository.UsuarioDao;
import com.softlond.base.repository.UsuarioInformacionDao;

@Service
public class MenuService {

	private static final Logger logger = Logger.getLogger(MenuService.class);

	@Autowired
	MenuDao menuDao;

	@Autowired
	UsuarioInformacionDao usuarioInformacionDao;

	@Autowired
	UsuarioDao usuarioDao;


	
	/* Metodo para crear menus en el sistema */
	@PreAuthorize("hasAuthority('SUPER')")
	public ResponseEntity<Object> crearMenu(Menu nuevoMenu) {
		
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		
		try {
			if (!this.existePorNombre(nuevoMenu.getNombre())) {
				
				nuevoMenu.setIdCreador(usuarioAutenticado);
				nuevoMenu.setFechaCreacion(new Date());
				this.menuDao.save(nuevoMenu);
				
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito");
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
			}
			else {
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, "Error nombre de menú ya existe");
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.BAD_REQUEST);
			}
		} 
		catch (Exception e) {
			logger.error("Error en la creacion del menu");
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error en la creacion del menu");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return respuesta;
	}

	/* VER TODOS LOS MENUS */
	/* Metodo para ver todos los menus en el sistema */
	@PreAuthorize("hasAuthority('SUPER')")
	public ResponseEntity<Object> obtenerTodos() {
		
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
				
		try {
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito");
			respuestaDto.setObjetoRespuesta(this.menuDao.findAllByOrderByFechaCreacionDesc());
			
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} 
		catch (Exception e) {
			logger.error("Error se presentan problemas al cargar los menus");
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error se presentan problemas al cargar los menus");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return respuesta;
	}
	

	/* Metodo para elminar los menus en el sistema */
	@PreAuthorize("hasAuthority('SUPER')")
	public @ResponseBody ResponseEntity<Object> borrarMenu(Integer id) {
		
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
			
		try {
			Menu auxMenu = this.menuDao.findById(id).get();
			this.menuDao.delete(auxMenu);
			
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} 
		catch (Exception e) {
			Throwable t = e.getCause();
			
			if (t instanceof ConstraintViolationException) {
				logger.error("El menu se encuentra asociado en el sistema" + e.getCause());
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, "Error, el menu se encuentra asociado en el sistema");
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.BAD_REQUEST);
			} 
			else {
				logger.error("Error en el borrado del Menú");
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error en el borrado del Menú");
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		return respuesta;
	}


	/* Metodo para actualizar un menú en el sistema */
	@PreAuthorize("hasAuthority('SUPER')")
	public ResponseEntity<Object> actualizarMenu(@RequestBody Menu menuNuevo) {
		
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		
		Authentication autentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autentication.getPrincipal();
		
		try {
			
			Menu menuActualizar = menuDao.findById(menuNuevo.getId()).get();
			
			if (!menuActualizar.getNombre().equals(menuNuevo.getNombre())) {
				
				if (this.existePorNombre(menuNuevo.getNombre())) {
					RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, "Error creando el menu, ya existe uno con el mismo nombre");
					respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.BAD_REQUEST);
				} 
				else {
					menuNuevo.setFechaActualizacion(new Date());
					menuNuevo.setIdUltimoUsuarioModifico(usuarioAutenticado);
					this.menuDao.save(menuNuevo);
					
					RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito");
					respuestaDto.setObjetoRespuesta(menuNuevo);
					respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
				}
			}
			else {
				menuNuevo.setFechaActualizacion(new Date());
				menuNuevo.setIdUltimoUsuarioModifico(usuarioAutenticado);
				menuDao.save(menuNuevo);
				
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito");
				respuestaDto.setObjetoRespuesta(menuNuevo);
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
			}
		} 
		catch (Exception e) {
			logger.error("Error en el actualizado del Menu");
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error en el actualizado del Menu");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return respuesta;
	}

	
	// Adicionado para retornar instancia Menu
	public Menu getNewMenu() {
		return new Menu();
	}

	// Metodo que recibe un nombre para el menu y responde con un boolean si el menu ya esta almacenado en la BD
	public boolean existePorNombre(String menuName) {
		
		boolean existe = false;
		
		if (menuDao.findByNombre(menuName) != null) {
			existe = true;
		}
		
		return existe;
	}

}
