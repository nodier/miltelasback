package com.softlond.base.service;

import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.ArticulosRemisionCompra;
import com.softlond.base.entity.ComprobanteEgreso;
import com.softlond.base.entity.ConceptoReciboEgreso;
import com.softlond.base.entity.ConsecutivoEgresoSede;
import com.softlond.base.entity.ContableM;
import com.softlond.base.entity.DescuentoComprobanteEgreso;
import com.softlond.base.entity.EstadoDocumento;
import com.softlond.base.entity.FacturaCompra;
import com.softlond.base.entity.FacturaRetenciones;
import com.softlond.base.entity.InformacionUsuario;
import com.softlond.base.entity.MaestroValor;
import com.softlond.base.entity.NotaDebito;
import com.softlond.base.entity.Organizacion;
import com.softlond.base.entity.PagoComprobanteEgreso;
import com.softlond.base.entity.Producto;
import com.softlond.base.entity.Proveedor;
import com.softlond.base.entity.RetencionComprobanteEgreso;
import com.softlond.base.entity.Tercero;
import com.softlond.base.entity.Usuario;
import com.softlond.base.repository.AsignacionComprobanteDao;
import com.softlond.base.repository.ComprobanteEgresoDao;
import com.softlond.base.repository.ConceptoReciboEgresoDao;
import com.softlond.base.repository.ConsecutivoEgresoSedeDao;
import com.softlond.base.repository.DescuentoComprobanteEgresoDao;
import com.softlond.base.repository.FacturaCompraDao;
import com.softlond.base.repository.FacturaRetencionesDao;
import com.softlond.base.repository.MaestroValorDao;
import com.softlond.base.repository.NotaCreditoDao;
import com.softlond.base.repository.NotaDebitoDao;
import com.softlond.base.repository.PagoComprobanteEgresoDao;
import com.softlond.base.repository.ProveedorDao;
import com.softlond.base.repository.RetencionComprobanteEgresoDao;
import com.softlond.base.repository.SequenceDao;
import com.softlond.base.repository.UsuarioInformacionDao;
import com.softlond.base.request.ComprobanteEgresoRequest;
import com.softlond.base.response.ReciboDocumentoPago;
import java.util.Date;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
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

import com.google.gson.Gson;
// import com.google.api.client.http.HttpHeaders;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.MediaType;

@Service
public class ComprobanteEgresoService {

	@Autowired
	private MaestroValorDao maestrovalorDao;

	@Autowired
	private PagoComprobanteEgresoDao pagoDao;

	@Autowired
	private ComprobanteEgresoDao comprobanteEgresoDao;
	@Autowired
	private DescuentoComprobanteEgresoDao descuentoDao;

	@Autowired
	private ConceptoReciboEgresoDao conceptoDao;

	@Autowired
	private ProveedorDao proveedorDao;

	@Autowired
	private RetencionComprobanteEgresoDao retencionDao;

	@Autowired
	private FacturaCompraDao facturaDao;

	@Autowired
	private AsignacionComprobanteDao asignacionComprobanteDao;

	@Autowired
	private NotaCreditoDao notaCreditoDao;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private ConsecutivoEgresoSedeDao consecutivoDao;

	@Autowired
	UsuarioInformacionDao usuarioInformacionDao;

	@Autowired
	FacturaRetencionesDao facturaRetencionesDao;

	@Autowired
	private NotaDebitoDao notaDebitoDao;

	@Autowired
	private SequenceDao sequenceDao;

	private static final Logger logger = Logger.getLogger(ComprobanteEgresoService.class);

	@SuppressWarnings("unused")
	public ComprobanteEgreso crearComprobante(ComprobanteEgresoRequest comprobante) throws Exception {
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao.buscarPorIdUsuario(usuarioAutenticado.getId());
		Organizacion sede = usuarioInformacion.getIdOrganizacion();
		logger.info(comprobante.getComprobante().getPagos().size());
		for (PagoComprobanteEgreso pago : comprobante.getComprobante().getPagos()) {
			logger.info(pago.getCuenta());
		}
		if (comprobanteEgresoDao
				.obtenerComprobantesPorNumeroC(comprobante.getComprobante().getNumeroDocumento(),
						sede.getId()) > 0) {
			logger.info("ya existe el comprobante " + comprobante.getComprobante().getNumeroDocumento().toString());
		} else {
			logger.info("no existe el comprobante (es nuevo)");
			logger.info(comprobante.getComprobante().getNumeroDocumento().toString());
		}
		logger.info("ingresa a crear comprobante de egresso serviceee ");
		if (comprobante.getComprobante() != null) {
			logger.info("llega el comprobante sin problemass");
		}
		ComprobanteEgreso body = comprobante.getComprobante();
		Integer idPrefijo = comprobante.getIdPrefijo();
		boolean conceptoAsignacion = false;
		int asignacionComprobante = 0;

		for (ConceptoReciboEgreso concepto : body.getConceptos()) {
			if (concepto != null) {
				if (concepto.getNumeroDocumento() != null) {
					logger.info(concepto.getNumeroDocumento());
				}
			}
			FacturaCompra factura = this.facturaDao.obtenerFacturaNumero(concepto.getNumeroDocumento(), sede.getId());
			if (factura != null) {
				logger.info("trae una factura");
			}
			String nombrePrefijo;
			String numeroPrefijo;
			NotaDebito notaDebito = null;
			if (factura == null && body.getTipoDocumento().equals("Proveedores")) {
				nombrePrefijo = obtenerPrefijoNotaDebito(concepto.getNumeroDocumento());
				numeroPrefijo = obtenerNumeroNotaDebito(concepto.getNumeroDocumento());
				notaDebito = this.notaDebitoDao.findByIdSedeNumeroYPrefijo(sede.getId(), numeroPrefijo, nombrePrefijo);
			}
			Integer asignacion = asignacionComprobanteDao.obtenerTotal(concepto.getNumeroDocumento(), sede.getId());
			Integer conceptoTotal = this.conceptoDao.obtenerTotalAbonos(concepto.getNumeroDocumento(), sede.getId());
			Integer totalDescuento = this.conceptoDao.obtenerTotalDescuentos(concepto.getNumeroDocumento(), sede.getId());

			if (body.getAsignacion() != null && body.getAsignacion().getNumero().equals(concepto.getNumeroDocumento())) {
				asignacionComprobante = body.getAsignacion().getNotaCredito().getTotal();
				conceptoAsignacion = true;
			}
			Double abono = 0.0;
			if (asignacion == null)
				asignacion = 0;
			if (conceptoTotal == null)
				conceptoTotal = 0;
			if (totalDescuento == null)
				totalDescuento = 0;
			if (concepto.getValorAbono() != null)
				abono = concepto.getValorAbono();
			if (factura != null
					&& factura.getTotal() - asignacion.doubleValue() - abono.intValue() - conceptoTotal.intValue()
							- totalDescuento - concepto.getDescuento() - asignacionComprobante <= 0) {
				EstadoDocumento estado = new EstadoDocumento();
				estado.setId(4);
				factura.setCodEstadoCon(estado);
				this.facturaDao.save(factura);
			} else if (notaDebito != null
					&& notaDebito.getTotal() - asignacion.doubleValue() - abono.intValue() - conceptoTotal.intValue()
							- totalDescuento - concepto.getDescuento() - asignacionComprobante <= 0) {
				notaDebito.setEstadoDocumento("Pagado");
				this.notaDebitoDao.save(notaDebito);
			}
		} // fin ciclo

		if (body.getAsignacion() != null) {
			cambiarEstadoFacturaPorAsignacion(body, conceptoAsignacion, sede.getId());
			body.getAsignacion().getNotaCredito().setEstadoDocumento("Asignado");
			notaCreditoDao.save(body.getAsignacion().getNotaCredito());
		}
		int numeroAnterior = body.getNumeroDocumento();
		logger.info(numeroAnterior);

		if (body.getId() == null
				&& comprobanteEgresoDao.obtenerComprobantesPorNumeroC(body.getNumeroDocumento(),
						sede.getId()) <= 0) {

			Integer idPrefijoO = comprobante.getIdPrefijo();
			logger.info(idPrefijoO);

			body.setSede(sede);
			logger.info(comprobante.getComprobante().getPrefijo() + " - " + sede.getId() + " - " + idPrefijo);
			sequenceDao.sequenciaNueva(comprobante.getComprobante().getPrefijo(), sede.getId(), idPrefijo);
		}
		if (body == null) {
			logger.info("no existe body comprobante de egreso");
		}
		ComprobanteEgreso comprobanteEgresoGuardado = null;
		if (!body.isRestringido()
				&& comprobanteEgresoDao.obtenerComprobantesPorNumeroC(body.getNumeroDocumento(),
						sede.getId()) <= 0) {

			body.setRestringido(true);

			logger.info(body.getId());
			// ! //TODO: se realiza la iteracion por la lista de cuentas asociadas al
			// comprobante
			if (comprobante.getListContable().size() > 0) {
				for (String[] fila : comprobante.getListContable()) {
					this.crearComprobanteMekano(fila, comprobante.getComprobante());
				}
			}
			comprobanteEgresoGuardado = comprobanteEgresoDao.save(body);
			Integer cantidadComprobantes = comprobanteEgresoDao.obtenerComprobantesPorNumeroC(
					comprobanteEgresoGuardado.getNumeroDocumento(), sede.getId());
			if (cantidadComprobantes > 1) {
				logger.info("existen comprobantes repetidos");
			}
		}
		logger.info("body.getId()");
		logger.info(body.getId());
		List<PagoComprobanteEgreso> pagosM = pagoDao.pagosComprobanteEgresos(comprobanteEgresoGuardado.getId());
		if (comprobanteEgresoGuardado.getId() != null) {
			comprobanteEgresoGuardado.setNumeroDocumento(numeroAnterior);
			comprobanteEgresoDao.findById(comprobanteEgresoGuardado.getId());
			List<ConceptoReciboEgreso> conceptos = conceptoDao.obtenerConceptos(comprobanteEgresoGuardado.getId());
			// List<PagoComprobanteEgreso> pago =
			// pagoDao.pagosComprobanteEgresos(comprobanteEgresoGuardado.getId());
			// for (PagoComprobanteEgreso pagoComprobante : pagosM) {
			// for (ConceptoReciboEgreso concepto : conceptos) {
			// // if
			// //
			// (pagoDao.pagoComprobanteEgresoPorId(pagoComprobante.getId()).get==concepto.getNumeroDocumento())
			// // {

			// // }
			// float descuento = concepto.getDescuento();
			// double descuentovalor = concepto.getValorDescuento();

			// FacturaCompra factura =
			// this.facturaDao.obtenerFacturaNumero(concepto.getNumeroDocumento(),
			// sede.getId());

			// // if (factura != null) {
			// // // EstadoDocumento estado = new EstadoDocumento();
			// // // estado.setId(4);
			// // // factura.setCodEstadoCon(estado);
			// // // this.facturaDao.save(factura);
			// // // logger.info("creo la factura");

			// // // if (body != null && (sede.getId() == 7 || sede.getId() == 15) && 1 !=
			// 1) {
			// // // if (comprobanteEgresoGuardado != null && (sede.getId() == 7 ||
			// sede.getId()
			// // // == 15)) {
			// // // logger.info(comprobanteEgresoGuardado != null && (sede.getId() == 7 ||
			// // // sede.getId() == 15));
			// // // logger.info(
			// // // comprobanteEgresoDao.obtenerComprobantesPorNumeroC(
			// // // comprobanteEgresoGuardado.getNumeroDocumento(), sede.getId()) <= 0);
			// // // // if (comprobanteEgresoDao.obtenerComprobantesPorNumeroC(
			// // // // comprobanteEgresoGuardado.getNumeroDocumento(), sede.getId()) <= 0)
			// {

			// // // // for (PagoComprobanteEgreso pagoCM : pagosM) {
			// // // Integer idSede = sede.getId();
			// // // Integer tipoPago = 1;
			// // // String uri =
			// "http://190.145.10.42:8080/api/v1/TApoloRestInterface/execute";

			// // // logger.info(comprobanteEgresoGuardado.getFechaDocumento().toString());
			// // // logger.info(comprobanteEgresoGuardado.getFechaDocumento().toString());
			// // // Date fechaEntradaVenta = body.getFechaDocumento();
			// // // Calendar calVent = Calendar.getInstance();
			// // // calVent.setTime(fechaEntradaVenta);

			// // // int yearVent = calVent.get(Calendar.YEAR);
			// // // int monthVent = calVent.get(Calendar.MONTH) + 1;
			// // // int dayVent = calVent.get(Calendar.DAY_OF_MONTH);

			// // // Date fechaEntradaVence = comprobanteEgresoGuardado.getFechaDocumento();
			// // // Calendar calVenc = Calendar.getInstance();
			// // // calVenc.setTime(fechaEntradaVence);

			// // // int yearVenc = calVenc.get(Calendar.YEAR);
			// // // int monthVenc = calVenc.get(Calendar.MONTH) + 1;
			// // // int dayVenc = calVenc.get(Calendar.DAY_OF_MONTH);

			// // // String fechaVenta = dayVent + "." + monthVent + "." + yearVent;
			// // // logger.info(fechaVenta);
			// // // String fechaVence = dayVenc + "." + monthVenc + "." + yearVenc;
			// // // logger.info(fechaVence);

			// // // ContableM facturaMekano = new ContableM();
			// // // String tipoDocumento = "E1";
			// // // facturaMekano.setCLAVE("Set_Contable_Primario");
			// // // switch (idSede) {
			// // // case 1:
			// // // tipoDocumento = "E4";
			// // // break;
			// // // case 6:
			// // // tipoDocumento = "E2";
			// // // break;
			// // // case 7:
			// // // tipoDocumento = "E3";
			// // // break;
			// // // case 11:
			// // // tipoDocumento = "E1";
			// // // break;
			// // // case 12:
			// // // tipoDocumento = "E6";
			// // // break;
			// // // case 13:
			// // // tipoDocumento = "E10";
			// // // break;
			// // // case 14:
			// // // tipoDocumento = "E9";
			// // // break;
			// // // case 15:
			// // // tipoDocumento = "E5";
			// // // break;

			// // // default:
			// // // break;
			// // // }
			// // // facturaMekano.setTIPO(tipoDocumento);
			// // // facturaMekano.setPREFIJO("_");
			// // // facturaMekano.setNUMERO(body.getNumeroDocumento().toString());
			// // // facturaMekano.setFECHA(fechaVenta);
			// // // logger.info(pagoComprobante.getCuenta().getCta());
			// // // facturaMekano.setCUENTA(pagoComprobante.getCuenta().getCta());
			// // // facturaMekano.setTERCERO(body.getProveedor().getNit());
			// // // switch (idSede) {
			// // // case 1:
			// // // facturaMekano.setCENTRO("04");
			// // // break;
			// // // case 6:
			// // // facturaMekano.setCENTRO("02");
			// // // break;
			// // // case 7:
			// // // facturaMekano.setCENTRO("03");
			// // // break;
			// // // case 11:
			// // // facturaMekano.setCENTRO("01");
			// // // break;
			// // // case 12:
			// // // facturaMekano.setCENTRO("06");
			// // // break;
			// // // case 13:
			// // // facturaMekano.setCENTRO("10");
			// // // break;
			// // // case 14:
			// // // facturaMekano.setCENTRO("09");
			// // // break;
			// // // case 15:
			// // // facturaMekano.setCENTRO("05");
			// // // break;

			// // // default:
			// // // break;
			// // // }
			// // // facturaMekano.setACTIVO("NA");
			// // // facturaMekano.setEMPLEADO("24347052");
			// // // tipoPago = pagoComprobante.getFormaPago().getId();
			// // // logger.info(concepto.getValorAbono());
			// // // if (tipoPago == 4) {
			// // // facturaMekano.setDEBITO(concepto.getValorAbono().toString());
			// // // facturaMekano.setCREDITO("0");
			// // // facturaMekano.setBASE("0");
			// // // } else if (tipoPago == 3) {
			// // // facturaMekano.setDEBITO("0");
			// // // facturaMekano.setCREDITO(concepto.getValorAbono().toString());
			// // // facturaMekano.setBASE("0");
			// // // } else {
			// // // facturaMekano.setDEBITO(concepto.getValorAbono().toString());
			// // // facturaMekano.setCREDITO("0");
			// // // facturaMekano.setBASE("0");
			// // // }
			// // // facturaMekano.setNOTA(concepto.getConcepto());
			// // // facturaMekano.setUSUARIO("SUPERVISOR");

			// // // Gson gson = new Gson();
			// // // String rjson = gson.toJson(facturaMekano);

			// // // logger.info(rjson);
			// // // HttpHeaders headers = new HttpHeaders();
			// // // headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
			// // // HttpEntity<String> entity = new HttpEntity<String>(rjson, headers);
			// // // logger.info(entity);
			// // // RestTemplate rest = new RestTemplate();

			// // // if (entity != null) {
			// // // String result = rest.postForObject(uri, entity, String.class);

			// // // if (result.contains("EL TERCERO")) {
			// // // if (result.contains("NO EXISTE")) {
			// // // logger.info("existe un error de tercero inexistente");
			// // // this.crearProveedorMekano(comprobanteEgresoGuardado.getProveedor(),
			// idSede);
			// // // // this.crearFacturaMekanoN(comprobanteEgresoGuardado, pagoComprobante,
			// // // sede);
			// // // }
			// // // }
			// // // logger.info(result);
			// // // }
			// // // // }
			// // // // }
			// // // }
			// // }
			// }
			// }
			logger.info(comprobanteEgresoDao.restringirEdicionComprobante(comprobanteEgresoGuardado.getId()));
			if (comprobanteEgresoDao.restringirEdicionComprobante(comprobanteEgresoGuardado.getId()) == false) {
				body.setRestringido(false);
			} else {
				body.setRestringido(true);
			}
		}
		// if (body == null) {
		// logger.info("no existe body comprobante de egreso");
		// }
		// ComprobanteEgreso comprobanteEgresoGuardado = null;
		// if (!body.isRestringido()
		// &&
		// comprobanteEgresoDao.obtenerComprobantesPorNumeroC(body.getNumeroDocumento(),
		// sede.getId()) <= 0) {

		// body.setRestringido(true);

		// logger.info(body.getId());
		// comprobanteEgresoGuardado = comprobanteEgresoDao.save(body);
		// Integer cantidadComprobantes =
		// comprobanteEgresoDao.obtenerComprobantesPorNumeroC(
		// comprobanteEgresoGuardado.getNumeroDocumento(), sede.getId());
		// if (cantidadComprobantes > 1) {
		// logger.info("existen comprobantes repetidos");
		// }
		// }

		if (comprobanteEgresoGuardado == null) {
			logger.info("no existe comprobante de egreso guardado");
		}
		return comprobanteEgresoGuardado;

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

	// public void crearFacturaMekanoN(ComprobanteEgreso body, PagoComprobanteEgreso
	// pagoCM, Organizacion sede) {
	// if
	// (comprobanteEgresoDao.obtenerComprobantesPorNumeroC(body.getNumeroDocumento(),
	// sede.getId()) <= 0) {

	// Integer idSede = sede.getId();
	// Integer tipoPago = 1;
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
	// facturaMekano.setNUMERO(body.getNumeroDocumento().toString());
	// facturaMekano.setFECHA(fechaVenta);
	// logger.info(pagoCM.getCuenta().getCta());
	// facturaMekano.setCUENTA(pagoCM.getCuenta().getCta());
	// facturaMekano.setTERCERO(body.getProveedor().getNit());
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
	// facturaMekano.setEMPLEADO("24347052");
	// tipoPago = pagoCM.getFormaPago().getId();
	// if (tipoPago == 4) {
	// facturaMekano.setDEBITO(pagoCM.getValor().toString());
	// facturaMekano.setCREDITO("0");
	// facturaMekano.setBASE("0");
	// } else if (tipoPago == 3) {
	// facturaMekano.setDEBITO("0");
	// facturaMekano.setCREDITO(pagoCM.getValor().toString());
	// facturaMekano.setBASE("0");
	// } else {
	// facturaMekano.setDEBITO(pagoCM.getValor().toString());
	// facturaMekano.setCREDITO("0");
	// facturaMekano.setBASE("0");
	// }
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
	// logger.info(result);
	// }
	// }
	// }

	private Integer validarConsecutivo(Integer idSede, ConsecutivoEgresoSede consecutivo, Integer numeroRemision) {
		ConsecutivoEgresoSede consec = new ConsecutivoEgresoSede();
		Integer numero = consecutivoDao.obtenerNumeroMaximo(idSede);
		Integer numeroGuardar;
		if (numero > numeroRemision) {
			numeroGuardar = numero + 1;
			consec.setValorActual(numeroGuardar);
		} else {
			numeroGuardar = numeroRemision;
			consec.setValorActual(numeroGuardar);
		}
		consec.setValorActual(numeroGuardar);
		consec.setSede(consecutivo.getSede());
		consec.setPrefijo(consecutivo.getPrefijo());
		consecutivoDao.save(consec);
		return numeroGuardar;
	}

	private void cambiarEstadoFacturaPorAsignacion(ComprobanteEgreso body, boolean conceptoAsignacion, Integer idSede) {
		if (!conceptoAsignacion) {
			FacturaCompra facturaCompra = facturaDao.obtenerFacturaNumero(body.getAsignacion().getNumero(), idSede);
			EstadoDocumento estado = new EstadoDocumento();
			estado.setId(4);
			facturaCompra.setCodEstadoCon(estado);
			this.facturaDao.save(facturaCompra);
		}
	}

	private String obtenerPrefijoNotaDebito(String numeroDocumento) {
		String separador[] = numeroDocumento.split("-");
		String nombrePrefijo = separador[0];
		return nombrePrefijo;
	}

	private String obtenerNumeroNotaDebito(String numeroDocumento) {
		String separador[] = numeroDocumento.split("-");
		String numero = separador[1];
		return numero;

	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> listarComprobantes(String fechaInicio, String fechaFin, Integer estado,
			String numeroComprobante, Integer page) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Pageable paging = PageRequest.of(page, 10);
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao.buscarPorIdUsuario(usuarioAutenticado.getId());
		Integer idsede = usuarioInformacion.getIdOrganizacion().getId();

		try {
			List<ComprobanteEgreso> comprobantes;
			StringBuilder query = new StringBuilder();
			StringBuilder queryCantidad = new StringBuilder();
			generarQuery(query, fechaInicio, fechaFin, estado, numeroComprobante, idsede);

			TypedQuery<ComprobanteEgreso> comprobantesInfoQuery = (TypedQuery<ComprobanteEgreso>) entityManager
					.createNativeQuery(query.toString(), ComprobanteEgreso.class);

			int pageNumber = paging.getPageNumber();
			int pageSize = paging.getPageSize();
			comprobantesInfoQuery.setFirstResult(pageNumber * pageSize);
			comprobantesInfoQuery.setMaxResults(pageSize);
			comprobantes = comprobantesInfoQuery.getResultList();//////////////////////////////// aca se genera mal la
																														//////////////////////////////// consulta al intentar hacer
																														//////////////////////////////// un getresult

			for (ComprobanteEgreso comprobante : comprobantes) {
				List<PagoComprobanteEgreso> pagos = pagoDao.pagosComprobanteEgresos(comprobante.getId());
				List<DescuentoComprobanteEgreso> descuentos = descuentoDao.descuentosComprobante(comprobante.getId());
				List<ConceptoReciboEgreso> conceptos = conceptoDao.obtenerConceptos(comprobante.getId());
				List<RetencionComprobanteEgreso> retenciones = retencionDao.obtenerRetencionesComprobante(comprobante.getId());
				comprobante.setPagos(pagos);
				comprobante.setConceptos(conceptos);
				comprobante.setDescuentos(descuentos);
				comprobante.setListRetenciones(retenciones);
			}
			generarQueryCantidad(queryCantidad, fechaInicio, fechaFin, estado, numeroComprobante, idsede);

			Query cantidad = entityManager.createNativeQuery(queryCantidad.toString());
			BigInteger countResult = (BigInteger) cantidad.getSingleResult();
			Integer cantidadTotal = countResult.intValue();

			Page<ComprobanteEgreso> result = new PageImpl<ComprobanteEgreso>(comprobantes, paging, cantidadTotal);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "exito en obtener comprobantes");
			respuestaDto.setObjetoRespuesta(result);

			respuestaDto.setVariable(String.valueOf(obtenerTotal(fechaInicio, fechaFin, estado, numeroComprobante, idsede)));

			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error("Error obteniendo comprobantes " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error obteniendo comprobantes");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> eliminarComprobante(Integer idComprobante) {
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		Organizacion sede = usuarioInformacion.getIdOrganizacion();
		try {
			List<ConceptoReciboEgreso> conceptos = conceptoDao.obtenerConceptos(idComprobante);
			for (ConceptoReciboEgreso concepto : conceptos) {
				FacturaCompra factura = this.facturaDao.obtenerFacturaNumero(concepto.getNumeroDocumento(), sede.getId());
				if (factura != null) {
					EstadoDocumento estado = new EstadoDocumento();
					estado.setId(5);
					factura.setCodEstadoCon(estado);
					this.facturaDao.save(factura);
				}
			}
			pagoDao.eliminarPagos(idComprobante);
			descuentoDao.eliminarDescuento(idComprobante);
			retencionDao.eliminarRetencion(idComprobante);
			conceptoDao.eliminarConceptos(idComprobante);
			ComprobanteEgreso comprobante = comprobanteEgresoDao.findById(idComprobante).orElse(null);
			if (comprobante.getAsignacion() != null) {
				asignacionComprobanteDao.deleteById(comprobante.getAsignacion().getId());
			}
			if (comprobante.getAsignacion() != null && comprobante.getAsignacion().getNotaCredito() != null) {
				comprobante.getAsignacion().getNotaCredito().setEstadoDocumento("Pendiente");
				notaCreditoDao.save(comprobante.getAsignacion().getNotaCredito());
			}
			comprobanteEgresoDao.deleteById(idComprobante);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "exito al eliminar el comprobantes");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error(
					"Error eliminando comprobantes " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error eliminando comprobantes");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@SuppressWarnings("unused")
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> actualizarComprobante(ComprobanteEgreso body) {
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		Organizacion sede = usuarioInformacion.getIdOrganizacion();
		try {
			List<ConceptoReciboEgreso> conceptosGuardados = conceptoDao.obtenerConceptos(body.getId());
			for (ConceptoReciboEgreso concepto : conceptosGuardados) {
				FacturaCompra factura = this.facturaDao.obtenerFacturaNumero(concepto.getNumeroDocumento(), sede.getId());
				String nombrePrefijo;
				String numeroPrefijo;
				NotaDebito notaDebito = null;
				if (factura == null && body.getTipoDocumento().equals("Proveedores")) {
					nombrePrefijo = obtenerPrefijoNotaDebito(concepto.getNumeroDocumento());
					numeroPrefijo = obtenerNumeroNotaDebito(concepto.getNumeroDocumento());
					notaDebito = this.notaDebitoDao.findByIdSedeNumeroYPrefijo(sede.getId(), numeroPrefijo, nombrePrefijo);
					notaDebito.setEstadoDocumento("Pendiente");
					this.notaDebitoDao.save(notaDebito);
				} else if (factura != null && body.getTipoDocumento().equals("Proveedores")) {
					logger.info("cambio de estado a pendiente");
					EstadoDocumento estado = new EstadoDocumento();
					estado.setId(2);
					factura.setCodEstadoCon(estado);
					this.facturaDao.save(factura);
				}
			}
			pagoDao.eliminarPagos(body.getId());
			descuentoDao.eliminarDescuento(body.getId());
			retencionDao.eliminarRetencion(body.getId());
			conceptoDao.eliminarConceptos(body.getId());
			if (body.getAsignacion() != null) {
				asignacionComprobanteDao.deleteById(body.getAsignacion().getId());
			}
			int asignacionComprobante = 0;
			boolean conceptoAsignacion = false;
			for (ConceptoReciboEgreso concepto : body.getConceptos()) {
				FacturaCompra factura = this.facturaDao.obtenerFacturaNumero(concepto.getNumeroDocumento(), sede.getId());
				String nombrePrefijo;
				String numeroPrefijo;
				NotaDebito notaDebito = null;
				if (factura == null && body.getTipoDocumento().equals("Proveedores")) {
					nombrePrefijo = obtenerPrefijoNotaDebito(concepto.getNumeroDocumento());
					numeroPrefijo = obtenerNumeroNotaDebito(concepto.getNumeroDocumento());
					notaDebito = this.notaDebitoDao.findByIdSedeNumeroYPrefijo(sede.getId(), numeroPrefijo, nombrePrefijo);
				}
				Integer asignacion = asignacionComprobanteDao.obtenerTotal(concepto.getNumeroDocumento(), sede.getId());
				Integer conceptoTotal = this.conceptoDao.obtenerTotalAbonos(concepto.getNumeroDocumento(), sede.getId());
				Integer totalDescuento = this.conceptoDao.obtenerTotalDescuentos(concepto.getNumeroDocumento(), sede.getId());
				if (body.getAsignacion() != null && body.getAsignacion().getNumero().equals(concepto.getNumeroDocumento())) {
					asignacionComprobante = body.getAsignacion().getNotaCredito().getTotal();
					conceptoAsignacion = true;
				}
				Double abono = 0.0;
				if (asignacion == null)
					asignacion = 0;
				if (conceptoTotal == null)
					conceptoTotal = 0;
				if (totalDescuento == null)
					totalDescuento = 0;
				if (concepto.getValorAbono() != null)
					abono = concepto.getValorAbono();
				if (factura != null
						&& factura.getTotal() - asignacion.doubleValue() - abono.intValue() - conceptoTotal.intValue()
								- totalDescuento - concepto.getDescuento() - asignacionComprobante <= 0) {
					EstadoDocumento estado = new EstadoDocumento();
					estado.setId(4);
					factura.setCodEstadoCon(estado);
					this.facturaDao.save(factura);
				} else if (notaDebito != null
						&& notaDebito.getTotal() - asignacion.doubleValue() - abono.intValue() - conceptoTotal.intValue()
								- totalDescuento - concepto.getDescuento() - asignacionComprobante <= 0) {
					notaDebito.setEstadoDocumento("Pagado");
					this.notaDebitoDao.save(notaDebito);
				}
			}
			if (body.getAsignacion() != null) {
				cambiarEstadoFacturaPorAsignacion(body, conceptoAsignacion, sede.getId());
				body.getAsignacion().getNotaCredito().setEstadoDocumento("Asignado");
				notaCreditoDao.save(body.getAsignacion().getNotaCredito());
				comprobanteEgresoDao.editarComprobanteEgreso(body.getAsignacion().getId(), body.getId()); // !
			}

			// comprobanteEgresoDao.save(body);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "exito al actualizar el comprobantes");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error(
					"Error actualizando comprobantes " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error actualizando comprobantes");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	private void generarQuery(StringBuilder query, String fechaInicio, String fechaFin, Integer estado,
			String numeroComprobante, Integer idSede) {
		query.append("select * from comprobante_egreso ");
		if (!fechaInicio.equals("null") && fechaFin.equals("null")) {
			query.append("where date_format(date(fecha_documento),'%Y-%m-%d')" + "= date('" + fechaInicio + "')");
		} else if (fechaInicio.equals("null") && !fechaFin.equals("null")) {
			query.append("where date_format(date(fecha_documento),'%Y-%m-%d')" + "<= date('" + fechaFin + "')");
		} else if (!fechaInicio.equals("null") && !fechaFin.equals("null")) {
			query.append("where date_format(date(fecha_documento),'%Y-%m-%d')" + "between " + "date('" + fechaInicio
					+ "') and " + "date('" + fechaFin + "')");
		}
		if ((!numeroComprobante.equals("null")) && (!fechaInicio.equals("null") || !fechaFin.equals("null"))) {
			query.append(" and numero_documento='" + numeroComprobante + "'");
		} else if ((!numeroComprobante.equals("null")) && (fechaInicio.equals("null") || fechaFin.equals("null"))) {
			query.append("where numero_documento='" + numeroComprobante + "'");
		}
		if ((estado != 0) && ((!fechaInicio.equals("null") || !fechaFin.equals("null"))
				|| !numeroComprobante.equals("null"))) {
			query.append(" and cod_estado_con='" + estado + "'");
		} else if ((estado != 0)
				&& ((fechaInicio.equals("null") || fechaFin.equals("null") || numeroComprobante.equals("null")))) {
			query.append("where cod_estado_con='" + estado + "'");
		}
		if ((idSede != 0) && ((!fechaInicio.equals("null") || !fechaFin.equals("null"))
				|| !numeroComprobante.equals("null") || estado != 0)) {
			query.append(" and id_sede= " + idSede);
		} else if ((idSede != 0)
				|| ((fechaInicio.equals("null") || fechaFin.equals("null") || numeroComprobante.equals("null")
						|| estado == 0))) {
			query.append("where id_sede= " + idSede);
		}
		// query.append(" and id_sede = " + idSede);

		// query.append(" order by fecha_documento desc");
	}

	private void generarQueryCantidad(StringBuilder query, String fechaInicio, String fechaFin, Integer estado,
			String numeroComprobante, Integer idSede) {
		query.append("select count(*) from comprobante_egreso ");
		if (!fechaInicio.equals("null") && fechaFin.equals("null")) {
			query.append("where date_format(date(fecha_documento),'%Y-%m-%d')" + "= date('" + fechaInicio + "')");
		} else if (fechaInicio.equals("null") && !fechaFin.equals("null")) {
			query.append("where date_format(date(fecha_documento),'%Y-%m-%d')" + "<= date('" + fechaFin + "')");
		} else if (!fechaInicio.equals("null") && !fechaFin.equals("null")) {
			query.append("where date_format(date(fecha_documento),'%Y-%m-%d')" + "between " + "date('" + fechaInicio
					+ "') and " + "date('" + fechaFin + "')");
		}
		if ((!numeroComprobante.equals("null")) && (!fechaInicio.equals("null") || !fechaFin.equals("null"))) {
			query.append(" and numero_documento='" + numeroComprobante + "'");
		} else if ((!numeroComprobante.equals("null")) && (fechaInicio.equals("null") || fechaFin.equals("null"))) {
			query.append("where numero_documento='" + numeroComprobante + "'");
		}
		if ((estado != 0) && ((!fechaInicio.equals("null") || !fechaFin.equals("null"))
				|| !numeroComprobante.equals("null"))) {
			query.append(" and cod_estado_con='" + estado + "'");
		} else if ((estado != 0)
				&& ((fechaInicio.equals("null") || fechaFin.equals("null") || numeroComprobante.equals("null")))) {
			query.append("where cod_estado_con='" + estado + "'");
		}
		if ((idSede != 0) && ((!fechaInicio.equals("null") || !fechaFin.equals("null"))
				|| !numeroComprobante.equals("null") || estado != 0)) {
			query.append(" and id_sede= " + idSede);
		} else if ((idSede != 0)
				&& ((fechaInicio.equals("null") || fechaFin.equals("null") || numeroComprobante.equals("null")
						|| estado == 0))) {
			query.append("where id_sede= " + idSede);
		}
		// query.append(" and id_sede = " + idSede);

		// query.append(" order by fecha_documento desc");
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> listarComprobantesTodos(String fechaInicio, String fechaFin, Integer estado,
			String numeroComprobante) {
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		Organizacion sede = usuarioInformacion.getIdOrganizacion();
		try {
			List<ComprobanteEgreso> comprobantes;
			StringBuilder query = new StringBuilder();
			generarQuery(query, fechaInicio, fechaFin, estado, numeroComprobante, sede.getId());
			TypedQuery<ComprobanteEgreso> comprobantesInfoQuery = (TypedQuery<ComprobanteEgreso>) entityManager
					.createNativeQuery(query.toString(), ComprobanteEgreso.class);
			comprobantes = comprobantesInfoQuery.getResultList();
			for (ComprobanteEgreso comprobante : comprobantes) {
				List<PagoComprobanteEgreso> pagos = pagoDao.pagosComprobanteEgresos(comprobante.getId());
				List<DescuentoComprobanteEgreso> descuentos = descuentoDao.descuentosComprobante(comprobante.getId());
				List<ConceptoReciboEgreso> conceptos = conceptoDao.obtenerConceptos(comprobante.getId());
				List<RetencionComprobanteEgreso> retenciones = retencionDao
						.obtenerRetencionesComprobante(comprobante.getId());
				comprobante.setPagos(pagos);
				comprobante.setConceptos(conceptos);
				comprobante.setDescuentos(descuentos);
				comprobante.setListRetenciones(retenciones);
			}
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "exito en obtener comprobantes");
			respuestaDto.setObjetoRespuesta(comprobantes);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error(
					"Error obteniendo comprobantes " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error obteniendo comprobantes");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	private void generarQuerySuma(StringBuilder query, String fechaInicio, String fechaFin, Integer estado,
			String numeroComprobante, Integer idSede) {
		query.append("select sum(total) from comprobante_egreso ");
		if (!fechaInicio.equals("null") && fechaFin.equals("null")) {
			query.append("where date_format(date(fecha_documento),'%Y-%m-%d')" + "= date('" + fechaInicio + "')");
		} else if (fechaInicio.equals("null") && !fechaFin.equals("null")) {
			query.append("where date_format(date(fecha_documento),'%Y-%m-%d')" + "<= date('" + fechaFin + "')");
		} else if (!fechaInicio.equals("null") && !fechaFin.equals("null")) {
			query.append("where date_format(date(fecha_documento),'%Y-%m-%d')" + "between " + "date('" + fechaInicio
					+ "') and " + "date('" + fechaFin + "')");
		}
		if ((!numeroComprobante.equals("null")) && (!fechaInicio.equals("null") || !fechaFin.equals("null"))) {
			query.append(" and numero_documento='" + numeroComprobante + "'");
		} else if ((!numeroComprobante.equals("null")) && (fechaInicio.equals("null") || fechaFin.equals("null"))) {
			query.append("where numero_documento='" + numeroComprobante + "'");
		}
		if ((estado != 0) && (!fechaInicio.equals("null") || !fechaFin.equals("null"))
				|| !numeroComprobante.equals("null")) {
			query.append(" and cod_estado_con='" + estado + "'");
		} else if ((estado != 0)
				&& (fechaInicio.equals("null") || fechaFin.equals("null") || numeroComprobante.equals("null"))) {
			query.append("where cod_estado_con='" + estado + "'");
		}
		if ((idSede != 0) && ((!fechaInicio.equals("null") || !fechaFin.equals("null"))
				|| !numeroComprobante.equals("null") || estado != 0)) {
			query.append(" and id_sede= " + idSede);
		} else if ((idSede != 0)
				|| ((fechaInicio.equals("null") || fechaFin.equals("null") || numeroComprobante.equals("null")
						|| estado == 0))) {
			query.append("where id_sede= " + idSede);
		}
	}

	@SuppressWarnings("unused")
	private int obtenerTotal(String fechaInicio, String fechaFin, Integer estado, String numeroComprobante,
			Integer idSede) {
		StringBuilder queryTotal = new StringBuilder();
		generarQuerySuma(queryTotal, fechaInicio, fechaFin, estado, numeroComprobante, idSede);
		Query resultQuery = entityManager.createNativeQuery(queryTotal.toString());

		BigDecimal result = (BigDecimal) resultQuery.getSingleResult();

		int total = 0;
		if (result != null)
			// total = result.doubleValue();
			total = result.intValue();
		return total;
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> listarComprobantesPagosDocumentos(String numeroComprobante, Integer page) {
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Pageable paging = PageRequest.of(page, 10);
		int idComprobante = 0;
		String iComprobante = null;
		String numerodocumento = null;
		String prefijo = null;
		float descuento = 0;
		try {
			int numeroDeAsignaciones = 0;
			ArrayList<ReciboDocumentoPago> recibos = new ArrayList<ReciboDocumentoPago>();
			Page<ConceptoReciboEgreso> conceptos = conceptoDao.obtenerConceptos(numeroComprobante,
					paging);
			for (ConceptoReciboEgreso concepto : conceptos) {
				ComprobanteEgreso comprobante = comprobanteEgresoDao.obtenerComprobante(concepto.getId());
				comprobante.getConceptos().add(concepto);
				idComprobante = comprobante.getNumeroDocumento();
				numerodocumento = String.valueOf(idComprobante);
				prefijo = comprobante.getPrefijo();
				iComprobante = (prefijo + " " + numerodocumento);
				descuento = concepto.getDescuento();

				concepto.setNombre(iComprobante);
				ReciboDocumentoPago documento = ReciboDocumentoPago.convertirReciboComprobante(comprobante, concepto);
				recibos.add(documento);
			}
			ComprobanteEgreso comprobante = comprobanteEgresoDao.obtenerComprobantesNumeroAsignacion(numeroComprobante);
			if (comprobante != null && comprobante.getAsignacion() != null) {
				ReciboDocumentoPago documento = ReciboDocumentoPago.convertirAsignacionComprobante(comprobante);
				recibos.add(documento);
				numeroDeAsignaciones += 1;
			}
			Page<ReciboDocumentoPago> result = new PageImpl<ReciboDocumentoPago>(recibos, paging,
					conceptos.getTotalElements() + numeroDeAsignaciones);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "exito en obtener comprobantes");
			respuestaDto.setObjetoRespuesta(result);
			respuestaDto.setVariable(String.valueOf(comprobanteEgresoDao.obtenerValorAbonos(numeroComprobante) +
					comprobanteEgresoDao.obtenerValorAsignacion(numeroComprobante)));

			int i = (int) descuento;
			respuestaDto.setSuma(i);
			respuestaDto.setCodigo(iComprobante);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error(
					"Error obteniendo comprobantes " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error obteniendo comprobantes");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> listarComprobantesPagosDocumentosNotasCredito(String numeroComprobante,
			Integer page) {
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Pageable paging = PageRequest.of(page, 10);
		try {
			Page<ComprobanteEgreso> result = comprobanteEgresoDao
					.obtenerComprobantesPorNumeroNotaCredito(numeroComprobante, paging);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "exito en obtener comprobantes");
			respuestaDto.setObjetoRespuesta(result);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error(
					"Error obteniendo comprobantes " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error obteniendo comprobantes");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerComprobantesGastosServicios(String numeroComprobante, Integer page) {
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Pageable paging = PageRequest.of(page, 10);
		int idComprobante = 0;

		try {
			ArrayList<ReciboDocumentoPago> recibos = new ArrayList<ReciboDocumentoPago>();
			Page<ComprobanteEgreso> comprobantes = comprobanteEgresoDao.obtenerComprobantesGastosServicios(numeroComprobante,
					paging);
			for (ComprobanteEgreso comprobante : comprobantes) {

				ReciboDocumentoPago documento = ReciboDocumentoPago.convertirReciboComprobanteGastosServicio(comprobante);
				recibos.add(documento);
				idComprobante = comprobante.getId();

			}
			Page<ReciboDocumentoPago> result = new PageImpl<ReciboDocumentoPago>(recibos, paging,
					comprobantes.getTotalElements());
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "exito en obtener comprobantes");
			respuestaDto.setObjetoRespuesta(result);
			respuestaDto.setVariable(String.valueOf(comprobanteEgresoDao.obtenerValorComprobante(numeroComprobante)));
			respuestaDto.setCodigo("0");
			respuestaDto.setSuma(idComprobante);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error(
					"Error obteniendo comprobantes " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error obteniendo comprobantes");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> listarComprobantesMes(Integer page) {
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Pageable paging = PageRequest.of(page, 10);
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao.buscarPorIdUsuario(usuarioAutenticado.getId());
		Organizacion sede = usuarioInformacion.getIdOrganizacion();
		MaestroValor rol = maestrovalorDao.buscarRol(usuarioInformacion.getIdRol().getId());
		try {
			Page<ComprobanteEgreso> comprobantes = comprobanteEgresoDao.obtenerComprobantesPorMes(sede.getId(), paging);
			for (ComprobanteEgreso comprobante : comprobantes) {
				List<PagoComprobanteEgreso> pagos = pagoDao.pagosComprobanteEgresos(comprobante.getId());
				List<DescuentoComprobanteEgreso> descuentos = descuentoDao.descuentosComprobante(comprobante.getId());
				List<ConceptoReciboEgreso> conceptos = conceptoDao.obtenerConceptos(comprobante.getId());
				List<RetencionComprobanteEgreso> retenciones = retencionDao.obtenerRetencionesComprobante(comprobante.getId());
				comprobante.setPagos(pagos);
				comprobante.setConceptos(conceptos);
				comprobante.setDescuentos(descuentos);
				comprobante.setListRetenciones(retenciones);
			}
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "exito en obtener comprobantes");
			respuestaDto.setObjetoRespuesta(comprobantes);
			respuestaDto.setDescripcion(rol.getNombre());
			respuestaDto.setVariable(String.valueOf(comprobanteEgresoDao.obtenerTotalComprobantesPorMes(sede.getId())));
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error(
					"Error obteniendo comprobantes " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error obteniendo comprobantes");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerRetencion(ComprobanteEgreso comprobante) {
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		Organizacion sede = usuarioInformacion.getIdOrganizacion();
		Integer total = 0;
		try {
			for (ConceptoReciboEgreso concepto : comprobante.getConceptos()) {
				FacturaCompra factura = facturaDao.obtenerFacturaNumero(concepto.getNumeroDocumento(), sede.getId());
				List<FacturaRetenciones> retenciones = facturaRetencionesDao.obtenerRetencionesFactura(factura.getId());
				factura.setListRetenciones(retenciones);
				for (FacturaRetenciones retencion : factura.getListRetenciones()) {
					if (retencion.isRealizadaEnComprobante()) {
						total += retencion.getValor();
					}
				}

			}
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "exito en obtener retencion");
			respuestaDto.setObjetoRespuesta(total);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error(
					"Error obteniendo retencion " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error obteniendo retencion");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> modificarEditableComprobante(Integer idComprobante) {
		logger.info(idComprobante);
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		Organizacion sede = usuarioInformacion.getIdOrganizacion();
		try {
			// ComprobanteEgreso comprobante =
			// comprobanteEgresoDao.findById(idComprobante).orElse(null);
			ComprobanteEgreso comprobante = comprobanteEgresoDao.obtenerComprobantePorId(idComprobante);
			if (comprobante != null) {
				logger.info(comprobante.getId());
				comprobante.setEditable(true);
				EstadoDocumento documento = new EstadoDocumento();
				documento.setId(4);
				comprobante.setEstado(documento);
				comprobante.setRestringido(false);
				if (comprobante.getId() == null) {
					logger.info("se encontro un comprobante sin id");
					Integer idComprobanteP = comprobanteEgresoDao.obtenerIdComprobante(
							comprobante.getNumeroDocumento().toString(),
							comprobante.getSede().getId());
					ComprobanteEgreso comprobanteN = comprobanteEgresoDao.obtenerComprobantePorId(idComprobanteP);
					comprobanteEgresoDao.editarComprobanteEgresoEditable(comprobanteN.isEditable(),
							comprobanteN.getId(),
							comprobanteN.isRestringido());
					Integer cantidadComprobantes = comprobanteEgresoDao.obtenerComprobantesPorNumeroC(
							comprobanteN.getNumeroDocumento(), sede.getId());
					// ! crear metodo que elimine el comprobante duplicado pendiente y deje el
					// pagado ademas que actualice la secuencia mayor al comprobante
					// ! de los comprobantes cuyo numero sea (pagado-1) hasta que se recorran todos
					// los comprobantes hasta el mas actual de la sede
					// Integer cantidadComprobantes =
					// comprobanteEgresoDao.obtenerComprobantesPorNumeroC(
					// comprobanteN.getNumeroDocumento().toString(), sede.getId());
					if (cantidadComprobantes > 1) {
						logger.info("existen comprobantes repetidos");
					}
				} else {
					comprobanteEgresoDao.editarComprobanteEgresoEditable(comprobante.isEditable(),
							comprobante.getId(),
							comprobante.isRestringido());
					Integer cantidadComprobantes = comprobanteEgresoDao
							.obtenerComprobantesPorNumeroC(comprobante.getNumeroDocumento(), sede.getId());
					if (cantidadComprobantes > 1) {
						logger.info("existen comprobantes repetidos");
						// List<Integer> idsComprobantesPendientesRepetidos = comprobanteEgresoDao
						// .obtenerComprobantesPendientesPorNumeroC(comprobante.getNumeroDocumento(),
						// sede.getId());
						// for (Integer idComprobanteRepetido : idsComprobantesPendientesRepetidos) {
						// conceptoDao.eliminarConceptos(idComprobanteRepetido);
						// pagoDao.eliminarPagos(idComprobanteRepetido);
						// retencionDao.eliminarRetencion(idComprobanteRepetido);
						// descuentoDao.eliminarDescuento(idComprobanteRepetido);
						// comprobanteEgresoDao.eliminarComprobantesPendientesRepetidosPorId(idComprobanteRepetido);
						// //actualizar numero de documento en comprobantes
						// comprobanteEgresoDao.editarComprobanteEgreso(cantidadComprobantes,
						// idComprobante);
						// //actualizar numero de secuencia
						// }
					}
				}
			}
			// comprobante.setEditable(true);
			// EstadoDocumento documento = new EstadoDocumento();
			// documento.setId(4);
			// comprobante.setEstado(documento);
			// if (comprobante != null) {
			// logger.info(comprobante.getId());
			// }
			// comprobante.setRestringido(false);
			// if (comprobante.getId() == null) {
			// logger.info("se encontro un comprobante sin id");
			// Integer idComprobanteP =
			// comprobanteEgresoDao.obtenerIdComprobante(comprobante.getNumeroDocumento().toString(),
			// comprobante.getSede().getId());
			// ComprobanteEgreso comprobanteN =
			// comprobanteEgresoDao.obtenerComprobantePorId(idComprobanteP);
			// comprobanteEgresoDao.editarComprobanteEgresoEditable(comprobanteN.isEditable(),
			// comprobanteN.getId(),
			// comprobanteN.isRestringido());
			// } else {
			// comprobanteEgresoDao.editarComprobanteEgresoEditable(comprobante.isEditable(),
			// comprobante.getId(),
			// comprobante.isRestringido());
			// }

			// logger.info(comprobanteEgresoDao.obtenerCantidadComprobantesPorId(comprobante.getNumeroDocumento()));//
			// !
			// comprobanteEgresoDao.save(comprobante);// ! revisar si con este cambio se
			// elimina el comprobante o solo queda el
			// comprobante inicial pero con los valores modificados

			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "exito al editar estado comprobante");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error(
					"Error al editar estado comprobante " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al editar estado comprobante");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	// ! //TODO: aca se crea el comprobante para mekano
	public ResponseEntity<Object> crearComprobanteMekano(String[] fila,
			ComprobanteEgreso comprobante)
			throws Exception {
		if (fila != null) {
			logger.info(fila);
			if (comprobante.getFechaDocumento() != null) {
				logger.info(comprobante.getFechaDocumento());
			}
		}
		logger.info(fila[0]);
		logger.info(fila[1]);
		logger.info(fila[2]);
		logger.info(fila[3]);

		RespuestaDto respuestaDto;
		ResponseEntity<Object> respuesta;
		// ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());

		Integer idSede = usuarioInformacion.getIdOrganizacion().getId();
		logger.info("FacturaCompraService => crearFacturaCompraMekano => idSede:" +
				idSede);
		try {
			// this.contable.clave = "Set_Contable_Primario";
			String tipoDocumento = "E1";
			String centro = "05";
			String uri = "http://190.145.10.42:8080/api/v1/TApoloRestInterface/execute";

			logger.info(comprobante.getFechaDocumento());
			SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
			String fech = comprobante.getFechaDocumento().toString();
			logger.info(fech);
			logger.info(comprobante.getFechaDocumento());
			// Date dataFormateada = formato.parse(fech);
			// logger.info(dataFormateada);
			// // logger.info(body.getFechaDocumento().toString());
			// Date fechaEntradaVenta = (Date) contable.getFECHA();
			Date fechaEntradaVenta = comprobante.getFechaDocumento();
			Calendar calVent = Calendar.getInstance();
			calVent.setTime(fechaEntradaVenta);

			int yearVent = calVent.get(Calendar.YEAR);
			int monthVent = calVent.get(Calendar.MONTH) + 1;
			int dayVent = calVent.get(Calendar.DAY_OF_MONTH);

			Date fechaEntradaVence = comprobante.getFechaDocumento();
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
					tipoDocumento = "E4";
					centro = "04";
					break;
				case 6:
					tipoDocumento = "E2";
					centro = "02";
					break;
				case 7:
					tipoDocumento = "E3";
					centro = "03";
					break;
				case 11:
					tipoDocumento = "E1";
					centro = "01";
					break;
				case 12:
					tipoDocumento = "E6";
					centro = "06";
					break;
				case 13:
					tipoDocumento = "E10";
					centro = "10";
					break;
				case 14:
					tipoDocumento = "E9";
					centro = "09";
					break;
				case 15:
					tipoDocumento = "E5";
					centro = "05";
					break;
				default:
					tipoDocumento = "E5";
					centro = "05";
					break;
			}

			ContableM comprobanteMekano = new ContableM();
			comprobanteMekano.setCLAVE("Set_Contable_Primario");
			// logger.info(contable.getTIPO());
			comprobanteMekano.setTIPO(tipoDocumento);
			comprobanteMekano.setPREFIJO("_");
			// logger.info(factura.getNroFactura().toString());
			comprobanteMekano.setNUMERO(comprobante.getNumeroDocumento().toString());
			comprobanteMekano.setFECHA(fechaVenta);
			// facturaMekano.setFECHA(fechaVenta);
			comprobanteMekano.setCUENTA(fila[0].toString());
			comprobanteMekano.setTERCERO(comprobante.getProveedor().getNit());
			comprobanteMekano.setCENTRO(centro);
			comprobanteMekano.setACTIVO("NA");
			comprobanteMekano.setEMPLEADO("24347052");
			comprobanteMekano.setDEBITO(fila[2].toString());
			comprobanteMekano.setCREDITO(fila[3].toString());
			comprobanteMekano.setBASE("0");
			comprobanteMekano.setNOTA(comprobante.getObservaciones().trim());
			comprobanteMekano.setUSUARIO("SUPERVISOR");

			Gson gson = new Gson();
			String rjson = gson.toJson(comprobanteMekano);

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
						logger.info("existe un error de tercero inexistente");
						throw new Exception("No existe TERCERO con este CODIGO");
						// Proveedor proveedor = proveedorDao.findByNit(contable.getTERCERO());
						// this.crearProveedorMekano(proveedor, idSede);
						// this.crearFacturaMekanoN(contable, fechaFactura);
					}
				}
				logger.info(result);

				if (result.contains("EL DOCUMENTO")) {
					if (result.contains("NO EXISTE")) {
						// logger.info("existe un error de CENTRO inexistente");
						// throw new Exception("No existe CENTRO con este CODIGO");
						logger.info("No existe CENTRO con este CODIGO");
						respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
								"Error al guardar el comprobante, No existe CENTRO con este CODIGO");
						respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
						return respuesta;
					}
				}
				logger.info(result);

				if (result.contains("LA CUENTA")) {
					if (result.contains("NO EXISTE")) {
						// logger.info("existe un error de CENTRO inexistente");
						// throw new Exception("No existe CENTRO con este CODIGO");
						logger.info("No existe CUENTA con este CODIGO");
						respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
								"Error al guardar el comprobante, No existe CUENTA con este CODIGO");
						respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
						return respuesta;
					}
				}
				logger.info(result);

				if (result.contains("LA CUENTA MANEJA TERCERO, ESTE DATO NO PUEDE ESTAR VACIO")) {
					// logger.info("existe un error de CENTRO inexistente");
					// throw new Exception("No existe CENTRO con este CODIGO");
					logger.info("No existe CUENTA con tercero con este CODIGO");
					respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
							"Error al guardar el comprobante, No existe CUENTA con tercero con este CODIGO");
					respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
					return respuesta;
				}

				logger.info(result);

				if (result.contains("LA CUENTA MANEJA CENTRO, ESTE DATO NO PUEDE ESTAR VACIO")) {
					// logger.info("existe un error de CENTRO inexistente");
					// throw new Exception("No existe CENTRO con este CODIGO");
					logger.info("No existe CENTRO con este CODIGO");
					respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
							"Error al guardar el comprobante, No existe CENTRO con este CODIGO");
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
}
