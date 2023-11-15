package com.softlond.base.repository;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softlond.base.entity.NotaCredito;
import com.softlond.base.entity.NotaDebito;
import com.softlond.base.entity.RemisionCompra;

@Transactional
public interface NotaCreditoDao extends CrudRepository<NotaCredito, Integer> {

	@Query(value = "SELECT * FROM nota_credito WHERE id_sede= :idSede", nativeQuery = true)
	public List<NotaCredito> findByIdSede(Integer idSede);

	@Query(value = "SELECT * FROM nota_credito WHERE id_sede = :idSede AND numero_documento = :numeroDocumento ORDER BY fecha_documento DESC", nativeQuery = true)
	public Page<NotaCredito> obtenerNumeroDocumento(@Param("idSede") Integer idSede,
			@Param("numeroDocumento") String numeroDocumento, Pageable pageable);

	@Query(value = "SELECT SUM(total) FROM nota_credito WHERE id_sede = :idSede AND numero_documento = :numeroDocumento", nativeQuery = true)
	public Integer suma1(@Param("idSede") Integer idSede, @Param("numeroDocumento") String numeroDocumento);

	@Query(value = "SELECT * FROM nota_credito WHERE id_sede = :idSede AND numero_documento = :numeroDocumento AND estado_documento = :estadoDocumento ORDER BY fecha_documento DESC", nativeQuery = true)
	public Page<NotaCredito> findByNumeroDocumentoEstado(@Param("idSede") Integer idSede,
			@Param("numeroDocumento") String numeroDocumento, @Param("estadoDocumento") String estadoDocumento,
			Pageable pageable);

	@Query(value = "SELECT SUM(total) FROM nota_credito WHERE id_sede = :idSede AND numero_documento = :numeroDocumento AND estado_documento = :estadoDocumento", nativeQuery = true)
	public Integer suma2(@Param("idSede") Integer idSede, @Param("numeroDocumento") String numeroDocumento,
			@Param("estadoDocumento") String estadoDocumento);

	@Query(value = "SELECT * FROM nota_credito WHERE  id_sede = :idSede AND numero_documento = :numeroDocumento AND date_format(date(fecha_documento),'%Y-%m-%d') = date_format(date(:fechaInicial),'%Y-%m-%d') ORDER BY fecha_documento DESC", nativeQuery = true)
	public Page<NotaCredito> findByNumeroDocumentoFechaI(@Param("idSede") Integer idSede,
			@Param("numeroDocumento") String numeroDocumento, @Param("fechaInicial") String fechaInicial, Pageable pageable);

	@Query(value = "SELECT SUM(total) FROM nota_credito WHERE id_sede = :idSede AND numero_documento = :numeroDocumento AND date_format(date(fecha_documento),'%Y-%m-%d') = date_format(date(:fechaInicial),'%Y-%m-%d')", nativeQuery = true)
	public Integer suma3(@Param("idSede") Integer idSede, @Param("numeroDocumento") String numeroDocumento,
			@Param("fechaInicial") String fechaInicial);

	@Query(value = "SELECT * FROM nota_credito WHERE  id_sede = :idSede AND numero_documento = :numeroDocumento"
			+ " AND date_format(date(fecha_documento),'%Y-%m-%d') BETWEEN date_format(date(:fechaInicial),'%Y-%m-%d') AND"
			+ " date_format(date(:fechaFinal), '%Y-%m-%d') ORDER BY fecha_documento DESC", nativeQuery = true)
	public Page<NotaCredito> findByNumeroFechas(@Param("idSede") Integer idSede,
			@Param("numeroDocumento") String numeroDocumento, @Param("fechaInicial") String fechaInicial,
			@Param("fechaFinal") String fechaFinal, Pageable pageable);

	@Query(value = "SELECT SUM(total) FROM nota_credito WHERE id_sede = :idSede AND numero_documento = :numeroDocumento"
			+ "	AND date_format(date(fecha_documento),'%Y-%m-%d') BETWEEN date_format(date(:fechaInicial),'%Y-%m-%d') AND"
			+ "	date_format(date(:fechaFinal), '%Y-%m-%d') ", nativeQuery = true)
	public Integer suma4(@Param("idSede") Integer idSede, @Param("numeroDocumento") String numeroDocumento,
			@Param("fechaInicial") String fechaInicial, @Param("fechaFinal") String fechaFinal);

	@Query(value = "SELECT * FROM nota_credito WHERE  id_sede = :idSede AND date_format(date(fecha_documento),'%Y-%m-%d') BETWEEN date_format(date(:fechaInicial),'%Y-%m-%d') AND"
			+ " date_format(date(:fechaFinal), '%Y-%m-%d') ORDER BY fecha_documento DESC", nativeQuery = true)
	public Page<NotaCredito> findByFechas(@Param("idSede") Integer idSede, @Param("fechaInicial") String fechaInicial,
			@Param("fechaFinal") String fechaFinal, Pageable pageable);

	@Query(value = "SELECT SUM(total) FROM nota_credito WHERE  id_sede = :idSede"
			+ " AND date_format(date(fecha_documento),'%Y-%m-%d') BETWEEN date_format(date(:fechaInicial),'%Y-%m-%d') AND"
			+ " date_format(date(:fechaFinal), '%Y-%m-%d') ORDER BY fecha_documento DESC", nativeQuery = true)
	public Integer suma5(@Param("idSede") Integer idSede, @Param("fechaInicial") String fechaInicial,
			@Param("fechaFinal") String fechaFinal);

	@Query(value = "SELECT * FROM nota_credito WHERE  id_sede = :idSede AND estado_documento = :estadoDocumento"
			+ " AND date_format(date(fecha_documento),'%Y-%m-%d') BETWEEN date_format(date(:fechaInicial),'%Y-%m-%d') AND"
			+ " date_format(date(:fechaFinal), '%Y-%m-%d')", nativeQuery = true)
	public Page<NotaCredito> findByFechasEstado(@Param("idSede") Integer idSede,
			@Param("estadoDocumento") String estadoDocumento, @Param("fechaInicial") String fechaInicial,
			@Param("fechaFinal") String fechaFinal, Pageable pageable);

	@Query(value = "SELECT SUM(total) FROM nota_credito WHERE  id_sede = :idSede AND estado_documento = :estadoDocumento"
			+ " AND date_format(date(fecha_documento),'%Y-%m-%d') BETWEEN date_format(date(:fechaInicial),'%Y-%m-%d') AND"
			+ " date_format(date(:fechaFinal), '%Y-%m-%d') ", nativeQuery = true)
	public Integer suma6(@Param("idSede") Integer idSede, @Param("estadoDocumento") String estadoDocumento,
			@Param("fechaInicial") String fechaInicial, @Param("fechaFinal") String fechaFinal);

	@Query(value = "SELECT * FROM nota_credito WHERE  id_sede = :idSede AND date_format(date(fecha_documento),'%Y-%m-%d') = date_format(date(:fechaInicial),'%Y-%m-%d') "
			+ "ORDER BY fecha_documento DESC", nativeQuery = true)
	public Page<NotaCredito> findByFechaInicial(@Param("idSede") Integer idSede,
			@Param("fechaInicial") String fechaInicial, Pageable pageable);

	@Query(value = "SELECT SUM(total) FROM nota_credito WHERE  id_sede = :idSede AND date_format(date(fecha_documento),'%Y-%m-%d') = date_format(date(:fechaInicial),'%Y-%m-%d') ", nativeQuery = true)
	public Integer suma7(@Param("idSede") Integer idSede, @Param("fechaInicial") String fechaInicial);

	@Query(value = "SELECT * FROM nota_credito WHERE  id_sede = :idSede AND estado_documento = :estadoDocumento"
			+ " AND date_format(date(fecha_documento),'%Y-%m-%d') = date_format(date(:fechaInicial),'%Y-%m-%d')", nativeQuery = true)
	public Page<NotaCredito> findByFechaInicialEstado(@Param("idSede") Integer idSede,
			@Param("estadoDocumento") String estadoDocumento, @Param("fechaInicial") String fechaInicial, Pageable pageable);

	@Query(value = "SELECT SUM(total) FROM nota_credito WHERE  id_sede = :idSede AND estado_documento = :estadoDocumento"
			+ " AND date_format(date(fecha_documento),'%Y-%m-%d') = date_format(date(:fechaInicial),'%Y-%m-%d')", nativeQuery = true)
	public Integer suma8(@Param("idSede") Integer idSede, @Param("estadoDocumento") String estadoDocumento,
			@Param("fechaInicial") String fechaInicial);

	@Query(value = "SELECT * FROM nota_credito WHERE id_sede = ?1 AND estado_documento = ?2 ORDER BY fecha_documento DESC", nativeQuery = true)
	public Page<NotaCredito> obtenerEstadoDocumento(Integer idSede, String estadoDocumento, Pageable pageable);

	@Query(value = "SELECT SUM(total) FROM nota_credito WHERE id_sede = ?1 AND estado_documento = ?2 ", nativeQuery = true)
	public Integer suma9(Integer idSede, String estadoDocumento);

	// -----------------

	@Query(value = "SELECT * FROM nota_credito WHERE id_sede= ?1 AND numero_documento= ?2", nativeQuery = true)
	public NotaCredito findByIdSedeNumero(Integer idSede, String numeroDocumento);

	// public NotaCredito findByIdNotaCredito (String numeroDocumento);

	@Query(value = "SELECT * FROM nota_credito WHERE id_sede= ?1 AND estado_documento='Pendiente'", nativeQuery = true)
	public NotaCredito listarnotacredito(Integer idSede);

	@Query(value = "SELECT * FROM nota_credito WHERE id_sede=?1 AND estado_documento='Pendiente'", nativeQuery = true)
	public List<NotaCredito> listarnotacreditopendiente(Integer idSede);

	@Query(value = "SELECT * FROM nota_credito WHERE id_proveedor = :idProv AND " +
			"fecha_documento BETWEEN :fechaInicial AND :fechaFinal and id_sede = :idSede", nativeQuery = true)
	public List<NotaCredito> busarPorProveedorYFecha(Integer idProv, Date fechaInicial, Date fechaFinal, Integer idSede);

	@Query(value = "SELECT * FROM nota_credito WHERE id_proveedor = ?1 AND estado_documento = 'Pendiente' and id_sede = ?2", nativeQuery = true)
	public List<NotaCredito> obtenerNotasCreditosPorPagar(Integer idProveedor, Integer idSede);

	@Query(value = "select sum(total) from nota_credito where id_proveedor = :idProveedor and estado_documento = 'Pendiente' and id_sede = :idSede", nativeQuery = true)
	public Integer obtenerTotalNotasCreditosPorPagar(Integer idProveedor, Integer idSede);

	@Query(value = "select * from nota_credito where month(fecha_documento) = month(CURRENT_DATE()) and year(fecha_documento) = year(CURRENT_DATE()) and id_sede=?1", nativeQuery = true)
	public Page<NotaCredito> obtenerNotasCreditoDelMes(Integer idSede, Pageable pageable);

	@Query(value = "select sum(total) from nota_credito where month(fecha_documento) = month(CURRENT_DATE()) and year(fecha_documento) = year(CURRENT_DATE()) and id_sede=?1", nativeQuery = true)
	public Integer sumaNotasCreditoDelMes(Integer idSede);

	@Modifying
	@Query(value = "DELETE FROM nota_credito WHERE id = ?", nativeQuery = true)
	public void eliminarNotaCredito(Integer idSede);

	@Query(value = "select * from nota_credito where id_sede = ? and estado_documento = 'Pendiente'", nativeQuery = true)
	public List<NotaCredito> obtenerNotasCreditosPorPagarAlmacen(Integer idSede);

	@Query(value = "select sum(total) from nota_credito where id_sede = ? and estado_documento = 'Pendiente'", nativeQuery = true)
	public Integer obtenerTotalNotasCreditosPorPagarAlmacen(Integer idSede);

	@Query(value = "select * from nota_credito where numero_documento = ? and estado_documento not in ('Asignado','Anulado')", nativeQuery = true)
	public List<NotaCredito> obtenerNotasCreditoAnular(String numero);

	@Query(value = "SELECT * FROM nota_credito WHERE id_proveedor = ?1 AND estado_documento = 'Pendiente'", nativeQuery = true)
	public Page<NotaCredito> obtenerNotasCreditosPorPagarPaginado(Integer idProveedor, Pageable page);

	@Query(value = "SELECT * FROM nota_credito nc join prefijo p on nc.prefijo=p.id WHERE concat(p.prefijo,nc.numero_documento) = ?", nativeQuery = true)
	public NotaCredito obtenerPorNumero(String numeroDocumento);

	@Query(value = "SELECT * FROM nota_credito WHERE id_proveedor = ?1 AND (estado_documento = 'Pendiente' or id=?2 )", nativeQuery = true)
	public Page<NotaCredito> obtenerNotasCreditosPorPagarPaginadoActualizar(Integer idProveedor, Integer idCredito,
			Pageable page);

	@Query(value = "SELECT * FROM nota_credito WHERE id = ?1", nativeQuery = true)
	public NotaCredito obtenerNotaCreditoPorId(Integer idNotaCredito);
}
