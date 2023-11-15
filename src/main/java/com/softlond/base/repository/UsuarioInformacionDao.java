package com.softlond.base.repository;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softlond.base.entity.InformacionUsuario;
import com.softlond.base.entity.MaestroValor;
import com.softlond.base.entity.Organizacion;
import com.softlond.base.entity.Usuario;

@Transactional
public interface UsuarioInformacionDao extends CrudRepository<InformacionUsuario, Integer> {

	InformacionUsuario findByIdUsuario(Usuario id);

	@Query(value = "SELECT * FROM informacion_usuario i " +
			"INNER JOIN usuario u ON (i.id_usuario = u.id) " +
			"WHERE u.activo = true ", nativeQuery = true)
	ArrayList<InformacionUsuario> obtenerTodos();

	@Query(value = "SELECT * FROM informacion_usuario iu " +
			"INNER JOIN usuario u ON iu.id_usuario = u.id " +
			"WHERE iu.correo = :email and u.activo = true ", nativeQuery = true)
	InformacionUsuario buscarPorCorreo(String email);

	@Query(value = "SELECT * FROM informacion_usuario WHERE id_usuario = :idUsuario", nativeQuery = true)
	InformacionUsuario buscarPorIdUsuario(@Param("idUsuario") Integer idUsuario);

	@Query(value = "SELECT * FROM informacion_usuario " +
			"WHERE id = :id ", nativeQuery = true)
	InformacionUsuario buscarPorId(@Param("id") Integer id);

	ArrayList<InformacionUsuario> findByIdRolOrderByFechaCreacionAsc(MaestroValor masterValue);

	@Query(value = "SELECT * FROM informacion_usuario", nativeQuery = true)
	public List<InformacionUsuario> listarInformacionUsuario();
}
