package com.softlond.base.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.softlond.base.entity.FacturaCompra;

public interface InformeFacturasVencidasDao extends CrudRepository<FacturaCompra, Integer>{
	
	
	@Query(value= "select * from fc_factura_compra where cod_estado_con =5 AND id_sede=?1"
			+ " and DATEDIFF(d_fecha_vencimiento,CURDATE()) < 0 order by DATEDIFF(d_fecha_vencimiento,CURDATE())", nativeQuery = true)
	public Page<FacturaCompra> listarinformeFacturasVencidas(Integer idSede, Pageable page);
	
	@Query(value= "select * from fc_factura_compra where cod_estado_con =5 AND id_sede=?1", nativeQuery = true)
	public Page<FacturaCompra> listarinformeFacturasVencidasCompra(Integer idSede, Pageable page);
	
	@Query(value= "select sum(m_total) from fc_factura_compra where cod_estado_con =5 AND id_sede=?1"
			+ " and DATEDIFF(d_fecha_vencimiento,CURDATE()) < 0 order by DATEDIFF(d_fecha_vencimiento,CURDATE())", nativeQuery = true)
	public Integer TotalInformeFacturasVencidas(Integer idSede);
}