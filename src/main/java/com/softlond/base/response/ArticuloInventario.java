package com.softlond.base.response;

import java.util.Date;

import com.softlond.base.entity.Articulo;
import com.softlond.base.entity.ProductoProveedor;
import com.softlond.base.entity.Proveedor;
import com.softlond.base.entity.RemisionCompra;

import lombok.Data;

@Data
public class ArticuloInventario {

	Articulo articulo;
	Proveedor proveedor;
	RemisionCompra remision;
	
	public static ArticuloInventario crearArticuloInventario(Articulo articulo, Proveedor proveedor,
			RemisionCompra remision) {
		ArticuloInventario articuloInventario = new ArticuloInventario();
		articuloInventario.articulo = articulo;
		articuloInventario.proveedor = proveedor;
		articuloInventario.remision = remision;
		return articuloInventario;
	}
}
