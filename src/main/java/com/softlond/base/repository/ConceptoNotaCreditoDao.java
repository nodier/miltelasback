package com.softlond.base.repository;

import java.util.ArrayList;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softlond.base.entity.ConceptoNotaCredito;
import com.softlond.base.entity.NotaCredito;

@Transactional
public interface ConceptoNotaCreditoDao extends CrudRepository <ConceptoNotaCredito, Integer> {
	
	@Query(value="SELECT * FROM concepto_nota_credito c INNER JOIN nota_credito n ON c.id_nota_credito = n.id AND n.id_sede=?1",nativeQuery=true)
	public ArrayList<ConceptoNotaCredito> obtenerConceptosNotaCredito(Integer idSede);
	
	@Query(value = "SELECT * FROM concepto_nota_credito c INNER JOIN nota_credito n ON c.id_nota_credito = n.id AND n.id_sede =?1 AND n.numero_documento =?2 ORDER BY n.fecha_documento DESC", nativeQuery = true)
	public Page<ConceptoNotaCredito> obtenerNumeroDocumento(Integer idSede, String numeroDocumento, Pageable pageable);

	@Query(value="SELECT SUM(total) FROM concepto_nota_credito c INNER JOIN nota_credito n ON c.id_nota_credito = n.id AND n.id_sede =?1 AND n.numero_documento =?2",nativeQuery=true)
	public Integer suma1(Integer idSede,  String numeroDocumento);
	
	@Query(value = "SELECT * FROM concepto_nota_credito c INNER JOIN nota_credito n ON c.id_nota_credito = n.id AND n.id_sede =?1 AND n.numero_documento =?2 AND n.estado_documento =?3 ORDER BY n.fecha_documento DESC", nativeQuery = true)
	public Page<ConceptoNotaCredito> findByNumeroDocumentoEstado( Integer idSede,  String numeroDocumento,  String estadoDocumento, Pageable pageable);

	@Query(value="SELECT SUM(total) FROM concepto_nota_credito c INNER JOIN nota_credito n ON c.id_nota_credito = n.id AND n.id_sede = :idSede AND n.numero_documento = :numeroDocumento AND n.estado_documento = :estadoDocumento",nativeQuery=true)
	public Integer suma2(@Param("idSede") Integer idSede, @Param ("numeroDocumento") String numeroDocumento, @Param ("estadoDocumento") String estadoDocumento);
	
	@Query(value = "SELECT * FROM concepto_nota_credito c INNER JOIN nota_credito n ON c.id_nota_credito = n.id AND n.id_sede = :idSede AND n.numero_documento = :numeroDocumento AND date_format(date(n.fecha_documento),'%Y-%m-%d') = date_format(date(:fechaInicial),'%Y-%m-%d')  "
		  		+ "ORDER BY n.fecha_documento DESC", nativeQuery = true)
	public Page<ConceptoNotaCredito> findByNumeroDocumentoFechaI(@Param("idSede") Integer idSede, @Param ("numeroDocumento") String numeroDocumento, @Param ("fechaInicial") String fechaInicial, Pageable pageable);
	
	@Query(value="SELECT SUM(total) FROM concepto_nota_credito c INNER JOIN nota_credito n ON c.id_nota_credito = n.id AND n.id_sede = :idSede AND n.numero_documento = :numeroDocumento AND date_format(date(n.fecha_documento),'%Y-%m-%d') = date_format(date(:fechaInicial),'%Y-%m-%d')",nativeQuery=true)
	public Integer suma3(@Param("idSede") Integer idSede, @Param ("numeroDocumento") String numeroDocumento, @Param ("fechaInicial") String fechaInicial);
	
	 @Query(value = "SELECT * FROM concepto_nota_credito c INNER JOIN nota_credito n ON c.id_nota_credito = n.id AND n.id_sede = :idSede AND n.numero_documento = :numeroDocumento"
	 		+ " AND date_format(date(n.fecha_documento),'%Y-%m-%d') BETWEEN date_format(date(:fechaInicial),'%Y-%m-%d') AND date_format(date(:fechaFinal),'%Y-%m-%d') "
		  		+ "ORDER BY n.fecha_documento DESC", nativeQuery = true)
			public Page<ConceptoNotaCredito> findByNumeroFechas(@Param("idSede") Integer idSede, @Param ("numeroDocumento") String numeroDocumento, @Param ("fechaInicial") String fechaInicial, @Param ("fechaFinal") String fechaFinal, Pageable pageable);
	
	 @Query(value="SELECT SUM(total) FROM concepto_nota_credito c INNER JOIN nota_credito n ON c.id_nota_credito = n.id AND n.id_sede = :idSede AND n.numero_documento = :numeroDocumento "
	 		+ "AND date_format(date(n.fecha_documento),'%Y-%m-%d') BETWEEN date_format(date(:fechaInicial),'%Y-%m-%d') AND"
	 		+ "	 date_format(date(:fechaFinal), '%Y-%m-%d') ",nativeQuery=true)
	 public Integer suma4(@Param("idSede") Integer idSede, @Param ("numeroDocumento") String numeroDocumento, @Param ("fechaInicial") String fechaInicial, @Param ("fechaFinal") String fechaFinal);
	 
	 @Query(value = "SELECT * FROM concepto_nota_credito c INNER JOIN nota_credito n ON c.id_nota_credito = n.id AND n.id_sede = :idSede  "
		 		+ " AND date_format(date(n.fecha_documento),'%Y-%m-%d') BETWEEN date_format(date(:fechaInicial),'%Y-%m-%d') AND "
		 		+ "  date_format(date(:fechaFinal), '%Y-%m-%d')  "
			  		+ "ORDER BY n.fecha_documento DESC", nativeQuery = true)
				public Page<ConceptoNotaCredito> findByFechas(@Param("idSede") Integer idSede,  @Param ("fechaInicial") String fechaInicial, @Param ("fechaFinal") String fechaFinal, Pageable pageable);
	
	 @Query(value = "SELECT SUM(total) FROM concepto_nota_credito c INNER JOIN nota_credito n ON c.id_nota_credito = n.id AND n.id_sede = :idSede  "
		 		+ " AND date_format(date(n.fecha_documento),'%Y-%m-%d') BETWEEN date_format(date(:fechaInicial),'%Y-%m-%d') AND "
		 		+ "  date_format(date(:fechaFinal), '%Y-%m-%d')  "
			  		+ "ORDER BY n.fecha_documento DESC", nativeQuery = true)
				public Integer suma5(@Param("idSede") Integer idSede,  @Param ("fechaInicial") String fechaInicial, @Param ("fechaFinal") String fechaFinal);
	
	 
	 @Query(value = "SELECT * FROM concepto_nota_credito c INNER JOIN nota_credito n ON c.id_nota_credito = n.id AND n.id_sede = :idSede AND n.estado_documento = :estadoDocumento AND date_format(date(n.fecha_documento),'%Y-%m-%d') BETWEEN date_format(date(:fechaInicial),'%Y-%m-%d') AND date_format(date(:fechaFinal),'%Y-%m-%d')", nativeQuery = true)
				public Page<ConceptoNotaCredito> findByFechasEstado(@Param("idSede") Integer idSede, @Param ("estadoDocumento") String estadoDocumento, @Param ("fechaInicial") String fechaInicial, @Param ("fechaFinal") String fechaFinal, Pageable pageable);
		
	 @Query(value = "SELECT SUM(total) FROM concepto_nota_credito c INNER JOIN nota_credito n ON c.id_nota_credito = n.id AND n.id_sede = :idSede AND n.estado_documento = :estadoDocumento "
		 		+ "AND date_format(date(n.fecha_documento),'%Y-%m-%d') BETWEEN date_format(date(:fechaInicial),'%Y-%m-%d') AND "
		 		+ "date_format(date(:fechaFinal), '%Y-%m-%d')", nativeQuery = true)
				public Integer suma6(@Param("idSede") Integer idSede, @Param ("estadoDocumento") String estadoDocumento, @Param ("fechaInicial") String fechaInicial, @Param ("fechaFinal") String fechaFinal);
		
	 @Query(value = "SELECT * FROM concepto_nota_credito c INNER JOIN nota_credito n ON c.id_nota_credito = n.id AND n.id_sede = :idSede "
		 		+ " AND date_format(date(n.fecha_documento),'%Y-%m-%d') = date_format(date(:fechaInicial),'%Y-%m-%d') "
			  		+ "ORDER BY n.fecha_documento DESC", nativeQuery = true)
				public Page<ConceptoNotaCredito> findByFechaInicial(@Param("idSede") Integer idSede,  @Param ("fechaInicial") String fechaInicial, Pageable pageable);

	 @Query(value = "SELECT SUM(total) FROM concepto_nota_credito c INNER JOIN nota_credito n ON c.id_nota_credito = n.id AND n.id_sede = :idSede"
		 		+ " AND date_format(date(n.fecha_documento),'%Y-%m-%d') = date_format(date(:fechaInicial),'%Y-%m-%d')", nativeQuery = true)
				public Integer suma7(@Param("idSede") Integer idSede,  @Param ("fechaInicial") String fechaInicial);

	 
	 @Query(value = "SELECT * FROM concepto_nota_credito c INNER JOIN nota_credito n ON c.id_nota_credito = n.id AND n.id_sede = :idSede AND n.estado_documento = :estadoDocumento AND date_format(date(n.fecha_documento),'%Y-%m-%d') = date_format(date(:fechaInicial),'%Y-%m-%d')", nativeQuery = true)
				public Page<ConceptoNotaCredito> findByFechaInicialEstado(@Param("idSede") Integer idSede,  @Param ("estadoDocumento") String estadoDocumento, @Param ("fechaInicial") String fechaInicial, Pageable pageable);

	 @Query(value = "SELECT SUM(total) FROM concepto_nota_credito c INNER JOIN nota_credito n ON c.id_nota_credito = n.id AND n.id_sede = :idSede AND n.estado_documento = :estadoDocumento AND date_format(date(n.fecha_documento),'%Y-%m-%d') = date_format(date(:fechaInicial),'%Y-%m-%d')", nativeQuery = true)
				public Integer suma8(@Param("idSede") Integer idSede,  @Param ("estadoDocumento") String estadoDocumento, @Param ("fechaInicial") String fechaInicial);

	 
	 @Query(value = "SELECT * FROM concepto_nota_credito c INNER JOIN nota_credito n ON c.id_nota_credito = n.id AND n.id_sede =?1 AND n.estado_documento =?2 ORDER BY n.fecha_documento DESC", nativeQuery = true)
	public Page<ConceptoNotaCredito> obtenerEstadoDocumento( Integer idSede,  String estadoDocumento, Pageable pageable);
	 
	 @Query(value = "SELECT SUM(total) FROM concepto_nota_credito c INNER JOIN nota_credito n ON c.id_nota_credito = n.id AND n.id_sede =?1 AND n.estado_documento =?2 ", nativeQuery = true)
		public Integer suma9( Integer idSede,  String estadoDocumento);
	 
//------------------
	 @Query(value="SELECT * FROM concepto_nota_credito WHERE id_nota_credito=?1", nativeQuery=true)
	 public ArrayList<ConceptoNotaCredito> obtenerConceptoIdNotaCredito(Integer idNotaCredito);
	 
	 @Modifying
	 @Query(value="DELETE FROM concepto_nota_credito WHERE id_nota_credito=?1", nativeQuery=true)
	 public void eliminarConceptosNotasCreditos(Integer idNotaCredito);

}
