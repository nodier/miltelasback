package com.softlond.base.service;

import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.web.bind.annotation.ResponseBody;

import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.InformacionUsuario;
import com.softlond.base.entity.Menu;
import com.softlond.base.entity.MenusPorModulo;
import com.softlond.base.entity.Modulo;
import com.softlond.base.entity.Usuario;
import com.softlond.base.repository.MenuDao;
import com.softlond.base.repository.MenusPorModuloDao;
import com.softlond.base.repository.ModuloDao;
import com.softlond.base.repository.ModulosPorPerfilDao;
import com.softlond.base.repository.UsuarioDao;
import com.softlond.base.repository.UsuarioInformacionDao;
import com.softlond.base.response.JsonModuleMenuModel;

@Service
public class ModuloService {

	private static final Logger logger = Logger.getLogger(MenuService.class);

	@Autowired
	public ModuloDao moduloDao;

	@Autowired
	public UsuarioDao usuarioDao;

	@Autowired
	public UsuarioInformacionDao usuarioInformacionDao;

	@Autowired
	private MenusPorModuloDao moduloMenuDao;

	@Autowired
	private MenuDao menuDao;

	@Autowired
	private ModulosPorPerfilDao moduloPerfilDao;

	
	
	
	// Listar todos los modulos siendo un Super user//
	@PreAuthorize("hasAuthority('SUPER')")
	public @ResponseBody ResponseEntity<Object> obtenerTodos() {
		
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		
		try {
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito");
			respuestaDto.setObjetoRespuesta(this.moduloDao.findAllByOrderByFechaCreacionDesc());
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} 
		catch (Exception e) {
			logger.error("Error al lista módulos desde super");
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error al lista módulos desde super");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return respuesta;
	}
	
	
	/* Borrar Modulos */
	@PreAuthorize("hasAuthority('SUPER')")
	public @ResponseBody ResponseEntity<Object> borrarModulo(Integer idModulo) {
		
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		
		try {
			this.moduloDao.delete(moduloDao.findById(idModulo).get());
			
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} 
		catch (Exception e) {
			Throwable t = e.getCause();
			if (t instanceof ConstraintViolationException) {
				logger.error("El modulo se encuentra asociado en el sistema " + e.getCause());
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, "Error al elminar módulo, se encuentra asociado en el sistema");
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.BAD_REQUEST);
			} 
			else {
				logger.error("Error en el borrado del modulo");
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error al elminar módulo");
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		return respuesta;
	}
	
	
	/* SE RECIBE UN MODULO PARA LA CREACION DEL MISMO */
	@PreAuthorize("hasAuthority('SUPER')")
	public @ResponseBody ResponseEntity<Object> crearModulo(Modulo nuevoModulo) {
		
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		
		try {
			if (!this.existePorNombre(nuevoModulo.getNombre())) {
				nuevoModulo.setIdCreador(usuarioAutenticado);
				nuevoModulo.setFechaCreacion(new Date());
				this.moduloDao.save(nuevoModulo);
				
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito");
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
			}
			else {
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, "Error nombre del módulo ya existe");
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.BAD_REQUEST);
			}
		} 
		catch (Exception e) {
			logger.error("Error en la creacion del módulo");
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error en la creacion del módulo");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return respuesta;
	}
	
	
	// Actualizar modulo
	@PreAuthorize("hasAuthority('SUPER')")
	public @ResponseBody ResponseEntity<Object> actualizarModulo(@RequestBody Modulo nuevoModulo) {
		
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		
		try {
			Modulo modActualizar = moduloDao.findById(nuevoModulo.getId()).get();
			
			if (!modActualizar.getNombre().equals(nuevoModulo.getNombre())) {
				
				if (this.existePorNombre(nuevoModulo.getNombre())) {	
					RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, "Error, el nombre del módulo ya existe");
					respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.BAD_REQUEST);			
				} 
				else {
					this.moduloDao.save(nuevoModulo);
					
					RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito");
					respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
				}
			}
			else {
				this.moduloDao.save(nuevoModulo);
				
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito");
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
			}
		} 
		catch (Exception e) {
			logger.error("Error al actualizar módulo");
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error al actualizar módulo");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return respuesta;
	}
	
	
	/* LISTAR MIS MODULOS SIENDO UN ADMINISTRADOR
	@PreAuthorize("hasAuthority('ADMIN')")
	public @ResponseBody ResponseEntity<Object> listarDesdeAdmin() {
		
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		
		try {
			ArrayList<Modulo> modulos = this.moduloDao.findMyModulesAdminOrderByCreatedDateDesc(usuarioAutenticado.getId());
			
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito");
			respuestaDto.setObjetoRespuesta(modulos);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} 
		catch (Exception e) {
			logger.error("Error al lista módulos desde administrador");
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error al lista módulos desde administrador");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return respuesta;
	} */
	

	// Listar menus de un modulo
	@PreAuthorize("hasAuthority('SUPER')")
	public @ResponseBody ResponseEntity<Object> listasMenusPorModulo(Integer idModulo) {
		
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		
		try {
			List<Integer> index = menuDao.obtenerMenuPorModulo(idModulo);
			ArrayList<Menu> menuList = new ArrayList<>();
			
			for (Integer i : index) {
				menuList.add(menuDao.findById(i).get());
			}
			
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito");
			respuestaDto.setObjetoRespuesta(menuList);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		}
		catch (Exception e) {
			logger.error("Error al listar menús de un módulo");
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error al listar menús de un módulo");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return respuesta;
	}
	
	/* METODO PARA EL EMPAREJADO DE UN MODULO Y LOS MENU */
	@PreAuthorize("hasAuthority('SUPER')")
	public @ResponseBody ResponseEntity<Object> enlazarMenu(MenusPorModulo moduleMenu) {
		
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		
		if (!existeRelacion(moduleMenu.getIdModulo().getId(), moduleMenu.getIdMenu().getId())) {
			try {
				this.moduloMenuDao.save(moduleMenu);
				
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito");
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
			} 
			catch (Exception e) {
				logger.error("Error al emparejar menú a módulo");
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error al emparejar menú a módulo");
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		else {
			logger.error("Error en el emparejamiento del modulo y el menu");
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, "Error en el emparejamiento del modulo y el menu");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.BAD_REQUEST);
		}
		
		return respuesta;
	}


	/* METODO PARA EL DESEMPAREJADO DE UN MODULO Y LOS MENU */
	@PreAuthorize("hasAuthority('SUPER')")
	public @ResponseBody ResponseEntity<Object> desemparejarMenu(MenusPorModulo menuDesemparejar) {
		
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		
		try {
			MenusPorModulo auxMenu = getNewModuleMenu();
			auxMenu = this.moduloMenuDao.findByIdMenuAndIdModulo(this.menuDao.findById(menuDesemparejar.getIdMenu().getId()).get(),
			this.moduloDao.findById(menuDesemparejar.getIdModulo().getId()).get());
			
			this.moduloMenuDao.delete(auxMenu);
			
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} 
		catch (Exception e) {
			logger.error("Error en el desenlaze del menu con el modulo");
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error en el desenlaze del menu con el modulo");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return respuesta;
	}

	
	// Obtiene el listado de menús por modulo para cargar el menú del usuario en sesión del front
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')" )
	public @ResponseBody ResponseEntity<Object> jsonModuloMenu() {
		
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioAux = this.usuarioInformacionDao.findByIdUsuario(this.usuarioDao.findById(usuarioAutenticado.getId()).get());
		JsonModuleMenuModel auxJson = new JsonModuleMenuModel();
		
		try {
			
			ArrayList<JsonModuleMenuModel> listJson = new ArrayList<JsonModuleMenuModel>();
			ArrayList<Modulo> idsModules = this.moduloDao.buscarModulosUsuario(usuarioAux.getIdUsuario().getId());

			List<Integer> indexModules = moduloPerfilDao.obtenerOrdenadosIndice(usuarioAux.getIdUsuario().getId());

			for (Integer modules : indexModules) {
				
				if (idsModules.contains(this.moduloDao.findById(modules).get())) {
					auxJson = new JsonModuleMenuModel();
					List<Integer> indexMenus = menuDao.obtenerMenuPorModulo(modules);
					ArrayList<Menu> menuModules = new ArrayList<>();
					
					for (Integer i : indexMenus) {
						menuModules.add(menuDao.findById(i).get());
					}
					
					auxJson.setModule(moduloDao.findById(modules).get());
					auxJson.setListMenu(menuModules);
					listJson.add(auxJson);
				}
			}
			
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito");
			respuestaDto.setObjetoRespuesta(listJson);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} 
		catch (Exception e) {
			logger.error("Error al cargar menú commpleto del usuario " + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error al cargar menú commpleto del usuario");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return respuesta;
	}

	/* Metodo que se encarga de guardar las posiciones de menus en el modulo de un usuario
	 * 
	 */
	@PreAuthorize("hasAuthority('SUPER')")
	public @ResponseBody ResponseEntity<Object> guardarPosicion(@RequestBody MenusPorModulo menusModulos) {
		
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		
		try {
			Integer menuModulo = moduloMenuDao.menuPorModuloExiste(menusModulos.getIdModulo().getId(), menusModulos.getIdMenu().getId());
			MenusPorModulo aux = moduloMenuDao.findById(menuModulo).get();
			aux.setIndice(menusModulos.getIndice());
			
			this.moduloMenuDao.save(aux);
			
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito");
			respuestaDto.setObjetoRespuesta(aux);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} 
		catch (Exception e) {
			logger.error("Error al actualizar las posiciones de menú en módulo");
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error al actualizar las posiciones de menú en módulo");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return respuesta;
	}

	/* Verifica si ya existe una relacion menu modulo */ /* YAAAAAAAAAA */
	public boolean existeRelacion(Integer idModulo, Integer idMenu) {
		
		boolean existe = false;
		
		if (this.moduloMenuDao.menuPorModuloExiste(idModulo, idMenu) != null) {
			existe = true;
		}
		
		return existe;
	}

	// metodo que recibe un nombre para el modulo y responde con un boolean si el modulo ya esta almacenado en la BD
	public boolean existePorNombre(String nombreModulo) {
		
		boolean existe = false;
		
		if (this.moduloDao.findByNombre(nombreModulo) != null) {
			existe = true;
		} 
		
		return existe;
	}

	// retornar instancia modelo
	public MenusPorModulo getNewModuleMenu() {
		return new MenusPorModulo();
	}

	// retornar instancia modelo
	public Modulo getNewModule() {
		return new Modulo();
	}

}
