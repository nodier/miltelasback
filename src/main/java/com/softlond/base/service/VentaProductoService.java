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
import org.springframework.web.bind.annotation.RequestBody;

import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.Clientes;
import com.softlond.base.entity.Factura;
import com.softlond.base.entity.InformacionUsuario;
import com.softlond.base.entity.InveLocal;
import com.softlond.base.entity.Producto;
import com.softlond.base.entity.Usuario;
import com.softlond.base.repository.FacturaArticuloDao;
import com.softlond.base.repository.FacturaDao;
import com.softlond.base.repository.LocalDao;
import com.softlond.base.repository.SectorDao;
import com.softlond.base.repository.UsuarioInformacionDao;
import com.softlond.base.response.Locales;
import com.softlond.base.response.Paginacion;
import com.softlond.base.response.VentaProducto;

@Service
public class VentaProductoService {

	private static final Logger logger = Logger.getLogger(VentaProductoService.class);

	final int ITEMS_POR_PAGINA = 10;

	@Autowired
	UsuarioInformacionDao usuarioInformacionDao;

	@Autowired
	private FacturaArticuloDao facturaArticuloDao;
	
	@Autowired
	private FacturaDao facturaDao;
	
	@PersistenceContext
    private EntityManager entityManager;
			
			
		//Busqueda venta productos 
		@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
		public ResponseEntity<Object> obtenerVentaProducto(String fechaInicial, String fechaFinal, String order, boolean sort, Integer page) {
			 ResponseEntity<Object> respuesta; 
			  Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication(); 
			  Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal(); 
			  Pageable paging = PageRequest.of(page, 10); 
			  InformacionUsuario usuarioInformacion = this.usuarioInformacionDao 
			    .buscarPorIdUsuario(usuarioAutenticado.getId()); 
			try {
				List<Producto> productos;
				List<VentaProducto> Vproducto = new ArrayList<VentaProducto>();
				StringBuilder query = new StringBuilder();
				StringBuilder queryCantidad = new StringBuilder();
				generarQueryVentaProductoFiltros(query, usuarioInformacion.getIdOrganizacion().getId(), fechaInicial, fechaFinal, order, sort);
				
				TypedQuery<Producto> productosInfoQuery = (TypedQuery<Producto>) entityManager.createNativeQuery(query.toString(), Producto.class);
				int pageNumber = paging.getPageNumber();
				int pageSize = paging.getPageSize();
				productosInfoQuery.setFirstResult(pageNumber * pageSize);
				productosInfoQuery.setMaxResults(pageSize);
				productos = productosInfoQuery.getResultList();
				generarQueryCantidadVentaProductoFiltros(queryCantidad, usuarioInformacion.getIdOrganizacion().getId(), fechaInicial, fechaFinal);				
				Query cantidad = entityManager.createNativeQuery(queryCantidad.toString());
				BigInteger countResult = (BigInteger) cantidad.getSingleResult();
				Integer cantidadTotal = countResult.intValue();
				Page<Producto> result = new PageImpl<Producto>(productos, paging, cantidadTotal);
				for (Producto producto : result) {
					VentaProducto veProductos = VentaProducto.convertirVentaProducto(producto);
					Integer numFact = facturaDao.obtenerNumFacturas(usuarioInformacion.getIdOrganizacion().getId(), producto.getId(), fechaInicial, fechaFinal);
					Double cantidadd = facturaArticuloDao.sumCantidad(usuarioInformacion.getIdOrganizacion().getId(), producto.getId(), fechaInicial, fechaFinal);
					veProductos.setNumFact(numFact);
					veProductos.setCantidad(cantidadd);
					Vproducto.add(veProductos);
				}
				Page<VentaProducto> resultProductos = new PageImpl<VentaProducto>(Vproducto, paging, cantidadTotal);
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de ventas de productos exitoso");
				respuestaDto.setObjetoRespuesta(resultProductos);
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
			} catch (Exception e) {
				logger.error("Error obteniendo ventas de productos " + e + "linea " + e.getStackTrace()[0].getLineNumber());
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
						"Error obteniendo ventas de productos" + e + "linea " + e.getStackTrace()[0].getLineNumber());
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			return respuesta;
		}
				
		public void generarQueryVentaProductoFiltros(StringBuilder query, Integer idSede, String fechaInicial, String fechaFinal, String order, boolean sort){
			query.append("select distinct p.* from producto p join articulo a on p.id = a.id_producto join fac_articulos fa on a.id = fa.nid_articulo\r\n" + 
					"join fac_facturas ff on ff.nid_factura = fa.nid_factura where ff.id_sede= ");
			
			query.append(""+idSede);
			
			if (!fechaInicial.equals("null") && fechaFinal.equals("null")) {
				query.append(" and date_format(date(ff.d_fecha_venta),'%Y-%m-%d')" + "= date('" + fechaInicial + "')");
			} else if (fechaInicial.equals("null") && !fechaFinal.equals("null")) {
				query.append(" and date_format(date(ff.d_fecha_venta),'%Y-%m-%d')" + "= date('" + fechaFinal + "')");
			} else if (!fechaInicial.equals("null") && !fechaFinal.equals("null")) {
				query.append(" and date_format(date(ff.d_fecha_venta),'%Y-%m-%d')" + "between " + "date_format(date('" + fechaInicial
						+ "'),'%Y-%m-%d')" + " and " + "date_format(date('" + fechaFinal + "'),'%Y-%m-%d')");
			}
			if (sort) {
				QueryOrdenar(query, order);
			}
		}
		
		public void generarQueryCantidadVentaProductoFiltros(StringBuilder query, Integer idSede, String fechaInicial, String fechaFinal){
			query.append("select count(*) from (select p.* from producto p join articulo a on p.id = a.id_producto join fac_articulos fa on a.id = fa.nid_articulo\r\n" + 
					"join fac_facturas ff on ff.nid_factura = fa.nid_factura where ff.id_sede= ");
			
			query.append(""+idSede);
			
			if (!fechaInicial.equals("null") && fechaFinal.equals("null")) {
				query.append(" and date_format(date(ff.d_fecha_venta),'%Y-%m-%d')" + "= date('" + fechaInicial + "')");
			} else if (fechaInicial.equals("null") && !fechaFinal.equals("null")) {
				query.append(" and date_format(date(ff.d_fecha_venta),'%Y-%m-%d')" + "= date('" + fechaFinal + "')");
			} else if (!fechaInicial.equals("null") && !fechaFinal.equals("null")) {
				query.append(" and date_format(date(ff.d_fecha_venta),'%Y-%m-%d')" + "between " + "date_format(date('" + fechaInicial
						+ "'),'%Y-%m-%d')" + " and " + "date_format(date('" + fechaFinal + "'),'%Y-%m-%d')");
			}
			query.append(" group by p.id)t");
		}
		
		
		private void QueryOrdenar(StringBuilder query, String order) {
			switch (order) {
			case "codigo":
				query.append(" order by p.codigo desc");
				break;
			case "producto":
				query.append(" order by p.producto desc");
				break;
			case "FechaVenta":
				query.append(" order by order by d_fecha_venta desc");
				break;
			case "numFact":
				query.append(" group by ff.nid_factura order by numFact desc");
				break;
			case "cantidad":
				query.append(" group by fa.nid_factura order by cantidad desc");
				break;
			default:
				break;
			}
		}
					
}