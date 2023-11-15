package com.softlond.base.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.softlond.base.entity.RetencionComprobanteEgreso;

@Transactional
public interface RetencionComprobanteEgresoDao extends CrudRepository<RetencionComprobanteEgreso, Integer>{
	
	@Query(value = "select * from com_comprobante_retenciones where nid_comprobante_egreso = ?", nativeQuery = true)
	public List<RetencionComprobanteEgreso> obtenerRetencionesComprobante(Integer idComprobante);
	
	@Modifying
	@Query(value = "delete from com_comprobante_retenciones where nid_comprobante_egreso = ?", nativeQuery = true)
	public void eliminarRetencion(Integer idComprobante);

}
