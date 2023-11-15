package com.softlond.base.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.softlond.base.entity.SalidaMercancia;

@Repository
@Transactional
public interface SalidaMercanciaDao extends CrudRepository<SalidaMercancia, Integer> {

	@Query(value = "select * from salida_mercancia where month(fecha_documento) = month(CURRENT_DATE()) and year(fecha_documento) = year(CURRENT_DATE()) and id_sede=?1", nativeQuery = true)
	public Page<SalidaMercancia> salidaMercanciaMes(Integer idSede, Pageable page);

	@Query(value = "select * from salida_mercancia where numero_documento=?1", nativeQuery = true)
	public List<SalidaMercancia> salidaMercanciaNumero(String numero);

	@Query(value = "SELECT * FROM salida_articulos sa join salida_mercancia sm on sm.id_salida_mercancia=sa.nid_salida where sa.id_articulo = ?1", nativeQuery = true)
	public List<SalidaMercancia> obtenerArticuloInformeInventario(Integer idArticulo);
}
