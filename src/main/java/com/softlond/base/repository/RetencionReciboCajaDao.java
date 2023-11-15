package com.softlond.base.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.softlond.base.entity.RetencionReciboCaja;

@Transactional
public interface RetencionReciboCajaDao extends CrudRepository<RetencionReciboCaja, Integer> {

	@Modifying
	@Query(value = "DELETE FROM retencion_factura WHERE nid_factura=?", nativeQuery = true)
	public void eliminarRetencionesFactura(Integer idReciboCaja);

	@Modifying
	@Query(value = "DELETE FROM conceptos_recibo_caja WHERE	id_recibo_caja=?", nativeQuery = true)
	public void eliminarRetenciones(Integer idReciboCaja);
}
