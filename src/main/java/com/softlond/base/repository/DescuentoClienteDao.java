package com.softlond.base.repository;

import java.util.ArrayList;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.softlond.base.entity.Clientes;


	@Transactional
	public interface DescuentoClienteDao extends CrudRepository<Clientes, Integer> {
		
		@Query(value="SELECT * FROM clientes", nativeQuery = true)
		public ArrayList<Clientes> obtenerDescuentoClientes();
		
		@Query(value="SELECT * FROM clientes cl inner join config_ter_clasificaciones co on cl.id_clasificacion = co.id inner join prd_descuentos pd on pd.id_clasificacion = co.id", nativeQuery = true)
		public Page<Clientes> obtenerTodosDescuentosClientes(Pageable page);
		 
		
}
