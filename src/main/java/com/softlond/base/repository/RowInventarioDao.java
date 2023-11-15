package com.softlond.base.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.softlond.base.entity.RowInventario;

@Transactional
public interface RowInventarioDao extends CrudRepository<RowInventario, Integer> {

	@Query(value = "select * from row_inventario where codigo_articulo = ?2 and empresa = ?1", nativeQuery = true)
	public Optional<RowInventario> validarExistencia(Integer idSede, String codigo);

	@Query(value = "select * from row_inventario where id_inventario = ?1 order by local, sector", nativeQuery = true)
	public List<RowInventario> obtenerRowsInventario(Integer idInventario);

	@Query(value = "select (sum(cantidad_disponible * costo_unidad)) from row_inventario where id_inventario = ?1 order by local, sector", nativeQuery = true)
	public Integer Total(Integer idInventario);

	@Modifying
	@Query(value = "delete from row_inventario where empresa = ?1", nativeQuery = true)
	public void eliminarRowsEmpresa(Integer idSede);

	@Modifying
	@Query(value = "delete from row_inventario where codigo_articulo = ?1", nativeQuery = true)
	public void eliminarRowsArticulo(String codigo);

	@Query(value = "select * from row_inventario where id = ?1", nativeQuery = true)
	public RowInventario buscarRowsInventario(Integer idRow);

	@Query(value = "select * from row_inventario where empresa = ?1", nativeQuery = true)
	public List<RowInventario> consultarRowsEmpresa(Integer idSede);

	@Query(value = "select * from row_inventario where id_inventario = ?1 order by local, sector", nativeQuery = true)
	public Page<RowInventario> obtenerRowsInventarioPage(Integer idInventario, Pageable page);

	@Query(value = "select * from row_inventario where id_inventario = ?1 and local = ?2 and sector = ?3 order by local, sector", nativeQuery = true)
	public List<RowInventario> obtenerRowsInventarioFiltro(Integer idInventario, Integer local, Integer sector);

	@Query(value = "select * from row_inventario where id_inventario = ?1 and estado = 4 order by local, sector", nativeQuery = true)
	public List<RowInventario> obtenerRowsInventarioFinalizados(Integer idInventario);

	@Query(value = "select * from row_inventario where id_inventario = ?1 and local = ?2 order by local, sector", nativeQuery = true)
	public List<RowInventario> obtenerRowsInventarioFiltroLocal(Integer idInventario, Integer local);

	@Query(value = "select * from row_inventario where id_inventario = ?1 order by local, sector", nativeQuery = true)
	public Page<RowInventario> obtenerRowsInventario2(Integer idInventario, Pageable page);

	@Query(value = "select * from row_inventario where id_inventario = ?1 and local = ?2 and sector = ?3 order by local, sector", nativeQuery = true)
	public Page<RowInventario> obtenerRowsInventarioFiltro2(Integer idInventario, Integer local, Integer sector,
			Pageable page);
}
