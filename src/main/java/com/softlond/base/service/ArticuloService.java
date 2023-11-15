package com.softlond.base.service;

import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.Articulo;
import com.softlond.base.entity.ArticulosRemisionCompra;
import com.softlond.base.entity.Clientes;
import com.softlond.base.entity.EstadoArticulo;
import com.softlond.base.entity.FacturaArticulos;
import com.softlond.base.entity.FacturaCompra;
import com.softlond.base.entity.InformacionUsuario;
import com.softlond.base.entity.PrdTipos;
import com.softlond.base.entity.Precio;
import com.softlond.base.entity.Prefijo;
import com.softlond.base.entity.Producto;
import com.softlond.base.entity.Promocion;
import com.softlond.base.entity.RemisionVenta;
import com.softlond.base.entity.Secuencia;
import com.softlond.base.entity.Usuario;
import com.softlond.base.repository.ArticuloDao;
import com.softlond.base.repository.ArticulosRemisionCompraDao;
import com.softlond.base.repository.EstadoArticuloDao;
import com.softlond.base.repository.PrecioDao;
import com.softlond.base.repository.PromocionDao;
import com.softlond.base.repository.SecuenciaDao;
import com.softlond.base.repository.UsuarioInformacionDao;
import com.softlond.base.response.ProductoResponse;
import com.softlond.base.response.TotalesInfoArticulos;
import com.softlond.base.response.recaudoPagosReciboResponse;
import com.softlond.base.util.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigInteger;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

@Service
public class ArticuloService {

	private final Logger logger = Logger.getLogger(getClass());

	@Autowired
	private ArticuloDao articuloDao;
	@Autowired
	private SecuenciaDao secuenciaDao;
	@Autowired
	private StringUtils stringUtils;
	@Autowired
	private PrecioDao precioDao;
	@Autowired
	public ArticulosRemisionCompraDao articulosRemisionCompraDao;

	@Autowired
	UsuarioInformacionDao usuarioInformacionDao;

	@Autowired
	EstadoArticuloDao estadoArticuloDao;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	public PromocionDao promocionDao;

	public List<Articulo> listarTodos() throws Exception {
		List<Articulo> articulos = (List<Articulo>) articuloDao.findAll();
		return articulos;
	}

	public List<Articulo> listarPorSede(Integer idSede) throws Exception {
		if (idSede == null || idSede <= 0) {
			throw new Exception("ID no válido para sedes");
		}
		List<Articulo> articulos = articuloDao.buscarPorSede(idSede);
		for (Articulo articulo : articulos) {
			if (articulo.getProducto() != null) {
				Precio precio = precioDao.obtenerPrecio(articulo.getProducto().getId(), articulo.getSede().getId())
						.orElse(null);
				List<Precio> precios = new ArrayList<Precio>();
				articulo.getProducto().setPrecios(precios);
				articulo.getProducto().getPrecios().add(precio);
			}
		}
		return articulos;
	}

	public List<Articulo> listarPorProducto(Integer idProducto) throws Exception {
		if (idProducto == null || idProducto <= 0) {
			throw new Exception("ID no válido para productos");
		}
		List<Articulo> articulos = articuloDao.buscarPorProducto(idProducto);
		return articulos;
	}

	public List<Articulo> listarPorProductoYSede(Integer idProducto, Integer idSede) throws Exception {
		if (idSede == null || idSede <= 0) {
			throw new Exception("ID no válido para sedes");
		}
		if (idProducto == null || idProducto <= 0) {
			throw new Exception("ID no válido para productos");
		}
		List<Articulo> articulos = articuloDao.buscarPorProductoYSede(idProducto, idSede);
		return articulos;
	}

	public Articulo crearArticulo(Articulo body) throws Exception {
		// TODO: validar body
		Articulo nuevoArticulo = articuloDao.save(body);
		return nuevoArticulo;
	}

	public void editarArticulo(Articulo body) throws Exception {
		// TODO: validar body
		articuloDao.save(body);
	}

	public void elimiarArticulo(Integer idArticulo) throws Exception {
		Articulo articulo = articuloDao.findById(idArticulo).get();
		if (articulo == null) {
			throw new Exception("No existe un artículo con este ID");
		}
		articuloDao.delete(articulo);
	}

	private String generarCodigo(Articulo articulo) {
		Secuencia sec = secuenciaDao.buscarPorSede(articulo.getSede().getId());
		return "";
	}

	// listar
	@PreAuthorize("hasAuthority('USER') or hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerCodigo(Integer idSede) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		try {

			String codigo = this.articuloDao.codigo(idSede);

			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de codigos exitosas");
			respuestaDto.setObjetoRespuesta(codigo);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo codigos " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error obteniendo codigos");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	// Crear articulos
	@PreAuthorize("hasAuthority('USER') or hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> crearArticuloRemision(@RequestBody Articulo articuloNuevo) {
		// logger.info(articuloNuevo);
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		try {
			if (this.articuloDao.findByCodigo(articuloNuevo.getCodigo()) != null) {
				String nuevoCodigo = "0" + (Integer.parseInt(articuloNuevo.getCodigo()) + 1);
				articuloNuevo.setCodigo(nuevoCodigo);
			}
			Articulo guardado = this.articuloDao.save(articuloNuevo);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito creando el artículo");
			respuestaDto.setObjetoRespuesta(guardado);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error en la creación del articulo" + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error en crear articulo");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return respuesta;
	}

	// Actualizar
	@PreAuthorize("hasAuthority('USER') or hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> actualizarArticulo(Articulo articuloActualizado) {
		logger.info("ING actualizarArticulo");
		logger.info(articuloActualizado.getProducto());
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		try {
			Double t = (double) (Math.round((articuloActualizado.getPrecioCosto()) * Math.pow(10, 4)) / Math.pow(10, 4));
			Articulo articuloGuardar = this.articuloDao.findById(articuloActualizado.getId()).orElse(null);
			this.articuloDao.save(articuloActualizado);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito creando el artículo");
			respuestaDto.setObjetoRespuesta(articuloActualizado);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error en la actualización del articulo " + e.getStackTrace()[0].getLineNumber() + " " + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error en la actualización del articulo");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('USER') or hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerArticuloCodigo(String codigo) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		try {
			Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
			Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
			InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
					.buscarPorIdUsuario(usuarioAutenticado.getId());
			Integer idSede = usuarioInformacion.getIdOrganizacion().getId();
			Articulo articulo = this.articuloDao.obtenerCodigoSede(codigo, idSede);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de articulo exitosa");
			respuestaDto.setObjetoRespuesta(articulo);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo articulo " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error obteniendo articulo");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('USER') or hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerArticuloCodigoInventario(String codigo) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		try {
			Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
			Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
			InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
					.buscarPorIdUsuario(usuarioAutenticado.getId());
			Integer idSede = usuarioInformacion.getIdOrganizacion().getId();
			Articulo articuloC = this.articuloDao.obtenerCodigoSede(codigo, idSede);
			logger.info(articuloC.getId());
			List<Articulo> articulo = this.articuloDao.obtenerCodigoSedeInventario(articuloC.getId());
			logger.info(articulo.size());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de articulo exitosa");
			respuestaDto.setObjetoRespuesta(articulo);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo articulo " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error obteniendo articulo");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	private boolean existeArticulo(Articulo articuloBuscar) {

		boolean flag = false;

		if (this.articuloDao.findByCodigo(articuloBuscar.getCodigo()) != null) {
			flag = true;
		}

		return flag;
	}

	public ResponseEntity<Object> obtenerNumeroArticulos(Integer idProducto, Integer idProveedor) {
		ResponseEntity<Object> respuesta;
		RespuestaDto respuestaDto;
		try {
			Integer total = articuloDao.obtenerArticulosProducto(idProducto, idProveedor);
			respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
			respuestaDto.setObjetoRespuesta(total);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			String msj = "Error listando articulos por producto... " + e.getMessage();
			logger.error(e);
			respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.BAD_REQUEST);
		}
		return respuesta;
	}

	public ResponseEntity<Object> obtenerCostoArticulo(Integer idProducto, Integer idProveedor) {
		ResponseEntity<Object> respuesta;
		RespuestaDto respuestaDto;
		try {
			Integer precio = articuloDao.ultimoPrecio(idProducto, idProveedor);
			respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
			respuestaDto.setObjetoRespuesta(precio);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			String msj = "Error listando articulos por producto... " + e.getMessage();
			logger.error(e);
			respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.BAD_REQUEST);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('USER') or hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerArticulosSinRemision(Integer sede) {
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		try {
			List<Articulo> sinRemision = new ArrayList<Articulo>();
			List<Articulo> articulos = this.articuloDao.buscarPorSede(sede);

			for (Articulo articulo : articulos) {
				List<ArticulosRemisionCompra> remision = this.articulosRemisionCompraDao
						.buscarArticulo(articulo.getId());
				if (remision.size() == 0) {
					sinRemision.add(articulo);
				}

			}
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito obteniendo articulos sin remision");
			respuestaDto.setObjetoRespuesta(sinRemision);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception ex) {
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error obteniendo articulos sin remisión");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('USER') or hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerArticulosDisponibles(String texto, Integer page) {
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		Integer idSede = usuarioInformacion.getIdOrganizacion().getId();
		try {
			Pageable pageConfig = PageRequest.of(page, 10);
			Page<Articulo> articulos = articuloDao.obtenerArticulosDisponibles(texto, idSede, pageConfig);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito obteniendo articulos disponibles");
			respuestaDto.setObjetoRespuesta(articulos);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error obteniendo articulos disponibles");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerArticulosFiltros(Integer page, Integer tipo, Integer referencia,
			Integer presentacion, Integer color, String texto, String order, boolean sort) {
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		Integer idSede = usuarioInformacion.getIdOrganizacion().getId();
		Pageable paging = PageRequest.of(page, 10);
		try {
			List<Articulo> articulos;
			StringBuilder query = new StringBuilder();
			StringBuilder queryCantidad = new StringBuilder();
			generarQueryArticulosFiltros(idSede, tipo, referencia, presentacion, color, query, order, sort, texto);
			TypedQuery<Articulo> articulosInfoQuery = (TypedQuery<Articulo>) entityManager.createNativeQuery(query.toString(),
					Articulo.class);
			int pageNumber = paging.getPageNumber();
			int pageSize = paging.getPageSize();
			articulosInfoQuery.setFirstResult(pageNumber * pageSize);
			articulosInfoQuery.setMaxResults(pageSize);
			articulos = articulosInfoQuery.getResultList();
			generarQueryArticulosFiltrosCantidad(idSede, tipo, referencia, presentacion, color, queryCantidad, texto);
			Query cantidad = entityManager.createNativeQuery(queryCantidad.toString());
			BigInteger countResult = (BigInteger) cantidad.getSingleResult();
			Integer cantidadTotal = countResult.intValue();
			Page<Articulo> result = new PageImpl<Articulo>(articulos, paging, cantidadTotal);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de articulos exitosa");
			respuestaDto.setObjetoRespuesta(result);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo articulos " + e + "linea " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error obteniendo articulos");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerArticulosFiltrosCambiarEstado(Integer page, Integer tipo, Integer referencia,
			Integer presentacion, Integer color, String texto, String order, boolean sort) {
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		Integer idSede = usuarioInformacion.getIdOrganizacion().getId();
		Pageable paging = PageRequest.of(page, 10);
		try {
			List<Articulo> articulos;
			StringBuilder query = new StringBuilder();
			StringBuilder queryCantidad = new StringBuilder();
			generarQueryArticulosFiltrosCambioEstado(idSede, tipo, referencia, presentacion, color, query, order, sort,
					texto);
			TypedQuery<Articulo> articulosInfoQuery = (TypedQuery<Articulo>) entityManager.createNativeQuery(query.toString(),
					Articulo.class);
			int pageNumber = paging.getPageNumber();
			int pageSize = paging.getPageSize();
			articulosInfoQuery.setFirstResult(pageNumber * pageSize);
			articulosInfoQuery.setMaxResults(pageSize);
			articulos = articulosInfoQuery.getResultList();
			generarQueryArticulosFiltrosCantidad(idSede, tipo, referencia, presentacion, color, queryCantidad, texto);
			Query cantidad = entityManager.createNativeQuery(queryCantidad.toString());
			BigInteger countResult = (BigInteger) cantidad.getSingleResult();
			Integer cantidadTotal = countResult.intValue();
			Page<Articulo> result = new PageImpl<Articulo>(articulos, paging, cantidadTotal);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de articulos exitosa");
			respuestaDto.setObjetoRespuesta(result);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo articulos " + e + "linea " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error obteniendo articulos");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	// Listar todos los articulos por producto por id
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerArticuloProducto(Integer idProducto) {
		ResponseEntity<Object> respuesta;
		try {
			/*
			 * Authentication autenticacion =
			 * SecurityContextHolder.getContext().getAuthentication(); Usuario
			 * usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
			 * InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
			 * .buscarPorIdUsuario(usuarioAutenticado.getId());
			 * 
			 * int idSede = usuarioInformacion.getIdOrganizacion().getId();
			 */
			// int idSede = 11;

			Double articulos = this.articuloDao.obtenerCantidadArticulos(idProducto);

			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK,
					"obtencion del articulo del producto por id exitoso");
			respuestaDto.setObjetoRespuesta(articulos);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error("Error al listar el articulo del producto por id " + e.getMessage());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"No se pudo obtener el articulo del producto por id ");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	private void generarQueryArticulosFiltros(Integer idSede, Integer tipo, Integer referencia, Integer presentacion,
			Integer color, StringBuilder query, String order, boolean sort, String texto) {
		// query.append("select * from articulo a join (select CONCAT(pt.t_tipo, ' ',
		// pr.t_referencia,' ', pp.t_presentacion, ' ', pc.t_color) productox, p.* from
		// producto p) on a.id_producto = p.id where a.id_sede = " + idSede +" and
		// a.cantidad_disponible > 0 and a.nid_estado_articulo in (1,2)");
		// query.append("select * from articulo a join producto p on a.id_producto =
		// p.id where a.id_sede = " + idSede +" and a.cantidad_disponible > 0 and
		// a.nid_estado_articulo in (1,2)");
		query.append(
				"select a.*, p.* from articulo as a inner join producto as p inner join (select CONCAT(pt.t_tipo, ' ', pr.t_referencia,' ', pp.t_presentacion, ' ', pc.t_color) as productox, pin.* from producto as pin inner join prd_colores as pc inner join prd_tipos as pt inner join prd_referencia as pr inner join prd_presentacion as pp on pin.id_color = pc.id and pin.tipo = pt.id and pin.referencia = pr.id and pin.id_presentacion = pp.id) as n on p.id = a.id_producto and p.id = n.id where a.nid_estado_articulo in (1,2) and a.cantidad_disponible > 0 and a.id_sede ="
						+ idSede);
		if (tipo != 0) {
			query.append(" and p.tipo = " + tipo);
		}
		if (referencia != 0) {
			query.append(" and p.referencia = " + referencia);
		}
		if (presentacion != 0) {
			query.append(" and p.id_presentacion = " + presentacion);
		}
		if (color != 0) {
			query.append(" and p.id_color = " + color);
		}
		if (!texto.equals("null")) {
			query.append(" and (p.codigo like '%" + texto + "%' or n.productox like '%" + texto + "%' or a.codigo like '%"
					+ texto + "%')");
		}

		query.append(" group by a.id");
		if (sort) {
			query.append(" order by " + order + " desc");
		}
		logger.info(query);
	}

	private void generarQueryArticulosFiltrosCambioEstado(Integer idSede, Integer tipo, Integer referencia,
			Integer presentacion, Integer color, StringBuilder query, String order, boolean sort, String texto) {
		// query.append(
		// "select * from articulo a join producto p on a.id_producto = p.id where
		// a.nid_estado_articulo in (1,2) and a.cantidad_disponible > 0 and a.id_sede =
		// "
		// + idSede);
		query.append(
				"select * from articulo as a inner join producto as p inner join (select CONCAT(pt.t_tipo, ' ', pr.t_referencia,' ', pp.t_presentacion, ' ', pc.t_color) as productox, pin.* from producto as pin inner join prd_colores as pc inner join prd_tipos as pt inner join prd_referencia as pr inner join prd_presentacion as pp on pin.id_color = pc.id and pin.tipo = pt.id and pin.referencia = pr.id and pin.id_presentacion = pp.id) as n on p.id = a.id_producto and p.id = n.id where a.id_sede ="
						+ idSede);
		if (tipo != 0) {
			query.append(" and p.tipo = " + tipo);
		}
		if (referencia != 0) {
			query.append(" and p.referencia = " + referencia);
		}
		if (presentacion != 0) {
			query.append(" and p.id_presentacion = " + presentacion);
		}
		if (color != 0) {
			query.append(" and p.id_color = " + color);
		}
		// if (!texto.equals("null")) {
		// query.append(" and (p.codigo like '%" + texto + "%' or p.producto like '%" +
		// texto
		// + "%' or a.codigo like '%" + texto + "%')");
		// }

		if (!texto.equals("null")) {
			query.append(" and (p.codigo like '%" + texto + "%' or n.productox like '%" + texto
					+ "%' or a.codigo like '%" + texto + "%')");
		}
		if (sort) {
			query.append(" order by " + order + " desc");
		}
		logger.info(query);
	}

	private void generarQueryArticulosFiltrosCantidad(Integer idSede, Integer tipo, Integer referencia,
			Integer presentacion, Integer color, StringBuilder query, String texto) {
		// query.append("select count(*) from articulo a join producto p on
		// a.id_producto = p.id where a.id_sede = " + idSede);
		query.append(
				"select count(*) from articulo as a inner join producto as p inner join (select CONCAT(pt.t_tipo, ' ', pr.t_referencia,' ', pp.t_presentacion, ' ', pc.t_color) as productox, pin.* from producto as pin inner join prd_colores as pc inner join prd_tipos as pt inner join prd_referencia as pr inner join prd_presentacion as pp on pin.id_color = pc.id and pin.tipo = pt.id and pin.referencia = pr.id and pin.id_presentacion = pp.id) as n on p.id = a.id_producto and p.id = n.id where a.nid_estado_articulo in (1,2) and a.cantidad_disponible > 0 and a.id_sede ="
						+ idSede);
		if (tipo != 0) {
			query.append(" and p.tipo = " + tipo);
		}
		if (referencia != 0) {
			query.append(" and p.referencia = " + referencia);
		}
		if (presentacion != 0) {
			query.append(" and p.id_presentacion = " + presentacion);
		}
		if (color != 0) {
			query.append(" and p.id_color = " + color);
		}
		if (!texto.equals("null")) {
			query.append(" and (p.codigo like '%" + texto + "%' or n.productox like '%" + texto
					+ "%' or a.codigo like '%" + texto + "%')");
		}

		// logger.info(query);
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerArticulosFiltrosExportar(Integer tipo, Integer referencia, Integer presentacion,
			Integer color, String texto) {
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao.buscarPorIdUsuario(usuarioAutenticado.getId());
		Integer idSede = usuarioInformacion.getIdOrganizacion().getId();
		try {
			List<Articulo> articulos;
			StringBuilder query = new StringBuilder();
			generarQueryArticulosFiltros(idSede, tipo, referencia, presentacion, color, query, "", false, texto);
			TypedQuery<Articulo> articulosInfoQuery = (TypedQuery<Articulo>) entityManager.createNativeQuery(query.toString(),
					Articulo.class);
			articulos = articulosInfoQuery.getResultList();
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de articulos exitosa");
			respuestaDto.setObjetoRespuesta(articulos);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo articulos " + e + "linea " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error obteniendo articulos");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	// Consulta avanzada recodificar productos
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerRecodificarProductosConsulta(Integer producto, Integer estadoArticulo,
			Integer page) {
		ResponseEntity<Object> respuesta;
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		Pageable paging = PageRequest.of(page, 10);
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		try {
			List<Articulo> articulos;
			StringBuilder query = new StringBuilder();
			StringBuilder queryCantidad = new StringBuilder();
			generarQuery(query, usuarioInformacion.getIdOrganizacion().getId(), producto, estadoArticulo);
			TypedQuery<Articulo> artiuclosInfoQuery = (TypedQuery<Articulo>) entityManager.createNativeQuery(query.toString(),
					Articulo.class);
			int pageNumber = paging.getPageNumber();
			int pageSize = paging.getPageSize();
			artiuclosInfoQuery.setFirstResult(pageNumber * pageSize);
			artiuclosInfoQuery.setMaxResults(pageSize);
			articulos = artiuclosInfoQuery.getResultList();
			/*
			 * for (RemisionVenta remision : remisiones) { List<FacturaArticulos> articulos
			 * = facturaArticuloDao.findAllBySedeRemision(remision.getId());
			 * remision.setFacArticulos(articulos); }
			 */
			generarQueryCantidad(queryCantidad, usuarioInformacion.getIdOrganizacion().getId(), producto,
					estadoArticulo);
			Query cantidad = entityManager.createNativeQuery(queryCantidad.toString());
			BigInteger countResult = (BigInteger) cantidad.getSingleResult();
			Integer cantidadTotal = countResult.intValue();
			Page<Articulo> result = new PageImpl<Articulo>(articulos, paging, cantidadTotal);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de articulos exitosa");
			respuestaDto.setObjetoRespuesta(result);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo articulos " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error obteniendo articulos " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	private void generarQuery(StringBuilder query, Integer idSede, Integer producto, Integer estadoArticulo) {
		query.append("select * from articulo where id_sede= ");

		query.append("" + idSede);

		if (producto != 0) {
			query.append(" and id_producto=" + producto);
		}

		if (estadoArticulo != 0 && producto != 0) {
			query.append(" and nid_estado_articulo=" + estadoArticulo);
		}

		else if (estadoArticulo != 0) {
			query.append(" and nid_estado_articulo=" + estadoArticulo);
		}

		// query.append(" order by id asc limit 10");
	}

	private void generarQueryCantidad(StringBuilder query, Integer idSede, Integer producto, Integer estadoArticulo) {
		query.append("SELECT count(*) FROM articulo where id_sede= ");
		query.append("" + idSede);

		if (producto != 0) {
			query.append(" and id_producto=" + producto);
		}

		if (estadoArticulo != 0 && producto != 0) {
			query.append(" and nid_estado_articulo=" + estadoArticulo);
		}

		else if (estadoArticulo != 0) {
			query.append(" and nid_estado_articulo=" + estadoArticulo);
		}

	}

	// Listar todos los articulos por id y sede
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerArticuloId(Integer idArticulo) {
		ResponseEntity<Object> respuesta;
		try {
			Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
			Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
			InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
					.buscarPorIdUsuario(usuarioAutenticado.getId());

			int idSede = usuarioInformacion.getIdOrganizacion().getId();
			// int idSede = 11;

			Articulo articulos = this.articuloDao.obtenerArticuloId(idSede, idArticulo);

			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK,
					"obtencion del precio del producto por id y sede exitoso");
			respuestaDto.setObjetoRespuesta(articulos);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error("Error al listar el precio del producto por id y sede" + e.getMessage());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"No se pudo obtener el precio del producto por id y sede");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> ActualizarCodificarProducto(@RequestBody Articulo articulo) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());

		try {
			articulo.setSede(usuarioInformacion.getIdOrganizacion());
			List<Precio> precios = precioDao.listarTodosPorProducto(articulo.getProducto().getId());
			for (Precio precio : precios) {
				precio.setProducto(articulo.getProducto());
			}
			articulo.getProducto().setPrecios(precios);
			Articulo guardado = this.articuloDao.save(articulo);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK,
					"Exito actualizando el id del producto del articulo");
			respuestaDto.setObjetoRespuesta(guardado);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);

		} catch (Exception e) {
			logger.error("Error en la actualizacion el id del producto del articulo" + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error en la actualizacion el id del producto del articulo " + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return respuesta;
	}

	@PreAuthorize("hasAuthority('USER') or hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> cambiarEstadoArticulo(@RequestBody Articulo articuloNuevo) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		try {
			Articulo articulo = articuloDao.findById(articuloNuevo.getId()).orElse(null);
			articulo.setEstadoArticulo(articuloNuevo.getEstadoArticulo());
			articulo.setCantidadDisponible(articuloNuevo.getCantidadDisponible());
			this.articuloDao.save(articulo);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito al cambiar el estado del articulo");
			respuestaDto.setObjetoRespuesta(articulo);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error al cambiar el estado del articulo" + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al cambiar el estado del articulo");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return respuesta;
	}

	@PreAuthorize("hasAuthority('USER') or hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerArticulosPorProducto(Integer idProducto, Integer page, String order,
			String column) {
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		String sumas = "";
		try {
			Sort sort;
			if (order.equals("desc")) {
				sort = new Sort(Sort.Direction.DESC, column);
			} else {
				sort = new Sort(Sort.Direction.ASC, column);
			}
			Pageable pageConfig = PageRequest.of(page, 10, sort);
			Page<Articulo> articulos = articuloDao.buscarPorProductoDisponible(idProducto, pageConfig);
			sumas = articuloDao.findByPagoTotalArticulosInventario(idProducto).toString();
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito obteniendo articulos");
			respuestaDto.setObjetoRespuesta(articulos);
			logger.info(sumas);
			respuestaDto.setSumas(sumas);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error obteniendo articulos " + ex + " " + ex.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error obteniendo articulos");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	public List<Articulo> listarTodosSelector() throws Exception {
		List<Articulo> articulos = (List<Articulo>) articuloDao.findAll();
		return articulos;
	}

	public List<Articulo> listarTodosFiltroTexto(String texto) throws Exception {
		List<Articulo> articulos = (List<Articulo>) articuloDao.obtenerArticulosTexto(texto);
		return articulos;
	}

	// Consulta avanzada articulos
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerArticulosConsulta(Integer articulo, Integer estadoArticulo, Integer page) {
		ResponseEntity<Object> respuesta;
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		Pageable paging = PageRequest.of(page, 10);
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		try {
			List<Articulo> articulos;
			StringBuilder query = new StringBuilder();
			StringBuilder queryCantidad = new StringBuilder();
			generarQueryArticulo(query, usuarioInformacion.getIdOrganizacion().getId(), articulo, estadoArticulo);
			TypedQuery<Articulo> artiuclosInfoQuery = (TypedQuery<Articulo>) entityManager.createNativeQuery(query.toString(),
					Articulo.class);
			int pageNumber = paging.getPageNumber();
			int pageSize = paging.getPageSize();
			artiuclosInfoQuery.setFirstResult(pageNumber * pageSize);
			artiuclosInfoQuery.setMaxResults(pageSize);
			articulos = artiuclosInfoQuery.getResultList();
			generarQueryCantidadArticulo(queryCantidad, usuarioInformacion.getIdOrganizacion().getId(), articulo,
					estadoArticulo);
			Query cantidad = entityManager.createNativeQuery(queryCantidad.toString());
			BigInteger countResult = (BigInteger) cantidad.getSingleResult();
			Integer cantidadTotal = countResult.intValue();
			Page<Articulo> result = new PageImpl<Articulo>(articulos, paging, cantidadTotal);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de articulos exitosa");
			respuestaDto.setObjetoRespuesta(result);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo articulos " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error obteniendo articulos " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	private void generarQueryArticulo(StringBuilder query, Integer idSede, Integer articulo, Integer estadoArticulo) {
		query.append("select * from articulo where id_sede= ");

		query.append("" + idSede);

		if (articulo != 0) {
			query.append(" and id = " + articulo);
		}

		if (estadoArticulo != 0 && articulo != 0) {
			query.append(" and nid_estado_articulo=" + estadoArticulo);
		}

		else if (estadoArticulo != 0) {
			query.append(" and nid_estado_articulo=" + estadoArticulo);
		}

		// query.append(" order by id asc limit 10");
	}

	private void generarQueryCantidadArticulo(StringBuilder query, Integer idSede, Integer articulo,
			Integer estadoArticulo) {
		query.append("SELECT count(*) FROM articulo where id_sede= ");
		query.append("" + idSede);

		if (articulo != 0) {
			query.append(" and id = " + articulo);
		}

		if (estadoArticulo != 0 && articulo != 0) {
			query.append(" and nid_estado_articulo=" + estadoArticulo);
		}

		else if (estadoArticulo != 0) {
			query.append(" and nid_estado_articulo=" + estadoArticulo);
		}

	}

	@PreAuthorize("hasAuthority('USER') or hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerArticulos() {
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		try {
			List<Articulo> articulos = articuloDao.buscarArticulo();
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito obteniendo articulos");
			respuestaDto.setObjetoRespuesta(articulos);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception ex) {
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error obteniendo articulos");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	// Consulta avanzada articulos
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerArticulosProductoMovimientoConsulta(Integer producto, Integer articulo,
			Integer estadoArticulo, Integer page) {
		ResponseEntity<Object> respuesta;
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		Pageable paging = PageRequest.of(page, 10);
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		try {
			List<Articulo> articulos;
			StringBuilder query = new StringBuilder();
			StringBuilder queryCantidad = new StringBuilder();
			generarQueryArticuloProducto(query, usuarioInformacion.getIdOrganizacion().getId(), producto, articulo,
					estadoArticulo);
			TypedQuery<Articulo> artiuclosInfoQuery = (TypedQuery<Articulo>) entityManager.createNativeQuery(query.toString(),
					Articulo.class);
			int pageNumber = paging.getPageNumber();
			int pageSize = paging.getPageSize();
			artiuclosInfoQuery.setFirstResult(pageNumber * pageSize);
			artiuclosInfoQuery.setMaxResults(pageSize);
			articulos = artiuclosInfoQuery.getResultList();
			generarQueryCantidadArticuloProducto(queryCantidad, usuarioInformacion.getIdOrganizacion().getId(), producto,
					articulo, estadoArticulo);
			Query cantidad = entityManager.createNativeQuery(queryCantidad.toString());
			BigInteger countResult = (BigInteger) cantidad.getSingleResult();
			Integer cantidadTotal = countResult.intValue();
			Page<Articulo> result = new PageImpl<Articulo>(articulos, paging, cantidadTotal);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de articulos exitosa");
			respuestaDto.setObjetoRespuesta(result);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo articulos " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error obteniendo articulos " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	private void generarQueryArticuloProducto(StringBuilder query, Integer idSede, Integer producto, Integer articulo,
			Integer estadoArticulo) {
		query.append("select * from articulo where id_sede= ");

		query.append("" + idSede);

		if (producto != 0) {
			query.append(" and id_producto = " + producto);
		}

		if (articulo != 0 && producto != 0) {
			query.append(" and id = " + articulo);
		}

		else if (articulo != 0) {
			query.append(" and id = " + articulo);
		}

		if (estadoArticulo != 0 && (producto != 0 || articulo != 0)) {
			query.append(" and nid_estado_articulo=" + estadoArticulo);
		}

		else if (estadoArticulo != 0) {
			query.append(" and nid_estado_articulo=" + estadoArticulo);
		}

		// query.append(" order by id asc limit 10");
	}

	private void generarQueryCantidadArticuloProducto(StringBuilder query, Integer idSede, Integer producto,
			Integer articulo, Integer estadoArticulo) {
		query.append("SELECT count(*) FROM articulo where id_sede= ");
		query.append("" + idSede);

		if (producto != 0) {
			query.append(" and id_producto = " + producto);
		}

		if (articulo != 0 && producto != 0) {
			query.append(" and id = " + articulo);
		}

		else if (articulo != 0) {
			query.append(" and id = " + articulo);
		}

		if (estadoArticulo != 0 && (producto != 0 || articulo != 0)) {
			query.append(" and nid_estado_articulo=" + estadoArticulo);
		}

		else if (estadoArticulo != 0) {
			query.append(" and nid_estado_articulo=" + estadoArticulo);
		}
	}

	@PreAuthorize("hasAuthority('USER') or hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerArticulosPorLocal(Integer local) {
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		Integer idSede = usuarioInformacion.getIdOrganizacion().getId();
		try {
			List<Articulo> articulos = articuloDao.buscarArticulosPorLocal(idSede, local);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito obteniendo articulos");
			respuestaDto.setObjetoRespuesta(articulos);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error obteniendo articulos " + ex + " " + ex.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error obteniendo articulos");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('USER') or hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerArticulosDisponibles(Integer page, String order, String column) {
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		String sumas = "";
		try {
			Sort sort;
			if (order.equals("desc")) {
				sort = new Sort(Sort.Direction.DESC, column);
			} else {
				sort = new Sort(Sort.Direction.ASC, column);
			}
			Pageable pageConfig = PageRequest.of(page, 10, sort);
			Page<Articulo> articulos = articuloDao.buscarDisponible(usuarioInformacion.getIdOrganizacion().getId(),
					pageConfig);
			sumas = articuloDao.findByPagoTotalArticulosDisponibles(usuarioInformacion.getIdOrganizacion().getId())
					.toString();
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito obteniendo articulos");
			respuestaDto.setObjetoRespuesta(articulos);
			respuestaDto.setSumas(sumas);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error obteniendo articulos " + ex + " " + ex.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error obteniendo articulos");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('USER') or hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerArticulosLocalSector(Integer page, String local, String sector, String order,
			String column) {
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		Page<Articulo> articulos = null;
		String sumas = "";
		try {
			Sort sort;
			if (order.equals("desc")) {
				sort = new Sort(Sort.Direction.DESC, column);
			} else {
				sort = new Sort(Sort.Direction.ASC, column);
			}
			Pageable pageConfig = PageRequest.of(page, 10, sort);
			logger.info(sector);
			if (sector.equals("null")) {
				logger.info(sector);
				articulos = articuloDao.buscarDisponibleLocal(
						usuarioInformacion.getIdOrganizacion().getId(), local, pageConfig);
				sumas = articuloDao.findByPagoTotalArticulosLocal(usuarioInformacion.getIdOrganizacion().getId(), local)
						.toString();
			} else {
				articulos = articuloDao.buscarDisponibleLocalSector(
						usuarioInformacion.getIdOrganizacion().getId(), local, sector, pageConfig);
				sumas = articuloDao
						.findByPagoTotalArticulosLocalSector(usuarioInformacion.getIdOrganizacion().getId(), local, sector)
						.toString();
			}
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito obteniendo articulos");
			respuestaDto.setObjetoRespuesta(articulos);
			respuestaDto.setSumas(sumas);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error obteniendo articulos " + ex + " " + ex.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error obteniendo articulos");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('USER') or hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerTodosArticulos(Integer page, String order, String column) {
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		String sumas = "";
		try {
			Sort sort;
			if (order.equals("desc")) {
				sort = new Sort(Sort.Direction.DESC, column);
			} else {
				sort = new Sort(Sort.Direction.ASC, column);
			}
			Pageable pageConfig = PageRequest.of(page, 10, sort);
			Page<Articulo> articulos = articuloDao.obtenerTodosSede(usuarioInformacion.getIdOrganizacion().getId(),
					pageConfig);
			sumas = articuloDao.findByPagoTotalArticulos(usuarioInformacion.getIdOrganizacion().getId()).toString();
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito obteniendo articulos");
			respuestaDto.setObjetoRespuesta(articulos);
			respuestaDto.setSumas(sumas);

			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error obteniendo articulos " + ex + " " + ex.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error obteniendo articulos");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	public List<Articulo> listarPorSedeCodigo(Integer idSede, String codigo) throws Exception {
		if (idSede == null || idSede <= 0) {
			throw new Exception("ID no válido para sedes");
		}

		List<Articulo> articulos = articuloDao.buscarPorSedeCodigo(idSede, codigo);

		for (Articulo articulo : articulos) {
			if (articulo.getProducto() != null) {
				// Precio precio = precioDao.obtenerPrecio(articulo.getProducto().getId(),
				// articulo.getSede().getId()).orElse(null);
				List<Precio> precios = precioDao.listarTodosPorProducto(articulo.getProducto().getId());
				articulo.getProducto().setPrecios(precios);
			}
		}
		return articulos;
	}

	public List<Articulo> listarPorSedeCodigoLocal(Integer idSede, String codigo, Integer local) throws Exception {
		if (idSede == null || idSede <= 0 || local == null) {
			throw new Exception("ID no válido para sedes");
		}

		List<Articulo> articulos = articuloDao.buscarPorSedeCodigoLocal(idSede, codigo, local);

		for (Articulo articulo : articulos) {
			if (articulo.getProducto() != null) {
				// Precio precio = precioDao.obtenerPrecio(articulo.getProducto().getId(),
				// articulo.getSede().getId()).orElse(null);
				List<Precio> precios = precioDao.listarTodosPorProducto(articulo.getProducto().getId());
				articulo.getProducto().setPrecios(precios);
			}
		}
		return articulos;
	}

	@PreAuthorize("hasAuthority('USER') or hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerArticulosExistentesProducto(Integer idProducto, Integer page) {
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		try {
			Pageable pageConfig = PageRequest.of(page, 10);
			Page<Articulo> articulos = articuloDao.obtenerSedeExistenteProductos(
					usuarioInformacion.getIdOrganizacion().getId(), idProducto, pageConfig);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito obteniendo articulos");
			respuestaDto.setObjetoRespuesta(articulos);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error obteniendo articulos " + ex + " " + ex.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error obteniendo articulos");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerTotalesArticulosFiltros(Integer tipo, Integer referencia, Integer presentacion,
			Integer color, String texto) {
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		Integer idSede = usuarioInformacion.getIdOrganizacion().getId();
		try {
			StringBuilder query = new StringBuilder();
			StringBuilder queryCantidad = new StringBuilder();
			generarQueryTotalesArticulosFiltros(idSede, tipo, referencia, presentacion, color, query, texto);
			org.hibernate.Query resultado = (org.hibernate.Query) entityManager.createNativeQuery(query.toString());
			TotalesInfoArticulos valores = (TotalesInfoArticulos) resultado
					.setResultTransformer(Transformers.aliasToBean(TotalesInfoArticulos.class)).getSingleResult();
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de articulos exitosa");
			respuestaDto.setObjetoRespuesta(valores);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo articulos " + e + "linea " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error obteniendo articulos");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	private void generarQueryTotalesArticulosFiltros(Integer idSede, Integer tipo, Integer referencia,
			Integer presentacion, Integer color, StringBuilder query, String texto) {
		query.append(
				"select ifnull(sum(a.cantidad_disponible),0) as totalDisponible, ifnull(sum(a.cantidad_disponible * a.precio_costo),0) as totalValorArticulos from articulo a join producto p on a.id_producto = p.id where a.nid_estado_articulo in (1,2) and a.cantidad_disponible > 0 and a.id_sede = "
						+ idSede);
		if (tipo != 0) {
			query.append(" and p.tipo = " + tipo);
		}
		if (referencia != 0) {
			query.append(" and p.referencia = " + referencia);
		}
		if (presentacion != 0) {
			query.append(" and p.id_presentacion = " + presentacion);
		}
		if (color != 0) {
			query.append(" and p.id_color = " + color);
		}
		if (!texto.equals("null")) {
			query.append(" and (p.codigo like '%" + texto + "%' or p.producto like '%" + texto
					+ "%' or a.codigo like '%" + texto + "%')");
		}
		// logger.info(query);
	}

	// Consulta avanzada articulos para movimientos
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerArticulosProductoMovimientosConsulta(String producto, String articulo,
			Integer estadoArticulo, Integer page) {
		ResponseEntity<Object> respuesta;
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		Pageable paging = PageRequest.of(page, 10);
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		try {
			List<Articulo> articulos;
			StringBuilder query = new StringBuilder();
			StringBuilder queryCantidad = new StringBuilder();
			generarQueryArticulosProductos(query, usuarioInformacion.getIdOrganizacion().getId(), producto, articulo,
					estadoArticulo);

			TypedQuery<Articulo> artiuclosInfoQuery = (TypedQuery<Articulo>) entityManager.createNativeQuery(query.toString(),
					Articulo.class);
			int pageNumber = paging.getPageNumber();
			int pageSize = paging.getPageSize();
			artiuclosInfoQuery.setFirstResult(pageNumber * pageSize);
			artiuclosInfoQuery.setMaxResults(pageSize);
			articulos = artiuclosInfoQuery.getResultList();
			generarQueryCantidadArticulosProductos(queryCantidad, usuarioInformacion.getIdOrganizacion().getId(), producto,
					articulo, estadoArticulo);
			Query cantidad = entityManager.createNativeQuery(queryCantidad.toString());
			BigInteger countResult = (BigInteger) cantidad.getSingleResult();
			Integer cantidadTotal = countResult.intValue();
			Page<Articulo> result = new PageImpl<Articulo>(articulos, paging, cantidadTotal);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de articulos exitosa");
			respuestaDto.setObjetoRespuesta(result);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo articulos " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error obteniendo articulos " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	private void generarQueryArticulosProductos(StringBuilder query, Integer idSede, String producto, String articulo,
			Integer estadoArticulo) {
		query.append("select * from articulo a join producto p on a.id_producto = p.id where a.id_sede= ");

		query.append("" + idSede);

		if (!producto.equals("")) {
			query.append(" and (p.codigo like '%" + producto + "%' or p.producto like '%" + producto + "%')");
		}

		if (!articulo.equals("")) {
			query.append(" and a.codigo like '%" + articulo + "%'");
		}
		if (estadoArticulo != 0) {
			query.append(" and nid_estado_articulo=" + estadoArticulo);
		}
	}

	private void generarQueryCantidadArticulosProductos(StringBuilder query, Integer idSede, String producto,
			String articulo, Integer estadoArticulo) {
		query.append("select count(*) from articulo a join producto p on a.id_producto = p.id where a.id_sede= ");

		query.append("" + idSede);

		if (!producto.equals("null")) {
			query.append(" and (p.codigo like '%" + producto + "%' or p.producto like '%" + producto + "%')");
		}

		if (!articulo.equals("null")) {
			query.append(" and a.codigo like '%" + articulo + "%'");
		}
		if (estadoArticulo != 0) {
			query.append(" and nid_estado_articulo=" + estadoArticulo);
		}
	}

	@PreAuthorize("hasAuthority('USER') or hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerArticuloSede(String codigo) {
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao.buscarPorIdUsuario(usuarioAutenticado.getId());
		Integer idSede = usuarioInformacion.getIdOrganizacion().getId();

		try {
			Articulo articulo = articuloDao.obtenerCodigoSede(codigo, idSede);
			if (articulo != null) {
				List<Precio> precios = precioDao.obtenerPrecioArticulo(articulo.getProducto().getId());
				articulo.getProducto().setPrecios(precios);
			}
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito obteniendo articulos");
			respuestaDto.setObjetoRespuesta(articulo);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error obteniendo articulos " + ex + " " + ex.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error obteniendo articulos");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('USER') or hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerPrecioCostoUltimoArticulo(Integer idProducto) {
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		Integer idSede = usuarioInformacion.getIdOrganizacion().getId();
		try {
			Double precioCosto = articuloDao.obtenerUltimoPrecioCosto(idProducto, idSede);
			if (precioCosto == null) {
				precioCosto = 0.0;
			}

			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito obteniendo precio de costo");
			respuestaDto.setObjetoRespuesta(precioCosto);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error obteniendo precio de costo " + ex + " " + ex.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error obteniendo precio de costo");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('USER') or hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerPrecioVenta(Integer idProducto) {
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao.buscarPorIdUsuario(usuarioAutenticado.getId());
		Integer idSede = usuarioInformacion.getIdOrganizacion().getId();
		try {
			Double precioVenta = articuloDao.obtenerPrecioVenta(idProducto, idSede);
			if (precioVenta == null) {
				precioVenta = 0.0;
			}

			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito obteniendo precio de venta");
			respuestaDto.setObjetoRespuesta(precioVenta);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error obteniendo precio de venta " + ex + " " + ex.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error obteniendo precio de venta");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

}
