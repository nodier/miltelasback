package com.softlond.base.response;

import com.softlond.base.entity.PrdColores;

import lombok.Data;

@Data
public class ColorCantidadResponse {

	PrdColores color;
	Integer cantidad;
	
	public ColorCantidadResponse() {
		super();
	}
	
	
}
