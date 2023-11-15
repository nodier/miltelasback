package com.softlond.base.repository;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.softlond.base.entity.Organizacion;

@Transactional
public interface OrganizacionDao extends CrudRepository<Organizacion, Integer>{

	public ArrayList<Organizacion> findAllByOrderByFechaCreacionDesc();
	
	public Organizacion findByIdUnico(String uniqueId);
	
	@Query(value = "select o.* from usuario_sedes u join organizacion o on u.id_organizacion=o.id where u.id_usuario=?1", nativeQuery = true)
	public List<Organizacion> obtenerSedes(Integer idUsuario);
	
	@Query(value = "SELECT * FROM organizacion AS o INNER JOIN usuario_sedes As us ON o.id = us.id_organizacion\r\n"
			+ "INNER JOIN informacion_usuario AS f ON us.id_usuario = f.id WHERE f.id_usuario =? limit 1", nativeQuery = true)
	public Organizacion obtenerSede(Integer idUsuario);
	
}
