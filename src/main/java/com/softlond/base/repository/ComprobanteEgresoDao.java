package com.softlond.base.repository;

import com.softlond.base.entity.ComprobanteEgreso;
import com.softlond.base.entity.ReciboCajaVenta;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

@Transactional
public interface ComprobanteEgresoDao extends CrudRepository<ComprobanteEgreso, Integer> {

	@Query(value = "select * from comprobante_egreso ce join rbe_recibo_egreso_conceptos cre on ce.id = cre.nid_rbo_egreso where cre.numero_documento = ?", nativeQuery = true)
	public Page<ComprobanteEgreso> obtenerComprobantesPorNumero(String numero, Pageable page);

	// @Query(value = "select count(*) from comprobante_egreso ce join
	// rbe_recibo_egreso_conceptos cre on ce.id = cre.nid_rbo_egreso where
	// cre.numero_documento = ?", nativeQuery = true)
	// public Integer obtenerComprobantesPorNumeroC(String numero);

	@Query(value = "select count(*) from comprobante_egreso cre where cre.numero_documento = ?1 and cre.id_sede=?2", nativeQuery = true)
	public Integer obtenerComprobantesPorNumeroC(Integer numero, Integer idSede);

	@Query(value = "select id from comprobante_egreso cre where cre.numero_documento = ?1 and cre.id_sede=?2 and cod_estado_con=2", nativeQuery = true)
	public List<Integer> obtenerComprobantesPendientesPorNumeroC(Integer numero, Integer idSede);

	// eliminarComprobantesPendientesRepetidos(comprobante.getNumeroDocumento(),
	// sede.getId())
	@Modifying
	@Query(value = "delete from comprobante_egreso where id =?", nativeQuery = true)
	public void eliminarComprobantesPendientesRepetidosPorId(Integer idComprobante);

	@Query(value = "select id from comprobante_egreso cre where cre.numero_documento = ?1 and cre.id_sede=?2", nativeQuery = true)
	public Integer obtenerIdComprobante(String numero, Integer idSede);

	@Query(value = "select * from comprobante_egreso cre where cre.id= ?1", nativeQuery = true)
	public ComprobanteEgreso obtenerComprobantePorId(Integer numero);

	@Query(value = "select IFNULL(sum(t_valor_abono),0) from comprobante_egreso ce join rbe_recibo_egreso_conceptos cre on ce.id = cre.nid_rbo_egreso where cre.numero_documento = ?", nativeQuery = true)
	public Integer obtenerValorAbonos(String numero);

	@Query(value = "select IFNULL(sum(n.total),0) from asignaciones_comprobante a join nota_credito n on a.nota_credito_id = n.id where a.numero_fact = ?", nativeQuery = true)
	public Integer obtenerValorAsignacion(String numero);

	@Query(value = "select * from comprobante_egreso ce join asignaciones_comprobante ac on ce.id_comprobante = ac.id join nota_credito nc on ac.nota_credito_id = nc.id\r\n"
			+ "join prefijo p on nc.prefijo = p.id where concat(p.prefijo,nc.numero_documento) = ?", nativeQuery = true)
	public Page<ComprobanteEgreso> obtenerComprobantesPorNumeroNotaCredito(String numero, Pageable page);

	@Query(value = "select * from comprobante_egreso where concat(prefijo,numero_documento) = ?", nativeQuery = true)
	public Page<ComprobanteEgreso> obtenerComprobantesGastosServicios(String numero, Pageable page);

	@Query(value = "select * from comprobante_egreso c join rbe_recibo_egreso_conceptos cc on c.id = cc.nid_rbo_egreso where cc.nid_concepto = ?", nativeQuery = true)
	public ComprobanteEgreso obtenerComprobante(Integer idConcepto);

	@Query(value = "select * from comprobante_egreso ce join asignaciones_comprobante ac on ce.id_comprobante = ac.id where ac.numero_fact = ?1", nativeQuery = true)
	public ComprobanteEgreso obtenerComprobantesNumeroAsignacion(String numeroComprobante);

	@Query(value = "select IFNULL(total,0) from comprobante_egreso where concat(prefijo,numero_documento)= ?", nativeQuery = true)
	public Integer obtenerValorComprobante(String numero);

	@Query(value = "select IFNULL(sum(total),0) from comprobante_egreso ce where ce.id_proveedor = ?1 and ce.tipo_documento = 'Proveedores' and ce.id_sede= ?2", nativeQuery = true)
	public Integer obtenerValorAbonosProveedor(Integer proveedor, Integer idSede);

	@Query(value = "select IFNULL(sum(cre.descuento),0) from comprobante_egreso ce join rbe_recibo_egreso_conceptos cre on ce.id = cre.nid_rbo_egreso where ce.id_proveedor = ?1 and ce.tipo_documento = 'Proveedores' and ce.id_sede= ?2", nativeQuery = true)
	public Integer obtenerValorDescuentosProveedor(Integer proveedor, Integer idSede);

	@Query(value = "select IFNULL(sum(rc.t_valor_abono),0) from comprobante_egreso ce join rbe_recibo_egreso_conceptos rc on\r\n"
			+ "ce.id = rc.nid_rbo_egreso join fc_factura_compra fc on fc.t_nro_factura = rc.numero_documento\r\n"
			+ "where ce.id_sede = ? and ce.tipo_documento = 'Proveedores' and DATEDIFF(fc.d_fecha_vencimiento,CURDATE()) < 0 and fc.cod_estado_con = 5", nativeQuery = true)
	public Integer obtenerValorAbonosSede(Integer idSede);

	@Query(value = "select IFNULL(sum(rc.descuento),0) from comprobante_egreso ce join rbe_recibo_egreso_conceptos rc on\r\n"
			+ "ce.id = rc.nid_rbo_egreso join fc_factura_compra fc on fc.t_nro_factura = rc.numero_documento\r\n"
			+ "where ce.id_sede = ? and ce.tipo_documento = 'Proveedores' and fc.cod_estado_con = 5", nativeQuery = true)
	public Integer obtenerValorDescuentosSede(Integer idSede);

	@Query(value = "select  ifnull(sum(nc.total),0) from comprobante_egreso ce join rbe_recibo_egreso_conceptos cre on ce.id = cre.nid_rbo_egreso\r\n"
			+ "join asignaciones_comprobante asi on ce.id_comprobante = asi.id join nota_credito nc on asi.nota_credito_id = nc.id\r\n"
			+ "join fc_factura_compra fc on fc.t_nro_factura = cre.numero_documento\r\n"
			+ "where ce.id_sede = ? and ce.tipo_documento = 'Proveedores' and DATEDIFF(fc.d_fecha_vencimiento,CURDATE()) < 0 and fc.cod_estado_con = 5", nativeQuery = true)
	public Integer obtenerValorAsignacionSede(Integer idSede);

	@Query(value = "SELECT * FROM comprobante_egreso where id_proveedor = ?1 and date_format(date(fecha_documento),'%Y-%m-%d') between date(?2) and date(?3) and id_sede=?4", nativeQuery = true)
	public List<ComprobanteEgreso> obtenerComprobantesFiltros(int idProveedor, Date fechaInicial, Date fechaFinal,
			Integer idSede);

	@Query(value = "select * from comprobante_egreso where month(fecha_documento) = month(CURRENT_DATE()) and year(fecha_documento) = year(CURRENT_DATE()) and id_sede=?1 order by numero_documento desc", nativeQuery = true)
	public Page<ComprobanteEgreso> obtenerComprobantesPorMes(Integer idSede, Pageable page);

	@Query(value = "select ifnull(sum(total),0) from comprobante_egreso where month(fecha_documento) = month(CURRENT_DATE()) and year(fecha_documento) = year(CURRENT_DATE()) and id_sede=?1", nativeQuery = true)
	public Integer obtenerTotalComprobantesPorMes(Integer idSede);

	@Query(value = "select IFNULL(sum(rc.t_valor_abono),0) from comprobante_egreso ce join rbe_recibo_egreso_conceptos rc on\r\n"
			+ "ce.id = rc.nid_rbo_egreso\r\n"
			+ "where ce.id_sede = ? and ce.tipo_documento = 'Proveedores'", nativeQuery = true)
	public Integer obtenerValorAbonosSedeCuentasPorPagar(Integer idSede);

	@Query(value = "select IFNULL(sum(rc.t_valor_abono),0) from comprobante_egreso ce\r\n"
			+ "join rbe_recibo_egreso_conceptos rc on\r\n"
			+ "ce.id = rc.nid_rbo_egreso join fc_factura_compra fc on fc.t_nro_factura = rc.numero_documento\r\n"
			+ "where ce.id_proveedor = ?1 and ce.tipo_documento = 'Proveedores' and fc.cod_estado_con = 4 and ce.id_sede = ?2", nativeQuery = true)
	public Integer obtenerValorAbonosProveedorFacturasPagadas(Integer proveedor, Integer idSede);

	@Query(value = "select IFNULL(sum(rc.descuento),0) from comprobante_egreso ce\r\n"
			+ "join rbe_recibo_egreso_conceptos rc on\r\n"
			+ "ce.id = rc.nid_rbo_egreso join fc_factura_compra fc on fc.t_nro_factura = rc.numero_documento\r\n"
			+ "where ce.id_proveedor = ?1 and ce.tipo_documento = 'Proveedores' and fc.cod_estado_con = 4 and ce.id_sede= ?2", nativeQuery = true)
	public Integer obtenerValorDescuentosProveedorFacturasPagadas(Integer proveedor, Integer idSede);

	@Query(value = "select IFNULL(sum(rc.t_valor_abono),0) from comprobante_egreso ce join rbe_recibo_egreso_conceptos rc on\r\n"
			+ "ce.id = rc.nid_rbo_egreso join fc_factura_compra fc on fc.t_nro_factura = rc.numero_documento\r\n"
			+ "where ce.id_sede = ?1 and ce.tipo_documento = 'Proveedores' and fc.cod_estado_con = 4 and fc.id_sede = ?1", nativeQuery = true)
	public Integer obtenerValorAbonosSedeCuentasPorPagarPagados(Integer idSede);

	@Query(value = "select IFNULL(sum(rc.descuento),0) from comprobante_egreso ce join rbe_recibo_egreso_conceptos rc on\r\n"
			+ "ce.id = rc.nid_rbo_egreso join fc_factura_compra fc on fc.t_nro_factura = rc.numero_documento\r\n"
			+ "where ce.id_sede = ? and ce.tipo_documento = 'Proveedores' and fc.cod_estado_con = 4", nativeQuery = true)
	public Integer obtenerValorDescuentosSedePagado(Integer idSede);

	@Query(value = "select IFNULL(sum(rc.t_valor_abono),0) from comprobante_egreso ce join rbe_recibo_egreso_conceptos rc on\r\n"
			+ "ce.id = rc.nid_rbo_egreso join nota_debito nd on CONCAT((select p.prefijo from prefijo p where p.id_sede=?1 and p.tipo_documento='Nota debito' and p.tipo_prefijo='Proveedores'),'-', nd.numero_documento) = rc.numero_documento\r\n"
			+ "where ce.id_sede = ?1 and ce.tipo_documento = 'Proveedores' and nd.estado_documento != 'Anulado' and nd.id_sede = ?1", nativeQuery = true)
	public Integer obtenerValorAbonosSedeNotasDebito(Integer idSede);

	@Query(value = "select IFNULL(sum(rc.descuento),0) from comprobante_egreso ce join rbe_recibo_egreso_conceptos rc on\r\n"
			+ "ce.id = rc.nid_rbo_egreso join nota_debito nd on CONCAT((select p.prefijo from prefijo p where p.id_sede=?1 and p.tipo_documento='Nota debito' and p.tipo_prefijo='Proveedores'),'-', nd.numero_documento) = rc.numero_documento\r\n"
			+ "where ce.id_sede = ?1 and ce.tipo_documento = 'Proveedores' and nd.estado_documento = 'Pendiente' and nd.id_sede = ?1", nativeQuery = true)
	public Integer obtenerValorDescuentosSedeNotaDebito(Integer idSede);

	// ------------------------------------
	// @Modifying
	// @Query(value = "update comprobante_egreso SET cod_estado_con = 4, editable
	// =?1 where id = ?2", nativeQuery = true)
	// public void editarComprobanteEgresoEditable(Boolean editable, Integer
	// idComprobante);

	@Modifying
	@Query(value = "update comprobante_egreso SET cod_estado_con = 4, editable =?1, is_restringido=?3 where id = ?2", nativeQuery = true)
	public void editarComprobanteEgresoEditable(Boolean editable, Integer idComprobante, Boolean restringido);

	@Modifying
	@Query(value = "update comprobante_egreso SET id_comprobante=?1 where id = ?2", nativeQuery = true)
	public void editarComprobanteEgreso(Integer idAsignacion, Integer idComprobante);

	@Query(value = "select is_restringido from comprobante_egreso ce where ce.id = ?1", nativeQuery = true)
	public boolean restringirEdicionComprobante(Integer idComprobante); // !

	@Query(value = "select count(*) from comprobante_egreso ce where ce.id = ?1", nativeQuery = true)
	public Integer obtenerCantidadComprobantesPorId(Integer idComprobante); // ! consulta test
}
