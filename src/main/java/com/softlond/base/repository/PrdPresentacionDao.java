package com.softlond.base.repository;

import java.util.ArrayList;
import java.util.List;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.softlond.base.entity.PrdClasificaciones;
import com.softlond.base.entity.PrdColores;
import com.softlond.base.entity.PrdPresentacion;

public interface PrdPresentacionDao extends CrudRepository<PrdPresentacion, Integer> {
	
	@Query(value = "select ifnull(count(*),0) from producto where id_presentacion = ?1", nativeQuery = true)
	Integer cantidadProductos(Integer idPresentacion);
	
	 @Query(value="select * from prd_presentacion", nativeQuery = true)
		public ArrayList<PrdPresentacion> obtenerPresentaciones();
	 
	 @Query(value="SELECT * FROM prd_presentacion where id = ?1 ", nativeQuery = true)
		public List<PrdPresentacion> obtenerPresentacion(Integer idPresentacion);
	@Query(value = "select * from prd_presentacion where t_presentacion like %?1%", nativeQuery = true)
	List<PrdPresentacion> listarPresentacionTexto(String texto);
	
	@Query(value="select max(convert(id, UNSIGNED INTEGER)) from prd_colores", nativeQuery = true)
	public Integer IdPresentacion();
}
