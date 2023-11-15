package com.softlond.base.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.Articulo;
import com.softlond.base.entity.Producto;
import com.softlond.base.entity.ProductoProveedor;
import com.softlond.base.entity.Proveedor;
import com.softlond.base.repository.ArticuloDao;
import com.softlond.base.repository.ProductoDao;
import com.softlond.base.repository.ProductoProveedorDao;

@Service
public class ProductoProveedorService {
	
	private static final Logger logger = Logger.getLogger(ProductoProveedorService.class);
	
	@Autowired
	private ProductoProveedorDao productoProveedorDao;
	@Autowired
	private ProductoDao productoDao;
	
	@Autowired
	private ArticuloDao articuloDao;

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> crearPrductoProveedor(ProductoProveedor proveedorProducto){
		ResponseEntity<Object> respuesta;
		try {
			if(productoProveedorDao.findByCodigoAndProveedor(proveedorProducto.getCodigo(), 
					proveedorProducto.getProveedor())!=null) throw new Exception();
			productoProveedorDao.save(proveedorProducto);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "creación de proveedore exitosa");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
			
		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error("Error creando proveedor" + e.getMessage());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo crar un proveedor");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}
	
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> actualizarProductoProveedor(ProductoProveedor proveedorProducto){
		ResponseEntity<Object> respuesta;
		try {
			productoProveedorDao.save(proveedorProducto);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "creación de proveedore exitosa");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
			
		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error("Error creando proveedor" + e.getMessage());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo crar un proveedor");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}
	
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> eliminarProductoProveedor(Integer idProveedorProducto){
		ResponseEntity<Object> respuesta;
		try {
			productoProveedorDao.deleteById(idProveedorProducto);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "eliminación de producto exitosa");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
			
		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error("Error creando proveedor" + e.getMessage());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo eliminar un producto");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}
	
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> ObtenerProductoProveedores(){
		ResponseEntity<Object> respuesta;
		try {
			List<ProductoProveedor> proveedoresProductos = (List<ProductoProveedor>) productoProveedorDao.findAll();
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de proveedores exitosa");
			respuestaDto.setObjetoRespuesta(proveedoresProductos);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error("Error creando proveedor" + e.getMessage());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo obteneer los proveedores");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}
	
	/***
	 * 
	 * @return listado de los productos por proveedor que no estan asignados a un producto miltelas
	 */
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> ObtenerProductoProveedorNoAsignado(Integer idProveedor){
		ResponseEntity<Object> respuesta;
		try {
			List<ProductoProveedor> proveedoresProductos = (List<ProductoProveedor>) productoProveedorDao.obtenerProductoProveedorNoAsignado(idProveedor);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de proveedores exitosa");
			respuestaDto.setObjetoRespuesta(proveedoresProductos);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error("Error creando proveedor" + e.getMessage());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo obteneer los proveedores");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}
	
	/***
	 * obtiene la cantidad de los prodcutos asignados a un proveedor
	 * @param idProveedor
	 * @return
	 */
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> ObtenerCantidadDeProductos(Integer idProveedor){
		ResponseEntity<Object> respuesta;
		try {
			Integer cantidad = productoProveedorDao.obtenerCantidadProductos(idProveedor);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de proveedores exitosa");
			respuestaDto.setObjetoRespuesta(cantidad);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error("Error creando proveedor" + e.getMessage());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo obteneer los proveedores");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}
	
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> ObtenerProductoProveedoresbyProveedor(Integer idProveedor, Integer page){
		ResponseEntity<Object> respuesta;
		try {
			Pageable pageConfig = PageRequest.of(page, 10);
			Page<ProductoProveedor> proveedoresProductos = productoProveedorDao.obtenerProductosProveedoresbyProveedor(idProveedor,pageConfig);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de proveedores exitosa");
			respuestaDto.setObjetoRespuesta(proveedoresProductos);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error("Error creando proveedor" + e.getMessage());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo obteneer los proveedores");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}
	
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> ObtenerProductoByCodigo(String codigo, Integer idProveedor){
		ResponseEntity<Object> respuesta;
		try {
			Integer productoId = productoProveedorDao.obtenerIdProducto(codigo,idProveedor);
			Producto nombre= this.productoDao.obtenerProducto(productoId);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de producto exitosa");
			respuestaDto.setObjetoRespuesta(nombre);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error("Error obteniendo producto" + e.getMessage());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo obtener los productos");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}
	
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> ObtenerProductoByNombre(String nombre, Integer idProveedor){
		ResponseEntity<Object> respuesta;
		try {
			Integer productoId = productoProveedorDao.obtenerIdProductoByNombre(nombre,idProveedor);
			Producto nombreProducto= this.productoDao.obtenerProducto(productoId);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de producto exitosa");
			respuestaDto.setObjetoRespuesta(nombreProducto);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error("Error obteniendo producto" + e.getMessage());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo obtener los productos");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}
	

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> ObtenerProductosProveedor(Integer idProveedor){
		ResponseEntity<Object> respuesta;
		try {
			
			List<ProductoProveedor> proveedoresProductos = productoProveedorDao.obtenerProductosProveedor(idProveedor);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de proveedores exitosa");
			respuestaDto.setObjetoRespuesta(proveedoresProductos);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error("Error creando proveedor" + e.getMessage());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo obteneer los proveedores");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}
	
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> ObtenerProductosProveedorNoAsignadosRemision(Integer idProveedor){
		ResponseEntity<Object> respuesta;
		try {
			
			List<ProductoProveedor> proveedoresProductos = productoProveedorDao.obtenerProductosProveedor(idProveedor);
			for (ProductoProveedor productoProveedor : proveedoresProductos) {
				List<Articulo> articulos = articuloDao.obtenerArticulosSinRemision(productoProveedor.getProducto().getId());
				productoProveedor.getProducto().setArticulos(articulos);
			}
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de proveedores exitosa");
			respuestaDto.setObjetoRespuesta(proveedoresProductos);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error("Error obteniendo proveedor" + e.getMessage());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo obteneer los proveedores");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}
	
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> ObtenerProductosProveedorPorProducto(Producto producto){
		ResponseEntity<Object> respuesta;
		try {
			
			List<ProductoProveedor> proveedoresProductos = productoProveedorDao.findByProducto(producto);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de productos exitosa");
			respuestaDto.setObjetoRespuesta(proveedoresProductos);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error("Error obteniendo productos" + e.getMessage());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo obtener los productos");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}
	
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> ObtenerProductosProveedorPorArticuloYProveedor(Integer producto, Integer proveedor){
		ResponseEntity<Object> respuesta;
		try {
			
			ProductoProveedor proveedorProducto = productoProveedorDao.obtenerProductoProveedor(producto, proveedor);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de productos exitosa");
			respuestaDto.setObjetoRespuesta(proveedorProducto);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error("Error obteniendo productos" + e.getMessage());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo obtener los productos");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}
	
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> ObtenerProductoProveedoresbyProveedorAndProducto(Integer idProveedor, String busqueda, Integer page){
		ResponseEntity<Object> respuesta;
		try {
			Pageable pageConfig = PageRequest.of(page, 10);
			Page<ProductoProveedor> proveedoresProductos = productoProveedorDao.obtenerProductosProveedoresbyProveedorAndProducto(idProveedor, busqueda, pageConfig);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de proveedores exitosa");
			respuestaDto.setObjetoRespuesta(proveedoresProductos);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error("Error creando proveedor" + e.getMessage());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo obteneer los proveedores");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}
}
