package com.softlond.base.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.Articulo;
import com.softlond.base.entity.Factura;
import com.softlond.base.entity.FacturaCompra;
import com.softlond.base.entity.InformacionUsuario;
import com.softlond.base.entity.NotaCredito;
import com.softlond.base.entity.NotaDebito;
import com.softlond.base.entity.Precio;
import com.softlond.base.entity.Producto;
import com.softlond.base.entity.Usuario;
import com.softlond.base.repository.ArticuloDao;
import com.softlond.base.repository.AsignacionReciboDao;
import com.softlond.base.repository.ConceptosReciboCajaDao;
import com.softlond.base.repository.PrecioDao;
import com.softlond.base.repository.ProductoDao;
import com.softlond.base.repository.UsuarioInformacionDao;
import com.softlond.base.response.InfromeFacturasVencidas;
import com.softlond.base.response.InfromeFacturasVencidasCliente;
import com.softlond.base.response.MovimientoProveedor;
import com.softlond.base.response.Paginacion;
import com.softlond.base.response.ProductoPrecio;
import com.softlond.base.response.ProductoResponse;
import com.softlond.base.response.ProductosExistentes;
import com.softlond.base.response.ProductosNoExistentes;

@Service
public class ProductosNoExistentesService {

	private static final Logger logger = Logger.getLogger(ProductosNoExistentesService.class);

	final int ITEMS_POR_PAGINA = 10;

	@Autowired
	UsuarioInformacionDao usuarioInformacionDao;

	@Autowired
	private ArticuloService articuloService;

	@Autowired
	private ProductoDao productoDao;
	
	@Autowired 
	private ArticuloDao articuloDao;
	
	@Autowired
	private PrecioDao precioDao;
	
	
	@PersistenceContext
    private EntityManager entityManager;
	
	
	
	//Busqueda avanzada productos no existentes 
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerProductosNoExistentesFiltros(String idProducto, Integer idClasificacion, Integer page) {
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao.buscarPorIdUsuario(usuarioAutenticado.getId());
		Integer idSede = usuarioInformacion.getIdOrganizacion().getId();
		
		
		Pageable paging = PageRequest.of(page, 10);
		try {
			List<Producto> productos;
			List<ProductosNoExistentes> Pnoexistentes = new ArrayList<ProductosNoExistentes>();
			StringBuilder query = new StringBuilder();
			StringBuilder queryCantidad = new StringBuilder();
			generarQueryProductosNoExistentesFiltros(idProducto, idClasificacion, query, idSede);		
			TypedQuery<Producto> productosInfoQuery = (TypedQuery<Producto>) entityManager.createNativeQuery(query.toString(), Producto.class);
			int pageNumber = paging.getPageNumber();
			int pageSize = paging.getPageSize();
			productosInfoQuery.setFirstResult(pageNumber * pageSize);
			productosInfoQuery.setMaxResults(pageSize);
			productos = productosInfoQuery.getResultList();
			generarQueryCantidadProductosFiltros(idProducto, idClasificacion, queryCantidad, idSede);
		
			Query cantidad = entityManager.createNativeQuery(queryCantidad.toString());
			BigInteger countResult = (BigInteger) cantidad.getSingleResult();
			Integer cantidadTotal = countResult.intValue();
			Page<Producto> result = new PageImpl<Producto>(productos, paging, cantidadTotal);
			for (Producto producto : result) {
				ProductosNoExistentes noexistentes = ProductosNoExistentes.convertirProductos(producto);
				String idUltArticulo = articuloDao.obtenerUltId(producto.getId());
				Date ultFecha = articuloDao.obtenerUltFechaCompra(producto.getId());
				Double ultCosto = precioDao.obtenerUltimoCosto(producto.getId());
				Double ultPrecioVenta = precioDao.obtenerUltimoPrecioVenta(producto.getId());
				
				noexistentes.setIdUltArticulo(idUltArticulo);
				noexistentes.setUltFecha(ultFecha);
				noexistentes.setUltCosto(ultCosto);
				noexistentes.setUltPrecioVenta(ultPrecioVenta);
				
				Pnoexistentes.add(noexistentes);
				
			}
			Page<ProductosNoExistentes> resultProductos = new PageImpl<ProductosNoExistentes>(Pnoexistentes, paging,
					cantidadTotal);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de productos exitosa");
			respuestaDto.setObjetoRespuesta(resultProductos);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo productos " + e + "linea " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error obteniendo productos");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}
			
	public void generarQueryProductosNoExistentesFiltros(String idProducto,Integer idClasificacion, StringBuilder query, Integer idsede){
	
		//query.append("select distinct p.* from articulo as a inner join producto as p inner join (select CONCAT(pt.t_tipo, ' ', pr.t_referencia,' ', pp.t_presentacion, ' ', pc.t_color) as productox, pin.* from producto as pin inner join prd_colores as pc inner join prd_tipos as pt inner join prd_referencia as pr inner join prd_presentacion as pp on pin.id_color = pc.id and pin.tipo = pt.id and pin.referencia = pr.id and pin.id_presentacion = pp.id) as n on p.id = a.id_producto and p.id = n.id where a.cantidad_disponible <= 0 and a.id_sede ="+idsede);
		query.append("select distinct p.* from producto p where p.id not in (select distinct p2.id from producto p2 inner join articulo a on a.id_producto = p2.id) or p.id in (select distinct p3.id from producto p3 inner join articulo a2 on a2.id_producto = p3.id where a2.cantidad_disponible <= 0)");

		/*if(idProducto != null) {
			//query.append(" and p.codigo = " + idProducto);
			query.append(" and (p.codigo like '%" + idProducto + "%' or n.productox like '%" + idProducto + "%')");
		}*/
		if(idProducto != null) {
			//query.append(" and p.codigo = " + idProducto);
			query.append(" and p.codigo like '%" + idProducto + "%'");
		}
		if(idClasificacion != 0) {
			query.append(" and p.id_clasificacion = " + idClasificacion);
		}
		query.append(" group by p.id");		
	}
	
	public void generarQueryCantidadProductosFiltros(String idProducto, Integer idClasificacion, StringBuilder query, Integer idsede){
		//query.append("select count(*) from producto paux inner join (select distinct p.*, n.productox from articulo as a inner join producto as p inner join (select CONCAT(pt.t_tipo, ' ', pr.t_referencia,' ', pp.t_presentacion, ' ', pc.t_color) as productox, pin.* from producto as pin inner join prd_colores as pc inner join prd_tipos as pt inner join prd_referencia as pr inner join prd_presentacion as pp on pin.id_color = pc.id and pin.tipo = pt.id and pin.referencia = pr.id and pin.id_presentacion = pp.id) as n on p.id = a.id_producto and p.id = n.id where a.cantidad_disponible <= 0 and a.id_sede ="+idsede+") as u on u.id = paux.id");
		query.append("select count(distinct p.id) from producto p where p.id not in (select distinct p2.id from producto p2 inner join articulo a on a.id_producto = p2.id) or p.id in (select distinct p3.id from producto p3 inner join articulo a2 on a2.id_producto = p3.id where a2.cantidad_disponible <= 0)");
		if(idProducto != null) {
			query.append(" and p.codigo like '%" + idProducto + "%'");
		}
		if(idClasificacion != 0) {
			query.append(" and p.id_clasificacion = " + idClasificacion);
		}
		
		//query.append(" group by p.id)");	
	}
	
}