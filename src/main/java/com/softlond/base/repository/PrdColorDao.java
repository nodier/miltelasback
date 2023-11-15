package com.softlond.base.repository;

import java.util.ArrayList;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.softlond.base.entity.PrdColores;
import com.softlond.base.entity.Producto;

public interface PrdColorDao extends CrudRepository<PrdColores, Integer> {
	
	@Query(value = "select ifnull(count(*),0) from producto where id_color = ?1", nativeQuery = true)
	Integer cantidadProductos(Integer idColor);
	
	 @Query(value="select * from prd_colores", nativeQuery = true)
		public ArrayList<PrdColores> obtenerColores();
	@Query(value = "select * from prd_colores where t_color like %?1%", nativeQuery = true)
	List<PrdColores> listaColoresTexto(String texto);
	
	@Query(value="select max(convert(id, UNSIGNED INTEGER)) from prd_colores", nativeQuery = true)
	public Integer IdColor();
	

	
}
