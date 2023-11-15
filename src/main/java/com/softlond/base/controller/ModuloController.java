package com.softlond.base.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.softlond.base.constant.ApiConstant;
import com.softlond.base.entity.MenusPorModulo;
import com.softlond.base.entity.Modulo;
import com.softlond.base.service.ModuloService;

@RestController
@RequestMapping(ApiConstant.MODULO_CONTROLADOR_API)
public class ModuloController {

	
	@Autowired
	public ModuloService moduloService;


	/* Listar todos los modulos siendo un Super user */
	@GetMapping(value = ApiConstant.MODULO_CONTROLADOR_API_SUPER_LISTAR_TODOS, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerTodos() {
		return moduloService.obtenerTodos();
	}
	
	/* Borrar Modulo */
	@DeleteMapping(value = ApiConstant.MODULO_CONTROLADOR_API_BORRAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> borrarModulo(Integer idModulo) {
		return moduloService.borrarModulo(idModulo);
	}
	
	/* SE RECIBE UN MODULO PARA LA CREACION DEL MISMO */
	@PostMapping(value = ApiConstant.MODULO_CONTROLADOR_API_CREAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> crearModulo(@Valid @RequestBody Modulo nuevoModulo) {
		return moduloService.crearModulo(nuevoModulo);
	}
	
	// ACTUALIZAR MODULOS
	@PostMapping(value = ApiConstant.MODULO_CONTROLADOR_API_ACTUALIZAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> actualizarModulo(@Valid @RequestBody Modulo nuevoModulo) {
		return moduloService.actualizarModulo(nuevoModulo);
	}
	
	/* LISTAR MIS MODULOS SIENDO UN ADMINISTRADOR
	@GetMapping(value = ApiConstant.MODULE_CONTROLLER_API_ADMIN_LIST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> listarDesdeAdmin() {
		return moduloService.listarDesdeAdmin();
	} */

	// Listar menus de un modulo
	@GetMapping(value = ApiConstant.MODULO_CONTROLADOR_API_LISTAR_MENUS_POR_MODULO, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> listasMenusPorModulo(Integer idModulo) {
		return moduloService.listasMenusPorModulo(idModulo);
	}

	/* METODO PARA EL EMPAREJADO DE UN MODULO Y LOS MENU */
	@PostMapping(value = ApiConstant.MODULO_CONTROLADOR_API_ENLAZAR_MENU, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> enlazarMenu(@Valid @RequestBody MenusPorModulo moduloMenu) {
		return moduloService.enlazarMenu(moduloMenu);
	}

	/* METODO PARA EL DESEMPAREJADO DE UN MODULO Y LOS MENU */
	@PostMapping(value = ApiConstant.MODULO_CONTROLADOR_API_DESEMPAREJAR_MENU, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> desemparejarMenu(@Valid @RequestBody MenusPorModulo menuDesemparejar) {
		return moduloService.desemparejarMenu(menuDesemparejar);
	}

	// Obtiene el listado de menús por modulo para cargar el menú del usuario en Sesión del front
	@RequestMapping(value = ApiConstant.MODULO_CONTROLADOR_API_OBTENER_MODULOS_MENU_JSON, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> jsonModuloMenu() {
		return moduloService.jsonModuloMenu();
	}
	
	// Actualiza la posición index de un menú en un módulo
	@PostMapping(value = ApiConstant.MODULO_CONTROLAR_API_GUARDAR_POSICION, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> guardarPosicion(@RequestBody MenusPorModulo menusModulos) {
		return moduloService.guardarPosicion(menusModulos);
	}

}
