package com.softlond.base.repository;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.softlond.base.entity.ArticulosRemisionVenta;

@Transactional
public interface ArticulosRemisionVentaDao extends CrudRepository  <ArticulosRemisionVenta, Integer>{

	/*public ArticulosRemisionVenta findByIdArticulo(Integer idArticulo);
	
	@Query(value="SELECT * FROM articulos_remision_venta WHERE id_articulo = :idArticulo",nativeQuery = true)
	public List<ArticulosRemisionVenta> buscarArticulo(Integer idArticulo);
	
	public ArticulosRemisionVenta findByIdRemisionCompra(Integer idRemision);
	
	@Query(value = "SELECT * FROM articulos_remision_venta WHERE id_remision_venta = :idRemision",nativeQuery = true)
	public List<ArticulosRemisionVenta> findByRemision(Integer idRemision);
	
	@Query(value="SELECT * FROM articulos_remision_venta AS am INNER JOIN articulo As a ON am.id_articulo = a.id AND am.id_remision_venta = :idRemisionCompra order by a.codigo", nativeQuery = true)
	public ArrayList <ArticulosRemisionVenta> findByIdRemisionCommpra(Integer idRemisionCompra);
	
	@Query(value="SELECT * FROM articulos_remision_venta AS am INNER JOIN articulo As a ON am.id_articulo = a.id INNER JOIN remision_venta rem ON am.id_remision_venta = rem.id AND am.id_remision_venta = :idRemisionCompra", nativeQuery = true)
	public List <ArticulosRemisionVenta> findByIdRemisionCompraPageable(Integer idRemisionCompra);
	
	@Query(value="select * from articulos_remision_venta ar join fc_factura_ventas_remisiones_venta fr on  ar.id_remision_venta = fr.nid_fcremision join articulo a  on a.id = ar.id_articulo"
			+ " where fr.nid_factura = :idFacturaCompra", nativeQuery = true)
	public List <ArticulosRemisionVenta> findByIdRemisionCompraUpdate(Integer idFacturaCompra);
	
	@Query(value="select * from articulos_remision_venta ar join fc_factura_ventas_remisiones_venta fr on ar.id_remision_venta = fr.nid_fcremision join\r\n"
			+ "remision_venta r on r.id = fr.nid_fcremision join articulo a on a.id = ar.id_articulo\r\n"
			+ "where a.id_producto = ?\r\n"
			+ "group by r.id_cliente", nativeQuery = true)
	public Page<ArticulosRemisionVenta> listarArticulosParaListadoProductoPorProveedor(Integer idProducto, Pageable page);
	
	@Modifying
	@Query(value="delete from articulos_remision_venta where id_remision_venta = ?", nativeQuery = true)
	public void eliminarArticulosRemision(Integer idRemision);
	*/
}
