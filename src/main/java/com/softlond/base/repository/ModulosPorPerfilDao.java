package com.softlond.base.repository;

import java.util.ArrayList;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softlond.base.entity.ModulosPorPerfil;

@Transactional
public interface ModulosPorPerfilDao extends CrudRepository<ModulosPorPerfil, Integer> {

	@Query(value = "SELECT * from modulos_por_perfil " +
				   "WHERE id_perfil = :idPerfil and id_modulo = :idModulo ", nativeQuery = true)
	public ModulosPorPerfil buscarModuloDesenparejar(@Param("idPerfil") Integer idPerfil, @Param("idModulo") Integer idModulo);

	@Query(value = "SELECT id from modulos_por_perfil " +
				   "WHERE id_perfil = :idPerfil and id_modulo = :idModulo", nativeQuery = true)
	public Integer existeModuloEnPerfil(@Param("idPerfil") Integer idPerfil, @Param("idModulo") Integer idModulo);

	@Query(value = "SELECT mpp.id_modulo FROM modulos_por_perfil mpp " +
				   "INNER JOIN informacion_usuario iu ON (iu.id_perfil_activado = mpp.id_perfil) " +
			       "WHERE iu.id_usuario = :idUsuario " +
			       "ORDER BY mpp.indice ", nativeQuery = true)
	public ArrayList<Integer> obtenerOrdenadosIndice(@Param("idUsuario") Integer idUsuario);

}
