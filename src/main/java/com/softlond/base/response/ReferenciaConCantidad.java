package com.softlond.base.response;

import com.softlond.base.entity.PrdReferencia;

import lombok.Data;

@Data
public class ReferenciaConCantidad {

	PrdReferencia referencia;
	Integer cantidad;
	
	public ReferenciaConCantidad() {
		super();
	}
	
	
}
