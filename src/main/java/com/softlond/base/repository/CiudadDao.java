package com.softlond.base.repository;

import java.util.ArrayList;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softlond.base.entity.Ciudad;

@Transactional
public interface CiudadDao extends CrudRepository<Ciudad,Integer>{
	
	ArrayList<Ciudad> findByOrderByIdDesc();
	
	
	@Query(value = "SELECT * FROM ciudad c " +
				   "ORDER BY c.nombre", nativeQuery = true)	
	ArrayList<Ciudad> listarOrdenadasNombre();
	
	
	@Query(value = "select * from ciudad " +
				   "where id_departamento = :idDepartamento order by nombre ", nativeQuery = true)
	ArrayList<Ciudad> buscarPorDepartamento(@Param ("idDepartamento") Integer idDepartamento);
}
