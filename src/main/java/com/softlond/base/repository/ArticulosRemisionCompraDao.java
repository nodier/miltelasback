package com.softlond.base.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.softlond.base.entity.ArticulosRemisionCompra;
import com.softlond.base.entity.FacturaCompra;

@Transactional
public interface ArticulosRemisionCompraDao extends CrudRepository<ArticulosRemisionCompra, Integer> {

	public ArticulosRemisionCompra findByIdArticulo(Integer idArticulo);

	@Query(value = "SELECT * FROM articulos_remision_compra WHERE id_articulo = :idArticulo", nativeQuery = true)
	public List<ArticulosRemisionCompra> buscarArticulo(Integer idArticulo);

	public ArticulosRemisionCompra findByIdRemisionCompra(Integer idRemision);

	@Query(value = "SELECT * FROM articulos_remision_compra WHERE id_remision_compra = :idRemision", nativeQuery = true)
	public List<ArticulosRemisionCompra> findByRemision(Integer idRemision);

	@Query(value = "SELECT * FROM articulos_remision_compra AS am INNER JOIN articulo As a ON am.id_articulo = a.id AND am.id_remision_compra = :idRemisionCompra order by a.codigo", nativeQuery = true)
	public ArrayList<ArticulosRemisionCompra> findByIdRemisionCommpra(Integer idRemisionCompra);

	@Query(value = "SELECT * FROM articulos_remision_compra AS am INNER JOIN articulo As a ON am.id_articulo = a.id INNER JOIN remision_compra rem ON am.id_remision_compra = rem.id AND am.id_remision_compra = :idRemisionCompra", nativeQuery = true)
	public List<ArticulosRemisionCompra> findByIdRemisionCompraPageable(Integer idRemisionCompra);

	@Query(value = "select * from articulos_remision_compra ar join fc_factura_remisiones fr on  ar.id_remision_compra = fr.nid_fcremision join articulo a  on a.id = ar.id_articulo"
			+ " where fr.nid_factura_compra = :idFacturaCompra", nativeQuery = true)
	public List<ArticulosRemisionCompra> findByIdRemisionCompraUpdate(Integer idFacturaCompra);

	@Query(value = "select * from articulos_remision_compra ar join fc_factura_remisiones fr on ar.id_remision_compra = fr.nid_fcremision join remision_compra r on r.id = fr.nid_fcremision join articulo a on a.id = ar.id_articulo join producto p on a.id_producto = p.id join (select distinct CONCAT(pt.t_tipo, ' ', pr.t_referencia,' ', pp.t_presentacion, ' ', pc.t_color) productox, pcon.id as idp from producto pcon inner join prd_colores pc inner join prd_tipos pt inner join prd_referencia pr inner join prd_presentacion pp on pcon.id_color = pc.id and pcon.tipo = pt.id and pcon.referencia = pr.id and pcon.id_presentacion = pp.id) as pl on pl.idp = p.id where p.codigo like %?1% or pl.productox like %?1% group by pl.productox order by r.id_proveedor asc", nativeQuery = true)
	public Page<ArticulosRemisionCompra> listarArticulosParaListadoProductoPorProveedor(String idProducto,
			Pageable page);

	@Modifying
	@Query(value = "delete from articulos_remision_compra where id_remision_compra = ?", nativeQuery = true)
	public void eliminarArticulosRemision(Integer idRemision);

	@Query(value = "select * from articulos_remision_compra arc \r\n"
			+ "join remision_compra rc on rc.id = arc.id_remision_compra join articulo a on a.id = arc.id_articulo where rc.id_sede = :idSede and arc.id_articulo = :idArticulo", nativeQuery = true)
	public List<ArticulosRemisionCompra> buscarPorIdArticulo(Integer idSede, Integer idArticulo);

	@Query(value = "SELECT count(*) FROM articulos_remision_compra WHERE id_remision_compra = :idRemisionCompra and id_articulo = :idArticulo", nativeQuery = true)
	public Integer buscarPorIdArticuloRemision(Integer idRemisionCompra, Integer idArticulo);

}
