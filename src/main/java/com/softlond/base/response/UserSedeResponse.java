package com.softlond.base.response;

import com.softlond.base.entity.InformacionUsuario;
import com.softlond.base.entity.Organizacion;

public class UserSedeResponse {

	private InformacionUsuario user;
	private Organizacion sede;
	
	public InformacionUsuario getUser() {
		return user;
	}

	public void setUser(InformacionUsuario user) {
		this.user = user;
	}

	public Organizacion getSede() {
		return sede;
	}

	public void setSede(Organizacion sede) {
		this.sede = sede;
	}
	
	public UserSedeResponse() {
		super();
	}
	
	public UserSedeResponse(InformacionUsuario user,Organizacion sede) {
		this.user = user;
		this.sede = sede;
	}
	
}
