package com.softlond.base.repository;

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

import com.softlond.base.entity.ArticulosRemisionCompra;
import com.softlond.base.entity.ReciboCajaVenta;

@Transactional
public interface ReciboCajaVentaDao extends CrudRepository<ReciboCajaVenta, Integer> {

	public ReciboCajaVenta findByCodRbocajaventa(String codRecibo);

	public Optional<ReciboCajaVenta> findById(Integer id);

	@Query(value = "SELECT * FROM rcb_rbocajaventa WHERE id_cliente=?", nativeQuery = true)
	public ReciboCajaVenta obtenerRecibo(Integer idCliente);

	@Query(value = "SELECT * FROM rcb_rbocajaventa where cod_rbocajaventa like %:numRecibo%", nativeQuery = true)
	public ArrayList<ReciboCajaVenta> findCodRbocaja(String numRecibo);

	@Query(value = "select sum(cambio) from rcb_rbocajaventa\r\n"
			+ "where id_caja=?1 and date_format(date(fecha_pago),'%Y-%m-%d') = date(?2)", nativeQuery = true)
	public Integer obtenerCambioTotalCaja(Integer idCaja, String fecha);

	@Query(value = "select sum(cambio) from rcb_rbocajaventa\r\n"
			+ "where id_sede=?1 and date_format(date(fecha_pago),'%Y-%m-%d') = date(?2)", nativeQuery = true)
	public Integer obtenerCambioTotalSede(Integer idSede, String fecha);

	@Modifying
	@Query(value = "delete FROM rcb_rbocajaventa WHERE id = ?", nativeQuery = true)
	public void eliminarRecibo(Integer idRecibo);

	@Query(value = "select * from rcb_rbocajaventa rc join asignaciones_recibo ar on ar.id = rc.id_recibo where ar.numero_fact = ?1", nativeQuery = true)
	public ReciboCajaVenta obtenerRecibosNumeroAsignacion(String numeroRecibo);

	@Query(value = "select IFNULL(sum(nc.total),0) from rcb_rbocajaventa rc join asignaciones_recibo ar on ar.id = rc.id_recibo join nota_credito_cliente nc \r\n"
			+ "on nc.id = ar.nota_credito_id where ar.numero_fact = ?1", nativeQuery = true)
	public Integer obtenerTotalAsignacionNumero(String numeroRecibo);

	@Query(value = "select max(fecha_pago) from rcb_rbocajaventa where id_cliente = ? limit 1", nativeQuery = true)
	public Date obtenerFechaUltimoPago(Integer idCliente);

	@Query(value = "select * from rcb_rbocajaventa rr join fac_facturas ff on rr.id = ff.id_recibo_caja \r\n" +
			"join fac_articulos fa on ff.nid_factura = fa.nid_factura join articulo a on a.id = fa.nid_articulo \r\n" +
			"where rr.id_sede = :idSede and fa.nid_articulo = :idArticulo", nativeQuery = true)
	public List<ReciboCajaVenta> buscarPorIdArticulo2(Integer idSede, Integer idArticulo);

}
