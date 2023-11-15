package com.softlond.base.repository;

import com.softlond.base.entity.Articulo;
import com.softlond.base.entity.ArticuloMovimientos;
import com.softlond.base.entity.EstadoArticulo;
import com.softlond.base.entity.FacturaArticulos;
import com.softlond.base.entity.PrdTipos;
import com.softlond.base.entity.Precio;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import java.math.BigInteger;

@Transactional
public interface ArticuloDao extends CrudRepository<Articulo, Integer> {
        public Articulo findByCodigo(String codigo);

        @Query(value = "SELECT * FROM articulo WHERE id_sede= ? and (cantidad_disponible >0 and nid_estado_articulo not in(2,4)) and id_producto is not null", nativeQuery = true)
        public List<Articulo> buscarPorSede(Integer idSede);

        @Query(value = "SELECT max(codigo) FROM articulo WHERE id_sede= ?1 and fecha_ingreso = (SELECT max(fecha_ingreso) FROM articulo WHERE id_sede= ?1)", nativeQuery = true)
        public String codigo(Integer idSede);

        @Query(value = "SELECT * FROM articulo WHERE id_producto = :idProducto", nativeQuery = true)
        public List<Articulo> buscarPorProducto(Integer idProducto);

        @Query(value = "SELECT * FROM articulo WHERE id_producto = :idProducto and id_sede=:idSede", nativeQuery = true)
        public List<Articulo> buscarPorProductoSede(Integer idProducto, Integer idSede);

        @Query(value = "SELECT * FROM articulo WHERE id_producto = :idProducto AND id_sede = :idSede", nativeQuery = true)
        public List<Articulo> buscarPorProductoYSede(Integer idProducto, Integer idSede);

        @Query(value = "select max(a.cantidad_compra) from articulo a join articulos_remision_compra ar on a.id = ar.id_articulo join remision_compra r on ar.id_remision_compra = r.id\n"
                        + "where r.id_proveedor = ?2 and a.id_producto = ?1 and a.fecha_ingreso = (select max(a.fecha_ingreso) from articulo a join articulos_remision_compra ar on \n"
                        + "a.id = ar.id_articulo join remision_compra r on ar.id_remision_compra = r.id \n"
                        + "where r.id_proveedor = ?2 and a.id_producto = ?1);", nativeQuery = true)
        public Integer obtenerArticulosProducto(Integer idProducto, Integer idProveedor);

        @Query(value = "select max(a.precio_costo) from articulo a join articulos_remision_compra ar on a.id = ar.id_articulo join remision_compra r on ar.id_remision_compra = r.id\n"
                        + "where r.id_proveedor = ?2 and a.id_producto = ?1 and a.fecha_ingreso = (select max(a.fecha_ingreso) from articulo a join articulos_remision_compra ar on \n"
                        + "a.id = ar.id_articulo join remision_compra r on ar.id_remision_compra = r.id \n"
                        + "where r.id_proveedor = ?2 and a.id_producto = ?1);", nativeQuery = true)
        public Integer ultimoPrecio(Integer idProducto, Integer idProveedor);

        @Query(value = "select * from articulo where id_producto = ? and id not in(select id_articulo from articulos_remision_compra)", nativeQuery = true)
        public List<Articulo> obtenerArticulosSinRemision(Integer idProducto);

        @Query(value = "select * from articulo a left join articulos_remision_compra ar on a.id = ar.id_articulo "
                        + "join producto p on a.id_producto = p.id"
                        + " where ar.id_articulo is null and (CONCAT(a.codigo,p.producto) like %?1%) and a.id_sede =?2", nativeQuery = true)
        public Page<Articulo> obtenerArticulosDisponibles(String texto, Integer idSede, Pageable page);

        @Query(value = "SELECT * FROM articulo WHERE id IN (SELECT id_articulo FROM articulos_remision_compra WHERE id_remision_compra = :idRemision)", nativeQuery = true)
        public List<Articulo> buscarPorRemision(Integer idRemision);

        // @Query(value = "SELECT * FROM articulo WHERE id IN (SELECT id_articulo FROM
        // articulos_remision_compra WHERE id_remision_compra = :idRemision) and id_sede
        // = :idSede", nativeQuery = true)
        // public List<Articulo> buscarPorRemision(Integer idRemision, Integer idSede);

        @Query(value = "SELECT * FROM articulo WHERE id_producto IN (SELECT id FROM producto WHERE codigo LIKE :codigo%) AND nid_estado_articulo in (1,2)", nativeQuery = true)
        public List<Articulo> buscarPorCodigoProducto(String codigo);

        @Query(value = "SELECT * FROM articulo WHERE id_producto IN (SELECT id FROM producto WHERE codigo LIKE :codigo%) AND nid_estado_articulo in (1,2) and id_sede = :idSede", nativeQuery = true)
        public List<Articulo> buscarPorCodigoProducto(String codigo, Integer idSede);

        @Query(value = "SELECT * FROM articulo a WHERE a.sector = :idSector AND a.nid_estado_articulo in (1,2)", nativeQuery = true)
        public List<Articulo> buscarPorSector(Integer idSector);

        @Query(value = "SELECT * FROM articulo a WHERE a.sector = :idSector AND a.nid_estado_articulo in (1,2) and id_sede = :idSede", nativeQuery = true)
        public List<Articulo> buscarPorSector(Integer idSector, Integer idSede);

        @Query(value = "SELECT * FROM articulo WHERE lower(codigo) LIKE :codigo% AND nid_estado_articulo in (1,2)", nativeQuery = true)
        public List<Articulo> buscarPorCodigo(String codigo);

        @Query(value = "SELECT * FROM articulo WHERE lower(codigo) LIKE :codigo% AND nid_estado_articulo in (1,2,3,4) and id_sede = :idSede", nativeQuery = true)
        public List<Articulo> buscarPorCodigo(String codigo, Integer idSede);

        @Query(value = "select count(cantidad_disponible) from articulo where id_producto= ?1 and cantidad_disponible > 0", nativeQuery = true)
        public Integer cantidadProducto(Integer id);

        /*
         * @Query(value =
         * "select sum(cantidad_disponible) from articulo where id_producto= ?1 and cantidad_disponible > 0"
         * , nativeQuery = true)
         * public Double sumaDisponibleProducto(Integer id);
         */

        @Query(value = "select sum(cantidad_disponible) from articulo a join producto p on a.id_producto = p.id where p.id= :id and a.cantidad_disponible > 0 and a.id_sede = :sede group by a.id_sede", nativeQuery = true)
        public Double sumaDisponibleProducto(Integer id, Integer sede);

        @Query(value = "select distinct a.codigo from articulo a join producto p on a.id_producto =p.id\r\n" +
                        "where a.codigo = (select max(codigo) from articulo where id_producto = :idProducto and cantidad_disponible <= 0) and p.id = :idProducto", nativeQuery = true)
        public String obtenerUltId(Integer idProducto);

        @Query(value = "select distinct a.fecha_ingreso from articulo a join producto as p on a.id_producto=p.id\r\n" +
                        "where a.fecha_ingreso = (select max(fecha_ingreso) from articulo where id_producto = :idProducto and cantidad_disponible <=0) and p.id = :idProducto", nativeQuery = true)
        public Date obtenerUltFechaCompra(Integer idProducto);

        @Query(value = "select count(cantidad_disponible) from articulo where id_producto= ?1 and cantidad_disponible > 0 ", nativeQuery = true)
        public List<Articulo> findAllByArticuloProducto(Integer id);

        @Query(value = "select count(cantidad_disponible) from articulo where id_producto= ?1 and cantidad_disponible > 0 and id_sede=15", nativeQuery = true)
        public Integer cantidadArticuloProducto(Integer id);

        @Query(value = "SELECT * FROM articulo WHERE id_producto = ?1 and id_sede =15", nativeQuery = true)
        public List<Articulo> obtenerArticuloProducto(Integer idProducto);

        @Query(value = "select ifnull(count(*),0) from articulo where nid_estado_articulo != 4 and id_producto = ?", nativeQuery = true)
        public Double obtenerCantidadArticulos(Integer idProducto);

        @Query(value = "SELECT * FROM articulo where id_sede = ?1 and id = ?2", nativeQuery = true)
        public Articulo obtenerArticuloId(Integer idSede, Integer idArtiuclo);

        @Query(value = "SELECT * FROM articulo WHERE id_producto = :idProducto and nid_estado_articulo = 1", nativeQuery = true)
        public Page<Articulo> buscarPorProductoDisponible(Integer idProducto, Pageable page);

        @Query(value = "SELECT sum(cantidad_disponible*precio_costo) FROM articulo WHERE id_producto = :idProducto and nid_estado_articulo = 1", nativeQuery = true)
        public BigInteger findByPagoTotalArticulosInventario(Integer idProducto);

        @Modifying
        @Query(value = "update articulo SET id_producto = null where id_producto = ?", nativeQuery = true)
        public void desvincularProductoDeArticulos(Integer idproducto);

        @Modifying
        @Query(value = "update articulo SET id_producto = ?1 where id = ?2", nativeQuery = true)
        public void vincularProductoDeArticulos(Integer idproducto, Integer idArticulo);

        @Query(value = "select * from articulo where codigo like %?% limit 50", nativeQuery = true)
        public List<Articulo> obtenerArticulosTexto(String texto);

        @Query(value = "SELECT * FROM articulo", nativeQuery = true)
        public List<Articulo> buscarArticulo();

        @Query(value = "SELECT * FROM articulo WHERE id_sede = :idSede and local = :local", nativeQuery = true)
        public List<Articulo> buscarArticulosPorLocal(Integer idSede, Integer local);

        @Query(value = "SELECT * FROM articulo WHERE cantidad_disponible > 0 and id_sede = :idSede", nativeQuery = true)
        public Page<Articulo> buscarDisponible(Integer idSede, Pageable page);

        @Query(value = "SELECT sum(cantidad_disponible*precio_costo) FROM articulo WHERE cantidad_disponible > 0 and id_sede = :idSede", nativeQuery = true)
        public BigInteger findByPagoTotalArticulosDisponibles(Integer idSede);

        @Query(value = "SELECT * FROM articulo WHERE cantidad_disponible > 0 and id_sede = :idSede and local = :local and sector = :sector and nid_estado_articulo in (1,2)", nativeQuery = true)
        public Page<Articulo> buscarDisponibleLocalSector(Integer idSede, String local, String sector, Pageable page);

        @Query(value = "SELECT sum(cantidad_disponible*precio_costo) FROM articulo where id_sede = :idSede and local = :local and sector = :sector and nid_estado_articulo in (1,2)", nativeQuery = true)
        public BigInteger findByPagoTotalArticulosLocalSector(Integer idSede, String local, String sector);

        @Query(value = "SELECT * FROM articulo  WHERE cantidad_disponible > 0 and id_sede = :idSede and local = :local and nid_estado_articulo in (1,2)", nativeQuery = true)
        public Page<Articulo> buscarDisponibleLocal(Integer idSede, String local, Pageable page);

        @Query(value = "SELECT sum(cantidad_disponible*precio_costo) FROM articulo where id_sede = :idSede and local = :local and nid_estado_articulo in (1,2)", nativeQuery = true)
        public BigInteger findByPagoTotalArticulosLocal(Integer idSede, String local);

        @Query(value = "SELECT * FROM articulo WHERE id_sede = :idSede and nid_estado_articulo in (1,2) and cantidad_disponible > 0", nativeQuery = true)
        public Page<Articulo> obtenerTodosSede(Integer idSede, Pageable page);

        // @Query(value = "select a.*, p.* from articulo as a inner join producto as p
        // inner join (select CONCAT(pt.t_tipo, ' ', pr.t_referencia,' ',
        // pp.t_presentacion, ' ', pc.t_color) as productox, pin.* from producto as pin
        // inner join prd_colores as pc inner join prd_tipos as pt inner join
        // prd_referencia as pr inner join prd_presentacion as pp on pin.id_color =
        // pc.id and pin.tipo = pt.id and pin.referencia = pr.id and pin.id_presentacion
        // = pp.id) as n on p.id = a.id_producto and p.id = n.id where
        // a.nid_estado_articulo in (1,2) and a.cantidad_disponible > 0 and a.id_sede =
        // :idSede", nativeQuery = true)
        // public Page<Articulo> obtenerTodosSede(Integer idSede, Pageable page);

        @Query(value = "SELECT sum(cantidad_disponible*precio_costo) FROM articulo where id_sede = :idSede and nid_estado_articulo in (1,2) and cantidad_disponible > 0", nativeQuery = true)
        public BigInteger findByPagoTotalArticulos(Integer idSede);

        @Query(value = "select a.* from articulo as a inner join producto as p inner join (select CONCAT(pt.t_tipo, ' ', pr.t_referencia,' ', pp.t_presentacion, ' ', pc.t_color) as productox, pin.* from producto as pin inner join prd_colores as pc inner join prd_tipos as pt inner join prd_referencia as pr inner join prd_presentacion as pp on pin.id_color = pc.id and pin.tipo = pt.id and pin.referencia = pr.id and pin.id_presentacion = pp.id) as n on p.id = a.id_producto and p.id = n.id where a.cantidad_disponible > 0 and a.nid_estado_articulo in (1,2) and a.id_sede =?1 and (a.codigo like %?2% or n.productox like %?2%) limit 20", nativeQuery = true)
        public List<Articulo> buscarPorSedeCodigo(Integer idSede, String codigo);

        @Query(value = "select a.* from articulo as a inner join producto as p inner join (select CONCAT(pt.t_tipo, ' ', pr.t_referencia,' ', pp.t_presentacion, ' ', pc.t_color) as productox, pin.* from producto as pin inner join prd_colores as pc inner join prd_tipos as pt inner join prd_referencia as pr inner join prd_presentacion as pp on pin.id_color = pc.id and pin.tipo = pt.id and pin.referencia = pr.id and pin.id_presentacion = pp.id) as n on p.id = a.id_producto and p.id = n.id where a.cantidad_disponible > 0 and a.nid_estado_articulo in (1,2) and a.id_sede =?1 and a.local=?3 and (a.codigo like %?2% or n.productox like %?2%) limit 20", nativeQuery = true)
        public List<Articulo> buscarPorSedeCodigoLocal(Integer idSede, String codigo, Integer local);

        /*
         * @Query(value =
         * "select a.* from articulo as a inner join producto as p on p.id = a.id_producto where a.cantidad_disponible > 0 and a.nid_estado_articulo in (1,2) and a.id_sede =?1 and a.codigo = ?2 limit 1"
         * , nativeQuery = true)
         * public Articulo buscarPorSedeCodigo(Integer idSede, String codigo);
         */

        @Query(value = "select * from articulo a join producto p on a.id_producto = p.id and a.nid_estado_articulo in (1,2) and a.id_producto = :idProducto and id_sede = :idSede", nativeQuery = true)
        public Page<Articulo> obtenerSedeExistenteProductos(Integer idSede, Integer idProducto, Pageable page);

        @Query(value = "SELECT * FROM articulo where id_sede = ?2 and codigo = ?1", nativeQuery = true)
        public Articulo obtenerCodigoSede(String codigo, Integer idSede);

        @Query(value = "SELECT * FROM articulo_movimientos where id_articulo = ?1", nativeQuery = true)
        public List<Articulo> obtenerCodigoSedeInventario(Integer idArticulo);

        // @Query(value = "SELECT * FROM articulo where id_sede = ?2 and codigo = ?1",
        // nativeQuery = true)
        // public ArrayList<Articulo> obtenerArticuloInformeInventario(String codigo,
        // Integer idSede);

        @Query(value = "SELECT cantidad_disponible FROM articulo where id = ?1", nativeQuery = true)
        public Float obtenerCantDispArticulo(Number id);

        @Query(value = "SELECT cantidad_disponible FROM articulo where id = ?1", nativeQuery = true)
        public Float obtenerCantDispArticuloFac(Integer id);

        /*
         * @Query(
         * value="select precio_costo from articulo where id_producto=?1 and id_sede=?2 and id=(select max(id) from articulo where id_sede=?2 and id_producto=?1)"
         * , nativeQuery = true)
         * public Double obtenerUltimoPrecioCosto(Integer idProducto, Integer idSede);
         */

        @Query(value = "select m_precio_costo_calc from prd_precios where nid_producto=?1 and id_pyme =?2 and b_activo =1 and nid_precio=(select max(nid_precio) from prd_precios where id_pyme=?2 and nid_producto=?1 and b_activo=1);", nativeQuery = true)
        public Double obtenerUltimoPrecioCosto(Integer idProducto, Integer idSede);

        // @Query(value="SELECT m_precio_venta FROM prd_precios where nid_producto=?1
        // and b_activo=1 and id_pyme= ?2 and nid_precio=(select max(nid_precio) from
        // prd_precios where id_pyme=?2 and nid_producto=?1 and b_activo=1);",
        // nativeQuery = true)
        // public Double obtenerPrecioVenta(Integer idProducto, Integer idsede);
        @Query(value = "SELECT m_precio_venta FROM prd_precios where nid_producto=?1 and id_pyme= ?2 and nid_precio=(select max(nid_precio) from prd_precios where id_pyme=?2 and nid_producto=?1);", nativeQuery = true)
        public Double obtenerPrecioVenta(Integer idProducto, Integer idsede);

        @Modifying
        @Query(value = "update articulo a SET cantidad_disponible = ?1 where id = ?2", nativeQuery = true)
        public void cambiarCantidadDisponible(Float cantidadDisponibleNueva, Number id);

        @Modifying
        @Query(value = "update articulo a SET cantidad_disponible = ?1 where id = ?2", nativeQuery = true)
        public void cambiarCantidadDisponibleFac(Float cantidadDisponibleNueva, Integer id);

        @Modifying
        @Query(value = "update articulo a SET cantidad_disponible = ?1 where id = ?2", nativeQuery = true)
        public void cambiarCantidadDisponibleRemision(Float cantidadDisponibleNueva, Integer id);

        @Modifying
        @Query(value = "update articulo a SET cantidad_disponible = ?1 where id_producto = ?2", nativeQuery = true)
        public void cambiarCantidadDisponibleRecodificar(Float cantidadDisponibleNueva, Integer id);

        @Modifying
        @Query(value = "update articulo a SET nid_estado_articulo = ?1 where id = ?2", nativeQuery = true)
        public void cambiarEstadoArticuloFinalizado(Integer estadoArticulo, Integer id);

        @Query(value = "SELECT * FROM articulo WHERE id_sede= ?1 and nid_estado_articulo in(1,2) and cantidad_disponible > 0", nativeQuery = true)
        public List<Articulo> todosSede(Integer idSede);

        @Query(value = "SELECT * FROM articulo WHERE id_sede= ?1 and nid_estado_articulo = 3", nativeQuery = true)
        public List<Articulo> articulosDevueltos(Integer idSede);

        // @Query(value = "SELECT * FROM articulo_movimientos where id_articulo = ?1
        // LIMIT 1", nativeQuery = true)
        // public ArticuloMovimientos obtenerArticuloInformeInventario(Integer
        // idArticulo);
}
