package com.softlond.base.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softlond.base.entity.DevolucionArticulosCompra;
import com.softlond.base.entity.DevolucionArticulosVenta;

@Transactional
public interface DevolucionVentasArticulosDao extends CrudRepository<DevolucionArticulosVenta, Integer>{
	
	
	@Modifying
	@Query(value = "delete FROM dev_ventas_ventas_articulos WHERE nid_dev_compra = ?", nativeQuery = true)
	public void eliminarArticulosDevolucionIdDevolucion(Integer idDev);
	
	@Query(value = "select sum(dvva.n_cantidad_devuelta) from dev_ventas_ventas_articulos dvva inner join fac_articulos fa on\r\n" + 
			"dvva.nid_factura_articulo = fa.nid_fac_articulo inner join articulo a on a.id = fa.nid_articulo where a.id = ?1", nativeQuery = true)
	public Double cantDevVenta(Integer id);
	
	@Query(value = "SELECT * FROM dev_ventas_ventas_articulos WHERE nid_dev_compra = ?", nativeQuery = true)
	public List<DevolucionArticulosVenta> findBynDevolucion(Integer idDev);
	
}
