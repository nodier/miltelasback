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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.softlond.base.constant.ApiConstant;
import com.softlond.base.entity.Menu;
import com.softlond.base.service.MenuService;

@RestController
@RequestMapping(ApiConstant.MENU_CONTROLADOR_API)
public class MenuController {

	@Autowired
	public MenuService menuService;

	
	/* Metodo para ver todos los menus en el sistema */
	@GetMapping(value = ApiConstant.MENU_CONTROLADOR_API_LISTAR_TODOS, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerTodos() {
		return menuService.obtenerTodos();
	}
	
	/* Metodo para crear menus en el sistema */
	@PostMapping(value = ApiConstant.MENU_CONTROLADOR_API_CREAR)
	public ResponseEntity<Object> crearMenu(@Valid @RequestBody Menu newMenu) {
		return menuService.crearMenu(newMenu);
	}

	/* Metodo para elminar los menus en el sistema */
	@DeleteMapping(value = ApiConstant.MENU_CONTROLADOR_API_BORRAR)
	public ResponseEntity<Object> borrarMenu(Integer id) {
		return menuService.borrarMenu(id);
	}

	/* Metodo para actualizar un menú en el sistema */
	@PostMapping(value = ApiConstant.MENU_CONTROLADOR_API_ACTUALIZAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> actualizarMenu(@RequestBody Menu nuevoMenu) {
		return menuService.actualizarMenu(nuevoMenu);
	}
}
