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
import com.softlond.base.entity.PrdDescuentos;
import com.softlond.base.entity.PrdColores;
import com.softlond.base.entity.Precio;
import com.softlond.base.entity.Producto;
import com.softlond.base.entity.Proveedor;
import com.softlond.base.repository.FacturaArticuloDao;
import com.softlond.base.repository.PrecioDao;
import com.softlond.base.repository.ProductoDao;
import com.softlond.base.repository.ProductoProveedorDao;
import com.softlond.base.request.DevolucionRequest;
import com.softlond.base.response.ProductoCliente;
import com.softlond.base.entity.RemisionVenta;
import com.softlond.base.entity.Usuario;
import com.softlond.base.repository.ArticuloDao;
import com.softlond.base.repository.PrecioDao;
import com.softlond.base.repository.ProductoDao;
import com.softlond.base.repository.UsuarioInformacionDao;
import com.softlond.base.repository.FacturaArticuloDao;
import com.softlond.base.repository.PrecioDao;
import com.softlond.base.repository.ProductoDao;
import com.softlond.base.request.DevolucionRequest;
import com.softlond.base.response.ProductoCliente;
import com.softlond.base.response.ProductoPrecio;
import com.softlond.base.response.ProductoResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

@Service
public class ProductoService {

	private static final Logger logger = Logger.getLogger(ProductoService.class);

	@Autowired
	private ProductoDao productoDao;
	@Autowired
	UsuarioInformacionDao usuarioInformacionDao;

	@Autowired
	private PrecioDao precio;

	@Autowired
	private ArticuloDao articuloDao;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private FacturaArticuloDao facArticuloDao;

	@Autowired
	private ProductoProveedorDao productoProveedorDao;

	public List<Producto> listarTodos(Integer idI) throws Exception {
		// List<Producto> productos = (List<Producto>) productoDao.findAll();
		logger.info(idI);
		List<Producto> productos = (List<Producto>) productoDao.obtenerProductosRango(idI);
		return productos;
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> crearProductos(Producto producto) {
		ResponseEntity<Object> respuesta;

		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		try {
			for (Precio precio : producto.getPrecios()) {
				precio.setSede(usuarioInformacion.getIdOrganizacion());
			}
			Producto proveedorBusqueda = productoDao.findBycodigo(producto.getCodigo());
			if (proveedorBusqueda != null)
				throw new Exception();
			productoDao.save(producto);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "creación de productos exitosa");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);

		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.INTERNAL_SERVER_ERROR);
			logger.error("Error en la creación de producto " + e.getMessage() + "linea "
					+ e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"No se puede crear un producto con un mismo código");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return respuesta;
	}

	// Crear Producto
	/*
	 * @PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER')"
	 * ) public ResponseEntity<Object> crearProductos(@RequestBody Producto
	 * producto) {
	 * 
	 * ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
	 * Authentication autenticacion =
	 * SecurityContextHolder.getContext().getAuthentication(); Usuario
	 * usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
	 * InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
	 * .buscarPorIdUsuario(usuarioAutenticado.getId());
	 * 
	 * try { for (Precio precio : producto.getPrecios()) {
	 * precio.setSede(usuarioInformacion.getIdOrganizacion()); }
	 * 
	 * Producto proveedorBusqueda = productoDao.findByCod(producto.getCodigo()); if
	 * (proveedorBusqueda != null) throw new Exception();
	 * productoDao.save(producto);
	 * 
	 * respuesta = ResponseEntity.ok(HttpStatus.OK); RespuestaDto respuestaDto = new
	 * RespuestaDto(HttpStatus.OK, "Exito creando producto"); respuesta = new
	 * ResponseEntity<>(respuestaDto, HttpStatus.OK);
	 * 
	 * } catch (Exception e) {
	 * 
	 * respuesta = ResponseEntity.ok(HttpStatus.INTERNAL_SERVER_ERROR);
	 * //logger.error("Error en la creación de producto " + e.getMessage());
	 * RespuestaDto respuestaDto = new
	 * RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
	 * "No se puede crear un producto con un mismo código"); respuesta = new
	 * ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR); }
	 * 
	 * return respuesta; }
	 */

	public Producto crearProducto(Producto body) throws Exception {
		// TODO: validar body
		logger.info("ingresa a crear producto service");
		Producto nuevoProducto = productoDao.save(body);
		return nuevoProducto;
	}

	public void editarProducto(Producto body) throws Exception {
		// TODO: validar body
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		for (Precio precio : body.getPrecios()) {
			precio.setSede(usuarioInformacion.getIdOrganizacion());
		}
		productoDao.save(body);
	}

	public void editarProductoRecodificar(Integer page, Producto body, Integer id) throws Exception {
		// TODO: validar body
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		Pageable pageConfig = PageRequest.of(page, 10);
		// Producto productoE =
		// productoDao.obtenerTodosProductoRecodificar(pageConfig, tipo, referencia,
		// presentacion, color);
		// List<Producto> productoE = productoDao.buscarProductoDescuentoc(tipo,
		// referencia, presentacion);
		// if (!productoE.equals(null)) {
		// logger.info(productoE.getCodigo());
		// } else {
		// logger.info("no hay productoE");
		// }
		logger.info(body.getId()); // producto del que se transfieren los articulos
		logger.info(id);// producto a transferirle los articulos

		List<Articulo> articulos = articuloDao.obtenerArticuloProducto(body.getId());
		logger.info(articulos.size()); // 11
		Integer cantidadActual = articuloDao.cantidadArticuloProducto(id);
		logger.info(cantidadActual); // 3

		// for (int index = 0; index < articulos.size(); index++) {
		// logger.info(articulos.get(index).getId() + " - cod: " +
		// articulos.get(index).getCodigo());
		// }

		for (int index = 0; index < articulos.size(); index++) {
			articuloDao.vincularProductoDeArticulos(id, articulos.get(index).getId());
		}

		List<Articulo> articulos1 = articuloDao.obtenerArticuloProducto(body.getId());
		logger.info(articulos1.size()); // 0
		Integer cantidadActual1 = articuloDao.cantidadArticuloProducto(id);
		logger.info(cantidadActual1); // 14

		// logger.info(body.getProducto());
		// logger.info(body.getArticulos());
		// logger.info(body.getCodigo());
		// logger.info(body.getId());
		// Integer cantidadActual = articuloDao.cantidadArticuloProducto(id);
		// logger.info(cantidadActual);
		// float cantidadDisponibleNueva = cantidadActual + articulos.size();
		// logger.info(cantidadDisponibleNueva);
		// articuloDao.cambiarCantidadDisponibleRecodificar(cantidadDisponibleNueva,
		// body.getId());
		// for (int i = 0; i < body.getArticulos().size(); i++) {
		// logger.info("ingresa al ciclo para recorrer articulos body");
		// logger.info(body.getArticulos().get(i).getProducto().getProducto());
		// }

		// for (Precio precio : body.getPrecios()) {
		// precio.setSede(usuarioInformacion.getIdOrganizacion());
		// }
		// productoDao.save(body);
		// return;
	}

	public void eliminarProducto(Integer idProducto) throws Exception {
		Producto producto = productoDao.findById(idProducto).get();
		if (producto == null) {
			throw new Exception("No existe un producto con este ID");
		}
		producto.setClasificacion(null);
		productoDao.save(producto);
		precio.desvincularProductoDePrecios(idProducto);
		productoProveedorDao.desvincularProductoDeProveedor(idProducto);
		articuloDao.desvincularProductoDeArticulos(idProducto);
		productoDao.deleteById(idProducto);
	}

	public List<ProductoPrecio> listarProductosBusqueda(String texto) throws Exception {
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		List<ProductoPrecio> productos = new ArrayList<ProductoPrecio>();
		if (!texto.equals("")) {
			List<Producto> productosSinPrecio = productoDao.obtenerProductoFiltro(texto);
			for (Producto producto : productosSinPrecio) {
				Precio precioObject = precio.obtenerPrecio(producto.getId(), usuarioInformacion.getIdOrganizacion().getId())
						.orElse(null);
				Double total = 0.0;
				if (precioObject != null)
					total = precioObject.getPrecioVenta();
				productos.add(new ProductoPrecio(producto, total));
			}
		}
		return productos;
	}

	public List<Producto> listarProductosPorProveedor(Integer proveedor) throws Exception {
		List<Producto> productos = productoDao.obtenerProductoProveedor(proveedor);
		return productos;
	}

	public List<Producto> listarProducto(Integer idSede) {
		List<Producto> lista = productoDao.listarProducto(idSede);
		return lista;
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerProductosClientes(String fechaInicial, String fechaFinal, Integer idCliente,
			Integer page) {
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Pageable paging = PageRequest.of(page, 10);
		try {
			List<Producto> productos;
			List<ProductoCliente> productosCliente = new ArrayList<ProductoCliente>();
			StringBuilder query = new StringBuilder();
			StringBuilder queryCantidad = new StringBuilder();
			generarQuery(query, fechaInicial, fechaFinal, idCliente);

			TypedQuery<Producto> productosInfoQuery = (TypedQuery<Producto>) entityManager.createNativeQuery(query.toString(),
					Producto.class);
			int pageNumber = paging.getPageNumber();
			int pageSize = paging.getPageSize();
			productosInfoQuery.setFirstResult(pageNumber * pageSize);
			productosInfoQuery.setMaxResults(pageSize);
			productos = productosInfoQuery.getResultList();
			generarQueryCantidad(queryCantidad, fechaInicial, fechaFinal, idCliente);

			Query cantidad = entityManager.createNativeQuery(queryCantidad.toString());
			BigInteger countResult = (BigInteger) cantidad.getSingleResult();
			Integer cantidadTotal = countResult.intValue();
			Page<Producto> result = new PageImpl<Producto>(productos, paging, cantidadTotal);
			for (Producto producto : result) {
				Factura factura;
				List<Factura> facturas1;
				StringBuilder queryProductos = new StringBuilder();
				generarQueryFacturas(queryProductos, fechaInicial, fechaFinal, idCliente, producto.getId());

				TypedQuery<Factura> facturaInfoQuery = (TypedQuery<Factura>) entityManager
						.createNativeQuery(queryProductos.toString(), Factura.class);
				factura = facturaInfoQuery.getSingleResult();
				facturas1 = facturaInfoQuery.getResultList();
				// Integer cantidadProductos;
				double cantidadProductos;
				if (!fechaInicial.equals("null") && !fechaFinal.equals(null)) {
					cantidadProductos = productoDao.cantidadProductosAcumuladoClientes(producto.getId(), fechaInicial,
							fechaFinal);
				} else {
					cantidadProductos = facArticuloDao.obtenerCantidadProducto(producto.getId(), factura.getId());
					if (cantidadProductos > 0.0) {
						logger.info(cantidadProductos);
					}

				}
				logger.info("sale del else de cantidad de productos");
				String numeroFactura = factura.getPrefijo().getPrefijo() + factura.getNroFactura();
				ProductoCliente productoCliente = new ProductoCliente(producto.getCodigo(), producto.getProducto(),
						numeroFactura, cantidadProductos, factura.getFechaVenta());
				productosCliente.add(productoCliente);
			}
			Page<ProductoCliente> resultProductos = new PageImpl<ProductoCliente>(productosCliente, paging,
					cantidadTotal);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de productos exitosa");
			respuestaDto.setObjetoRespuesta(resultProductos);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo productos " + e + " linea " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error obteniendo productos");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	private void generarQuery(StringBuilder query, String fechaInicio, String fechaFin, Integer cliente) {

		query.append(
				"select * from producto p join articulo a on p.id = a.id_producto join fac_articulos fa on fa.nid_articulo =a.id join fac_facturas f on f.nid_factura=fa.nid_factura where a.cantidad_disponible > 0 and f.nid_cliente ="
						+ cliente);

		if (!fechaInicio.equals("null") && fechaFin.equals("null")) {
			query.append(" and date_format(date(f.d_fecha_venta),'%Y-%m-%d')" + "= date('" + fechaInicio + "')");
		} else if (fechaInicio.equals("null") && !fechaFin.equals("null")) {
			query.append(" and date_format(date(f.d_fecha_venta),'%Y-%m-%d')" + "<= date('" + fechaFin + "')");
		} else if (!fechaInicio.equals("null") && !fechaFin.equals("null")) {
			query.append(" and date_format(date(f.d_fecha_venta),'%Y-%m-%d')" + "between " + "date('" + fechaInicio
					+ "') and " + "date('" + fechaFin + "')");
		}
		query.append(" group by p.id, f.nid_cliente");
		logger.info(query);
	}

	private void generarQueryFacturas(StringBuilder query, String fechaInicio, String fechaFin, Integer cliente,
			Integer producto) {

		// query.append(
		// "select * from fac_facturas f join fac_articulos fa on f.nid_factura =
		// fa.nid_factura join articulo a on a.id = fa.nid_articulo \r\n"
		// + "where f.nid_cliente =" + cliente + " and a.id_producto =" + producto
		// + " and f.d_fecha_venta = (select max(f.d_fecha_venta) from fac_facturas f
		// join fac_articulos fa on f.nid_factura = fa.nid_factura join articulo a on
		// a.id = fa.nid_articulo \r\n"
		// + "where f.nid_cliente = " + cliente + " and a.id_producto = " + producto);

		query.append(
				"select * from fac_facturas f join fac_articulos fa on f.nid_factura = fa.nid_factura join articulo a on a.id = fa.nid_articulo where a.cantidad_disponible > 0 and f.nid_cliente ="
						+ cliente + " and a.id_producto =" + producto
						+ " and f.nid_factura = (select max(f.nid_factura) from fac_facturas f join fac_articulos fa on f.nid_factura = fa.nid_factura join articulo a on a.id = fa.nid_articulo where a.cantidad_disponible > 0 and f.nid_cliente = "
						+ cliente + " and a.id_producto = " + producto
						+ ") and fa.nid_fac_articulo = (select max(fa.nid_fac_articulo) from fac_facturas f join fac_articulos fa on f.nid_factura = fa.nid_factura join articulo a on a.id = fa.nid_articulo where a.cantidad_disponible > 0 and f.nid_cliente = "
						+ cliente + " and a.id_producto = " + producto);

		if (!fechaInicio.equals("null") && fechaFin.equals("null")) {
			query.append(" and date_format(date(f.d_fecha_venta),'%Y-%m-%d')" + "= date('" + fechaInicio + "')");
		} else if (fechaInicio.equals("null") && !fechaFin.equals("null")) {
			query.append(" and date_format(date(f.d_fecha_venta),'%Y-%m-%d')" + "<= date('" + fechaFin + "')");
		} else if (!fechaInicio.equals("null") && !fechaFin.equals("null")) {
			query.append(" and date_format(date(f.d_fecha_venta),'%Y-%m-%d')" + "between " + "date('" + fechaInicio
					+ "') and " + "date('" + fechaFin + "')");
		}
		query.append(")");
		logger.info(query);
	}

	private void generarQueryCantidad(StringBuilder query, String fechaInicio, String fechaFin, Integer cliente) {

		query.append(
				"select count(*) from(select producto from producto p join articulo a on p.id = a.id_producto join fac_articulos fa on fa.nid_articulo = a.id \r\n"
						+ "join fac_facturas f on f.nid_factura=fa.nid_factura where f.nid_cliente =" + cliente);

		if (!fechaInicio.equals("null") && fechaFin.equals("null")) {
			query.append(" and date_format(date(f.d_fecha_venta),'%Y-%m-%d')" + "= date('" + fechaInicio + "')");
		} else if (fechaInicio.equals("null") && !fechaFin.equals("null")) {
			query.append(" and date_format(date(f.d_fecha_venta),'%Y-%m-%d')" + "<= date('" + fechaFin + "')");
		} else if (!fechaInicio.equals("null") && !fechaFin.equals("null")) {
			query.append(" and date_format(date(f.d_fecha_venta),'%Y-%m-%d')" + "between " + "date('" + fechaInicio
					+ "') and " + "date('" + fechaFin + "')");
		}
		query.append(" group by p.id, f.nid_cliente) as t");
		logger.info(query);
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerProductosClientesExportar(String fechaInicial, String fechaFinal,
			Integer idCliente) {
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		try {
			List<Producto> productos;
			List<ProductoCliente> productosCliente = new ArrayList<ProductoCliente>();
			StringBuilder query = new StringBuilder();
			StringBuilder queryCantidad = new StringBuilder();
			generarQuery(query, fechaInicial, fechaInicial, idCliente);
			TypedQuery<Producto> productosInfoQuery = (TypedQuery<Producto>) entityManager.createNativeQuery(query.toString(),
					Producto.class);
			productos = productosInfoQuery.getResultList();
			for (Producto producto : productos) {
				Factura factura;
				StringBuilder queryProductos = new StringBuilder();
				generarQueryFacturas(queryProductos, fechaInicial, fechaInicial, idCliente, producto.getId());
				TypedQuery<Factura> facturaInfoQuery = (TypedQuery<Factura>) entityManager
						.createNativeQuery(queryProductos.toString(), Factura.class);
				factura = facturaInfoQuery.getSingleResult();
				String numeroFactura = factura.getPrefijo().getPrefijo() + factura.getNroFactura();
				Double cantidadProductos = facArticuloDao.obtenerCantidadProducto(producto.getId(), factura.getId());
				ProductoCliente productoCliente = new ProductoCliente(producto.getCodigo(), producto.getProducto(),
						numeroFactura, cantidadProductos, factura.getFechaVenta());
				productosCliente.add(productoCliente);
			}
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de productos exitosa");
			respuestaDto.setObjetoRespuesta(productosCliente);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo productos " + e + "linea " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error obteniendo productos");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	// Consulta avanzada de descuentos cliente
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerDescuentoClienteConsulta(Integer cliente, Integer page) {
		ResponseEntity<Object> respuesta;
		Pageable paging = PageRequest.of(page, 10);
		try {
			List<Producto> descuento;
			StringBuilder query = new StringBuilder();
			StringBuilder queryCantidad = new StringBuilder();
			generarQueryCliente(query, cliente);
			TypedQuery<Producto> descuentosInfoQuery = (TypedQuery<Producto>) entityManager
					.createNativeQuery(query.toString(), Producto.class);
			int pageNumber = paging.getPageNumber();
			int pageSize = paging.getPageSize();
			descuentosInfoQuery.setFirstResult(pageNumber * pageSize);
			descuentosInfoQuery.setMaxResults(pageSize);
			descuento = descuentosInfoQuery.getResultList();
			generarQueryCantidadCliente(queryCantidad, cliente);
			Query cantidad = entityManager.createNativeQuery(queryCantidad.toString());
			BigInteger countResult = (BigInteger) cantidad.getSingleResult();
			Integer cantidadTotal = countResult.intValue();
			Page<Producto> result = new PageImpl<Producto>(descuento, paging, cantidadTotal);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de descuento exitosa");
			respuestaDto.setObjetoRespuesta(result);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo descuento cliente " + e + " Linea error: "
					+ e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error obteniendo descuento cliente");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	private void generarQueryCliente(StringBuilder query, Integer cliente) {
		query.append(
				"select * from producto pd inner join config_ter_clasificaciones co on co.id  = pd.id_clasificacion inner join clientes cl on cl.id_clasificacion = co.id  where");
		if (cliente != 0) {
			query.append(" cl.id=" + "'" + cliente + "'");
		}

	}

	private void generarQueryCantidadCliente(StringBuilder query, Integer cliente) {
		query.append(
				"SELECT count(*) from prd_descuentos pd inner join config_ter_clasificaciones co on co.id  = pd.id_clasificacion inner join clientes cl on cl.id_clasificacion = co.id  where");
		if (cliente != 0) {
			query.append(" cl.id=" + "'" + cliente + "'");
		}

	}

	// listar todos los productos
	@PreAuthorize("hasAuthority('USER') or hasAuthority('SUPER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> listarProducto() {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		try {

			ArrayList<Producto> producto = this.productoDao.obtenerProductos();
			logger.info("ingresa a listar todos productos");
			if (producto != null) {
				logger.info("existen productos");
				logger.info(producto.size());
			}

			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de productos exitosa");
			respuestaDto.setObjetoRespuesta(producto);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo productos " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error obteniendo usuarios");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerProductosFiltros(String fechaInicial, String fechaFinal, String texto,
			Integer idClasificacion, String requierePrecio, Integer page) {
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Pageable paging = PageRequest.of(page, 10);
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		Integer idsede = usuarioInformacion.getIdOrganizacion().getId();
		try {
			List<Producto> productos;
			List<ProductoResponse> productosResponse = new ArrayList<ProductoResponse>();
			StringBuilder query = new StringBuilder();
			StringBuilder queryCantidad = new StringBuilder();
			generarQueryProductosFiltros(fechaInicial, fechaFinal, texto, idClasificacion, requierePrecio, query);

			TypedQuery<Producto> productosInfoQuery = (TypedQuery<Producto>) entityManager.createNativeQuery(query.toString(),
					Producto.class);
			int pageNumber = paging.getPageNumber();
			int pageSize = paging.getPageSize();
			productosInfoQuery.setFirstResult(pageNumber * pageSize);
			productosInfoQuery.setMaxResults(pageSize);
			productos = productosInfoQuery.getResultList();
			generarQueryCantidadProductosFiltros(fechaInicial, fechaFinal, texto, idClasificacion, requierePrecio,
					queryCantidad);

			Query cantidad = entityManager.createNativeQuery(queryCantidad.toString());
			BigInteger countResult = (BigInteger) cantidad.getSingleResult();
			Integer cantidadTotal = countResult.intValue();
			Page<Producto> result = new PageImpl<Producto>(productos, paging, cantidadTotal);
			for (Producto producto : result.getContent()) {
				List<Precio> precios = precio.listarTodosPorProducto(producto.getId());
				producto.setPrecios(precios);
				ProductoResponse productoResponse = ProductoResponse.convertirProducto(producto);
				Double cantidadArticulos = productoDao.obtenerCantidadArticulos(producto.getId(), idsede);
				productoResponse.setCantidadArticulos(cantidadArticulos);
				productosResponse.add(productoResponse);
			}
			Page<ProductoResponse> resultProductos = new PageImpl<ProductoResponse>(productosResponse, paging,
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

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerProductosFiltrosDisponibles(String fechaInicial, String fechaFinal, String texto,
			Integer idClasificacion, String requierePrecio, Integer page) {
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Pageable paging = PageRequest.of(page, 10);
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		Integer idsede = usuarioInformacion.getIdOrganizacion().getId();
		try {
			List<Producto> productos;
			List<ProductoResponse> productosResponse = new ArrayList<ProductoResponse>();
			StringBuilder query = new StringBuilder();
			StringBuilder queryCantidad = new StringBuilder();
			generarQueryProductosFiltrosDisponibles(fechaInicial, fechaFinal, texto, idClasificacion, requierePrecio, query,
					idsede);

			TypedQuery<Producto> productosInfoQuery = (TypedQuery<Producto>) entityManager.createNativeQuery(query.toString(),
					Producto.class);
			int pageNumber = paging.getPageNumber();
			int pageSize = paging.getPageSize();
			productosInfoQuery.setFirstResult(pageNumber * pageSize);
			productosInfoQuery.setMaxResults(pageSize);
			productos = productosInfoQuery.getResultList();
			generarQueryCantidadProductosFiltrosDisponibles(fechaInicial, fechaFinal, texto, idClasificacion, requierePrecio,
					queryCantidad, idsede);

			Query cantidad = entityManager.createNativeQuery(queryCantidad.toString());
			BigInteger countResult = (BigInteger) cantidad.getSingleResult();
			Integer cantidadTotal = countResult.intValue();
			Page<Producto> result = new PageImpl<Producto>(productos, paging, cantidadTotal);
			for (Producto producto : result.getContent()) {
				List<Precio> precios = precio.listarTodosPorProducto(producto.getId());
				producto.setPrecios(precios);
				ProductoResponse productoResponse = ProductoResponse.convertirProducto(producto);
				Double cantidadArticulos = productoDao.obtenerCantidadArticulos(producto.getId(), idsede);
				productoResponse.setCantidadArticulos(cantidadArticulos);
				productosResponse.add(productoResponse);
			}
			Page<ProductoResponse> resultProductos = new PageImpl<ProductoResponse>(productosResponse, paging,
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

	// consulta avanzada de productos exitentes
	public List<Producto> listarproductosExistentesFiltros(int producto, int clasificacion) {
		List<Producto> productos;
		StringBuilder query = new StringBuilder();
		generarQueryProductosExistentes(query, producto, clasificacion);
		TypedQuery<Producto> productosexistentesInfoQuery = (TypedQuery<Producto>) entityManager
				.createNativeQuery(query.toString(), Producto.class);
		productos = productosexistentesInfoQuery.getResultList();
		return productos;
	}

	private void generarQueryProductosExistentes(StringBuilder query, int producto, int clasificacion) {

		query.append(
				"select distinct p.* from articulo a inner join producto p ON a.id_producto = p.id where  a.cantidad_disponible > 0");

		if (producto != 0) {
			query.append(" and a.id_producto=" + producto);
		}

		if (clasificacion != 0 && producto != 0) {
			query.append(" and p.id_clasificacion=" + clasificacion);
		}

		else if (clasificacion != 0) {
			query.append(" and p.id_clasificacion=" + clasificacion);
		}
		query.append(" order by p.id desc");
	}

	public void generarQueryProductosFiltros(String fechaInicial, String fechaFinal, String texto,
			Integer idClasificacion, String requierePrecio, StringBuilder query) {
		query.append(
				"select * from producto m inner join (select CONCAT(pt.t_tipo, ' ', pr.t_referencia,' ', pp.t_presentacion, ' ', pc.t_color) productox, p.* from producto p inner join prd_colores pc inner join prd_tipos pt inner join prd_referencia pr inner join prd_presentacion pp on p.id_color = pc.id and p.tipo = pt.id and p.referencia = pr.id and p.id_presentacion = pp.id)n on m.id = n.id ");
		if (!fechaInicial.equals("null") && fechaFinal.equals("null")) {
			query.append("where date_format(date(m.fecha_modificacion),'%Y-%m-%d')" + "= date('" + fechaInicial + "')");
		} else if (fechaInicial.equals("null") && !fechaFinal.equals("null")) {
			query.append("where date_format(date(m.fecha_modificacion),'%Y-%m-%d')" + "<= date('" + fechaFinal + "')");
		} else if (!fechaInicial.equals("null") && !fechaFinal.equals("null")) {
			query.append("where date_format(date m.fecha_modificacion),'%Y-%m-%d')" + "between " + "date('"
					+ fechaInicial + "') and " + "date('" + fechaFinal + "')");
		}
		if (!texto.equals("null") && (!fechaInicial.equals("null") || !fechaFinal.equals("null"))) {
			query.append(" and m.codigo like '%" + texto + "%' or n.productox like '%" + texto + "%'");
		} else if (!texto.equals("null")) {

			query.append("where m.codigo like '%" + texto + "%' or n.productox like '%" + texto + "%'");
		}
		if (idClasificacion != 0
				&& (!fechaInicial.equals("null") || !fechaFinal.equals("null") || !texto.equals("null"))) {
			query.append(" and m.id_clasificacion = " + idClasificacion);
		} else if (idClasificacion != 0) {
			query.append("where m.id_clasificacion = " + idClasificacion);
		}
		if (!requierePrecio.equals("null")) {
			query.append(" and m.requiere_precio = " + requierePrecio);
		}
		query.append(" group by m.id");
		logger.info(query);
	}

	public void generarQueryProductosFiltrosDisponibles(String fechaInicial, String fechaFinal, String texto,
			Integer idClasificacion, String requierePrecio, StringBuilder query, Integer idsede) {
		query.append(
				"select * from producto m inner join articulo a inner join (select CONCAT(pt.t_tipo, ' ', pr.t_referencia,' ', pp.t_presentacion, ' ', pc.t_color) productox, p.* from producto p inner join prd_colores pc inner join prd_tipos pt inner join prd_referencia pr inner join prd_presentacion pp on p.id_color = pc.id and p.tipo = pt.id and p.referencia = pr.id and p.id_presentacion = pp.id)n on m.id = n.id and m.id = a.id_producto and a.id_sede="
						+ idsede + " ");
		if (!fechaInicial.equals("null") && fechaFinal.equals("null")) {
			query.append("where date_format(date(m.fecha_modificacion),'%Y-%m-%d')" + "= date('" + fechaInicial + "')");
		} else if (fechaInicial.equals("null") && !fechaFinal.equals("null")) {
			query.append("where date_format(date(m.fecha_modificacion),'%Y-%m-%d')" + "<= date('" + fechaFinal + "')");
		} else if (!fechaInicial.equals("null") && !fechaFinal.equals("null")) {
			query.append("where date_format(date m.fecha_modificacion),'%Y-%m-%d')" + "between " + "date('"
					+ fechaInicial + "') and " + "date('" + fechaFinal + "')");
		}
		if (!texto.equals("null") && (!fechaInicial.equals("null") || !fechaFinal.equals("null"))) {
			query.append(" and m.codigo like '%" + texto + "%' or n.productox like '%" + texto + "%'");
		} else if (!texto.equals("null")) {

			query.append("where m.codigo like '%" + texto + "%' or n.productox like '%" + texto + "%'");
		}
		if (idClasificacion != 0
				&& (!fechaInicial.equals("null") || !fechaFinal.equals("null") || !texto.equals("null"))) {
			query.append(" and m.id_clasificacion = " + idClasificacion);
		} else if (idClasificacion != 0) {
			query.append("where m.id_clasificacion = " + idClasificacion);
		}
		if (!requierePrecio.equals("null")) {
			query.append(" and m.requiere_precio = " + requierePrecio);
		}
		query.append(" group by m.id order by m.id asc");
		logger.info(query);
	}

	public void generarQueryCantidadProductosFiltros(String fechaInicial, String fechaFinal, String texto,
			Integer idClasificacion, String requierePrecio, StringBuilder query) {

		query.append(
				"select count(*) from producto m inner join (select CONCAT(pt.t_tipo, ' ', pr.t_referencia,' ', pp.t_presentacion, ' ', pc.t_color) productox, p.* from producto p inner join prd_colores pc inner join prd_tipos pt inner join prd_referencia pr inner join prd_presentacion pp on p.id_color = pc.id and p.tipo = pt.id and p.referencia = pr.id and p.id_presentacion = pp.id)n on m.id = n.id ");
		if (!fechaInicial.equals("null") && fechaFinal.equals("null")) {
			query.append("where date_format(date(m.fecha_modificacion),'%Y-%m-%d')" + "= date('" + fechaInicial + "')");
		} else if (fechaInicial.equals("null") && !fechaFinal.equals("null")) {
			query.append("where date_format(date(m.fecha_modificacion),'%Y-%m-%d')" + "<= date('" + fechaFinal + "')");
		} else if (!fechaInicial.equals("null") && !fechaFinal.equals("null")) {
			query.append("where date_format(date m.fecha_modificacion),'%Y-%m-%d')" + "between " + "date('"
					+ fechaInicial + "') and " + "date('" + fechaFinal + "')");
		}
		if (!texto.equals("null") && (!fechaInicial.equals("null") || !fechaFinal.equals("null"))) {
			query.append(" and m.codigo like '%" + texto + "%' or n.productox like '%" + texto + "%'");
		} else if (!texto.equals("null")) {

			query.append("where m.codigo like '%" + texto + "%' or n.productox like '%" + texto + "%'");
		}
		if (idClasificacion != 0
				&& (!fechaInicial.equals("null") || !fechaFinal.equals("null") || !texto.equals("null"))) {
			query.append(" and m.id_clasificacion = " + idClasificacion);
		} else if (idClasificacion != 0) {
			query.append("where m.id_clasificacion = " + idClasificacion);
		}
		if (!requierePrecio.equals("null")) {
			query.append(" and m.requiere_precio = " + requierePrecio);
		}
		// query.append(" group by p.id");
	}

	public void generarQueryCantidadProductosFiltrosDisponibles(String fechaInicial, String fechaFinal, String texto,
			Integer idClasificacion, String requierePrecio, StringBuilder query, Integer idSede) {

		query.append(
				"select count(*) from producto m inner join articulo a inner join (select CONCAT(pt.t_tipo, ' ', pr.t_referencia,' ', pp.t_presentacion, ' ', pc.t_color) productox, p.* from producto p inner join prd_colores pc inner join prd_tipos pt inner join prd_referencia pr inner join prd_presentacion pp on p.id_color = pc.id and p.tipo = pt.id and p.referencia = pr.id and p.id_presentacion = pp.id)n on m.id = n.id and m.id = a.id_producto and a.id_sede="
						+ idSede + " ");
		if (!fechaInicial.equals("null") && fechaFinal.equals("null")) {
			query.append("where date_format(date(m.fecha_modificacion),'%Y-%m-%d')" + "= date('" + fechaInicial + "')");
		} else if (fechaInicial.equals("null") && !fechaFinal.equals("null")) {
			query.append("where date_format(date(m.fecha_modificacion),'%Y-%m-%d')" + "<= date('" + fechaFinal + "')");
		} else if (!fechaInicial.equals("null") && !fechaFinal.equals("null")) {
			query.append("where date_format(date m.fecha_modificacion),'%Y-%m-%d')" + "between " + "date('"
					+ fechaInicial + "') and " + "date('" + fechaFinal + "')");
		}
		if (!texto.equals("null") && (!fechaInicial.equals("null") || !fechaFinal.equals("null"))) {
			query.append(" and m.codigo like '%" + texto + "%' or n.productox like '%" + texto + "%'");
		} else if (!texto.equals("null")) {

			query.append("where m.codigo like '%" + texto + "%' or n.productox like '%" + texto + "%'");
		}
		if (idClasificacion != 0
				&& (!fechaInicial.equals("null") || !fechaFinal.equals("null") || !texto.equals("null"))) {
			query.append(" and m.id_clasificacion = " + idClasificacion);
		} else if (idClasificacion != 0) {
			query.append("where m.id_clasificacion = " + idClasificacion);
		}
		if (!requierePrecio.equals("null")) {
			query.append(" and m.requiere_precio = " + requierePrecio);
		}
		query.append(" order by m.id asc");
		logger.info(query);
	}

	public ResponseEntity<Object> replicarProductos(List<Producto> productos) throws Exception {
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		int cantidadReplicados = 0;
		try {
			for (Producto producto : productos) {
				if (productoDao.findByCodigo(producto.getCodigo()).orElse(null) == null) {
					Producto productoSave = productoDao.save(producto);
					List<Precio> precios = producto.getPrecios();
					for (Precio precioSave : precios) {
						precioSave.setProducto(productoSave);
					}
					precio.saveAll(precios);
					cantidadReplicados++;
				}
			}
			if (cantidadReplicados == 0) {
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "no_replicado");
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
			} else {
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "replicacion de productos exitosa");
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
			}
		} catch (Exception e) {
			logger.error("Error replicando productos " + e + "linea " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error replicando productos");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	// Listar todos los productos por paginado
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerTodosProducto(Integer page, Integer idProducto) {
		logger.info(idProducto);
		ResponseEntity<Object> respuesta;
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());

		int idSede = usuarioInformacion.getIdOrganizacion().getId();
		try {
			Pageable pageConfig = PageRequest.of(page, 10);
			Page<Producto> productos = productoDao.obtenerTodosProducto(pageConfig, idProducto);
			// List<Producto> productos = productoDao.obtenerProductoId(idProducto);
			// for (Producto producto : productos) {
			// if (producto != null) {
			// logger.info("hay producto" + producto.getId());
			// } else {
			// logger.info("no hay producto");
			// }
			// }
			// }
			// logger.info(productos);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			// for (Producto producto : productos) {
			// // List<Precio> precios = precio.findAllByPrecioProducto(producto.getId(),
			// // idSede);
			// // producto.setPrecios(precios);
			// }
			/*
			 * for (Producto producto : productos) { List<Articulo> articulos =
			 * articuloDao.findAllByArticuloProducto(producto.getId());
			 * producto.setArticulos(articulos); }
			 */
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de los producto por id exitoso");
			// logger.info(productos.get(0).getId());
			// logger.info(productos);
			respuestaDto.setObjetoRespuesta(productos);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error("Error al listar los productos por id" + e.getMessage());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"No se pudo obtener los productos por id");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	// Listar todos los productos por paginado
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerTodosProductoPage(Integer page) {
		// logger.info(idProducto);
		ResponseEntity<Object> respuesta;
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());

		int idSede = usuarioInformacion.getIdOrganizacion().getId();
		try {
			Pageable pageConfig = PageRequest.of(page, 10);
			Page<Producto> productos = productoDao.obtenerTodosProductoPage(pageConfig);
			// List<Producto> productos = productoDao.obtenerProductoId(idProducto);
			// for (Producto producto : productos) {
			// if (producto != null) {
			// logger.info("hay producto" + producto.getId());
			// } else {
			// logger.info("no hay producto");
			// }
			// }
			// }
			// logger.info(productos);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			// for (Producto producto : productos) {
			// // List<Precio> precios = precio.findAllByPrecioProducto(producto.getId(),
			// // idSede);
			// // producto.setPrecios(precios);
			// }
			/*
			 * for (Producto producto : productos) { List<Articulo> articulos =
			 * articuloDao.findAllByArticuloProducto(producto.getId());
			 * producto.setArticulos(articulos); }
			 */
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de los producto por id exitoso");
			// logger.info(productos.get(0).getId());
			// logger.info(productos);
			respuestaDto.setObjetoRespuesta(productos);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error("Error al listar los productos por id" + e.getMessage());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"No se pudo obtener los productos por id");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	// Listar todos los productos por paginado
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerTodosProductoPageDisponibles(Integer page) {
		// logger.info(idProducto);
		ResponseEntity<Object> respuesta;
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());

		int idSede = usuarioInformacion.getIdOrganizacion().getId();
		try {
			Pageable pageConfig = PageRequest.of(page, 10);
			Page<Producto> productos = productoDao.obtenerTodosProductoPageDisponibles(pageConfig);
			// List<Producto> productos = productoDao.obtenerProductoId(idProducto);
			// for (Producto producto : productos) {
			// if (producto != null) {
			// logger.info("hay producto" + producto.getId());
			// } else {
			// logger.info("no hay producto");
			// }
			// }
			// }
			// logger.info(productos);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			// for (Producto producto : productos) {
			// // List<Precio> precios = precio.findAllByPrecioProducto(producto.getId(),
			// // idSede);
			// // producto.setPrecios(precios);
			// }
			/*
			 * for (Producto producto : productos) { List<Articulo> articulos =
			 * articuloDao.findAllByArticuloProducto(producto.getId());
			 * producto.setArticulos(articulos); }
			 */
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de los producto por id exitoso");
			// logger.info(productos.get(0).getId());
			// logger.info(productos);
			respuestaDto.setObjetoRespuesta(productos);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error("Error al listar los productos por id" + e.getMessage());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"No se pudo obtener los productos por id");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	// // Listar todos los productos por paginado recodificar
	// @PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or
	// hasAuthority('USER') or hasAuthority('VENDEDOR')")

	// public ResponseEntity<Object> obtenerTodosProductoRecodificar(Integer page,
	// Integer tipo, Integer referencia,
	// Integer presentacion, Integer color) {
	// // logger.info(idProducto);
	// // logger.info(idProducto.getTipo().getTTipo());
	// ResponseEntity<Object> respuesta;
	// Authentication autenticacion =
	// SecurityContextHolder.getContext().getAuthentication();
	// Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
	// InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
	// .buscarPorIdUsuario(usuarioAutenticado.getId());

	// int idSede = usuarioInformacion.getIdOrganizacion().getId();
	// try {
	// Pageable pageConfig = PageRequest.of(page, 10);
	// // Page<Producto> productos =
	// // productoDao.obtenerTodosProductoRecodificar(pageConfig, idProducto);
	// logger.info(tipo + " " + referencia + " " + presentacion + " " + color);
	// Page<Producto> productos =
	// productoDao.obtenerTodosProductoRecodificar(pageConfig, tipo, referencia,
	// presentacion,
	// color);
	// // List<Producto> productos = productoDao.obtenerProductoId(idProducto);
	// // for (Producto producto : productos) {
	// // if (producto != null) {
	// // logger.info("hay producto" + producto.getId());
	// // } else {
	// // logger.info("no hay producto");
	// // }
	// // }
	// // }
	// // logger.info(productos);
	// respuesta = ResponseEntity.ok(HttpStatus.OK);
	// // for (Producto producto : productos) {
	// // // List<Precio> precios = precio.findAllByPrecioProducto(producto.getId(),
	// // // idSede);
	// // // producto.setPrecios(precios);
	// // }
	// /*
	// * for (Producto producto : productos) { List<Articulo> articulos =
	// * articuloDao.findAllByArticuloProducto(producto.getId());
	// * producto.setArticulos(articulos); }
	// */
	// RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de los
	// producto por id exitoso");
	// // logger.info(productos.get(0).getId());
	// // logger.info(productos);
	// respuestaDto.setObjetoRespuesta(productos);
	// respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
	// } catch (Exception e) {
	// respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
	// logger.error("Error al listar los productos por id" + e.getMessage());
	// RespuestaDto respuestaDto = new
	// RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
	// "No se pudo obtener los productos por id");
	// respuesta = new ResponseEntity<>(respuestaDto,
	// HttpStatus.INTERNAL_SERVER_ERROR);
	// }
	// return respuesta;
	// }

	// // Listar todos los productos por paginado recodificar
	// @PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or
	// hasAuthority('USER') or hasAuthority('VENDEDOR')")

	// public ResponseEntity<Object> obtenerTodosProductoRecodificarObtener(Integer
	// page, Integer tipo, Integer referencia,
	// Integer presentacion, Integer color) {
	// // logger.info(idProducto);
	// // logger.info(idProducto.getTipo().getTTipo());
	// ResponseEntity<Object> respuesta;
	// Authentication autenticacion =
	// SecurityContextHolder.getContext().getAuthentication();
	// Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
	// InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
	// .buscarPorIdUsuario(usuarioAutenticado.getId());

	// int idSede = usuarioInformacion.getIdOrganizacion().getId();
	// try {
	// Pageable pageConfig = PageRequest.of(page, 10);
	// // Page<Producto> productos =
	// // productoDao.obtenerTodosProductoRecodificar(pageConfig, idProducto);
	// logger.info(tipo + " " + referencia + " " + presentacion + " " + color);
	// Page<Producto> productos =
	// productoDao.obtenerTodosProductoRecodificar(pageConfig, tipo, referencia,
	// presentacion,
	// color);
	// // List<Producto> productos = productoDao.obtenerProductoId(idProducto);
	// // for (Producto producto : productos) {
	// // if (producto != null) {
	// // logger.info("hay producto" + producto.getId());
	// // } else {
	// // logger.info("no hay producto");
	// // }
	// // }
	// // }
	// // logger.info(productos);
	// respuesta = ResponseEntity.ok(HttpStatus.OK);
	// // for (Producto producto : productos) {
	// // // List<Precio> precios = precio.findAllByPrecioProducto(producto.getId(),
	// // // idSede);
	// // // producto.setPrecios(precios);
	// // }
	// /*
	// * for (Producto producto : productos) { List<Articulo> articulos =
	// * articuloDao.findAllByArticuloProducto(producto.getId());
	// * producto.setArticulos(articulos); }
	// */
	// RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de los
	// producto por id exitoso");
	// // logger.info(productos.get(0).getId());
	// // logger.info(productos);
	// respuestaDto.setObjetoRespuesta(productos);
	// respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
	// } catch (Exception e) {
	// respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
	// logger.error("Error al listar los productos por id" + e.getMessage());
	// RespuestaDto respuestaDto = new
	// RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
	// "No se pudo obtener los productos por id");
	// respuesta = new ResponseEntity<>(respuestaDto,
	// HttpStatus.INTERNAL_SERVER_ERROR);
	// }
	// return respuesta;
	// }

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> ActualizarPrecios(@RequestBody Producto producto) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());

		try {
			for (Precio precio : producto.getPrecios()) {
				precio.setSede(usuarioInformacion.getIdOrganizacion());
			}
			Producto guardado = this.productoDao.save(producto);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito actualizando el precio del producto");
			respuestaDto.setObjetoRespuesta(guardado);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);

		} catch (Exception e) {
			logger.error("Error en la actualizacion del precio del producto" + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error en la actualizacion del precio del producto " + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return respuesta;
	}

	// Listar todos los productos por id
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerProductoId(Integer idProducto) {
		ResponseEntity<Object> respuesta;
		try {

			List<Producto> productos = this.productoDao.obtenerProductoId(idProducto);
			respuesta = ResponseEntity.ok(HttpStatus.OK);

			/*
			 * for (Producto producto : productos) { List<Precio> precios =
			 * precio.findAllByPrecioProducto(producto.getId());
			 * producto.setPrecios(precios); }
			 */
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion del producto por id exitoso");
			respuestaDto.setObjetoRespuesta(productos);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error("Error al listar el producto por id" + e.getMessage());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"No se pudo obtener el producto por id");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	// productos existentes por id producto
	public List<Producto> listarProductosExistentes(Integer idProducto) {
		List<Producto> lista = productoDao.listarProductosExistentes(idProducto);
		return lista;
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerProductosFiltrosExcel(String fechaInicial, String fechaFinal, String texto,
			Integer idClasificacion, String requierePrecio) {
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		Integer idSede = usuarioInformacion.getIdOrganizacion().getId();
		try {
			List<Producto> productos;
			List<ProductoResponse> productosResponse = new ArrayList<ProductoResponse>();
			StringBuilder query = new StringBuilder();
			generarQueryProductosFiltros(fechaInicial, fechaFinal, texto,
					idClasificacion, requierePrecio, query);
			TypedQuery<Producto> productosInfoQuery = (TypedQuery<Producto>) entityManager.createNativeQuery(query.toString(),
					Producto.class);
			productos = productosInfoQuery.getResultList();
			for (Producto producto : productos) {
				ProductoResponse productoResponse = ProductoResponse.convertirProducto(producto);
				Double cantidadArticulos = productoDao.obtenerCantidadArticulos(producto.getId(), idSede);
				productoResponse.setCantidadArticulos(cantidadArticulos);
				productosResponse.add(productoResponse);
			}
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de productos exitosa");
			respuestaDto.setObjetoRespuesta(productosResponse);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo productos " + e + "linea " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error obteniendo productos");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

}
