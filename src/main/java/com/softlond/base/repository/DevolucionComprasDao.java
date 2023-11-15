package com.softlond.base.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.softlond.base.entity.DevolucionCompras;

public interface DevolucionComprasDao extends CrudRepository<DevolucionCompras, Integer> {

	@Query(value = "SELECT * FROM dev_compras WHERE n_nro_documento = :numero", nativeQuery = true)
	public DevolucionCompras findByNroDevolucion(String numero);

	@Query(value = "SELECT * FROM dev_compras dc join dev_compras_articulos da join articulos_remision_compra ar on dc.nid_dev_compra=da.nid_dev_compra and ar.id=da.nid_fcrem_articulo WHERE ar.id_articulo = :numero", nativeQuery = true)
	public List<DevolucionCompras> obtenerDevolucionArticulo(Integer numero);
}
