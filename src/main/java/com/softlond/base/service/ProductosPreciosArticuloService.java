package com.softlond.base.service;

import java.math.BigInteger;
import java.util.ArrayList;
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
import com.softlond.base.entity.InformacionUsuario;
import com.softlond.base.entity.Usuario;
import com.softlond.base.repository.ArticuloDao;
import com.softlond.base.repository.DevolucionComprasArticulosDao;
import com.softlond.base.repository.DevolucionVentasArticulosDao;
import com.softlond.base.repository.FacturaArticuloDao;
import com.softlond.base.repository.UsuarioInformacionDao;
import com.softlond.base.response.ProductosCantidadesArticulos;
import com.softlond.base.response.ProductosPreciosArticulos;

@Service
public class ProductosPreciosArticuloService {

	private static final Logger logger = Logger.getLogger(ProductosPreciosArticuloService.class);

	final int ITEMS_POR_PAGINA = 10;

	@Autowired
	UsuarioInformacionDao usuarioInformacionDao;

	@Autowired
	private ArticuloService articuloService;
	
	@Autowired 
	private ArticuloDao articuloDao;
	
	@Autowired
	public DevolucionComprasArticulosDao devolucionComprasArticulosDao;
	
	@Autowired
	public DevolucionVentasArticulosDao devolucionVentasArticulosDao;
	
	@Autowired
	private FacturaArticuloDao facturaArticuloDao;
		
	@PersistenceContext
    private EntityManager entityManager;
	
	
	//Busqueda avanzada productos precios articulos 
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerProductosPreciosArticuloFiltros(String fechaInicial, String fechaFinal, Integer tipo,
    		Integer referencia, Integer presentacion, Integer clasificacion, Integer color, 
    		Integer estado, String order, boolean sort, Integer page) {
		 ResponseEntity<Object> respuesta; 
		  Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication(); 
		  Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal(); 
		  Pageable paging = PageRequest.of(page, 10); 
		  InformacionUsuario usuarioInformacion = this.usuarioInformacionDao 
		    .buscarPorIdUsuario(usuarioAutenticado.getId()); 
		
		try {
			List<Articulo> articulos;
			List<ProductosPreciosArticulos> pCantidadArticulos = new ArrayList<ProductosPreciosArticulos>();
			StringBuilder query = new StringBuilder();
			StringBuilder queryCantidad = new StringBuilder();
			generarQueryProductosPreciosArticuloFiltros(query, usuarioInformacion.getIdOrganizacion().getId(), fechaInicial, fechaFinal, tipo, referencia, presentacion, clasificacion,  color, estado, order, sort);
		
			TypedQuery<Articulo> productosInfoQuery = (TypedQuery<Articulo>) entityManager.createNativeQuery(query.toString(), Articulo.class);
			int pageNumber = paging.getPageNumber();
			int pageSize = paging.getPageSize();
			productosInfoQuery.setFirstResult(pageNumber * pageSize);
			productosInfoQuery.setMaxResults(pageSize);
			articulos = productosInfoQuery.getResultList();
			generarQueryCantidadProductosPreciosArticuloFiltros(queryCantidad, usuarioInformacion.getIdOrganizacion().getId(), fechaInicial, fechaFinal, tipo, referencia, presentacion, clasificacion,  color, estado);
			
			Query cantidad = entityManager.createNativeQuery(queryCantidad.toString());
			BigInteger countResult = (BigInteger) cantidad.getSingleResult();
			Integer cantidadTotal = countResult.intValue();
			Page<Articulo> result = new PageImpl<Articulo>(articulos, paging, cantidadTotal);
			for (Articulo articulo : result) {
				ProductosPreciosArticulos prCantidadArticulo = ProductosPreciosArticulos.convertirArticulo(articulo);
				Double cantDevCompra = devolucionComprasArticulosDao.cantDevCompra(articulo.getId());
				Double cantDevVenta = devolucionVentasArticulosDao.cantDevVenta(articulo.getId());
				Double cantVendida = facturaArticuloDao.cantVendida(articulo.getId());
				Double precioVendida = facturaArticuloDao.sumCantVendida(articulo.getId());
				/*Float vVsC = devolucionVentasArticulosDao.obtenerUltimoPrecioVenta(articulo.getId());*/
				Double precioDeVCompra = devolucionComprasArticulosDao.SumCantDevCompra(articulo.getId());
				prCantidadArticulo.setCantDevCompra(cantDevCompra);
				prCantidadArticulo.setCantDevVenta(cantDevVenta);
				prCantidadArticulo.setCantVendida(cantVendida);
								/*prCantidadArticulo.setUltPrecioVenta(ultPrecioVenta);*/
				prCantidadArticulo.setPrecioVendida(precioVendida);
				prCantidadArticulo.setPrecioDeVCompra(precioDeVCompra);
				pCantidadArticulos.add(prCantidadArticulo);
				
			}
			Page<ProductosPreciosArticulos> resultProductos = new PageImpl<ProductosPreciosArticulos>(pCantidadArticulos, paging,
					cantidadTotal);	
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de productos por cantidad de artículos exitosa");
			respuestaDto.setObjetoRespuesta(resultProductos);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);		
		} catch (Exception e) {
			logger.error("Error obteniendo productos por cantidad de artículos" + e + "linea " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error obteniendo productos por cantidad de artículos" + e.getStackTrace()[0].getLineNumber());
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}
			
	public void generarQueryProductosPreciosArticuloFiltros(StringBuilder query, Integer idSede, String fechaInicial, String fechaFinal, Integer tipo,
    		Integer referencia, Integer presentacion, Integer clasificacion, Integer color, 
    		Integer estado, String order, boolean sort){
		query.append("select a.* from producto p join articulo a on a.id_producto=p.id where a.id_sede=");
		query.append(""+idSede);
		
		if (!fechaInicial.equals("null") && fechaFinal.equals("null")) {
			query.append(" and date_format(date(a.fecha_ingreso),'%Y-%m-%d')" + "= date('" + fechaInicial + "')");
		} else if (fechaInicial.equals("null") && !fechaFinal.equals("null")) {
			query.append(" and date_format(date(a.fecha_ingreso),'%Y-%m-%d')" + "= date('" + fechaFinal + "')");
		} else if (!fechaInicial.equals("null") && !fechaFinal.equals("null")) {
			query.append(" and date_format(date(a.fecha_ingreso),'%Y-%m-%d')" + "between " + "date_format(date('" + fechaInicial
					+ "'),'%Y-%m-%d')" + " and " + "date_format(date('" + fechaFinal + "'),'%Y-%m-%d')");
		}
		
		if (tipo != 0 && (!fechaInicial.equals("null") && !fechaFinal.equals("null"))) {
			query.append(" and p.tipo=" + tipo);
		}
		
		else if (tipo != 0) {
			query.append(" and p.tipo=" + tipo);
		}
		
		if (referencia != 0 && (!fechaInicial.equals("null") && !fechaFinal.equals("null") || tipo != 0)) {
			query.append(" and p.referencia=" + referencia);
		}
		
		else if (referencia != 0) {
			query.append(" and p.referencia=" + referencia);
		}
		
		if (presentacion != 0 && (!fechaInicial.equals("null") && !fechaFinal.equals("null") || tipo != 0 || referencia != 0)) {
			query.append(" and p.id_presentacion=" + presentacion);
		}
		
		else if (presentacion != 0) {
			query.append(" and p.id_presentacion=" + presentacion);
		}
		
		if (clasificacion != 0 && (!fechaInicial.equals("null") && !fechaFinal.equals("null") || tipo != 0 || referencia != 0 || presentacion != 0)) {
			query.append(" and p.id_clasificacion=" + clasificacion);
		}
		
		else if (clasificacion != 0) {
			query.append(" and p.id_clasificacion=" + clasificacion);
		}
		
		if (color != 0 && (!fechaInicial.equals("null") && !fechaFinal.equals("null") || tipo != 0 || referencia != 0 || presentacion != 0
				|| clasificacion != 0	)) {
			query.append(" and p.id_color=" + color);
		}
		
		else if (color != 0) {
			query.append(" and p.id_color=" + color);
		}
		
		if (estado != 0 && (!fechaInicial.equals("null") && !fechaFinal.equals("null") || tipo != 0 || referencia != 0 || presentacion != 0
				|| clasificacion != 0 || color != 0)) {
			query.append(" and a.nid_estado_articulo=" + estado);
		}
		
		else if (estado != 0) {
			query.append(" and a.nid_estado_articulo=" + estado);
		}
		
		if (sort) {
			query.append(" order by " + order + " desc");
		}
		//query.append(" group by a.id");
	}
	
	public void generarQueryCantidadProductosPreciosArticuloFiltros(StringBuilder query, Integer idSede, String fechaInicial, String fechaFinal, Integer tipo,
    		Integer referencia, Integer presentacion, Integer clasificacion, Integer color, 
    		Integer estado){
		query.append("select count(*) from (select p.* from producto p join articulo a on a.id_producto=p.id where a.id_sede= ");
		query.append(""+idSede);
		
		if (!fechaInicial.equals("null") && fechaFinal.equals("null")) {
			query.append(" and date_format(date(a.fecha_ingreso),'%Y-%m-%d')" + "= date('" + fechaInicial + "')");
		} else if (fechaInicial.equals("null") && !fechaFinal.equals("null")) {
			query.append(" and date_format(date(a.fecha_ingreso),'%Y-%m-%d')" + "= date('" + fechaFinal + "')");
		} else if (!fechaInicial.equals("null") && !fechaFinal.equals("null")) {
			query.append(" and date_format(date(a.fecha_ingreso),'%Y-%m-%d')" + "between " + "date_format(date('" + fechaInicial
					+ "'),'%Y-%m-%d')" + " and " + "date_format(date('" + fechaFinal + "'),'%Y-%m-%d')");
		}
		
		if (tipo != 0 && (!fechaInicial.equals("null") && !fechaFinal.equals("null"))) {
			query.append(" and p.tipo=" + tipo);
		}
		
		else if (tipo != 0) {
			query.append(" and p.tipo=" + tipo);
		}
		
		if (referencia != 0 && (!fechaInicial.equals("null") && !fechaFinal.equals("null") || tipo != 0)) {
			query.append(" and p.referencia=" + referencia);
		}
		
		else if (referencia != 0) {
			query.append(" and p.referencia=" + referencia);
		}
		
		if (presentacion != 0 && (!fechaInicial.equals("null") && !fechaFinal.equals("null") || tipo != 0 || referencia != 0)) {
			query.append(" and p.id_presentacion=" + presentacion);
		}
		
		else if (presentacion != 0) {
			query.append(" and p.id_presentacion=" + presentacion);
		}
		
		if (clasificacion != 0 && (!fechaInicial.equals("null") && !fechaFinal.equals("null") || tipo != 0 || referencia != 0 || presentacion != 0)) {
			query.append(" and p.id_clasificacion=" + clasificacion);
		}
		
		else if (clasificacion != 0) {
			query.append(" and p.id_clasificacion=" + clasificacion);
		}
		
		if (color != 0 && (!fechaInicial.equals("null") && !fechaFinal.equals("null") || tipo != 0 || referencia != 0 || presentacion != 0
				|| clasificacion != 0	)) {
			query.append(" and p.id_color=" + color);
		}
		
		else if (color != 0) {
			query.append(" and p.id_color=" + color);
		}
		
		if (estado != 0 && (!fechaInicial.equals("null") && !fechaFinal.equals("null") || tipo != 0 || referencia != 0 || presentacion != 0
				|| clasificacion != 0 || color != 0)) {
			query.append(" and a.nid_estado_articulo=" + estado);
		}
		
		else if (estado != 0) {
			query.append(" and a.nid_estado_articulo=" + estado);
		}
		
		query.append(" group by a.id)t");
	}
	
	
	//Busqueda avanzada productos precios articulos 
		@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
		public ResponseEntity<Object> obtenerProductosPreciosArticuloFiltrosPaginado(Integer page) {
			 ResponseEntity<Object> respuesta; 
			  Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication(); 
			  Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal(); 
			  Pageable paging = PageRequest.of(page, 10); 
			  InformacionUsuario usuarioInformacion = this.usuarioInformacionDao 
			    .buscarPorIdUsuario(usuarioAutenticado.getId()); 
			
			try {
				List<Articulo> articulos;
				List<ProductosPreciosArticulos> pCantidadArticulos = new ArrayList<ProductosPreciosArticulos>();
				StringBuilder query = new StringBuilder();
				StringBuilder queryCantidad = new StringBuilder();
				generarQueryProductosPreciosArticuloFiltrosPaginado(query, usuarioInformacion.getIdOrganizacion().getId());
		
				TypedQuery<Articulo> productosInfoQuery = (TypedQuery<Articulo>) entityManager.createNativeQuery(query.toString(), Articulo.class);
				int pageNumber = paging.getPageNumber();
				int pageSize = paging.getPageSize();
				productosInfoQuery.setFirstResult(pageNumber * pageSize);
				productosInfoQuery.setMaxResults(pageSize);
				articulos = productosInfoQuery.getResultList();
				generarQueryCantidadProductosPreciosArticuloFiltrosPaginado(queryCantidad, usuarioInformacion.getIdOrganizacion().getId());
			
				Query cantidad = entityManager.createNativeQuery(queryCantidad.toString());
				BigInteger countResult = (BigInteger) cantidad.getSingleResult();
				Integer cantidadTotal = countResult.intValue();
				Page<Articulo> result = new PageImpl<Articulo>(articulos, paging, cantidadTotal);
				for (Articulo articulo : result) {
					ProductosPreciosArticulos prCantidadArticulo = ProductosPreciosArticulos.convertirArticulo(articulo);
					Double cantDevCompra = devolucionComprasArticulosDao.cantDevCompra(articulo.getId());
					Double cantDevVenta = devolucionVentasArticulosDao.cantDevVenta(articulo.getId());
					Double cantVendida = facturaArticuloDao.cantVendida(articulo.getId());
					Double precioVendida = facturaArticuloDao.sumCantVendida(articulo.getId());
					/*Float vVsC = devolucionVentasArticulosDao.obtenerUltimoPrecioVenta(articulo.getId());*/
					Double precioDeVCompra = devolucionComprasArticulosDao.SumCantDevCompra(articulo.getId());
					
					prCantidadArticulo.setCantDevCompra(cantDevCompra);
					prCantidadArticulo.setCantDevVenta(cantDevVenta);
					prCantidadArticulo.setCantVendida(cantVendida);
					/*prCantidadArticulo.setUltPrecioVenta(ultPrecioVenta);*/
					prCantidadArticulo.setPrecioVendida(precioVendida);
					prCantidadArticulo.setPrecioDeVCompra(precioDeVCompra);
					pCantidadArticulos.add(prCantidadArticulo);
				}
				Page<ProductosPreciosArticulos> resultProductos = new PageImpl<ProductosPreciosArticulos>(pCantidadArticulos, paging,
						cantidadTotal);	
				respuesta = ResponseEntity.ok(HttpStatus.OK);
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de productos por cantidad de artículos exitosa");
				respuestaDto.setObjetoRespuesta(resultProductos);
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);		
			} catch (Exception e) {
				logger.error("Error obteniendo productos por cantidad de artículos" + e + "linea " + e.getStackTrace()[0].getLineNumber());
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
						"Error obteniendo productos por cantidad de artículos" + e.getStackTrace()[0].getLineNumber());
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			return respuesta;
		}
				
		public void generarQueryProductosPreciosArticuloFiltrosPaginado(StringBuilder query, Integer idSede){
			query.append("select a.* from producto p join articulo a on a.id_producto=p.id where a.id_sede=");
			query.append(""+idSede);
			
			query.append(" group by a.id");
		}
		
		public void generarQueryCantidadProductosPreciosArticuloFiltrosPaginado(StringBuilder query, Integer idSede){
			query.append("select count(*) from (select p.* from producto p join articulo a on a.id_producto=p.id where a.id_sede= ");
			query.append(""+idSede);
			
			query.append(" group by a.id)t");
		}
	
}