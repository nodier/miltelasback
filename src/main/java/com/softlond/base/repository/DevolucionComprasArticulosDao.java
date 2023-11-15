package com.softlond.base.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softlond.base.entity.DevolucionArticulosCompra;

@Transactional
public interface DevolucionComprasArticulosDao extends CrudRepository<DevolucionArticulosCompra, Integer>{
	
	@Query(value = "SELECT * FROM dev_compras_articulos WHERE nid_dev_compra = :idDev", nativeQuery = true)
	public List<DevolucionArticulosCompra> findBynDevolucion(@Param("idDev") Integer idDev);
	
	
	@Query(value = "select sum(dca.n_cantidad_devuelta) from dev_compras_articulos dca inner join articulos_remision_compra arc ON \r\n" + 
			"dca.nid_fcrem_articulo = arc.id inner join articulo a on a.id = arc.id_articulo \r\n" + 
			"where a.id =?1", nativeQuery = true)
	public Double cantDevCompra(Integer id);
	
	@Query(value = "select sum(dca.m_valor_devuelto) from dev_compras_articulos dca inner join articulos_remision_compra arc ON \r\n" + 
			"dca.nid_fcrem_articulo = arc.id inner join articulo a on a.id = arc.id_articulo \r\n" + 
			"where a.id =?1", nativeQuery = true)
	public Double SumCantDevCompra(Integer id);
	
}
