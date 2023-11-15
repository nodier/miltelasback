package com.softlond.base.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.softlond.base.constant.ApiConstant;
import com.softlond.base.entity.ModulosPorPerfil;
import com.softlond.base.entity.Perfil;
import com.softlond.base.service.PerfilService;

@Controller
@RequestMapping(ApiConstant.PERFIL_CONTROLADOR_API)
public class PerfilController {


	@Autowired
	private PerfilService perfilService;
	
	
	/* SE RECIBE UN PERFIL PARA LA CREACION DEL MISMO */
	@PostMapping(value = ApiConstant.PERFIL_CONTROLADOR_API_CREAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> crearPerfil(@RequestBody Perfil nuevoPerfil, HttpServletRequest request, HttpServletResponse response) {
		return this.perfilService.crearPerfil(nuevoPerfil);
	}

	/* MUESTRA TODOS LOS PERFILES DEL SISTEMA */
	@GetMapping(value = ApiConstant.PERFIL_CONTROLADOR_API_VER_TODOS, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerTodos() {
		return this.perfilService.obtenerTodos();
	}
	
	/* SE RECIBE UN PERFIL PARA LA EDICIÓN DEL MISMO */
	@PostMapping(value = ApiConstant.PERFIL_CONTROLADOR_API_ACTUALIZAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> actualizarPerfil(@RequestBody Perfil nuevoPerfil, HttpServletRequest request, HttpServletResponse response) {
		return this.perfilService.actualizarPerfil(nuevoPerfil, request, response);
	}
	
	/* ELIMINA UN PERFIL POR ID */
	@DeleteMapping(value = ApiConstant.PERFIL_CONTROLADOR_API_BORRAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> borrarPerfil(Integer idPerfil, HttpServletRequest request, HttpServletResponse response) {
		return this.perfilService.borrarPerfil(idPerfil);
	}
	
	/* SE RECIBE UN ID DE PERFIL PARA OBTENER LOS MODULOS DEL MISMO */
	@GetMapping(value = ApiConstant.PERFIL_CONTROLADOR_API_LISTAR_MODULOS_POR_PERFIL, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> listarModulosPorPerfil(Long idPerfil) {
		return this.perfilService.listarModulosPorPerfil(idPerfil);
	}
	
	/* Eliminar emparejado de un modulo a un perfil */
	@PostMapping(value = ApiConstant.PERFIL_CONTROLADOR_API_DESEMPAREJAR_PERFIL_MODULO,  produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> desemparejarModuloPerfil(@RequestBody ModulosPorPerfil moduloPerfil, HttpServletRequest request, HttpServletResponse response) {
		return this.perfilService.desemparejarModuloPerfil(moduloPerfil, request, response);
	}
	
	/* Empareja un modulo a un perfil */
	@PostMapping(value = ApiConstant.PERFIL_CONTROLADOR_API_EMPAREJAR_PERFIL_MODULO, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> emparejarModuloPerfil(@RequestBody ModulosPorPerfil moduloPerfil,
			HttpServletRequest request, HttpServletResponse response) {
		return this.perfilService.emparejarModuloPerfil(moduloPerfil, request, response);
	}
	
	/* ACTUALIZA ORDENAMIENTO DE LA RELACIÓN DE MODULOS CON PERFILES */
	@PostMapping(value = ApiConstant.PERFIL_CONTROLADOR_API_ACTUALIZAR_POSICIONES, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> guardarPosicion(@RequestBody ModulosPorPerfil moduloPerfil,
			HttpServletRequest request, HttpServletResponse response) {
		return this.perfilService.guardarPosicion(moduloPerfil, request, response);
	}
	
	
	/* MUESTRA TODOS LOS PERFILES DEL SISTEMA POR COMPAÑIA 
	@GetMapping(value = ApiConstant.PROFILE_CONTROLLER_API_LIST_PROFILES_BY_COMPANY, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GeneralRest> takeAllMyProfilesLikeAdmin(HttpServletRequest request, HttpServletResponse response) {
		return this.perfilService.takeAllMyProfilesByCompany(request, response);
	} */
}