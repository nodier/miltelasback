package com.softlond.base.response;

import java.util.Date;

import com.softlond.base.entity.Producto;
import com.softlond.base.entity.Proveedor;

import lombok.Data;

@Data
public class ProductoPrecio {

	Producto producto;
	Double precio;
	
	public ProductoPrecio() {
	}

	public ProductoPrecio(Producto producto, Double precio) {
		super();
		this.producto = producto;
		this.precio = precio;
	}
	
	
}
