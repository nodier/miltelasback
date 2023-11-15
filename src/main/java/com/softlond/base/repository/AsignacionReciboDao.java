package com.softlond.base.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.softlond.base.entity.AsignacionRecibo;

@Transactional
public interface AsignacionReciboDao extends CrudRepository<AsignacionRecibo, Integer>{

	@Query(value="select IFNULL(sum(n.total),0) from asignaciones_recibo  ar join nota_credito_cliente n on ar.nota_credito_id=n.id where ar.numero_fact = ?", nativeQuery = true)
	public Integer asignacionFactura(String numero);
	
	@Query(value = "select ifnull(sum(n.total),0) from asignaciones_recibo ar join nota_credito_cliente n on ar.nota_credito_id = n.id"+
			" where ar.numero_fact = ?", nativeQuery = true)
			public Integer obtenerTotal(String numeroFactura);
	
	@Query(value="select * from asignaciones_recibo ar join rcb_rbocajaventa rc on ar.id = rc.id_recibo where id=?", nativeQuery = true)
	public AsignacionRecibo asignacion(Integer idRecibo);
	
	@Query(value="select ifnull(sum(nc.total),0) from fac_facturas f join prefijo p on p.id = f.t_prefijo join conceptos_recibo_caja cr\r\n"
			+ "on cr.nro_documento = concat(p.prefijo, f.n_nro_factura) join rcb_rbocajaventa r on r.id = cr.id_recibo_caja join asignaciones_recibo ar on ar.id = r.id_recibo\r\n"
			+ "join nota_credito_cliente nc on nc.id = ar.nota_credito_id where r.id_cliente = ?1 and f.cod_estado_con in (2,5) and r.id_sede = ?2", nativeQuery = true)
	public Integer asignacionFacturaCliente(Integer idCliente, Integer idSede);
}
