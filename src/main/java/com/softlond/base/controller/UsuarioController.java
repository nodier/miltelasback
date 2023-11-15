package com.softlond.base.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

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
import com.softlond.base.entity.InformacionUsuario;
import com.softlond.base.request.CambioContrasenaReq;
import com.softlond.base.request.UsuarioAuxReq;
import com.softlond.base.service.UsuarioService;

@RestController
@RequestMapping(ApiConstant.USUARIO_CONTROLADOR_API)
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;

	
	/* Listar todos los usuarios siendo el SUPER_USER */
	@GetMapping(value = ApiConstant.USUARIO_CONTROLADOR_API_SUPER_LISTADO, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> listarTodos() {
		return usuarioService.listarTodos();
	}
	
	/* Metodo para crear usuarios en el sistema siendo un super*/
	@PostMapping(value = ApiConstant.USUARIO_CONTROLADOR_API_SUPER_CREAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> crearUsuario(@Valid @RequestBody UsuarioAuxReq nuevoUsuarioAux, HttpServletRequest request,
			HttpServletResponse response) {
		return this.usuarioService.crearUsuario(nuevoUsuarioAux, request, response);
	}
	
	/* Metodo para eliminar usuarios por correo en el sistema */
	@DeleteMapping(value = ApiConstant.USUARIO_CONTROLADOR_API_BORRAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> borrarUsuario(@RequestParam String nombreUsuario) {
		return usuarioService.borrarUsuario(nombreUsuario);
	}
	
	/* Metodo para actualizar usuarios en el sistema */
	@PostMapping(value = ApiConstant.USUARIO_CONTROLADOR_API_ACTUALIZAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> actualizarUsuario(@Valid @RequestBody InformacionUsuario usuario, HttpServletRequest request,
			HttpServletResponse response) {
		return usuarioService.actualizarUsuario(usuario, request, response);
	}

	/* Metodo para cambiar la contraseña de un usuario del sistema DESDE SUPER */
	@PostMapping(value = ApiConstant.USUARIO_CONTROLADOR_API_CAMBIAR_CONTRASENA, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> cambiarContrasena(@Valid @RequestBody CambioContrasenaReq cambiarContrasenaAux, HttpServletRequest request,
			HttpServletResponse response) {
		return this.usuarioService.cambiarContrasena(cambiarContrasenaAux, request, response);
	}
	
	/* Método para obtener la información relacionada a un usuario en el sistema que se encuentra autenticado y realiza esta petición */
	@GetMapping(value = ApiConstant.USUARIO_CONTOLADOR_API_OBTENER_INFO_USUARIO, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerInformacionUsuarioAutenticado() {
		return usuarioService.obtenerInformacionUsuarioAutenticado();
	}
	
	/* Metodo para cambiar la contraseña de un usuario del sistema*/
	@PostMapping(value = ApiConstant.USUARIO_CONTROLADOR_API_CAMBIAR_CONTRASENA_LOGUEADO, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> cambiarContrasenaUsuarioAutenticado(@Valid @RequestBody CambioContrasenaReq cambiarContrasenaAux, HttpServletRequest request,
			HttpServletResponse response) {
		return this.usuarioService.cambiarContrasenaUsuarioAutenticado(cambiarContrasenaAux, request, response);
	}
	
    /* Listar toda la informacion del usuario */
	@GetMapping(value = ApiConstant.USUARIO_CONTOLADOR_API_LISTAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> listarInformacionUsuario() {
		return usuarioService.listarInformacionUsuario();
	}
}