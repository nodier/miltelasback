package com.softlond.base.repository;

import java.util.ArrayList;
import javax.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;

import com.softlond.base.entity.Departamento;

@Transactional
public interface DepartamentoDao extends CrudRepository<Departamento,Integer>{
	
	ArrayList<Departamento> findByOrderByNombre();
}
