package com.softlond.base.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.softlond.base.entity.FacturaRemision;

@Transactional
public interface FacturaRemisionDao extends CrudRepository<FacturaRemision, Integer>{

	
	@Query(value = "SELECT Count(*) FROM fc_factura_remisiones where nid_factura_compra = ?", nativeQuery = true)
	public Integer numeroRemisiones(Integer idFactura);
	
	@Query(value = "SELECT * FROM fc_factura_remisiones where nid_factura_compra = ?", nativeQuery = true)
	public List<FacturaRemision> obtenerRemisiones(Integer idFactura);
	
	@Modifying
	@Query(value = "Delete FROM fc_factura_remisiones where nid_factura_compra = ?", nativeQuery = true)
	public void eliminarPorFacturaCompra(Integer idFactura);
}
