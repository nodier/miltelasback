package com.softlond.base.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.softlond.base.entity.ConceptoReciboEgreso;

@Transactional
public interface ConceptoReciboEgresoDao extends CrudRepository<ConceptoReciboEgreso, Integer>{

	@Query(value = "select IFNULL(sum(rbe.t_valor_abono),0) from rbe_recibo_egreso_conceptos rbe join comprobante_egreso ce on rbe.nid_rbo_egreso = ce.id \r\n"
			+ "where rbe.numero_documento = ?1 and ce.id_sede = ?2 ", nativeQuery = true)
	public Integer obtenerTotalAbonos(String numeroDocumento, Integer idSede);
	
	@Query(value = "select * from rbe_recibo_egreso_conceptos where nid_rbo_egreso = ?", nativeQuery = true)
	public List<ConceptoReciboEgreso> obtenerConceptos(Integer idConcepto);
	
	@Modifying
	@Query(value = "delete from rbe_recibo_egreso_conceptos where nid_rbo_egreso = ?", nativeQuery = true)
	public void eliminarConceptos(Integer idConcepto);
	
	@Query(value = "select * from rbe_recibo_egreso_conceptos where nid_rbo_egreso = ?", nativeQuery = true)
	public List<ConceptoReciboEgreso> obtenerConceptosPorDocumento(String numeroDocumento);
	
	@Query(value = "select * from rbe_recibo_egreso_conceptos where nid_rbo_egreso = ?1 and numero_documento = ?2", nativeQuery = true)
	public List<ConceptoReciboEgreso> obtenerConceptosPagosDocumentos(Integer idComprobante, String numeroDocumento);
	
	@Query(value = "select IFNULL(sum(rc.descuento),0) from rbe_recibo_egreso_conceptos rc join comprobante_egreso ce\r\n"
			+ "on rc.nid_rbo_egreso=ce.id where rc.numero_documento = ?1 and ce.id_sede = ?2", nativeQuery = true)
	public Integer obtenerTotalDescuentos(String numeroDocumento, Integer idSede);
	
	@Query(value = "select * from rbe_recibo_egreso_conceptos where numero_documento = ?1", nativeQuery = true)
	public Page<ConceptoReciboEgreso> obtenerConceptos(String numeroDocumento, Pageable page);
	
	@Query(value = "select t_valor_abono from rbe_recibo_egreso_conceptos where nid_rbo_egreso = ?2 and numero_documento = ?1", nativeQuery = true)
	public Integer obtenerTotalAbono(String numeroDocumento,Integer idComprobante);
	
	@Query(value = "select IFNULL(rc.descuento,0) from rbe_recibo_egreso_conceptos rc join comprobante_egreso ce\r\n"
			+ "on rc.nid_rbo_egreso=ce.id where rc.numero_documento = ?1 and ce.id_sede = ?2 and rc.nid_rbo_egreso = ?3", nativeQuery = true)
	public Integer obtenerDescuento(String numeroDocumento, Integer idSede, Integer idComprobante);
}
