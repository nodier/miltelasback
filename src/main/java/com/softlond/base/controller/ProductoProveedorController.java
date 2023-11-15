package com.softlond.base.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.softlond.base.constant.ApiConstant;
import com.softlond.base.entity.Producto;
import com.softlond.base.entity.ProductoProveedor;
import com.softlond.base.entity.Proveedor;
import com.softlond.base.service.ProductoProveedorService;

@Controller
@RequestMapping(ApiConstant.PRODUCTO_PROVEEDOR_API)
public class ProductoProveedorController {
	
	@Autowired
	private ProductoProveedorService productoProveedorService;
	
	@PostMapping(value = ApiConstant.PRODUCTO_PROVEEDOR_API_CREAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> crearProductoProveedor(@RequestBody ProductoProveedor productoProveedor){
		return this.productoProveedorService.crearPrductoProveedor(productoProveedor);
	}
	
	@PostMapping(value = ApiConstant.PRODUCTO_PROVEEDOR_API_ACTUALIZAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> actualizarProductoProveedor(@RequestBody ProductoProveedor productoProveedor){
		return this.productoProveedorService.actualizarProductoProveedor(productoProveedor);
	}
	
	@GetMapping(value = ApiConstant.PRODUCTO_PROVEEDOR_API_LISTAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> obtenerProdcutosProveedores(){
		return this.productoProveedorService.ObtenerProductoProveedores();
	}
	
	@GetMapping(value = ApiConstant.PRODUCTO_PROVEEDOR_API_LISTAR_PROVEEDOR, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> obtenerProdcutosProveedores(Integer idProveedor, Integer page){
		return this.productoProveedorService.ObtenerProductoProveedoresbyProveedor(idProveedor, page);
	}
	
	@DeleteMapping(value = ApiConstant.PRODUCTO_PROVEEDOR_API_ELIMINAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> eliminarProductoProveedor(Integer idProveedorProducto){
		return this.productoProveedorService.eliminarProductoProveedor(idProveedorProducto);
	}

	@GetMapping(value = ApiConstant.PRODUCTO_PROVEEDOR_API_BUSCAR_CODIGO, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> obtenerProductoAlmacenCodigo(String codigo, Integer idProveedor){
		return this.productoProveedorService.ObtenerProductoByCodigo(codigo, idProveedor);
	}
	
	@GetMapping(value = ApiConstant.PRODUCTO_PROVEEDOR_API_BUSCAR_NOMBRE_PROVEEDOR, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> obtenerProductoNombre(String nombre, Integer idProveedor){
		return this.productoProveedorService.ObtenerProductoByNombre(nombre, idProveedor);
	}
	
	@GetMapping(value = ApiConstant.PRODUCTO_PROVEEDOR_API_LISTAR_PROVEEDOR_NO_ASIGNADO, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> obtenerProductosProveedorNoAsignado(Integer idProveedor){
		return this.productoProveedorService.ObtenerProductoProveedorNoAsignado(idProveedor);
	}
	
	@GetMapping(value = ApiConstant.PRODUCTO_PROVEEDOR_API_CANTIDAD_PRODUCTO, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> obtenerCantidadProductos(Integer idProveedor){
		return this.productoProveedorService.ObtenerCantidadDeProductos(idProveedor);
	}
	
	@GetMapping(value = ApiConstant.PRODUCTO_PROVEEDOR_API_LISTAR_POR_PROVEEDOR, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> obtenerProdcutosProveedor(Integer idProveedor){
		return this.productoProveedorService.ObtenerProductosProveedor(idProveedor);
	}
	
	@GetMapping(value = ApiConstant.PRODUCTO_PROVEEDOR_API_LISTAR_POR_PROVEEDOR_NO_ASIGNADOS, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> obtenerProductosProveedorNoAsignados(Integer idProveedor){
		return this.productoProveedorService.ObtenerProductosProveedorNoAsignadosRemision(idProveedor);
	}
	
	@PostMapping(value = ApiConstant.PRODUCTO_PROVEEDOR_API_LISTAR_POR_PRODUCTO, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> obtenerProductosProveedorPorProducto(@RequestBody Producto producto){
		return this.productoProveedorService.ObtenerProductosProveedorPorProducto(producto);
	}
	
	@GetMapping(value = ApiConstant.PRODUCTO_PROVEEDOR_API_OBTENER_POR_ARTICULO, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> obtenerProductosProveedorPorProducto(Integer producto, Integer proveedor){
		return this.productoProveedorService.ObtenerProductosProveedorPorArticuloYProveedor(producto, proveedor);
	}
	
	@GetMapping(value = ApiConstant.PRODUCTO_PROVEEDOR_API_LISTAR_PROVEEDOR_PRODUCTO, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> obtenerProductosProveedoresFiltro(Integer idProveedor, String busqueda, Integer page){
		return this.productoProveedorService.ObtenerProductoProveedoresbyProveedorAndProducto(idProveedor, busqueda, page);
	}
}
