package com.softlond.base.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.softlond.base.entity.ArticulosRemisionCompra;
import com.softlond.base.entity.FacturaCompra;
import com.softlond.base.entity.RemisionCompra;

public interface FacturaCompraDao extends CrudRepository<FacturaCompra, Integer> {

	@Query(value = "SELECT * FROM fc_factura_compra where t_nro_factura = ?1 and id_sede = ?2", nativeQuery = true)
	public FacturaCompra findByNroFactura(String nro, Integer idSede);

	@Query(value = "SELECT t_nro_factura FROM fc_factura_compra", nativeQuery = true)
	public List<String> listarNumeroFacturas();

	@Query(value = "SELECT * FROM fc_factura_compra where id_sede = ?", nativeQuery = true)
	public List<FacturaCompra> listarFacturas(Integer idSede);

	// @Query(value = "SELECT * FROM fc_factura_compra where nid_proveedor =?1 and
	// cod_estado_con = 5 and id_sede =?2", nativeQuery = true)
	// public List<FacturaCompra> listarFacturasProveedor(Integer idProveedor,
	// Integer idSede);

	@Query(value = "SELECT * FROM fc_factura_compra where nid_proveedor =?1 and cod_estado_con in (1,2,4,5) and id_sede =?2", nativeQuery = true)
	public List<FacturaCompra> listarFacturasProveedor(Integer idProveedor, Integer idSede);

	@Query(value = "SELECT * FROM fc_factura_compra WHERE id_sede=?1 AND cod_estado_con=5", nativeQuery = true)
	public List<FacturaCompra> findByIdSede(Integer idSede);

	@Query(value = "SELECT * FROM fc_factura_compra WHERE nid_factura_compra = :idFactura", nativeQuery = true)
	public FacturaCompra findByIdFactura(Integer idFactura);

	@Query(value = "SELECT * FROM fc_factura_compra WHERE nid_proveedor = :idProv", nativeQuery = true)
	public List<FacturaCompra> busarPorProveedor(Integer idProv);

	@Query(value = "SELECT * FROM fc_factura_compra where nid_proveedor = ? and cod_estado_con = 5 order by d_fecha_factura desc", nativeQuery = true)
	public Page<FacturaCompra> obtenerFacturasPendientesProveedor(Integer idProveedor, Pageable pageable);

	@Query(value = "SELECT sum(m_total) FROM fc_factura_compra where nid_proveedor = ? and cod_estado_con = 5", nativeQuery = true)
	public Integer obtenerTotalFacturasPendientesProveedor(Integer idProveedor);

	@Query(value = "SELECT * FROM fc_factura_compra where id_sede = ? and cod_estado_con = 5", nativeQuery = true)
	public Page<FacturaCompra> obtenerFacturasPorPagarSede(Integer idSede, Pageable pageable);

	@Query(value = "SELECT * FROM fc_factura_compra WHERE t_nro_factura = :numeroFactura", nativeQuery = true)
	public FacturaCompra findByNumeroDocumento(String numeroFactura);

	@Query(value = "SELECT * FROM fc_factura_compra WHERE nid_proveedor = :idProv AND "
			+ "d_fecha_factura BETWEEN :fechaInicial AND :fechaFinal and id_sede = :idSede", nativeQuery = true)

	public List<FacturaCompra> busarPorProveedorYFecha(Integer idProv, Date fechaInicial, Date fechaFinal,
			Integer idSede);

	@Query(value = "SELECT * FROM fc_factura_compra WHERE id_sede=?1 AND nid_proveedor=?2 AND cod_estado_con=5", nativeQuery = true)
	public List<FacturaCompra> findByIdSedeProveedor(Integer idSede, Integer idProveedor);

	@Query(value = "select * from fc_factura_compra where nid_proveedor = ?1 and cod_estado_con in (2,5,1) and id_sede = ?2", nativeQuery = true)
	public List<FacturaCompra> obtenerFacturasPorPagar(Integer idProveedor, Integer idSede);

	@Query(value = "select sum(m_total) from fc_factura_compra where nid_proveedor = ?1 and cod_estado_con in (2,5,1) and id_sede = ?2", nativeQuery = true)
	public Integer obtenerTotalFacturasPorPagar(Integer idProveedor, Integer idSede);

	@Query(value = "SELECT * FROM fc_factura_compra where id_sede = ? and cod_estado_con = 5", nativeQuery = true)
	public List<FacturaCompra> obtenerFacturasPorPagarSede1(Integer idSede);

	@Query(value = "select * from fc_factura_compra where cod_estado_con =5 AND id_sede=?1", nativeQuery = true)
	public List<FacturaCompra> listarinformeFacturasVencidasCompraPaginado(Integer idSede);

	@Query(value = "select * from fc_factura_compra where month(d_fecha_factura) = month(CURRENT_DATE()) and year(d_fecha_factura) = year(CURRENT_DATE()) and id_sede=?1 order by d_fecha_factura desc", nativeQuery = true)
	public Page<FacturaCompra> obtenerFacturasDelMes(Integer idSede, Pageable pageable);

	@Query(value = "SELECT * FROM fc_factura_compra where cod_estado_con = 5 order by d_fecha_factura desc", nativeQuery = true)
	public Page<FacturaCompra> obtenerTodasFacturasPendientesProveedor(Pageable pageable);

	@Query(value = "SELECT sum(m_total) FROM fc_factura_compra where cod_estado_con in (2,5,1) order by d_fecha_factura desc", nativeQuery = true)
	public Integer obtenerSumaTodasFacturasPendientesProveedor();

	@Query(value = "select sum(m_total) from fc_factura_compra where id_sede=? and cod_estado_con = 5", nativeQuery = true)
	public Integer obtenerTotalFacturasPorPagarSede(Integer idSede);

	@Query(value = "SELECT * FROM fc_factura_compra where t_nro_factura = ?1 and cod_estado_con not in (3)", nativeQuery = true)
	public List<FacturaCompra> obtenerFacturasAnular(String numero);

	@Query(value = "select * from fc_factura_compra fc left join asignaciones_comprobante ac on fc.t_nro_factura = ac.numero_fact\n"
			+ "where ac.numero_fact is null and fc.nid_proveedor=?1 and fc.id_sede =?2 and fc.cod_estado_con in (2,5,1)", nativeQuery = true)
	public List<FacturaCompra> obtenerFacturasSinAsignarComprobante(Integer idProveedor, Integer sede);

	@Query(value = "SELECT * FROM fc_factura_compra WHERE nid_proveedor = :idProv and cod_estado_con in (2,5,1) and id_sede = :idSede", nativeQuery = true)
	public List<FacturaCompra> busarPorProveedorPendiente(Integer idProv, Integer idSede);

	@Query(value = "SELECT * FROM fc_factura_compra where t_nro_factura = ?1 and id_sede = ?2", nativeQuery = true)
	public FacturaCompra obtenerFacturaNumero(String numero, Integer idSede);

	@Query(value = "SELECT * FROM fc_factura_compra fc left join rbe_recibo_egreso_conceptos rc on fc.t_nro_factura = rc.numero_documento WHERE nid_proveedor = ?1"
			+
			" and fc.id_sede = ?3 and rc.nid_rbo_egreso=?2", nativeQuery = true)
	public List<FacturaCompra> busarPorProveedorPendienteActualizar(Integer idProv, Integer idComprobante,
			Integer idSede);

	@Query(value = "select * from fc_factura_compra fc left join asignaciones_comprobante ac on fc.t_nro_factura = ac.numero_fact\n"
			+ "where fc.nid_proveedor=?1 and fc.id_sede =?2 and fc.cod_estado_con in (2,5,1)", nativeQuery = true)
	public List<FacturaCompra> obtenerFacturasSinAsignarComprobanteActualizar(Integer idProveedor, Integer sede);

	@Query(value = "SELECT m_subtotal_compras FROM fc_factura_compra where t_nro_factura = ?1 and id_sede = ?2", nativeQuery = true)
	public Double obtenerFacturaSubtotal(String numero, Integer idSede);

	@Query(value = "SELECT nid_fcremision FROM fc_factura_remisiones where nid_factura_compra = ?1", nativeQuery = true)
	public Integer obtenerFacturaIdRemisionCompra(Integer numero);

	@Query(value = "select * from articulos_remision_compra where nid_fcremision = ?1", nativeQuery = true)
	public List<ArticulosRemisionCompra> obtenerArticulosRemisionCompra(Integer idRemisionCompra);
}
