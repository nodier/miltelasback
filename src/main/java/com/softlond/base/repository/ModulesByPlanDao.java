package com.softlond.base.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.softlond.base.entity.Modulo;
import com.softlond.base.entity.ModulosPorPlan;
import com.softlond.base.entity.Plan;

@Transactional
public interface ModulesByPlanDao extends CrudRepository<ModulosPorPlan, Integer> {

	public ModulosPorPlan findByIdPlanAndIdModulo(Plan idPlan, Modulo idModulo);

	@Query(value = "SELECT id from modulos_por_plan " +
				   "WHERE id_plan = :idPlan and id_modulo = :idModulo ", nativeQuery = true)
	public Integer buscarModuloPorPlan(@Param("idPlan") Integer idPlan, @Param("idModulo") Integer idModulo);

}
