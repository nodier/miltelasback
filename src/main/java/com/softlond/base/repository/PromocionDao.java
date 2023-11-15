package com.softlond.base.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.softlond.base.entity.Promocion;

public interface PromocionDao extends CrudRepository<Promocion, Integer> {

	@Query(value = "select * from prd_promociones where nombre like %?% limit 50", nativeQuery = true)
	public List<Promocion> obtenerPromocionesTextoBusqueda(String texto);

	@Query(value = "select * from prd_promociones where tipo_id = ?2 and referencia_id = ?3 and presentacion_id = ?1 and activo = 1 and id_sede=?4", nativeQuery = true)
	public Optional<Promocion> obtenerPromocion(Integer presentacion, Integer tipo, Integer referencia, Integer idSede);

	// ! se obtienen la cantidad de descuentos segun los parametros ingresados para
	// evitar crear descuentos repetidos _ para la opcion crear descuento

	@Query(value = "select count(nid_promocion) from prd_promociones d where d.tipo_id= ?1 and d.referencia_id= ?2 and presentacion_id= ?3 and id_sede=?4", nativeQuery = true)
	public Integer existeDescuentoTipoReferenciaPresentacionCreacion(Integer tipo, Integer referencia,
			Integer presentacion, Integer idSede);

	@Query(value = "select count(nid_promocion) from prd_promociones d where d.tipo_id is ?1 and d.referencia_id is ?2 and d.presentacion_id is ?3 and id_sede=?4", nativeQuery = true)
	public Integer existeDescuentoTodosCreacion(Integer tipo, Integer referencia, Integer presentacion, Integer idSede);

	@Query(value = "select count(nid_promocion) from prd_promociones d where d.tipo_id= ?1 and d.referencia_id is ?2 and d.presentacion_id is ?3 and id_sede=?4", nativeQuery = true)
	public Integer existeDescuentoReferenciaPresentacionCreacion(Integer tipo, Integer referencia, Integer presentacion,
			Integer idSede);

	@Query(value = "select count(nid_promocion) from prd_promociones d where d.tipo_id= ?1 and d.referencia_id= ?2 and d.presentacion_id is ?3 and id_sede=?4", nativeQuery = true)
	public Integer existeDescuentoPresentacionCreacion(Integer tipo, Integer referencia, Integer presentacion,
			Integer idSede);

	// ! se obtienen la cantidad de descuentos segun los parametros ingresados para
	// evitar crear descuentos repetidos _ para la opcion editar descuento

	@Query(value = "select count(nid_promocion) from prd_promociones d where d.nid_promocion <> ?1 and d.tipo_id= ?2 and d.referencia_id= ?3 and d.presentacion_id= ?4 and d.id_sede=?5", nativeQuery = true)
	public Integer existeDescuentoTipoReferenciaPresentacion(Integer id, Integer tipo, Integer referencia,
			Integer presentacion, Integer idSede);

	@Query(value = "select count(nid_promocion) from prd_promociones d where d.nid_promocion <> ?1 and d.tipo_id is ?2 and d.referencia_id is ?3 and d.presentacion_id is ?4 and id_sede=?5", nativeQuery = true)
	public Integer existeDescuentoTodos(Integer id, Integer tipo, Integer referencia, Integer presentacion,
			Integer idSede);

	@Query(value = "select count(nid_promocion) from prd_promociones d where d.nid_promocion <> ?1 and d.tipo_id= ?2 and d.referencia_id is ?3 and d.presentacion_id is ?4 and id_sede=?5", nativeQuery = true)
	public Integer existeDescuentoReferenciaPresentacion(Integer id, Integer tipo, Integer referencia,
			Integer presentacion, Integer idSede);

	@Query(value = "select count(nid_promocion) from prd_promociones d where d.nid_promocion <> ?1 and d.tipo_id= ?2 and d.referencia_id= ?3 and d.presentacion_id is ?4 and id_sede=?5", nativeQuery = true)
	public Integer existeDescuentoPresentacion(Integer id, Integer tipo, Integer referencia, Integer presentacion,
			Integer idSede);

	// ! se obtienen los valores de los descuentos para poder hacer las
	// comparaciones por niveles de prioridad en la creacion

	@Query(value = "select max(d.descuento) from prd_promociones d where d.tipo_id= ?1 and d.referencia_id= ?2 and id_sede=?3", nativeQuery = true)
	public Float existeDescuentoTipoReferenciaPresentacionCreacionValor(Integer tipo, Integer referencia, Integer idSede);

	@Query(value = "select d.descuento from prd_promociones d where d.tipo_id is ?1 and d.referencia_id is ?2 and d.presentacion_id is ?3 and id_sede=?4", nativeQuery = true)
	public Float existeDescuentoTodosCreacionValor(Integer tipo, Integer referencia, Integer presentacion,
			Integer idSede);

	@Query(value = "select d.descuento from prd_promociones d where d.tipo_id= ?1 and d.referencia_id is ?2 and d.presentacion_id is ?3 and id_sede=?4", nativeQuery = true)
	public Float existeDescuentoReferenciaPresentacionCreacionValor(Integer tipo, Integer referencia,
			Integer presentacion, Integer idSede);

	@Query(value = "select d.descuento from prd_promociones d where d.tipo_id= ?1 and d.referencia_id= ?2 and d.presentacion_id is ?3 and id_sede=?4", nativeQuery = true)
	public Float existeDescuentoPresentacionCreacionValor(Integer tipo, Integer referencia, Integer presentacion,
			Integer idSede);

	@Query(value = "select d.descuento from prd_promociones d where d.tipo_id= ?1 and d.presentacion_id is ?2 and id_sede=?3", nativeQuery = true)
	public Float existeDescuentoNPresentacionCreacionValor(Integer tipo, Integer presentacion, Integer idSede);
	// ! valor del descuento menor

	@Query(value = "select min(d.descuento) from prd_promociones d where id_sede=?1", nativeQuery = true)
	public Float descuentoMenorCreacionValor(Integer idSede);

	// ---------------------------------- en la edicion

	// ! se obtienen los valores de los descuentos para poder hacer las
	// comparaciones por niveles de prioridad en la creacion

	@Query(value = "select max(d.descuento) from prd_promociones d where d.nid_promocion <> ?1 and d.tipo_id= ?2 and d.referencia_id= ?3 and id_sede=?4", nativeQuery = true)
	public Float existeDescuentoTipoReferenciaPresentacionEdicionValor(Integer id, Integer tipo, Integer referencia,
			Integer idSede);

	@Query(value = "select d.descuento from prd_promociones d where d.nid_promocion <> ?1 and d.tipo_id is ?2 and d.referencia_id is ?3 and d.presentacion_id is ?4 and id_sede=?5", nativeQuery = true)
	public Float existeDescuentoTodosEdicionValor(Integer id, Integer tipo, Integer referencia, Integer presentacion,
			Integer idSede);

	@Query(value = "select d.descuento from prd_promociones d where d.nid_promocion <> ?1 and d.tipo_id= ?2 and d.referencia_id is ?3 and d.presentacion_id is ?4 and id_sede=?5", nativeQuery = true)
	public Float existeDescuentoReferenciaPresentacionEdicionValor(Integer id, Integer tipo, Integer referencia,
			Integer presentacion, Integer idSede);

	@Query(value = "select d.descuento from prd_promociones d where d.nid_promocion <> ?1 and d.tipo_id= ?2 and d.referencia_id= ?3 and d.presentacion_id is ?4 and id_sede=?5", nativeQuery = true)
	public Float existeDescuentoPresentacionEdicionValor(Integer id, Integer tipo, Integer referencia,
			Integer presentacion, Integer idSede); // si presentacion es nulo

	@Query(value = "select d.descuento from prd_promociones d where d.nid_promocion <> ?1 and d.tipo_id= ?2 and d.presentacion_id is ?3 and id_sede=?4", nativeQuery = true)
	public Float existeDescuentoNPresentacionEdicionValor(Integer id, Integer tipo, Integer presentacion, Integer idSede);

	// ! valor del descuento menor

	@Query(value = "select min(d.descuento) from prd_promociones d where d.nid_promocion <> ?1 and id_sede=?2", nativeQuery = true)
	public Float descuentoMenorEdicionValor(Integer id, Integer idSede);
}
