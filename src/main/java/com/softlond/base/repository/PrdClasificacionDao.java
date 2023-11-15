package com.softlond.base.repository;

import java.util.ArrayList;
import java.util.List;

import com.softlond.base.entity.PrdClasificaciones;
import com.softlond.base.entity.Precio;
import com.softlond.base.entity.PrdClasificaciones;
import java.util.ArrayList;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.softlond.base.entity.PrdClasificaciones;

public interface PrdClasificacionDao extends CrudRepository<PrdClasificaciones, Integer> {
	
	 @Query(value="SELECT * FROM prd_clasificaciones", nativeQuery = true)
		public ArrayList<PrdClasificaciones> obtenerClasificaciones();

	@Query(value = "select ifnull(count(*),0) from producto where id_clasificacion = ?1", nativeQuery = true)
	Integer cantidadProductos(Integer idClasificacion);
}
