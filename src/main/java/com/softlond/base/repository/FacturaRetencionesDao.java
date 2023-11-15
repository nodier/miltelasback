package com.softlond.base.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.softlond.base.entity.FacturaRetenciones;

@Transactional
public interface FacturaRetencionesDao extends CrudRepository<FacturaRetenciones, Integer>{
	
	@Query(value="SELECT * FROM fac_factura_retenciones where nid_factura_compra = ?", nativeQuery = true)
	public List<FacturaRetenciones> obtenerRetencionesFactura(Integer idFacturaCompra);
	
	@Modifying
	@Query(value="delete FROM fac_factura_retenciones where nid_factura_compra = ?", nativeQuery = true)
	public void borrarRetencionesFactura(Integer idFacturaCompra);
}
