package com.softlond.base.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.softlond.base.entity.ClasificacionLegal;

@Transactional
@Repository
public interface ClasificacionLegalDao extends CrudRepository<ClasificacionLegal, Integer>{

	List<ClasificacionLegal> findByVisible(Boolean visible);
}
