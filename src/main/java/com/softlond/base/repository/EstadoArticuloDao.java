package com.softlond.base.repository;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.softlond.base.entity.EstadoArticulo;

@Transactional
public interface EstadoArticuloDao extends CrudRepository<EstadoArticulo, Integer> {

	@Query(value = "SELECT * FROM prd_estados_articulo", nativeQuery = true)
	public List<EstadoArticulo> listarEstados();

	@Query(value = "SELECT * FROM prd_estados_articulo where t_estado ='Disponible'", nativeQuery = true)
	public EstadoArticulo EstadoDisponible();

	@Query(value = "SELECT * FROM prd_estados_articulo where t_estado ='Finalizado'", nativeQuery = true)
	public EstadoArticulo EstadoFinalizado();

	@Query(value = "SELECT * FROM prd_estados_articulo where t_estado ='En Bodega'", nativeQuery = true)
	public EstadoArticulo EstadoBodega();
}