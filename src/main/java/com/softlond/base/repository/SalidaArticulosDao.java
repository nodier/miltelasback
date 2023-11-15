package com.softlond.base.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.softlond.base.entity.SalidaArticulos;

@Repository
@Transactional
public interface SalidaArticulosDao extends CrudRepository<SalidaArticulos, Integer>{
	
	@Query(value = "select * from salida_articulos where nid_salida = ?", nativeQuery = true)
	public List<SalidaArticulos> salidasArticulos(Integer idSalidaMercancia);
	
	@Modifying
	@Query(value = "delete from salida_articulos where nid_salida = ?", nativeQuery = true)
	public void eliminarArticulosSalida(Integer idSalidaMercancia);

}
