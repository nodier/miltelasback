package com.softlond.base.service;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
import com.softlond.base.entity.ArticulosRemisionCompra;
import com.softlond.base.entity.ComprobanteEgreso;
import com.softlond.base.entity.ContableM;
import com.softlond.base.entity.ContableMD;
import com.softlond.base.entity.EstadoDocumento;
import com.softlond.base.entity.Factura;
import com.softlond.base.entity.FacturaArticulos;
import com.softlond.base.entity.FacturaCompra;
import com.softlond.base.entity.FacturaImpuestos;
import com.softlond.base.entity.FacturaM;
import com.softlond.base.entity.FacturaRemision;
import com.softlond.base.entity.FacturaRetenciones;
import com.softlond.base.entity.InformacionUsuario;
import com.softlond.base.entity.Usuario;
import com.softlond.base.repository.ArticulosRemisionCompraDao;
import com.softlond.base.repository.EstadoDocumentoDao;
import com.softlond.base.repository.FacturaCompraDao;
import com.softlond.base.repository.FacturaImpuestosDao;
import com.softlond.base.repository.FacturaRemisionDao;
import com.softlond.base.repository.FacturaRetencionesDao;
import com.softlond.base.repository.ProveedorDao;
import com.softlond.base.repository.RemisionCompraDao;
import com.softlond.base.repository.UsuarioInformacionDao;
import com.softlond.base.request.FacturaCompraRemisionRequest;
import com.softlond.base.entity.NotaCredito;
import com.softlond.base.entity.Producto;
import com.softlond.base.entity.Proveedor;
import com.softlond.base.entity.ReferenciaProd;
import com.softlond.base.entity.Tercero;
import com.google.gson.Gson;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.MediaType;

@Service
public class FacturaCompraService {

	private static final Logger logger = Logger.getLogger(FacturaCompraService.class);

	@Autowired
	private FacturaCompraDao facturaCompraDao;

	@Autowired
	private ProveedorDao proveedorDao;

	@Autowired
	private EstadoDocumentoDao estadoDocumento;

	@Autowired
	private FacturaCompraDao facturaDao;

	public FacturaCompraDao getFacturaCompraDao() {
		return facturaCompraDao;
	}

	public void setFacturaCompraDao(FacturaCompraDao facturaCompraDao) {
		this.facturaCompraDao = facturaCompraDao;
	}

	@Autowired
	private FacturaRemisionDao facturaRemisionDao;

	@Autowired
	UsuarioInformacionDao usuarioInformacionDao;

	@Autowired
	FacturaRetencionesDao facturaRetencionDao;

	public void setUsuarioInformacionDao(UsuarioInformacionDao usuarioInformacionDao) {
		this.usuarioInformacionDao = usuarioInformacionDao;
	}

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private FacturaRetencionesDao facturaRetencionesDao;

	@Autowired
	private FacturaImpuestosDao facturaImpuestosDao;

	@Autowired
	private RemisionCompraDao remisionDao;

	@Autowired
	private ProductoService productoService;

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN')")
	public ResponseEntity<Object> crearFactura(@RequestBody FacturaCompraRemisionRequest factura) throws Exception {
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());

		try {
			factura.getFacturaCompra().setUsuarioMod(usuarioInformacion);
			factura.getFacturaCompra().setIdSede(usuarioInformacion.getIdOrganizacion());
			// ! //TODO: se realiza la iteracion por la lista de cuentas asociadas a la
			// ! factura de compra
			if (factura.getListContable().size() > 0) {
				for (String[] fila : factura.getListContable()) {
					respuesta = this.crearFacturaCompraMekano(fila, factura.getFacturaCompra());
					if (respuesta.getStatusCode().is5xxServerError()) {
						throw new Exception("No existe tercero con este CODIGO");
					}
					logger.info(respuesta);
				}
			}

			logger.info(respuesta);
			facturaCompraDao.save(factura.getFacturaCompra());

			FacturaCompra facturaSave = facturaCompraDao.findByNroFactura(factura.getFacturaCompra().getNroFactura(),
					usuarioInformacion.getIdOrganizacion().getId());
			for (FacturaRemision remision : factura.getFacturaRemision()) {
				remision.setFacturaCompra(facturaSave);
				remision.getRemision().setEstadoDocumento("Asignado");
				remision.getRemision().setAsignado(1);
				remisionDao.save(remision.getRemision());

				// !termina consulta api contable fac compra
				// List<ArticulosRemisionCompra> articulosRemisionCompra =
				// facturaDao.obtenerArticulosRemisionCompra(remision.getId());
				// for (ArticulosRemisionCompra articulo : articulosRemisionCompra) {
				// if (facturaSave.getIdSede().getId() == 7 || facturaSave.getIdSede().getId()
				// == 15) {
				// logger.info("ingresa a la creacion de factura de compra desde la sede: " +
				// facturaSave.getIdSede().getId());

				// String uri = "http://190.145.10.42:8080/api/v1/TApoloRestInterface/execute";

				// logger.info(facturaSave.getFechaFactura().toString());
				// logger.info(facturaSave.getFechaVencimiento().toString());
				// // Date fechaEntradaVenta = facturaSave.getFechaFactura();
				// // Date fechaEntradaVence = facturaSave.getFechaVencimiento();
				// // String fechaVenta = fechaEntradaVenta.getDay() + "." +
				// // fechaEntradaVenta.getMonth() + "."
				// // + fechaEntradaVenta.getYear();
				// // logger.info(fechaVenta);
				// // String fechaVence = fechaEntradaVence.getDay() + "." +
				// // fechaEntradaVence.getMonth() + "."
				// // + fechaEntradaVence.getYear();
				// // logger.info(fechaVence);
				// Date fechaEntradaVenta = facturaSave.getFechaFactura();
				// Calendar calVent = Calendar.getInstance();
				// calVent.setTime(fechaEntradaVenta);

				// int yearVent = calVent.get(Calendar.YEAR);
				// int monthVent = calVent.get(Calendar.MONTH) + 1;
				// int dayVent = calVent.get(Calendar.DAY_OF_MONTH);

				// Date fechaEntradaVence = facturaSave.getFechaVencimiento();
				// Calendar calVenc = Calendar.getInstance();
				// calVenc.setTime(fechaEntradaVence);

				// int yearVenc = calVenc.get(Calendar.YEAR);
				// int monthVenc = calVenc.get(Calendar.MONTH) + 1;
				// int dayVenc = calVenc.get(Calendar.DAY_OF_MONTH);

				// String fechaVenta = dayVent + "." + monthVent + "." + yearVent;
				// logger.info(fechaVenta);
				// String fechaVence = dayVenc + "." + monthVenc + "." + yearVenc;
				// logger.info(fechaVence);

				// FacturaM facturaMekano = new FacturaM();
				// facturaMekano.setCLAVE("Set_Gestion_Primario");
				// facturaMekano.setTIPO("FC1");
				// facturaMekano.setPREFIJO("FC1");
				// logger.info(facturaSave.getNroFactura().toString());
				// facturaMekano.setNUMERO(facturaSave.getNroFactura().toString());
				// facturaMekano.setFECHA(fechaVenta);
				// facturaMekano.setVENCE(fechaVence);
				// facturaMekano.setTERCERO("75082596");
				// // facturaMekano.setVENDEDOR(factura.getIdVendedor().getNumeroDocumento());
				// facturaMekano.setVENDEDOR("24347052");
				// facturaMekano.setLISTA("NA");
				// facturaMekano.setBANCO("CG");
				// facturaMekano.setUSUARIO("SUPERVISOR");
				// switch (facturaSave.getIdSede().getId()) {
				// case 1:
				// facturaMekano.setCENTRO("04");
				// break;
				// case 6:
				// facturaMekano.setCENTRO("02");
				// break;
				// case 7:
				// facturaMekano.setCENTRO("03");
				// break;
				// case 11:
				// facturaMekano.setCENTRO("01");
				// break;
				// case 12:
				// facturaMekano.setCENTRO("06");
				// break;
				// case 13:
				// facturaMekano.setCENTRO("10");
				// break;
				// case 14:
				// facturaMekano.setCENTRO("09");
				// break;
				// case 15:
				// facturaMekano.setCENTRO("05");
				// break;

				// default:
				// break;
				// }
				// facturaMekano.setBODEGA("NA");
				// facturaMekano.setREFERENCIA(articulo.getIdArticulo().getProducto().getCodigo());
				// facturaMekano.setENTRADA(articulosRemisionCompra.size());
				// facturaMekano.setSALIDA(0);
				// logger.info(articulo.getIdArticulo().getPrecioCosto());
				// if (articulo.getIdArticulo().getPrecioCosto() > 0.0) {
				// facturaMekano
				// .setUNITARIO(articulo.getIdArticulo().getPrecioCosto().intValue());
				// } else {
				// facturaMekano.setUNITARIO(0);
				// }
				// // !revisar el campo de observaciones porque con factura.getObservaciones()
				// sale
				// // !error se envia valor por defecto
				// // if (!factura.getObservaciones().equals(null)) {
				// // facturaMekano.setNOTA(factura.getObservaciones());
				// // } else {
				// // facturaMekano.setNOTA("-");
				// // }
				// facturaMekano.setNOTA("-");

				// Gson gson = new Gson();
				// String rjson = gson.toJson(facturaMekano);

				// logger.info(rjson);
				// HttpHeaders headers = new HttpHeaders();
				// headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
				// HttpEntity<String> entity = new HttpEntity<String>(rjson, headers);
				// logger.info(entity);
				// RestTemplate rest = new RestTemplate();

				// if (entity != null) {
				// String result = rest.postForObject(uri, entity, String.class);

				// if (result.contains("EL TERCERO")) {
				// if (result.contains("NO EXISTE")) {
				// logger.info("existe un error de tercero inexistente");
				// // this.crearClienteMekano(facturaSave.getProveedor());
				// this.crearFacturaMekanoN(facturaSave, articulosRemisionCompra, articulo);
				// }
				// }
				// logger.info(result);

				// if (result.contains("LA REFERENCIA")) {
				// if (result.contains("NO EXISTE")) {
				// logger.info("existe un error de referencia inexistente");
				// this.crearProductoMekano(articulo.getIdArticulo().getProducto().getId());
				// this.crearFacturaMekanoN(facturaSave, articulosRemisionCompra, articulo);
				// }
				// }
				// logger.info(result);
				// }
				// }
				// }

			}
			logger.info(respuesta);
			facturaRemisionDao.saveAll(factura.getFacturaRemision());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Factura de compra creada exitosamente");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error al guardar la factura linea " + e.getStackTrace()[0].getLineNumber() + " " + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al guardar la factura" + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			logger.info("se ejecuta la sentencia finally");
			// facturaRemisionDao.saveAll(factura.getFacturaRemision());
			// RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Factura de
			// compra creada exitosamente");
			// respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		}
		return respuesta;
	}

	public ResponseEntity<Object> crearFacturaCompraMekano(String[] fila,
			FacturaCompra factura) throws Exception {
		// if (contable != null) {
		// logger.info(contable);
		// if (fechaFactura != null) {
		// logger.info(fechaFactura);
		// }
		// }
		// ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		RespuestaDto respuestaDto;
		ResponseEntity<Object> respuesta;
		// ResponseEntity<Object> respuesta = null;
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());

		Integer idSede = usuarioInformacion.getIdOrganizacion().getId();
		logger.info("FacturaCompraService => crearFacturaCompraMekano => idSede:" +
				idSede);
		try {
			String tipoDocumento = "C5";
			String centro = "05";
			String uri = "http://190.145.10.42:8080/api/v1/TApoloRestInterface/execute";

			// logger.info(contable.getFECHA());
			SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
			String fech = factura.getFechaFactura().toString();
			// logger.info(fech);
			// logger.info(fechaFactura);
			// Date dataFormateada = formato.parse(fech);
			// logger.info(dataFormateada);
			// // logger.info(body.getFechaDocumento().toString());
			// Date fechaEntradaVenta = (Date) contable.getFECHA();
			Date fechaEntradaVenta = factura.getFechaFactura();
			Calendar calVent = Calendar.getInstance();
			calVent.setTime(fechaEntradaVenta);

			int yearVent = calVent.get(Calendar.YEAR);
			int monthVent = calVent.get(Calendar.MONTH) + 1;
			int dayVent = calVent.get(Calendar.DAY_OF_MONTH);

			Date fechaEntradaVence = factura.getFechaFactura();
			Calendar calVenc = Calendar.getInstance();
			calVenc.setTime(fechaEntradaVence);

			int yearVenc = calVenc.get(Calendar.YEAR);
			int monthVenc = calVenc.get(Calendar.MONTH) + 1;
			int dayVenc = calVenc.get(Calendar.DAY_OF_MONTH);

			String fechaVenta = dayVent + "." + monthVent + "." + yearVent;
			logger.info(fechaVenta);
			String fechaVence = dayVenc + "." + monthVenc + "." + yearVenc;
			logger.info(fechaVence);

			switch (idSede) {
				case 1:
					tipoDocumento = "C4";
					centro = "04";
					break;
				case 6:
					tipoDocumento = "C2";
					centro = "02";
					break;
				case 7:
					tipoDocumento = "C3";
					centro = "03";
					break;
				case 11:
					tipoDocumento = "C1";
					centro = "01";
					break;
				case 12:
					tipoDocumento = "C6";
					centro = "06";
					break;
				case 13:
					tipoDocumento = "C10";
					centro = "10";
					break;
				case 14:
					tipoDocumento = "C9";
					centro = "09";
					break;
				case 15:
					tipoDocumento = "C5";
					centro = "05";
					break;
				default:
					tipoDocumento = "C5";
					centro = "05";
					break;
			}
			ContableM facturaMekano = new ContableM();
			facturaMekano.setCLAVE("Set_Contable_Primario");
			// logger.info(contable.getTIPO());
			facturaMekano.setTIPO(tipoDocumento);
			facturaMekano.setPREFIJO("_");
			// logger.info(factura.getNroFactura().toString());
			facturaMekano.setNUMERO(factura.getNroFactura());
			facturaMekano.setFECHA(fechaVenta);
			// facturaMekano.setFECHA(fechaVenta);
			facturaMekano.setCUENTA(fila[0].toString());
			if (factura.getProveedor().getNit().contains("-")) {
				// throw new Exception("No existe tercero con este CODIGO");
				respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
						"Error al guardar la factura");
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
				logger.info(respuesta);
				return respuesta;
			} else {
				facturaMekano.setTERCERO(factura.getProveedor().getNit());
			}
			// facturaMekano.setTERCERO(factura.getProveedor().getNit());
			facturaMekano.setCENTRO(centro);
			facturaMekano.setACTIVO("NA");
			facturaMekano.setEMPLEADO("24347052");
			facturaMekano.setDEBITO(fila[2].toString());
			facturaMekano.setCREDITO(fila[3].toString());
			facturaMekano.setBASE("0");
			facturaMekano.setNOTA(factura.getObservaciones().trim());
			facturaMekano.setUSUARIO("SUPERVISOR");

			Gson gson = new Gson();
			String rjson = gson.toJson(facturaMekano);

			// logger.info(rjson);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
			HttpEntity<String> entity = new HttpEntity<String>(rjson, headers);
			logger.info(entity);
			RestTemplate rest = new RestTemplate();

			// if (entity != null && 1 != 1) {
			if (entity != null) {
				TimeUnit.SECONDS.sleep(2);
				String result = rest.postForObject(uri, entity, String.class);

				if (result.contains("EL TERCERO")) {
					if (result.contains("NO EXISTE")) {
						respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
								"error de proveedor inexistente");
						logger.info("existe un error de tercero inexistente");
						respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
						logger.info(respuesta);
						return respuesta;
						// Proveedor proveedor = proveedorDao.findByNit(contable.getTERCERO());
						// RespuestaDto respuestaDto = new
						// RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
						// "Error al guardar la factura", "");
						// respuesta = new ResponseEntity<>(respuestaDto,
						// HttpStatus.INTERNAL_SERVER_ERROR);
						// return respuesta;
						// throw new Exception("No existe tercero con este CODIGO");
						// this.crearProveedorMekano(proveedor, idSede);
						// this.crearFacturaMekanoN(contable, fechaFactura);
					}
				}
				logger.info(result);

				if (result.contains("EL DOCUMENTO")) {
					if (result.contains("NO EXISTE")) {
						logger.info("existe un error de documento inexistente");
						respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
								"Error al guardar la factura");
						respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
						return respuesta;
					}
				}
				logger.info(result);

				if (result.contains("LA CUENTA")) {
					if (result.contains("NO EXISTE")) {
						logger.info("existe un error de cuenta inexistente");
						respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
								"Error al guardar la factura");
						respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
						return respuesta;
					}
				}
				logger.info(result);

				if (result.contains("LA CUENTA MANEJA TERCERO, ESTE DATO NO PUEDE ESTAR VACIO")) {
					logger.info("existe un error de CENTRO inexistente");
					respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
							"Error al guardar la factura");
					respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
					return respuesta;
				}

				logger.info(result);

				if (result.contains("LA CUENTA MANEJA CENTRO, ESTE DATO NO PUEDE ESTAR VACIO")) {
					logger.info("existe un error de CENTRO inexistente");
					respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
							"Error al guardar la factura");
					respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
					return respuesta;
				}

				// if (result.contains("LA REFERENCIA")) {
				// if (result.contains("NO EXISTE")) {
				// logger.info("existe un error de referencia inexistente");
				//
				// this.crearProductoMekano(factura.getFacArticulos().get(j).getArticulo().getProducto().getId());
				// this.crearFacturaMekanoN(factura, j);
				// }
				// }
				// logger.info(result);
			}
			// try {
			// factura.getFacturaCompra().setUsuarioMod(usuarioInformacion);
			// factura.getFacturaCompra().setIdSede(usuarioInformacion.getIdOrganizacion());
			// facturaCompraDao.save(factura.getFacturaCompra());
			// FacturaCompra facturaSave =
			// facturaCompraDao.findByNroFactura(factura.getFacturaCompra().getNroFactura(),
			// usuarioInformacion.getIdOrganizacion().getId());
			// for (FacturaRemision remision : factura.getFacturaRemision()) {
			// remision.setFacturaCompra(facturaSave);
			// remision.getRemision().setEstadoDocumento("Asignado");
			// remision.getRemision().setAsignado(1);
			// remisionDao.save(remision.getRemision());

			// // !termina consulta api contable fac compra
			// // List<ArticulosRemisionCompra> articulosRemisionCompra =
			// // facturaDao.obtenerArticulosRemisionCompra(remision.getId());
			// // for (ArticulosRemisionCompra articulo : articulosRemisionCompra) {
			// // if (facturaSave.getIdSede().getId() == 7 ||
			// facturaSave.getIdSede().getId()
			// // == 15) {
			// // logger.info("ingresa a la creacion de factura de compra desde la sede: " +
			// // facturaSave.getIdSede().getId());

			// // String uri =
			// "http://190.145.10.42:8080/api/v1/TApoloRestInterface/execute";

			// // logger.info(facturaSave.getFechaFactura().toString());
			// // logger.info(facturaSave.getFechaVencimiento().toString());
			// // // Date fechaEntradaVenta = facturaSave.getFechaFactura();
			// // // Date fechaEntradaVence = facturaSave.getFechaVencimiento();
			// // // String fechaVenta = fechaEntradaVenta.getDay() + "." +
			// // // fechaEntradaVenta.getMonth() + "."
			// // // + fechaEntradaVenta.getYear();
			// // // logger.info(fechaVenta);
			// // // String fechaVence = fechaEntradaVence.getDay() + "." +
			// // // fechaEntradaVence.getMonth() + "."
			// // // + fechaEntradaVence.getYear();
			// // // logger.info(fechaVence);
			// // Date fechaEntradaVenta = facturaSave.getFechaFactura();
			// // Calendar calVent = Calendar.getInstance();
			// // calVent.setTime(fechaEntradaVenta);

			// // int yearVent = calVent.get(Calendar.YEAR);
			// // int monthVent = calVent.get(Calendar.MONTH) + 1;
			// // int dayVent = calVent.get(Calendar.DAY_OF_MONTH);

			// // Date fechaEntradaVence = facturaSave.getFechaVencimiento();
			// // Calendar calVenc = Calendar.getInstance();
			// // calVenc.setTime(fechaEntradaVence);

			// // int yearVenc = calVenc.get(Calendar.YEAR);
			// // int monthVenc = calVenc.get(Calendar.MONTH) + 1;
			// // int dayVenc = calVenc.get(Calendar.DAY_OF_MONTH);

			// // String fechaVenta = dayVent + "." + monthVent + "." + yearVent;
			// // logger.info(fechaVenta);
			// // String fechaVence = dayVenc + "." + monthVenc + "." + yearVenc;
			// // logger.info(fechaVence);

			// // FacturaM facturaMekano = new FacturaM();
			// // facturaMekano.setCLAVE("Set_Gestion_Primario");
			// // facturaMekano.setTIPO("FC1");
			// // facturaMekano.setPREFIJO("FC1");
			// // logger.info(facturaSave.getNroFactura().toString());
			// // facturaMekano.setNUMERO(facturaSave.getNroFactura().toString());
			// // facturaMekano.setFECHA(fechaVenta);
			// // facturaMekano.setVENCE(fechaVence);
			// // facturaMekano.setTERCERO("75082596");
			// // //
			// facturaMekano.setVENDEDOR(factura.getIdVendedor().getNumeroDocumento());
			// // facturaMekano.setVENDEDOR("24347052");
			// // facturaMekano.setLISTA("NA");
			// // facturaMekano.setBANCO("CG");
			// // facturaMekano.setUSUARIO("SUPERVISOR");
			// // switch (facturaSave.getIdSede().getId()) {
			// // case 1:
			// // facturaMekano.setCENTRO("04");
			// // break;
			// // case 6:
			// // facturaMekano.setCENTRO("02");
			// // break;
			// // case 7:
			// // facturaMekano.setCENTRO("03");
			// // break;
			// // case 11:
			// // facturaMekano.setCENTRO("01");
			// // break;
			// // case 12:
			// // facturaMekano.setCENTRO("06");
			// // break;
			// // case 13:
			// // facturaMekano.setCENTRO("10");
			// // break;
			// // case 14:
			// // facturaMekano.setCENTRO("09");
			// // break;
			// // case 15:
			// // facturaMekano.setCENTRO("05");
			// // break;

			// // default:
			// // break;
			// // }
			// // facturaMekano.setBODEGA("NA");
			// //
			// facturaMekano.setREFERENCIA(articulo.getIdArticulo().getProducto().getCodigo());
			// // facturaMekano.setENTRADA(articulosRemisionCompra.size());
			// // facturaMekano.setSALIDA(0);
			// // logger.info(articulo.getIdArticulo().getPrecioCosto());
			// // if (articulo.getIdArticulo().getPrecioCosto() > 0.0) {
			// // facturaMekano
			// // .setUNITARIO(articulo.getIdArticulo().getPrecioCosto().intValue());
			// // } else {
			// // facturaMekano.setUNITARIO(0);
			// // }
			// // // !revisar el campo de observaciones porque con
			// factura.getObservaciones()
			// // sale
			// // // !error se envia valor por defecto
			// // // if (!factura.getObservaciones().equals(null)) {
			// // // facturaMekano.setNOTA(factura.getObservaciones());
			// // // } else {
			// // // facturaMekano.setNOTA("-");
			// // // }
			// // facturaMekano.setNOTA("-");

			// // Gson gson = new Gson();
			// // String rjson = gson.toJson(facturaMekano);

			// // logger.info(rjson);
			// // HttpHeaders headers = new HttpHeaders();
			// // headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
			// // HttpEntity<String> entity = new HttpEntity<String>(rjson, headers);
			// // logger.info(entity);
			// // RestTemplate rest = new RestTemplate();

			// // if (entity != null) {
			// // String result = rest.postForObject(uri, entity, String.class);

			// // if (result.contains("EL TERCERO")) {
			// // if (result.contains("NO EXISTE")) {
			// // logger.info("existe un error de tercero inexistente");
			// // // this.crearClienteMekano(facturaSave.getProveedor());
			// // this.crearFacturaMekanoN(facturaSave, articulosRemisionCompra, articulo);
			// // }
			// // }
			// // logger.info(result);

			// // if (result.contains("LA REFERENCIA")) {
			// // if (result.contains("NO EXISTE")) {
			// // logger.info("existe un error de referencia inexistente");
			// // this.crearProductoMekano(articulo.getIdArticulo().getProducto().getId());
			// // this.crearFacturaMekanoN(facturaSave, articulosRemisionCompra, articulo);
			// // }
			// // }
			// // logger.info(result);
			// // }
			// // }
			// // }

			// }
			// facturaRemisionDao.saveAll(factura.getFacturaRemision());
			respuestaDto = new RespuestaDto(HttpStatus.OK, "Factura de	compra creada exitosamente");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error al guardar la factura linea " +
					e.getStackTrace()[0].getLineNumber() + " " + e);
			respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al guardar la factura" + e);
			respuesta = new ResponseEntity<>(respuestaDto,
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		// finally {
		// logger.info("se ejecuta la sentencia finally");
		// // facturaRemisionDao.saveAll(factura.getFacturaRemision());
		// // RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Factura de
		// // compra creada exitosamente");
		// // respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		// }
		return respuesta;
	}

	private void crearProveedorMekano(Proveedor proveedor, Integer idSede) {

		if (proveedor != null && (idSede == 7 || idSede == 15)) {
			String clave = "Set_Terceros";
			String codigoProveedor = "";
			String digitoVerificacion = "";
			String clasificacionLegal = "";
			String tContacto = "";
			String nombre = "";
			String nombreD = "";
			String apellido = "";
			String apellidoD = "";
			String tProveedor = "";
			String tDireccion = "";
			String tTelefono = "";
			String tEmail = "";
			String tCodTipoIdentificacion = "";
			String nidCiudad = "";
			String codSociedad = "";

			// if (proveedor.getProveedor() != null) {
			// logger.info(proveedor.getProveedor());
			// }
			// !si el tipo de proveedor es persona natural
			if (proveedor.getClasificacionLegal().getId() == 1 || proveedor.getClasificacionLegal().getId() == 12 || proveedor
					.getClasificacionLegal().getId() == 13 || proveedor.getClasificacionLegal().getId() == 15) {
				String str = proveedor.getProveedor();
				String[] arr = str.split("-");
				logger.info(arr.length);
				nombre = arr[0];
				nombreD = arr[1];
				apellido = arr[2];
				apellidoD = arr[3];
			} else {
				// nombre = proveedor.getProveedor();
			}

			codigoProveedor = proveedor.getNit() != null ? proveedor.getNit() : "";
			digitoVerificacion = proveedor.getDigito() != null ? proveedor.getDigito().toString() : "";

			switch (proveedor.getClasificacionLegal().getId()) {
				case 1:
					clasificacionLegal = "N";
					codSociedad = "02";
					break;
				case 2:
					clasificacionLegal = "J";
					codSociedad = "04";
					break;
				case 3:
					clasificacionLegal = "J";
					codSociedad = "06";
					break;
				case 4:
					clasificacionLegal = "J";
					codSociedad = "05";
					break;
				case 5:
					clasificacionLegal = "J";
					codSociedad = "09";
					break;
				case 6:
					clasificacionLegal = "J";
					codSociedad = "09";
					break;
				case 7:
					clasificacionLegal = "J";
					codSociedad = "03";
					break;
				case 8:
					clasificacionLegal = "J";
					codSociedad = "09";
					break;
				case 9:
					clasificacionLegal = "N";
					codSociedad = "01";
					break;
				// TODO: caso alternativo si se
				// ! deben adicionar otras clasificaciones
				// case 10:
				// clasificacionLegal = "J";
				// codSociedad = "00N";
				// break;
				// case 11:
				// clasificacionLegal = "J";
				// codSociedad = "00R";
				// break;
				// case 12:
				// clasificacionLegal = "N";
				// codSociedad = "01";
				// break;
				default:
					break;
			}

			tProveedor = proveedor.getProveedor() != null ? proveedor.getProveedor() : "";
			tDireccion = proveedor.getDireccion() != null ? proveedor.getDireccion() : "";
			tTelefono = proveedor.getTelefono() != null ? proveedor.getTelefono() : "";
			tEmail = proveedor.getEmail() != null ? proveedor.getEmail() : "";

			switch (proveedor.getTipoIdentificacion().getId()) {
				case 1:
					tCodTipoIdentificacion = "13";
					break;
				case 2:
					tCodTipoIdentificacion = "22";
					break;
				case 8:
					tCodTipoIdentificacion = "31";
					break;
				case 3:
					tCodTipoIdentificacion = "41";
					break;
				case 6:
					tCodTipoIdentificacion = "42";
					break;
				default:
					break;
			}
			nidCiudad = proveedor.getCiudad().getId() != null ? proveedor.getCiudad().getId().toString() : "";
			String uri = "http://190.145.10.42:8080/api/v1/TApoloRestInterface/execute";
			logger.info(uri);
			Tercero proveedorMekano = new Tercero();
			proveedorMekano.setCLAVE(clave);
			proveedorMekano.setCODIGO(codigoProveedor);
			proveedorMekano.setDV(digitoVerificacion);
			proveedorMekano.setNATURALEZA(clasificacionLegal);
			proveedorMekano.setNOM1(nombre.toUpperCase());
			proveedorMekano.setNOM2(nombreD.toUpperCase());
			proveedorMekano.setAPL1(apellido.toUpperCase());
			proveedorMekano.setAPL2(apellidoD.toUpperCase());
			proveedorMekano.setEMPRESA(tProveedor.toUpperCase());
			proveedorMekano.setRAZON_COMERCIAL(tProveedor.toUpperCase());
			proveedorMekano.setDIRECCION(tDireccion);
			proveedorMekano.setTELEFONO(tTelefono);
			proveedorMekano.setMOVIL(tTelefono);
			proveedorMekano.setEMAIL(tEmail);
			proveedorMekano.setCODIGO_POSTAL("NA");
			proveedorMekano.setGERENTE("");
			proveedorMekano.setCODIDENTIDAD(tCodTipoIdentificacion);
			proveedorMekano.setCODSOCIEDAD(codSociedad);
			proveedorMekano.setCODPAIS("169");
			proveedorMekano.setCODACTIVIDAD("NA");
			proveedorMekano.setCODZONA("NA");
			if (nidCiudad.length() == 1) {
				proveedorMekano.setCODMUNICIPIO("000" + nidCiudad);
			} else if (nidCiudad.length() == 2) {
				proveedorMekano.setCODMUNICIPIO("00" + nidCiudad);
			} else if (nidCiudad.length() == 4) {
				proveedorMekano.setCODMUNICIPIO("0" + nidCiudad);
			} else {
				proveedorMekano.setCODMUNICIPIO(nidCiudad);
			}

			Gson gson = new Gson();
			String rjson = gson.toJson(proveedorMekano);
			logger.info(rjson);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
			HttpEntity<String> entity = new HttpEntity<String>(rjson, headers);
			RestTemplate rest = new RestTemplate();
			// String result = rest.postForObject(uri, entity, String.class);
			// logger.info(result);
		}
	}

	public ResponseEntity<Object> crearFacturaMekanoN(@RequestBody ContableM contable, Date fechaFactura)
			throws InterruptedException {
		logger.info("FacturaCompraService => ResponseEntity<Object>crearFacturaMekanoN => crearFacturaMekanoN:");
		if (contable != null) {
			logger.info(contable);
		}
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());

		Integer idSede = usuarioInformacion.getIdOrganizacion().getId();
		logger.info("FacturaCompraService => crearFacturaCompraMekano => idSede:" +
				idSede);
		// try {
		String uri = "http://190.145.10.42:8080/api/v1/TApoloRestInterface/execute";

		logger.info(contable.getFECHA());
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		String fech = contable.getFECHA().toString();
		logger.info(fech);
		logger.info(fechaFactura);
		// Date dataFormateada = formato.parse(fech);
		// logger.info(dataFormateada);
		// // logger.info(body.getFechaDocumento().toString());
		// Date fechaEntradaVenta = (Date) contable.getFECHA();
		Date fechaEntradaVenta = fechaFactura;
		Calendar calVent = Calendar.getInstance();
		calVent.setTime(fechaEntradaVenta);

		int yearVent = calVent.get(Calendar.YEAR);
		int monthVent = calVent.get(Calendar.MONTH) + 1;
		int dayVent = calVent.get(Calendar.DAY_OF_MONTH);

		Date fechaEntradaVence = fechaFactura;
		Calendar calVenc = Calendar.getInstance();
		calVenc.setTime(fechaEntradaVence);

		int yearVenc = calVenc.get(Calendar.YEAR);
		int monthVenc = calVenc.get(Calendar.MONTH) + 1;
		int dayVenc = calVenc.get(Calendar.DAY_OF_MONTH);

		String fechaVenta = dayVent + "." + monthVent + "." + yearVent;
		logger.info(fechaVenta);
		String fechaVence = dayVenc + "." + monthVenc + "." + yearVenc;
		logger.info(fechaVence);

		ContableM facturaMekano = new ContableM();
		facturaMekano.setCLAVE("Set_Contable_Primario");
		// logger.info(contable.getTIPO());
		facturaMekano.setTIPO(contable.getTIPO());
		facturaMekano.setPREFIJO("_");
		// logger.info(factura.getNroFactura().toString());
		facturaMekano.setNUMERO(contable.getNUMERO());
		facturaMekano.setFECHA(fechaVenta);
		// facturaMekano.setFECHA(fechaVenta);
		facturaMekano.setCUENTA(contable.getCUENTA());
		facturaMekano.setTERCERO(contable.getTERCERO());
		facturaMekano.setCENTRO(contable.getCENTRO());
		facturaMekano.setACTIVO("NA");
		facturaMekano.setEMPLEADO("NA");
		facturaMekano.setDEBITO(contable.getDEBITO());
		facturaMekano.setCREDITO(contable.getCREDITO());
		facturaMekano.setBASE("0");
		facturaMekano.setNOTA(contable.getNOTA());
		facturaMekano.setUSUARIO("SUPERVISOR");

		Gson gson = new Gson();
		String rjson = gson.toJson(facturaMekano);

		logger.info(rjson);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		HttpEntity<String> entity = new HttpEntity<String>(rjson, headers);
		logger.info(entity);
		RestTemplate rest = new RestTemplate();

		// if (entity != null && 1 != 1) {
		if (entity != null) {
			TimeUnit.SECONDS.sleep(2);
			String result = rest.postForObject(uri, entity, String.class);

			// if (result.contains("EL TERCERO")) {
			// if (result.contains("NO EXISTE")) {
			// logger.info("existe un error de tercero inexistente");
			// this.crearClienteMekano(contable.getTERCERO());
			// this.crearFacturaMekanoN(contable, fechaFactura);
			// }
			// }
			logger.info(result);

			// if (result.contains("LA REFERENCIA")) {
			// if (result.contains("NO EXISTE")) {
			// logger.info("existe un error de referencia inexistente");
			//
			// this.crearProductoMekano(factura.getFacArticulos().get(j).getArticulo().getProducto().getId());
			// this.crearFacturaMekanoN(factura, j);
			// }
			// }
			// logger.info(result);
		}
		// try {
		// factura.getFacturaCompra().setUsuarioMod(usuarioInformacion);
		// factura.getFacturaCompra().setIdSede(usuarioInformacion.getIdOrganizacion());
		// facturaCompraDao.save(factura.getFacturaCompra());
		// FacturaCompra facturaSave =
		// facturaCompraDao.findByNroFactura(factura.getFacturaCompra().getNroFactura(),
		// usuarioInformacion.getIdOrganizacion().getId());
		// for (FacturaRemision remision : factura.getFacturaRemision()) {
		// remision.setFacturaCompra(facturaSave);
		// remision.getRemision().setEstadoDocumento("Asignado");
		// remision.getRemision().setAsignado(1);
		// remisionDao.save(remision.getRemision());

		// // !termina consulta api contable fac compra
		// // List<ArticulosRemisionCompra> articulosRemisionCompra =
		// // facturaDao.obtenerArticulosRemisionCompra(remision.getId());
		// // for (ArticulosRemisionCompra articulo : articulosRemisionCompra) {
		// // if (facturaSave.getIdSede().getId() == 7 ||
		// facturaSave.getIdSede().getId()
		// // == 15) {
		// // logger.info("ingresa a la creacion de factura de compra desde la sede: " +
		// // facturaSave.getIdSede().getId());

		// // String uri =
		// "http://190.145.10.42:8080/api/v1/TApoloRestInterface/execute";

		// // logger.info(facturaSave.getFechaFactura().toString());
		// // logger.info(facturaSave.getFechaVencimiento().toString());
		// // // Date fechaEntradaVenta = facturaSave.getFechaFactura();
		// // // Date fechaEntradaVence = facturaSave.getFechaVencimiento();
		// // // String fechaVenta = fechaEntradaVenta.getDay() + "." +
		// // // fechaEntradaVenta.getMonth() + "."
		// // // + fechaEntradaVenta.getYear();
		// // // logger.info(fechaVenta);
		// // // String fechaVence = fechaEntradaVence.getDay() + "." +
		// // // fechaEntradaVence.getMonth() + "."
		// // // + fechaEntradaVence.getYear();
		// // // logger.info(fechaVence);
		// // Date fechaEntradaVenta = facturaSave.getFechaFactura();
		// // Calendar calVent = Calendar.getInstance();
		// // calVent.setTime(fechaEntradaVenta);

		// // int yearVent = calVent.get(Calendar.YEAR);
		// // int monthVent = calVent.get(Calendar.MONTH) + 1;
		// // int dayVent = calVent.get(Calendar.DAY_OF_MONTH);

		// // Date fechaEntradaVence = facturaSave.getFechaVencimiento();
		// // Calendar calVenc = Calendar.getInstance();
		// // calVenc.setTime(fechaEntradaVence);

		// // int yearVenc = calVenc.get(Calendar.YEAR);
		// // int monthVenc = calVenc.get(Calendar.MONTH) + 1;
		// // int dayVenc = calVenc.get(Calendar.DAY_OF_MONTH);

		// // String fechaVenta = dayVent + "." + monthVent + "." + yearVent;
		// // logger.info(fechaVenta);
		// // String fechaVence = dayVenc + "." + monthVenc + "." + yearVenc;
		// // logger.info(fechaVence);

		// // FacturaM facturaMekano = new FacturaM();
		// // facturaMekano.setCLAVE("Set_Gestion_Primario");
		// // facturaMekano.setTIPO("FC1");
		// // facturaMekano.setPREFIJO("FC1");
		// // logger.info(facturaSave.getNroFactura().toString());
		// // facturaMekano.setNUMERO(facturaSave.getNroFactura().toString());
		// // facturaMekano.setFECHA(fechaVenta);
		// // facturaMekano.setVENCE(fechaVence);
		// // facturaMekano.setTERCERO("75082596");
		// // //
		// facturaMekano.setVENDEDOR(factura.getIdVendedor().getNumeroDocumento());
		// // facturaMekano.setVENDEDOR("24347052");
		// // facturaMekano.setLISTA("NA");
		// // facturaMekano.setBANCO("CG");
		// // facturaMekano.setUSUARIO("SUPERVISOR");
		// // switch (facturaSave.getIdSede().getId()) {
		// // case 1:
		// // facturaMekano.setCENTRO("04");
		// // break;
		// // case 6:
		// // facturaMekano.setCENTRO("02");
		// // break;
		// // case 7:
		// // facturaMekano.setCENTRO("03");
		// // break;
		// // case 11:
		// // facturaMekano.setCENTRO("01");
		// // break;
		// // case 12:
		// // facturaMekano.setCENTRO("06");
		// // break;
		// // case 13:
		// // facturaMekano.setCENTRO("10");
		// // break;
		// // case 14:
		// // facturaMekano.setCENTRO("09");
		// // break;
		// // case 15:
		// // facturaMekano.setCENTRO("05");
		// // break;

		// // default:
		// // break;
		// // }
		// // facturaMekano.setBODEGA("NA");
		// //
		// facturaMekano.setREFERENCIA(articulo.getIdArticulo().getProducto().getCodigo());
		// // facturaMekano.setENTRADA(articulosRemisionCompra.size());
		// // facturaMekano.setSALIDA(0);
		// // logger.info(articulo.getIdArticulo().getPrecioCosto());
		// // if (articulo.getIdArticulo().getPrecioCosto() > 0.0) {
		// // facturaMekano
		// // .setUNITARIO(articulo.getIdArticulo().getPrecioCosto().intValue());
		// // } else {
		// // facturaMekano.setUNITARIO(0);
		// // }
		// // // !revisar el campo de observaciones porque con
		// factura.getObservaciones()
		// // sale
		// // // !error se envia valor por defecto
		// // // if (!factura.getObservaciones().equals(null)) {
		// // // facturaMekano.setNOTA(factura.getObservaciones());
		// // // } else {
		// // // facturaMekano.setNOTA("-");
		// // // }
		// // facturaMekano.setNOTA("-");

		// // Gson gson = new Gson();
		// // String rjson = gson.toJson(facturaMekano);

		// // logger.info(rjson);
		// // HttpHeaders headers = new HttpHeaders();
		// // headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		// // HttpEntity<String> entity = new HttpEntity<String>(rjson, headers);
		// // logger.info(entity);
		// // RestTemplate rest = new RestTemplate();

		// // if (entity != null) {
		// // String result = rest.postForObject(uri, entity, String.class);

		// // if (result.contains("EL TERCERO")) {
		// // if (result.contains("NO EXISTE")) {
		// // logger.info("existe un error de tercero inexistente");
		// // // this.crearClienteMekano(facturaSave.getProveedor());
		// // this.crearFacturaMekanoN(facturaSave, articulosRemisionCompra, articulo);
		// // }
		// // }
		// // logger.info(result);

		// // if (result.contains("LA REFERENCIA")) {
		// // if (result.contains("NO EXISTE")) {
		// // logger.info("existe un error de referencia inexistente");
		// // this.crearProductoMekano(articulo.getIdArticulo().getProducto().getId());
		// // this.crearFacturaMekanoN(facturaSave, articulosRemisionCompra, articulo);
		// // }
		// // }
		// // logger.info(result);
		// // }
		// // }
		// // }

		// }
		// facturaRemisionDao.saveAll(factura.getFacturaRemision());
		// RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Factura de
		// compra creada exitosamente");
		// respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		// } catch (Exception e) {
		// logger.error("Error al guardar la factura linea " +
		// e.getStackTrace()[0].getLineNumber() + " " + e);
		// RespuestaDto respuestaDto = new
		// RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
		// "Error al guardar la factura" + e);
		// respuesta = new ResponseEntity<>(respuestaDto,
		// HttpStatus.INTERNAL_SERVER_ERROR);
		// } finally {
		// logger.info("se ejecuta la sentencia finally");
		// // facturaRemisionDao.saveAll(factura.getFacturaRemision());
		// // RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Factura de
		// // compra creada exitosamente");
		// // respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		// }
		RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Factura de compra creada exitosamente");
		respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		// } catch (Exception e) {
		// // TODO: handle exception
		// logger.error("Error al guardar la factura linea " +
		// e.getStackTrace()[0].getLineNumber() + " " + e);
		// RespuestaDto respuestaDto = new
		// RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
		// "Error al guardar la factura" + e);
		// respuesta = new ResponseEntity<>(respuestaDto,
		// HttpStatus.INTERNAL_SERVER_ERROR);
		// }
		return respuesta;
	}

	// crearFacturaCompraMekano(ContableM body){
	// Authentication autenticacion =
	// SecurityContextHolder.getContext().getAuthentication();
	// Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
	// InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
	// .buscarPorIdUsuario(usuarioAutenticado.getId());

	// Integer idSede = usuarioInformacion.getIdOrganizacion().getId();
	// logger.info("FacturaCompraService => crearFacturaCompraMekano => idSede:"+
	// idSede);

	// String uri = "http://190.145.10.42:8080/api/v1/TApoloRestInterface/execute";

	// logger.info(body.getFechaDocumento().toString());
	// logger.info(body.getFechaDocumento().toString());
	// Date fechaEntradaVenta = body.getFechaDocumento();
	// Calendar calVent = Calendar.getInstance();
	// calVent.setTime(fechaEntradaVenta);

	// int yearVent = calVent.get(Calendar.YEAR);
	// int monthVent = calVent.get(Calendar.MONTH) + 1;
	// int dayVent = calVent.get(Calendar.DAY_OF_MONTH);

	// Date fechaEntradaVence = body.getFechaDocumento();
	// Calendar calVenc = Calendar.getInstance();
	// calVenc.setTime(fechaEntradaVence);

	// int yearVenc = calVenc.get(Calendar.YEAR);
	// int monthVenc = calVenc.get(Calendar.MONTH) + 1;
	// int dayVenc = calVenc.get(Calendar.DAY_OF_MONTH);

	// String fechaVenta = dayVent + "." + monthVent + "." + yearVent;
	// logger.info(fechaVenta);
	// String fechaVence = dayVenc + "." + monthVenc + "." + yearVenc;
	// logger.info(fechaVence);

	// ContableM facturaMekano = new ContableM();
	// String tipoDocumento = "E1";
	// facturaMekano.setCLAVE("Set_Contable_Primario");
	// switch (idSede) {
	// case 1:
	// tipoDocumento = "E4";
	// break;
	// case 6:
	// tipoDocumento = "E2";
	// break;
	// case 7:
	// tipoDocumento = "E3";
	// break;
	// case 11:
	// tipoDocumento = "E1";
	// break;
	// case 12:
	// tipoDocumento = "E6";
	// break;
	// case 13:
	// tipoDocumento = "E10";
	// break;
	// case 14:
	// tipoDocumento = "E9";
	// break;
	// case 15:
	// tipoDocumento = "E5";
	// break;

	// default:
	// break;
	// }
	// facturaMekano.setTIPO(tipoDocumento);
	// facturaMekano.setPREFIJO("_");
	// // logger.info(factura.getNroFactura().toString());
	// facturaMekano.setNUMERO(body.getNumeroDocumento().toString());
	// facturaMekano.setFECHA(fechaVenta);
	// facturaMekano.setCUENTA(body.getSaldo().toString());
	// facturaMekano.setTERCERO("12345678");
	// switch (idSede) {
	// case 1:
	// facturaMekano.setCENTRO("04");
	// break;
	// case 6:
	// facturaMekano.setCENTRO("02");
	// break;
	// case 7:
	// facturaMekano.setCENTRO("03");
	// break;
	// case 11:
	// facturaMekano.setCENTRO("01");
	// break;
	// case 12:
	// facturaMekano.setCENTRO("06");
	// break;
	// case 13:
	// facturaMekano.setCENTRO("10");
	// break;
	// case 14:
	// facturaMekano.setCENTRO("09");
	// break;
	// case 15:
	// facturaMekano.setCENTRO("05");
	// break;

	// default:
	// break;
	// }
	// facturaMekano.setACTIVO("NA");
	// facturaMekano.setEMPLEADO("NA");
	// facturaMekano.setDEBITO("0");
	// facturaMekano.setCREDITO("0");
	// facturaMekano.setBASE("0");
	// facturaMekano.setNOTA("-");
	// facturaMekano.setUSUARIO("SUPERVISOR");

	// Gson gson = new Gson();
	// String rjson = gson.toJson(facturaMekano);

	// logger.info(rjson);
	// HttpHeaders headers = new HttpHeaders();
	// headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
	// HttpEntity<String> entity = new HttpEntity<String>(rjson, headers);
	// logger.info(entity);
	// RestTemplate rest = new RestTemplate();

	// if (entity != null) {
	// String result = rest.postForObject(uri, entity, String.class);

	// if (result.contains("EL TERCERO")) {
	// if (result.contains("NO EXISTE")) {
	// logger.info("existe un error de tercero inexistente");
	// // this.crearClienteMekano(factura.getIdCliente());
	// // this.crearFacturaMekanoN(factura, j);
	// }
	// }
	// logger.info(result);

	// // if (result.contains("LA REFERENCIA")) {
	// // if (result.contains("NO EXISTE")) {
	// // logger.info("existe un error de referencia inexistente");
	// //
	// this.crearProductoMekano(factura.getFacArticulos().get(j).getArticulo().getProducto().getId());
	// // this.crearFacturaMekanoN(factura, j);
	// // }
	// // }
	// // logger.info(result);
	// }
	// }

	// private void crearFacturaMekanoN(@RequestBody ContableM contable) {
	// // if (factura.getIdTipoVenta().getId() == 15) {
	// String uri = "http://190.145.10.42:8080/api/v1/TApoloRestInterface/execute";
	// logger.info(facturaSave.getFechaFactura().toString());
	// logger.info(facturaSave.getFechaVencimiento().toString());
	// Date fechaEntradaVenta = facturaSave.getFechaFactura();
	// Date fechaEntradaVence = facturaSave.getFechaVencimiento();
	// String fechaVenta = fechaEntradaVenta.getDay() + "." +
	// fechaEntradaVenta.getMonth() + "."
	// + fechaEntradaVenta.getYear();
	// logger.info(fechaVenta);
	// String fechaVence = fechaEntradaVence.getDay() + "." +
	// fechaEntradaVence.getMonth() + "."
	// + fechaEntradaVence.getYear();
	// logger.info(fechaVence);

	// FacturaM facturaMekano = new FacturaM();
	// facturaMekano.setCLAVE("Set_Gestion_Primario");
	// facturaMekano.setTIPO("FC1");
	// facturaMekano.setPREFIJO("FC1");
	// logger.info(facturaSave.getNroFactura().toString());
	// facturaMekano.setNUMERO(facturaSave.getNroFactura().toString());
	// facturaMekano.setFECHA(fechaVenta);
	// facturaMekano.setVENCE(fechaVence);
	// facturaMekano.setTERCERO("75082596");
	// // facturaMekano.setVENDEDOR(factura.getIdVendedor().getNumeroDocumento());
	// // se envia un valor por defecto para vendedores porque la api e integracion
	// de
	// // usuarios no estaba en el alcance de la integracion con mekano
	// facturaMekano.setVENDEDOR("24347052");
	// facturaMekano.setLISTA("NA");
	// facturaMekano.setBANCO("CG");
	// // se envia un valor por defecto para usuario en sesion porque la api e
	// // integracion de
	// // usuarios no estaba en el alcance de la integracion con mekano
	// facturaMekano.setUSUARIO("SUPERVISOR");
	// switch (facturaSave.getIdSede().getId()) {
	// case 1:
	// facturaMekano.setCENTRO("04");
	// break;
	// case 6:
	// facturaMekano.setCENTRO("02");
	// break;
	// case 7:
	// facturaMekano.setCENTRO("03");
	// break;
	// case 11:
	// facturaMekano.setCENTRO("01");
	// break;
	// case 12:
	// facturaMekano.setCENTRO("06");
	// break;
	// case 13:
	// facturaMekano.setCENTRO("10");
	// break;
	// case 14:
	// facturaMekano.setCENTRO("09");
	// break;
	// case 15:
	// facturaMekano.setCENTRO("05");
	// break;

	// default:
	// break;
	// }
	// facturaMekano.setBODEGA("NA");
	// facturaMekano.setREFERENCIA(articulo.getIdArticulo().getProducto().getCodigo());
	// facturaMekano.setENTRADA(articulosRemisionCompra.size());
	// facturaMekano.setSALIDA(0);
	// logger.info(articulo.getIdArticulo().getPrecioCosto());
	// if (articulo.getIdArticulo().getPrecioCosto() > 0.0) {
	// facturaMekano
	// .setUNITARIO(articulo.getIdArticulo().getPrecioCosto().intValue());
	// } else {
	// facturaMekano.setUNITARIO(0);
	// }
	// // !revisar el campo de observaciones porque con factura.getObservaciones()
	// sale
	// // !error se envia valor por defecto
	// // if (!factura.getObservaciones().equals(null)) {
	// // facturaMekano.setNOTA(factura.getObservaciones());
	// // } else {
	// // facturaMekano.setNOTA("-");
	// // }
	// facturaMekano.setNOTA("-");

	// Gson gson = new Gson();
	// String rjson = gson.toJson(facturaMekano);

	// logger.info(rjson);
	// HttpHeaders headers = new HttpHeaders();
	// headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
	// HttpEntity<String> entity = new HttpEntity<String>(rjson, headers);
	// logger.info(entity);
	// RestTemplate rest = new RestTemplate();

	// if (entity != null) {
	// String result = rest.postForObject(uri, entity, String.class);
	// if (result.contains("LA REFERENCIA")) {
	// if (result.contains("NO EXISTE")) {
	// logger.info("existe un error de referencia inexistente");
	// this.crearProductoMekano(articulo.getIdArticulo().getProducto().getId());
	// this.crearFacturaMekanoN(facturaSave, articulosRemisionCompra, articulo);
	// }
	// }
	// logger.info(result);
	// }
	// // }
	// }

	// private void crearProductoMekano(Integer idI) {

	// String codigo = "";
	// String codigoD = "";
	// String nombre = "";
	// String nombreD = "";
	// String costo = "0.0";
	// String precio = "0.0";
	// String iva = "E19";
	// String ivaV = "";
	// String medida = "MTR";
	// // Double costo = ArticuloDao.obtenerUltimoPrecioCosto(body.getId(), idSede);

	// try {
	// List<Producto> productos = productoService.listarTodos(idI);
	// logger.info("ingresa a productos listar-todos");
	// logger.info(productos.size());
	// if (productos.size() > 0 && productos.get(0) != null) {
	// logger.info(productos.size());
	// codigo = productos.get(0).getCodigo() != null ? productos.get(0).getCodigo()
	// : "";
	// nombre = productos.get(0).getTipo().getTTipo() + " " +
	// productos.get(0).getReferencia().getTreferencia() + " "
	// + productos.get(0).getPresentacion().getTPresentacion() + " " +
	// productos.get(0).getColor().getTColor();
	// if (productos.get(0).getPrecios().size() > 0) {
	// costo = productos.get(0).getPrecios().get(0).getPrecioCosto() != null
	// ? productos.get(0).getPrecios().get(0).getPrecioCosto().toString()
	// : "0.0";
	// precio = productos.get(0).getPrecios().get(0).getPrecioVenta() != null
	// ? productos.get(0).getPrecios().get(0).getPrecioVenta().toString()
	// : "0.0";
	// }
	// ivaV = productos.get(0).getIva() + "";
	// logger.info("el iva es de:");
	// logger.info(ivaV);

	// if (ivaV.equals("0.0")) {
	// iva = "E0";
	// } else if (ivaV.equals("19.0")) {
	// iva = "E19";
	// } else if (ivaV.equals("5.0")) {
	// iva = "E5";
	// }

	// if (productos.get(0).getUnidad().getId() == 1) {
	// logger.info("la unidad es el metro");
	// medida = "MTR";
	// } else if (productos.get(0).getUnidad().getId() == 2) {
	// logger.info("la unidad es el unidad");
	// medida = "UND";
	// } else if (productos.get(0).getUnidad().getId() == 3) {
	// logger.info("la unidad es el paquete");
	// medida = "PAQ";
	// }

	// logger.info(iva);
	// logger.info(medida);

	// String uri = "http://190.145.10.42:8080/api/v1/TApoloRestInterface/execute";

	// ReferenciaProd referenciaMekano = new ReferenciaProd();
	// referenciaMekano.setCLAVE("Set_Referencias");
	// referenciaMekano.setCODIGO(codigo);
	// referenciaMekano.setCODIGO2(codigoD);
	// referenciaMekano.setNOMBRE(nombre.toUpperCase());
	// referenciaMekano.setNOMBRE2(nombreD.toUpperCase());
	// referenciaMekano.setCOSTO(costo);
	// referenciaMekano.setPRECIO(precio);
	// referenciaMekano.setCODLINEA("PROD");
	// referenciaMekano.setCODMEDIDA(medida);
	// referenciaMekano.setCOD_ESQIMPUESTO(iva);
	// referenciaMekano.setCOD_ESQRETENCION("ECOM1");
	// referenciaMekano.setCOD_ESQCONTABLE("ESQ");

	// Gson gson = new Gson();
	// String rjson = gson.toJson(referenciaMekano);
	// logger.info(rjson);
	// HttpHeaders headers = new HttpHeaders();
	// headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
	// HttpEntity<String> entity = new HttpEntity<String>(rjson, headers);
	// logger.info(entity);
	// RestTemplate rest = new RestTemplate();
	// if (entity != null) {
	// String result = rest.postForObject(uri, entity, String.class);
	// logger.info(result);
	// }
	// }
	// } catch (Exception ex) {
	// String msj = "Error creando producto...";
	// logger.error(msj + "\n");
	// logger.error(ex);
	// }
	// }

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN')")
	public ResponseEntity<Object> Actualizar(@RequestBody FacturaCompraRemisionRequest factura) {
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());

		try {
			factura.getFacturaCompra().setUsuarioMod(usuarioInformacion);
			List<FacturaRemision> remisiones = facturaRemisionDao.obtenerRemisiones(factura.getFacturaCompra().getId());
			for (FacturaRemision remision : remisiones) {
				remision.getRemision().setEstadoDocumento("Pendiente");
				remision.getRemision().setAsignado(0);
				remisionDao.save(remision.getRemision());
			}
			facturaCompraDao.save(factura.getFacturaCompra());
			facturaRemisionDao.eliminarPorFacturaCompra(factura.getFacturaCompra().getId());
			for (FacturaRemision remision : factura.getFacturaRemision()) {
				remision.setFacturaCompra(factura.getFacturaCompra());
				remision.getRemision().setEstadoDocumento("Asignado");
				remision.getRemision().setAsignado(1);
				remisionDao.save(remision.getRemision());
			}
			facturaRemisionDao.saveAll(factura.getFacturaRemision());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Factura de compra actualizada exitosamente");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error al actualizar registro" + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al actualizar la factura" + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN')")
	public ResponseEntity<Object> eliminar(Integer idFactura) {
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		try {
			FacturaCompra factura = facturaCompraDao.findById(idFactura).orElse(null);
			List<FacturaRemision> remisiones = facturaRemisionDao.obtenerRemisiones(factura.getId());
			facturaRemisionDao.eliminarPorFacturaCompra(factura.getId());
			for (FacturaRemision remision : remisiones) {
				remision.getRemision().setEstadoDocumento("Pendiente");
				remision.getRemision().setAsignado(0);
				remisionDao.save(remision.getRemision());
			}
			facturaCompraDao.delete(factura);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Factura de compra eliminada exitosamente");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error al eliminar registro" + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al eliminar la factura" + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN')")
	public ResponseEntity<Object> listarNumeroFacturas() {
		ResponseEntity<Object> respuesta;
		try {
			List<String> numerofacturas = facturaCompraDao.listarNumeroFacturas();
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "listado de facturas exitoso");
			respuestaDto.setObjetoRespuesta(numerofacturas);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error("Error al listar facturas" + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al listar facturas" + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN')")
	public ResponseEntity<Object> listarFacturas(Integer idProveedor) {
		ResponseEntity<Object> respuesta;
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		try {
			List<FacturaCompra> facturas = facturaCompraDao.listarFacturasProveedor(idProveedor,
					usuarioInformacion.getIdOrganizacion().getId());
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "listado de facturas exitoso");
			respuestaDto.setObjetoRespuesta(facturas);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error("Error al listar facturas" + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al listar facturas" + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN')")
	public ResponseEntity<Object> ObtenerFacturasEstadosCuenta(Integer idProveedor, Integer page) {
		ResponseEntity<Object> respuesta;
		try {
			Pageable pageConfig = PageRequest.of(page, 10);
			Page<FacturaCompra> numerofacturas = facturaCompraDao.obtenerFacturasPendientesProveedor(idProveedor,
					pageConfig);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "listado de facturas exitoso");
			respuestaDto.setObjetoRespuesta(numerofacturas);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error("Error al listar facturas" + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al listar facturas" + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN')")
	public ResponseEntity<Object> ObtenerTotalFacturasEstadosCuenta(Integer idProveedor) {
		ResponseEntity<Object> respuesta;
		try {
			Integer total = facturaCompraDao.obtenerTotalFacturasPendientesProveedor(idProveedor);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "listado de facturas exitoso");
			respuestaDto.setObjetoRespuesta(total);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error("Error al listar facturas" + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al listar facturas" + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN')")
	public ResponseEntity<Object> ObtenerFacturasEstadosCuentaSede(Integer page) {
		ResponseEntity<Object> respuesta;
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		try {
			Pageable pageConfig = PageRequest.of(page, 10);
			Page<FacturaCompra> numerofacturas = facturaCompraDao
					.obtenerFacturasPorPagarSede(usuarioInformacion.getId(), pageConfig);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "listado de facturas exitoso");
			respuestaDto.setObjetoRespuesta(numerofacturas);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error("Error al listar facturas" + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al listar facturas" + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN')")
	public ResponseEntity<Object> listarNumeroFacturasConsulta(String numeroFactura, String fechaInicial,
			String fechaFinal, Integer estado, Integer proveedor, Integer page) {
		ResponseEntity<Object> respuesta;
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		Pageable paging = PageRequest.of(page, 8);
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		Integer total = 0;
		Integer idSede = usuarioInformacion.getIdOrganizacion().getId();
		try {
			List<FacturaCompra> facturas;
			StringBuilder query = new StringBuilder();
			StringBuilder queryCantidad = new StringBuilder();
			generarQuery(query, idSede, fechaInicial, fechaFinal, estado,
					numeroFactura, proveedor);
			TypedQuery<FacturaCompra> facturasInfoQuery = (TypedQuery<FacturaCompra>) entityManager
					.createNativeQuery(query.toString(), FacturaCompra.class);
			int pageNumber = paging.getPageNumber();
			int pageSize = paging.getPageSize();
			facturasInfoQuery.setFirstResult(pageNumber * pageSize);
			facturasInfoQuery.setMaxResults(pageSize);
			facturas = facturasInfoQuery.getResultList();
			for (FacturaCompra facturaCompra : facturas) {
				List<FacturaRetenciones> retenciones = facturaRetencionesDao
						.obtenerRetencionesFactura(facturaCompra.getId());
				facturaCompra.setListRetenciones(retenciones);
				List<FacturaImpuestos> impuestos = facturaImpuestosDao.obtenerImpuestosFactura(facturaCompra.getId());
				facturaCompra.setListOtrosImpuestos(impuestos);
				total = total + facturaCompra.getTotal();

			}
			generarQueryCantidadDatos(queryCantidad, idSede, fechaInicial,
					fechaFinal, estado, numeroFactura, proveedor);
			Query cantidad = entityManager.createNativeQuery(queryCantidad.toString());
			BigInteger countResult = (BigInteger) cantidad.getSingleResult();
			Integer cantidadTotal = countResult.intValue();
			Page<FacturaCompra> result = new PageImpl<FacturaCompra>(facturas, paging, cantidadTotal);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de facturas exitosa");
			respuestaDto.setObjetoRespuesta(result);
			respuestaDto.setSuma(total);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error("Error obteniendo facturas " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error obteniendo facturas");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER')")
	public ResponseEntity<Object> obtenerNumero(Integer idSede) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		try {
			List<FacturaCompra> facturaC = this.facturaCompraDao.findByIdSede(idSede);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de factura de compra exitosa");
			respuestaDto.setObjetoRespuesta(facturaC);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo factura de compra " + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error obteniendo factura de compra");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return respuesta;

	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER')")
	public ResponseEntity<Object> obtenerNumeroFactura(Integer idSede, Integer idProveedor) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		try {
			List<FacturaCompra> facturaC = this.facturaCompraDao.findByIdSedeProveedor(idSede, idProveedor);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de factura de compra exitosa");
			respuestaDto.setObjetoRespuesta(facturaC);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo factura de compra " + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error obteniendo factura de compra");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return respuesta;

	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER')")
	public ResponseEntity<Object> disminuirTotal(@RequestBody FacturaCompra factura) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Integer nuevoTotal = 0;

		try {
			FacturaCompra facturaV = this.facturaCompraDao.findByIdFactura(factura.getId());

			if (factura.getId() == facturaV.getId()) {
				nuevoTotal = facturaV.getTotal() - factura.getTotal();
				factura.setTotal(nuevoTotal);

				FacturaCompra actualTotal = this.facturaCompraDao.save(factura);

				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito actualizando valor");
				respuestaDto.setObjetoRespuesta(actualTotal);
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
			} else {
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, "Error, la factura no existe");
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			logger.error("Error en la actualizacion del valor");
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error en la creación de la nota crédito");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return respuesta;
	}

	private void generarQuery(StringBuilder query, Integer idSede, String fechaInicio, String fechaFin, Integer estado,
			String numeroFactura, Integer proveedor) {

		query.append("select * FROM fc_factura_compra ");

		if (!fechaInicio.equals("null") && fechaFin.equals("null")) {
			query.append(" where date_format(date(d_fecha_factura),'%Y-%m-%d')" + "= date('" + fechaInicio + "')");
		} else if (fechaInicio.equals("null") && !fechaFin.equals("null")) {
			query.append(" where date_format(date(d_fecha_factura),'%Y-%m-%d')" + "<= date('" + fechaFin + "')");
		} else if (!fechaInicio.equals("null") && !fechaFin.equals("null")) {
			query.append(" where date_format(date(d_fecha_factura),'%Y-%m-%d')" + "between " + "date('" + fechaInicio
					+ "') and " + "date('" + fechaFin + "')");
		}
		if (!numeroFactura.equals("null") && (!fechaInicio.equals("null") || !fechaFin.equals("null"))) {
			query.append(" and t_nro_factura='" + numeroFactura + "'");
		} else if (!numeroFactura.equals("null")) {
			query.append(" where t_nro_factura='" + numeroFactura + "'");
		}

		if (proveedor != 0
				&& (!fechaInicio.equals("null") || !fechaFin.equals("null") || !numeroFactura.equals("null"))) {
			query.append(" and nid_proveedor=" + proveedor);
		} else if (proveedor != 0) {
			query.append(" where nid_proveedor=" + proveedor);
		}

		if (estado != 0 && (!fechaInicio.equals("null") || !fechaFin.equals("null") || !numeroFactura.equals("null")
				|| proveedor != 0)) {
			query.append(" and cod_estado_con = " + estado);
		} else if (estado != 0) {
			query.append(" where cod_estado_con = " + estado);
		}

		if (idSede != 0 && (!fechaInicio.equals("null") || !fechaFin.equals("null") || !numeroFactura.equals("null")
				|| proveedor != 0 || estado != 0)) {
			query.append(" and id_sede = " + idSede);
		} else if (idSede != 0) {
			query.append(" where id_sede = " + idSede);
		}
		query.append(" order by d_fecha_factura desc");

	}

	private void generarQueryCantidadDatos(StringBuilder query, Integer idSede, String fechaInicio, String fechaFin,
			Integer estado, String numeroFactura, Integer proveedor) {

		// query.append("select count(*) FROM fc_factura_compra where id_sede=");
		query.append("select count(*) FROM fc_factura_compra ");

		if (!fechaInicio.equals("null") && fechaFin.equals("null")) {
			query.append(" where date_format(date(d_fecha_factura),'%Y-%m-%d')" + "= date('" + fechaInicio + "')");
		} else if (fechaInicio.equals("null") && !fechaFin.equals("null")) {
			query.append(" where date_format(date(d_fecha_factura),'%Y-%m-%d')" + "<= date('" + fechaFin + "')");
		} else if (!fechaInicio.equals("null") && !fechaFin.equals("null")) {
			query.append(" where date_format(date(d_fecha_factura),'%Y-%m-%d')" + "between " + "date('" + fechaInicio
					+ "') and " + "date('" + fechaFin + "')");
		}
		if (!numeroFactura.equals("null") && (!fechaInicio.equals("null") || !fechaFin.equals("null"))) {
			query.append(" and t_nro_factura='" + numeroFactura + "'");
		} else if (!numeroFactura.equals("null")) {
			query.append(" where t_nro_factura='" + numeroFactura + "'");
		}

		if (proveedor != 0
				&& (!fechaInicio.equals("null") || !fechaFin.equals("null") || !numeroFactura.equals("null"))) {
			query.append(" and nid_proveedor=" + proveedor);
		} else if (proveedor != 0) {
			query.append(" where nid_proveedor=" + proveedor);
		}

		if (estado != 0 && (!fechaInicio.equals("null") || !fechaFin.equals("null") || !numeroFactura.equals("null")
				|| proveedor != 0)) {
			query.append(" and cod_estado_con = " + estado);
		} else if (estado != 0) {
			query.append(" where cod_estado_con = " + estado);
		}

		if (idSede != 0 && (!fechaInicio.equals("null") || !fechaFin.equals("null") || !numeroFactura.equals("null")
				|| proveedor != 0 || estado != 0)) {
			query.append(" and id_sede = " + idSede);
		} else if (idSede != 0) {
			query.append(" where id_sede = " + idSede);
		}
		query.append(" order by d_fecha_factura desc");

	}

	public List<FacturaCompra> facturasPorProveedor(Integer idProveedor, Date fechaInicial, Date fechaFinal,
			Integer idSede) {
		List<FacturaCompra> lista = facturaCompraDao.busarPorProveedorYFecha(idProveedor, fechaInicial, fechaFinal,
				idSede);
		return lista;
	}

	public List<FacturaCompra> facturasPorProveedor(Integer idProveedor, Integer idSede) {
		List<FacturaCompra> lista = facturaCompraDao.busarPorProveedorPendiente(idProveedor, idSede);
		return lista;
	}

	// facturas vencidas por estados
	public List<FacturaCompra> listarinformeFacturasVencidasCompra(Integer idSede) {
		List<FacturaCompra> lista = facturaCompraDao.listarinformeFacturasVencidasCompraPaginado(idSede);
		return lista;
	}

	public List<FacturaCompra> listarinformeFacturasVencidasCompraFiltros(Integer idSede, Integer idProveedor,
			String fechaInicial, String fechaFinal) {
		List<FacturaCompra> facturas;
		Integer suma = 0;
		StringBuilder query = new StringBuilder();
		generarQueryInformeFacturasCompraVencida(query, idSede, fechaInicial, fechaFinal, idProveedor);
		TypedQuery<FacturaCompra> facturasInfoQuery = (TypedQuery<FacturaCompra>) entityManager
				.createNativeQuery(query.toString(), FacturaCompra.class);
		facturas = facturasInfoQuery.getResultList();

		return facturas;
	}

	private void generarQueryInformeFacturasCompraVencida(StringBuilder query, Integer idSede, String fechaInicio,
			String fechaFin, Integer proveedor) {

		query.append("select * FROM fc_factura_compra where id_sede=");

		query.append("" + idSede);
		if (!fechaInicio.equals("null") && fechaFin.equals("null")) {
			query.append(" and date_format(date(d_fecha_factura),'%Y-%m-%d')" + "= date('" + fechaInicio + "')");
		} else if (fechaInicio.equals("null") && !fechaFin.equals("null")) {
			query.append(" and date_format(date(d_fecha_factura),'%Y-%m-%d')" + "<= date('" + fechaFin + "')");
		} else if (!fechaInicio.equals("null") && !fechaFin.equals("null")) {
			query.append(" and date_format(date(d_fecha_factura),'%Y-%m-%d')" + "between " + "date('" + fechaInicio
					+ "') and " + "date('" + fechaFin + "')");
		}
		if (proveedor != 0) {
			query.append(" and nid_proveedor=" + proveedor);
		}
		query.append(" and cod_estado_con in (2,5,1)");
		// query.append(" and DATEDIFF(d_fecha_vencimiento,CURDATE()) < 0");
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN')")
	public ResponseEntity<Object> ObtenerFacturasMes(Integer page) {
		ResponseEntity<Object> respuesta;
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		Integer total = 0;
		try {
			Pageable pageConfig = PageRequest.of(page, 10);
			Page<FacturaCompra> numerofacturas = facturaCompraDao
					.obtenerFacturasDelMes(usuarioInformacion.getIdOrganizacion().getId(), pageConfig);

			for (FacturaCompra facturaCompra : numerofacturas) {
				List<FacturaRetenciones> retenciones = facturaRetencionesDao
						.obtenerRetencionesFactura(facturaCompra.getId());
				facturaCompra.setListRetenciones(retenciones);
				List<FacturaImpuestos> impuestos = facturaImpuestosDao.obtenerImpuestosFactura(facturaCompra.getId());
				facturaCompra.setListOtrosImpuestos(impuestos);
				total = total + facturaCompra.getTotal();
			}
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "listado de facturas exitoso");
			respuestaDto.setObjetoRespuesta(numerofacturas);
			respuestaDto.setSuma(total);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error("Error al listar facturas" + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al listar facturas" + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN')")
	public ResponseEntity<Object> ObtenerTodasFacturasEstadosCuenta(Integer page) {
		ResponseEntity<Object> respuesta;
		try {
			Pageable pageConfig = PageRequest.of(page, 10);
			Page<FacturaCompra> numerofacturas = facturaCompraDao.obtenerTodasFacturasPendientesProveedor(pageConfig);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "listado de facturas exitoso");
			int suma = facturaCompraDao.obtenerSumaTodasFacturasPendientesProveedor();
			respuestaDto.setObjetoRespuesta(numerofacturas);
			respuestaDto.setVariable(String.valueOf(suma));
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error("Error al listar facturas" + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al listar facturas" + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('SUPER')")
	public ResponseEntity<Object> ObtenerFacturasAnular(String numero) {
		ResponseEntity<Object> respuesta;
		try {
			List<FacturaCompra> numerofacturas = facturaCompraDao.obtenerFacturasAnular(numero);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "listado de facturas exitoso");
			int suma = facturaCompraDao.obtenerSumaTodasFacturasPendientesProveedor();
			respuestaDto.setObjetoRespuesta(numerofacturas);
			respuestaDto.setVariable(String.valueOf(suma));
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error("Error al listar facturas" + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al listar facturas" + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN')")
	public ResponseEntity<Object> anular(FacturaCompra factura) {
		logger.info(factura);
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		try {
			logger.info(factura);
			List<FacturaRemision> remisiones = facturaRemisionDao.obtenerRemisiones(factura.getId());
			facturaRemisionDao.eliminarPorFacturaCompra(factura.getId());
			for (FacturaRemision remision : remisiones) {
				remision.getRemision().setEstadoDocumento("Pendiente");
				remision.getRemision().setAsignado(0);
				remisionDao.save(remision.getRemision());
			}
			EstadoDocumento estado = estadoDocumento.findById(3).orElse(null);
			factura.setCodEstadoCon(estado);
			facturaCompraDao.save(factura);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Factura de compra anulada exitosamente");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error al anular registro" + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al anular la factura" + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	public List<FacturaCompra> facturasPorProveedorActualizar(Integer idProveedor, Integer idComprobante,
			Integer idSede) {
		List<FacturaCompra> lista = facturaCompraDao.busarPorProveedorPendienteActualizar(idProveedor, idComprobante,
				idSede);
		return lista;
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN')")
	public ResponseEntity<Object> ObtenerFacturasPendientes(Integer idProveedor, Integer page) {
		ResponseEntity<Object> respuesta;
		try {
			Pageable pageConfig = PageRequest.of(page, 10);
			Page<FacturaCompra> numerofacturas = facturaCompraDao.obtenerFacturasPendientesProveedor(idProveedor,
					pageConfig);
			for (FacturaCompra facturaCompra : numerofacturas) {
				List<FacturaRetenciones> retenciones = facturaRetencionesDao
						.obtenerRetencionesFactura(facturaCompra.getId());
				facturaCompra.setListRetenciones(retenciones);
				List<FacturaImpuestos> impuestos = facturaImpuestosDao.obtenerImpuestosFactura(facturaCompra.getId());
				facturaCompra.setListOtrosImpuestos(impuestos);
			}
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "listado de facturas exitoso");
			respuestaDto.setObjetoRespuesta(numerofacturas);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error("Error al listar facturas" + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al listar facturas" + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN')")
	public ResponseEntity<Object> ActualizarRetencion(@RequestBody FacturaCompra factura) {
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		try {
			facturaRetencionDao.borrarRetencionesFactura(factura.getId());
			facturaCompraDao.save(factura);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Factura de compra actualizada exitosamente");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error al actualizar registro" + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al actualizar la factura" + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

}
