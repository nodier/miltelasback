package com.softlond.base.repository;

import java.util.ArrayList;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.softlond.base.entity.PlazoCredito;

@Transactional
public interface PlazoCreditoDao extends CrudRepository  <PlazoCredito, Integer>{
	
public ArrayList<PlazoCredito> findAllByOrderByFechaCreacionDesc();
	
	public PlazoCredito findByNombre(String nombre);

}
