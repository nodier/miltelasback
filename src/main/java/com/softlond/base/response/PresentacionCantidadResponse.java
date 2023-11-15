package com.softlond.base.response;

import com.softlond.base.entity.PrdPresentacion;

import lombok.Data;

@Data
public class PresentacionCantidadResponse {

	PrdPresentacion presentacion;
	Integer cantidad;
	
	
	public PresentacionCantidadResponse() {
		super();
	}
	
}
