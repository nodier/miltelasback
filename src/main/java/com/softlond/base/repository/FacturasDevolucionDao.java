package com.softlond.base.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.softlond.base.entity.DevolucionCompras;
import com.softlond.base.entity.DevolucionVentasCliente;

@Transactional
public interface FacturasDevolucionDao extends CrudRepository<DevolucionVentasCliente, Integer> {

	@Query(value = "select max(n_nro_documento) from dev_ventas_cliente", nativeQuery = true)
	public String Consecutivo();

	@Query(value = "SELECT * FROM dev_ventas_cliente WHERE id_sede=?1 AND cod_estado_con=2", nativeQuery = true)
	public List<DevolucionVentasCliente> findByIdSede(Integer idSede);

	@Query(value = "SELECT * FROM dev_ventas_cliente WHERE id_sede=?1 AND nid_cliente=?2 AND cod_estado_con=2", nativeQuery = true)
	public List<DevolucionVentasCliente> findByIdSedeCliente(Integer idSede, Integer idCliente);

	@Query(value = "SELECT * FROM dev_ventas_cliente WHERE nid_factura =?1 and id_sede=?2", nativeQuery = true)
	public List<DevolucionVentasCliente> findByIdFactura(Integer id, Integer sede);

	@Modifying
	@Query(value = "delete FROM dev_ventas_cliente where nid_dev_venta_cliente = ?", nativeQuery = true)
	public void eliminarDevolucion(Integer idDevolucion);

	@Query(value = "select * from dev_ventas_cliente where month(d_fecha_factura) = month(CURRENT_DATE()) and year(d_fecha_factura) = year(CURRENT_DATE()) and id_sede=?1", nativeQuery = true)
	public Page<DevolucionVentasCliente> obtenerDevolucionesDelMes(Integer idSede, Pageable pageable);

	@Query(value = "SELECT * FROM dev_ventas_cliente WHERE id_sede=?1 and n_nro_documento=?2", nativeQuery = true)
	public DevolucionVentasCliente findBynumeroDocumento(Integer idSede, String numerodocumento);

}
