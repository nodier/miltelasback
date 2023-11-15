package com.softlond.base.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.softlond.base.entity.Entrada;

@Repository
public interface EntradaDao extends CrudRepository<Entrada, Integer> {

	@Query(value = "select * from salida_mercancia where month(fecha_documento) = month(CURRENT_DATE()) and year(fecha_documento) = year(CURRENT_DATE()) and id_sede=?1", nativeQuery = true)
	public Page<Entrada> entradasMes(Integer idSede, Pageable page);

	@Query(value = "select * from salida_mercancia where numero_documento=?1", nativeQuery = true)
	public List<Entrada> entradasNumero(String numero);

	@Query(value = "SELECT * FROM entrada_articulos ea join entrada em on em.id_entrada_mercancia=ea.nid_entrada where ea.id_articulo = ?1", nativeQuery = true)
	public List<Entrada> obtenerArticuloInformeInventario(Integer idArticulo);
}
