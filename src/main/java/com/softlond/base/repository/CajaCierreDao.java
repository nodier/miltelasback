package com.softlond.base.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.softlond.base.entity.CajaCierre;

public interface CajaCierreDao extends CrudRepository<CajaCierre, Integer> {

	
		@Query(value = "SELECT *  FROM caja_cierre WHERE id_caja =?", nativeQuery = true)
		public ArrayList<CajaCierre> obtenerCierre(Integer idCaja);
	
}
