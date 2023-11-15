package com.softlond.base.repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softlond.base.entity.Clientes;
import com.softlond.base.entity.EstadoDocumento;
import com.softlond.base.entity.Factura;
import com.softlond.base.entity.FacturaCompra;
import com.softlond.base.entity.ReciboCajaVenta;

@Transactional
public interface FacturaDao extends CrudRepository<Factura, Integer> {

	@Query(value = "SELECT * FROM fac_facturas where id_sede=?1 ORDER BY d_fecha_venta DESC", nativeQuery = true)
	public Page<Factura> findAllBySede(Integer id, Pageable pageable);

	@Query(value = "SELECT * FROM fac_facturas where date_format(date(d_fecha_venta),'%Y-%m-%d') = date_format(date(:fecha),'%Y-%m-%d') AND id_sede = :idSede ORDER BY n_nro_factura DESC, d_fecha_venta DESC", nativeQuery = true)
	public Page<Factura> findByFechaSede(@Param("idSede") Integer idSede, @Param("fecha") String fecha,
			Pageable pageable);

	@Query(value = "SELECT * FROM fac_facturas where date_format(date(d_fecha_venta),'%Y-%m-%d') = date_format(date(:fecha),'%Y-%m-%d') AND id_sede = :idSede", nativeQuery = true)
	public ArrayList<Factura> findByFechaSedeTotal(@Param("idSede") Integer idSede, @Param("fecha") String fecha);

	@Query(value = "SELECT * FROM fac_facturas WHERE date_format(date(d_fecha_venta),'%Y-%m-%d') BETWEEN date_format(date(:fechaInicial),'%Y-%m-%d') AND "
			+ " date_format(date(:fechaFinal), '%Y-%m-%d') AND id_sede = :idSede ORDER BY n_nro_factura DESC, d_fecha_venta DESC", nativeQuery = true)
	public Page<Factura> findByFechas(@Param("idSede") Integer idSede, @Param("fechaInicial") String fechaInicial,
			@Param("fechaFinal") String fechaFinal, Pageable pageable);

	@Query(value = "SELECT * FROM fac_facturas WHERE date_format(date(d_fecha_venta),'%Y-%m-%d') BETWEEN date_format(date(:fechaInicial),'%Y-%m-%d') AND "
			+ " date_format(date(:fechaFinal), '%Y-%m-%d') AND id_sede = :idSede ", nativeQuery = true)
	public ArrayList<Factura> findByFechasTotal(@Param("idSede") Integer idSede,
			@Param("fechaInicial") String fechaInicial, @Param("fechaFinal") String fechaFinal);

	@Query(value = "SELECT * FROM fac_facturas WHERE nid_cliente = :idCliente AND id_sede = :idSede ORDER BY n_nro_factura DESC", nativeQuery = true)
	public Page<Factura> findByClienteSede(@Param("idSede") Integer idSede, @Param("idCliente") Integer idCliente,
			Pageable pageable);

	@Query(value = "SELECT * FROM fac_facturas WHERE nid_cliente = :idCliente AND id_sede = :idSede", nativeQuery = true)
	public ArrayList<Factura> findBySedeClienteTotal(@Param("idSede") Integer idSede,
			@Param("idCliente") Integer idCliente);

	@Query(value = "SELECT * FROM fac_facturas AS f INNER JOIN conceptos_recibo_caja AS c ON f.id_concepto = c.id "
			+ " and f.nid_pyme_tipo_venta = 15 AND c.saldo_final != 0  AND f.id_sede = :idSede ORDER BY n_nro_factura DESC", nativeQuery = true)
	public Page<Factura> findByVentasCredito(@Param("idSede") Integer idSede, Pageable pageable);

	@Query(value = "SELECT sum(f.m_total) FROM fac_facturas AS f INNER JOIN conceptos_recibo_caja AS c ON f.id_concepto = c.id "
			+ " WHERE c.saldo_final != 0 AND f.nid_pyme_tipo_venta = 15 AND f.id_sede = :idSede ", nativeQuery = true)
	public Integer findByCreditoTotal(@Param("idSede") Integer idSede);

	@Query(value = "SELECT * FROM fac_facturas AS f INNER JOIN conceptos_recibo_caja AS c ON"
			+ "  (f.id_concepto = c.id  and c.saldo_final = 0) "
			+ " AND f.id_sede = :idSede ORDER BY n_nro_factura DESC, d_fecha_venta DESC", nativeQuery = true)
	public Page<Factura> findByVentasPago(@Param("idSede") Integer idSede, Pageable pageable);

	@Query(value = "SELECT sum(f.m_total) FROM fac_facturas AS f INNER JOIN conceptos_recibo_caja AS c ON"
			+ " f.id_concepto = c.id  and c.saldo_final = 0 " + " AND f.id_sede = :idSede ", nativeQuery = true)
	public Integer findByPagoTotal(@Param("idSede") Integer idSede);

	@Query(value = "SELECT * FROM fac_facturas  AS f INNER JOIN conceptos_recibo_caja AS c ON"
			+ "	f.id_concepto = c.id AND f.n_nro_factura = :numFact AND id_sede = :idSede ORDER BY :orden DESC", nativeQuery = true)
	public Page<Factura> findNumeroFact(@Param("idSede") Integer idSede, @Param("numFact") Integer numFact,
			Pageable pageable, String orden);

	@Query(value = "SELECT * FROM fac_facturas AS f where f.n_nro_factura = :numFact AND id_sede = :idSede ORDER BY :orden DESC", nativeQuery = true)
	public Page<Factura> findNumeroFactA(@Param("idSede") Integer idSede, @Param("numFact") Integer numFact,
			Pageable pageable, String orden);

	@Query(value = "SELECT sum(f.m_total) FROM fac_facturas  AS f INNER JOIN conceptos_recibo_caja AS c ON"
			+ " f.id_concepto = c.id AND f.n_nro_factura = :numFact AND id_sede = :idSede", nativeQuery = true)
	public Integer findByNumFact(@Param("idSede") Integer idSede, @Param("numFact") Integer numFact);

	@Query(value = "SELECT * FROM fac_facturas WHERE n_nro_factura=?", nativeQuery = true)
	public ArrayList<Factura> findByNum(Integer numFact);

	// @Query(value = "SELECT * FROM fac_facturas WHERE n_nro_factura like %?% ",
	// nativeQuery = true)
	// public ArrayList<Factura> findByNumAnular(Integer numFact);

	@Query(value = "SELECT * FROM fac_facturas WHERE n_nro_factura =:numFact AND id_sede = :idSede", nativeQuery = true)
	public ArrayList<Factura> findByNumAnular(Integer numFact, Integer idSede);

	@Query(value = "SELECT * FROM fac_facturas WHERE nid_pyme_tipo_venta = 15 AND nid_cliente=?1", nativeQuery = true)
	public ArrayList<Factura> findByCliente(Integer idCliente);

	@Query(value = "SELECT * FROM fac_facturas AS F INNER JOIN conceptos_recibo_caja AS c ON f.nid_factura = c.id_factura AND c.id=?1", nativeQuery = true)
	public Factura findByFactura(Integer idConcepto);

	@Query(value = "SELECT * FROM  fac_facturas AS f INNER JOIN conceptos_recibo_caja AS crc  ON crc.id = f.id_concepto "
			+ "WHERE crc.saldo_final != 0 AND f.nid_pyme_tipo_venta = 15 AND f.nid_cliente=?", nativeQuery = true)
	public ArrayList<Factura> obtenerConceptos(Integer idUsuario);

	// Busqueda entre consultas listado facturas
	@Query(value = "SELECT * FROM fac_facturas AS f INNER JOIN conceptos_recibo_caja AS c ON"
			+ "	 f.id_concepto = c.id and f.nid_cliente = :idCliente and "
			+ "date_format(date(f.d_fecha_venta),'%Y-%m-%d') = date_format(date(:fechaInicial),'%Y-%m-%d') AND f.id_sede = :idSede"
			+ "  ORDER BY :orden DESC", nativeQuery = true)
	public Page<Factura> findByClienteFechaInicial(@Param("idSede") Integer idSede,
			@Param("idCliente") Integer idCliente, @Param("fechaInicial") String fechaInicial, Pageable pageable,
			String orden);

	@Query(value = "SELECT * FROM fac_facturas AS f INNER JOIN conceptos_recibo_caja AS c ON"
			+ "	 f.id_concepto = c.id and f.nid_cliente = :idCliente and "
			+ "date_format(date(f.d_fecha_venta),'%Y-%m-%d') = date_format(date(:fechaInicial),'%Y-%m-%d') AND f.id_sede = :idSede", nativeQuery = true)
	public ArrayList<Factura> findByClienteFechaInicialTotal(@Param("idSede") Integer idSede,
			@Param("idCliente") Integer idCliente, @Param("fechaInicial") String fechaInicial);

	@Query(value = "SELECT * FROM fac_facturas AS f INNER JOIN conceptos_recibo_caja AS c ON"
			+ "	 f.id_concepto = c.id and f.nid_cliente = :idCliente and "
			+ "date_format(date(f.d_fecha_venta),'%Y-%m-%d') BETWEEN date_format(date(:fechaInicial),'%Y-%m-%d') AND "
			+ "	 date_format(date(:fechaFinal), '%Y-%m-%d') AND f.id_sede = :idSede"
			+ "  ORDER BY :orden DESC", nativeQuery = true)
	public Page<Factura> findByClienteFechas(@Param("idSede") Integer idSede, @Param("idCliente") Integer idCliente,
			@Param("fechaInicial") String fechaInicial, @Param("fechaFinal") String fechaFinal, Pageable pageable,
			String orden);

	@Query(value = "SELECT * FROM fac_facturas AS f INNER JOIN conceptos_recibo_caja AS c ON"
			+ "	 f.id_concepto = c.id and f.nid_cliente = :idCliente and "
			+ "date_format(date(f.d_fecha_venta),'%Y-%m-%d') BETWEEN date_format(date(:fechaInicial),'%Y-%m-%d') AND "
			+ "	 date_format(date(:fechaFinal), '%Y-%m-%d') AND f.id_sede = :idSede", nativeQuery = true)
	public ArrayList<Factura> findByClienteFechasTotal(@Param("idSede") Integer idSede,
			@Param("idCliente") Integer idCliente, @Param("fechaInicial") String fechaInicial,
			@Param("fechaFinal") String fechaFinal);

	@Query(value = "SELECT * FROM fac_facturas AS f INNER JOIN conceptos_recibo_caja AS c ON f.id_concepto = c.id "
			+ " and f.nid_pyme_tipo_venta = 15 AND c.saldo_final != 0 and f.nid_cliente = :idCliente  AND f.id_sede = :idSede ORDER BY :orden DESC", nativeQuery = true)
	public Page<Factura> findByClienteEstadoCredito(@Param("idSede") Integer idSede,
			@Param("idCliente") Integer idCliente, Pageable pageable, String orden);

	@Query(value = "SELECT * FROM fac_facturas AS f INNER JOIN conceptos_recibo_caja AS c ON f.id_concepto = c.id "
			+ " and f.nid_pyme_tipo_venta = 15 AND c.saldo_final != 0 and f.nid_cliente = :idCliente  AND f.id_sede = :idSede", nativeQuery = true)
	public ArrayList<Factura> findByClienteEstadoCreditoTotal(@Param("idSede") Integer idSede,
			@Param("idCliente") Integer idCliente);

	@Query(value = "SELECT * FROM fac_facturas AS f INNER JOIN conceptos_recibo_caja AS c ON"
			+ "  (f.id_concepto = c.id  and c.saldo_final = 0) and f.nid_cliente = :idCliente "
			+ " AND f.id_sede = :idSede ORDER BY :orden DESC", nativeQuery = true)
	public Page<Factura> findByClienteEstadoContado(@Param("idSede") Integer idSede,
			@Param("idCliente") Integer idCliente, Pageable pageable, String orden);

	@Query(value = "SELECT * FROM fac_facturas AS f INNER JOIN conceptos_recibo_caja AS c ON"
			+ "  (f.id_concepto = c.id  and c.saldo_final = 0) and f.nid_cliente = :idCliente "
			+ " AND f.id_sede = :idSede ", nativeQuery = true)
	public ArrayList<Factura> findByClienteEstadoContadoTotal(@Param("idSede") Integer idSede,
			@Param("idCliente") Integer idCliente);

	@Query(value = "SELECT * FROM fac_facturas AS f INNER JOIN conceptos_recibo_caja AS c ON"
			+ "	 f.id_concepto = c.id and f.nid_pyme_tipo_venta = 15 AND c.saldo_final != 0 and "
			+ "date_format(date(f.d_fecha_venta),'%Y-%m-%d') = date_format(date(:fechaInicial),'%Y-%m-%d') AND f.id_sede = :idSede"
			+ "  ORDER BY ORDER BY :orden DESC, f.d_fecha_venta DESC", nativeQuery = true)
	public Page<Factura> findByFechaEstadoCredito(@Param("idSede") Integer idSede,
			@Param("fechaInicial") String fechaInicial, Pageable pageable, String orden);

	@Query(value = "SELECT * FROM fac_facturas AS f INNER JOIN conceptos_recibo_caja AS c ON"
			+ "	 f.id_concepto = c.id and f.nid_pyme_tipo_venta = 15 AND c.saldo_final != 0 and "
			+ "date_format(date(f.d_fecha_venta),'%Y-%m-%d') = date_format(date(:fechaInicial),'%Y-%m-%d') AND f.id_sede = :idSede", nativeQuery = true)
	public ArrayList<Factura> findByFechaEstadoCreditoTotal(@Param("idSede") Integer idSede,
			@Param("fechaInicial") String fechaInicial);

	@Query(value = "SELECT * FROM fac_facturas AS f INNER JOIN conceptos_recibo_caja AS c ON"
			+ "  (f.id_concepto = c.id  and c.saldo_final = 0) and date_format(date(f.d_fecha_venta),'%Y-%m-%d') = date_format(date(:fechaInicial),'%Y-%m-%d') "
			+ " AND f.id_sede = :idSede ORDER BY :orden DESC", nativeQuery = true)
	public Page<Factura> findByFechaEstadoContado(@Param("idSede") Integer idSede,
			@Param("fechaInicial") String fechaInicial, Pageable pageable, String orden);

	@Query(value = "SELECT * FROM fac_facturas AS f INNER JOIN conceptos_recibo_caja AS c ON"
			+ "  (f.id_concepto = c.id  and c.saldo_final = 0) and date_format(date(f.d_fecha_venta),'%Y-%m-%d') = date_format(date(:fechaInicial),'%Y-%m-%d') "
			+ " AND f.id_sede = :idSede", nativeQuery = true)
	public ArrayList<Factura> findByFechaEstadoContadoTotal(@Param("idSede") Integer idSede,
			@Param("fechaInicial") String fechaInicial);

	@Query(value = "SELECT * FROM fac_facturas AS f INNER JOIN conceptos_recibo_caja AS c ON"
			+ "	 f.id_concepto = c.id and f.nid_pyme_tipo_venta = 15 AND c.saldo_final != 0 and "
			+ "date_format(date(f.d_fecha_venta),'%Y-%m-%d') BETWEEN date_format(date(:fechaInicial),'%Y-%m-%d') AND "
			+ "	date_format(date(:fechaFinal), '%Y-%m-%d') AND f.id_sede = :idSede"
			+ "  ORDER BY :orden DESC", nativeQuery = true)
	public Page<Factura> findByFechasEstadoCredito(@Param("idSede") Integer idSede,
			@Param("fechaInicial") String fechaInicial, @Param("fechaFinal") String fechaFinal, Pageable pageable,
			String orden);

	@Query(value = "SELECT * FROM fac_facturas AS f INNER JOIN conceptos_recibo_caja AS c ON"
			+ "	 f.id_concepto = c.id and f.nid_pyme_tipo_venta = 15 AND c.saldo_final != 0 and "
			+ "date_format(date(f.d_fecha_venta),'%Y-%m-%d') BETWEEN date_format(date(:fechaInicial),'%Y-%m-%d') AND "
			+ "	date_format(date(:fechaFinal), '%Y-%m-%d') AND f.id_sede = :idSede", nativeQuery = true)
	public ArrayList<Factura> findByFechasEstadoCreditoTotal(@Param("idSede") Integer idSede,
			@Param("fechaInicial") String fechaInicial, @Param("fechaFinal") String fechaFinal);

	@Query(value = "SELECT * FROM fac_facturas AS f INNER JOIN conceptos_recibo_caja AS c ON"
			+ "  (f.id_concepto = c.id  and c.saldo_final = 0) and date_format(date(f.d_fecha_venta),'%Y-%m-%d') BETWEEN date_format(date(:fechaInicial),'%Y-%m-%d') AND "
			+ "	date_format(date(:fechaFinal), '%Y-%m-%d') "
			+ " AND f.id_sede = :idSede ORDER BY :orden DESC", nativeQuery = true)
	public Page<Factura> findByFechasEstadoContado(@Param("idSede") Integer idSede,
			@Param("fechaInicial") String fechaInicial, @Param("fechaFinal") String fechaFinal, Pageable pageable,
			String orden);

	@Query(value = "SELECT * FROM fac_facturas AS f INNER JOIN conceptos_recibo_caja AS c ON"
			+ "  (f.id_concepto = c.id  and c.saldo_final = 0) and date_format(date(f.d_fecha_venta),'%Y-%m-%d') BETWEEN date_format(date(:fechaInicial),'%Y-%m-%d') AND "
			+ "	date_format(date(:fechaFinal), '%Y-%m-%d') "
			+ " AND f.id_sede = :idSede ", nativeQuery = true)
	public ArrayList<Factura> findByFechasEstadoContadoTotal(@Param("idSede") Integer idSede,
			@Param("fechaInicial") String fechaInicial, @Param("fechaFinal") String fechaFinal);

	// Anulacion factura
	// @Query(value = "SELECT * FROM fac_facturas f JOIN rcb_rbocajaventa c ON
	// c.cod_rbocajaventa LIKE CONCAT('%',:numRecibo,'%') AND c.id =
	// f.id_recibo_caja ", nativeQuery = true)
	// public ArrayList<Factura> findCodRbocaja(@Param("numRecibo") String
	// numRecibo);

	// @Query(value = "SELECT * FROM rcb_rbocajaventa c where c.cod_rbocajaventa
	// LIKE '%:numRecibo%'", nativeQuery = true)
	// public ReciboCajaVenta findCodRbocaja(@Param("numRecibo") String numRecibo);

	@Query(value = "SELECT * FROM  fac_facturas GROUP BY nid_cliente ORDER BY count(*) desc LIMIT 50", nativeQuery = true)
	public ArrayList<Factura> obtenerClientesFrecuentes();

	@Query(value = "SELECT * FROM  fac_facturas GROUP BY nid_cliente ORDER BY ifnull(sum(m_total),0) desc LIMIT 50", nativeQuery = true)
	public ArrayList<Factura> obtenerClientesEspeciales();

	@Query(value = "select * from fac_facturas where nid_cliente = ?1 and cod_estado_con = 5  and id_sede = ?2", nativeQuery = true)
	public List<Factura> obtenerFacturasPorPagar(Integer idCliente, Integer idSede);

	@Query(value = "select ifnull(sum(m_total),0) from fac_facturas where nid_cliente = ?1 and cod_estado_con in(2,5) and id_sede = ?2", nativeQuery = true)
	public Integer obtenerTotalFacturasPorPagar(Integer idCliente, Integer idSede);

	@Query(value = "select * from fac_facturas where nid_cliente = ?1 and nid_pyme_tipo_venta = 15 and cod_estado_con in (2,5) and id_sede =?2", nativeQuery = true)
	public List<Factura> obtenerFacturasCredito2(Integer idCliente, Integer idSede);

	@Query(value = "select * from fac_facturas where nid_cliente = ? and nid_pyme_tipo_venta = 15 and cod_estado_con in (2,5)", nativeQuery = true)
	public List<Factura> obtenerFacturasCredito(Integer idCliente);

	@Query(value = "select * from fac_facturas where id_sede=?1 and DATEDIFF(d_fecha_vencimiento_cr,CURDATE()) < 0 and cod_estado_con in (2,5)", nativeQuery = true)
	public List<Factura> listarinformeFacturasVencidasClientePaginado(Integer idSede);

	@Query(value = "SELECT * FROM fac_facturas WHERE id_sede=?1 AND nid_cliente=?2 AND cod_estado_con=5", nativeQuery = true)
	public List<Factura> findByIdSedeCliente(Integer idSede, Integer idCliente);

	@Query(value = "SELECT * FROM fac_facturas WHERE n_nro_factura = :numeroFactura", nativeQuery = true)
	public Factura findByNumeroDocumento(String numeroFactura);

	@Query(value = "SELECT * FROM fac_facturas WHERE nid_cliente = :idClie", nativeQuery = true)
	public List<Factura> busarPorCliente(Integer idClie);

	@Query(value = "SELECT * FROM fac_facturas WHERE nid_cliente = :idClie AND "
			+ "d_fecha_venta BETWEEN :fechaInicial AND :fechaFinal", nativeQuery = true)
	public List<Factura> busarPorClienteYFecha(Integer idClie, Date fechaInicial, Date fechaFinal);

	@Query(value = "select * from fac_facturas where id_sede=?1 and cod_estado_con in(2,5)", nativeQuery = true)
	public List<Factura> listarCarteraCliente(Integer idSede);

	@Query(value = "select * from fac_factura where AND id_sede=?1 and DATEDIFF(d_fecha_vencimiento_cr,CURDATE()) < 0", nativeQuery = true)
	public List<Factura> listarlistarmovimientosClientePaginado(Integer idSede);

	@Query(value = "select * from fac_facturas where nid_cliente =?1 and id_sede =?2 and cod_estado_con in (5,2) and nid_pyme_tipo_venta = 15", nativeQuery = true)
	public List<Factura> FacturasDisponiblesAsignarCliente2(Integer idClie, Integer sede);

	@Query(value = "select * from fac_facturas f join prefijo p on f.t_prefijo = p.id left join asignaciones_recibo ar on concat(p.prefijo,f.n_nro_factura) = ar.numero_fact\n"
			+ "where ar.numero_fact is null and f.nid_cliente=? and f.nid_pyme_tipo_venta = 15 and f.cod_estado_con = 5", nativeQuery = true)
	public List<Factura> FacturasDisponiblesAsignarCliente(Integer idClie);

	@Query(value = "select * from fac_facturas f join prefijo p on f.t_prefijo = p.id where "
			+ "concat(p.prefijo,f.n_nro_factura) = ?1 and nid_cliente = ?2", nativeQuery = true)
	public Factura facturaPorNumero(String numeroFactura, Integer cliente);

	@Query(value = "select * from fac_facturas f join prefijo p on f.t_prefijo = p.id where "
			+ "concat(p.prefijo,f.n_nro_factura) = ?1", nativeQuery = true)
	public Factura facturaPorNumero(String numeroFactura);

	@Query(value = "select * from fac_facturas f join prefijo p on f.t_prefijo = p.id left join asignaciones_recibo ar on concat(p.prefijo,f.n_nro_factura) = ar.numero_fact\n"
			+ "where f.nid_cliente=? and f.nid_pyme_tipo_venta = 15", nativeQuery = true)
	public List<Factura> FacturasDisponiblesAsignarClienteActualizar(Integer idClie);

	@Query(value = "select * from fac_facturas f join prefijo p on f.t_prefijo = p.id left join conceptos_recibo_caja cr on concat(p.prefijo,f.n_nro_factura) = cr.nro_documento\n"
			+ "where f.nid_cliente = ?1 and f.nid_pyme_tipo_venta = 15 and f.cod_estado_con in (2,5,1) or cr.id_recibo_caja = ?2", nativeQuery = true)
	public List<Factura> FacturasCreditoActualizar(Integer idCliente, Integer idRecibo);

	// @Query(value = "SELECT * FROM fac_facturas WHERE nid_cliente=? AND
	// cod_estado_con != 3", nativeQuery = true)
	// public List<Factura> obtenerFacturasCliente(Integer idCliente);

	@Query(value = "SELECT * FROM fac_facturas WHERE nid_cliente=? order by (nid_factura) desc limit 50", nativeQuery = true)
	public List<Factura> obtenerFacturasCliente(Integer idCliente);

	/*
	 * @Query(value =
	 * "select count(*) from producto p join articulo a on p.id = a.id_producto join fac_articulos fa on a.id = fa.nid_articulo\r\n"
	 * +
	 * "join fac_facturas ff on ff.nid_factura = fa.nid_factura where p.id = ?",
	 * nativeQuery = true)
	 * public Integer obtenerNumFacturas(Integer id);
	 */

	@Query(value = "select * from fac_facturas where nid_factura =?", nativeQuery = true)
	public Factura buscarporIdFactura2(Integer id);

	@Query(value = "select count(*) from producto p join articulo a on p.id = a.id_producto join fac_articulos fa on a.id = fa.nid_articulo\r\n"
			+
			"join fac_facturas ff on ff.nid_factura = fa.nid_factura where ff.id_sede = :idSede and p.id = :id and date_format(date(d_fecha_venta),'%Y-%m-%d') BETWEEN date_format(date(:fechaInicial),'%Y-%m-%d') AND\r\n"
			+
			"date_format(date(:fechaFinal), '%Y-%m-%d')", nativeQuery = true)
	public Integer obtenerNumFacturas(Integer idSede, Integer id, String fechaInicial, String fechaFinal);

	@Query(value = "select * from fac_facturas ff join fac_articulos fa on ff.nid_factura = fa.nid_factura where fa.nid_factura = :id", nativeQuery = true)
	public Factura buscarporIdFactura(Integer id);

	@Query(value = "SELECT * FROM fac_facturas WHERE nid_cliente=? AND cod_estado_con in (2,5) and nid_pyme_tipo_venta = 15", nativeQuery = true)
	public Page<Factura> obtenerFacturasClientePendientes(Integer idCliente, Pageable page);

	@Query(value = "SELECT * FROM fac_facturas WHERE nid_cliente=? AND cod_estado_con in (2,5) and nid_pyme_tipo_venta = 15", nativeQuery = true)
	public List<Factura> obtenerFacturasClientePendientesAsignaciones(Integer idCliente);

	@Query(value = "select * from fac_articulos fa \r\n" +
			"join fac_facturas ff on ff.nid_factura = fa.nid_factura where ff.id_sede = :idSede and fa.nid_articulo = :idArticulo", nativeQuery = true)
	public List<Factura> buscarPorIdArticulo(Integer idSede, Integer idArticulo);

	// ------------------------------------------------------------------

	@Query(value = "SELECT * FROM fac_facturas AS f where f.id_sede = :idSede ORDER BY f.n_nro_factura DESC", nativeQuery = true)
	public Page<Factura> findByTodoRemisiones(@Param("idSede") Integer idSede, Pageable pageConfig);

	// @Query(value = "SELECT * FROM fac_facturas AS f ORDER BY n_nro_factura DESC",
	// nativeQuery = true)
	// public Page<Factura> findByTodoRemisiones(@Param("ord") String order,
	// Pageable pageConfig);

	@Query(value = "SELECT sum(f.m_total) FROM fac_facturas AS f where f.id_sede = :idSede ", nativeQuery = true)
	public BigInteger findByPagoTotalRemisiones(@Param("idSede") Integer idSede);

	@Query(value = "select * from fac_facturas where nid_factura = :id", nativeQuery = true)
	public Factura buscarporIdFacturaConcepto(Integer id);

}
