package com.softlond.base.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softlond.base.entity.DevolucionCompras;
import com.softlond.base.entity.DevolucionVentasCliente;
import com.softlond.base.entity.FacturaCompra;

// import antlr.collections.List;

public interface DevolucionClienteDao extends CrudRepository<DevolucionVentasCliente, Integer> {

	@Query(value = "SELECT * FROM dev_ventas_cliente WHERE n_nro_documento = :nDocumento AND id_sede = :idSede ", nativeQuery = true)
	public Page<DevolucionVentasCliente> findBynDocumentoSede(@Param("idSede") Integer idSede,
			@Param("nDocumento") String nDocumento, Pageable pageable);

	@Query(value = "SELECT * FROM dev_ventas_cliente WHERE n_nro_documento = :nDocumento AND date_format(date(d_fecha_factura),'%Y-%m-%d') = date_format(date(:fechaInicial),'%Y-%m-%d') AND id_sede = :idSede", nativeQuery = true)
	public Page<DevolucionVentasCliente> findByDevolucionFechaInicial(@Param("idSede") Integer idSede,
			@Param("nDocumento") String nDocumento, @Param("fechaInicial") String fechaInicial, Pageable pageable);

	@Query(value = "SELECT * FROM dev_ventas_cliente WHERE n_nro_documento = :nDocumento  and date_format(date(d_fecha_factura),'%Y-%m-%d') BETWEEN date_format(date(:fechaInicial),'%Y-%m-%d') AND date_format(date(:fechaFinal), '%Y-%m-%d') AND id_sede = :idSede", nativeQuery = true)
	public Page<DevolucionVentasCliente> findByDevolucionFechas(@Param("idSede") Integer idSede,
			@Param("nDocumento") String nDocumento, @Param("fechaInicial") String fechaInicial,
			@Param("fechaFinal") String fechaFinal, Pageable pageable);

	@Query(value = "SELECT * FROM dev_ventas_cliente WHERE n_nro_documento = :nDocumento  AND id_sede = :idSede ", nativeQuery = true)
	public Page<DevolucionVentasCliente> findByDevolucionEstado1(@Param("idSede") Integer idSede,
			@Param("nDocumento") String nDocumento, Pageable pageable);

	@Query(value = "SELECT * FROM dev_ventas_cliente WHERE n_nro_documento = :nDocumento  AND id_sede = :idSede ", nativeQuery = true)
	public Page<DevolucionVentasCliente> findByDevolucionEstado2(@Param("idSede") Integer idSede,
			@Param("nDocumento") String nDocumento, Pageable pageable);

	@Query(value = "SELECT * FROM dev_ventas_cliente WHERE n_nro_documento = :nDocumento  AND id_sede = :idSede ", nativeQuery = true)
	public Page<DevolucionVentasCliente> findByDevolucionEstado3(@Param("idSede") Integer idSede,
			@Param("nDocumento") String nDocumento, Pageable pageable);

	@Query(value = "SELECT * FROM dev_ventas_cliente WHERE n_nro_documento = :nDocumento  AND id_sede = :idSede ", nativeQuery = true)
	public Page<DevolucionVentasCliente> findByDevolucionEstado4(@Param("idSede") Integer idSede,
			@Param("nDocumento") String nDocumento, Pageable pageable);

	@Query(value = "SELECT * FROM dev_ventas_cliente WHERE n_nro_documento = :nDocumento  AND id_sede = :idSede ", nativeQuery = true)
	public Page<DevolucionVentasCliente> findByDevolucionEstado5(@Param("idSede") Integer idSede,
			@Param("nDocumento") String nDocumento, Pageable pageable);

	@Query(value = "SELECT * FROM dev_ventas_cliente WHERE date_format(date(d_fecha_factura),'%Y-%m-%d') = date_format(date(:fechaInicial),'%Y-%m-%d') AND id_sede = :idSede", nativeQuery = true)
	public Page<DevolucionVentasCliente> findFechaInicial(@Param("idSede") Integer idSede,
			@Param("fechaInicial") String fechaInicial, Pageable pageable);

	@Query(value = "SELECT * FROM dev_ventas_cliente WHERE date_format(date(d_fecha_factura),'%Y-%m-%d') = date_format(date(:fechaFinal),'%Y-%m-%d') AND id_sede = :idSede", nativeQuery = true)
	public Page<DevolucionVentasCliente> findFechaFinal(@Param("idSede") Integer idSede,
			@Param("fechaFinal") String fechaFinal, Pageable pageable);

	@Query(value = "SELECT * FROM dev_ventas_cliente WHERE date_format(date(d_fecha_factura),'%Y-%m-%d') = date_format(date(:fechaInicial),'%Y-%m-%d') AND id_sede = :idSede", nativeQuery = true)
	public Page<DevolucionVentasCliente> findByFechaEstado1(@Param("idSede") Integer idSede,
			@Param("fechaInicial") String fechaInicial, Pageable pageable);

	@Query(value = "SELECT * FROM dev_ventas_cliente WHERE date_format(date(d_fecha_factura),'%Y-%m-%d') = date_format(date(:fechaInicial),'%Y-%m-%d') AND id_sede = :idSede", nativeQuery = true)
	public Page<DevolucionVentasCliente> findByFechaEstado2(@Param("idSede") Integer idSede,
			@Param("fechaInicial") String fechaInicial, Pageable pageable);

	@Query(value = "SELECT * FROM dev_ventas_cliente WHERE date_format(date(d_fecha_factura),'%Y-%m-%d') = date_format(date(:fechaInicial),'%Y-%m-%d') AND id_sede = :idSede", nativeQuery = true)
	public Page<DevolucionVentasCliente> findByFechaEstado3(@Param("idSede") Integer idSede,
			@Param("fechaInicial") String fechaInicial, Pageable pageable);

	@Query(value = "SELECT * FROM dev_ventas_cliente WHERE date_format(date(d_fecha_factura),'%Y-%m-%d') = date_format(date(:fechaInicial),'%Y-%m-%d') AND id_sede = :idSede", nativeQuery = true)
	public Page<DevolucionVentasCliente> findByFechaEstado4(@Param("idSede") Integer idSede,
			@Param("fechaInicial") String fechaInicial, Pageable pageable);

	@Query(value = "SELECT * FROM dev_ventas_cliente WHERE date_format(date(d_fecha_factura),'%Y-%m-%d') = date_format(date(:fechaInicial),'%Y-%m-%d') AND id_sede = :idSede", nativeQuery = true)
	public Page<DevolucionVentasCliente> findByFechaEstado5(@Param("idSede") Integer idSede,
			@Param("fechaInicial") String fechaInicial, Pageable pageable);

	@Query(value = "SELECT * FROM dev_ventas_cliente WHERE date_format(date(d_fecha_factura),'%Y-%m-%d') BETWEEN date_format(date(:fechaInicial),'%Y-%m-%d') AND "
			+ " date_format(date(:fechaFinal), '%Y-%m-%d') AND id_sede = :idSede ", nativeQuery = true)
	public Page<DevolucionVentasCliente> findByFechas(@Param("idSede") Integer idSede,
			@Param("fechaInicial") String fechaInicial, @Param("fechaFinal") String fechaFinal, Pageable pageable);

	@Query(value = "SELECT * FROM dev_ventas_cliente WHERE date_format(date(d_fecha_factura),'%Y-%m-%d') BETWEEN date_format(date(:fechaInicial),'%Y-%m-%d') AND "
			+ "	date_format(date(:fechaFinal), '%Y-%m-%d') AND id_sede = :idSede", nativeQuery = true)
	public Page<DevolucionVentasCliente> findByFechasEstado1(@Param("idSede") Integer idSede,
			@Param("fechaInicial") String fechaInicial, @Param("fechaFinal") String fechaFinal, Pageable pageable);

	@Query(value = "SELECT * FROM dev_ventas_cliente WHERE date_format(date(d_fecha_factura),'%Y-%m-%d') BETWEEN date_format(date(:fechaInicial),'%Y-%m-%d') AND "
			+ "	date_format(date(:fechaFinal), '%Y-%m-%d') AND id_sede = :idSede", nativeQuery = true)
	public Page<DevolucionVentasCliente> findByFechasEstado2(@Param("idSede") Integer idSede,
			@Param("fechaInicial") String fechaInicial, @Param("fechaFinal") String fechaFinal, Pageable pageable);

	@Query(value = "SELECT * FROM dev_ventas_cliente WHERE date_format(date(d_fecha_factura),'%Y-%m-%d') BETWEEN date_format(date(:fechaInicial),'%Y-%m-%d') AND "
			+ "	date_format(date(:fechaFinal), '%Y-%m-%d') AND id_sede = :idSede", nativeQuery = true)
	public Page<DevolucionVentasCliente> findByFechasEstado3(@Param("idSede") Integer idSede,
			@Param("fechaInicial") String fechaInicial, @Param("fechaFinal") String fechaFinal, Pageable pageable);

	@Query(value = "SELECT * FROM dev_ventas_cliente WHERE date_format(date(d_fecha_factura),'%Y-%m-%d') BETWEEN date_format(date(:fechaInicial),'%Y-%m-%d') AND "
			+ "	date_format(date(:fechaFinal), '%Y-%m-%d') AND id_sede = :idSede", nativeQuery = true)
	public Page<DevolucionVentasCliente> findByFechasEstado4(@Param("idSede") Integer idSede,
			@Param("fechaInicial") String fechaInicial, @Param("fechaFinal") String fechaFinal, Pageable pageable);

	@Query(value = "SELECT * FROM dev_ventas_cliente WHERE date_format(date(d_fecha_factura),'%Y-%m-%d') BETWEEN date_format(date(:fechaInicial),'%Y-%m-%d') AND "
			+ "	date_format(date(:fechaFinal), '%Y-%m-%d') AND id_sede = :idSede", nativeQuery = true)
	public Page<DevolucionVentasCliente> findByFechasEstado5(@Param("idSede") Integer idSede,
			@Param("fechaInicial") String fechaInicial, @Param("fechaFinal") String fechaFinal, Pageable pageable);

	@Query(value = "SELECT * FROM dev_ventas_cliente WHERE cod_estado_con = 3 AND id_sede = :idSede ", nativeQuery = true)
	public Page<DevolucionVentasCliente> findByDevolucionEstado1(@Param("idSede") Integer idSede, Pageable pageable);

	@Query(value = "SELECT * FROM dev_ventas_cliente WHERE cod_estado_con = 4 AND id_sede = :idSede ", nativeQuery = true)
	public Page<DevolucionVentasCliente> findByDevolucionEstado2(@Param("idSede") Integer idSede, Pageable pageable);

	@Query(value = "SELECT * FROM dev_ventas_cliente WHERE cod_estado_con = 5 AND id_sede = :idSede ", nativeQuery = true)
	public Page<DevolucionVentasCliente> findByDevolucionEstado3(@Param("idSede") Integer idSede, Pageable pageable);

	@Query(value = "SELECT * FROM dev_ventas_cliente WHERE cod_estado_con = 2 AND id_sede = :idSede ", nativeQuery = true)
	public Page<DevolucionVentasCliente> findByDevolucionEstado4(@Param("idSede") Integer idSede, Pageable pageable);

	@Query(value = "SELECT * FROM dev_ventas_cliente WHERE cod_estado_con = 1 AND id_sede = :idSede ", nativeQuery = true)
	public Page<DevolucionVentasCliente> findByDevolucionEstado5(@Param("idSede") Integer idSede, Pageable pageable);

	@Query(value = "SELECT * FROM dev_ventas_cliente WHERE id_sede = :idSede ", nativeQuery = true)
	public Page<DevolucionVentasCliente> Todo(@Param("idSede") Integer idSede, Pageable pageable);

	@Query(value = "SELECT * FROM dev_ventas_ventas_articulos WHERE id_dev = :id_dev ", nativeQuery = true)
	public Page<DevolucionVentasCliente> ListarArticulos(@Param("id_dev") Integer id_dev, Pageable pageable);

	@Query(value = "select * from dev_ventas_cliente dev inner join dev_ventas_ventas_articulos art on dev.nid_dev_venta_cliente = art.nid_dev_compra where dev.id_sede = ? and art.n_cantidad_devuelta = 5", nativeQuery = true)
	public ArrayList<DevolucionVentasCliente> obtenerCantidadDevolucion(Integer idSede);

	@Query(value = "select * from dev_ventas_cliente where month(d_fecha_factura) = month(CURRENT_DATE()) and year(d_fecha_factura) = year(CURRENT_DATE()) and id_sede=?1", nativeQuery = true)
	public Page<DevolucionVentasCliente> obtenerDevolucionesDelMes(Integer idSede, Pageable pageable);

	@Query(value = "select * from dev_ventas_cliente dv join dev_ventas_ventas_articulos da join fac_articulos fa on dv.nid_dev_venta_cliente=da.nid_dev_compra and fa.nid_fac_articulo=da.nid_factura_articulo where fa.nid_articulo=?1", nativeQuery = true)
	public List<DevolucionVentasCliente> obtenerDevolucionArticulo(Integer articulo);

}
