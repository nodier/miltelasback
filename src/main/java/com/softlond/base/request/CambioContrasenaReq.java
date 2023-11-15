package com.softlond.base.request;

import lombok.Data;

@Data
public class CambioContrasenaReq {

	private Integer idUsuario;
	private String contrasenaAntigua;
	private String contrasenaNueva;
	

	public CambioContrasenaReq() {
		super();
	}

	public CambioContrasenaReq(Integer idUsuario, String contrasenaAntigua, String contrasenaNueva) {
		super();
		this.idUsuario = idUsuario;
		this.contrasenaAntigua = contrasenaAntigua;
		this.contrasenaNueva = contrasenaNueva;
	}
}
