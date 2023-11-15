package com.softlond.base.repository;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.softlond.base.entity.FormasPago;

@Transactional
public interface FormasPagoDao extends CrudRepository<FormasPago,Integer> {

	ArrayList<FormasPago> findAll();
	
	
}
