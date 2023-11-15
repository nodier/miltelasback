package com.softlond.base.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.softlond.base.entity.RetencionFactura;

@Transactional
public interface RetencionFacturaVentaDao extends CrudRepository<RetencionFactura, Integer>{
	
	@Modifying
	@Query(value="delete FROM retencion_factura where nid_factura = ?", nativeQuery = true)
	public void borrarRetencionesFactura(Integer idFacturaCompra);

	@Query(value="SELECT * FROM retencion_factura where nid_factura = ?", nativeQuery = true)
	public List<RetencionFactura> obtenerRetencionesFactura(Integer idFacturaCompra);
}
