package com.softlond.base.response;

import java.util.Date;

import lombok.Data;

@Data
public class ProductoCliente {

	String codigoProducto;
	String nombreProducto;
	String numeroFactura;
	// Integer cantidad;
	Double cantidad;
	Date utimaCompra;

	public ProductoCliente(String codigoProducto, String nombreProducto, String numeroFactura, Double cantidad,
			Date utimaCompra) {
		super();
		this.codigoProducto = codigoProducto;
		this.nombreProducto = nombreProducto;
		this.numeroFactura = numeroFactura;
		this.cantidad = cantidad;
		this.utimaCompra = utimaCompra;
	}

}
