package com.softlond.base.repository;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.softlond.base.entity.Clientes;
import com.softlond.base.entity.ConfigTerClasificaciones;
import com.softlond.base.entity.PrdDescuentos;
import com.softlond.base.entity.PrdPresentacion;
import com.softlond.base.entity.PrdReferencia;
import com.softlond.base.entity.PrdTipos;

@Transactional
public interface PrdDescuentosDao extends CrudRepository<PrdDescuentos, Integer> {

	@Query(value = "SELECT * FROM prd_descuentos", nativeQuery = true)
	public ArrayList<PrdDescuentos> obtenerDescuentos();

	@Query(value = "select * from prd_descuentos", nativeQuery = true)
	public Page<PrdDescuentos> obtenerTodosDescuentos(Pageable pageable);

	public PrdDescuentos findByIdClasificacion(ConfigTerClasificaciones configTerClasificaciones);

	@Query(value = "select * from prd_descuentos pd inner join config_ter_clasificaciones co on\r\n" +
			"co.id  = pd.id_clasificacion inner join clientes cl on cl.id_clasificacion = \r\n" +
			"co.id ", nativeQuery = true)
	public Page<PrdDescuentos> obtenerTodosDescuentosClientes(Pageable page);

	@Query(value = "select * from prd_descuentos d join producto p on p.id_descuento=d.id where p.id = ?2", nativeQuery = true)
	public Page<PrdDescuentos> obtenerTodosDescuentosProducto(Pageable pageable, Integer idProducto);

	// ! se obtienen la cantidad de descuentos segun los parametros ingresados para
	// evitar crear descuentos repetidos _ para la opcion crear descuento

	@Query(value = "select count(id) from prd_descuentos d where d.id_clasificacion= ?1 and d.tipo= ?2 and d.referencia= ?3 and id_presentacion= ?4", nativeQuery = true)
	public Integer existeDescuentoTipoReferenciaPresentacionCreacion(Integer clasificacion, Integer tipo,
			Integer referencia, Integer presentacion); // si ningun parametro es nulo

	@Query(value = "select count(id) from prd_descuentos d where d.id_clasificacion= ?1 and d.tipo is ?2 and d.referencia is ?3 and id_presentacion is ?4", nativeQuery = true)
	public Integer existeDescuentoTodosCreacion(Integer clasificacion, Integer tipo, Integer referencia,
			Integer presentacion); // si todos son nulos

	@Query(value = "select count(id) from prd_descuentos d where d.id_clasificacion= ?1 and d.tipo= ?2 and d.referencia is ?3 and id_presentacion is ?4", nativeQuery = true)
	public Integer existeDescuentoReferenciaPresentacionCreacion(Integer clasificacion, Integer tipo, Integer referencia,
			Integer presentacion); // si referencia y presentacion son nulos

	@Query(value = "select count(id) from prd_descuentos d where d.id_clasificacion= ?1 and d.tipo= ?2 and d.referencia= ?3 and id_presentacion is ?4", nativeQuery = true)
	public Integer existeDescuentoPresentacionCreacion(Integer clasificacion, Integer tipo, Integer referencia,
			Integer presentacion); // si presentacion es nulo

	// ! se obtienen la cantidad de descuentos segun los parametros ingresados para
	// evitar crear descuentos repetidos _ para la opcion editar descuento

	@Query(value = "select count(id) from prd_descuentos d where d.id <> ?1 and d.id_clasificacion= ?2 and d.tipo= ?3 and d.referencia= ?4 and id_presentacion= ?5", nativeQuery = true)
	public Integer existeDescuentoTipoReferenciaPresentacion(Integer id, Integer clasificacion, Integer tipo,
			Integer referencia, Integer presentacion); // si ningun parametro es nulo

	@Query(value = "select count(id) from prd_descuentos d where d.id <> ?1 and d.id_clasificacion= ?2 and d.tipo is ?3 and d.referencia is ?4 and id_presentacion is ?5", nativeQuery = true)
	public Integer existeDescuentoTodos(Integer id, Integer clasificacion, Integer tipo, Integer referencia,
			Integer presentacion); // si todos son nulos

	@Query(value = "select count(id) from prd_descuentos d where d.id <> ?1 and d.id_clasificacion= ?2 and d.tipo= ?3 and d.referencia is ?4 and id_presentacion is ?5", nativeQuery = true)
	public Integer existeDescuentoReferenciaPresentacion(Integer id, Integer clasificacion, Integer tipo,
			Integer referencia, Integer presentacion); // si referencia y presentacion son nulos

	@Query(value = "select count(id) from prd_descuentos d where d.id <> ?1 and d.id_clasificacion= ?2 and d.tipo= ?3 and d.referencia= ?4 and id_presentacion is ?5", nativeQuery = true)
	public Integer existeDescuentoPresentacion(Integer id, Integer clasificacion, Integer tipo, Integer referencia,
			Integer presentacion); // si presentacion es nulo

	// ! se obtienen los valores de los descuentos para poder hacer las
	// comparaciones por niveles de prioridad en la creacion

	@Query(value = "select max(d.descuento) from prd_descuentos d where d.id_clasificacion= ?1 and d.tipo= ?2 and d.referencia= ?3", nativeQuery = true)
	public Float existeDescuentoTipoReferenciaPresentacionCreacionValor(Integer clasificacion, Integer tipo,
			Integer referencia); // si ningun parametro es nulo

	@Query(value = "select d.descuento from prd_descuentos d where d.id_clasificacion= ?1 and d.tipo is ?2 and d.referencia is ?3 and id_presentacion is ?4", nativeQuery = true)
	public Float existeDescuentoTodosCreacionValor(Integer clasificacion, Integer tipo, Integer referencia,
			Integer presentacion); // si todos son nulos

	@Query(value = "select d.descuento from prd_descuentos d where d.id_clasificacion= ?1 and d.tipo= ?2 and d.referencia is ?3 and id_presentacion is ?4", nativeQuery = true)
	public Float existeDescuentoReferenciaPresentacionCreacionValor(Integer clasificacion, Integer tipo,
			Integer referencia, Integer presentacion); // si referencia y presentacion son nulos

	@Query(value = "select d.descuento from prd_descuentos d where d.id_clasificacion= ?1 and d.tipo= ?2 and d.referencia= ?3 and id_presentacion is ?4", nativeQuery = true)
	public Float existeDescuentoPresentacionCreacionValor(Integer clasificacion, Integer tipo, Integer referencia,
			Integer presentacion); // si presentacion es nulo

	@Query(value = "select d.descuento from prd_descuentos d where d.id_clasificacion= ?1 and d.tipo= ?2 and id_presentacion is ?3", nativeQuery = true)
	public Float existeDescuentoNPresentacionCreacionValor(Integer clasificacion, Integer tipo, Integer presentacion); // si
																																																											// presentacion
																																																											// es
																																																											// nulo

	// ! valor del descuento menor

	@Query(value = "select min(d.descuento) from prd_descuentos d where d.id_clasificacion= ?1", nativeQuery = true)
	public Float descuentoMenorCreacionValor(Integer clasificacion); // descuento de menor valor en los existentes

	// ----------------------------- en la opcion editar

	// ! se obtienen los valores de los descuentos para poder hacer las
	// comparaciones por niveles de prioridad en la edicion

	@Query(value = "select max(d.descuento) from prd_descuentos d where d.id <> ?1 and d.id_clasificacion= ?2 and d.tipo= ?3 and d.referencia= ?4", nativeQuery = true)
	public Float existeDescuentoTipoReferenciaPresentacionEdicionValor(Integer id, Integer clasificacion, Integer tipo,
			Integer referencia); // si ningun parametro es nulo

	@Query(value = "select d.descuento from prd_descuentos d where d.id <> ?1 and d.id_clasificacion= ?2 and d.tipo is ?3 and d.referencia is ?4 and id_presentacion is ?5", nativeQuery = true)
	public Float existeDescuentoTodosEdicionValor(Integer id, Integer clasificacion, Integer tipo, Integer referencia,
			Integer presentacion); // si todos son nulos

	@Query(value = "select d.descuento from prd_descuentos d where d.id <> ?1 and d.id_clasificacion= ?2 and d.tipo= ?3 and d.referencia is ?4 and id_presentacion is ?5", nativeQuery = true)
	public Float existeDescuentoReferenciaPresentacionEdicionValor(Integer id, Integer clasificacion, Integer tipo,
			Integer referencia, Integer presentacion); // si referencia y presentacion son nulos

	@Query(value = "select d.descuento from prd_descuentos d where d.id <> ?1 and d.id_clasificacion= ?2 and d.tipo= ?3 and d.referencia= ?4 and id_presentacion is ?5", nativeQuery = true)
	public Float existeDescuentoPresentacionEdicionValor(Integer id, Integer clasificacion, Integer tipo,
			Integer referencia, Integer presentacion); // si presentacion es nulo

	@Query(value = "select d.descuento from prd_descuentos d where d.id <> ?1 and d.id_clasificacion= ?2 and d.tipo= ?3 and id_presentacion is ?4", nativeQuery = true)
	public Float existeDescuentoNPresentacionEdicionValor(Integer id, Integer clasificacion, Integer tipo,
			Integer presentacion); // si presentacion es nulo

	// ! valor del descuento menor

	@Query(value = "select min(d.descuento) from prd_descuentos d where d.id <> ?1 and d.id_clasificacion= ?2", nativeQuery = true)
	public Float descuentoMenorEdicionValor(Integer id, Integer clasificacion); // descuento de menor valor en los
																																							// existentes
}
