package com.softlond.base.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.softlond.base.entity.Usuario;

@Repository
public interface UsuarioDao extends JpaRepository<Usuario, Integer> {

	
	@Query("SELECT DISTINCT usuario FROM Usuario usuario " + 
		   "INNER JOIN FETCH usuario.autoridades AS authorities " + 
		   "WHERE usuario.nombreUsuario = :nombreUsuario and usuario.activo = true")
	Usuario buscarPorNombreUsuario(@Param("nombreUsuario") String nombreUsuario);
	
	
	@Query(value = "SELECT DISTINCT u.* FROM usuario u " +
				   "INNER JOIN informacion_usuario i ON i.id_usuario = u.id and u.activo = true " +
			       "WHERE i.numero_documento = :documento",nativeQuery = true)
	Usuario buscarPorNumeroDocumento(@Param("documento") String documento);

	@Modifying
	@Transactional
	@Query(value = "INSERT into autoridades_usuario values (:userId, :authorityId) ", nativeQuery = true)
	void guardarAutoridad(@Param("userId") Integer userId, @Param("authorityId") Integer authorityId);
	
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM autoridades_usuario WHERE id_usuario = :userId ", nativeQuery = true)
	void borrarAutoridad(@Param("userId") Integer userId);

}
