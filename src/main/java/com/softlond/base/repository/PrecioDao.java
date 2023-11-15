package com.softlond.base.repository;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.softlond.base.entity.FacturaArticulos;
import com.softlond.base.entity.PrdClasificaciones;
import com.softlond.base.entity.Precio;

@Transactional
public interface PrecioDao extends CrudRepository<Precio, Integer> {

	@Query(value = "select * from prd_precios as pr join producto as p on pr.nid_producto=p.id" +
			" where p.id =:idProducto and pr.b_activo = 1 and pr.id_pyme =:idSede limit 1", nativeQuery = true)
	public Optional<Precio> obtenerPrecio(Integer idProducto, Integer idSede);

	@Query(value = "select distinct pr.m_precio_costo_calc from prd_precios as pr join producto as p on pr.nid_producto=p.id join articulo a on a.id_producto = p.id\r\n"
			+
			"where pr.m_precio_costo_calc = (select max(m_precio_costo_calc) from prd_precios where nid_producto= :idProducto) and p.id = :idProducto and a.cantidad_disponible <=0", nativeQuery = true)
	public Double obtenerUltimoCosto(Integer idProducto);

	@Query(value = "select max(m_precio_costo_calc) from prd_precios where nid_producto= :idProducto", nativeQuery = true)
	public Double obtenerUltimoPrecioCosto(Integer idProducto);

	@Query(value = "select distinct pr.m_precio_venta from prd_precios as pr join producto as p on pr.nid_producto=p.id join articulo a on a.id_producto = p.id \r\n"
			+
			"where pr.m_precio_venta = (select max(m_precio_venta) from prd_precios where nid_producto= :idProducto) and p.id = :idProducto and a.cantidad_disponible <=0", nativeQuery = true)
	public Double obtenerUltimoPrecioVenta(Integer idProducto);

	@Query(value = "SELECT * FROM prd_precios where nid_producto =?1 and b_activo =1 and id_pyme = ?2", nativeQuery = true)
	public List<Precio> findAllByPrecioProducto(Integer id, Integer idSede);

	@Query(value = "SELECT * FROM prd_precios where id_pyme = ?1 and nid_producto = ?2  and b_activo = 1 ", nativeQuery = true)
	public Precio obtenerPrecioProducto(Integer idSede, Integer idProducto);

	@Query(value = "SELECT * FROM fac_articulos where nid_remision=?1", nativeQuery = true)
	public List<FacturaArticulos> findAllBySedeRemision(Integer id);

	@Query(value = "SELECT * FROM prd_precios where nid_producto =?1", nativeQuery = true)
	public List<Precio> listarTodosPorProducto(Integer id);

	@Modifying
	@Query(value = "update prd_precios SET nid_producto = null where nid_producto =?1", nativeQuery = true)
	public void desvincularProductoDePrecios(Integer idproducto);

	@Query(value = "select * from prd_precios as pr where nid_producto=:idProducto and b_activo=1 limit 1", nativeQuery = true)
	public List<Precio> obtenerPrecioArticulo(Integer idProducto);

	@Modifying
	@Query(value = "update prd_precios SET b_activo = 0 where nid_producto =?1 and id_pyme =?2", nativeQuery = true)
	public void cambiarEstadoPrecio(Integer idproducto, Integer idSede);

	@Modifying
	@Query(value = "insert into prd_precios (b_activo, m_precio_venta, nid_producto, id_pyme, d_fecha_mod) values (1, :precioVenta, :nidProducto, :nidPyme , :fecha)", nativeQuery = true)
	public void insertarPrecioNuevo(Double precioVenta, Integer nidProducto, Integer nidPyme, Date fecha);

	// @Query(value = "SELECT * FROM prd_precios where nid_producto=:id",
	// nativeQuery = true)
	// public List<Precio> listarTodosPrecios(Integer id);
	@Query(value = "SELECT * FROM prd_precios where nid_producto=:id and id_pyme=:idSede", nativeQuery = true)
	public List<Precio> listarTodosPrecios(Integer id, Integer idSede);

	@Query(value = "select m_precio_venta from prd_precios where nid_producto = :idProducto and id_pyme = :idSede and nid_precio =(select max(nid_precio) from prd_precios where nid_producto = :idProducto and id_pyme =:idSede)", nativeQuery = true)
	public Double obtenerPrecioVentaPorIdProducto(Integer idProducto, Integer idSede);

	@Query(value = "select count(*) from prd_precios where nid_producto = :idProducto and id_pyme = :idSede", nativeQuery = true)
	public Integer cantidadPreciosPorIdProducto(Integer idProducto, Integer idSede);

}
