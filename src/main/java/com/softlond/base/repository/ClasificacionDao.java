package com.softlond.base.repository;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.ConfigTerClasificaciones;
import com.softlond.base.entity.Cuenta;

@Transactional
public interface ClasificacionDao extends CrudRepository<ConfigTerClasificaciones,Integer>{

	//ArrayList<ConfigTerClasificaciones> findByOrderByNombre();
	
	/*@Query(value = "SELECT * FROM cuenta where sede_id is null or sede_id = ?", nativeQuery = true)
	public List<Cuenta> obtenerCuentasCredito(Integer idSede);*/
}
