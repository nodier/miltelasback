package com.softlond.base.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.softlond.base.entity.Proveedor;

public interface ProveedorDao extends CrudRepository<Proveedor, Integer> {

	@Query(value = "select * from ter_proveedores p left join fc_factura_compra fc on p.nid_proveedor = fc.nid_proveedor "
			+ "group by p.nid_proveedor order by p.t_proveedor", nativeQuery = true)
	public Page<Proveedor> obtenerTodosProveedores(Pageable pageable);

	@Query(value = "select concat(tnitocc,'--',t_proveedor) from ter_proveedores;", nativeQuery = true)
	public List<String> obtenerNombres();

	@Query(value = "SELECT * FROM ter_proveedores", nativeQuery = true)
	public ArrayList<Proveedor> obtenerProveedores();

	@Query(value = "select ifnull(sum(fc.m_total),0) from ter_proveedores pr join fc_factura_compra fc on pr.nid_proveedor = fc.nid_proveedor where pr.nid_proveedor = ?1 and fc.cod_estado_con = 5 and fc.id_sede = ?2", nativeQuery = true)
	public Integer obtenerValorFacturas(Integer idProveedor, Integer idSede);

	public Proveedor findByNitAndDigito(String nit, Integer digito);

	public Proveedor findByNit(String nit);

	@Query(value = "SELECT * FROM ter_proveedores " +
			"WHERE LOWER(tProveedor) LIKE '%:palabra%' " +
			"ORDER BY tProveedor ASC", nativeQuery = true)
	public List<Proveedor> buscarPorPalabraClave(String palabra); // ! Revisar esta funcion porque like funciona sin la
																																// comilla simple
}
