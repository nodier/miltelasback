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
import com.softlond.base.entity.PrdDescuentos;
import com.softlond.base.entity.Producto;
import com.softlond.base.entity.Usuario;
import com.softlond.base.repository.ArticuloDao;
import com.softlond.base.repository.AsignacionReciboDao;
import com.softlond.base.repository.ConceptosReciboCajaDao;
import com.softlond.base.repository.ProductoDao;
import com.softlond.base.repository.UsuarioInformacionDao;
import com.softlond.base.response.InfromeFacturasVencidas;
import com.softlond.base.response.InfromeFacturasVencidasCliente;
import com.softlond.base.response.Paginacion;
import com.softlond.base.response.ProductosExistentes;
import com.softlond.base.response.ProductosNoExistentes;


@Service
public class ProductosExistentesService {

	private static final Logger logger = Logger.getLogger(ProductosExistentesService.class);

	final int ITEMS_POR_PAGINA = 10;

	@Autowired
	UsuarioInformacionDao usuarioInformacionDao;

	@Autowired
	private ArticuloService articuloService;
	
	@Autowired
	private ProductoDao productoDao;

	@Autowired
	private ProductoService productoService;
	
	@Autowired 
	private ArticuloDao articuloDao;
	
	
	@PersistenceContext
    private EntityManager entityManager;

	
	//Busqueda avanzada productos existentes 
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerProductosExistentesFiltros(String texto, Integer idClasificacion,
			Integer page) {
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		Integer idSede = usuarioInformacion.getIdOrganizacion().getId();
		
		Pageable paging = PageRequest.of(page, 10);
		try {
			List<Producto> productos;
			List<ProductosExistentes> Pexistentes = new ArrayList<ProductosExistentes>();
			StringBuilder query = new StringBuilder();
			StringBuilder queryCantidad = new StringBuilder();
			generarQueryProductosExistentesFiltros(texto, idClasificacion, query, idSede);
			
			TypedQuery<Producto> productosInfoQuery = (TypedQuery<Producto>) entityManager.createNativeQuery(query.toString(), Producto.class);
			int pageNumber = paging.getPageNumber();
			int pageSize = paging.getPageSize();
			productosInfoQuery.setFirstResult(pageNumber * pageSize);
			productosInfoQuery.setMaxResults(pageSize);
			productos = productosInfoQuery.getResultList();
			generarQueryCantidadProductosFiltros(texto, idClasificacion, queryCantidad,idSede);
			
			Query cantidad = entityManager.createNativeQuery(queryCantidad.toString());
			BigInteger countResult = (BigInteger) cantidad.getSingleResult();
			Integer cantidadTotal = countResult.intValue();
			Page<Producto> result = new PageImpl<Producto>(productos, paging, cantidadTotal);
			for (Producto producto : result) {
				ProductosExistentes existentes = ProductosExistentes.convertirProductos(producto);
				//Integer articulos = articuloDao.cantidadProducto(producto.getId());
				Double disponible = articuloDao.sumaDisponibleProducto(producto.getId(), idSede);
				Double cantidadArticulos = productoDao.obtenerCantidadArticulos(producto.getId(), idSede);
				double ArticuloCant = cantidadArticulos;
		        int ArticulosCantidadProducto = (int) ArticuloCant;
				existentes.setArticulos(ArticulosCantidadProducto);
				
				if (disponible == null) {
					disponible = 0.0;
				} else {
					disponible = (double) (Math.round(disponible * 1000000d) / 1000000d);
				}
				existentes.setDisponible(disponible);
				Pexistentes.add(existentes);
			}
			Page<ProductosExistentes> resultProductos = new PageImpl<ProductosExistentes>(Pexistentes, paging,
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

		public void generarQueryProductosExistentesFiltros(String texto, Integer idClasificacion, StringBuilder query, Integer idsede){
			
			//query.append("select distinct p.* from articulo a inner join producto p ON a.id_producto = p.id where a.cantidad_disponible > 0 and a.id_sede=15");
			//query.append("select m.* from producto m inner join (select CONCAT(pt.t_tipo, ' ', pr.t_referencia,' ', pp.t_presentacion, ' ', pc.t_color) productox, p.* from producto p inner join prd_colores pc inner join prd_tipos pt inner join prd_referencia pr inner join prd_presentacion pp on p.id_color = pc.id and p.tipo = pt.id and p.referencia = pr.id and p.id_presentacion = pp.id)n inner join articulo a on m.id = a.id_producto and m.id = n.id where a.cantidad_disponible > 0 and a.id_sede=15");
			query.append("select distinct p.* from articulo as a inner join producto as p inner join (select CONCAT(pt.t_tipo, ' ', pr.t_referencia,' ', pp.t_presentacion, ' ', pc.t_color) as productox, pin.* from producto as pin inner join prd_colores as pc inner join prd_tipos as pt inner join prd_referencia as pr inner join prd_presentacion as pp on pin.id_color = pc.id and pin.tipo = pt.id and pin.referencia = pr.id and pin.id_presentacion = pp.id) as n on p.id = a.id_producto and p.id = n.id where a.cantidad_disponible > 0 and a.id_sede ="+idsede);
			if(!texto.equals("null")) {
				query.append(" and (p.codigo like '%" + texto + "%' or n.productox like '%" + texto +"%') ");
			}
			if(idClasificacion != 0) {
				query.append(" and p.id_clasificacion = " + idClasificacion);
			}
			query.append(" group by p.id");
		}
		
		public void generarQueryCantidadProductosFiltros(String texto, Integer idClasificacion, StringBuilder query, Integer idsede){
			//query.append("select count(*) from (select p.* from producto p join articulo a on a.id_producto=p.id where a.cantidad_disponible > 0 and a.id_sede=15");
			query.append("select count(*) from producto paux inner join (select distinct p.*, n.productox from articulo as a inner join producto as p inner join (select CONCAT(pt.t_tipo, ' ', pr.t_referencia,' ', pp.t_presentacion, ' ', pc.t_color) as productox, pin.* from producto as pin inner join prd_colores as pc inner join prd_tipos as pt inner join prd_referencia as pr inner join prd_presentacion as pp on pin.id_color = pc.id and pin.tipo = pt.id and pin.referencia = pr.id and pin.id_presentacion = pp.id) as n on p.id = a.id_producto and p.id = n.id where a.cantidad_disponible > 0 and a.id_sede ="+idsede+") as u on u.id = paux.id");
			
			if(!texto.equals("null")) {
				query.append(" and (paux.codigo like '%" + texto + "%' or u.productox like '%" + texto +"%') ");
			}
			if(idClasificacion != 0) {
				query.append(" and paux.id_clasificacion = " + idClasificacion);
			}
			
			//query.append(" group by p.id)t");
		}
		
	public List<ProductosExistentes> listarProductosExistentes(int idProducto) {
		List<ProductosExistentes> vecom = new ArrayList<>();
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		Integer idSede = usuarioInformacion.getIdOrganizacion().getId();

		// Obtener productos
		List<Producto> productos = productoService.listarProductosExistentes(idProducto);
		for (Producto p : productos) {
			ProductosExistentes existentes = ProductosExistentes.convertirProductos(p);
			Integer articulos = articuloDao.cantidadProducto(p.getId());
			Double disponible = articuloDao.sumaDisponibleProducto(p.getId(), idSede);
			existentes.setArticulos(articulos);
			existentes.setDisponible(disponible);
			vecom.add(existentes);
		}
		return vecom;
	}

	public Paginacion listarProductosExistentesPaginado(int idProducto, int pagina) {
		List<ProductosExistentes> vecom = listarProductosExistentes(idProducto);
		return Paginacion.paginar(vecom, ITEMS_POR_PAGINA, pagina);
	}
	
}

