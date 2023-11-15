package com.softlond.base.repository;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softlond.base.entity.Factura;
import com.softlond.base.entity.RemisionCompra;

@Transactional
public interface RemisionCompraDao extends CrudRepository<RemisionCompra, Integer> {

	@Query(value = "SELECT * FROM remision_compra WHERE id_proveedor=?1 AND id=?2", nativeQuery = true)
	public RemisionCompra findByIdNumeroRemision(Integer proveedor, String idRemision);

	public RemisionCompra findByNumeroRemision(String numeroRemision);

	@Query(value = "SELECT * FROM remision_compra WHERE numero_remision = :numeroRemision", nativeQuery = true)
	public List<RemisionCompra> obtenerNumeroRemision(@Param("numeroRemision") String numeroRemision);

	@Query(value = "SELECT * FROM remision_compra WHERE id_proveedor = :idProveedor AND id_sede = :idSede ORDER BY fecha_creacion DESC", nativeQuery = true)
	public Page<RemisionCompra> findByProveedorSede(@Param("idSede") Integer idSede,
			@Param("idProveedor") Integer idProveedor, Pageable pageable);

	@Query(value = "SELECT SUM(total) FROM remision_compra WHERE id_proveedor = :idProveedor AND id_sede = :idSede", nativeQuery = true)
	public Double suma1(@Param("idSede") Integer idSede, @Param("idProveedor") Integer idProveedor);

	@Query(value = "SELECT * FROM remision_compra WHERE id_proveedor = :idProveedor AND id_sede = :idSede AND numero_remision = :numeroRemision "
			+ "ORDER BY fecha_creacion DESC", nativeQuery = true)
	public Page<RemisionCompra> findByProveedorNumeroRemision(@Param("idSede") Integer idSede,
			@Param("idProveedor") Integer idProveedor, @Param("numeroRemision") String numeroRemision,
			Pageable pageable);

	@Query(value = "SELECT SUM(total) FROM remision_compra WHERE id_proveedor = :idProveedor AND id_sede = :idSede AND numero_remision = :numeroRemision", nativeQuery = true)
	public Double suma2(@Param("idSede") Integer idSede, @Param("idProveedor") Integer idProveedor,
			@Param("numeroRemision") String numeroRemision);

	@Query(value = "SELECT * FROM remision_compra WHERE id_proveedor = :idProveedor AND id_sede = :idSede AND date_format(date(fecha_creacion),'%Y-%m-%d') = date_format(date(:fechaInicial),'%Y-%m-%d')  "
			+ "ORDER BY fecha_creacion DESC", nativeQuery = true)
	public Page<RemisionCompra> findByProveedorFechaInicial(@Param("idSede") Integer idSede,
			@Param("idProveedor") Integer idProveedor, @Param("fechaInicial") String fechaInicial, Pageable pageable);

	@Query(value = "SELECT SUM(total) FROM remision_compra WHERE id_proveedor = :idProveedor AND id_sede = :idSede AND date_format(date(fecha_creacion),'%Y-%m-%d') = date_format(date(:fechaInicial),'%Y-%m-%d')  ", nativeQuery = true)
	public Double suma3(@Param("idSede") Integer idSede, @Param("idProveedor") Integer idProveedor,
			@Param("fechaInicial") String fechaInicial);

	@Query(value = "SELECT * FROM remision_compra WHERE id_proveedor = :idProveedor AND id_sede = :idSede AND date_format(date(fecha_creacion),'%Y-%m-%d') BETWEEN date_format(date(:fechaInicial),'%Y-%m-%d') AND "
			+ " date_format(date(:fechaFinal), '%Y-%m-%d') ORDER BY fecha_creacion DESC", nativeQuery = true)
	public Page<RemisionCompra> findByProveedorFechas(@Param("idSede") Integer idSede,
			@Param("idProveedor") Integer idProveedor, @Param("fechaInicial") String fechaInicial,
			@Param("fechaFinal") String fechaFinal, Pageable pageable);

	@Query(value = "SELECT sum(total) FROM remision_compra WHERE id_proveedor = :idProveedor AND id_sede = :idSede AND date_format(date(fecha_creacion),'%Y-%m-%d') BETWEEN date_format(date(:fechaInicial),'%Y-%m-%d') AND "
			+ " date_format(date(:fechaFinal), '%Y-%m-%d')", nativeQuery = true)
	public Double suma4(@Param("idSede") Integer idSede, @Param("idProveedor") Integer idProveedor,
			@Param("fechaInicial") String fechaInicial, @Param("fechaFinal") String fechaFinal);

	@Query(value = "SELECT * FROM remision_compra WHERE  id_sede = :idSede AND numero_remision = :numeroRemision "
			+ "ORDER BY fecha_creacion DESC", nativeQuery = true)
	public Page<RemisionCompra> findNumeroRemision(@Param("idSede") Integer idSede,
			@Param("numeroRemision") String numeroRemision, Pageable pageable);

	@Query(value = "SELECT sum(total) FROM remision_compra WHERE  id_sede = :idSede AND numero_remision = :numeroRemision ", nativeQuery = true)
	public Double suma6(@Param("idSede") Integer idSede, @Param("numeroRemision") String numeroRemision);

	@Query(value = "SELECT * FROM remision_compra WHERE  id_sede = :idSede AND date_format(date(fecha_creacion),'%Y-%m-%d') = date_format(date(:fechaInicial),'%Y-%m-%d')  "
			+ "ORDER BY fecha_creacion DESC", nativeQuery = true)
	public Page<RemisionCompra> findByFechaInicial(@Param("idSede") Integer idSede,
			@Param("fechaInicial") String fechaInicial, Pageable pageable);

	@Query(value = "SELECT sum(total) FROM remision_compra WHERE  id_sede = :idSede AND date_format(date(fecha_creacion),'%Y-%m-%d') = date_format(date(:fechaInicial),'%Y-%m-%d')  ", nativeQuery = true)
	public Double suma8(@Param("idSede") Integer idSede, @Param("fechaInicial") String fechaInicial);

	@Query(value = "SELECT * FROM remision_compra WHERE  id_sede = :idSede AND date_format(date(fecha_creacion),'%Y-%m-%d') BETWEEN date_format(date(:fechaInicial),'%Y-%m-%d') AND "
			+ " date_format(date(:fechaFinal), '%Y-%m-%d') ORDER BY fecha_creacion DESC", nativeQuery = true)
	public Page<RemisionCompra> findByFechas(@Param("idSede") Integer idSede,
			@Param("fechaInicial") String fechaInicial, @Param("fechaFinal") String fechaFinal, Pageable pageable);

	@Query(value = "SELECT sum(total) FROM remision_compra WHERE  id_sede = :idSede AND date_format(date(fecha_creacion),'%Y-%m-%d') BETWEEN date_format(date(:fechaInicial),'%Y-%m-%d') AND "
			+ " date_format(date(:fechaFinal), '%Y-%m-%d')", nativeQuery = true)
	public Double suma9(@Param("idSede") Integer idSede, @Param("fechaInicial") String fechaInicial,
			@Param("fechaFinal") String fechaFinal);

	@Query(value = "SELECT * FROM remision_compra WHERE id_proveedor = :idProveedor AND id_sede = :idSede AND estado_documento = :estado "
			+ "ORDER BY fecha_creacion DESC", nativeQuery = true)
	public Page<RemisionCompra> findByProveedorEstado(@Param("idSede") Integer idSede,
			@Param("idProveedor") Integer idProveedor, @Param("estado") String estado, Pageable pageable);

	@Query(value = "SELECT sum(total) FROM remision_compra WHERE id_proveedor = :idProveedor AND id_sede = :idSede AND estado_documento = :estado ", nativeQuery = true)
	public Double suma5(@Param("idSede") Integer idSede, @Param("idProveedor") Integer idProveedor,
			@Param("estado") String estado);

	@Query(value = "SELECT * FROM remision_compra WHERE id_sede = :idSede AND numero_remision = :numeroRemision AND estado_documento = :estado "
			+ "ORDER BY fecha_creacion DESC", nativeQuery = true)
	public Page<RemisionCompra> findNumeroRemisionEstado(@Param("idSede") Integer idSede,
			@Param("numeroRemision") String numeroRemision, @Param("estado") String estado, Pageable pageable);

	@Query(value = "SELECT sum(total) FROM remision_compra WHERE id_sede = :idSede AND numero_remision = :numeroRemision AND estado_documento = :estado ", nativeQuery = true)
	public Double suma7(@Param("idSede") Integer idSede, @Param("numeroRemision") String numeroRemision,
			@Param("estado") String estado);

	@Query(value = "SELECT * FROM remision_compra WHERE  id_sede = :idSede AND estado_documento = :estado AND date_format(date(fecha_creacion),'%Y-%m-%d') = date_format(date(:fechaInicial),'%Y-%m-%d')  "
			+ "ORDER BY fecha_creacion DESC", nativeQuery = true)
	public Page<RemisionCompra> findByFechaInicialEstado(@Param("idSede") Integer idSede,
			@Param("fechaInicial") String fechaInicial, @Param("estado") String estado, Pageable pageable);

	@Query(value = "SELECT sum(total) FROM remision_compra WHERE  id_sede = :idSede AND estado_documento = :estado AND date_format(date(fecha_creacion),'%Y-%m-%d') = date_format(date(:fechaInicial),'%Y-%m-%d')  ", nativeQuery = true)
	public Double suma10(@Param("idSede") Integer idSede, @Param("fechaInicial") String fechaInicial,
			@Param("estado") String estado);

	@Query(value = "SELECT * FROM remision_compra WHERE  id_sede = :idSede AND estado_documento = :estado  "
			+ "ORDER BY fecha_creacion DESC", nativeQuery = true)
	public Page<RemisionCompra> findByEstado(@Param("idSede") Integer idSede, @Param("estado") String estado,
			Pageable pageable);

	@Query(value = "SELECT * FROM remision_compra WHERE  id_sede =:idSede ORDER BY fecha_creacion DESC", nativeQuery = true)
	public Page<RemisionCompra> findByEstadoTodos(@Param("idSede") Integer idSede, Pageable pageable);

	@Query(value = "SELECT sum(total) FROM remision_compra WHERE  id_sede = :idSede AND estado_documento = :estado  ", nativeQuery = true)
	public Double suma14(@Param("idSede") Integer idSede, @Param("estado") String estado);

	@Query(value = "SELECT sum(total) FROM remision_compra WHERE  id_sede = :idSede  ", nativeQuery = true)
	public Double suma13(@Param("idSede") Integer idSede);

	@Query(value = "SELECT * FROM remision_compra WHERE  id_sede = :idSede AND estado_documento = :estado AND date_format(date(fecha_creacion),'%Y-%m-%d') BETWEEN date_format(date(:fechaInicial),'%Y-%m-%d') AND "
			+ " date_format(date(:fechaFinal), '%Y-%m-%d') ORDER BY fecha_creacion DESC", nativeQuery = true)
	public Page<RemisionCompra> findByFechasEstado(@Param("idSede") Integer idSede,
			@Param("fechaInicial") String fechaInicial, @Param("fechaFinal") String fechaFinal,
			@Param("estado") String estado, Pageable pageable);

	@Query(value = "SELECT sum(total) FROM remision_compra WHERE  id_sede = :idSede AND estado_documento = :estado AND date_format(date(fecha_creacion),'%Y-%m-%d') BETWEEN date_format(date(:fechaInicial),'%Y-%m-%d') AND "
			+ " date_format(date(:fechaFinal), '%Y-%m-%d')", nativeQuery = true)
	public Double suma12(@Param("idSede") Integer idSede, @Param("fechaInicial") String fechaInicial,
			@Param("fechaFinal") String fechaFinal, @Param("estado") String estado);

	@Query(value = "SELECT * FROM remision_compra where estado_documento = 'Pendiente' and id_proveedor = ?1 and id_sede= ?2", nativeQuery = true)
	public List<RemisionCompra> obtenerRemisionesPendientes(Integer idProveedor, Integer idSede);

	@Query(value = "select * from remision_compra where month(fecha_creacion) = month(CURRENT_DATE()) and year(fecha_creacion) = year(CURRENT_DATE()) and id_sede=?1", nativeQuery = true)
	public Page<RemisionCompra> obtenerRemisionesDelMes(Integer idSede, Pageable pageable);

	@Query(value = "select sum(total) from remision_compra where month(fecha_creacion) = month(CURRENT_DATE()) and year(fecha_creacion) = year(CURRENT_DATE()) and id_sede=?", nativeQuery = true)
	public Double totalMes(Integer idSede);

	@Query(value = "SELECT * FROM remision_compra WHERE id_proveedor = :idProveedor AND id_sede = :idSede AND numero_remision = :numeroRemision and estado_documento = :estado "
			+ "ORDER BY fecha_creacion DESC", nativeQuery = true)
	public Page<RemisionCompra> findByProveedorNumeroRemisionEstado(@Param("idSede") Integer idSede,
			@Param("idProveedor") Integer idProveedor, @Param("numeroRemision") String numeroRemision,
			@Param("estado") String estado, Pageable pageable);

	@Query(value = "SELECT SUM(total) FROM remision_compra WHERE id_proveedor = :idProveedor AND id_sede = :idSede AND numero_remision = :numeroRemision and estado_documento = :estado", nativeQuery = true)
	public Double suma10(@Param("idSede") Integer idSede, @Param("idProveedor") Integer idProveedor,
			@Param("numeroRemision") String numeroRemision, @Param("estado") String estado);

	@Query(value = "SELECT * FROM remision_compra WHERE id_proveedor = :idProveedor AND id_sede = :idSede AND numero_remision = :numeroRemision and estado_documento = :estado "
			+ "and date_format(date(fecha_creacion),'%Y-%m-%d') BETWEEN date_format(date(:fechaInicial),'%Y-%m-%d') AND date_format(date(:fechaFinal), '%Y-%m-%d')"
			+ "ORDER BY fecha_creacion DESC", nativeQuery = true)
	public Page<RemisionCompra> findByProveedorNumeroRemisionEstadoFechas(@Param("idSede") Integer idSede,
			@Param("idProveedor") Integer idProveedor, @Param("numeroRemision") String numeroRemision,
			@Param("estado") String estado, @Param("fechaInicial") String fechaInicial,
			@Param("fechaFinal") String fechaFinal, Pageable pageable);

	@Query(value = "SELECT SUM(total) FROM remision_compra WHERE id_proveedor = :idProveedor AND id_sede = :idSede AND numero_remision = :numeroRemision and estado_documento = :estado "
			+ "and date_format(date(fecha_creacion),'%Y-%m-%d') BETWEEN date_format(date(:fechaInicial),'%Y-%m-%d') AND date_format(date(:fechaFinal), '%Y-%m-%d')", nativeQuery = true)
	public Double suma11(@Param("idSede") Integer idSede, @Param("idProveedor") Integer idProveedor,
			@Param("numeroRemision") String numeroRemision, @Param("estado") String estado,
			@Param("fechaInicial") String fechaInicial, @Param("fechaFinal") String fechaFinal);

	@Query(value = "SELECT * FROM remision_compra r join fc_factura_remisiones fr on r.id = fr.nid_fcremision where fr.nid_factura_compra = ?", nativeQuery = true)
	public List<RemisionCompra> obtenerRemisionesFactura(Integer idFactura);

	@Query(value = "SELECT * FROM remision_compra WHERE id IN (SELECT id_remision_compra FROM articulos_remision_compra WHERE id_articulo = :idArticulo)", nativeQuery = true)
	public RemisionCompra buscarPorArticulo(Integer idArticulo);

	@Query(value = "SELECT * FROM remision_compra WHERE id IN (SELECT id_remision_compra FROM articulos_remision_compra WHERE id_articulo = :idArticulo)", nativeQuery = true)
	public List<RemisionCompra> buscarPorArticuloLista(Integer idArticulo);

	// @Query(value = "SELECT id_articulo FROM articulos_remision_compra ar join
	// remision_compra rc on ar.id_remision_compra=rc.id WHERE id IN (SELECT
	// id_remision_compra FROM articulos_remision_compra WHERE id_articulo =
	// :idArticulo) LIMIT 1", nativeQuery = true)
	// public List<RemisionCompra> buscarArticuloRemision(Integer idArticulo);

	@Query(value = "SELECT * FROM remision_compra WHERE id = (SELECT id_remision_compra FROM articulos_remision_compra WHERE id_articulo IN :idsArticulo) LIMIT 1", nativeQuery = true)
	public List<RemisionCompra> buscarPorArticulos(List<Integer> idsArticulo);

	@Query(value = "SELECT * FROM remision_compra WHERE numero_remision = ?1 and id_proveedor = ?2", nativeQuery = true)
	public RemisionCompra buscarNumeroRemisionYProveedor(String numeroRemision, Integer idProveedor);

	@Query(value = "SELECT * FROM remision_compra WHERE id_proveedor = :idProveedor AND id_sede = :idSede and estado_documento = 'Pendiente' ORDER BY fecha_creacion DESC", nativeQuery = true)
	public Page<RemisionCompra> findByProveedorSedeNoAsignada(@Param("idSede") Integer idSede,
			@Param("idProveedor") Integer idProveedor, Pageable pageable);

	@Query(value = "SELECT * FROM remision_compra WHERE numero_remision like %:numeroRemision% AND id_sede = :idSede and estado_documento = 'Pendiente' ORDER BY fecha_creacion DESC", nativeQuery = true)
	public Page<RemisionCompra> findByNRemisionSedeNoAsignada(@Param("idSede") Integer idSede,
			@Param("numeroRemision") String numeroRemision, Pageable pageable);

	@Query(value = "SELECT * FROM remision_compra WHERE numero_remision like %:numeroRemision% AND id_sede = :idSede and estado_documento = 'Pendiente' and id_proveedor = :idProveedor ORDER BY fecha_creacion DESC", nativeQuery = true)
	public Page<RemisionCompra> findByNRemisionSedeAndProveedorNoAsignada(@Param("idSede") Integer idSede,
			@Param("numeroRemision") String numeroRemision, @Param("idProveedor") Integer idProveedor, Pageable pageable);

	@Query(value = "SELECT * FROM remision_compra WHERE id_sede = :idSede and estado_documento = 'Pendiente' ORDER BY fecha_creacion DESC", nativeQuery = true)
	public Page<RemisionCompra> findByRemisionSedeNoAsignada(@Param("idSede") Integer idSede, Pageable pageable);
}
