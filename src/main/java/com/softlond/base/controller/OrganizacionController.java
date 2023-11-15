package com.softlond.base.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.softlond.base.constant.ApiConstant;
import com.softlond.base.entity.Organizacion;
import com.softlond.base.service.OrganizacionService;

@RestController
@RequestMapping(ApiConstant.ORGANIZACION_CONTROLADOR_API)
public class OrganizacionController {

	@Autowired
	public OrganizacionService organizacionService;
	

	// Listar todos las organizaciones 
	@GetMapping(value = ApiConstant.ORGANIZACION_CONTROLADOR_API_SUPER_LIST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerTodas() {
		return organizacionService.obtenerTodas();
	}
	
	// Crear compañia en el sistema siendo
	@PostMapping(value = ApiConstant.ORGANIZACION_CONTROLADOR_API_SUPER_CREATE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> crerOrganizacion(@RequestBody Organizacion organizacionNueva) {
		return organizacionService.crerOrganizacion(organizacionNueva);
	}
	
	// Actualizar compañia en el sistema
	@PostMapping(value = ApiConstant.ORGANIZACION_CONTROLADOR_API_ACTUALIZAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> actualizarOrganizacion(@RequestBody Organizacion organizacionActualizada) {
		return organizacionService.actualizarOrganizacion(organizacionActualizada);
	}
	
	// Borrar orgaizacion en el sistema
	@DeleteMapping(value = ApiConstant.ORGANIZACION_CONTROLADOR_API_SUPER_BORRAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> borrarOrganizacion(@RequestParam Integer idOrganizacion) {
		return organizacionService.borrarOrganizacion(idOrganizacion);
	}
	
	// Ver la compañia a la que pertenezco
	@GetMapping(value = ApiConstant.ORGANIZACION_CONTROLADOR_API_VER_MI_ORGANIZACION, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> verMiOrganizacion() {
		return organizacionService.verMiOrganizacion();
	}
}
