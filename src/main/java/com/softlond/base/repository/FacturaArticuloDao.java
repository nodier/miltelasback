package com.softlond.base.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.softlond.base.entity.ArticulosRemisionCompra;
import com.softlond.base.entity.Factura;
import com.softlond.base.entity.FacturaArticulos;

@Transactional
public interface FacturaArticuloDao extends CrudRepository<FacturaArticulos, Integer> {

	@Query(value = "SELECT * FROM fac_articulos where nid_factura=?1", nativeQuery = true)
	public List<FacturaArticulos> findAllBySede(Integer id);

	@Query(value = "SELECT * FROM fac_articulos where nid_remision=?1", nativeQuery = true)
	public List<FacturaArticulos> findAllBySedeRemision(Integer id);

	@Query(value = "select fa.n_cantidad from fac_articulos fa join articulo a on fa.nid_articulo=a.id where fa.nid_factura = ?2 and a.id_producto = ?1 and fa.nid_fac_articulo = (select max(fa.nid_fac_articulo) from fac_articulos fa join articulo a on fa.nid_articulo=a.id where fa.nid_factura = ?2 and a.id_producto = ?1)", nativeQuery = true)
	public Double obtenerCantidadProducto(Integer idProducto, Integer idFactura);

	// @Query(value = "select fa.n_cantidad from fac_articulos fa join articulo a on
	// fa.nid_articulo=a.id where fa.nid_factura = ?2 and a.id_producto = ?1",
	// nativeQuery = true)
	// public Integer obtenerCantidadProducto(Integer idProducto, Integer
	// idFactura);

	@Query(value = "select sum(fa.n_cantidad) from fac_articulos fa inner join articulo a on fa.nid_articulo = a.id where a.id = ?1", nativeQuery = true)
	public Double cantVendida(Integer id);

	@Query(value = "select sum(fa.m_precio_unitario) from fac_articulos fa inner join articulo a on fa.nid_articulo = a.id where a.id = ?1", nativeQuery = true)
	public Double sumCantVendida(Integer id);

	@Query(value = "select sum(fa.n_cantidad) from producto p join articulo a on p.id = a.id_producto join fac_articulos fa on a.id = fa.nid_articulo\r\n"
			+
			"join fac_facturas ff on ff.nid_factura = fa.nid_factura where ff.id_sede = :idSede and p.id = :id and date_format(date(ff.d_fecha_venta),'%Y-%m-%d')\r\n"
			+
			"between date_format(date(:fechaInicial),'%Y-%m-%d') and date_format(date(:fechaFinal),'%Y-%m-%d')", nativeQuery = true)
	public Double sumCantidad(Integer idSede, Integer id, String fechaInicial, String fechaFinal);

	@Query(value = "select * from fac_articulos fa join fac_facturas ff on ff.nid_factura = fa.nid_factura\r\n" +
			"join rcb_rbocajaventa rr on rr.id = ff.id_recibo_caja where ff.id_recibo_caja = :id", nativeQuery = true)
	public FacturaArticulos buscarporIdRecibocaja(Integer id);

	@Query(value = "SELECT * FROM fac_articulos where nid_factura=?1 and nid_articulo=?2", nativeQuery = true)
	public List<FacturaArticulos> obtenerFactArticuloCodigoYFactura(Integer id, Integer articulo);

	@Modifying
	@Query(value = "delete FROM fac_articulos where nid_remision=?1", nativeQuery = true)
	public void eliminarArticulosRemision(Integer idRemision);

	public FacturaArticulos save(Optional<FacturaArticulos> fac);

}
