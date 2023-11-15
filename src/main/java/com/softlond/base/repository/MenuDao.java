package com.softlond.base.repository;

import java.util.ArrayList;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.softlond.base.entity.Menu;

@Transactional
public interface MenuDao extends CrudRepository<Menu, Integer> {

	public Menu findByNombre(String menuname);
	
	public ArrayList<Menu> findAllByOrderByFechaCreacionDesc();

	@Query(value = "SELECT id_menu from menus_por_modulo where id_modulo = :idModulo order by indice asc", nativeQuery = true)
	public ArrayList<Integer> obtenerMenuPorModulo(@Param("idModulo") Integer idModulo);

	@Query(value = "SELECT * FROM menu menu " +
				   "INNER JOIN menus_por_modulo mpm ON (menu.id = mpm.id_menu) " +
				   "INNER JOIN modulo modulo ON (modulo.id = mpm.id_modulo) " +
				   "INNER JOIN modulos_por_plan mpp ON (mpp.id_modulo = modulo.id) " +
				   "INNER JOIN informacion_usuario iu ON (iu.id_plan_activado = mpp.id_plan) " +
				   "WHERE iu.id_usuario = :idUsuario ", nativeQuery = true)
	public ArrayList<Menu> obtenerTodosUsuario(@Param("idUsuario") Integer idUsuario);
}
