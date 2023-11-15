package com.softlond.base.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.softlond.base.entity.ArticuloMovimientos;
import com.softlond.base.entity.ConceptoNotaDebitoCliente;
import com.softlond.base.entity.Inventario;

@Transactional
public interface InventarioDao extends CrudRepository<Inventario, Integer> {

	@Query(value = "SELECT * FROM inventario WHERE empresa=?1", nativeQuery = true)
	public Optional<Inventario> obtenerInventario(Integer idSede);

	@Modifying
	@Query(value = "delete from inventario WHERE empresa=?1", nativeQuery = true)
	public void eliminarInventario(Integer idSede);

	@Query(value = "SELECT * FROM inventario WHERE empresa=?1", nativeQuery = true)
	public Inventario Inventario(Integer idSede);

	@Query(value = "SELECT * FROM articulo_movimientos where id_articulo = ?1", nativeQuery = true)
	public List<ArticuloMovimientos> obtenerArticuloInformeInventario(Integer idArticulo);

	// @Query(value = "SELECT * FROM articulo WHERE id_producto = :idProducto",
	// nativeQuery = true)
	// public List<Articulo> buscarPorProducto(Integer idProducto);
}
