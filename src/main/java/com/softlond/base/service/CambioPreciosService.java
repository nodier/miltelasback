package com.softlond.base.service;

import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.Articulo;
import com.softlond.base.entity.Clientes;
import com.softlond.base.entity.FacturaArticulos;
import com.softlond.base.entity.FacturaRemision;
import com.softlond.base.entity.InformacionUsuario;
import com.softlond.base.entity.DevolucionVentasCliente;
import com.softlond.base.entity.Factura;
import com.softlond.base.entity.NotaDebito;
import com.softlond.base.entity.Organizacion;
import com.softlond.base.entity.PrdDescuentos;
import com.softlond.base.entity.Precio;
import com.softlond.base.entity.Producto;
import com.softlond.base.entity.RemisionVenta;
import com.softlond.base.entity.Usuario;
import com.softlond.base.repository.ArticuloDao;
import com.softlond.base.repository.PrecioDao;
import com.softlond.base.repository.ProductoDao;
import com.softlond.base.repository.UsuarioInformacionDao;
import com.softlond.base.repository.FacturaArticuloDao;
import com.softlond.base.repository.OrganizacionDao;
import com.softlond.base.repository.PrecioDao;
import com.softlond.base.repository.ProductoDao;
import com.softlond.base.request.DevolucionRequest;
import com.softlond.base.response.ProductoCliente;
import com.softlond.base.response.ProductoPrecio;
import com.softlond.base.response.ProductoResponse;

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

import java.math.BigInteger;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

@Service
public class CambioPreciosService {

	private static final Logger logger = Logger.getLogger(CambioPreciosService.class);

	@Autowired
	private ProductoDao productoDao;

	@Autowired
	OrganizacionDao organizacionDao;

	@Autowired
	UsuarioInformacionDao usuarioInformacionDao;

	@Autowired
	private PrecioDao precio;

	@PersistenceContext
	private EntityManager entityManager;

	// Consulta avanzada de productos
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerProductosConsulta(Integer tipo, Integer referencia, Integer presentacion,
			Integer color, Integer page) {
		ResponseEntity<Object> respuesta;
		Pageable paging = PageRequest.of(page, 10);
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao.buscarPorIdUsuario(usuarioAutenticado.getId());

		Integer idSede = usuarioInformacion.getIdOrganizacion().getId();
		try {
			List<Producto> productos;
			StringBuilder query = new StringBuilder();
			StringBuilder queryCantidad = new StringBuilder();
			generarQuery(query, tipo, referencia, presentacion, color, idSede);
			TypedQuery<Producto> productosInfoQuery = (TypedQuery<Producto>) entityManager.createNativeQuery(query.toString(),
					Producto.class);
			int pageNumber = paging.getPageNumber();
			int pageSize = paging.getPageSize();
			productosInfoQuery.setFirstResult(pageNumber * pageSize);
			productosInfoQuery.setMaxResults(pageSize);
			productos = productosInfoQuery.getResultList();
			for (Producto producto : productos) {
				List<Precio> precios = precio.listarTodosPorProducto(producto.getId());
				producto.setPrecios(precios);
			}
			generarQueryCantidad(queryCantidad, tipo, referencia, presentacion, color, idSede);
			Query cantidad = entityManager.createNativeQuery(queryCantidad.toString());
			BigInteger countResult = (BigInteger) cantidad.getSingleResult();
			Integer cantidadTotal = countResult.intValue();
			Page<Producto> result = new PageImpl<Producto>(productos, paging, cantidadTotal);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de productos exitosa");
			respuestaDto.setObjetoRespuesta(result);
			respuestaDto.setSuma(idSede);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo productos " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error obteniendo productos");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	private void generarQuery(StringBuilder query, Integer tipo, Integer referencia, Integer presentacion,
			Integer color, Integer idSede) {
		query.append("select * from producto");

		if (tipo != 0) {
			// tipo=tipo+1;
			query.append(" where tipo=" + tipo);
		}

		if (referencia != 0 && tipo != 0) {
			query.append(" and referencia=" + referencia);
		}

		else if (referencia != 0) {
			query.append(" where referencia=" + referencia);
		}

		if (presentacion != 0 && (referencia != 0 || tipo != 0)) {
			query.append("  and id_presentacion=" + presentacion);
		}

		else if (presentacion != 0) {
			query.append(" where id_presentacion=" + presentacion);
		}

		if (color != 0 && (referencia != 0 || tipo != 0 || presentacion != 0)) {
			query.append(" and id_color=" + color);
		}

		else if (color != 0) {
			query.append(" where id_color=" + color);
		}

		// if (idSede != 0 && (color != 0 || referencia != 0 || tipo != 0 ||
		// presentacion != 0)) {
		// query.append(" and id_sede =" + idSede);
		// } else if (idSede != 0) {
		// query.append(" where id_sede =" + idSede);
		// }
		logger.info(query);
		// query.append(" order by id desc");
	}

	private void generarQueryCantidad(StringBuilder query, Integer tipo, Integer referencia, Integer presentacion,
			Integer color, Integer idSede) {
		query.append("SELECT count(*) FROM producto");

		if (tipo != 0) {
			query.append(" where tipo=" + tipo);
		}

		if (referencia != 0 && tipo != 0) {
			query.append(" and referencia=" + referencia);
		}

		else if (referencia != 0) {
			query.append(" where referencia=" + referencia);
		}

		if (presentacion != 0 && (referencia != 0 || tipo != 0)) {
			query.append("  and id_presentacion=" + presentacion);
		}

		else if (presentacion != 0) {
			query.append(" where id_presentacion=" + presentacion);
		}

		if (color != 0 && (referencia != 0 || tipo != 0 || presentacion != 0)) {
			query.append(" and id_color=" + color);
		}

		else if (color != 0) {
			query.append(" where id_color=" + color);
		}

		// if (idSede != 0 && (color != 0 || referencia != 0 || tipo != 0 ||
		// presentacion != 0)) {
		// query.append(" and id_sede =" + idSede);
		// } else if (idSede != 0) {
		// query.append(" where id_sede =" + idSede);
		// }
		logger.info(query);

	}

	// Consulta avanzada de productos
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> modificarPreciosConsulta(Integer tipo, Integer referencia, Integer presentacion,
			Integer color, Double precioVenta) {
		ResponseEntity<Object> respuesta;
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao.buscarPorIdUsuario(usuarioAutenticado.getId());
		Integer idSede = usuarioInformacion.getIdOrganizacion().getId();
		// logger.info(usuarioInformacion.getIdOrganizacion());

		try {
			List<Producto> productos;
			StringBuilder query = new StringBuilder();
			// StringBuilder queryCantidad = new StringBuilder();
			generarQuery(query, tipo, referencia, presentacion, color, idSede);
			TypedQuery<Producto> productosInfoQuery = (TypedQuery<Producto>) entityManager.createNativeQuery(query.toString(),
					Producto.class);
			productos = productosInfoQuery.getResultList();

			for (Producto producto : productos) {
				// precio.insertarPrecioNuevo(precioVenta, producto.getId(), idSede, new
				// Date(new java.util.Date().getTime()));
				List<Precio> precios = precio.listarTodosPrecios(producto.getId(), idSede);
				for (Precio preciox : precios) {
					if (preciox.getSede() == usuarioInformacion.getIdOrganizacion()) {
						// logger.info("ingresa a preciox.getSede()");
						preciox.setActivo(0);
						precio.save(preciox);
					}
				}
				precio.insertarPrecioNuevo(precioVenta, producto.getId(), idSede, new Date(new java.util.Date().getTime()));
			}

			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de productos exitosa");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo productos " + e + " Linea error: "
					+ e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error obteniendo productos");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}
}
