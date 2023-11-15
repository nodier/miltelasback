package com.softlond.base.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.softlond.base.entity.Menu;
import com.softlond.base.entity.MenusPorModulo;
import com.softlond.base.entity.Modulo;

@Transactional
public interface MenusPorModuloDao extends CrudRepository<MenusPorModulo, Integer> {

	public MenusPorModulo findByIdMenuAndIdModulo(Menu idMenu, Modulo idModulo);

	@Query(value = "SELECT id from menus_por_modulo where id_menu = :idMenu and id_modulo = :idModulo", nativeQuery = true)
	public Integer menuPorModuloExiste(@Param("idModulo") Integer idModulo, @Param("idMenu") Integer idMenu);

}