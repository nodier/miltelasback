package com.softlond.base.request;

import lombok.Data;

@Data
public class UsuarioSedeRequest {

	private Integer userId;
	private Integer sedeId;
	
	public UsuarioSedeRequest(){
		
	}
	
	public UsuarioSedeRequest(Integer userId, Integer sedeId) {
		this.userId = userId;
		this.sedeId = sedeId;
	}
}
