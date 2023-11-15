package com.softlond.base.repository;

import java.util.ArrayList;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.softlond.base.entity.PeriodosContables;

@Transactional
public interface PeriodosContablesDao extends CrudRepository<PeriodosContables, Integer> {

	/* LLAMADA A PROCEDIMIENTO */
	@Modifying
	@Query(value = "CALL CrearPeriodoContablePorSede();", nativeQuery = true)
	void procedurePeriodoContable();

	@Query(value = "Call CrearPeriodoContablePorSede()", nativeQuery = true)
	public void ejecutarProcedure();

	public ArrayList<PeriodosContables> findAll();

	@Query(value = "SELECT * FROM pyme_con_periodos_contables WHERE id_sede= ?1 \n"
			+ "ORDER BY id DESC \n"
			+ "LIMIT 1", nativeQuery = true)
	public ArrayList<PeriodosContables> findPeriodoSede(Integer idSede);

}
