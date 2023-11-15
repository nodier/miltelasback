package com.softlond.base.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.softlond.base.entity.Entrada;
import com.softlond.base.entity.Traslado;

@Repository
public interface TrasladosDao extends CrudRepository<Traslado, Integer> {

	@Query(value = "select * from traslado where month(fecha_documento) = month(CURRENT_DATE()) and year(fecha_documento) = year(CURRENT_DATE()) and id_sede=?1", nativeQuery = true)
	public Page<Traslado> trasladoMes(Integer idSede, Pageable page);

	@Query(value = "select * from traslado where numero_documento=?1", nativeQuery = true)
	public List<Traslado> obtenerTrasladoNumero(String numero);

	@Query(value = "select * from traslado tr join articulo_movimientos am on tr.id_traslado = am.nid_movimiento where am.id_articulo =?1 and tr.id_sede=?2", nativeQuery = true)
	public List<Traslado> obtenerTrasladoArticulo(Integer idArticulo, Integer idSede);

	@Query(value = "SELECT * FROM articulo_movimientos am join traslado t on t.id_traslado=am.nid_movimiento where am.id_articulo = ?1", nativeQuery = true)
	public List<Traslado> obtenerArticuloInformeInventario(Integer idArticulo);

}
