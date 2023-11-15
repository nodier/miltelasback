package com.softlond.base.repository;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.softlond.base.entity.Clientes;
import com.softlond.base.entity.Factura;
import com.softlond.base.entity.FacturaCompra;
import com.softlond.base.entity.NotaCredito;
import com.softlond.base.entity.Proveedor;
import com.softlond.base.entity.FacturaCompra;

@Transactional
public interface ClientesDao extends CrudRepository<Clientes, Integer> {

	@Query(value = "SELECT * FROM clientes", nativeQuery = true)
	public ArrayList<Clientes> obtenerClientes();

	@Query(value = "SELECT * FROM clientes WHERE id=?", nativeQuery = true)
	public Clientes obtenerCliente(Integer id);

	public Clientes findByNitocc(String nitocc);

	@Query(value = "select * from clientes", nativeQuery = true)
	public Page<Clientes> obtenerTodosClientes(Pageable pageable);

	@Query(value = "select concat(nombres,'--',apellidos) from clientes;", nativeQuery = true)
	public List<String> obtenerNombres();

	@Query(value = "SELECT * FROM clientes WHERE id_sede=?1", nativeQuery = true)
	public List<Clientes> listarClientes(Integer idSede);

	@Query(value = "SELECT * FROM clientes where nombres != '99'", nativeQuery = true)
	public ArrayList<Clientes> obtenerClientesCredito();

	@Query(value = "SELECT * FROM clientes where nitocc like ?1% or (concat(nombres,' ',apellidos) like ?1%) limit 20", nativeQuery = true)
	public ArrayList<Clientes> obtenerClientestexto(String texto);

	//! obtener la cantidad de clientes que existen
	@Query(value = "SELECT count(*) FROM clientes", nativeQuery = true)
	public Integer obtenerCantidadClientes();

}
