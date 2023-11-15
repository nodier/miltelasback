package com.softlond.base.repository;

import java.util.ArrayList;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.softlond.base.entity.Articulo;
import com.softlond.base.entity.Clientes;
import com.softlond.base.entity.Factura;
import com.softlond.base.entity.PrdDescuentos;
import com.softlond.base.entity.PrdReferencia;
import com.softlond.base.entity.PrdTipos;
import com.softlond.base.entity.Producto;
import com.softlond.base.entity.Proveedor;

@Transactional
public interface ProductoDao extends CrudRepository<Producto, Integer> {

	@Query(value = "SELECT count(*) FROM producto WHERE tipo=?1 and referencia=?2 and id_presentacion=?3 and isnull(id_descuento)", nativeQuery = true)
	public Integer buscarProductoDescuento(Integer tipo, Integer referencia, Integer presentacion);

	@Query(value = "SELECT * FROM producto WHERE tipo=?1 and referencia=?2 and id_presentacion=?3", nativeQuery = true)
	public List<Producto> buscarProductoDescuentoc(Integer tipo, Integer referencia, Integer presentacion);

	@Modifying
	@Query(value = "update producto SET id_descuento = ?1 WHERE tipo=?2 and referencia=?3 and id_presentacion=?4 and isnull(id_descuento)", nativeQuery = true)
	public void asociarDescuentosProductos(Integer idDescuento, Integer tipo, Integer referencia, Integer presentacion);

	@Query(value = "SELECT * FROM producto WHERE id=?1", nativeQuery = true)
	public Producto obtenerProducto(Integer codigo);

	@Query(value = "select * from producto where concat(codigo,producto) like %?% limit 100", nativeQuery = true)
	public List<Producto> obtenerProductoFiltro(String text);

	@Query(value = "select * from producto p join producto_proveedor pp on p.id = pp.producto where pp.proveedor=?1", nativeQuery = true)
	public List<Producto> obtenerProductoProveedor(Integer proveedor);

	@Query(value = "SELECT * FROM producto WHERE id_sede=?1", nativeQuery = true)
	public List<Producto> listarProducto(Integer idSede);

	@Query(value = "SELECT * FROM producto", nativeQuery = true)
	public ArrayList<Producto> obtenerProductos();

	@Query(value = "SELECT * FROM producto where id=:idI", nativeQuery = true)
	public ArrayList<Producto> obtenerProductosRango(Integer idI);

	@Query(value = "select * from producto where concat(codigo,producto) like %?% limit 100", nativeQuery = true)
	public List<Producto> obtenerProductoNoExistenteFiltro(String producto);

	@Query(value = "SELECT * FROM producto WHERE id = :idProd AND "
			+ "id_clasificacion= :idClas", nativeQuery = true)
	public List<Producto> buscarPorProductoYClasificacion(Integer idProd, Integer idClas);

	@Query(value = "select ifnull(sum(fa.n_cantidad),0) from fac_facturas f join fac_articulos fa on f.nid_factura = fa.nid_factura join articulo a on a.id = fa.nid_articulo\r\n"
			+ "where a.id_producto = ?1 and date_format(date(f.d_fecha_venta),'%Y-%m-%d') between date(?2) and (?3)", nativeQuery = true)
	public Double cantidadProductosAcumuladoClientes(Integer idProducto, String fechaInicial, String fechaFinal);

	@Query(value = "select ifnull(count(*),0) from producto p join articulo a on a.id_producto=p.id where a.nid_estado_articulo != 4 and p.id = ?1 and a.id_sede= ?2", nativeQuery = true)
	public Double obtenerCantidadArticulos(Integer idProducto, Integer idSede);

	public Optional<Producto> findByCodigo(String codigo);

	@Query(value = "select * from producto where id = ?1", nativeQuery = true)
	public Page<Producto> obtenerTodosProducto(Pageable pageable, Integer idProducto);

	@Query(value = "select * from producto", nativeQuery = true)
	public Page<Producto> obtenerTodosProductoPage(Pageable pageable);

	@Query(value = "select * from producto p join articulo a on a.id_producto = p.id where a.id_sede =15", nativeQuery = true)
	public Page<Producto> obtenerTodosProductoPageDisponibles(Pageable pageable);

	// @Query(value = "select * from producto p where p.tipo = ?1 and
	// p.referencia=?2 and p.id_presentacion=?3 and p.id_color=?4", nativeQuery =
	// true)
	// public Producto obtenerTodosProductoRecodificar(Pageable pageable, Integer
	// tipo, Integer referencia,
	// Integer presentacion, Integer color);

	@Query(value = "SELECT * FROM producto where id = ?1", nativeQuery = true)
	public List<Producto> obtenerProductoId(Integer idProducto);

	@Query(value = "select distinct p.* from articulo a inner join producto p ON a.id_producto = p.id where p.id =?1 and a.cantidad_disponible > 0", nativeQuery = true)
	public List<Producto> listarProductosExistentes(Integer idProducto);

	public Producto findBycodigo(String codigo);

	@Query(value = "SELECT * FROM producto where id_color = ?1", nativeQuery = true)
	public List<Producto> listarProductosIdColor(Integer idColor);

	@Query(value = "SELECT * FROM producto where id_presentacion = ?1", nativeQuery = true)
	public List<Producto> listarProductosIdPresentacion(Integer idPresentacion);

	@Query(value = "SELECT * FROM producto where referencia = ?1", nativeQuery = true)
	public List<Producto> listarProductosIdReferencia(Integer idReferencia);

	@Query(value = "SELECT * FROM producto where tipo = ?1", nativeQuery = true)
	public List<Producto> listarProductosIdTipo(Integer idTipo);

	@Query(value = "SELECT * FROM producto p join articulo a on a.id_producto=p.id where a.id = ?1", nativeQuery = true)
	public Producto obteneArticulo(Integer idArticulo);

}
