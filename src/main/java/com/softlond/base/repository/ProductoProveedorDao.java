package com.softlond.base.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.softlond.base.entity.Producto;
import com.softlond.base.entity.ProductoProveedor;
import com.softlond.base.entity.Proveedor;

@Transactional
public interface ProductoProveedorDao extends CrudRepository<ProductoProveedor, Integer>{

	@Query(value = "SELECT * FROM producto_proveedor where proveedor=?1 and producto is not null", nativeQuery = true)
	public Page<ProductoProveedor> obtenerProductosProveedoresbyProveedor(Integer idProveedor, Pageable page);
	
	@Query(value = "SELECT producto FROM producto_proveedor WHERE codigo=?1 AND proveedor=?2",  nativeQuery = true)
	public Integer obtenerIdProducto(String codigo, Integer idProveedor);
	
	@Query(value = "SELECT producto FROM producto_proveedor WHERE nombre=?1 AND proveedor=?2",  nativeQuery = true)
	public Integer obtenerIdProductoByNombre(String nombre, Integer idProveedor);
	
	@Query(value = "SELECT * FROM producto_proveedor WHERE producto is null and proveedor=?",  nativeQuery = true)
	public List<ProductoProveedor> obtenerProductoProveedorNoAsignado(Integer idProveedor);
	
	@Query(value = "SELECT count(*) FROM producto_proveedor where proveedor=?1 and producto is not null", nativeQuery = true)
	public Integer obtenerCantidadProductos(Integer idProveedor);
	
	@Query(value = "SELECT * FROM producto_proveedor where proveedor=?1 ", nativeQuery = true)
	public List<ProductoProveedor> obtenerProductosProveedor(Integer idProveedor);
	
	public ProductoProveedor findByCodigoAndProveedor(String codigo, Proveedor proveedor);
	
	public List<ProductoProveedor> findByProducto(Producto producto);
	
	@Query(value = "SELECT * FROM producto_proveedor where proveedor=?2 and producto=?1", nativeQuery = true)
	public ProductoProveedor obtenerProductoProveedor(Integer producto, Integer proveedor);
	
	@Query(value = "SELECT * FROM producto_proveedor pp join producto p on p.id=pp.producto where proveedor=?1 and (concat(p.codigo,p.producto) like %?2%)", nativeQuery = true)
	public Page<ProductoProveedor> obtenerProductosProveedoresbyProveedorAndProducto(Integer idProveedor,String  busqueda, Pageable page);
	
	@Modifying
    @Query(value="update producto_proveedor SET producto = null where producto = ?", nativeQuery = true)
    public void desvincularProductoDeProveedor(Integer idproducto);
}
