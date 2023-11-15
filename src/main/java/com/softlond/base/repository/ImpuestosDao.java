package com.softlond.base.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.softlond.base.entity.Articulo;
import com.softlond.base.entity.FacturaImpuestos;
import com.softlond.base.entity.Impuesto;

public interface ImpuestosDao extends CrudRepository<Impuesto, Integer> {

	@Query(value="SELECT * FROM pyme_cfg_impuestos where t_cod_impuesto = 'factura compra'", nativeQuery = true)
	public List<Impuesto> obtenerImpuestosProveedores();
	
	@Query(value="SELECT * FROM pyme_cfg_impuestos where t_cod_impuesto = 'recibo de caja'", nativeQuery = true)
	public List<Impuesto> obtenerImpuestosRecibo();
	
	@Query(value="SELECT * FROM pyme_cfg_impuestos where t_cod_impuesto = 'comprobante egreso'", nativeQuery = true)
	public List<Impuesto> obtenerImpuestosClientes();
	
	@Query(value="SELECT * FROM pyme_cfg_impuestos where nid_fac_retencion = ?1", nativeQuery = true)
	public Impuesto insertarEnlace(Integer id);

	@Query(value="SELECT * FROM pyme_cfg_impuestos where t_cod_impuesto = 'comprobante egreso' and enlace = 0", nativeQuery = true)
	public List<Impuesto> obtenerImpuestosClientesEnlace();
	
	@Query(value="SELECT * FROM pyme_cfg_impuestos where t_cod_impuesto = 'factura compra' and enlace = 0", nativeQuery = true)
	public List<Impuesto> obtenerImpuestosProveedoresEnlace();
}


