package com.softlond.base.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

// import com.google.api.client.http.HttpHeaders;
// import com.google.common.net.MediaType;
import com.google.gson.Gson;
import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.ArticulosRemisionCompra;
import com.softlond.base.entity.AsignacionRecibo;
import com.softlond.base.entity.Clientes;
import com.softlond.base.entity.ComprobanteEgreso;
import com.softlond.base.entity.ConceptoReciboEgreso;
import com.softlond.base.entity.ConceptosReciboCaja;
import com.softlond.base.entity.DescuentoComprobanteEgreso;
import com.softlond.base.entity.EstadoDocumento;
import com.softlond.base.entity.Factura;
import com.softlond.base.entity.FacturaArticulos;
import com.softlond.base.entity.FacturaCompra;
import com.softlond.base.entity.NotaDebitoCliente;
import com.softlond.base.entity.Organizacion;
import com.softlond.base.entity.PagoComprobanteEgreso;
import com.softlond.base.entity.PagosReciboCaja;
import com.softlond.base.entity.Prefijo;
import com.softlond.base.entity.ReciboCajaVenta;
import com.softlond.base.entity.RemisionVenta;
import com.softlond.base.entity.RetencionComprobanteEgreso;
import com.softlond.base.entity.Secundario;
import com.softlond.base.entity.Sequence;
import com.softlond.base.entity.Tercero;
import com.softlond.base.entity.Usuario;
import com.softlond.base.repository.AsignacionReciboDao;
import com.softlond.base.repository.ConceptosReciboCajaDao;
import com.softlond.base.repository.EstadoDocumentoDao;
import com.softlond.base.repository.FacturaDao;
import com.softlond.base.repository.NotaCreditoClienteDao;
import com.softlond.base.repository.NotaDebitoClienteDao;
import com.softlond.base.repository.PagosReciboCajaDao;
import com.softlond.base.repository.PrefijoDao;
import com.softlond.base.repository.ReciboCajaVentaDao;
import com.softlond.base.repository.RetencionReciboCajaDao;
import com.softlond.base.repository.SequenceDao;
import com.softlond.base.response.ReciboDocumentoPago;
import com.softlond.base.entity.InformacionUsuario;
import com.softlond.base.repository.UsuarioInformacionDao;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@Service
public class ReciboCajaVentaService {

	private static final Logger logger = Logger.getLogger(ReciboCajaVentaService.class);

	@Autowired
	ReciboCajaVentaDao reciboCajaDao;

	@Autowired
	private FacturaDao facturaDao;

	@Autowired
	private SequenceDao sequenceDao;

	@Autowired
	ConceptosReciboCajaDao conceptosReciboCajaDao;

	@Autowired
	private PrefijoDao prefijoDao;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private PagosReciboCajaDao pagosDao;

	@Autowired
	private ConceptosReciboCajaDao conceptosRecibosDao;

	@Autowired
	private RetencionReciboCajaDao retencionDao;

	@Autowired
	private AsignacionReciboDao asignacionDao;

	@Autowired
	private NotaDebitoClienteDao notaDebitoDao;

	@Autowired
	private NotaCreditoClienteDao notaCreditoDao;

	@Autowired
	UsuarioInformacionDao usuarioInformacionDao;

	@Autowired
	private EstadoDocumentoDao estadoDao;

	// Crear recibos de caja venta
	// @PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or
	// hasAuthority('USER') or hasAuthority('VENDEDOR')")
	// public ResponseEntity<Object> guardarRecibo(@RequestBody ReciboCajaVenta
	// reciboNuevo) {

	// ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

	// Authentication autenticacion =
	// SecurityContextHolder.getContext().getAuthentication();
	// Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();

	// try {
	// Prefijo prefijo =
	// prefijoDao.obtenerPrefijoPorNombreSede(reciboNuevo.getIdSede().getId(),
	// reciboNuevo.getPrefijo());
	// reciboNuevo.setFechaPago(new Date(new java.util.Date().getTime()));
	// reciboNuevo.setIdCreador(usuarioAutenticado);
	// // this.reciboCajaDao.save(reciboNuevo);
	// boolean conceptoAsignacion = false;
	// logger.info(reciboNuevo.getConceptos());
	// for (ConceptosReciboCaja concepto : reciboNuevo.getConceptos()) {
	// logger.info(concepto.getNroDocumento());
	// // Factura factura = facturaDao.facturaPorNumero(concepto.getNroDocumento(),
	// // reciboNuevo.getIdCliente().getId());
	// Factura factura = facturaDao.facturaPorNumero(concepto.getNroDocumento(),
	// reciboNuevo.getIdCliente().getId());
	// NotaDebitoCliente notaDebito =
	// notaDebitoDao.findByNumeroDocumento(concepto.getNroDocumento());
	// int asignacionRecibo = 0;
	// Integer descuentos =
	// conceptosReciboCajaDao.obtenerTotalDescuentoConcepto(concepto.getNroDocumento());

	// if (reciboNuevo.getAsignacion() != null
	// &&
	// reciboNuevo.getAsignacion().getNumero().equals(concepto.getNroDocumento())) {
	// asignacionRecibo = reciboNuevo.getAsignacion().getNotaCredito().getTotal();
	// reciboNuevo.getAsignacion().getNotaCredito().setEstadoDocumento("Asignado");
	// notaCreditoDao.save(reciboNuevo.getAsignacion().getNotaCredito());
	// conceptoAsignacion = true;
	// }
	// if (factura != null && factura.getTotal() - concepto.getValorAbono() -
	// asignacionRecibo
	// - concepto.getValorDescuento() - descuentos <= 0) {
	// EstadoDocumento estado = new EstadoDocumento();
	// estado.setId(4);
	// factura.setCodEstadoCon(estado);
	// facturaDao.save(factura);
	// }
	// if (notaDebito != null && notaDebito.getTotal() - concepto.getValorAbono() -
	// asignacionRecibo
	// - concepto.getValorDescuento() - descuentos <= 0) {
	// notaDebito.setEstadoDocumento("Asignado");
	// notaDebitoDao.save(notaDebito);
	// }
	// }
	// if (reciboNuevo.getAsignacion() != null) {
	// cambiarEstadoFacturaPorAsignacion(reciboNuevo, conceptoAsignacion);
	// reciboNuevo.getAsignacion().getNotaCredito().setEstadoDocumento("Asignado");
	// notaCreditoDao.save(reciboNuevo.getAsignacion().getNotaCredito());
	// }
	// sequenceDao.sequenciaNueva(prefijo.getPrefijo(),
	// reciboNuevo.getIdSede().getId(), prefijo.getId());
	// this.reciboCajaDao.save(reciboNuevo);
	// RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito guardando
	// Recibo de Caja venta");
	// respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);

	// } catch (Exception e) {
	// logger.error("Error en la creación del recibo de caja " +
	// e.getStackTrace()[0].getLineNumber() + e);
	// RespuestaDto respuestaDto = new
	// RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
	// "Error en la creación del recibo de caja");
	// respuesta = new ResponseEntity<>(respuestaDto,
	// HttpStatus.INTERNAL_SERVER_ERROR);
	// }

	// return respuesta;
	// }

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")

	public ResponseEntity<Object> guardarRecibo(@RequestBody ReciboCajaVenta reciboNuevo) throws Exception {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();

		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		Integer idSede = usuarioInformacion.getIdOrganizacion().getId();

		try {

			Prefijo prefijo = prefijoDao.obtenerPrefijoPorNombreSede(reciboNuevo.getIdSede().getId(),
					reciboNuevo.getPrefijo());

			reciboNuevo.setFechaPago(new Date(new java.util.Date().getTime()));

			reciboNuevo.setIdCreador(usuarioAutenticado);

			// TODO: se cambia estado al recibo a asignado
			// ! al crearlo ya tiene una(s) factura(s) enlazadas
			reciboNuevo.setCodEstadoMon("Pendiente");

			ReciboCajaVenta guardado = this.reciboCajaDao.save(reciboNuevo);

			boolean conceptoAsignacion = false;

			for (ConceptosReciboCaja concepto : reciboNuevo.getConceptos()) {

				logger.info(reciboNuevo.getConceptos());

				logger.info(reciboNuevo.getIdCliente().getId());

				int number = Integer.parseInt(concepto.getCodDocumento());

				logger.info(number);

				Factura factura = facturaDao.buscarporIdFactura2(number);
				NotaDebitoCliente notaDebito = notaDebitoDao.findByNumeroDocumento(concepto.getNroDocumento());

				int asignacionRecibo = 0;

				Integer descuentos = conceptosReciboCajaDao.obtenerTotalDescuentoConcepto(concepto.getNroDocumento());

				if (reciboNuevo.getAsignacion() != null
						&& reciboNuevo.getAsignacion().getNumero().equals(concepto.getNroDocumento())) {

					asignacionRecibo = reciboNuevo.getAsignacion().getNotaCredito().getTotal();

					reciboNuevo.getAsignacion().getNotaCredito().setEstadoDocumento("Asignado");

					notaCreditoDao.save(reciboNuevo.getAsignacion().getNotaCredito());

					conceptoAsignacion = true;

				}
				if (factura.getRetenciones() == null) {
					if (factura != null && factura.getTotal() - concepto.getValorAbono() - asignacionRecibo -
							-concepto.getValorDescuento() - descuentos <= 0) {
						EstadoDocumento estado = new EstadoDocumento();
						estado.setId(4);
						factura.setCodEstadoCon(estado);
						facturaDao.save(factura);
					}
				} else {
					if (factura != null
							&& factura.getTotal() - concepto.getValorAbono() - asignacionRecibo - factura.getRetenciones()
									- concepto.getValorDescuento() - descuentos <= 0) {
						EstadoDocumento estado = new EstadoDocumento();
						estado.setId(4);
						factura.setCodEstadoCon(estado);
						facturaDao.save(factura);
					}
				}

				if (notaDebito != null && notaDebito.getTotal() - concepto.getValorAbono() - asignacionRecibo

						- concepto.getValorDescuento() - descuentos <= 0) {

					notaDebito.setEstadoDocumento("Asignado");

					notaDebitoDao.save(notaDebito);

				}

				// if (reciboNuevo != null && (idSede == 7 || idSede == 15) && 1 != 1) {
				if (reciboNuevo != null && (idSede == 7 || idSede == 15)) {
					String uri = "http://190.145.10.42:8080/api/v1/TApoloRestInterface/execute";
					logger.info(uri);

					logger.info(reciboNuevo.getFechaPago().toString());
					Date fechaEntradaVenta = reciboNuevo.getFechaPago();
					Calendar calVent = Calendar.getInstance();
					calVent.setTime(fechaEntradaVenta);

					int yearVent = calVent.get(Calendar.YEAR);
					int monthVent = calVent.get(Calendar.MONTH) + 1;
					int dayVent = calVent.get(Calendar.DAY_OF_MONTH);

					Date fechaEntradaVence = reciboNuevo.getFechaPago();
					Calendar calVenc = Calendar.getInstance();
					calVenc.setTime(fechaEntradaVence);

					int yearVenc = calVenc.get(Calendar.YEAR);
					int monthVenc = calVenc.get(Calendar.MONTH) + 1;
					int dayVenc = calVenc.get(Calendar.DAY_OF_MONTH);
					// int dayVenc = calVenc.get(Calendar.DAY_OF_MONTH) + 10;

					String fechaVenta = dayVent + "." + monthVent + "." + yearVent;
					logger.info(fechaVenta);
					String fechaVence = dayVenc + "." + monthVenc + "." + yearVenc;
					logger.info(fechaVence);

					Secundario reciboCajaMekano = new Secundario();
					reciboCajaMekano.setCLAVE("Set_Gestion_Secundario");
					reciboCajaMekano.setTIPO("RC1");
					String prefijoN = "";
					switch (guardado.getIdSede().getId()) {
						case 1:
							prefijoN = "RCMT4";
							break;
						case 6:
							prefijoN = "RCMT2";
							break;
						case 7:
							prefijoN = "RCMT3";
							break;
						case 11:
							prefijoN = "RCMT1";
							break;
						case 12:
							prefijoN = "RCMT6";
							break;
						case 13:
							prefijoN = "RCMT10";
							break;
						case 14:
							prefijoN = "RCMT9";
							break;
						case 15:
							prefijoN = "RCMT5";
							break;

						default:
							break;
					}
					// !el prefijo se debe cambiar por el del recibo de caja
					reciboCajaMekano.setPREFIJO(prefijoN);
					reciboCajaMekano.setNUMERO(reciboNuevo.getCodRbocajaventa().toString());
					reciboCajaMekano.setFECHA(fechaVenta);
					reciboCajaMekano.setVENCE(fechaVence);
					reciboCajaMekano.setNOTA(reciboNuevo.getObservaciones().toString().trim());
					reciboCajaMekano.setTERCERO(reciboNuevo.getIdCliente().getNitocc().toString().trim());
					reciboCajaMekano.setVENDEDOR("24347052");
					reciboCajaMekano.setBANCO("CG");
					reciboCajaMekano.setUSUARIO("SUPERVISOR");
					reciboCajaMekano.setTIPO_REF("FE1");
					// reciboCajaMekano.setPREFIJO_REF(factura.getPrefijo().getPrefijo());//
					// !valores facturas asociadas
					// reciboCajaMekano.setNUMERO_REF(factura.getNroFactura().toString());//
					// !valores facturas asociadas
					reciboCajaMekano.setPREFIJO_REF(factura.getPrefijo().getPrefijo());
					reciboCajaMekano.setNUMERO_REF(factura.getNroFactura().toString());
					double Abono = concepto.getValorAbono();
					logger.info(Abono);
					int Ab = (int) Abono;
					logger.info(Ab);
					reciboCajaMekano.setABONO(Ab);

					Gson gson = new Gson();
					String rjson = gson.toJson(reciboCajaMekano);

					logger.info(rjson);
					HttpHeaders headers = new HttpHeaders();
					headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
					HttpEntity<String> entity = new HttpEntity<String>(rjson, headers);
					RestTemplate rest = new RestTemplate();
					logger.info(entity);
					// if (entity != null && 1 != 1) {
					if (entity != null) {
						TimeUnit.SECONDS.sleep(2);
						String result = rest.postForObject(uri, entity, String.class);

						if (result.contains("EL TERCERO")) {
							if (result.contains("NO EXISTE")) {
								logger.info("existe un error de tercero inexistente");
								// this.crearClienteMekano(reciboNuevo.getIdCliente());
								// this.crearFacturaMekanoN(reciboNuevo, factura, concepto);
								logger.info("tercero inexistente");
								throw new Exception("no existe un tercero");
							}
						}
						logger.info(result);

						if (result.contains("EL DOCUMENTO PRIMARIO")) {
							if (result.contains("NO EXISTE")) {
								logger.info("existe un error de documento primario inexistente");
								throw new Exception("no existe un DOCUMENTO con este CODIGO");

							}
						}

						logger.info(result);

						if (result.contains("EL DOCUMENTO PRIMARIO")) {
							if (result.contains("NO TIENE SALDO")) {
								logger.info("existe un error de documento primario inexistente");
								throw new Exception("no existe un DOCUMENTO con este CODIGO con saldo");
							}
						}
					}

				}

			}

			if (reciboNuevo.getAsignacion() != null) {

				cambiarEstadoFacturaPorAsignacion(reciboNuevo, conceptoAsignacion);

				reciboNuevo.getAsignacion().getNotaCredito().setEstadoDocumento("Asignado");

				notaCreditoDao.save(reciboNuevo.getAsignacion().getNotaCredito());

			}

			sequenceDao.sequenciaNueva(prefijo.getPrefijo(), reciboNuevo.getIdSede().getId(), prefijo.getId());

			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito guardando Recibo de Caja venta");

			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);

		} catch (Exception e) {

			logger.error("Error en la creación del recibo de caja " + e.getStackTrace()[0].getLineNumber() + " " + e);

			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,

					"Error en la creación del recibo de caja");

			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);

		}

		return respuesta;

	}

	private void crearFacturaMekanoN(ReciboCajaVenta guardado, Factura factura, ConceptosReciboCaja concepto)
			throws InterruptedException {

		if (guardado != null) {
			String uri = "http://190.145.10.42:8080/api/v1/TApoloRestInterface/execute";
			logger.info(uri);

			logger.info(guardado.getFechaPago().toString());
			logger.info(guardado.getFechaPago().toString());
			Date fechaEntradaVenta = guardado.getFechaPago();
			Calendar calVent = Calendar.getInstance();
			calVent.setTime(fechaEntradaVenta);

			int yearVent = calVent.get(Calendar.YEAR);
			int monthVent = calVent.get(Calendar.MONTH) + 1;
			int dayVent = calVent.get(Calendar.DAY_OF_MONTH);

			Date fechaEntradaVence = guardado.getFechaPago();
			Calendar calVenc = Calendar.getInstance();
			calVenc.setTime(fechaEntradaVence);

			int yearVenc = calVenc.get(Calendar.YEAR);
			int monthVenc = calVenc.get(Calendar.MONTH) + 1;
			int dayVenc = calVenc.get(Calendar.DAY_OF_MONTH);

			String fechaVenta = dayVent + "." + monthVent + "." + yearVent;
			logger.info(fechaVenta);
			String fechaVence = dayVenc + "." + monthVenc + "." + yearVenc;
			logger.info(fechaVence);

			Secundario reciboCajaMekano = new Secundario();
			reciboCajaMekano.setCLAVE("Set_Gestion_Secundario");
			reciboCajaMekano.setTIPO("RC1");
			String prefijo = "";
			switch (guardado.getIdSede().getId()) {
				case 1:
					prefijo = "RCMT4";
					break;
				case 6:
					prefijo = "RCMT2";
					break;
				case 7:
					prefijo = "RCMT3";
					break;
				case 11:
					prefijo = "RCMT1";
					break;
				case 12:
					prefijo = "RCMT6";
					break;
				case 13:
					prefijo = "RCMT10";
					break;
				case 14:
					prefijo = "RCMT9";
					break;
				case 15:
					prefijo = "RCMT5";
					break;

				default:
					break;
			}
			reciboCajaMekano.setPREFIJO(prefijo);
			reciboCajaMekano.setNUMERO(guardado.getCodRbocajaventa().toString());
			reciboCajaMekano.setFECHA(fechaVenta);
			reciboCajaMekano.setVENCE(fechaVence);
			// !revisar si no falla con observaciones osino enviar por dedecto "-"
			// reciboCajaMekano.setNOTA(guardado.getObservaciones().toString());
			reciboCajaMekano.setNOTA(guardado.getObservaciones());
			// reciboCajaMekano.setNOTA("-");
			reciboCajaMekano.setTERCERO(guardado.getIdCliente().getNitocc().toString());// !saldo tercero
			reciboCajaMekano.setVENDEDOR("24347052");
			reciboCajaMekano.setBANCO("CG");
			reciboCajaMekano.setUSUARIO("SUPERVISOR");
			reciboCajaMekano.setTIPO_REF("FE1");
			// reciboCajaMekano.setPREFIJO_REF(factura.getPrefijo().getPrefijo());//
			// !valores facturas asociadas
			// reciboCajaMekano.setNUMERO_REF(factura.getNroFactura().toString());//
			// !valores facturas asociadas
			reciboCajaMekano.setPREFIJO_REF(factura.getPrefijo().getPrefijo());
			reciboCajaMekano.setNUMERO_REF(factura.getNroFactura().toString());
			double Abono = concepto.getValorAbono();
			logger.info(Abono);
			int Ab = (int) Abono;
			logger.info(Ab);
			reciboCajaMekano.setABONO(Ab);

			Gson gson = new Gson();
			String rjson = gson.toJson(reciboCajaMekano);

			logger.info(rjson);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
			HttpEntity<String> entity = new HttpEntity<String>(rjson, headers);
			RestTemplate rest = new RestTemplate();

			// if (entity != null && 1 != 1) {
			if (entity != null) {
				TimeUnit.SECONDS.sleep(2);
				String result = rest.postForObject(uri, entity, String.class);
				logger.info(result);
			}
		}
	}

	private void crearClienteMekano(Clientes clientes) {
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

		if (clientes != null) {
			logger.info(clientes.getCodClasificacionLegal().getId());
			codigoProveedor = clientes.getNitocc() != null ? clientes.getNitocc() : "";
			// ! adicionar digito de verificacion
			digitoVerificacion = clientes.getDigito() != null ? clientes.getDigito().toString() : "";
			nombre = clientes.getNombres() != null ? clientes.getNombres().toUpperCase() : "";
			apellido = clientes.getApellidos() != null ? clientes.getApellidos().toUpperCase() : "";
			tDireccion = clientes.getDireccion() != null ? clientes.getDireccion().toUpperCase() : "";
			tTelefono = clientes.getTelefono() != null ? clientes.getTelefono() : "";
			tEmail = clientes.getEmail() != null ? clientes.getEmail().toUpperCase() : "";

			switch (clientes.getIdTipoDocumento().getId()) {
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

			switch (clientes.getCodClasificacionLegal().getId()) {
				case 1:
					clasificacionLegal = "N";
					codSociedad = "01";
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
					codSociedad = "09";
					break;
				case 8:
					clasificacionLegal = "J";
					codSociedad = "09";
					break;
				case 9:
					clasificacionLegal = "N";
					codSociedad = "01";
					break;
				case 10:
					clasificacionLegal = "J";
					codSociedad = "00N";
					break;
				case 11:
					clasificacionLegal = "J";
					codSociedad = "00R";
					break;
				case 12:
					clasificacionLegal = "N";
					codSociedad = "01";
					break;
				case 13:
					clasificacionLegal = "N";
					codSociedad = "02";
					break;
				case 14:
					clasificacionLegal = "J";
					codSociedad = "03";
					break;
				case 15:
					clasificacionLegal = "N";
					codSociedad = "04";
					break;
				case 16:
					clasificacionLegal = "J";
					codSociedad = "05";
					break;
				case 17:
					clasificacionLegal = "J";
					codSociedad = "06";
					break;
				case 18:
					clasificacionLegal = "J";
					codSociedad = "07";
					break;
				case 19:
					clasificacionLegal = "J";
					codSociedad = "08";
					break;
				case 20:
					clasificacionLegal = "J";
					codSociedad = "09";
					break;
				case 21:
					clasificacionLegal = "J";
					codSociedad = "10";
					break;
				default:
					break;
			}

			nidCiudad = clientes.getIdCiudad().getId() != null ? clientes.getIdCiudad().getId().toString() : "";
			String uri = "http://190.145.10.42:8080/api/v1/TApoloRestInterface/execute";
			logger.info(uri);
			Tercero proveedorMekano = new Tercero();
			proveedorMekano.setCLAVE(clave);
			proveedorMekano.setCODIGO(codigoProveedor);
			proveedorMekano.setDV(digitoVerificacion);
			proveedorMekano.setNATURALEZA(clasificacionLegal);
			proveedorMekano.setNOM1(nombre);
			proveedorMekano.setNOM2(nombreD);
			proveedorMekano.setAPL1(apellido);
			proveedorMekano.setAPL2(apellidoD);
			proveedorMekano.setEMPRESA(tProveedor);
			proveedorMekano.setRAZON_COMERCIAL(tProveedor);
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

	// Obtener recibo de caja por cliente
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerReciboCliente(Integer idCliente) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		try {

			ReciboCajaVenta recibo = this.reciboCajaDao.obtenerRecibo(idCliente);

			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de prefijos exitosa");
			respuestaDto.setObjetoRespuesta(recibo);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo usuarios " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error obteniendo usuarios");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	private boolean existeReciboCaja(ReciboCajaVenta reciboBuscar) {

		boolean flag = false;

		if (this.reciboCajaDao.findByCodRbocajaventa(reciboBuscar.getCodRbocajaventa()) != null) {
			flag = true;
		}

		return flag;
	}

	// Obtener el total de cambio de la caja
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerCambioCaja(Integer idCaja, String fecha) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		try {

			Integer cambio = this.reciboCajaDao.obtenerCambioTotalCaja(idCaja, fecha);

			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de cambio exitosa");
			respuestaDto.setObjetoRespuesta(cambio);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo usuarios " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error obteniendo cambio");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	// Obtener el total de cambio de la sede
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerCambioSede(Integer idSede, String fecha) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		try {

			Integer cambio = this.reciboCajaDao.obtenerCambioTotalSede(idSede, fecha);

			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de cambio exitosa");
			respuestaDto.setObjetoRespuesta(cambio);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo usuarios " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error obteniendo cambio");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	// Obtener recibo de caja
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerRecibo(@RequestParam String numRecibo) {

		logger.info(numRecibo);

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		try {

			ArrayList<ReciboCajaVenta> recibo = this.reciboCajaDao.findCodRbocaja(numRecibo);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de recibo exitosa");
			respuestaDto.setObjetoRespuesta(recibo);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);

		} catch (Exception e) {
			logger.error("Error obteniendo recibo " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error obteniendo recibo");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	// Anular recibo de caja
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> anularRecibo(@RequestBody ReciboCajaVenta recibo) {
		if (recibo != null) {
			logger.info(recibo.getId());
		}
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		ReciboCajaVenta reciboAnular = this.reciboCajaDao.findById(recibo.getId()).get();

		try {

			if (reciboAnular.getId().equals(recibo.getId())) {

				reciboAnular.setFechaMod(new Date(new java.util.Date().getTime()));
				reciboAnular.setCambio(0.0);
				reciboAnular.setSaldo(0);
				reciboAnular.setTotalRecibo(0);
				reciboAnular.setNroFacPendientes(0);
				reciboAnular.setFechaPago(null);
				reciboAnular.setCodEstadoMon("Anulado");
				reciboAnular.setPrefijo(null);
				reciboAnular.setIdCaja(null);
				reciboAnular.setIdCliente(null);
				reciboAnular.setObservaciones(null);
				reciboAnular.setIdMoneda(null);
				// reciboAnular.setCodEstadoMon("Anulado");
				ArrayList<ConceptosReciboCaja> conceptosR = conceptosReciboCajaDao.listarConceptos(recibo.getId());
				conceptosReciboCajaDao.borrarConceptos(reciboAnular.getId());
				EstadoDocumento estado = estadoDao.findById(5).orElse(null);
				logger.info(estado);
				for (int i = 0; i < conceptosR.size(); i++) {
					Factura f = facturaDao.buscarporIdFacturaConcepto(Integer.parseInt(conceptosR.get(i).getCodDocumento()));
					f.setCodEstadoCon(estado);
					f.setRetenciones(0);
					retencionDao.eliminarRetencionesFactura(f.getId());
				}
				pagosDao.borrarPagosRecibo(reciboAnular.getId());
				ReciboCajaVenta anulado = this.reciboCajaDao.save(reciboAnular);

				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Recibo Anulado");
				respuestaDto.setObjetoRespuesta(anulado);
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
			}

		} catch (Exception e) {
			logger.error("Error obteniendo recibo " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error obteniendo recibo");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	// Obtener recibos de caja
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerRecibosCaja(String fechaInicio, String fechaFin, String estado,
			String numeroRecibo, Integer page) {
		Pageable paging = PageRequest.of(page, 10);
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao.buscarPorIdUsuario(usuarioAutenticado.getId());
		Integer idSede = usuarioInformacion.getIdOrganizacion().getId();

		try {
			List<ReciboCajaVenta> recibos;
			StringBuilder query = new StringBuilder();
			StringBuilder queryCantidad = new StringBuilder();
			generarQuery(query, fechaInicio, fechaFin, estado, numeroRecibo.trim(), idSede);
			TypedQuery<ReciboCajaVenta> comprobantesInfoQuery = (TypedQuery<ReciboCajaVenta>) entityManager
					.createNativeQuery(query.toString(), ReciboCajaVenta.class);
			int pageNumber = paging.getPageNumber();
			int pageSize = paging.getPageSize();
			comprobantesInfoQuery.setFirstResult(pageNumber * pageSize);
			comprobantesInfoQuery.setMaxResults(pageSize);
			recibos = comprobantesInfoQuery.getResultList();
			for (ReciboCajaVenta recibo : recibos) {
				List<PagosReciboCaja> pagos = pagosDao.findPagos(recibo.getId());
				List<ConceptosReciboCaja> conceptos = conceptosRecibosDao.listarConceptos(recibo.getId());
				recibo.setIdPagos(pagos);
				recibo.setConceptos(conceptos);
			}
			generarQueryCantidad(queryCantidad, fechaInicio, fechaFin, estado, numeroRecibo.trim(), idSede);
			Query cantidad = entityManager.createNativeQuery(queryCantidad.toString());
			BigInteger countResult = (BigInteger) cantidad.getSingleResult();
			Integer cantidadTotal = countResult.intValue();
			Page<ReciboCajaVenta> result = new PageImpl<ReciboCajaVenta>(recibos, paging, cantidadTotal);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de prefijos exitosa");
			respuestaDto.setObjetoRespuesta(result);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo recibos " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error obteniendo recibos");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	private void generarQuery(StringBuilder query, String fechaInicio, String fechaFin, String estado,
			String numeroRecibo, Integer idSede) {
		logger.info(numeroRecibo);
		query.append("select * from rcb_rbocajaventa ");
		if (!fechaInicio.equals("null") && fechaFin.equals("null")) {
			query.append("where date_format(date(fecha_pago),'%Y-%m-%d')" + "= date('" + fechaInicio + "')");
		} else if (fechaInicio.equals("null") && !fechaFin.equals("null")) {
			query.append("where date_format(date(fecha_pago),'%Y-%m-%d')" + "<= date('" + fechaFin + "')");
		} else if (!fechaInicio.equals("null") && !fechaFin.equals("null")) {
			query.append("where date_format(date(fecha_pago),'%Y-%m-%d')" + "between " + "date('" + fechaInicio
					+ "') and " + "date('" + fechaFin + "')");
		}
		if ((!numeroRecibo.equals("null")) && (!fechaInicio.equals("null") || !fechaFin.equals("null"))) {
			query.append(" and cod_rbocajaventa='" + numeroRecibo + "'");
		} else if (!numeroRecibo.equals("null")) {
			query.append("where cod_rbocajaventa='" + numeroRecibo + "'");
		}
		if (!estado.equals("") && ((!fechaInicio.equals("null") || !fechaFin.equals("null"))
				|| !numeroRecibo.equals("null"))) {
			query.append(" and cod_estado_mon like'%" + estado + "%'");
		} else if (!estado.equals("")) {
			query.append("where cod_estado_mon like'%" + estado + "%'");
		}
		if ((idSede != 0) && ((!fechaInicio.equals("null") || !fechaFin.equals("null"))
				|| !numeroRecibo.equals("null") || !estado.equals(""))) {
			query.append(" and id_sede= " + idSede);
		} else if (idSede != 0) {
			query.append("where id_sede= " + idSede);
		}
		query.append(" order by fecha_pago desc, cod_rbocajaventa desc");
		logger.info(query);
	}

	private void generarQueryCantidad(StringBuilder query, String fechaInicio, String fechaFin, String estado,
			String numeroRecibo, Integer idSede) {
		query.append("select count(*) from rcb_rbocajaventa ");
		if (!fechaInicio.equals("null") && fechaFin.equals("null")) {
			query.append("where date_format(date(fecha_pago),'%Y-%m-%d')" + "= date('" + fechaInicio + "')");
		} else if (fechaInicio.equals("null") && !fechaFin.equals("null")) {
			query.append("where date_format(date(fecha_pago),'%Y-%m-%d')" + "<= date('" + fechaFin + "')");
		} else if (!fechaInicio.equals("null") && !fechaFin.equals("null")) {
			query.append("where date_format(date(fecha_pago),'%Y-%m-%d')" + "between " + "date('" + fechaInicio
					+ "') and " + "date('" + fechaFin + "')");
		}
		if ((!numeroRecibo.equals("null")) && (!fechaInicio.equals("null") || !fechaFin.equals("null"))) {
			query.append(" and cod_rbocajaventa='" + numeroRecibo + "'");
		} else if (!numeroRecibo.equals("null")) {
			query.append("where cod_rbocajaventa='" + numeroRecibo + "'");
		}
		if (!estado.equals("") && ((!fechaInicio.equals("null") || !fechaFin.equals("null"))
				|| !numeroRecibo.equals("null"))) {
			query.append(" and cod_estado_mon like'%" + estado + "%'");
		} else if (!estado.equals("")) {
			query.append("where cod_estado_mon like'%" + estado + "%'");
		}
		if ((idSede != 0) && ((!fechaInicio.equals("null") || !fechaFin.equals("null"))
				|| !numeroRecibo.equals("null") || !estado.equals(""))) {
			query.append(" and id_sede= " + idSede);
		} else if (idSede != 0) {
			query.append("where id_sede= " + idSede);
		}
		logger.info(query);
	}

	// Obtener recibo de caja
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> eliminarRecibo(@RequestParam Integer idRecibo) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		try {
			pagosDao.borrarPagosRecibo(idRecibo);
			conceptosRecibosDao.borrarConceptos(idRecibo);
			retencionDao.eliminarRetenciones(idRecibo);
			ReciboCajaVenta recibo = reciboCajaDao.findById(idRecibo).orElse(null);
			this.reciboCajaDao.eliminarRecibo(idRecibo);
			this.asignacionDao.deleteById(recibo.getAsignacion().getId());
			;
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "eliminación de recibo exitosa");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);

		} catch (Exception e) {
			logger.error("Error eliminando recibo " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error eliminando recibo");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	// Actualizar recibos de caja venta
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> actualizarRecibo(@RequestBody ReciboCajaVenta reciboNuevo) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();

		try {
			Prefijo prefijo = prefijoDao.obtenerPrefijoPorNombreSede(reciboNuevo.getIdSede().getId(),
					reciboNuevo.getPrefijo());
			reciboNuevo.setFechaPago(new Date(new java.util.Date().getTime()));
			reciboNuevo.setIdCreador(usuarioAutenticado);
			pagosDao.borrarPagosRecibo(reciboNuevo.getId());
			conceptosRecibosDao.borrarConceptos(reciboNuevo.getId());
			retencionDao.eliminarRetenciones(reciboNuevo.getId());
			AsignacionRecibo asignacion = asignacionDao.asignacion(reciboNuevo.getId());
			ReciboCajaVenta guardado = this.reciboCajaDao.save(reciboNuevo);
			asignacionDao.deleteById(asignacion.getId());
			for (ConceptosReciboCaja concepto : reciboNuevo.getConceptos()) {
				Factura factura = facturaDao.facturaPorNumero(concepto.getNroDocumento(), reciboNuevo.getIdCliente().getId());
				NotaDebitoCliente notaDebito = notaDebitoDao.findByNumeroDocumento(concepto.getNroDocumento());
				int asignacionRecibo = 0;
				if (reciboNuevo.getAsignacion() != null
						&& reciboNuevo.getAsignacion().getNumero().equals(concepto.getNroDocumento())) {
					asignacionRecibo = reciboNuevo.getAsignacion().getNotaCredito().getTotal();
					reciboNuevo.getAsignacion().getNotaCredito().setEstadoDocumento("Pagado");
					notaCreditoDao.save(reciboNuevo.getAsignacion().getNotaCredito());
				}
				if (factura != null && factura.getTotal() - concepto.getValorAbono() - asignacionRecibo <= 0) {
					EstadoDocumento estado = new EstadoDocumento();
					estado.setId(4);
					factura.setCodEstadoCon(estado);
					facturaDao.save(factura);
				}
				if (notaDebito != null && notaDebito.getTotal() - concepto.getValorAbono() - asignacionRecibo <= 0) {
					notaDebito.setEstadoDocumento("Pagado");
					notaDebitoDao.save(notaDebito);
				}
			}
			Integer numero = Integer.parseInt(reciboNuevo.getCodRbocajaventa());
			Sequence sequence = sequenceDao.findByIdSedeAndIdPrefijo(reciboNuevo.getIdSede().getId(), prefijo.getId())
					.orElse(null);
			if (sequence == null) {
				Sequence nuevaSecuencia = new Sequence();
				nuevaSecuencia.setIdSede(reciboNuevo.getIdSede().getId());
				nuevaSecuencia.setIdPrefijo(prefijo.getId());
				nuevaSecuencia.setValorSequencia(numero);
				sequenceDao.save(nuevaSecuencia);
			} else {
				sequenceDao.save(sequence);
			}
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito guardando Recibo de Caja venta");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error en la creación del recibo de caja " + e.getStackTrace()[0].getLineNumber() + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error en la creación del recibo de caja");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return respuesta;
	}

	// Obtener recibos de caja
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerRecibosCajaParaExportar(String fechaInicio, String fechaFin, String estado,
			String numeroRecibo) {
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao.buscarPorIdUsuario(usuarioAutenticado.getId());
		Integer idSede = usuarioInformacion.getIdOrganizacion().getId();

		try {
			List<ReciboCajaVenta> recibos;
			StringBuilder query = new StringBuilder();
			StringBuilder queryCantidad = new StringBuilder();
			generarQuery(query, fechaInicio, fechaFin, estado, numeroRecibo, idSede);
			TypedQuery<ReciboCajaVenta> comprobantesInfoQuery = (TypedQuery<ReciboCajaVenta>) entityManager
					.createNativeQuery(query.toString(), ReciboCajaVenta.class);
			recibos = comprobantesInfoQuery.getResultList();
			for (ReciboCajaVenta recibo : recibos) {
				List<PagosReciboCaja> pagos = pagosDao.findPagos(recibo.getId());
				List<ConceptosReciboCaja> conceptos = conceptosRecibosDao.listarConceptos(recibo.getId());
				recibo.setIdPagos(pagos);
				recibo.setConceptos(conceptos);
			}
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de prefijos exitosa");
			respuestaDto.setObjetoRespuesta(recibos);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo recibos " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error obteniendo recibos");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	public List<ReciboCajaVenta> listardetallesDePagoFiltros(Integer idSede, String fechaInicial, String fechaFinal,
			String numeroDocumento, Integer cliente) {
		List<ReciboCajaVenta> recibos;
		StringBuilder query = new StringBuilder();
		generarQuery(query, idSede, fechaInicial, fechaFinal, numeroDocumento, cliente);
		TypedQuery<ReciboCajaVenta> recibosInfoQuery = (TypedQuery<ReciboCajaVenta>) entityManager
				.createNativeQuery(query.toString(), ReciboCajaVenta.class);
		recibos = recibosInfoQuery.getResultList();
		for (ReciboCajaVenta recibo : recibos) {
			List<ConceptosReciboCaja> conceptos = conceptosReciboCajaDao.findAllBySedeRecibo(recibo.getId());
			recibo.setConceptos(conceptos);
		}
		return recibos;
	}

	private void generarQuery(StringBuilder query, Integer idSede, String fechaInicial, String fechaFinal,
			String numeroDocumento, Integer cliente) {

		query.append("select * from rcb_rbocajaventa where id_sede=");

		query.append("" + idSede);
		if (!fechaInicial.equals("null") && fechaFinal.equals("null")) {
			query.append(" and date_format(date(fecha_pago),'%Y-%m-%d')" + "= date('" + fechaInicial + "')");
		} else if (fechaInicial.equals("null") && !fechaFinal.equals("null")) {
			query.append(" and date_format(date(fecha_pago),'%Y-%m-%d')" + "= date('" + fechaFinal + "')");
		} else if (!fechaInicial.equals("null") && !fechaFinal.equals("null")) {
			query.append(" and date_format(date(fecha_pago),'%Y-%m-%d')" + " between " + "date_format(date('"
					+ fechaInicial + "'),'%Y-%m-%d')" + " and " + "date_format(date('" + fechaFinal + "'),'%Y-%m-%d')");
		}

		if (!numeroDocumento.equals("null")) {
			query.append(" and cod_rbocajaventa='" + numeroDocumento + "'");
		}

		if (cliente != 0) {
			query.append(" and id_cliente=" + cliente);
		}

	}

	public List<ReciboCajaVenta> listardetallesDePagoFiltros1(Integer idSede, String fechaInicial, String fechaFinal,
			String numeroDocumento, Integer cliente) {
		List<ReciboCajaVenta> recibos;
		StringBuilder query = new StringBuilder();
		generarQuery(query, idSede, fechaInicial, fechaFinal, numeroDocumento, cliente);
		TypedQuery<ReciboCajaVenta> recibosInfoQuery = (TypedQuery<ReciboCajaVenta>) entityManager
				.createNativeQuery(query.toString(), ReciboCajaVenta.class);
		recibos = recibosInfoQuery.getResultList();
		return recibos;
	}

	public void generarQueryRecibos(StringBuilder query, Integer idSede, String fechaInicial, String fechaFinal,
			String numeroDocumento, Integer cliente, Integer idRecibo) {

		query.append(
				"select crc.* from rcb_rbocajaventa rr join conceptos_recibo_caja crc on rr.id = crc.id_recibo_caja where id_sede=");

		query.append("" + idSede + " and rr.id= " + idRecibo);
		if (!fechaInicial.equals("null") && fechaFinal.equals("null")) {
			query.append(" and date_format(date(rr.fecha_pago),'%Y-%m-%d')" + "= date('" + fechaInicial + "')");
		} else if (fechaInicial.equals("null") && !fechaFinal.equals("null")) {
			query.append(" and date_format(date(rr.fecha_pago),'%Y-%m-%d')" + "= date('" + fechaFinal + "')");
		} else if (!fechaInicial.equals("null") && !fechaFinal.equals("null")) {
			query.append(" and date_format(date(rr.fecha_pago),'%Y-%m-%d')" + " between " + "date_format(date('"
					+ fechaInicial + "'),'%Y-%m-%d')" + " and " + "date_format(date('" + fechaFinal + "'),'%Y-%m-%d')");
		}

		if (!numeroDocumento.equals("null")) {
			query.append(" and rr.cod_rbocajaventa='" + numeroDocumento + "'" + " or crc.nro_documento='"
					+ numeroDocumento + "'");
		}

		if (cliente != 0) {
			query.append(" and rr.id_cliente=" + cliente);
		}

	}

	public Integer obtenerTotalDescuentos(Integer idSede, String fechaInicial, String fechaFinal,
			String numeroDocumento, Integer cliente) {
		StringBuilder query = new StringBuilder();
		generarQueryTotalDescuentos(query, idSede, fechaInicial, fechaFinal, numeroDocumento, cliente);
		Query resultado = entityManager.createNativeQuery(query.toString());
		Double resultQuery = (Double) resultado.getSingleResult();
		Integer totalValor = resultQuery.intValue();
		return totalValor;
	}

	public Integer obtenerTotalAbonos(Integer idSede, String fechaInicial, String fechaFinal, String numeroDocumento,
			Integer cliente) {
		StringBuilder query = new StringBuilder();
		generarQueryTotalRecibos(query, idSede, fechaInicial, fechaFinal, numeroDocumento, cliente);
		Query resultado = entityManager.createNativeQuery(query.toString());
		Double resultQuery = (Double) resultado.getSingleResult();
		Integer totalValor = resultQuery.intValue();
		return totalValor;
	}

	public void generarQueryTotalDescuentos(StringBuilder query, Integer idSede, String fechaInicial, String fechaFinal,
			String numeroDocumento, Integer cliente) {

		query.append(
				"select ifnull(sum(crc.valor_descuento),0) from rcb_rbocajaventa rr join conceptos_recibo_caja crc on rr.id = crc.id_recibo_caja where id_sede=");

		query.append("" + idSede);
		if (!fechaInicial.equals("null") && fechaFinal.equals("null")) {
			query.append(" and date_format(date(rr.fecha_pago),'%Y-%m-%d')" + "= date('" + fechaInicial + "')");
		} else if (fechaInicial.equals("null") && !fechaFinal.equals("null")) {
			query.append(" and date_format(date(rr.fecha_pago),'%Y-%m-%d')" + "= date('" + fechaFinal + "')");
		} else if (!fechaInicial.equals("null") && !fechaFinal.equals("null")) {
			query.append(" and date_format(date(rr.fecha_pago),'%Y-%m-%d')" + " between " + "date_format(date('"
					+ fechaInicial + "'),'%Y-%m-%d')" + " and " + "date_format(date('" + fechaFinal + "'),'%Y-%m-%d')");
		}

		if (!numeroDocumento.equals("null")) {
			query.append(" and rr.cod_rbocajaventa='" + numeroDocumento + "'" + " or crc.nro_documento='"
					+ numeroDocumento + "'");
		}

		if (cliente != 0) {
			query.append(" and rr.id_cliente=" + cliente);
		}

	}

	public void generarQueryTotalRecibos(StringBuilder query, Integer idSede, String fechaInicial, String fechaFinal,
			String numeroDocumento, Integer cliente) {

		query.append(
				"select ifnull(sum(crc.valor_abono),0) from rcb_rbocajaventa rr join conceptos_recibo_caja crc on rr.id = crc.id_recibo_caja where id_sede=");

		query.append("" + idSede);
		if (!fechaInicial.equals("null") && fechaFinal.equals("null")) {
			query.append(" and date_format(date(rr.fecha_pago),'%Y-%m-%d')" + "= date('" + fechaInicial + "')");
		} else if (fechaInicial.equals("null") && !fechaFinal.equals("null")) {
			query.append(" and date_format(date(rr.fecha_pago),'%Y-%m-%d')" + "= date('" + fechaFinal + "')");
		} else if (!fechaInicial.equals("null") && !fechaFinal.equals("null")) {
			query.append(" and date_format(date(rr.fecha_pago),'%Y-%m-%d')" + " between " + "date_format(date('"
					+ fechaInicial + "'),'%Y-%m-%d')" + " and " + "date_format(date('" + fechaFinal + "'),'%Y-%m-%d')");
		}

		if (!numeroDocumento.equals("null")) {
			query.append(" and rr.cod_rbocajaventa='" + numeroDocumento + "'" + " or crc.nro_documento='"
					+ numeroDocumento + "'");
		}

		if (cliente != 0) {
			query.append(" and rr.id_cliente=" + cliente);
		}

	}

	// Obtener recibo de caja por factur
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerReciboFacturas(@RequestParam String numRecibo, Integer page) {
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Pageable paging = PageRequest.of(page, 10);
		try {
			int numeroDeAsignaciones = 0;
			ArrayList<ReciboDocumentoPago> recibos = new ArrayList<ReciboDocumentoPago>();
			Page<ConceptosReciboCaja> conceptos = conceptosRecibosDao.listarConceptosNumero(numRecibo, paging);
			for (ConceptosReciboCaja concepto : conceptos) {
				ReciboCajaVenta recibo = reciboCajaDao.findById(conceptosRecibosDao.obtenerIdRecibo(concepto.getId()))
						.orElse(null);
				recibo.getConceptos().add(concepto);
				ReciboDocumentoPago documento = ReciboDocumentoPago.convertirReciboDocumento(recibo);
				recibos.add(documento);
			}
			ReciboCajaVenta recibo = reciboCajaDao.obtenerRecibosNumeroAsignacion(numRecibo);
			if (recibo != null && recibo.getAsignacion() != null) {
				ReciboDocumentoPago documento = ReciboDocumentoPago.convertirAsignacionDocumento(recibo);
				recibos.add(documento);
				numeroDeAsignaciones += 1;
			}
			Page<ReciboDocumentoPago> result = new PageImpl<ReciboDocumentoPago>(recibos, paging,
					conceptos.getTotalElements() + numeroDeAsignaciones);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de recibo exitosa");
			respuestaDto.setObjetoRespuesta(result);
			respuestaDto.setVariable(String.valueOf(conceptosRecibosDao.totalConceptosNumero(numRecibo)
					+ reciboCajaDao.obtenerTotalAsignacionNumero(numRecibo)));
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);

		} catch (Exception e) {
			logger.error("Error obteniendo recibo " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error obteniendo recibo");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	private String extraerNumeros(String cadena) {
		char[] cadenaArray = cadena.toCharArray();
		String numero = "";
		for (int i = 0; i < cadenaArray.length; i++) {
			if (Character.isDigit(cadenaArray[i])) {
				numero += cadenaArray[i];
			}
		}
		return numero;
	}

	public List<ReciboCajaVenta> buscarPorIdArticulo2(Integer idSede, Integer idArticulo) {
		List<ReciboCajaVenta> lista = reciboCajaDao.buscarPorIdArticulo2(idSede, idArticulo);
		return lista;
	}

	private void cambiarEstadoFacturaPorAsignacion(ReciboCajaVenta body, boolean conceptoAsignacion) {
		if (!conceptoAsignacion) {
			logger.info("ingresa a asignacion conceptoooo");
			Factura factura = facturaDao.facturaPorNumero(body.getAsignacion().getNumero(), body.getIdCliente().getId());
			EstadoDocumento estado = new EstadoDocumento();
			estado.setId(4);
			factura.setCodEstadoCon(estado);
			this.facturaDao.save(factura);
		}
	}
}
