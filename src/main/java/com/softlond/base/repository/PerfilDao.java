package com.softlond.base.repository;

import java.util.ArrayList;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softlond.base.entity.Perfil;

@Transactional
public interface PerfilDao extends CrudRepository<Perfil, Integer> {

	public ArrayList<Perfil> findAllByOrderByFechaCreacionDesc();

	@Query(value = "SELECT * FROM perfil " +
				   "WHERE nombre = :nombrePerfil ", nativeQuery = true)
	public Perfil buscarPorNombre(@Param("nombrePerfil") String  nombrePerfil);

}
