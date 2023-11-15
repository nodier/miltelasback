package com.softlond.base.repository;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softlond.base.entity.NotaCredito;
import com.softlond.base.entity.NotaDebito;
import com.softlond.base.entity.RemisionCompra;

@Transactional
public interface NotaDebitoDao extends CrudRepository<NotaDebito, Integer> {
	
	public NotaDebito findByNumeroDocumento (String numeroDocumento);
	
	@Query(value = "SELECT * FROM nota_debito WHERE id_sede= :idSede", nativeQuery = true)
    public List<NotaDebito> findByIdSede(Integer idSede);
	
	@Query(value = "SELECT * FROM nota_debito WHERE id_sede = :idSede AND numero_documento = :numeroDocumento ORDER BY fecha_documento DESC", nativeQuery = true)
	public Page<NotaDebito> obtenerNumeroDocumento(@Param("idSede") Integer idSede, @Param ("numeroDocumento") String numeroDocumento, Pageable pageable);

	@Query(value="SELECT SUM(total) FROM nota_debito WHERE id_sede = :idSede AND numero_documento = :numeroDocumento",nativeQuery=true)
	public Integer suma1(@Param("idSede") Integer idSede, @Param ("numeroDocumento") String numeroDocumento);
	
	@Query(value = "SELECT * FROM nota_debito WHERE id_sede = :idSede AND numero_documento = :numeroDocumento AND estado_documento = :estadoDocumento ORDER BY fecha_documento DESC", nativeQuery = true)
	public Page<NotaDebito> findByNumeroDocumentoEstado(@Param("idSede") Integer idSede, @Param ("numeroDocumento") String numeroDocumento, @Param ("estadoDocumento") String estadoDocumento, Pageable pageable);

	@Query(value="SELECT SUM(total) FROM nota_debito WHERE id_sede = :idSede AND numero_documento = :numeroDocumento AND estado_documento = :estadoDocumento",nativeQuery=true)
	public Integer suma2(@Param("idSede") Integer idSede, @Param ("numeroDocumento") String numeroDocumento, @Param ("estadoDocumento") String estadoDocumento);
	
	@Query(value = "SELECT * FROM nota_debito WHERE  id_sede = :idSede AND numero_documento = :numeroDocumento AND date_format(date(fecha_documento),'%Y-%m-%d') = date_format(date(:fechaInicial),'%Y-%m-%d')  "
		  		+ "ORDER BY fecha_documento DESC", nativeQuery = true)
	public Page<NotaDebito> findByNumeroDocumentoFechaI(@Param("idSede") Integer idSede, @Param ("numeroDocumento") String numeroDocumento, @Param ("fechaInicial") String fechaInicial, Pageable pageable);
	
	@Query(value="SELECT SUM(total) FROM nota_debito WHERE id_sede = :idSede AND numero_documento = :numeroDocumento AND date_format(date(fecha_documento),'%Y-%m-%d') = date_format(date(:fechaInicial),'%Y-%m-%d')",nativeQuery=true)
	public Integer suma3(@Param("idSede") Integer idSede, @Param ("numeroDocumento") String numeroDocumento, @Param ("fechaInicial") String fechaInicial);
	
	 @Query(value = "SELECT * FROM nota_debito WHERE  id_sede = :idSede AND numero_documento = :numeroDocumento "
	 		+ " AND date_format(date(fecha_documento),'%Y-%m-%d') BETWEEN date_format(date(:fechaInicial),'%Y-%m-%d') AND "
	 		+ "  date_format(date(:fechaFinal), '%Y-%m-%d')  "
		  		+ "ORDER BY fecha_documento DESC", nativeQuery = true)
			public Page<NotaDebito> findByNumeroFechas(@Param("idSede") Integer idSede, @Param ("numeroDocumento") String numeroDocumento, @Param ("fechaInicial") String fechaInicial, @Param ("fechaFinal") String fechaFinal, Pageable pageable);
	
	 @Query(value="SELECT SUM(total) FROM nota_debito WHERE id_sede = :idSede AND numero_documento = :numeroDocumento "
	 		+ "	AND date_format(date(fecha_documento),'%Y-%m-%d') BETWEEN date_format(date(:fechaInicial),'%Y-%m-%d') AND"
	 		+ "	 date_format(date(:fechaFinal), '%Y-%m-%d') ",nativeQuery=true)
	 public Integer suma4(@Param("idSede") Integer idSede, @Param ("numeroDocumento") String numeroDocumento, @Param ("fechaInicial") String fechaInicial, @Param ("fechaFinal") String fechaFinal);
	 
	 @Query(value = "SELECT * FROM nota_debito WHERE  id_sede = :idSede  "
		 		+ " AND date_format(date(fecha_documento),'%Y-%m-%d') BETWEEN date_format(date(:fechaInicial),'%Y-%m-%d') AND "
		 		+ "  date_format(date(:fechaFinal), '%Y-%m-%d')  "
			  		+ "ORDER BY fecha_documento DESC", nativeQuery = true)
				public Page<NotaDebito> findByFechas(@Param("idSede") Integer idSede,  @Param ("fechaInicial") String fechaInicial, @Param ("fechaFinal") String fechaFinal, Pageable pageable);
	
	 @Query(value = "SELECT SUM(total) FROM nota_debito WHERE  id_sede = :idSede  "
		 		+ " AND date_format(date(fecha_documento),'%Y-%m-%d') BETWEEN date_format(date(:fechaInicial),'%Y-%m-%d') AND "
		 		+ "  date_format(date(:fechaFinal), '%Y-%m-%d')  "
			  		+ "ORDER BY fecha_documento DESC", nativeQuery = true)
				public Integer suma5(@Param("idSede") Integer idSede,  @Param ("fechaInicial") String fechaInicial, @Param ("fechaFinal") String fechaFinal);
	
	 
	 @Query(value = "SELECT * FROM nota_debito WHERE  id_sede = :idSede AND estado_documento = :estadoDocumento "
		 		+ " AND date_format(date(fecha_documento),'%Y-%m-%d') BETWEEN date_format(date(:fechaInicial),'%Y-%m-%d') AND "
		 		+ "  date_format(date(:fechaFinal), '%Y-%m-%d')  ", nativeQuery = true)
				public Page<NotaDebito> findByFechasEstado(@Param("idSede") Integer idSede, @Param ("estadoDocumento") String estadoDocumento, @Param ("fechaInicial") String fechaInicial, @Param ("fechaFinal") String fechaFinal, Pageable pageable);
		
	 @Query(value = "SELECT SUM(total) FROM nota_debito WHERE  id_sede = :idSede AND estado_documento = :estadoDocumento "
		 		+ " AND date_format(date(fecha_documento),'%Y-%m-%d') BETWEEN date_format(date(:fechaInicial),'%Y-%m-%d') AND "
		 		+ "  date_format(date(:fechaFinal), '%Y-%m-%d') ", nativeQuery = true)
				public Integer suma6(@Param("idSede") Integer idSede, @Param ("estadoDocumento") String estadoDocumento, @Param ("fechaInicial") String fechaInicial, @Param ("fechaFinal") String fechaFinal);
		
	 @Query(value = "SELECT * FROM nota_debito WHERE  id_sede = :idSede "
		 		+ " AND date_format(date(fecha_documento),'%Y-%m-%d') = date_format(date(:fechaInicial),'%Y-%m-%d') "
			  		+ "ORDER BY fecha_documento DESC", nativeQuery = true)
				public Page<NotaDebito> findByFechaInicial(@Param("idSede") Integer idSede,  @Param ("fechaInicial") String fechaInicial, Pageable pageable);

	 @Query(value = "SELECT SUM(total) FROM nota_debito WHERE  id_sede = :idSede "
		 		+ " AND date_format(date(fecha_documento),'%Y-%m-%d') = date_format(date(:fechaInicial),'%Y-%m-%d') ", nativeQuery = true)
				public Integer suma7(@Param("idSede") Integer idSede,  @Param ("fechaInicial") String fechaInicial);

	 
	 @Query(value = "SELECT * FROM nota_debito WHERE  id_sede = :idSede AND estado_documento = :estadoDocumento"
		 		+ " AND date_format(date(fecha_documento),'%Y-%m-%d') = date_format(date(:fechaInicial),'%Y-%m-%d') "
			  		+ "ORDER BY fecha_documento DESC", nativeQuery = true)
				public Page<NotaDebito> findByFechaInicialEstado(@Param("idSede") Integer idSede,  @Param ("estadoDocumento") String estadoDocumento, @Param ("fechaInicial") String fechaInicial, Pageable pageable);

	 @Query(value = "SELECT SUM(total) FROM nota_debito WHERE  id_sede = :idSede AND estado_documento = :estadoDocumento"
		 		+ " AND date_format(date(fecha_documento),'%Y-%m-%d') = date_format(date(:fechaInicial),'%Y-%m-%d') ", nativeQuery = true)
				public Integer suma8(@Param("idSede") Integer idSede,  @Param ("estadoDocumento") String estadoDocumento, @Param ("fechaInicial") String fechaInicial);

	 
	 @Query(value = "SELECT * FROM nota_debito WHERE id_sede =?1 AND estado_documento =?2 ORDER BY fecha_documento DESC", nativeQuery = true)
	public Page<NotaDebito> obtenerEstadoDocumento( Integer idSede,  String estadoDocumento, Pageable pageable);
	 
	 @Query(value = "SELECT SUM(total) FROM nota_debito WHERE id_sede =?1 AND estado_documento =?2 ", nativeQuery = true)
		public Integer suma9( Integer idSede,  String estadoDocumento);
	 
//--------
	 @Query(value="SELECT * FROM nota_debito WHERE id_sede=?1 AND numero_documento=?2",nativeQuery=true)
	 public NotaDebito findByIdSedeNumero(Integer idSede, String numeroDocumento);
	 
	 @Query(value="SELECT * FROM nota_debito WHERE id_sede=?1 AND estado_documento='Pendiente'",nativeQuery=true)
	 public List<NotaDebito> listarnotadebitopendiente(Integer idSede);

	@Query(value = "SELECT * FROM nota_debito WHERE id_proveedor = :idProv AND " +
			"fecha_documento BETWEEN :fechaInicial AND :fechaFinal and id_sede = :idSede",
			nativeQuery = true)
	
	
	public List<NotaDebito> busarPorProveedorYFecha(Integer idProv, Date fechaInicial, Date fechaFinal, Integer idSede);
	
	@Query(value = "select * from nota_debito where id_proveedor = ?1 and estado_documento = 'Pendiente' and id_sede = ?2", nativeQuery = true)
	public List<NotaDebito> obtenerNotasDebitosPorPagar(Integer idProveedor, Integer idSede);
	
	@Query(value = "select sum(total) from nota_debito where id_proveedor = ?1 and estado_documento != 'Anulado' and id_sede = ?2", nativeQuery = true)
	public Integer obtenerTotalNotasDebitosPorPagar(Integer idProveedor, Integer idSede);
	
	@Query(value="select * from nota_debito where month(fecha_documento) = month(CURRENT_DATE()) and year(fecha_documento) = year(CURRENT_DATE()) and id_sede=?1", nativeQuery = true)
	  public Page<NotaDebito> obtenerNotasDebitoDelMes(Integer idSede, Pageable pageable);
	
	@Query(value="select sum(total) from nota_credito where month(fecha_documento) = month(CURRENT_DATE()) and year(fecha_documento) = year(CURRENT_DATE()) and id_sede=?1", nativeQuery = true)
	  public Integer obtenerSumaDebitoDelMes(Integer idSede);
	
	@Query(value = "select * from nota_debito where id_sede = ? and estado_documento = 'Pendiente'", nativeQuery = true)
	public List<NotaDebito> obtenerNotasDebitosPorPagarAlmacen(Integer idSede);
	
	@Query(value = "select sum(total) from nota_debito where id_sede = ? and estado_documento != 'Anulado'", nativeQuery = true)
	public Integer obtenerTotalNotasDebitosPorPagarAlmacen(Integer idSede);
	
	@Query(value="select * from nota_debito where numero_documento = ? and estado_documento not in ('Asignado','Anulado')", nativeQuery = true)
	  public List<NotaDebito> obtenerNotasDebitoAnular(String numero);
	
	@Query(value="SELECT * FROM nota_debito n join prefijo p on n.prefijo=p.id WHERE n.id_sede=?1 AND n.numero_documento= ?2 and p.prefijo = ?3",nativeQuery=true)
	 public NotaDebito findByIdSedeNumeroYPrefijo(Integer idSede, String numeroDocumento, String Prefijo);
	
	@Query(value = "select sum(total) from nota_debito where id_sede = ? and estado_documento = 'Pendiente'", nativeQuery = true)
	public Integer obtenerTotalNotasDebitosPorPagarAlmacenEstadoCuentas(Integer idSede);
	
	@Query(value = "SELECT * FROM nota_debito nd join prefijo p on nd.prefijo=p.id left join rbe_recibo_egreso_conceptos rc on concat(p.prefijo,'-',nd.numero_documento) = rc.numero_documento WHERE nd.id_proveedor = ?1\r\n"
			+ "and nd.id_sede = ?2 and rc.nid_rbo_egreso=?3", nativeQuery = true)
	public List<NotaDebito> obtenerNotasDebitosPorPagarActualizar(Integer idProveedor, Integer idSede, Integer idComprobante);
}
