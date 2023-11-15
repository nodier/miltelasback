package com.softlond.base.response;

import com.softlond.base.entity.PrdTipos;

import lombok.Data;

@Data
public class TiposCantidadResponse {

	PrdTipos tipo;
	Integer Cantidad;
	
	public TiposCantidadResponse() {
		super();
	}
	
	
}
