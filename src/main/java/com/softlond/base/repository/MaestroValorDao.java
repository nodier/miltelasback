package com.softlond.base.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softlond.base.entity.ClasificacionLegal;
import com.softlond.base.entity.MaestroValor;

public interface MaestroValorDao extends CrudRepository<MaestroValor, Integer> {

	public ArrayList<MaestroValor> findByObjetivo(String objetivo);
	
	public ArrayList<MaestroValor> findByNombre(String nombre);
	
	@Query(value = "SELECT * FROM maestro_valor m " +
				   "WHERE m.objetivo = :objetivo " +
				   "ORDER BY m.nombre ", nativeQuery = true)
	ArrayList<MaestroValor> findByShoesOrderBy(@Param("objetivo") String objetivo);
	
	
	@Query(value = "SELECT * FROM master_value m " +
				   "WHERE m.objetive = :objetive " +
				   "ORDER BY m.id ", nativeQuery = true)
	ArrayList<MaestroValor> findBySizeOrderBy(@Param("objetive") String objetive);
	
	@Query(value = "select * from maestro_valor where objetivo in ('PERSONAL','EMPRESARIAL','TRIBUTARIO')", nativeQuery = true)
	public List<MaestroValor> obtenerClasificacionesLegales();
	
	@Query(value = "select * from maestro_valor where objetivo in ('TIPO_PROVEEDOR')", nativeQuery = true)
	public List<MaestroValor> obtenerTipoProveedor();
	
	// @Query(value = "select * from maestro_valor where objetivo in ('MOTIVO_SALIDA')", nativeQuery = true)
	// public List<MaestroValor> obtenerMotivosSalida();
	
	@Query(value = "select * from maestro_valor where nombre in ('salida')", nativeQuery = true)
	public List<MaestroValor> obtenerMotivosSalida();
	
	@Query(value = "select * from maestro_valor where id=?1 ", nativeQuery = true)
	public MaestroValor buscarRol(Integer id);
}
