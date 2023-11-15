package com.softlond.base.repository;

import com.softlond.base.entity.PagoComprobanteEgreso;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

@Transactional
public interface PagoComprobanteEgresoDao extends CrudRepository<PagoComprobanteEgreso, Integer> {

	@Query(value = "select * from pagos_comprobante_egreso where id_comprobante=?", nativeQuery = true)
	public List<PagoComprobanteEgreso> pagosComprobanteEgresos(Integer idComprobante);

	@Query(value = "select * from pagos_comprobante_egreso where id=?", nativeQuery = true)
	public PagoComprobanteEgreso pagoComprobanteEgresoPorId(Integer idPago);

	@Modifying
	@Query(value = "delete from pagos_comprobante_egreso where id_comprobante=?", nativeQuery = true)
	public void eliminarPagos(Integer idComprobante);
}
