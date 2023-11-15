package com.softlond.base.repository;

import com.softlond.base.entity.DescuentoComprobanteEgreso;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

@Transactional
public interface DescuentoComprobanteEgresoDao extends CrudRepository<DescuentoComprobanteEgreso, Integer> {
	
	@Query(value = "select * from dcto_comprobante_egreso where id_comprobante = ?", nativeQuery = true)
	public List<DescuentoComprobanteEgreso> descuentosComprobante(Integer idComprobante);
	
	@Modifying
	@Query(value = "delete from dcto_comprobante_egreso where id_comprobante = ?", nativeQuery = true)
	public void eliminarDescuento(Integer idComprobante);
}
