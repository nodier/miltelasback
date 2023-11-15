package com.softlond.base.repository;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softlond.base.entity.Caja;
import com.softlond.base.entity.Organizacion;


public interface CajaDao extends CrudRepository<Caja, Integer>{

public Optional<Caja> findById(Integer id);
	
	@Query(value = "select c.* from cajas c JOIN organizacion o on o.id = c.sede_id where o.id=?1", nativeQuery = true)
	public Iterable<Caja> findMyCajas(Integer sedeId);
	
	@Query(value = "select c.* from cajas c JOIN organizacion o on o.id = c.sede_id", nativeQuery = true)
	public Iterable<Caja> findMyCajas();
	
	@Modifying
	@Query(value = "insert into cajas (nombre, sede_id) values (:nombre,:sede_id)", nativeQuery = true)
	public void saveCaja(@Param("nombre")String nombre, @Param("sede_id")Integer sedeId);
	
	@Query(value = "delete from cajas c where c.id=?1", nativeQuery = true)
	public void deleteAllMyCajas(Integer id);
	
	@Query(value = "SELECT *  FROM cajas WHERE  sede_id=?", nativeQuery = true)
	public ArrayList<Caja> obtenerCajasSede(Integer sedeID);
	
	@Query(value = "select c.* from cajas c JOIN organizacion o on o.id = c.sede_id where o.id=?2 and c.nombre=?1", nativeQuery = true)
	public Optional<Caja> findNombreAndSede(String nombre, Integer sede);
}
