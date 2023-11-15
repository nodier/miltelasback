package com.softlond.base.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.softlond.base.entity.Barrio;

@Repository
public interface BarrioDao extends CrudRepository<Barrio, Integer>{

	@Query(value = "SELECT * FROM barrio where ciudad_id=?", nativeQuery = true)
	public List<Barrio> obtenerBarriosCiudad(Integer idCiudad);
}
