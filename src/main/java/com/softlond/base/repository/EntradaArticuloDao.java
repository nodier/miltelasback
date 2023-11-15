package com.softlond.base.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.softlond.base.entity.Entrada;
import com.softlond.base.entity.EntradaArticulos;

@Repository
@Transactional
public interface EntradaArticuloDao extends CrudRepository<EntradaArticulos, Integer> {

	@Query(value = "select * from salida_mercancia where month(fecha_documento) = month(CURRENT_DATE()) and year(fecha_documento) = year(CURRENT_DATE()) and id_sede=?1", nativeQuery = true)
	public Page<Entrada> entradasMes(Integer idSede, Pageable page);

	@Query(value = "select * from salida_mercancia where numero_documento=?1", nativeQuery = true)
	public List<Entrada> entradasNumero(String numero);

	@Query(value = "SELECT * FROM entrada_articulos ea join entrada em on em.id_entrada_mercancia=ea.nid_entrada where ea.id_articulo = ?1", nativeQuery = true)
	public List<Entrada> obtenerArticuloInformeInventario(Integer idArticulo);

	@Query(value = "select * from entrada_articulos where nid_entrada = ?", nativeQuery = true)
	public List<EntradaArticulos> entradasArticulos(Integer idEntradaMercancia);

	@Modifying
	@Query(value = "delete from entrada_articulos where nid_entrada =?", nativeQuery = true)
	public void eliminarArticulosEntrada(Integer idEntradaMercancia);

}
