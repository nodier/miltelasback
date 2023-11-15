package com.softlond.base.request;

import com.softlond.base.entity.InformacionUsuario;
import com.softlond.base.entity.Usuario;

import lombok.Data;

@Data
public class UsuarioAuxReq {

	private Usuario usuario;
	private InformacionUsuario usuarioInformacion;
	
	public UsuarioAuxReq() {
		super();
	}

	public UsuarioAuxReq(Usuario usuario, InformacionUsuario usuarioInformacion) {
		super();
		this.usuario = usuario;
		this.usuarioInformacion = usuarioInformacion;
	}
}