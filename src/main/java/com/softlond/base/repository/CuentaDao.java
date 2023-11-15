package com.softlond.base.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.softlond.base.entity.Cuenta;

public interface CuentaDao extends CrudRepository<Cuenta, Integer> {

	
	@Query(value = "SELECT * FROM cuenta where (sede_id is null or sede_id = ?) and tipo_documento = 'NOTA DEBITO'", nativeQuery = true)
	public List<Cuenta> obtenerCuentasDebito(Integer idSede);
	
	@Query(value = "SELECT * FROM cuenta where (sede_id is null or sede_id = ?) and tipo_documento = 'Nota debito cliente'", nativeQuery = true)
	public List<Cuenta> obtenerCuentasDebitoCliente(Integer idSede);
	
	@Query(value = "select * from cuenta where (sede_id is null or sede_id = ?) and tipo_documento = 'NOTA CREDITO PROVEEDORES'", nativeQuery = true)
	public List<Cuenta> obtenerCuentasCredito(Integer idSede);
	
	@Query(value = "SELECT * FROM cuenta where (sede_id is null or sede_id = ?) and tipo_documento = 'NOTA CREDITO PROVEEDORES'", nativeQuery = true)
	public List<Cuenta> obtenerCuentasPrecargadasDevolucion(Integer idSede);
	
	@Query(value = "SELECT * FROM cuenta where (sede_id is null or sede_id = ?) and tipo_documento = 'NOTA CREDITO CLIENTES'", nativeQuery = true)
	public List<Cuenta> obtenerCuentasPrecargadasDevolucionClientes(Integer idSede);
	
	@Query(value = "SELECT * FROM cuenta where (sede_id is null or sede_id = ?) and tipo_documento = 'Nota credito cliente'", nativeQuery = true)
	public List<Cuenta> obtenerCuentasCreditoCliente(Integer idSede);
	@Query(value="select * from cuenta where sede_id = ?1 and tipo_cuenta = 'Debito' and tipo_documento = 'CUENTAS BANCARIAS'", nativeQuery = true)
	public List<Cuenta> obtenerCuentasBancariasDebito(Integer idSede);
	
	@Query(value="select * from cuenta where sede_id = ?1 and tipo_cuenta = 'Credito' and tipo_documento = 'CUENTAS BANCARIAS'", nativeQuery = true)
	public List<Cuenta> obtenerCuentasBancariasCredito(Integer idSede);
	
	@Query(value="select * from cuenta where (sede_id = ?1 or sede_id is null) and tipo_documento = 'COMPROBANTE SERVICIOS'", nativeQuery = true)
	public List<Cuenta> obtenerCuentasComprobanteServicios(Integer idSede);
	
	@Query(value="select * from cuenta where (sede_id = ?1 or sede_id is null) and tipo_documento = 'COMPROBANTE GASTOS'", nativeQuery = true)
	public List<Cuenta> obtenerCuentasComprobanteGastos(Integer idSede);
	
	@Query(value="select * from cuenta where sede_id = ?1 and tipo_cuenta = 'EFECTIVO' and tipo_documento = 'CUENTAS BANCARIAS'", nativeQuery = true)
	public List<Cuenta> obtenerCuentasBancariasEfectivo(Integer idSede);
	
	@Query(value = "select * from cuenta where sede_id = ?1 and tipo_documento = 'CUENTAS BANCARIAS'", nativeQuery = true)
	public List<Cuenta> obtenerCuentasBancarias(Integer idSede);
	
	@Query(value = "select * from cuenta where (nombre like '%retencion%' or nombre like '%gravadas%' or nombre like '%iva%') \r\n" + 
			"and (sede_id is null or sede_id = ?1) and tipo_documento = 'NOTA CREDITO' or tipo_documento = 'NOTA DEBITO'\r\n" + 
			"or tipo_documento = 'Nota debito cliente' or tipo_documento = 'Nota credito cliente' \r\n" + 
			"or tipo_documento = 'COMPROBANTE SERVICIOS' or tipo_documento = 'COMPROBANTE GASTOS'", nativeQuery = true)
	public List<Cuenta> obtenerCuentasProveedores(Integer idSede);
	
	@Query(value = "select * from cuenta where (sede_id is null or sede_id = ?) and tipo_documento = 'NOTA CREDITO CLIENTES'", nativeQuery = true)
	public List<Cuenta> obtenerCuentasCreditoClientes(Integer idSede);
}


