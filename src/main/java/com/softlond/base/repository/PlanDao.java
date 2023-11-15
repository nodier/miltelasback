package com.softlond.base.repository;

import java.util.ArrayList;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.softlond.base.entity.Plan;

@Transactional
@Repository
public interface PlanDao extends CrudRepository<Plan, Integer> {

	public Plan findByNombre(String nombre);

	public ArrayList<Plan> findAllByOrderByFechaCreacionDesc();
}
