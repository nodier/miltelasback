package com.softlond.base.repository;

import java.sql.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softlond.base.entity.NotaCreditoCliente;
import com.softlond.base.entity.NotaDebitoCliente;

@Transactional
public interface NotaDebitoClienteDao extends CrudRepository<NotaDebitoCliente, Integer> {

	public NotaDebitoCliente findByNumeroDocumento(String numeroDocumento);

	@Query(value = "SELECT * FROM nota_debito_cliente WHERE id_sede= :idSede", nativeQuery = true)
	public List<NotaDebitoCliente> findByIdSede(Integer idSede);

	@Query(value = "SELECT * FROM nota_debito_cliente WHERE id_sede = :idSede AND numero_documento = :numeroDocumento ORDER BY fecha_documento DESC", nativeQuery = true)
	public Page<NotaDebitoCliente> obtenerNumeroDocumento(@Param("idSede") Integer idSede,
			@Param("numeroDocumento") String numeroDocumento, Pageable pageable);

	@Query(value = "SELECT SUM(total) FROM nota_debito_cliente WHERE id_sede = :idSede AND numero_documento = :numeroDocumento", nativeQuery = true)
	public Integer suma1(@Param("idSede") Integer idSede, @Param("numeroDocumento") String numeroDocumento);

	@Query(value = "SELECT * FROM nota_debito_cliente WHERE id_sede = :idSede AND numero_documento = :numeroDocumento AND estado_documento = :estadoDocumento ORDER BY fecha_documento DESC", nativeQuery = true)
	public Page<NotaDebitoCliente> findByNumeroDocumentoEstado(@Param("idSede") Integer idSede,
			@Param("numeroDocumento") String numeroDocumento, @Param("estadoDocumento") String estadoDocumento,
			Pageable pageable);

	@Query(value = "SELECT SUM(total) FROM nota_debito_cliente WHERE id_sede = :idSede AND numero_documento = :numeroDocumento AND estado_documento = :estadoDocumento", nativeQuery = true)
	public Integer suma2(@Param("idSede") Integer idSede, @Param("numeroDocumento") String numeroDocumento,
			@Param("estadoDocumento") String estadoDocumento);

	@Query(value = "SELECT * FROM nota_debito_cliente WHERE  id_sede = :idSede AND numero_documento = :numeroDocumento AND date_format(date(fecha_documento),'%Y-%m-%d') = date_format(date(:fechaInicial),'%Y-%m-%d')  "
			+ "ORDER BY fecha_documento DESC", nativeQuery = true)
	public Page<NotaDebitoCliente> findByNumeroDocumentoFechaI(@Param("idSede") Integer idSede,
			@Param("numeroDocumento") String numeroDocumento, @Param("fechaInicial") String fechaInicial,
			Pageable pageable);

	@Query(value = "SELECT SUM(total) FROM nota_debito_cliente WHERE id_sede = :idSede AND numero_documento = :numeroDocumento AND date_format(date(fecha_documento),'%Y-%m-%d') = date_format(date(:fechaInicial),'%Y-%m-%d')", nativeQuery = true)
	public Integer suma3(@Param("idSede") Integer idSede, @Param("numeroDocumento") String numeroDocumento,
			@Param("fechaInicial") String fechaInicial);

	@Query(value = "SELECT * FROM nota_debito_cliente WHERE  id_sede = :idSede AND numero_documento = :numeroDocumento "
			+ " AND date_format(date(fecha_documento),'%Y-%m-%d') BETWEEN date_format(date(:fechaInicial),'%Y-%m-%d') AND "
			+ "  date_format(date(:fechaFinal), '%Y-%m-%d')  " + "ORDER BY fecha_documento DESC", nativeQuery = true)
	public Page<NotaDebitoCliente> findByNumeroFechas(@Param("idSede") Integer idSede,
			@Param("numeroDocumento") String numeroDocumento, @Param("fechaInicial") String fechaInicial,
			@Param("fechaFinal") String fechaFinal, Pageable pageable);

	@Query(value = "SELECT SUM(total) FROM nota_debito_cliente WHERE id_sede = :idSede AND numero_documento = :numeroDocumento "
			+ "	AND date_format(date(fecha_documento),'%Y-%m-%d') BETWEEN date_format(date(:fechaInicial),'%Y-%m-%d') AND"
			+ "	 date_format(date(:fechaFinal), '%Y-%m-%d') ", nativeQuery = true)
	public Integer suma4(@Param("idSede") Integer idSede, @Param("numeroDocumento") String numeroDocumento,
			@Param("fechaInicial") String fechaInicial, @Param("fechaFinal") String fechaFinal);

	@Query(value = "SELECT * FROM nota_debito_cliente WHERE  id_sede = :idSede  "
			+ " AND date_format(date(fecha_documento),'%Y-%m-%d') BETWEEN date_format(date(:fechaInicial),'%Y-%m-%d') AND "
			+ "  date_format(date(:fechaFinal), '%Y-%m-%d')  " + "ORDER BY fecha_documento DESC", nativeQuery = true)
	public Page<NotaDebitoCliente> findByFechas(@Param("idSede") Integer idSede,
			@Param("fechaInicial") String fechaInicial, @Param("fechaFinal") String fechaFinal, Pageable pageable);

	@Query(value = "SELECT SUM(total) FROM nota_debito_cliente WHERE  id_sede = :idSede  "
			+ " AND date_format(date(fecha_documento),'%Y-%m-%d') BETWEEN date_format(date(:fechaInicial),'%Y-%m-%d') AND "
			+ "  date_format(date(:fechaFinal), '%Y-%m-%d')  " + "ORDER BY fecha_documento DESC", nativeQuery = true)
	public Integer suma5(@Param("idSede") Integer idSede, @Param("fechaInicial") String fechaInicial,
			@Param("fechaFinal") String fechaFinal);

	@Query(value = "SELECT * FROM nota_debito_cliente WHERE  id_sede = :idSede AND estado_documento = :estadoDocumento "
			+ " AND date_format(date(fecha_documento),'%Y-%m-%d') BETWEEN date_format(date(:fechaInicial),'%Y-%m-%d') AND "
			+ "  date_format(date(:fechaFinal), '%Y-%m-%d')  ", nativeQuery = true)
	public Page<NotaDebitoCliente> findByFechasEstado(@Param("idSede") Integer idSede,
			@Param("estadoDocumento") String estadoDocumento, @Param("fechaInicial") String fechaInicial,
			@Param("fechaFinal") String fechaFinal, Pageable pageable);

	@Query(value = "SELECT SUM(total) FROM nota_debito_cliente WHERE  id_sede = :idSede AND estado_documento = :estadoDocumento "
			+ " AND date_format(date(fecha_documento),'%Y-%m-%d') BETWEEN date_format(date(:fechaInicial),'%Y-%m-%d') AND "
			+ "  date_format(date(:fechaFinal), '%Y-%m-%d') ", nativeQuery = true)
	public Integer suma6(@Param("idSede") Integer idSede, @Param("estadoDocumento") String estadoDocumento,
			@Param("fechaInicial") String fechaInicial, @Param("fechaFinal") String fechaFinal);

	@Query(value = "SELECT * FROM nota_debito_cliente WHERE  id_sede = :idSede "
			+ " AND date_format(date(fecha_documento),'%Y-%m-%d') = date_format(date(:fechaInicial),'%Y-%m-%d') "
			+ "ORDER BY fecha_documento DESC", nativeQuery = true)
	public Page<NotaDebitoCliente> findByFechaInicial(@Param("idSede") Integer idSede,
			@Param("fechaInicial") String fechaInicial, Pageable pageable);

	@Query(value = "SELECT SUM(total) FROM nota_debito_cliente WHERE  id_sede = :idSede "
			+ " AND date_format(date(fecha_documento),'%Y-%m-%d') = date_format(date(:fechaInicial),'%Y-%m-%d') ", nativeQuery = true)
	public Integer suma7(@Param("idSede") Integer idSede, @Param("fechaInicial") String fechaInicial);

	@Query(value = "SELECT * FROM nota_debito_cliente WHERE  id_sede = :idSede AND estado_documento = :estadoDocumento"
			+ " AND date_format(date(fecha_documento),'%Y-%m-%d') = date_format(date(:fechaInicial),'%Y-%m-%d') "
			+ "ORDER BY fecha_documento DESC", nativeQuery = true)
	public Page<NotaDebitoCliente> findByFechaInicialEstado(@Param("idSede") Integer idSede,
			@Param("estadoDocumento") String estadoDocumento, @Param("fechaInicial") String fechaInicial,
			Pageable pageable);

	@Query(value = "SELECT SUM(total) FROM nota_debito_cliente WHERE  id_sede = :idSede AND estado_documento = :estadoDocumento"
			+ " AND date_format(date(fecha_documento),'%Y-%m-%d') = date_format(date(:fechaInicial),'%Y-%m-%d') ", nativeQuery = true)
	public Integer suma8(@Param("idSede") Integer idSede, @Param("estadoDocumento") String estadoDocumento,
			@Param("fechaInicial") String fechaInicial);

	@Query(value = "SELECT * FROM nota_debito_cliente WHERE id_sede =?1 AND estado_documento =?2 ORDER BY fecha_documento DESC", nativeQuery = true)
	public Page<NotaDebitoCliente> obtenerEstadoDocumento(Integer idSede, String estadoDocumento, Pageable pageable);

	@Query(value = "SELECT SUM(total) FROM nota_debito_cliente WHERE id_sede =?1 AND estado_documento =?2 ", nativeQuery = true)
	public Integer suma9(Integer idSede, String estadoDocumento);

//--------
	@Query(value = "SELECT * FROM nota_debito_cliente WHERE id_sede=?1 AND numero_documento=?2", nativeQuery = true)
	public NotaDebitoCliente findByIdSedeNumero(Integer idSede, String numeroDocumento);

	@Query(value = "SELECT * FROM nota_debito_cliente WHERE id_sede=?1", nativeQuery = true)
	public List<NotaDebitoCliente> listarnotadebitopendiente(Integer idSede);

	@Query(value = "SELECT * FROM nota_debito_cliente WHERE id_cliente = :idClie AND "
			+ "fecha_documento BETWEEN :fechaInicial AND :fechaFinal", nativeQuery = true)
	public List<NotaDebitoCliente> busarPorProveedorYFecha(Integer idClie, Date fechaInicial, Date fechaFinal);

	@Query(value = "select * from nota_debito_cliente where id_cliente = ? and estado_documento = 'Pendiente'", nativeQuery = true)
	public List<NotaDebitoCliente> obtenerNotasDebitosPorPagar(Integer idCliente);

	@Query(value = "select ifnull(sum(total),0) from nota_debito_cliente where id_cliente = ?1 and estado_documento = 'Pendiente' and id_sede = ?2", nativeQuery = true)
	public Integer obtenerTotalNotasDebitosPorPagar(Integer idCliente, Integer idSede);

	@Query(value = "select * from nota_debito_cliente where month(fecha_documento) = month(CURRENT_DATE()) and year(fecha_documento) = year(CURRENT_DATE()) and id_sede=?1", nativeQuery = true)
	public Page<NotaDebitoCliente> obtenerNotasDebitoDelMes(Integer idSede, Pageable pageable);

	@Query(value = "select sum(total) from nota_debito_cliente where month(fecha_documento) = month(CURRENT_DATE()) and year(fecha_documento) = year(CURRENT_DATE()) and id_sede=?1", nativeQuery = true)
	public Integer obtenerSumaDebitoDelMes(Integer idSede);

	@Query(value = "select * from nota_debito_cliente where id_sede = ? and estado_documento = 'Pendiente'", nativeQuery = true)
	public List<NotaDebitoCliente> obtenerNotasDebitosPorPagarAlmacen(Integer idSede);

	@Query(value = "select sum(total) from nota_debito_cliente where id_sede = ? and estado_documento = 'Pendiente'", nativeQuery = true)
	public Integer obtenerTotalNotasDebitosPorPagarAlmacen(Integer idSede);

	@Query(value = "select * from nota_debito_cliente where id_cliente = ?1 and estado_documento = 'Pendiente' and id_sede = ?2", nativeQuery = true) 
	 public List<NotaDebitoCliente> obtenerNotasDebitosPorPagar2(Integer idCliente, Integer idSede);
		
	
	@Query(value = "select sum(total) from nota_debito_cliente where id_cliente = ? and estado_documento = 'Pendiente'", nativeQuery = true) 
	 public Integer obtenerTotalFacturasPorPagar2(Integer idCliente);
	
	@Query(value="SELECT * FROM nota_debito_cliente WHERE id_sede=?1 and estado_documento = 'Pendiente'",nativeQuery=true)
	 public List<NotaDebitoCliente> listarnotadebitocartera(Integer idSede);

}
