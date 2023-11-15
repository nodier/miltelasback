package com.softlond.base.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softlond.base.entity.InformacionUsuario;

@Transactional
public interface UsuarioSedeDao extends CrudRepository<InformacionUsuario, Integer>{
	
	@Query(value = "select distinct i.* from usuario_sedes u join informacion_usuario i on u.id_usuario=i.id", nativeQuery = true)
	public List<InformacionUsuario> obtenerUsuarios();
	
	@Modifying
	@Query(value = "INSERT into usuario_sedes values (:idUsuario, :idSede) ", nativeQuery = true)
	public void AsignarSedeAUsuario(@Param("idUsuario") Integer idUsuario, @Param("idSede") Integer idSede);
	
	@Modifying
	@Query(value = "delete from usuario_sedes where id_usuario=?1 and id_organizacion=?2", nativeQuery = true)
	public void eliminarSedeAUsuario(@Param("idUsuario") Integer idUsuario, @Param("idSede") Integer idSede);
	
}
