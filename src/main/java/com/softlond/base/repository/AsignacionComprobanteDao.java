package com.softlond.base.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.softlond.base.entity.AsignacionComprobante;

public interface AsignacionComprobanteDao extends CrudRepository<AsignacionComprobante, Integer> {

	@Query(value = "select IFNULL(sum(nc.total),0) from asignaciones_comprobante ac join nota_credito nc on ac.nota_credito_id = nc.id"
			+ " where ac.numero_fact = ?1 and nc.id_sede = ?2", nativeQuery = true)
	public Integer obtenerTotal(String numeroFactura, Integer idSede);

	@Query(value = "select IFNULL(sum(nc.total),0) from asignaciones_comprobante ac join nota_credito nc on ac.nota_credito_id = nc.id\r\n"
			+ "join fc_factura_compra fc on fc.t_nro_factura = ac.numero_fact \r\n"
			+ "where nc.id_proveedor = ?1 and fc.cod_estado_con = 5 and fc.id_sede = ?2", nativeQuery = true)
	public Integer obtenerTotalProveedor(Integer idProveedor, Integer idSede);
}
