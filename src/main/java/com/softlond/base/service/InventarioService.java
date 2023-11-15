package com.softlond.base.service;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import com.softlond.base.entity.EstadoArticulo;
import com.softlond.base.entity.FacturaArticulos;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
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
import com.softlond.base.entity.ArticuloMovimientos;
import com.softlond.base.entity.DevolucionArticulosCompra;
import com.softlond.base.entity.DevolucionArticulosVenta;
import com.softlond.base.entity.DevolucionCompras;
import com.softlond.base.entity.DevolucionVentasCliente;
import com.softlond.base.entity.DocumentoMovimiento;
import com.softlond.base.entity.Entrada;
import com.softlond.base.entity.EntradaArticulos;
import com.softlond.base.entity.InformacionUsuario;
import com.softlond.base.entity.Inventario;
import com.softlond.base.entity.Organizacion;
import com.softlond.base.entity.Producto;
import com.softlond.base.entity.ProductoProveedor;
import com.softlond.base.entity.Proveedor;
import com.softlond.base.entity.RemisionCompra;
import com.softlond.base.entity.RemisionVenta;
import com.softlond.base.entity.RowInventario;
import com.softlond.base.entity.SalidaArticulos;
import com.softlond.base.entity.SalidaMercancia;
import com.softlond.base.entity.Traslado;
import com.softlond.base.entity.Usuario;
import com.softlond.base.repository.ArticuloDao;
import com.softlond.base.repository.PrecioDao;
import com.softlond.base.repository.ArticuloMovimientoDao;
import com.softlond.base.repository.ArticulosRemisionCompraDao;
import com.softlond.base.repository.DevolucionClienteDao;
import com.softlond.base.repository.DevolucionComprasDao;
import com.softlond.base.repository.EntradaDao;
import com.softlond.base.repository.EstadoArticuloDao;
import com.softlond.base.repository.InventarioDao;
import com.softlond.base.repository.ProductoDao;
import com.softlond.base.repository.ProductoProveedorDao;
import com.softlond.base.repository.RemisionCompraDao;
import com.softlond.base.repository.RemisionVentaDao;
import com.softlond.base.repository.RowInventarioDao;
import com.softlond.base.repository.SalidaMercanciaDao;
import com.softlond.base.repository.TrasladosDao;
import com.softlond.base.repository.UsuarioInformacionDao;
import com.softlond.base.response.ArticuloInventario;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Service
public class InventarioService {

	private final Logger logger = Logger.getLogger(getClass());

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private ArticuloDao articuloDao;
	@Autowired
	private PrecioDao PrecioDao;

	@Autowired
	private ProductoDao productoDao;

	@Autowired
	private RemisionVentaDao remisionVentaDao;

	@Autowired
	private ArticulosRemisionCompraDao ArticulosRemisionCompraDao;

	@Autowired
	private DevolucionClienteDao devolucionClienteDao;

	@Autowired
	private ArticuloMovimientoDao articuloMovimientoDao;

	@Autowired
	private SalidaMercanciaDao SalidaMercanciaDao;

	@Autowired
	private EntradaDao EntradaDao;

	@Autowired
	private TrasladosDao TrasladosDao;

	@Autowired
	private EstadoArticuloDao estadoArticuloDao;

	@Autowired
	private ProductoProveedorDao productoProveedorDao;

	@Autowired
	UsuarioInformacionDao usuarioInformacionDao;

	@Autowired
	RemisionCompraDao remisionCompraDao;

	@Autowired
	DevolucionComprasDao devolucionCompraDao;

	@Autowired
	InventarioDao inventarioDao;

	@Autowired
	RowInventarioDao rowInventarioDao;

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerArticuloCodigo(String codigo) {
		ResponseEntity<Object> respuesta;
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		int idSede = usuarioInformacion.getIdOrganizacion().getId();
		try {
			Articulo articulo = articuloDao.obtenerCodigoSede(codigo, idSede);
			if (articulo != null) {
				RemisionCompra remision = remisionCompraDao.buscarPorArticulo(articulo.getId());
				Proveedor proveedor = null;
				if (remision != null) {
					proveedor = remision.getIdProveedor();
				}
				ArticuloInventario articuloInventario = ArticuloInventario.crearArticuloInventario(articulo, proveedor,
						remision);
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de articulo exitosa");
				respuestaDto.setObjetoRespuesta(articuloInventario);
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
			} else {
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK,
						"No existe el codigo de articulo en esta sede");
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
			}
		} catch (Exception e) {
			logger.error("Error al obtener articulo" + e + " " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al obtener articulo " + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	/**
	 * @param idArticulo
	 * @return
	 */
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerArticulosInformeInventario(String idArticulo, String tipoDocumento) {
		logger.info(tipoDocumento);
		logger.info(idArticulo);
		ResponseEntity<Object> respuesta;
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		int idSede = usuarioInformacion.getIdOrganizacion().getId();
		try {
			// ArticuloMovimientos articuloIn = new ArticuloMovimientos();
			StringBuilder query = new StringBuilder();
			StringBuilder queryCantidad = new StringBuilder();
			String articulo = null;
			String codigoArticulo = idArticulo;
			// String codigoArticulo = "0322004276";
			logger.info(codigoArticulo);
			Integer idenArticulo = 0;
			String nombreArticulo = "";
			List<DocumentoMovimiento> lDocumentoMovimiento = new ArrayList<DocumentoMovimiento>();

			List<Articulo> articuloM = articuloDao.buscarPorCodigo(codigoArticulo, idSede);
			logger.info(articuloM.size());
			Producto productoArticulo = productoDao.obteneArticulo(articuloM.get(0).getId());
			nombreArticulo = productoArticulo.getTipo().getTTipo() + " " + productoArticulo.getReferencia().getTreferencia()
					+ " " + productoArticulo.getPresentacion().getTPresentacion() + " " + productoArticulo.getColor().getTColor();
			logger.info(nombreArticulo);
			if (articuloM != null) {
				logger.info(articuloM.size());
				idenArticulo = articuloM.get(0).getId();
				logger.info(idenArticulo);
			}
			if (tipoDocumento.equals("0") || tipoDocumento.equals("1")) {
				// ! remisiones de compra del articulo
				List<RemisionCompra> remisionCompra = new ArrayList<>();

				// List<RemisionCompra> remisionCompra =
				// remisionCompraDao.buscarPorArticuloLista(idenArticulo);
				List<Articulo> articulosPorProductoSede = articuloDao.buscarPorProductoSede(productoArticulo.getId(), idSede);
				logger.info(articulosPorProductoSede.size());
				for (Articulo articuloRC : articulosPorProductoSede) {
					logger.info(articuloRC.getId());
					if (remisionCompraDao.buscarPorArticulo(articuloRC.getId()) != null) {
						// logger.info(articuloRC);
						logger.info(articuloRC.getId());
						remisionCompra.add(remisionCompraDao.buscarPorArticulo(articuloRC.getId()));
					}
				}
				if (remisionCompra != null) {
					logger.info(remisionCompra.size());
					if (remisionCompra.size() > 0) {
						Integer cantidadRemision;
						for (RemisionCompra remision : remisionCompra) {
							for (Articulo articuloE : articulosPorProductoSede) {
								cantidadRemision = ArticulosRemisionCompraDao.buscarPorIdArticuloRemision(remision.getId(),
										articuloE.getId());
								logger.info(cantidadRemision);
								if (cantidadRemision > 0) {
									logger.info(remisionCompra.get(0).getId());
									DocumentoMovimiento documentoRC = new DocumentoMovimiento();
									documentoRC.setTipoDocumento("remision de compra");
									documentoRC.setCodArticulo(nombreArticulo);
									documentoRC.setUbInicial("NA");
									documentoRC.setUbFinal("NA");
									documentoRC.setCliente(
											remision.getIdProveedor() != null ? remision.getIdProveedor().getProveedor()
													: "");
									if (remision.getFechaRemision() != null) {
										// String patronEncontrado = "dd/MM/yyyy hh:mm:ss a";
										String datetimeString = remision.getFechaRemision().toLocaleString().toString();
										logger.info(datetimeString);
										// SimpleDateFormat datetimeFormat = new SimpleDateFormat();
										// logger.info(datetimeFormat.toPattern());
										// Date datetime =
										// datetimeFormat.format(remisionCompra.get(0).getFechaRemision());
										// logger.info(datetime);
										// SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
										// String fecha = "dd/MM/yyyy hh:mm:ss a";
										// String[] patrones = {
										// "dd/MM/yyyy",
										// "yyyy/MM/dd",
										// "dd-MM-yyyy",
										// "yyyy-MM-dd",
										// // Agrega aquí más patrones según tus necesidades
										// };

										// for (String patron : patrones) {
										// try {
										// SimpleDateFormat sdf = new SimpleDateFormat(patron);
										// Date date = sdf.parse(datetimeString);
										// if (sdf.format(date).equals(datetimeString)) {
										// patronEncontrado = patron;
										// }
										// } catch (Exception e) {
										// // La fecha no coincide con el patrón, continuar con el siguiente
										// }
										// }

										// SimpleDateFormat datetimeFormat = new SimpleDateFormat(patronEncontrado);
										// logger.info(datetimeFormat.toPattern());
										// Date datetime = datetimeFormat.parse(datetimeString);

										SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

										// String dateString = dateFormat.format(datetime);
										String dateString = dateFormat.format(remision.getFechaRemision());
										logger.info(dateString);
										documentoRC.setFechaCreacion(dateString);
									}
									documentoRC.setIdDocumento(
											remision.getNumeroRemision() != null ? remision.getNumeroRemision() : "");
									// ArticulosRemisionCompraDao.findByIdRemisionCommpra(remision.getId());
									// documentoRC.setCantidad(articuloM.get(0).getCantidadCompra().toString());
									documentoRC.setCantidad(articuloE.getCantidadCompra().toString());
									// documentoRC.setCantidad(remision.getCantidadArticulos().toString());
									logger.info((articuloE.getIvaCosto() + 1));
									documentoRC
											.setCosto(((articuloE.getCantidadCompra() * articuloE.getPrecioCosto().floatValue())
													/ (((articuloE.getIvaCosto()) / 100) + 1)) + "");
									documentoRC
											.setTotal(
													(articuloE.getCantidadCompra() * articuloE.getPrecioCosto().floatValue()) + "");
									// documentoRC.setTotal(remision.getTotal().toString());
									// documentoRC.setObservacion(remisionCompra.get(0).getObservaciones() != null ?
									// remisionCompra.get(0).getObservaciones() : "");
									documentoRC.setUsuarioCreador(
											remision.getIdCreador() != null ? remision.getIdCreador().getNombreUsuario()
													: "");
									documentoRC.setEstadoDocumento(
											remision.getEstadoDocumento() != null ? remision.getEstadoDocumento() : "");
									lDocumentoMovimiento.add(documentoRC);
								}
							}
						}
					}
				}
			}
			if (tipoDocumento.equals("0") || tipoDocumento.equals("2")) {
				// ! remisiones de venta del articulo
				List<RemisionVenta> remisionVenta = remisionVentaDao.buscarRemisionArticulo(idenArticulo);
				if (remisionVenta != null) {
					logger.info(remisionVenta.size());
					if (remisionVenta.size() > 0) {
						logger.info(remisionVenta.get(0).getId());
						for (int index = 0; index < remisionVenta.size(); index++) {
							DocumentoMovimiento documentoRV = new DocumentoMovimiento();
							documentoRV.setTipoDocumento("remision de venta");
							documentoRV.setCodArticulo(nombreArticulo);
							documentoRV.setUbInicial("NA");
							documentoRV.setUbFinal("NA");
							documentoRV.setCliente(
									remisionVenta.get(index).getIdCliente() != null ? remisionVenta.get(index).getIdCliente().getNombres()
											: "");

							if (remisionVenta.get(index).getFecha() != null) {
								String datetimeString = remisionVenta.get(index).getFecha().toLocaleString().toString();
								// // SimpleDateFormat datetimeFormat = new SimpleDateFormat("dd/MM/yyyy
								// hh:mm:ss
								// // a");
								// SimpleDateFormat datetimeF = new SimpleDateFormat();
								// // String dateString = dateFormat.format(datetime);
								// SimpleDateFormat datetimeFormat = new
								// SimpleDateFormat(datetimeF.toPattern());
								// logger.info(datetimeFormat.toPattern());
								// Date datetime = datetimeFormat.parse(datetimeString);

								SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

								String dateString = dateFormat.format(remisionVenta.get(index).getFecha());
								// logger.info(dateString);
								documentoRV.setFechaCreacion(dateString);
							}
							documentoRV.setIdDocumento(remisionVenta.get(index).getNumeroRemision());
							List<FacturaArticulos> articulosRemision = remisionVenta.get(index).getFacArticulos();
							float cantidad;
							float costo;
							Float subtotal;
							Float total;
							for (FacturaArticulos articuloF : articulosRemision) {
								if (articuloF.getArticulo().getId() == idenArticulo) {
									documentoRV.setCantidad(articuloF.getCantidad().toString());
									cantidad = articuloF.getCantidad().floatValue();
									costo = articuloF.getPrecioUnitario().floatValue();
									total = cantidad * costo;
									logger.info(articuloF.getPorcentajeIva() + 1);
									subtotal = total / (((articuloF.getPorcentajeIva()) / 100) + 1);
									documentoRV.setCosto(subtotal.toString());
									documentoRV.setTotal(total.toString());
								} else {
									documentoRV.setCantidad("0");
									documentoRV.setCosto("0");
									documentoRV.setTotal("0");
								}
							}
							// documentoRV.setCosto(remisionVenta.get(index).getSubTotal().toString());
							// documentoRV.setTotal(remisionVenta.get(index).getTotal().toString());
							// documentoRV.setObservacion("-");
							documentoRV.setUsuarioCreador(remisionVenta.get(index).getIdVendedor() != null
									? remisionVenta.get(index).getIdVendedor().getNombreCompleto()
									: "");
							documentoRV.setEstadoDocumento(remisionVenta.get(index).getCodEstadoCon() != null
									? remisionVenta.get(index).getCodEstadoCon().getEstado()
									: "");
							lDocumentoMovimiento.add(documentoRV);
						}
					}
				}
			}
			if (tipoDocumento.equals("0") || tipoDocumento.equals("3")) {
				// ! Devolucion de compras del articulo
				List<DevolucionCompras> devolucionCompra = devolucionCompraDao.obtenerDevolucionArticulo(idenArticulo);
				if (devolucionCompra != null) {
					logger.info(devolucionCompra.size());
					if (devolucionCompra.size() > 0) {
						for (DevolucionCompras devolucion : devolucionCompra) {
							logger.info(devolucionCompra.get(0).getId());
							DocumentoMovimiento documentoDC = new DocumentoMovimiento();
							documentoDC.setTipoDocumento("devolucion de compra");
							documentoDC.setCodArticulo(nombreArticulo);
							documentoDC.setUbInicial("NA");
							documentoDC.setUbFinal("NA");
							documentoDC.setCliente(
									devolucion.getProveedor() != null ? devolucion.getProveedor().getProveedor()
											: "");
							if (devolucion.getFechaMod() != null) {
								String datetimeString = devolucion.getFechaMod().toLocaleString().toString();
								logger.info(datetimeString);
								// // SimpleDateFormat datetimeFormat = new SimpleDateFormat("dd/MM/yyyy
								// hh:mm:ss
								// // a");
								// SimpleDateFormat datetimeF = new SimpleDateFormat();
								// // String dateString = dateFormat.format(datetime);
								// SimpleDateFormat datetimeFormat = new
								// SimpleDateFormat(datetimeF.toPattern());
								// logger.info(datetimeFormat.toPattern());
								// Date datetime = datetimeFormat.parse(datetimeString);

								SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

								String dateString = dateFormat.format(devolucion.getFechaMod());
								logger.info(dateString);
								documentoDC.setFechaCreacion(dateString);
							}
							documentoDC.setIdDocumento(devolucion.getNroDevolucion());
							List<DevolucionArticulosCompra> articulosDevolucion = devolucion.getDevolucionArticulos();
							float cantidad;
							float costo;
							Float subtotal;
							Float total;
							for (DevolucionArticulosCompra articuloF : articulosDevolucion) {
								if (articuloF.getArticuloRemision().getIdArticulo().getId() == idenArticulo) {
									documentoDC.setCantidad(articuloF.getCantidad().toString());
									cantidad = articuloF.getCantidad().floatValue();
									costo = articuloF.getArticuloRemision().getIdArticulo().getPrecioCosto().floatValue();
									total = cantidad * costo;
									logger.info(articuloF.getIvaDevuelto().floatValue() + 1);
									subtotal = total / (((articuloF.getIvaDevuelto().floatValue()) / 100) + 1);
									documentoDC.setCosto(subtotal.toString());
									documentoDC.setTotal(total.toString());
								} else {
									documentoDC.setCantidad("0");
									documentoDC.setCosto("0");
									documentoDC.setTotal("0");
								}
							}
							// documentoDC.setCantidad(Double.toString(devolucion.getCantidadTotal()));
							// documentoDC.setCosto(devolucion.getSubTotal().toString());
							// documentoDC.setTotal(devolucion.getTotal().toString());
							// documentoDC.setObservacion(devolucionCompra.get(0).getObservaciones());
							documentoDC.setUsuarioCreador(devolucion.getUsuarioMod() != null
									? devolucion.getUsuarioMod().getNombreCompleto()
									: "");
							documentoDC.setEstadoDocumento(
									devolucion.getCodEstadoCon() != null
											? devolucion.getCodEstadoCon().getEstado()
											: "");
							lDocumentoMovimiento.add(documentoDC);
						}
					}
				}
			}
			if (tipoDocumento.equals("0") || tipoDocumento.equals("4")) {
				// ! Devolucion de ventas del articulo
				List<DevolucionVentasCliente> devolucionVenta = devolucionClienteDao.obtenerDevolucionArticulo(idenArticulo);
				if (devolucionVenta != null) {
					logger.info(devolucionVenta.size());
					if (devolucionVenta.size() > 0) {
						for (DevolucionVentasCliente devolucion : devolucionVenta) {
							logger.info(devolucionVenta.get(0).getId());
							DocumentoMovimiento documentoDV = new DocumentoMovimiento();
							documentoDV.setTipoDocumento("devolucion de venta");
							documentoDV.setCodArticulo(nombreArticulo);
							documentoDV.setUbInicial("NA");
							documentoDV.setUbFinal("NA");
							documentoDV.setCliente(
									devolucion.getCliente() != null ? devolucion.getCliente().getNombres() : "");

							if (devolucion.getFechaMod() != null) {
								String datetimeString = devolucion.getFechaMod().toLocaleString().toString();
								logger.info(datetimeString);
								// // SimpleDateFormat datetimeFormat = new SimpleDateFormat("dd/MM/yyyy
								// hh:mm:ss
								// // a");
								// SimpleDateFormat datetimeF = new SimpleDateFormat();
								// // String dateString = dateFormat.format(datetime);
								// SimpleDateFormat datetimeFormat = new
								// SimpleDateFormat(datetimeF.toPattern());
								// logger.info(datetimeFormat.toPattern());
								// Date datetime = datetimeFormat.parse(datetimeString);

								SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

								String dateString = dateFormat.format(devolucionVenta.get(0).getFechaMod());
								logger.info(dateString);
								documentoDV.setFechaCreacion(dateString);
							}
							documentoDV.setIdDocumento(devolucion.getNroDevolucion());
							List<DevolucionArticulosVenta> devolucionArticulos = devolucion.getDevolucionArticulos();
							float cantidad;
							float costo;
							Float subtotal;
							Float total;
							for (DevolucionArticulosVenta articuloF : devolucionArticulos) {
								if (articuloF.getArticuloRemision().getArticulo().getId() == idenArticulo) {
									documentoDV.setCantidad(articuloF.getCantidad().toString());
									cantidad = articuloF.getCantidad().floatValue();
									costo = articuloF.getArticuloRemision().getArticulo().getPrecioCosto().floatValue();
									total = cantidad * costo;
									logger.info(articuloF.getIvaDevuelto().floatValue() + 1);
									subtotal = total / (((articuloF.getIvaDevuelto().floatValue()) / 100) + 1);
									documentoDV.setCosto(subtotal.toString());
									documentoDV.setTotal(total.toString());
								} else {
									documentoDV.setCantidad("0");
									documentoDV.setCosto("0");
									documentoDV.setTotal("0");
								}
							}
							// documentoDV.setCantidad(Double.toString(devolucion.getCantidadTotal()));
							// documentoDV.setCosto(devolucion.getSubTotal().toString());
							// documentoDV.setTotal(devolucion.getTotal().toString());
							// documentoDV.setObservacion(devolucionVenta.get(0).getObservaciones());
							documentoDV.setUsuarioCreador(devolucion.getUsuarioMod() != null
									? devolucion.getUsuarioMod().getNombreCompleto()
									: "");
							documentoDV.setEstadoDocumento(
									devolucion.getCodEstadoCon() != null ? devolucion.getCodEstadoCon().getEstado()
											: "");
							lDocumentoMovimiento.add(documentoDV);
						}
					}
				}
			}
			if (tipoDocumento.equals("0") || tipoDocumento.equals("6")) {
				List<Traslado> trasladoArticulos = TrasladosDao.obtenerArticuloInformeInventario(idenArticulo);
				logger.info(trasladoArticulos.size());
				if (trasladoArticulos.size() > 0) {
					for (Traslado movimiento : trasladoArticulos) {
						for (ArticuloMovimientos traslado : movimiento.getMovimientos()) {
							if (traslado != null) {
								DocumentoMovimiento documentoTA = new DocumentoMovimiento();
								documentoTA.setTipoDocumento("traslado");
								documentoTA.setCodArticulo(nombreArticulo);
								documentoTA.setUbInicial(traslado.getSectorOrigen().getIdLocal().getTLocal() + "--"
										+ traslado.getSectorOrigen().getTSector());
								documentoTA.setUbFinal(traslado.getSectorDestino().getIdLocal().getTLocal() + "--"
										+ traslado.getSectorDestino().getTSector());
								documentoTA.setCliente("NA");

								if (movimiento.getFechaDocumento() != null) {
									String datetimeString = movimiento.getFechaDocumento().toString();
									logger.info(datetimeString);

									// // SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd
									// // HH:mm:ss.SSS");
									// SimpleDateFormat datetimeF = new SimpleDateFormat();
									// // String dateString = dateFormat.format(datetime);
									// SimpleDateFormat datetimeFormat = new
									// SimpleDateFormat(datetimeF.toPattern());
									// logger.info(datetimeFormat.toPattern());
									// Date datetime = datetimeFormat.parse(datetimeString);

									SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

									String dateString = dateFormat.format(movimiento.getFechaDocumento());
									logger.info(dateString);
									documentoTA.setFechaCreacion(dateString);
								}
								documentoTA.setIdDocumento(movimiento.getNumeroDocumento().toString());
								documentoTA.setCantidad(traslado.getCantidad().toString());
								// documentoTA.setCosto(traslado.getArticulo().getPrecioCosto().toString());
								logger.info(traslado.getArticulo().getProducto().getId());
								logger.info(idSede);
								double precio = 0.0;
								if (PrecioDao.cantidadPreciosPorIdProducto(traslado.getArticulo().getProducto().getId(),
										idSede) > 0) {
									precio = PrecioDao.obtenerPrecioVentaPorIdProducto(traslado.getArticulo().getProducto().getId(),
											idSede);
								}
								logger.info(precio);
								if (precio > 0.0) {
									Float total = Float.parseFloat(Double.toString(traslado.getCantidad().doubleValue() * precio));
									String subtotal = (total / (((traslado.getArticulo().getIvaCosto()) / 100) + 1)) + "";
									documentoTA.setCosto(subtotal);
									documentoTA.setTotal(Double.toString(traslado.getCantidad().doubleValue() * precio));
								} else {
									documentoTA.setTotal("NA");
								}
								// documentoTA.setObservacion("-");
								documentoTA.setUsuarioCreador(movimiento.getUsuario() != null
										? movimiento.getUsuario().getNombreCompleto()
										: "");
								documentoTA.setEstadoDocumento(movimiento.getEstado() != null
										? movimiento.getEstado().getEstado()
										: "");
								lDocumentoMovimiento.add(documentoTA);
							}
						}
					}
				} else {
					logger.info("no hay traslados para este articulo");
				}
			}

			if (tipoDocumento.equals("0") || tipoDocumento.equals("7")) {
				List<SalidaMercancia> salidaArticulos = SalidaMercanciaDao.obtenerArticuloInformeInventario(idenArticulo);
				logger.info(salidaArticulos.size());
				if (salidaArticulos.size() > 0) {
					for (SalidaMercancia movimiento : salidaArticulos) {
						for (SalidaArticulos traslado : movimiento.getSalidas()) {
							if (traslado != null) {

								DocumentoMovimiento documentoSA = new DocumentoMovimiento();
								documentoSA.setTipoDocumento("salida");
								documentoSA.setCodArticulo(nombreArticulo);
								documentoSA.setUbInicial("NA");
								documentoSA.setUbFinal("NA");
								documentoSA.setCliente("NA");

								if (movimiento.getFechaDocumento() != null) {
									String datetimeString = movimiento.getFechaDocumento().toString();
									logger.info(datetimeString);
									// // SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd
									// // HH:mm:ss.SSS");
									// SimpleDateFormat datetimeF = new SimpleDateFormat();
									// // String dateString = dateFormat.format(datetime);
									// SimpleDateFormat datetimeFormat = new
									// SimpleDateFormat(datetimeF.toPattern());
									// logger.info(datetimeFormat.toPattern());
									// Date datetime = datetimeFormat.parse(datetimeString);

									SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

									String dateString = dateFormat.format(movimiento.getFechaDocumento());
									logger.info(dateString);
									documentoSA.setFechaCreacion(dateString);
								}
								documentoSA.setIdDocumento(movimiento.getNumeroDocumento().toString());
								documentoSA.setCantidad(Float.toString(traslado.getCantidad()));
								// documentoSA.setCosto(traslado.getArticulo().getPrecioCosto().toString());
								// Double precio =
								// PrecioDao.obtenerPrecioVentaPorIdProducto(traslado.getArticulo().getProducto().getId(),
								// idSede);
								Float total = traslado.getCantidad() * traslado.getCostoUnitario().floatValue();
								String subtotal = (total / (((traslado.getArticulo().getIvaCosto()) / 100) + 1)) + "";
								documentoSA.setCosto(subtotal);
								documentoSA.setTotal(Float.toString(traslado.getCantidad() * traslado.getCostoUnitario().floatValue()));
								// documentoSA.setObservacion("-");
								documentoSA.setUsuarioCreador(movimiento.getUsuario() != null
										? movimiento.getUsuario().getNombreCompleto()
										: "");
								documentoSA.setEstadoDocumento(movimiento.getEstado() != null
										? movimiento.getEstado().getEstado()
										: "");
								lDocumentoMovimiento.add(documentoSA);
							}
						}
					}
				} else {
					logger.info("no hay salidas para este articulo");
				}
			}
			if (tipoDocumento.equals("0") || tipoDocumento.equals("5")) {
				List<Entrada> entradaArticulos = EntradaDao.obtenerArticuloInformeInventario(idenArticulo);
				logger.info(entradaArticulos.size());
				if (entradaArticulos.size() > 0) {
					for (Entrada movimiento : entradaArticulos) {
						for (EntradaArticulos traslado : movimiento.getEntradas()) {
							if (traslado != null) {

								DocumentoMovimiento documentoEA = new DocumentoMovimiento();
								documentoEA.setTipoDocumento("entrada");
								documentoEA.setCodArticulo(nombreArticulo);
								documentoEA.setUbInicial("NA");
								documentoEA.setUbFinal("NA");
								documentoEA.setCliente("NA");

								if (movimiento.getFechaDocumento() != null) {
									String datetimeString = movimiento.getFechaDocumento().toString();
									logger.info(datetimeString);

									// // SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd
									// // HH:mm:ss.SSS");
									// SimpleDateFormat datetimeF = new SimpleDateFormat();
									// // String dateString = dateFormat.format(datetime);
									// SimpleDateFormat datetimeFormat = new
									// SimpleDateFormat(datetimeF.toPattern());
									// logger.info(datetimeFormat.toPattern());
									// Date datetime = datetimeFormat.parse(datetimeString);

									SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

									String dateString = dateFormat.format(movimiento.getFechaDocumento());
									logger.info(dateString);
									documentoEA.setFechaCreacion(dateString);
								}
								documentoEA.setIdDocumento(movimiento.getNumeroDocumento().toString());
								logger.info(traslado.getCantidad());
								logger.info(traslado.getCosto());
								documentoEA.setCantidad(Float.toString(traslado.getCantidad()));
								// documentoEA.setCosto(traslado.getArticulo().getPrecioCosto().toString());
								// Double precio =
								// PrecioDao.obtenerPrecioVentaPorIdProducto(traslado.getArticulo().getProducto().getId(),
								// idSede);
								Float total = traslado.getCantidad() * traslado.getCostoUnitario().floatValue();
								String subtotal = (total / (((traslado.getArticulo().getIvaCosto()) / 100) + 1)) + "";
								documentoEA.setCosto(subtotal);
								documentoEA.setTotal(Float.toString(traslado.getCantidad() * traslado.getCosto().floatValue()));
								// documentoEA.setObservacion("-");
								documentoEA.setUsuarioCreador(movimiento.getUsuario() != null
										? movimiento.getUsuario().getNombreCompleto()
										: "");
								documentoEA.setEstadoDocumento(movimiento.getEstado() != null
										? movimiento.getEstado().getEstado()
										: "");
								lDocumentoMovimiento.add(documentoEA);

							}
						}
					}
				} else {
					logger.info("no hay entradas para este articulo");
				}
			}
			if (lDocumentoMovimiento.size() > 0) {
				logger.info("articulo _ movimiento");
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de articulo movimiento exitosa");
				respuestaDto.setObjetoRespuesta(lDocumentoMovimiento);
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
			} else {
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK,
						"No existe movimientos de este articulo");
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
			}
		} catch (

		Exception e) {
			logger.error("Error al obtener articulo " + e + " Line: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al obtener articulo " + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerArticulosInformeInventarioProducto(String idProducto) {
		ResponseEntity<Object> respuesta;
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		Integer idSede = usuarioInformacion.getIdOrganizacion().getId();
		try {
			List<ArticuloMovimientos> articuloI;
			StringBuilder query = new StringBuilder();
			StringBuilder queryCantidad = new StringBuilder();
			String articulo = null;
			Producto ProductoMov = productoDao.findBycodigo(idProducto);
			Integer idProductoOb = ProductoMov.getId();
			// List<ArticuloMovimientos> articulo =
			// inventarioDao.obtenerArticuloInformeInventario(idArticulo);
			// inventarioDao.obtenerArticuloInformeInventario(idArticulo);
			// List<Articulo> articulos = articuloDao.buscarPorSedeCodigo(15, "0520000027");
			List<Articulo> articulos = articuloDao.buscarPorProductoSede(idProductoOb, idSede);
			// List<ArticuloMovimientos> articuloI =
			// articuloDao.obtenerArticuloInformeInventario(1460);
			if (articulos != null) {
				logger.info("articulo _ movimiento");
				// logger.info(articulos);
				// logger.info(articulo.size());
				// RemisionCompra remision =
				// remisionCompraDao.buscarPorArticulo(articulo.getId());
				// Proveedor proveedor = null;
				// if (remision != null) {
				// proveedor = remision.getIdProveedor();
				// }
				// ArticuloInventario articuloInventario =
				// ArticuloInventario.crearArticuloInventario(articulo, proveedor,remision);
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de articulo movimiento exitosa");
				respuestaDto.setObjetoRespuesta(articulos);
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
			} else {
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK,
						"No existe movimientos de este articulo");
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
			}
		} catch (Exception e) {
			logger.error("Error al obtener articulo " + e + " Line: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al obtener articulo " + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> guardarInventario(Inventario inventario) {
		ResponseEntity<Object> respuesta;
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		Organizacion idSede = usuarioInformacion.getIdOrganizacion();
		Inventario inven = new Inventario();
		inven = inventarioDao.Inventario(idSede.getId());
		int i = 0;

		if (inven == null) {
			inventarioDao.save(inventario);
			i = 1;
		}

		try {
			for (RowInventario row : inventario.getRows()) {
				row.setUsuario(usuarioInformacion);
			}
			inventario.setEmpresa(idSede);
			Inventario inventarioLocal = inventarioDao.obtenerInventario(idSede.getId()).orElse(null);
			if (inventarioLocal != null) {
				for (RowInventario row : inventario.getRows()) {
					row.setInventario(inventarioLocal);
					RowInventario rowExistente = rowInventarioDao.validarExistencia(idSede.getId(), row.getCodigoArticulo())
							.orElse(null);
					if (rowExistente == null) {
						rowInventarioDao.save(row);
					}
				}
				/*
				 * List<RowInventario> rowsOld =
				 * rowInventarioDao.obtenerRowsInventario(inventario.getId());
				 * List<RowInventario> merge = new
				 * ArrayList<RowInventario>(inventario.getRows());
				 * merge.addAll(rowsOld);
				 * inventario.setRows(merge);
				 */
			} else {
				if (i == 0) {
					inventarioDao.save(inventario);
				}
			}
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "guardado de inventario exitoso");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error al guardar inventario " + e.getStackTrace()[0].getLineNumber() + " " + e.getMessage());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al guardar inventario " + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	/*
	 * @PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')"
	 * )
	 * public ResponseEntity<Object> guardarInventario(Inventario inventario) {
	 * ResponseEntity<Object> respuesta;
	 * Authentication autenticacion =
	 * SecurityContextHolder.getContext().getAuthentication();
	 * Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
	 * InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
	 * .buscarPorIdUsuario(usuarioAutenticado.getId());
	 * Organizacion idSede = usuarioInformacion.getIdOrganizacion();
	 * inventario.setEmpresa(idSede);
	 * inventarioDao.save(inventario);
	 * 
	 * try {
	 * for (RowInventario row : inventario.getRows()) {
	 * row.setUsuario(usuarioInformacion);
	 * }
	 * Inventario inventarioLocal =
	 * inventarioDao.obtenerInventario(idSede.getId()).orElse(null);
	 * if(inventarioLocal!=null) {
	 * for (RowInventario row : inventario.getRows()) {
	 * row.setInventario(inventarioLocal);
	 * RowInventario rowExistente =
	 * rowInventarioDao.validarExistencia(idSede.getId(),
	 * row.getCodigoArticulo()).orElse(null);
	 * if(rowExistente == null) {
	 * rowInventarioDao.save(row);
	 * }
	 * }
	 * }
	 * RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK,
	 * "guardado de inventario exitoso");
	 * respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
	 * }catch (Exception e) {
	 * logger.error("Error al guardar inventario " +
	 * e.getStackTrace()[0].getLineNumber() + " " + e.getMessage());
	 * RespuestaDto respuestaDto = new
	 * RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
	 * "Error al guardar inventario " + e);
	 * respuesta = new ResponseEntity<>(respuestaDto,
	 * HttpStatus.INTERNAL_SERVER_ERROR);
	 * }
	 * return respuesta;
	 * }
	 */
	public ResponseEntity<Object> ValidarSiExisteArticuloInventario(String codigo) {
		ResponseEntity<Object> respuesta;
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		int idSede = usuarioInformacion.getIdOrganizacion().getId();
		try {
			RowInventario row = rowInventarioDao.validarExistencia(idSede, codigo).orElse(null);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Envio de validacion exitoso");
			respuestaDto.setObjetoRespuesta(row);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error al validar inventario " + e.getStackTrace()[0].getLineNumber() + " " + e.getMessage());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al validar inventario " + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	// public ResponseEntity<Object> obtenerInventario(Integer page) {
	// ResponseEntity<Object> respuesta;
	// Authentication autenticacion =
	// SecurityContextHolder.getContext().getAuthentication();
	// Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
	// InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
	// .buscarPorIdUsuario(usuarioAutenticado.getId());
	// int idSede = usuarioInformacion.getIdOrganizacion().getId();
	// int total = 0;
	// Pageable paging = PageRequest.of(page, 20);
	// List<RowInventario> rows = null;
	// try {
	// Inventario inventario = inventarioDao.obtenerInventario(idSede).orElse(null);
	// if (inventario != null) {
	// rows = rowInventarioDao.obtenerRowsInventario(inventario.getId());
	// if (rows != null) {
	// inventario.setRows(rows);
	// }
	// total = rowInventarioDao.Total(inventario.getId());
	// }

	// Page<RowInventario> pages = new PageImpl<RowInventario>(rows, paging,
	// rows.size());
	// RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Envio de
	// validacion exitoso");
	// respuestaDto.setObjetoRespuesta(pages);
	// respuestaDto.setSuma(total);
	// respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
	// } catch (Exception e) {
	// logger.error("Error al validar inventario " +
	// e.getStackTrace()[0].getLineNumber() + " " + e.getMessage());
	// RespuestaDto respuestaDto = new
	// RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
	// "Error al validar inventario " + e);
	// respuesta = new ResponseEntity<>(respuestaDto,
	// HttpStatus.INTERNAL_SERVER_ERROR);
	// }
	// return respuesta;
	// }

	public ResponseEntity<Object> obtenerInventario(Integer page) {
		ResponseEntity<Object> respuesta;
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		int idSede = usuarioInformacion.getIdOrganizacion().getId();
		int total = 0;
		// Pageable paging = PageRequest.of(page, 20);
		List<RowInventario> rows = null;
		Page<RowInventario> rows2 = null;
		Pageable paging = PageRequest.of(page, 20);

		try {
			Inventario inventario = inventarioDao.obtenerInventario(idSede).orElse(null);
			if (inventario != null) {
				rows = rowInventarioDao.obtenerRowsInventario(inventario.getId());
				rows2 = rowInventarioDao.obtenerRowsInventario2(inventario.getId(), paging);
				if (rows != null) {
					inventario.setRows(rows);
				}
				total = rowInventarioDao.Total(inventario.getId());
			}

			// Page<RowInventario> pages = new PageImpl<RowInventario>(rows, paging,
			// rows.size());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Envio de validacion exitoso");
			respuestaDto.setObjetoRespuesta(rows2);
			respuestaDto.setSuma(total);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error al validar inventario " + e.getStackTrace()[0].getLineNumber() + " " + e.getMessage());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al validar inventario " + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	public ResponseEntity<Object> obtenerInventario2() {
		ResponseEntity<Object> respuesta;
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		int idSede = usuarioInformacion.getIdOrganizacion().getId();
		int total = 0;
		List<RowInventario> rows = null;
		try {
			Inventario inventario = inventarioDao.obtenerInventario(idSede).orElse(null);
			if (inventario != null) {
				rows = rowInventarioDao.obtenerRowsInventario(inventario.getId());
				if (rows != null) {
					inventario.setRows(rows);
				}
				total = rowInventarioDao.Total(inventario.getId());
			}

			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Envio de validacion exitoso");
			respuestaDto.setObjetoRespuesta(inventario);
			respuestaDto.setSuma(total);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error al validar inventario " + e.getStackTrace()[0].getLineNumber() + " " + e.getMessage());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al validar inventario " + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	// public ResponseEntity<Object> obtenerInventarioLocal(Integer page) {
	// ResponseEntity<Object> respuesta;
	// Authentication autenticacion =
	// SecurityContextHolder.getContext().getAuthentication();
	// Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
	// InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
	// .buscarPorIdUsuario(usuarioAutenticado.getId());
	// List<Articulo> articulos = new ArrayList<Articulo>();
	// List<RowInventario> rows = new ArrayList<RowInventario>();
	// Pageable paging = PageRequest.of(page, 20);

	// int idSede = usuarioInformacion.getIdOrganizacion().getId();
	// try {
	// Inventario inventario = inventarioDao.obtenerInventario(idSede).orElse(null);
	// if (inventario != null) {
	// rows = rowInventarioDao.obtenerRowsInventario(inventario.getId());
	// }

	// for (RowInventario w : rows) {

	// Articulo articulo = articuloDao.obtenerCodigoSede(w.getCodigoArticulo(),
	// idSede);
	// articulos.add(articulo);
	// }
	// Page<Articulo> pages = new PageImpl<Articulo>(articulos, paging,
	// articulos.size());
	// RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Envio de
	// validacion exitoso");
	// respuestaDto.setObjetoRespuesta(pages);
	// respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
	// } catch (Exception e) {
	// logger.error("Error al validar inventario " +
	// e.getStackTrace()[0].getLineNumber() + " " + e.getMessage());
	// RespuestaDto respuestaDto = new
	// RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
	// "Error al validar inventario " + e);
	// respuesta = new ResponseEntity<>(respuestaDto,
	// HttpStatus.INTERNAL_SERVER_ERROR);
	// }
	// return respuesta;
	// }

	public ResponseEntity<Object> obtenerInventarioLocal(Integer page) {
		ResponseEntity<Object> respuesta;
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		List<Articulo> articulos = new ArrayList<Articulo>();
		// List<RowInventario> rows = new ArrayList<RowInventario>();
		Pageable paging = PageRequest.of(page, 20);

		// Page<RowInventario> rowspAGE = new PageImpl<RowInventario>(rows, paging,
		// articulos.size());

		int idSede = usuarioInformacion.getIdOrganizacion().getId();
		try {
			Inventario inventario = inventarioDao.obtenerInventario(idSede).orElse(null);
			int cont = 0;
			if (inventario != null && cont == 0) {
				Page<RowInventario> rows = rowInventarioDao.obtenerRowsInventario2(inventario.getId(), paging);
				// rows = rowInventarioDao.obtenerRowsInventario(inventario.getId());

				logger.info(rows.getPageable());
				for (RowInventario w : rows) {

					Articulo articulo = articuloDao.obtenerCodigoSede(w.getCodigoArticulo(), idSede);
					articulos.add(articulo);
					cont = cont + 1;
					logger.info(cont);
				}
			}
			Page<Articulo> pages = new PageImpl<Articulo>(articulos, paging, articulos.size());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Envio de validacion exitoso");
			respuestaDto.setObjetoRespuesta(pages);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error al validar inventario " + e.getStackTrace()[0].getLineNumber() + " " + e.getMessage());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al validar inventario " + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	public ResponseEntity<Object> eliminarInventario() {
		ResponseEntity<Object> respuesta;
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		int idSede = usuarioInformacion.getIdOrganizacion().getId();
		try {
			rowInventarioDao.eliminarRowsEmpresa(idSede);
			inventarioDao.eliminarInventario(idSede);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "eliminar inventario exitoso");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error al eliminar inventario " + e.getStackTrace()[0].getLineNumber() + " " + e.getMessage());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al eliminar inventario " + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> actualizarRow(RowInventario row) {
		ResponseEntity<Object> respuesta;
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		int idSede = usuarioInformacion.getIdOrganizacion().getId();
		try {
			Inventario inventarioLocal = inventarioDao.obtenerInventario(idSede).orElse(null);
			for (RowInventario rowAux : inventarioLocal.getRows()) {

				if (rowAux.getId() == row.getId()) {
					rowAux.setCantidadCompra(row.getCantidadCompra());
					rowAux.setCantidadDisponible(row.getCantidadDisponible());
					rowAux.setLocal(row.getLocal());
					rowAux.setSector(row.getSector());
				}
			}

			row.setInventario(inventarioLocal);
			rowInventarioDao.save(row);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "se guardo el registro exitosamente");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error al guardar registro " + e.getStackTrace()[0].getLineNumber() + " " + e.getMessage());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al guardar registro " + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> eliminarRow(Integer idRow) {
		ResponseEntity<Object> respuesta;
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		int idSede = usuarioInformacion.getIdOrganizacion().getId();
		List<RowInventario> row = null;
		try {
			rowInventarioDao.deleteById(idRow);

			row = rowInventarioDao.consultarRowsEmpresa(idSede);
			if (row.size() == 0) {
				inventarioDao.eliminarInventario(idSede);
			}
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "se elimino el registro exitosamente");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error al eliminar registro " + e.getStackTrace()[0].getLineNumber() + " " + e.getMessage());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al eliminar registro " + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> ActualizarInvetario() {
		ResponseEntity<Object> respuesta;
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		int idSede = usuarioInformacion.getIdOrganizacion().getId();
		try {
			Inventario inventario = inventarioDao.obtenerInventario(idSede).orElse(null);
			List<RowInventario> rows = new ArrayList<RowInventario>();
			if (inventario != null) {

				rows = inventario.getRows();
				EstadoArticulo estado = new EstadoArticulo();
				estado = estadoArticuloDao.EstadoDisponible();
				for (int i = 0; i < rows.size(); i++) {
					Articulo articulo = new Articulo();
					String codigo = rows.get(i).getCodigoArticulo();

					articulo = articuloDao.findByCodigo(codigo);
					double valorDouble = rows.get(i).getCantidadDisponible();
					float valorFloat = (float) valorDouble;
					if (articulo.getCantidadDisponible() != valorFloat) {
						articulo.setCantidadDisponible(valorFloat);
					}
					if (rows.get(i).getLocal() != articulo.getLocal()) {
						articulo.setLocal(rows.get(i).getLocal());
					}
					if (rows.get(i).getSector() != articulo.getSector()) {
						articulo.setSector(rows.get(i).getSector());
					}

					if (articulo.getEstadoArticulo() != estado) {
						articulo.setEstadoArticulo(estado);
					}
					articuloDao.save(articulo);
				}
			}
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "se liquido el inventario exitosamente");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error al liquidar el inventario" + e.getStackTrace()[0].getLineNumber() + " " + e.getMessage());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al liquidar el inventario " + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> liquidarInvetario() {
		ResponseEntity<Object> respuesta;
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		int idSede = usuarioInformacion.getIdOrganizacion().getId();
		try {
			Inventario inventario = inventarioDao.obtenerInventario(idSede).orElse(null);
			List<RowInventario> rows = new ArrayList<RowInventario>();
			if (inventario != null) {
				rows = rowInventarioDao.obtenerRowsInventario(inventario.getId());
			}
			for (RowInventario row : rows) {
				Articulo articulo = articuloDao.obtenerCodigoSede(row.getCodigoArticulo(), idSede);
				if (articulo != null) {
					articulo.setCantidadDisponible((float) (row.getCantidadDisponible().doubleValue()));
					articulo.setLocal(row.getLocal());
					articulo.setSector(row.getSector());

				}
			}
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "se liquido el inventario exitosamente");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error al liquidar el inventario" + e.getStackTrace()[0].getLineNumber() + " " + e.getMessage());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al liquidar el inventario " + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	// @SuppressWarnings("null")
	// @PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or
	// hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	// public ResponseEntity<Object> inventariosFiltros(Integer page, Integer local,
	// Integer sector) {
	// ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
	// Authentication autenticacion =
	// SecurityContextHolder.getContext().getAuthentication();
	// Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
	// InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
	// .buscarPorIdUsuario(usuarioAutenticado.getId());
	// Integer idSede = usuarioInformacion.getIdOrganizacion().getId();
	// Pageable paging = PageRequest.of(page, 20);
	// try {

	// List<RowInventario> rows = null;

	// Inventario inventario = inventarioDao.obtenerInventario(idSede).orElse(null);

	// if (inventario != null) {
	// if (local != null && sector != null) {
	// rows = rowInventarioDao.obtenerRowsInventarioFiltro(inventario.getId(),
	// local, sector);
	// } else if (local != null) {
	// rows = rowInventarioDao.obtenerRowsInventarioFiltroLocal(inventario.getId(),
	// local);
	// }
	// }
	// if (rows != null) {
	// inventario.setRows(rows);
	// }

	// Page<RowInventario> pages = new PageImpl<RowInventario>(rows, paging,
	// rows.size());

	// RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de
	// articulos exitosa");
	// respuestaDto.setObjetoRespuesta(pages);
	// respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
	// } catch (Exception e) {
	// logger.error("Error obteniendo articulos " + e + "linea " +
	// e.getStackTrace()[0].getLineNumber());
	// RespuestaDto respuestaDto = new
	// RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
	// "Error obteniendo articulos");
	// respuesta = new ResponseEntity<>(respuestaDto,
	// HttpStatus.INTERNAL_SERVER_ERROR);
	// }
	// return respuesta;
	// }

	@SuppressWarnings("null")
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> inventariosFiltros(Integer page, Integer local, Integer sector) {
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		Integer idSede = usuarioInformacion.getIdOrganizacion().getId();
		Pageable paging = PageRequest.of(page, 20);
		Page<RowInventario> rows2 = null;
		try {

			List<RowInventario> rows = null;

			Inventario inventario = inventarioDao.obtenerInventario(idSede).orElse(null);

			if (inventario != null) {
				if (local != null && sector != null) {
					rows = rowInventarioDao.obtenerRowsInventarioFiltro(inventario.getId(), local, sector);
					rows2 = rowInventarioDao.obtenerRowsInventarioFiltro2(inventario.getId(), local, sector, paging);
				}
			}
			if (rows != null) {
				inventario.setRows(rows);
			}

			Page<RowInventario> pages = new PageImpl<RowInventario>(rows, paging, rows.size());

			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de articulos exitosa");
			respuestaDto.setObjetoRespuesta(rows2);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo articulos " + e + "linea " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error obteniendo articulos");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@SuppressWarnings("null")
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> inventariosFiltrosLocal(Integer page, Integer local) {
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		Integer idSede = usuarioInformacion.getIdOrganizacion().getId();
		Pageable paging = PageRequest.of(page, 20);
		try {

			List<RowInventario> rows = null;

			Inventario inventario = inventarioDao.obtenerInventario(idSede).orElse(null);

			if (inventario != null) {

				rows = rowInventarioDao.obtenerRowsInventarioFiltroLocal(inventario.getId(), local);

			}
			if (rows != null) {
				inventario.setRows(rows);
			}

			Page<RowInventario> pages = new PageImpl<RowInventario>(rows, paging, rows.size());

			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de articulos exitosa");
			respuestaDto.setObjetoRespuesta(pages);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo articulos " + e + "linea " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error obteniendo articulos");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	// @SuppressWarnings("null")
	// @PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or
	// hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	// public ResponseEntity<Object> inventariosFiltrosWeb(Integer page, Integer
	// local, Integer sector) {
	// ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
	// Authentication autenticacion =
	// SecurityContextHolder.getContext().getAuthentication();
	// Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
	// InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
	// .buscarPorIdUsuario(usuarioAutenticado.getId());
	// Integer idSede = usuarioInformacion.getIdOrganizacion().getId();
	// Pageable paging = PageRequest.of(page, 20);
	// try {
	// List<Articulo> articulos = new ArrayList<Articulo>();
	// List<RowInventario> rows = null;

	// Inventario inventario = inventarioDao.obtenerInventario(idSede).orElse(null);
	// if (inventario != null) {
	// rows = rowInventarioDao.obtenerRowsInventarioFiltro(inventario.getId(),
	// local, sector);
	// }
	// if (rows != null) {
	// for (RowInventario w : rows) {

	// Articulo articulo = articuloDao.obtenerCodigoSede(w.getCodigoArticulo(),
	// idSede);
	// articulos.add(articulo);
	// }
	// }

	// Page<Articulo> pages = new PageImpl<Articulo>(articulos, paging,
	// articulos.size());

	// RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de
	// articulos exitosa");
	// respuestaDto.setObjetoRespuesta(pages);
	// respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
	// } catch (Exception e) {
	// logger.error("Error obteniendo articulos " + e + "linea " +
	// e.getStackTrace()[0].getLineNumber());
	// RespuestaDto respuestaDto = new
	// RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
	// "Error obteniendo articulos");
	// respuesta = new ResponseEntity<>(respuestaDto,
	// HttpStatus.INTERNAL_SERVER_ERROR);
	// }
	// return respuesta;
	// }

	@SuppressWarnings("null")
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> inventariosFiltrosWeb(Integer page, Integer local, Integer sector) {
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		Integer idSede = usuarioInformacion.getIdOrganizacion().getId();
		Pageable paging = PageRequest.of(page, 20);
		Page<RowInventario> rows2 = null;
		try {
			List<Articulo> articulos = new ArrayList<Articulo>();
			List<RowInventario> rows = null;

			Inventario inventario = inventarioDao.obtenerInventario(idSede).orElse(null);
			if (inventario != null) {
				rows = rowInventarioDao.obtenerRowsInventarioFiltro(inventario.getId(), local, sector);
				rows2 = rowInventarioDao.obtenerRowsInventarioFiltro2(inventario.getId(), local, sector, paging);
			}
			if (rows != null) {
				for (RowInventario w : rows2) {

					Articulo articulo = articuloDao.obtenerCodigoSede(w.getCodigoArticulo(), idSede);
					articulos.add(articulo);
				}
			}

			Page<Articulo> pages = new PageImpl<Articulo>(articulos, paging, articulos.size());

			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de articulos exitosa");
			respuestaDto.setObjetoRespuesta(pages);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo articulos " + e + "linea " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error obteniendo articulos");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@SuppressWarnings("null")
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> inventariosFiltrosLocalWeb(Integer page, Integer local) {
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		Integer idSede = usuarioInformacion.getIdOrganizacion().getId();
		Pageable paging = PageRequest.of(page, 20);
		try {
			List<Articulo> articulos = new ArrayList<Articulo>();
			List<RowInventario> rows = null;

			Inventario inventario = inventarioDao.obtenerInventario(idSede).orElse(null);
			if (inventario != null) {
				rows = rowInventarioDao.obtenerRowsInventarioFiltroLocal(inventario.getId(), local);
			}
			if (rows != null) {
				for (RowInventario w : rows) {

					Articulo articulo = articuloDao.obtenerCodigoSede(w.getCodigoArticulo(), idSede);
					articulos.add(articulo);
				}
			}

			Page<Articulo> pages = new PageImpl<Articulo>(articulos, paging, articulos.size());

			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de articulos exitosa");
			respuestaDto.setObjetoRespuesta(pages);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo articulos " + e + "linea " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error obteniendo articulos");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	public ResponseEntity<Object> obtenerArticulosFinalizados(Integer page) {
		ResponseEntity<Object> respuesta;
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		int idSede = usuarioInformacion.getIdOrganizacion().getId();
		Pageable paging = PageRequest.of(page, 20);
		int total = 0;
		List<RowInventario> rows = null;
		try {
			Inventario inventario = inventarioDao.obtenerInventario(idSede).orElse(null);
			if (inventario != null) {
				rows = rowInventarioDao.obtenerRowsInventarioFinalizados(inventario.getId());
				total = rowInventarioDao.Total(inventario.getId());
			}

			Page<RowInventario> pages = new PageImpl<RowInventario>(rows, paging, rows.size());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Envio de validacion exitoso");
			respuestaDto.setObjetoRespuesta(pages);
			respuestaDto.setSuma(total);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error al validar inventario " + e.getStackTrace()[0].getLineNumber() + " " + e.getMessage());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al validar inventario " + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	public ResponseEntity<Object> obtenerArticulosInexistentes(Integer page) {
		ResponseEntity<Object> respuesta;
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		int idSede = usuarioInformacion.getIdOrganizacion().getId();
		Pageable paging = PageRequest.of(page, 20);
		int total = 0;
		List<RowInventario> rows = null;
		List<Articulo> articulos = null;
		List<Articulo> articulosN = new ArrayList<Articulo>();
		boolean bandera = false;
		try {
			Inventario inventario = inventarioDao.obtenerInventario(idSede).orElse(null);
			if (inventario != null) {
				rows = rowInventarioDao.obtenerRowsInventario(inventario.getId());
				total = rowInventarioDao.Total(inventario.getId());
				articulos = articuloDao.todosSede(idSede);
			}
			for (Articulo a : articulos) {
				bandera = false;
				for (RowInventario r : rows) {

					if (r.getCodigoArticulo().equals(a.getCodigo())) {
						bandera = true;
					}

				}
				if (bandera == false) {
					articulosN.add(a);
				}
			}
			Page<Articulo> pages = new PageImpl<Articulo>(articulosN, paging, articulosN.size());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Envio de validacion exitoso");
			respuestaDto.setObjetoRespuesta(pages);
			respuestaDto.setSuma(total);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error al validar inventario " + e.getStackTrace()[0].getLineNumber() + " " + e.getMessage());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al validar inventario " + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	public ResponseEntity<Object> revivirArticulosFinalizados() {
		ResponseEntity<Object> respuesta;
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		int idSede = usuarioInformacion.getIdOrganizacion().getId();
		int total = 0;
		List<RowInventario> rows = null;
		try {
			Inventario inventario = inventarioDao.obtenerInventario(idSede).orElse(null);
			if (inventario != null) {
				rows = rowInventarioDao.obtenerRowsInventarioFinalizados(inventario.getId());
				total = rowInventarioDao.Total(inventario.getId());
			}

			for (int i = 0; i < rows.size(); i++) {
				Articulo articulo = new Articulo();
				EstadoArticulo estado = new EstadoArticulo();
				String codigo = rows.get(i).getCodigoArticulo();
				estado = estadoArticuloDao.EstadoDisponible();
				articulo = articuloDao.findByCodigo(codigo);
				articulo.setEstadoArticulo(estado);
				articuloDao.save(articulo);
			}
			for (RowInventario r : rows) {
				EstadoArticulo estado = new EstadoArticulo();
				estado = estadoArticuloDao.EstadoDisponible();
				r.setEstado(estado);
				rowInventarioDao.save(r);
			}
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Envio de validacion exitoso");
			respuestaDto.setObjetoRespuesta(null);
			respuestaDto.setSuma(total);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error al validar inventario " + e.getStackTrace()[0].getLineNumber() + " " + e.getMessage());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al validar inventario " + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	public ResponseEntity<Object> finalizarArticulosInexistentes() {
		ResponseEntity<Object> respuesta;
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		int idSede = usuarioInformacion.getIdOrganizacion().getId();
		int total = 0;
		List<RowInventario> rows = null;
		List<Articulo> articulos = null;
		List<Articulo> articulosN = new ArrayList<Articulo>();
		boolean bandera = false;
		try {
			Inventario inventario = inventarioDao.obtenerInventario(idSede).orElse(null);
			if (inventario != null) {
				rows = rowInventarioDao.obtenerRowsInventario(inventario.getId());
				total = rowInventarioDao.Total(inventario.getId());
				articulos = articuloDao.todosSede(idSede);
			}
			for (Articulo a : articulos) {
				bandera = false;
				for (RowInventario r : rows) {

					if (r.getCodigoArticulo().equals(a.getCodigo())) {
						bandera = true;
					}

				}
				if (bandera == false) {
					articulosN.add(a);
				}
			}

			for (Articulo a : articulosN) {
				EstadoArticulo estado = new EstadoArticulo();
				estado = estadoArticuloDao.EstadoFinalizado();
				a.setEstadoArticulo(estado);
				a.setCantidadDisponible((float) 0.0);
				articuloDao.save(a);
			}
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Envio de validacion exitoso");
			respuestaDto.setObjetoRespuesta(null);
			respuestaDto.setSuma(total);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error al validar inventario " + e.getStackTrace()[0].getLineNumber() + " " + e.getMessage());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al validar inventario " + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@SuppressWarnings("null")
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> estadoDevuelto() {
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		Integer idSede = usuarioInformacion.getIdOrganizacion().getId();
		try {
			List<Articulo> articulos = new ArrayList<Articulo>();

			articulos = articuloDao.articulosDevueltos(idSede);

			EstadoArticulo estado = new EstadoArticulo();
			estado = estadoArticuloDao.EstadoDisponible();
			for (Articulo a : articulos) {

				a.setEstadoArticulo(estado);
				articuloDao.save(a);
			}

			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de articulos exitosa");
			respuestaDto.setObjetoRespuesta(null);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo articulos " + e + "linea " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error obteniendo articulos");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@SuppressWarnings("null")
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> estadoBodega() {
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		Integer idSede = usuarioInformacion.getIdOrganizacion().getId();
		Inventario inventario = inventarioDao.obtenerInventario(idSede).orElse(null);
		List<RowInventario> rows = new ArrayList<RowInventario>();
		try {
			if (inventario != null) {

				rows = inventario.getRows();
				EstadoArticulo estado = new EstadoArticulo();
				estado = estadoArticuloDao.EstadoBodega();
				for (int i = 0; i < rows.size(); i++) {

					if (rows.get(i).getLocal().getTLocal().equals("BODEGA")) {
						logger.info("ingresa bodegaaaaa");
						Articulo articulo = new Articulo();
						String codigo = rows.get(i).getCodigoArticulo();
						articulo = articuloDao.findByCodigo(codigo);
						articulo.setEstadoArticulo(estado);
						articuloDao.save(articulo);
					}

				}
			}

			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de articulos exitosa");
			respuestaDto.setObjetoRespuesta(null);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo articulos " + e + "linea " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error obteniendo articulos");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

}
