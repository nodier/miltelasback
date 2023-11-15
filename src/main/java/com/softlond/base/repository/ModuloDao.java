package com.softlond.base.repository;

import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softlond.base.entity.Modulo;

@Transactional
public interface ModuloDao extends CrudRepository<Modulo, Integer> {

	Modulo findByNombre(String name);
	
	ArrayList<Modulo> findAllByOrderByFechaCreacionDesc();

	@Query(value = "SELECT * FROM modulo modulo " +
				   "INNER JOIN modulos_por_plan mpp ON (modulo.id = mpp.id_modulo) " +
				   "INNER JOIN informacion_usuario iu ON (iu.id_plan_activado = mpp.id_plan) " +
				   "WHERE iu.id_usuario = :idUsuario ", nativeQuery = true)
	public ArrayList<Modulo> buscarModulosUsuario(@Param("idUsuario") Integer idUsuario);

	
	@Query(value = "SELECT * FROM modulo m " +
				   "INNER JOIN modulos_por_plan mpp ON (mpp.id_modulo = m.id) " +
				   "WHERE mpp.id_plan = :planId ", nativeQuery = true)
	public List<Modulo> buscarModulosPorPlan(@Param("planId") Long planId);

	
	@Query(value = "SELECT id_modulo FROM modulos_por_perfil " +
				   "WHERE id_perfil = :idPerfil ORDER BY indice ", nativeQuery = true)
	public List<Integer> findModuleByProfileOrder(@Param("idPerfil") Long idPerfil);

}
