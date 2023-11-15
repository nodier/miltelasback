package com.softlond.base.repository;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.softlond.base.entity.Prefijo;

@Transactional
public interface PrefijoDao extends CrudRepository<Prefijo, Integer> {

	public ArrayList<Prefijo> findAllByOrderByFechaCreacionDesc();

	public Prefijo findByPrefijo(String prefijo);

	// Prefijos tipo de venta Credito
	@Query(value = "SELECT * FROM prefijo WHERE estado= 'Activo' AND id_tipo_venta = 15 AND id_sede =?", nativeQuery = true)
	public ArrayList<Prefijo> obtenerPrefijoSede(Integer idSede);

	// Prefijos tipo de venta Contado
	@Query(value = "SELECT *  FROM prefijo WHERE estado= 'Activo' AND id_tipo_venta = 14 AND id_sede =?", nativeQuery = true)
	public ArrayList<Prefijo> obtenerPrefijoSedeContado(Integer idSede);

	// Prefijos egresos
	@Query(value = "SELECT *  FROM prefijo WHERE LOWER(tipo_prefijo) = 'comprobante egreso' AND id_sede = :idSede", nativeQuery = true)
	public List<Prefijo> obtenerPrefijosEgresoSede(Integer idSede);

	@Query(value = "SELECT *  FROM prefijo WHERE LOWER(tipo_prefijo) = 'comprobante egreso'", nativeQuery = true)
	public List<Prefijo> obtenerPrefijosEgreso();

	@Query(value = "SELECT * FROM prefijo WHERE LOWER(tipo_prefijo) = 'comprobante egreso' AND tipo_gastos = :tipoGastos AND tipo_nomina = :tipoNomina AND tipo_proveedores = :tipoProveedores", nativeQuery = true)
	public List<Prefijo> obtenerPrefijosEgresoTipo(Boolean tipoGastos, Boolean tipoProveedores, Boolean tipoNomina);

	@Query(value = "SELECT * FROM prefijo WHERE LOWER(tipo_prefijo) = 'comprobante egreso' AND tipo_gastos = :tipoGastos AND tipo_nomina = :tipoNomina AND tipo_proveedores = :tipoProveedores AND id_sede = :idSede", nativeQuery = true)
	public List<Prefijo> obtenerPrefijosEgresoTipoYSede(Boolean tipoGastos, Boolean tipoProveedores, Boolean tipoNomina,
			Integer idSede);

	// Prefijos devoluciones
	@Query(value = "SELECT *  FROM prefijo WHERE estado= 'Activo' AND id_sede =? AND tipo_documento = 'Devolucion'", nativeQuery = true)
	public ArrayList<Prefijo> obtenerPrefijoDevolucion(Integer idSede);

	// Prefijos notas creditos
	@Query(value = "SELECT *  FROM prefijo WHERE estado= 'Activo' AND id_sede =? AND tipo_documento = 'Nota credito'", nativeQuery = true)
	public ArrayList<Prefijo> obtenerPrefijoNotaCredito(Integer idSede);

	// Prefijos notas debito
	@Query(value = "SELECT *  FROM prefijo WHERE estado= 'Activo' AND id_sede =? AND tipo_documento = 'Nota debito'", nativeQuery = true)
	public ArrayList<Prefijo> obtenerPrefijoNotaDebito(Integer idSede);

	// Prefijos remision venta
	@Query(value = "SELECT *  FROM prefijo WHERE estado= 'Activo' AND id_sede =? AND tipo_documento = 'Remision venta'", nativeQuery = true)
	public List<Prefijo> obtenerPrefijoRemisionVenta(Integer idSede);

	// Prefijos notas creditos
	@Query(value = "SELECT *  FROM prefijo WHERE estado= 'Activo' AND id_sede =? AND tipo_documento = 'Nota credito cliente'", nativeQuery = true)
	public ArrayList<Prefijo> obtenerPrefijoNotaCreditoCliente(Integer idSede);

	// Prefijos notas debito cliente
	@Query(value = "SELECT *  FROM prefijo WHERE estado= 'Activo' AND id_sede =? AND tipo_documento = 'Nota debito cliente'", nativeQuery = true)
	public ArrayList<Prefijo> obtenerPrefijoNotaDebitoCliente(Integer idSede);

	@Query(value = "SELECT *  FROM prefijo WHERE estado= 'Activo' AND id_tipo_venta = 15 AND id_sede =?1 AND prefijo = ?2", nativeQuery = true)
	public Prefijo obtenerPrefijoPorNombreSede(Integer idSede, String prefijo);

	// Prefijos remision venta
	@Query(value = "SELECT *  FROM prefijo WHERE estado= 'Activo' AND id_sede =? AND tipo_documento = 'Factura electronica'", nativeQuery = true)
	public List<Prefijo> obtenerPrefijoFacturaElectronica(Integer idSede);

	// Prefijos remision venta
	@Query(value = "SELECT *  FROM prefijo WHERE estado= 'Activo' AND id_sede =? AND tipo_documento = 'Recibo de caja'", nativeQuery = true)
	public List<Prefijo> obtenerPrefijoReciboCaja(Integer idSede);

	// Prefijos remision venta
	@Query(value = "SELECT *  FROM prefijo WHERE estado= 'Activo' AND id_sede =? AND tipo_documento = 'Remision venta' limit 1", nativeQuery = true)
	public Prefijo obtenerPrefijoRemision(Integer idSede);

	@Query(value = "SELECT *  FROM prefijo WHERE estado= 'Activo' AND id_tipo_venta = 15 AND id_sede =?1 AND tipo_documento = 'Remision venta' limit 1", nativeQuery = true)
	public Prefijo obtenerPrefijoRemisionSede(Integer idSede);

	// @Query(value = "SELECT * FROM prefijo WHERE estado= 'Activo' AND id_sede =?
	// AND id_tipo_venta = 26", nativeQuery = true)
	// public List<Prefijo> obtenerPrefijoInventario(Integer idSede);

	@Query(value = "SELECT *  FROM prefijo WHERE estado= 'Activo' AND id_sede =? AND id_tipo_venta in(26, 27, 28)", nativeQuery = true)
	public List<Prefijo> obtenerPrefijoInventario(Integer idSede);

	@Query(value = "SELECT *  FROM prefijo WHERE estado= 'Activo' AND id_sede =? AND tipo_documento = 'Devolucion cliente'", nativeQuery = true)
	public ArrayList<Prefijo> obtenerPrefijoDevolucionCliente(Integer idSede);

	@Query(value = "SELECT * FROM prefijo WHERE estado= 'Activo' AND id_sede =? AND tipo_documento = 'Informe Diario'", nativeQuery = true)
	public Prefijo obtenerPrefijoInformeDiario(Integer idSede);

	@Query(value = "SELECT *  FROM prefijo WHERE tipo_documento = 'Comprobante de egreso' and id_sede =?1", nativeQuery = true)
	public List<Prefijo> obtenerPrefijosEgreso1(Integer sede);
}
