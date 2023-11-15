package com.softlond.base.repository;

import com.softlond.base.entity.PrdColores;
import com.softlond.base.entity.PrdTipos;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface PrdTipoDao extends CrudRepository<PrdTipos, Integer> {
	
	@Query(value = "select ifnull(count(*),0) from producto where tipo = ?1", nativeQuery = true)
	Integer cantidadProductos(Integer idtipo);
	
	@Query(value="select * from prd_tipos", nativeQuery = true)
	public ArrayList<PrdTipos> obtenerTipo();
	@Query(value = "select * from prd_tipos where t_tipo like %?% limit 50", nativeQuery = true)
	public List<PrdTipos> obtenerTiposTexto(String texto);
	
	
	@Query(value="select max(convert(tid_tipo, UNSIGNED INTEGER)) from prd_tipos", nativeQuery = true)
	public Integer obtenerTidTipo();
}
