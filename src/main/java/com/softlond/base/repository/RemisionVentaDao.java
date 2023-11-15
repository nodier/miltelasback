package com.softlond.base.repository;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.softlond.base.entity.Factura;
import com.softlond.base.entity.NotaDebito;
import com.softlond.base.entity.RemisionVenta;
import com.softlond.base.entity.Vendedor;

@Transactional
public interface RemisionVentaDao extends CrudRepository<RemisionVenta, Integer> {

	@Query(value = "SELECT * from remision_venta WHERE id_sede= ?1 and fecha = CURRENT_DATE() ORDER BY fecha DESC, numero_remision DESC", nativeQuery = true)
	public Page<RemisionVenta> findAllBySede(Integer id, Pageable pageable);

	@Query(value = "SELECT sum(total) from remision_venta WHERE id_sede= ?1 and fecha = CURRENT_DATE() ORDER BY fecha DESC, numero_remision DESC", nativeQuery = true)
	public Integer findAllBySedeTotal(Integer id);

	@Query(value = "SELECT * FROM remision_venta WHERE id_sede= :idSede", nativeQuery = true)
	public List<RemisionVenta> findByIdSede(Integer idSede);

	@Query(value = "SELECT * FROM remision_venta where numero_remision = (SELECT max(numero_remision) FROM remision_venta);", nativeQuery = true)
	public List<RemisionVenta> obtenerNumeroM();

	@Query(value = "SELECT * from remision_venta where numero_remision = ?", nativeQuery = true)
	public Optional<RemisionVenta> obtenerRemision(String numero);

	@Query(value = "SELECT * from remision_venta where numero_remision= ?1 and id_sede= ?2 and cod_estado_con = 2", nativeQuery = true)
	public RemisionVenta obtenerRemisionSede(String numero, Integer idSede);

	@Query(value = "SELECT MAX(R.id) from(SELECT * from remision_venta where numero_remision= ?1 and id_sede= ?2)as R", nativeQuery = true)
	public Integer obtenerRemisionImpresion(String numero, Integer idSede);

	// SELECT * FROM fac_facturas GROUP BY nid_cliente ORDER BY count(*) desc LIMIT
	// 50

	/*
	 * @Query(value =
	 * "SELECT * FROM remision_venta WHERE id_sede= ?1 order by id desc LIMIT 25",
	 * nativeQuery = true)
	 * public List<RemisionVenta> buscarUltimosId(Integer id);
	 */

	@Query(value = "SELECT * from remision_venta WHERE nid_vendedor= ?1 and DAY(fecha) = DAY(CURDATE()) and MONTH(fecha) = MONTH(CURDATE()) and YEAR(fecha) = YEAR(CURDATE()) order by id desc", nativeQuery = true)
	public Page<RemisionVenta> buscarUltimosId(Integer id, Pageable pageable);

	@Query(value = "SELECT * from remision_venta where id_sede = ?1 and cod_estado_con = 2", nativeQuery = true)
	public Page<RemisionVenta> obtenerRemisionesPendientesSede(Integer idSede, Pageable page);

	// @Query(value = "SELECT id from fac_sede_vendedores WHERE nitocc = ?1",
	// nativeQuery = true)
	// public Integer buscarPorCedulaVendedor(String nitocc);

	@Query(value = "SELECT id from informacion_usuario WHERE nitocc = ?1", nativeQuery = true)
	public Integer buscarPorCedulaVendedor(String nitocc);

	/*-----------------------------*/

	@Query(value = "SELECT * from remision_venta WHERE id_sede= ?1 order by id desc LIMIT 1", nativeQuery = true)
	public RemisionVenta buscarNumeroRemision(Integer id);

	@Query(value = "SELECT * from remision_venta WHERE id_sede= ?1 and fecha = CURRENT_DATE()", nativeQuery = true)
	public Page<RemisionVenta> findAllBySedeOrder(Integer id, Pageable pageable);

	@Query(value = "SELECT sum(total) from remision_venta WHERE id_sede= ?1 and fecha = CURRENT_DATE()", nativeQuery = true)
	public Integer findAllBySedeOrderTotal(Integer id);

	@Query(value = "SELECT * from remision_venta WHERE id= ?1", nativeQuery = true)
	public RemisionVenta buscarRemisionImpresion(Integer id);

	@Query(value = "SELECT * from remision_venta rv join fac_articulos fa	on rv.id=fa.nid_remision where fa.nid_articulo=?1", nativeQuery = true)
	public List<RemisionVenta> buscarRemisionArticulo(Integer id);

	/*
	 * @Modifying
	 * 
	 * @Query(
	 * value="update remision_venta SET fecha= ?2, miva= ?3, numero_remision= ?4, m_sub_total= ?5, total= ?6, cod_estado_con= ?7, nid_cliente= ?8, nid_vendedor= ?9, id_sede= ?10 where id= ?1"
	 * , nativeQuery = true)
	 * public void actualizarRemision(Integer idRemision, Date fecha, Double iva,
	 * String numeroRemision, Double subTotal, Integer total, Integer codEstadoCon,
	 * Integer cliente, Integer vendedor, Integer sede);
	 */

	/*
	 * @Modifying
	 * 
	 * @Query(
	 * value="update remision_venta SET fecha= ?2, miva= ?3, numero_remision= ?4, m_sub_total= ?5, total= ?6, cod_estado_con= ?7, nid_cliente= ?8, nid_vendedor= ?9, id_sede= ?10 where id= ?1"
	 * , nativeQuery = true)
	 * public void actualizarRemision(Integer total, Integer sede, Integer vendedor,
	 * Integer idRemision, arra, Date fecha, Double iva, String numeroRemision,
	 * Double subTotal, Integer codEstadoCon, Integer cliente);
	 */

}
