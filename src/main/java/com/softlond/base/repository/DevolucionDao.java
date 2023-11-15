package com.softlond.base.repository;

import java.util.ArrayList;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softlond.base.entity.DevolucionCompras;
import com.softlond.base.entity.FacturaCompra;

public interface DevolucionDao extends CrudRepository<DevolucionCompras, Integer>{
		

	@Query(value = "SELECT * FROM dev_compras WHERE n_nro_documento like %:nDocumento% AND id_sede = :idSede", nativeQuery = true)
	public Page<DevolucionCompras> findBynDocumentoSede(@Param("idSede") Integer idSede, @Param ("nDocumento") String nDocumento, Pageable pageable);

	@Query(value = "SELECT * FROM dev_compras WHERE n_nro_documento = :nDocumento AND date_format(date(d_fecha_factura),'%Y-%m-%d') = date_format(date(:fechaInicial),'%Y-%m-%d') AND id_sede = :idSede", nativeQuery = true)
	public Page<DevolucionCompras> findByDevolucionFechaInicial(@Param("idSede") Integer idSede, @Param ("nDocumento") String nDocumento, @Param ("fechaInicial") String fechaInicial, Pageable pageable );

	@Query(value = "SELECT * FROM dev_compras WHERE n_nro_documento = :nDocumento  and date_format(date(d_fecha_factura),'%Y-%m-%d') BETWEEN date_format(date(:fechaInicial),'%Y-%m-%d') AND date_format(date(:fechaFinal), '%Y-%m-%d') AND id_sede = :idSede", nativeQuery = true)
	public Page<DevolucionCompras> findByDevolucionFechas(@Param("idSede") Integer idSede, @Param ("nDocumento") String nDocumento, @Param ("fechaInicial") String fechaInicial, @Param ("fechaFinal") String fechaFinal, Pageable pageable );

	@Query(value = "SELECT * FROM dev_compras WHERE n_nro_documento = :nDocumento  AND id_sede = :idSede ", nativeQuery = true)
	public Page<DevolucionCompras> findByDevolucionEstado1 (@Param("idSede") Integer idSede,  @Param ("nDocumento") String nDocumento, Pageable pageable);

	@Query(value = "SELECT * FROM dev_compras WHERE n_nro_documento = :nDocumento  AND id_sede = :idSede ", nativeQuery = true)
	public Page<DevolucionCompras> findByDevolucionEstado2 (@Param("idSede") Integer idSede, @Param ("nDocumento") String nDocumento, Pageable pageable);

	@Query(value = "SELECT * FROM dev_compras WHERE n_nro_documento = :nDocumento  AND id_sede = :idSede ", nativeQuery = true)
	public Page<DevolucionCompras> findByDevolucionEstado3 (@Param("idSede") Integer idSede, @Param ("nDocumento") String nDocumento, Pageable pageable);
	

	@Query(value = "SELECT * FROM dev_compras WHERE n_nro_documento = :nDocumento  AND id_sede = :idSede ", nativeQuery = true)
	public Page<DevolucionCompras> findByDevolucionEstado4(@Param("idSede") Integer idSede, @Param ("nDocumento") String nDocumento, Pageable pageable);
	
	@Query(value = "SELECT * FROM dev_compras WHERE n_nro_documento = :nDocumento  AND id_sede = :idSede ", nativeQuery = true)
	public Page<DevolucionCompras> findByDevolucionEstado5(@Param("idSede") Integer idSede, @Param ("nDocumento") String nDocumento, Pageable pageable);

	@Query(value = "SELECT * FROM dev_compras WHERE date_format(date(d_fecha_factura),'%Y-%m-%d') = date_format(date(:fechaInicial),'%Y-%m-%d') AND id_sede = :idSede", nativeQuery = true)
	public Page<DevolucionCompras>  findFechaInicial (@Param("idSede") Integer idSede, @Param("fechaInicial") String fechaInicial, Pageable pageable);
	
	@Query(value = "SELECT * FROM dev_compras WHERE date_format(date(d_fecha_factura),'%Y-%m-%d') = date_format(date(:fechaFinal),'%Y-%m-%d') AND id_sede = :idSede", nativeQuery = true)
	public Page<DevolucionCompras>  findFechaFinal (@Param("idSede") Integer idSede, @Param("fechaFinal") String fechaFinal, Pageable pageable);

	@Query(value = "SELECT * FROM dev_compras WHERE date_format(date(d_fecha_factura),'%Y-%m-%d') = date_format(date(:fechaInicial),'%Y-%m-%d') AND id_sede = :idSede", nativeQuery = true)
	public Page<DevolucionCompras> findByFechaEstado1 (@Param("idSede") Integer idSede, @Param ("fechaInicial") String fechaInicial, Pageable pageable );

	@Query(value = "SELECT * FROM dev_compras WHERE date_format(date(d_fecha_factura),'%Y-%m-%d') = date_format(date(:fechaInicial),'%Y-%m-%d') AND id_sede = :idSede", nativeQuery = true)
	public Page<DevolucionCompras> findByFechaEstado2 (@Param("idSede") Integer idSede, @Param ("fechaInicial") String fechaInicial, Pageable pageable );

	@Query(value = "SELECT * FROM dev_compras WHERE date_format(date(d_fecha_factura),'%Y-%m-%d') = date_format(date(:fechaInicial),'%Y-%m-%d') AND id_sede = :idSede", nativeQuery = true)
	public Page<DevolucionCompras> findByFechaEstado3 (@Param("idSede") Integer idSede, @Param ("fechaInicial") String fechaInicial, Pageable pageable );
	
	@Query(value = "SELECT * FROM dev_compras WHERE date_format(date(d_fecha_factura),'%Y-%m-%d') = date_format(date(:fechaInicial),'%Y-%m-%d') AND id_sede = :idSede", nativeQuery = true)
	public Page<DevolucionCompras> findByFechaEstado4 (@Param("idSede") Integer idSede, @Param ("fechaInicial") String fechaInicial, Pageable pageable );
	
	@Query(value = "SELECT * FROM dev_compras WHERE date_format(date(d_fecha_factura),'%Y-%m-%d') = date_format(date(:fechaInicial),'%Y-%m-%d') AND id_sede = :idSede", nativeQuery = true)
	public Page<DevolucionCompras> findByFechaEstado5 (@Param("idSede") Integer idSede, @Param ("fechaInicial") String fechaInicial, Pageable pageable );

	@Query(value = "SELECT * FROM dev_compras WHERE date_format(date(d_fecha_factura),'%Y-%m-%d') BETWEEN date_format(date(:fechaInicial),'%Y-%m-%d') AND "
			+ " date_format(date(:fechaFinal), '%Y-%m-%d') AND id_sede = :idSede ", nativeQuery = true)
	public Page<DevolucionCompras> findByFechas(@Param ("idSede") Integer idSede, @Param ("fechaInicial") String fechaInicial, @Param ("fechaFinal") String fechaFinal, Pageable pageable);

	@Query(value = "SELECT * FROM dev_compras WHERE date_format(date(d_fecha_factura),'%Y-%m-%d') BETWEEN date_format(date(:fechaInicial),'%Y-%m-%d') AND "
			+ "	date_format(date(:fechaFinal), '%Y-%m-%d') AND id_sede = :idSede", nativeQuery = true)
	public Page<DevolucionCompras> findByFechasEstado1(@Param("idSede") Integer idSede, @Param ("fechaInicial") String fechaInicial, @Param ("fechaFinal") String fechaFinal, Pageable pageable );

	@Query(value = "SELECT * FROM dev_compras WHERE date_format(date(d_fecha_factura),'%Y-%m-%d') BETWEEN date_format(date(:fechaInicial),'%Y-%m-%d') AND "
			+ "	date_format(date(:fechaFinal), '%Y-%m-%d') AND id_sede = :idSede", nativeQuery = true)
	public Page<DevolucionCompras> findByFechasEstado2(@Param("idSede") Integer idSede, @Param ("fechaInicial") String fechaInicial, @Param ("fechaFinal") String fechaFinal, Pageable pageable );
	
	@Query(value = "SELECT * FROM dev_compras WHERE date_format(date(d_fecha_factura),'%Y-%m-%d') BETWEEN date_format(date(:fechaInicial),'%Y-%m-%d') AND "
			+ "	date_format(date(:fechaFinal), '%Y-%m-%d') AND id_sede = :idSede", nativeQuery = true)
	public Page<DevolucionCompras> findByFechasEstado3(@Param("idSede") Integer idSede, @Param ("fechaInicial") String fechaInicial, @Param ("fechaFinal") String fechaFinal, Pageable pageable );
	
	@Query(value = "SELECT * FROM dev_compras WHERE date_format(date(d_fecha_factura),'%Y-%m-%d') BETWEEN date_format(date(:fechaInicial),'%Y-%m-%d') AND "
			+ "	date_format(date(:fechaFinal), '%Y-%m-%d') AND id_sede = :idSede", nativeQuery = true)
	public Page<DevolucionCompras> findByFechasEstado4(@Param("idSede") Integer idSede, @Param ("fechaInicial") String fechaInicial, @Param ("fechaFinal") String fechaFinal, Pageable pageable );

	@Query(value = "SELECT * FROM dev_compras WHERE date_format(date(d_fecha_factura),'%Y-%m-%d') BETWEEN date_format(date(:fechaInicial),'%Y-%m-%d') AND "
			+ "	date_format(date(:fechaFinal), '%Y-%m-%d') AND id_sede = :idSede", nativeQuery = true)
	public Page<DevolucionCompras> findByFechasEstado5(@Param("idSede") Integer idSede, @Param ("fechaInicial") String fechaInicial, @Param ("fechaFinal") String fechaFinal, Pageable pageable );
	
	@Query(value = "SELECT * FROM dev_compras WHERE cod_estado_con = 3 AND id_sede = :idSede ", nativeQuery = true)
	public Page<DevolucionCompras> findByDevolucionEstado1 (@Param("idSede") Integer idSede, Pageable pageable);
	
	@Query(value = "SELECT * FROM dev_compras WHERE cod_estado_con = 4 AND id_sede = :idSede ", nativeQuery = true)
	public Page<DevolucionCompras> findByDevolucionEstado2 (@Param("idSede") Integer idSede, Pageable pageable);
	
	@Query(value = "SELECT * FROM dev_compras WHERE cod_estado_con = 5 AND id_sede = :idSede ", nativeQuery = true)
	public Page<DevolucionCompras> findByDevolucionEstado3 (@Param("idSede") Integer idSede, Pageable pageable);
	
	@Query(value = "SELECT * FROM dev_compras WHERE cod_estado_con = 2 AND id_sede = :idSede ", nativeQuery = true)
	public Page<DevolucionCompras> findByDevolucionEstado4 (@Param("idSede") Integer idSede, Pageable pageable);
	
	@Query(value = "SELECT * FROM dev_compras WHERE cod_estado_con = 1 AND id_sede = :idSede ", nativeQuery = true)
	public Page<DevolucionCompras> findByDevolucionEstado5 (@Param("idSede") Integer idSede, Pageable pageable);
	
	@Query(value = "SELECT * FROM dev_compras WHERE id_sede = :idSede ", nativeQuery = true)
	public Page<DevolucionCompras> Todo (@Param("idSede") Integer idSede, Pageable pageable);
	
	@Query(value = "SELECT * FROM dev_compras_articulos WHERE id_dev = :id_dev ", nativeQuery = true)
	public Page<DevolucionCompras> ListarArticulos (@Param("id_dev") Integer id_dev, Pageable pageable);
	
	@Query(value = "select * from dev_compras dev inner join dev_compras_articulos art on dev.nid_dev_compra = art.nid_dev_compra where dev.id_sede = ? and art.n_cantidad_devuelta = 5", nativeQuery = true)
	public ArrayList<DevolucionCompras> obtenerCantidadDevolucion(Integer idSede);
	
	@Query(value="select * from dev_compras where month(d_fecha_factura) = month(CURRENT_DATE()) and year(d_fecha_factura) = year(CURRENT_DATE()) and id_sede=?1", nativeQuery = true)
	  public Page<DevolucionCompras> obtenerDevolucionesDelMes(Integer idSede, Pageable pageable);
	
}

