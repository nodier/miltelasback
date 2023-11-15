package com.softlond.base.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.softlond.base.entity.DevolucionCompras;
import com.softlond.base.entity.FacturaCompra;

public interface FacturaDevolucionDao extends CrudRepository<DevolucionCompras, Integer> {

	@Query(value = "select max(n_nro_documento) from dev_compras", nativeQuery = true)
	public String Consecutivo();

	@Query(value = "SELECT * FROM dev_compras WHERE id_sede=?1 AND cod_estado_con=2", nativeQuery = true)
	public List<DevolucionCompras> findByIdSede(Integer idSede);

	@Query(value = "SELECT * FROM dev_compras WHERE id_sede=?1 AND nid_proveedor=?2 AND cod_estado_con=2", nativeQuery = true)
	public List<DevolucionCompras> findByIdSedeProveedor(Integer idSede, Integer idProveedor);

	@Query(value = "select * from dev_compras where lower(trim(n_nro_documento)) = lower(trim(?))", nativeQuery = true)
	public List<DevolucionCompras> DevolucionesAnular(String numero);

	// @Query(value="select * from dev_compras where lower(trim(n_nro_documento)) =
	// lower(trim(?)) and cod_estado_con not in (1,3)",nativeQuery=true)
	// public List<DevolucionCompras> DevolucionesAnular(String numero);

	@Query(value = "select * from dev_compras dc left join rbe_recibo_egreso_conceptos rce on dc.n_nro_documento = rce.numero_documento\r\n"
			+ "where rce.numero_documento is null and dc.nid_proveedor=?1", nativeQuery = true)
	public List<DevolucionCompras> devolucionesPendientesAsignar(Integer idProveedor);
}
