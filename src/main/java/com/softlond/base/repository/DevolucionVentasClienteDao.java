package com.softlond.base.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.softlond.base.entity.DevolucionVentasCliente;

public interface DevolucionVentasClienteDao extends CrudRepository<DevolucionVentasCliente, Integer> {

	@Query(value="SELECT * FROM dev_ventas_cliente WHERE n_nro_documento = :numero", nativeQuery = true)
	public DevolucionVentasCliente findByNroDevolucion(String numero);
}
