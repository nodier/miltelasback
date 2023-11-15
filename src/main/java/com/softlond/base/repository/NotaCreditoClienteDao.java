package com.softlond.base.repository;

import java.sql.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softlond.base.entity.Factura;
import com.softlond.base.entity.NotaCredito;
import com.softlond.base.entity.NotaCreditoCliente;

@Transactional
public interface NotaCreditoClienteDao extends CrudRepository<NotaCreditoCliente, Integer> {
	
	@Query(value = "SELECT * FROM nota_credito_cliente WHERE numero_documento = :numeroDocumento", nativeQuery = true)
	public NotaCreditoCliente findByNumeroDocumento(String numeroDocumento);

	
	@Query(value = "SELECT * FROM nota_credito_cliente WHERE id_sede= :idSede", nativeQuery = true)
    public List<NotaCreditoCliente> findByIdSede(Integer idSede);
	
	@Query(value = "SELECT * FROM nota_credito_cliente WHERE id_sede = :idSede AND numero_documento = :numeroDocumento ORDER BY fecha_documento DESC", nativeQuery = true)
	public Page<NotaCreditoCliente> obtenerNumeroDocumento(@Param("idSede") Integer idSede, @Param ("numeroDocumento") String numeroDocumento, Pageable pageable);

	@Query(value="SELECT SUM(total) FROM nota_credito_cliente WHERE id_sede = :idSede AND numero_documento = :numeroDocumento",nativeQuery=true)
	public Integer suma1(@Param("idSede") Integer idSede, @Param ("numeroDocumento") String numeroDocumento);
	
	@Query(value = "SELECT * FROM nota_credito_cliente WHERE id_sede = :idSede AND numero_documento = :numeroDocumento AND estado_documento = :estadoDocumento ORDER BY fecha_documento DESC", nativeQuery = true)
	public Page<NotaCreditoCliente> findByNumeroDocumentoEstado(@Param("idSede") Integer idSede, @Param ("numeroDocumento") String numeroDocumento, @Param ("estadoDocumento") String estadoDocumento, Pageable pageable);

	@Query(value="SELECT SUM(total) FROM nota_credito_cliente WHERE id_sede = :idSede AND numero_documento = :numeroDocumento AND estado_documento = :estadoDocumento",nativeQuery=true)
	public Integer suma2(@Param("idSede") Integer idSede, @Param ("numeroDocumento") String numeroDocumento, @Param ("estadoDocumento") String estadoDocumento);
	
	@Query(value = "SELECT * FROM nota_credito_cliente WHERE  id_sede = :idSede AND numero_documento = :numeroDocumento AND date_format(date(fecha_documento),'%Y-%m-%d') = date_format(date(:fechaInicial),'%Y-%m-%d') ORDER BY fecha_documento DESC", nativeQuery = true)
	public Page<NotaCreditoCliente> findByNumeroDocumentoFechaI(@Param("idSede") Integer idSede, @Param ("numeroDocumento") String numeroDocumento, @Param ("fechaInicial") String fechaInicial, Pageable pageable);
	
	@Query(value="SELECT SUM(total) FROM nota_credito_cliente WHERE id_sede = :idSede AND numero_documento = :numeroDocumento AND date_format(date(fecha_documento),'%Y-%m-%d') = date_format(date(:fechaInicial),'%Y-%m-%d')",nativeQuery=true)
	public Integer suma3(@Param("idSede") Integer idSede, @Param ("numeroDocumento") String numeroDocumento, @Param ("fechaInicial") String fechaInicial);
	
	 @Query(value = "SELECT * FROM nota_credito_cliente WHERE  id_sede = :idSede AND numero_documento = :numeroDocumento"
	 		+ " AND date_format(date(fecha_documento),'%Y-%m-%d') BETWEEN date_format(date(:fechaInicial),'%Y-%m-%d') AND"
	 		+ " date_format(date(:fechaFinal), '%Y-%m-%d') ORDER BY fecha_documento DESC", nativeQuery = true)
			public Page<NotaCreditoCliente> findByNumeroFechas(@Param("idSede") Integer idSede, @Param ("numeroDocumento") String numeroDocumento, @Param ("fechaInicial") String fechaInicial, @Param ("fechaFinal") String fechaFinal, Pageable pageable);
	
	 @Query(value="SELECT SUM(total) FROM nota_credito_cliente WHERE id_sede = :idSede AND numero_documento = :numeroDocumento"
	 		+ "	AND date_format(date(fecha_documento),'%Y-%m-%d') BETWEEN date_format(date(:fechaInicial),'%Y-%m-%d') AND"
	 		+ "	date_format(date(:fechaFinal), '%Y-%m-%d') ",nativeQuery=true)
	 public Integer suma4(@Param("idSede") Integer idSede, @Param ("numeroDocumento") String numeroDocumento, @Param ("fechaInicial") String fechaInicial, @Param ("fechaFinal") String fechaFinal);
	 
	 @Query(value = "SELECT * FROM nota_credito_cliente WHERE  id_sede = :idSede AND date_format(date(fecha_documento),'%Y-%m-%d') BETWEEN date_format(date(:fechaInicial),'%Y-%m-%d') AND"
		 		+ " date_format(date(:fechaFinal), '%Y-%m-%d') ORDER BY fecha_documento DESC", nativeQuery = true)
				public Page<NotaCreditoCliente> findByFechas(@Param("idSede") Integer idSede,  @Param ("fechaInicial") String fechaInicial, @Param ("fechaFinal") String fechaFinal, Pageable pageable);
	
	 @Query(value = "SELECT SUM(total) FROM nota_credito_cliente WHERE  id_sede = :idSede"
		 		+ " AND date_format(date(fecha_documento),'%Y-%m-%d') BETWEEN date_format(date(:fechaInicial),'%Y-%m-%d') AND"
		 		+ " date_format(date(:fechaFinal), '%Y-%m-%d') ORDER BY fecha_documento DESC", nativeQuery = true)
				public Integer suma5(@Param("idSede") Integer idSede,  @Param ("fechaInicial") String fechaInicial, @Param ("fechaFinal") String fechaFinal);
	
	 
	 @Query(value = "SELECT * FROM nota_credito_cliente WHERE  id_sede = :idSede AND estado_documento = :estadoDocumento"
		 		+ " AND date_format(date(fecha_documento),'%Y-%m-%d') BETWEEN date_format(date(:fechaInicial),'%Y-%m-%d') AND"
		 		+ " date_format(date(:fechaFinal), '%Y-%m-%d')", nativeQuery = true)
				public Page<NotaCreditoCliente> findByFechasEstado(@Param("idSede") Integer idSede, @Param ("estadoDocumento") String estadoDocumento, @Param ("fechaInicial") String fechaInicial, @Param ("fechaFinal") String fechaFinal, Pageable pageable);
		
	 @Query(value = "SELECT SUM(total) FROM nota_credito_cliente WHERE  id_sede = :idSede AND estado_documento = :estadoDocumento"
		 		+ " AND date_format(date(fecha_documento),'%Y-%m-%d') BETWEEN date_format(date(:fechaInicial),'%Y-%m-%d') AND"
		 		+ " date_format(date(:fechaFinal), '%Y-%m-%d') ", nativeQuery = true)
				public Integer suma6(@Param("idSede") Integer idSede, @Param ("estadoDocumento") String estadoDocumento, @Param ("fechaInicial") String fechaInicial, @Param ("fechaFinal") String fechaFinal);
		
	 @Query(value = "SELECT * FROM nota_credito_cliente WHERE  id_sede = :idSede AND date_format(date(fecha_documento),'%Y-%m-%d') = date_format(date(:fechaInicial),'%Y-%m-%d') "
			  		+ "ORDER BY fecha_documento DESC", nativeQuery = true)
				public Page<NotaCreditoCliente> findByFechaInicial(@Param("idSede") Integer idSede,  @Param ("fechaInicial") String fechaInicial, Pageable pageable);

	 @Query(value = "SELECT SUM(total) FROM nota_credito_cliente WHERE  id_sede = :idSede AND date_format(date(fecha_documento),'%Y-%m-%d') = date_format(date(:fechaInicial),'%Y-%m-%d') ", nativeQuery = true)
				public Integer suma7(@Param("idSede") Integer idSede,  @Param ("fechaInicial") String fechaInicial);

	 
	 @Query(value = "SELECT * FROM nota_credito_cliente WHERE  id_sede = :idSede AND estado_documento = :estadoDocumento"
		 		+ " AND date_format(date(fecha_documento),'%Y-%m-%d') = date_format(date(:fechaInicial),'%Y-%m-%d')", nativeQuery = true)
				public Page<NotaCreditoCliente> findByFechaInicialEstado(@Param("idSede") Integer idSede,  @Param ("estadoDocumento") String estadoDocumento, @Param ("fechaInicial") String fechaInicial, Pageable pageable);

	 @Query(value = "SELECT SUM(total) FROM nota_credito_cliente WHERE  id_sede = :idSede AND estado_documento = :estadoDocumento"
		 		+ " AND date_format(date(fecha_documento),'%Y-%m-%d') = date_format(date(:fechaInicial),'%Y-%m-%d')", nativeQuery = true)
				public Integer suma8(@Param("idSede") Integer idSede,  @Param ("estadoDocumento") String estadoDocumento, @Param ("fechaInicial") String fechaInicial);

	 
	 @Query(value = "SELECT * FROM nota_credito_cliente WHERE id_sede = ?1 AND estado_documento = ?2 ORDER BY fecha_documento DESC", nativeQuery = true)
	public Page<NotaCreditoCliente> obtenerEstadoDocumento( Integer idSede,  String estadoDocumento, Pageable pageable);
	 
	 @Query(value = "SELECT SUM(total) FROM nota_credito_cliente WHERE id_sede = ?1 AND estado_documento = ?2 ", nativeQuery = true)
		public Integer suma9( Integer idSede,  String estadoDocumento);
	 
//-----------------
	 
@Query(value="SELECT * FROM nota_credito_cliente WHERE id_sede= ?1 AND numero_documento= ?2",nativeQuery=true)
public NotaCreditoCliente findByIdSedeNumero(Integer idSede, String numeroDocumento);

//public NotaCredito findByIdNotaCredito (String numeroDocumento);

@Query(value="SELECT * FROM nota_credito_cliente WHERE id_sede= ?1 AND estado_documento='Pendiente'",nativeQuery=true)
public NotaCreditoCliente listarnotacredito(Integer idSede);
/*
 * este
 */
@Query(value="SELECT * FROM nota_credito_cliente WHERE id_sede=?1 AND estado_documento='Pendiente'",nativeQuery=true)
public List<NotaCreditoCliente> listarnotacreditopendiente2(Integer idSede);

	@Query(value = "SELECT * FROM nota_credito_cliente WHERE id_cliente = :idClie AND " +
			"fecha_documento BETWEEN :fechaInicial AND :fechaFinal",
		nativeQuery = true)
	public List<NotaCreditoCliente> busarPorClienteYFecha(Integer idClie, Date fechaInicial, Date fechaFinal);
	/*
	 * este
	 */
	@Query(value = "SELECT * FROM nota_credito_cliente WHERE id_cliente = ?1 AND estado_documento = 'Pendiente'", nativeQuery = true)
	public List<NotaCreditoCliente> obtenerNotasCreditosPorPagar2(Integer idCliente);
	
	@Query(value = "select ifnull(sum(total),0) from nota_credito_cliente where id_cliente = :idCliente and estado_documento = 'Pendiente' and id_sede = :idSede", nativeQuery = true)
	public Integer obtenerTotalNotasCreditosClientePorPagar(Integer idCliente, Integer idSede);
	
	@Query(value="select * from nota_credito_cliente where month(fecha_documento) = month(CURRENT_DATE()) and year(fecha_documento) = year(CURRENT_DATE()) and id_sede=?1", nativeQuery = true)
	  public Page<NotaCreditoCliente> obtenerNotasCreditoDelMes(Integer idSede, Pageable pageable);
	
	@Query(value="select sum(total) from nota_credito_cliente where month(fecha_documento) = month(CURRENT_DATE()) and year(fecha_documento) = year(CURRENT_DATE()) and id_sede=?1", nativeQuery = true)
	  public Integer sumaNotasCreditoDelMes(Integer idSede);
	
	@Modifying
	@Query(value="DELETE FROM nota_credito_cliente WHERE id = ?", nativeQuery = true)
	  public void eliminarNotaCredito(Integer idSede);
	
	@Query(value = "select * from nota_credito_cliente where id_sede = ? and estado_documento = 'Pendiente'", nativeQuery = true)
	public List<NotaCreditoCliente> obtenerNotasCreditosPorPagarAlmacen(Integer idSede);
	
	@Query(value = "select sum(total) from nota_credito_cliente where id_sede = ? and estado_documento = 'Pendiente'", nativeQuery = true)
	public Integer obtenerTotalNotasCreditosPorPagarAlmacen(Integer idSede);
			
	/*
	 * ojitooooo
	 */
	
	@Query(value="SELECT * FROM nota_credito_cliente WHERE id_cliente=?1 AND estado_documento='Pendiente'",nativeQuery=true)
	public List<NotaCreditoCliente> listarnotacreditopendiente(Integer idCliente);


	@Query(value = "select * from nota_credito_cliente where id_cliente = ? AND estado_documento='Pendiente'", nativeQuery = true) 
	 public List<NotaCreditoCliente> obtenerNotasCreditosPorPagar(Integer idCliente);
	
	@Query(value = "select sum(total) from nota_credito_cliente where id_cliente = ? AND estado_documento='Pendiente' ", nativeQuery = true) 
	 public Integer obtenerTotalFacturasPorPagar(Integer idCliente);
	
	@Query(value="SELECT * FROM nota_credito_cliente WHERE id_sede=?1 and estado_documento = 'Pendiente'",nativeQuery=true)
	public List<NotaCreditoCliente> listarnotacreditocartera(Integer idSede);
	
	@Query(value = "SELECT * FROM nota_credito_cliente WHERE id_cliente = ?1 AND estado_documento = 'Pendiente'", nativeQuery = true)
	public Page<NotaCreditoCliente> obtenerNotasCreditosPorPagarPaginado(Integer idCliente, Pageable page);
	
	@Query(value = "SELECT * FROM nota_credito_cliente WHERE id_cliente = ?1 AND (estado_documento = 'Pendiente' or id=?2 )", nativeQuery = true)
	public Page<NotaCreditoCliente> obtenerNotasCreditosPorPagarPaginadoActualizar(Integer idProveedor,Integer idCredito, Pageable page);
	
	@Query(value = "select * from nota_credito_cliente where id_cliente = ?1 AND estado_documento='Pendiente' and id_sede = ?2", nativeQuery = true) 
	 public List<NotaCreditoCliente> obtenerNotasCreditosPorPagarSede(Integer idCliente, Integer idSede);
	
	@Query(value = "select estado_documento from nota_credito_cliente where id=?1 and id_sede = ?2", nativeQuery = true) 
	 public String estadoDocumento(Integer id, Integer id_sede);
	

}
