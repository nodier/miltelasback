package com.softlond.base.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.softlond.base.entity.FacturaImpuestos;

public interface FacturaImpuestosDao extends CrudRepository<FacturaImpuestos, Integer>{

	@Query(value="SELECT * FROM fc_factura_impuestos where nid_factura_compra = ?;", nativeQuery = true)
	public ArrayList<FacturaImpuestos> obtenerImpuestosFactura(Integer idFacturaCompra);
	
}
