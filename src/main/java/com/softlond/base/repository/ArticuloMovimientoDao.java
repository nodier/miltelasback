package com.softlond.base.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.softlond.base.entity.ArticuloMovimientos;
import com.softlond.base.entity.Traslado;

@Repository
@Transactional
public interface ArticuloMovimientoDao extends CrudRepository<ArticuloMovimientos, Integer> {

	@Query(value = "select * from articulo_movimientos where nid_movimiento = ?", nativeQuery = true)
	List<ArticuloMovimientos> obtenerMovimientosTraslado(Integer idTraslado);

	@Modifying
	@Query(value = "delete from articulo_movimientos where nid_movimiento = ?", nativeQuery = true)
	public void eliminarMovimientosTraslado(Integer idTraslado);

	@Query(value = "select * from articulo_movimientos where nid_movimiento = ?1 and id_articulo = ?2", nativeQuery = true)
	List<ArticuloMovimientos> obtenerMovimientosTrasladoArticulo(Integer idTraslado, Integer idArticulo);

	// @Query(value = "SELECT * FROM traslado t join articulo_movimientos am on
	// t.id_traslado=am.nid_movimiento where id_articulo = ?1", nativeQuery = true)
	// public List<Traslado> obtenerArticuloInformeInventario(Integer idArticulo);

	// @Query(value = "SELECT * FROM articulo_movimientos am join traslado t on
	// t.id_traslado=am.nid_movimiento where id_articulo = ?1", nativeQuery = true)
	// public List<ArticuloMovimientos> obtenerArticuloInformeInventario(Integer
	// idArticulo);

	// @Query(value = "SELECT * FROM articulo_movimientos am join traslado t on
	// t.id_traslado=am.nid_movimiento where am.id_articulo = ?1", nativeQuery =
	// true)
	// public List<Traslado> obtenerArticuloInformeInventario(Integer idArticulo);

}
