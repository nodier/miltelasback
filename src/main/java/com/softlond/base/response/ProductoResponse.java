package com.softlond.base.response;


import com.softlond.base.entity.Producto;
import lombok.Data;

@Data
public class ProductoResponse {

	Producto producto;
	Double cantidadArticulos;
	
	public ProductoResponse() {
		super();
	}
	
	public static ProductoResponse convertirProducto(Producto producto) {
		ProductoResponse productoResponse = new ProductoResponse();
		productoResponse.setProducto(producto);
		productoResponse.setCantidadArticulos(0.0);
		return productoResponse;
	}
	
}
