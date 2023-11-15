package com.softlond.base.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.softlond.base.entity.ProveedorCondicionesComerciales;

public interface ProveedorCondicionesComercialesDao extends CrudRepository<ProveedorCondicionesComerciales, Integer>{

	@Query(value = "SELECT * FROM ter_proveedor_condiciones_pago where nid_provedor=?1", nativeQuery = true)
	public List<ProveedorCondicionesComerciales> obtenerProductosProveedoresbyProveedor(Integer idProveedor);
}
