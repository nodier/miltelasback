package com.softlond.base.repository;

import java.util.ArrayList;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.softlond.base.entity.FormasPago;
import com.softlond.base.entity.RbcTipoTarjetas;

@Transactional
public interface TipoTarjetasDao extends CrudRepository<RbcTipoTarjetas,Integer> {

	ArrayList<RbcTipoTarjetas> findAll();
}
