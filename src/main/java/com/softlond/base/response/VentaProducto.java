package com.softlond.base.response;

import com.softlond.base.entity.Producto;

import lombok.Data;

@Data
public class VentaProducto {
	String codigo;
	String producto;
	Integer numFact;
	Double cantidad;
	
	public VentaProducto() {
	}
	
	public static VentaProducto convertirVentaProducto(Producto pr) {
		VentaProducto vp = new VentaProducto();
		vp.codigo = pr.getCodigo();
		vp.producto = pr.getProducto();
		vp.numFact = 0;
		vp.cantidad = 0.0;
		return vp;
	}

}
