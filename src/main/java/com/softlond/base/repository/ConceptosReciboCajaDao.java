package com.softlond.base.repository;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.softlond.base.entity.ConceptosReciboCaja;
import com.softlond.base.entity.Factura;
import com.softlond.base.entity.FacturaArticulos;

@Transactional
public interface ConceptosReciboCajaDao extends CrudRepository<ConceptosReciboCaja, Integer> {

	public ConceptosReciboCaja findByConceptos(String conceptos);

	@Query(value = "SELECT * FROM conceptos_recibo_caja WHERE id_recibo_caja=?", nativeQuery = true)
	public ArrayList<ConceptosReciboCaja> listarConceptos(Integer idComprobante);

	@Modifying
	@Query(value = "DELETE FROM conceptos_recibo_caja WHERE id_recibo_caja=?", nativeQuery = true)
	public void borrarConceptos(Integer idRecibo);

	@Query(value = "select ifnull(sum(valor_abono),0) from conceptos_recibo_caja where nro_documento = ?1", nativeQuery = true)
	public Integer obtenerSaldo(String numeroDocumento);

	@Query(value = "select ifnull(sum(valor_abono),0) from conceptos_recibo_caja where cod_documento = ?1", nativeQuery = true)
	public Integer obtenerSaldo2(String numeroDocumento);

	@Query(value = "SELECT * FROM conceptos_recibo_caja where id_recibo_caja=?1", nativeQuery = true)
	public List<ConceptosReciboCaja> findAllBySedeRecibo(Integer id);

	@Query(value = "SELECT * FROM conceptos_recibo_caja WHERE nro_documento=?", nativeQuery = true)
	public Page<ConceptosReciboCaja> listarConceptosNumero(String numero, Pageable page);

	@Query(value = "SELECT id_recibo_caja FROM conceptos_recibo_caja WHERE id=?", nativeQuery = true)
	public Integer obtenerIdRecibo(Integer idConcepto);

	@Query(value = "SELECT IFNULL(sum(valor_abono),0) FROM conceptos_recibo_caja WHERE nro_documento=?", nativeQuery = true)
	public Integer totalConceptosNumero(String numero);

	@Query(value = "select ifnull(sum(valor_descuento),0) from conceptos_recibo_caja where nro_documento = ?1", nativeQuery = true)
	public Integer obtenerTotalDescuentoConcepto(String numeroDocumento);

	@Query(value = "select ifnull(sum(cr.valor_abono),0) from fac_facturas f join prefijo p on p.id = f.t_prefijo join conceptos_recibo_caja cr\r\n"
			+ "on cr.nro_documento = concat(p.prefijo, f.n_nro_factura) join rcb_rbocajaventa r on r.id = cr.id_recibo_caja \r\n"
			+ "where r.id_cliente = ?1 and f.cod_estado_con in (2,5) and r.id_sede = ?2", nativeQuery = true)
	public Integer totalConceptosClienteFacturas(Integer idCliente, Integer idSede);

	@Query(value = "select ifnull(sum(cr.valor_descuento),0) from fac_facturas f join prefijo p on p.id = f.t_prefijo join conceptos_recibo_caja cr\r\n"
			+ "on cr.nro_documento = concat(p.prefijo, f.n_nro_factura) join rcb_rbocajaventa r on r.id = cr.id_recibo_caja \r\n"
			+ "where r.id_cliente = ?1 and f.cod_estado_con in (2,5) and r.id_sede = ?2", nativeQuery = true)
	public Integer obtenerTotalDescuentoClienteFacturas(Integer idCliente, Integer idSede);

	@Query(value = "select ifnull(sum(cr.valor_abono),0) from nota_debito_cliente ndc join prefijo p on p.id = ndc.prefijo join conceptos_recibo_caja cr\r\n"
			+ "on cr.nro_documento = concat(p.prefijo, ndc.numero_documento) join rcb_rbocajaventa r on r.id = cr.id_recibo_caja \r\n"
			+ "where r.id_cliente = ?1 and ndc.estado_documento = 'Pagado' and r.id_sede = ?2", nativeQuery = true)
	public Integer totalConceptosClienteNotaDebito(Integer idCliente, Integer idSede);

	@Query(value = "select ifnull(sum(cr.valor_descuento),0) from nota_debito_cliente ndc join prefijo p on p.id = ndc.prefijo join conceptos_recibo_caja cr\r\n"
			+ "on cr.nro_documento = concat(p.prefijo, ndc.numero_documento) join rcb_rbocajaventa r on r.id = cr.id_recibo_caja \r\n"
			+ "where r.id_cliente = ?1 and ndc.estado_documento = 'Pagado' and r.id_sede = ?2", nativeQuery = true)
	public Integer obtenerTotalDescuentoClienteNotaDebito(Integer idCliente, Integer idSede);

}
