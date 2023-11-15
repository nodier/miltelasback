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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.softlond.base.constant.ApiConstant;
import com.softlond.base.request.UsuarioSedeRequest;
import com.softlond.base.service.UsuarioSedeService;

@RestController
@RequestMapping(ApiConstant.USUARIO_SEDE_CONTROL_API)
public class UsuarioSedeController {

	@Autowired
	private UsuarioSedeService usuarioSedeService;
	
	@PostMapping(value = ApiConstant.USUARIO_SEDE_CONTROL_API_ADD, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> AsignarUsuarioASede(@Valid @RequestBody UsuarioSedeRequest usuarioSede){
		return usuarioSedeService.AsignarUsuarioASede(usuarioSede);
	}
	
	@GetMapping(value = ApiConstant.USUARIO_SEDE_CONTROL_API_GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> obtenerUsuarioSede(){
		return usuarioSedeService.ObtenerUsuariosConSede();
	}

	@GetMapping(value = ApiConstant.USUARIO_SEDE_CONTROL_API_SEDE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> obtenerSedeUsuario(){
		return usuarioSedeService.ObtenerSedeUsuario();
	}
	
	
	
	
	@PostMapping(value = ApiConstant.USUARIO_SEDE_CONTROL_API_REMOVE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> eliminarUsuarioSede(@Valid @RequestBody UsuarioSedeRequest usuarioSede){
		return usuarioSedeService.deleteUsuarioSede(usuarioSede);
	}
}
