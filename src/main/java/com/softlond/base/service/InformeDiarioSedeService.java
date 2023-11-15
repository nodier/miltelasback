package com.softlond.base.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
// import java.util.Date;
import java.sql.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.time.ZoneId;
import java.time.ZonedDateTime;
// import java.time.format.DateTimeFormatter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.ContableM;
import com.softlond.base.entity.ContableMD;
import com.softlond.base.entity.InformacionUsuario;
import com.softlond.base.entity.Prefijo;
import com.softlond.base.entity.Proveedor;
import com.softlond.base.entity.Tercero;
import com.softlond.base.entity.Usuario;
import com.softlond.base.repository.ContableDao;
import com.softlond.base.repository.PrefijoDao;
import com.softlond.base.repository.ReciboCajaVentaDao;
import com.softlond.base.repository.UsuarioInformacionDao;
import com.softlond.base.response.recaudoPagosReciboResponse;
import com.google.gson.Gson;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import com.softlond.base.repository.ProveedorDao;
import com.softlond.base.repository.SequenceDao;

@Service
public class InformeDiarioSedeService {

	private static final Logger logger = Logger.getLogger(InformeDiarioSedeService.class);
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	UsuarioInformacionDao usuarioInformacionDao;

	@Autowired
	SequenceDao SequenceDao;

	@Autowired
	private ProveedorDao proveedorDao;

	@Autowired
	PrefijoDao prefijoDao;

	@Autowired
	ReciboCajaVentaDao reciboCajaVentaDao;

	@Autowired
	ContableDao ContableDao;

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerInformeDiario(String fecha, String idSedeRequest) {

		logger.info("ingresa a obtener informe diario");
		ResponseEntity<Object> respuesta;
		Integer idSede;
		if (idSedeRequest.equals("null")) {
			Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
			Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
			InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
					.buscarPorIdUsuario(usuarioAutenticado.getId());
			idSede = usuarioInformacion.getIdOrganizacion().getId();
		} else {
			idSede = Integer.parseInt(idSedeRequest);
		}
		try {
			StringBuilder query = new StringBuilder();
			generarQuery(query, idSede, fecha);
			TypedQuery<Object> reporte = (TypedQuery<Object>) entityManager.createNativeQuery(query.toString());
			List<Object> reportes = reporte.getResultList();
			RespuestaDto respuestaDto;
			if (reportes.size() == 0) {
				respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, "no hay informacion para las busqueda actual");
			} else {
				respuestaDto = new RespuestaDto(HttpStatus.OK, "obtención de informe exitosa");
			}
			respuestaDto.setObjetoRespuesta(reportes);
			for (Object reportec : reportes) {
				logger.info(reportec);
			}
			logger.info(reportes.get(0));

			// if ((idSede == 7 || idSede == 15)) {
			// logger.info("ingresa a la creacion de informe desde la sede: " + idSede);

			// String uri = "http://190.145.10.42:8080/api/v1/TApoloRestInterface/execute";

			// // logger.info(factura.getFechaVenta().toString());
			// // logger.info(factura.getFechaVencimientoCr().toString());
			// // Date fechaEntradaVenta = factura.getFechaVenta();
			// // Date fechaEntradaVence = factura.getFechaVencimientoCr();
			// // String fechaVenta = fechaEntradaVenta.getDay() + "." +
			// // fechaEntradaVenta.getMonth() + "."
			// // + fechaEntradaVenta.getYear();
			// // logger.info(fechaVenta);
			// // String fechaVence = fechaEntradaVence.getDay() + "." +
			// // fechaEntradaVence.getMonth() + "."
			// // + fechaEntradaVence.getYear();
			// // logger.info(fechaVence);

			// ContableM facturaMekano = new ContableM();
			// facturaMekano.setCLAVE("Set_Contable_Primario");
			// facturaMekano.setTIPO("FV2");
			// facturaMekano.setPREFIJO("_");
			// // logger.info(factura.getNroFactura().toString());
			// facturaMekano.setNUMERO("1072");
			// logger.info(fecha);
			// facturaMekano.setFECHA("14.11.2022");
			// // si sum(miva)>0 setcuenta=620502 sino setcuenta=620503
			// facturaMekano.setCUENTA("620502");
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
			// facturaMekano.setEMPLEADO("24347052");
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
			// logger.info("termina la creacion de informe desde la sede: " + idSede);
			// }

			respuesta = ResponseEntity.ok(HttpStatus.OK);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error al obtener informe" + e);
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al obtener informe" + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;

	}

	public ResponseEntity<Object> crearInformeDiarioMekano(@RequestBody ContableM contable, String fechaFactura) {
		if (contable != null) {
			logger.info(contable);
		}
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());

		Integer idSede = usuarioInformacion.getIdOrganizacion().getId();
		logger.info("informe mekano sede => crearFacturaCompraMekano => idSede:" +
				idSede);
		try {
			// String uri = "http://190.145.10.42:8080/api/v1/TApoloRestInterface/execute";

			logger.info(contable.getFECHA());
			SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
			String fech = contable.getFECHA().toString();
			logger.info(fech);
			logger.info(fechaFactura);
			// Date dataFormateada = formato.parse(fech);
			// logger.info(dataFormateada);
			// // logger.info(body.getFechaDocumento().toString());
			// Date fechaEntradaVenta = (Date) contable.getFECHA();
			// Date fechaEntradaVenta = fechaFactura;
			String fechaEntradaVenta = fechaFactura;
			logger.info(fechaEntradaVenta);
			// Calendar calVent = Calendar.getInstance();
			// calVent.setTime(fechaEntradaVenta);

			// int yearVent = calVent.get(Calendar.YEAR);
			// int monthVent = calVent.get(Calendar.MONTH) + 1;
			// int dayVent = calVent.get(Calendar.DAY_OF_MONTH);

			// Date fechaEntradaVence = fechaFactura;
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
			ContableMD informeDiario = new ContableMD();
			// facturaMekano.setCLAVE("Set_Contable_Primario");
			// // logger.info(contable.getTIPO());
			// facturaMekano.setTIPO(contable.getTIPO());
			// facturaMekano.setPREFIJO("_");
			// // logger.info(factura.getNroFactura().toString());
			// facturaMekano.setNUMERO(contable.getNUMERO());
			// facturaMekano.setFECHA(fechaEntradaVenta);
			// // facturaMekano.setFECHA(fechaVenta);
			// facturaMekano.setCUENTA(contable.getCUENTA());
			// facturaMekano.setTERCERO(contable.getTERCERO());
			// facturaMekano.setCENTRO(contable.getCENTRO());
			// facturaMekano.setACTIVO("NA");
			// facturaMekano.setEMPLEADO("24347052");
			// facturaMekano.setDEBITO(contable.getDEBITO());
			// facturaMekano.setCREDITO(contable.getCREDITO());
			// facturaMekano.setBASE("0");
			// facturaMekano.setNOTA("-");
			// facturaMekano.setUSUARIO("SUPERVISOR");

			informeDiario.setCLAVE("Set_Contable_Primario");
			informeDiario.setTIPO(contable.getTIPO());
			informeDiario.setPREFIJO("_");
			// informeDiario.setNUMERO(contable.getNUMERO());
			Prefijo prefijo = prefijoDao.obtenerPrefijoInformeDiario(idSede);
			Integer sequencia = 0;
			logger.info(contable.getNUMERO());
			if (contable.getNUMERO().equals("_")) {
				// sequencia = SequenceDao.sequenciaNuevaInforme(contable.getTIPO(), idSede,
				// prefijo.getId());
				logger.info("contable.getNUMERO() == _");
				sequencia = SequenceDao.sequenciaNuevaInforme(contable.getTIPO(), idSede, prefijo.getId());
			} else {
				logger.info("contable.getNUMERO() == no_");
				sequencia = SequenceDao.sequenciaNuevaInforme(contable.getTIPO(), idSede, prefijo.getId());
				SequenceDao.sequenciaNueva(contable.getTIPO(), idSede, prefijo.getId());
			}
			logger.info(sequencia);
			// if (contable.getNUMERO() == "_") {
			// sequencia = sequencia - 1;
			// informeDiario.setNUMERO(sequencia.toString());
			// } else {
			// }
			informeDiario.setNUMERO(sequencia.toString());
			logger.info(informeDiario.getNUMERO());
			// DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date = Date.valueOf(fechaFactura);
			logger.info(date);
			informeDiario.setFECHA(date);
			informeDiario.setCUENTA(contable.getCUENTA());
			informeDiario.setTERCERO("900142797");
			informeDiario.setCENTRO(contable.getCENTRO());
			informeDiario.setACTIVO("NA");
			informeDiario.setEMPLEADO("NA");
			informeDiario.setDEBITO(contable.getDEBITO());
			informeDiario.setCREDITO(contable.getCREDITO());
			informeDiario.setBASE(contable.getBASE());
			informeDiario.setNOTA(contable.getNOTA());
			// informeDiario.setNOTA("-");
			informeDiario.setUSUARIO("SUPERVISOR");

			logger.info(sequencia);
			// informeDiario.setNUMERO(sequencia.toString());
			logger.info(informeDiario.getNUMERO());
			logger.info(ContableDao.existeInforme(informeDiario.getFECHA()));
			logger.info(informeDiario.getFECHA());
			// ! reestructurar existe informe para adicionar numero, tipo y prefijo
			// if (informeDiario != null &&
			// (ContableDao.existeInforme(informeDiario.getFECHA()) <= 0)) {
			ContableDao.save(informeDiario);
			// }
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Factura de	compra creada exitosamente");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error al guardar la factura linea " +
					e.getStackTrace()[0].getLineNumber() + " " + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al guardar la factura" + e);
			respuesta = new ResponseEntity<>(respuestaDto,
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	// public ResponseEntity<Object> enviarInformeContadoMekano() {
	// logger.info("FacturaCompraService =>
	// ResponseEntity<Object>crearFacturaMekanoN => crearFacturaMekanoN:");
	// ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
	// Authentication autenticacion =
	// SecurityContextHolder.getContext().getAuthentication();
	// Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
	// InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
	// .buscarPorIdUsuario(usuarioAutenticado.getId());

	// Integer idSede = usuarioInformacion.getIdOrganizacion().getId();
	// logger.info("FacturaCompraService => crearFacturaCompraMekano => idSede:" +
	// idSede);

	// ArrayList<ContableMD> informesNoEnviados =
	// ContableDao.BuscarContablePorNoEnviado();
	// logger.info(informesNoEnviados.size());
	// for (ContableMD contableMD : informesNoEnviados) {
	// String uri = "http://190.145.10.42:8080/api/v1/TApoloRestInterface/execute";

	// logger.info(contableMD.getFECHA());
	// SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
	// String fech = contableMD.getFECHA().toString();
	// logger.info(fech);

	// ContableM facturaMekano = new ContableM();
	// ContableMD informeDiario = new ContableMD();
	// facturaMekano.setCLAVE("Set_Contable_Primario");
	// facturaMekano.setTIPO(contableMD.getTIPO());
	// facturaMekano.setPREFIJO("_");
	// facturaMekano.setNUMERO(contableMD.getNUMERO());
	// facturaMekano.setFECHA(contableMD.getFECHA());
	// facturaMekano.setCUENTA(contableMD.getCUENTA());
	// facturaMekano.setTERCERO(contableMD.getTERCERO());
	// facturaMekano.setCENTRO(contableMD.getCENTRO());
	// facturaMekano.setACTIVO("NA");
	// facturaMekano.setEMPLEADO("NA");
	// facturaMekano.setDEBITO(contableMD.getDEBITO());
	// facturaMekano.setCREDITO(contableMD.getCREDITO());
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
	// Proveedor proveedor = proveedorDao.findByNit(facturaMekano.getTERCERO());
	// this.crearProveedorMekano(proveedor, idSede);
	// this.crearFacturaMekanoN(facturaMekano, facturaMekano.getFECHA());
	// }
	// }
	// logger.info(result);
	// ContableDao.save(informeDiario);
	// }

	// }

	// RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Factura de
	// compra creada exitosamente");
	// respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
	// return respuesta;
	// }

	public ResponseEntity<Object> crearFacturaMekanoN(@RequestBody ContableM contable, String fechaFactura)
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
		// Date fechaEntradaVenta = fechaFactura;
		// Calendar calVent = Calendar.getInstance();
		// calVent.setTime(fechaEntradaVenta);

		// int yearVent = calVent.get(Calendar.YEAR);
		// int monthVent = calVent.get(Calendar.MONTH) + 1;
		// int dayVent = calVent.get(Calendar.DAY_OF_MONTH);

		// Date fechaEntradaVence = fechaFactura;
		// Calendar calVenc = Calendar.getInstance();
		// calVenc.setTime(fechaEntradaVence);

		// int yearVenc = calVenc.get(Calendar.YEAR);
		// int monthVenc = calVenc.get(Calendar.MONTH) + 1;
		// int dayVenc = calVenc.get(Calendar.DAY_OF_MONTH);

		// String fechaVenta = dayVent + "." + monthVent + "." + yearVent;
		// logger.info(fechaVenta);
		// String fechaVence = dayVenc + "." + monthVenc + "." + yearVenc;
		// logger.info(fechaVence);

		ContableM facturaMekano = new ContableM();
		ContableMD informeDiario = new ContableMD();
		facturaMekano.setCLAVE("Set_Contable_Primario");
		// logger.info(contable.getTIPO());
		facturaMekano.setTIPO(contable.getTIPO());
		facturaMekano.setPREFIJO("_");
		// logger.info(factura.getNroFactura().toString());
		facturaMekano.setNUMERO(contable.getNUMERO());
		facturaMekano.setFECHA(fechaFactura);
		// facturaMekano.setFECHA(fechaVenta);
		facturaMekano.setCUENTA(contable.getCUENTA());
		facturaMekano.setTERCERO(contable.getTERCERO());
		facturaMekano.setCENTRO(contable.getCENTRO());
		facturaMekano.setACTIVO("NA");
		facturaMekano.setEMPLEADO("NA");
		facturaMekano.setDEBITO(contable.getDEBITO());
		facturaMekano.setCREDITO(contable.getCREDITO());
		logger.info("InformeDiarioSedeService => ResponseEntity<Object>crearFacturaMekanoN => contable.getBASE():"
				+ contable.getBASE());
		facturaMekano.setBASE(contable.getBASE());
		facturaMekano.setNOTA(contable.getNOTA());
		facturaMekano.setUSUARIO("SUPERVISOR");

		// informeDiario.setCLAVE("Set_Contable_Primario");
		// informeDiario.setTIPO(contable.getTIPO());
		// informeDiario.setPREFIJO("_");
		// informeDiario.setNUMERO(contable.getNUMERO());
		// informeDiario.setFECHA(fechaFactura);
		// informeDiario.setCUENTA(contable.getCUENTA());
		// informeDiario.setTERCERO(contable.getTERCERO());
		// informeDiario.setCENTRO(contable.getCENTRO());
		// informeDiario.setACTIVO("NA");
		// informeDiario.setEMPLEADO("NA");
		// informeDiario.setDEBITO(contable.getDEBITO());
		// informeDiario.setCREDITO(contable.getCREDITO());
		// informeDiario.setBASE("0");
		// informeDiario.setNOTA("-");
		// informeDiario.setUSUARIO("SUPERVISOR");

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
		ContableDao.save(informeDiario);
		return respuesta;
	}

	private void crearProveedorMekano(Proveedor proveedor, Integer idSede) {
		if (proveedor != null && (idSede == 7 || idSede == 15))

		{
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

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerInformeDiarioIva(String fecha, String idSedeRequest) {
		ResponseEntity<Object> respuesta;
		Integer idSede;
		if (idSedeRequest.equals("null")) {
			Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
			Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
			InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
					.buscarPorIdUsuario(usuarioAutenticado.getId());
			idSede = usuarioInformacion.getIdOrganizacion().getId();
		} else {
			idSede = Integer.parseInt(idSedeRequest);
		}
		try {
			StringBuilder query = new StringBuilder();
			generarQueryIva(query, idSede, fecha);

			TypedQuery<Object> reporte = (TypedQuery<Object>) entityManager.createNativeQuery(query.toString());
			List<Object> reportes = reporte.getResultList();
			RespuestaDto respuestaDto;
			if (reportes.size() == 0) {
				respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, "no hay informacion de iva para las busqueda actual");
			} else {
				respuestaDto = new RespuestaDto(HttpStatus.OK, "obtención de informe de iva exitosa");
			}
			respuestaDto.setObjetoRespuesta(reportes);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error al obtener informe" + e);
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al obtener informe" + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;

	}

	private void generarQuery(StringBuilder query, Integer idSede, String fecha) {
		query.append(
				"select m.nombre, nid_pyme_tipo_venta,count(*),sum(m_subtotal_ventas),sum(miva),sum(m_total),sum(m_ajustes), concat(p.prefijo,min(n_nro_factura)),concat(p.prefijo,max(n_nro_factura)),sum(f.m_total),m.id\r\n"
						+ "FROM fac_facturas f join maestro_valor m on f.nid_pyme_tipo_venta = m.id join prefijo p on f.t_prefijo= p.id\r\n"
						+ "where f.id_sede=");
		query.append("" + idSede + " and date_format(date(d_fecha_venta),'%Y-%m-%d') = date('");
		query.append("" + fecha + "') group by nid_pyme_tipo_venta");
	}

	private void generarQueryIva(StringBuilder query, Integer idSede, String fecha) {
		query.append(
				"select (sum(m_sub_total)) as total, nid_pyme_tipo_venta as tipo,fa.n_porcentajeiva as iva,\r\n"
						+ "Sum(((fa.n_cantidad * fa.m_precio_unitario)*(fa.n_porcentaje_descuento))) as descuento,\r\n"
						+ "Sum(((fa.n_cantidad * fa.m_precio_unitario)-(((fa.n_cantidad * fa.m_precio_unitario)*(fa.n_porcentaje_descuento))))-(((fa.n_cantidad * fa.m_precio_unitario)-(((fa.n_cantidad * fa.m_precio_unitario)*(fa.n_porcentaje_descuento))))/(1+(fa.n_porcentajeiva/100)))) as totalIva "
						+ "FROM fac_facturas f join maestro_valor m on f.nid_pyme_tipo_venta = m.id\r\n"
						+ "join fac_articulos fa on fa.nid_factura = f.nid_factura\r\n" + "where f.id_sede=");
		query.append("" + idSede + " and date_format(date(d_fecha_venta),'%Y-%m-%d') = date('");
		query.append("" + fecha + "') group by nid_pyme_tipo_venta,fa.n_porcentajeiva");
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerRecaudos(String fecha, String idSedeRequest) {
		ResponseEntity<Object> respuesta;
		Integer idSede;
		if (idSedeRequest.equals("null")) {
			Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
			Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
			InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
					.buscarPorIdUsuario(usuarioAutenticado.getId());
			idSede = usuarioInformacion.getIdOrganizacion().getId();
		} else {
			idSede = Integer.parseInt(idSedeRequest);
		}
		try {
			StringBuilder query = new StringBuilder();
			generarQueryRecaudo(query, idSede, fecha);
			Query resultado = (Query) entityManager.createNativeQuery(query.toString());
			List<recaudoPagosReciboResponse> reportes = resultado
					.setResultTransformer(Transformers.aliasToBean(recaudoPagosReciboResponse.class)).getResultList();
			RespuestaDto respuestaDto;
			respuestaDto = new RespuestaDto(HttpStatus.OK, "obtención de informe de iva exitosa");
			respuestaDto.setObjetoRespuesta(reportes);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error al obtener informe" + e + " linea :" + e.getStackTrace()[0].getLineNumber());
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al obtener informe" + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;

	}

	private void generarQueryRecaudo(StringBuilder query, Integer idSede, String fecha) {
		query.append("select r.prefijo,ifnull(sum(r.total_recibo),0) as valorRecaudo from rcb_rbocajaventa r left join \r\n"
				+ "fac_facturas f on r.id=f.id_recibo_caja where r.id_sede =");
		query.append("" + idSede + " and date_format(date(r.fecha_pago),'%Y-%m-%d') = date('");
		query.append("" + fecha + "') and f.nid_factura is null group by r.prefijo");
	}

	/*
	 * EXPLICACION
	 * segundos(0-59) - minutos(0-59) - horas(0-23) - diaMes(1-31) - mes(1-12) -
	 * diaSemana(0-6)
	 * 
	 * - ? -> no definido
	 * - * -> todos
	 * - / -> incremento: Ejm: en min poner 0/15 significa que se ejecuta cada 15
	 * min
	 * - , -> conjunto: Ejm: en dia semana poner 6,7 significa que se ejecuta
	 * sabados y domingos
	 */

	// Se ejecuta a las 12:30 AM de cada dia
	// @Scheduled(cron = "0 30 00 * * *", zone = "America/Bogota")
	// @Scheduled(cron = "0 20 09 * * *", zone = "America/Bogota")
	@Scheduled(cron = "0 54 12 * * *", zone = "America/Bogota")
	public void crearInformeDiario() throws Exception {
		logger.info("Realizando llamado a procedimiento almacenado para crear los informes diarios por sede");
		this.funcionCrearInforme();
	}

	public void funcionCrearInforme() throws Exception {
		Integer idSede;
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		if (autenticacion != null && autenticacion.getPrincipal() instanceof Usuario) {
			logger.info("ingresa a usuario autenticado con valor");
			Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		} else {
			logger.info("usuario autenticado nulo");
		}
		// InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
		// .buscarPorIdUsuario(usuarioAutenticado.getId());
		// idSede = usuarioInformacion.getIdOrganizacion().getId();
		// logger.info("ingresa a callprocedures muestro el valor: ");
		// String x = "";
		// // try {
		logger.info("ingresa a la creacion de informe desde la sede: ");

		String uri = "http://190.145.10.42:8080/api/v1/TApoloRestInterface/execute";

		// logger.info(factura.getFechaVenta().toString());
		// logger.info(factura.getFechaVencimientoCr().toString());
		// Date fechaEntradaVenta = factura.getFechaVenta();
		// Date fechaEntradaVence = factura.getFechaVencimientoCr();
		// String fechaVenta = fechaEntradaVenta.getDay() + "." +
		// fechaEntradaVenta.getMonth() + "."
		// + fechaEntradaVenta.getYear();
		// logger.info(fechaVenta);
		// String fechaVence = fechaEntradaVence.getDay() + "." +
		// fechaEntradaVence.getMonth() + "."
		// + fechaEntradaVence.getYear();
		// logger.info(fechaVence);
		// ArrayList<ContableMD> contables =
		// ContableDao.BuscarContablePorNumero("1072");
		ArrayList<ContableMD> contables = ContableDao.BuscarContablePorNoEnviado();
		logger.info(contables.size());
		for (ContableMD contable : contables) {
			ContableM facturaMekano = new ContableM();
			// ContableMD informeDiario = new ContableMD();
			facturaMekano.setCLAVE("Set_Contable_Primario");
			facturaMekano.setTIPO(contable.getTIPO());
			facturaMekano.setPREFIJO("_");
			// logger.info(factura.getNroFactura().toString());
			facturaMekano.setNUMERO(contable.getNUMERO());
			LocalDate fechaActual = LocalDate.now();
			DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			// String fechaFormateada = fechaActual.format(formato);
			String fechaFormateada = contable.getFECHA().toString().replace("-", ".");
			logger.info(fechaFormateada);

			facturaMekano.setFECHA(fechaFormateada);
			facturaMekano.setCUENTA(contable.getCUENTA());
			facturaMekano.setTERCERO(contable.getTERCERO());
			facturaMekano.setCENTRO(contable.getCENTRO());
			switch (contable.getCENTRO()) {
				case "04":
					idSede = 1;
					break;
				case "02":
					idSede = 6;
					break;
				case "03":
					idSede = 7;
					break;
				case "01":
					idSede = 11;
					break;
				case "06":
					idSede = 12;
					break;
				case "10":
					idSede = 13;
					break;
				case "09":
					idSede = 14;
					break;
				case "05":
					idSede = 15;
					break;
				default:
					idSede = 15;
					break;
			}
			facturaMekano.setACTIVO("NA");
			facturaMekano.setEMPLEADO(contable.getEMPLEADO());
			facturaMekano.setDEBITO(contable.getDEBITO());
			facturaMekano.setCREDITO(contable.getCREDITO());
			logger.info("InformeDiarioSedeService => voidfuncionCrearInforme => contable.getBASE():" + contable.getBASE());
			facturaMekano.setBASE(contable.getBASE());
			facturaMekano.setNOTA(contable.getNOTA());
			facturaMekano.setUSUARIO("SUPERVISOR");

			// informeDiario.setCLAVE("Set_Contable_Primario");
			// informeDiario.setTIPO("FV2");
			// informeDiario.setPREFIJO("_");
			// informeDiario.setNUMERO("1072");
			// informeDiario.setFECHA(fechaFormateada);
			// informeDiario.setCUENTA("11050501");
			// informeDiario.setTERCERO("12345678");
			// informeDiario.setCENTRO("04");
			// informeDiario.setACTIVO("NA");
			// informeDiario.setEMPLEADO("NA");
			// informeDiario.setDEBITO("0");
			// informeDiario.setCREDITO("0");
			// informeDiario.setBASE("0");
			// informeDiario.setNOTA("-");
			// informeDiario.setUSUARIO("SUPERVISOR");

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
				logger.info(result);
				if (result.contains("EL TERCERO")) {
					if (result.contains("NO EXISTE")) {
						logger.info("existe un error de tercero inexistente");
						Proveedor proveedor = proveedorDao.findByNit(facturaMekano.getTERCERO());
						// integer
						this.crearProveedorMekano(proveedor, idSede);
						this.crearFacturaMekanoN(facturaMekano, facturaMekano.getFECHA());
					}
				}
				logger.info(result);

				if (result.contains("EL DOCUMENTO")) {
					if (result.contains("NO EXISTE")) {
						logger.info("existe un error de CENTRO inexistente");
						throw new Exception("No existe CENTRO con este CODIGO");
					}
				}
				logger.info(result);

				if (result.contains("LA CUENTA")) {
					if (result.contains("NO EXISTE")) {
						logger.info("existe un error de CENTRO inexistente");
						throw new Exception("No existe CENTRO con este CODIGO");
					}
				}
				logger.info(result);

				if (result.contains("LA CUENTA MANEJA TERCERO, ESTE DATO NO PUEDE ESTAR VACIO")) {
					logger.info("existe un error de CENTRO inexistente");
					throw new Exception("No existe CENTRO con este CODIGO");
				}

				logger.info(result);

				if (result.contains("LA CUENTA MANEJA CENTRO, ESTE DATO NO PUEDE ESTAR VACIO")) {
					logger.info("existe un error de CENTRO inexistente");
					throw new Exception("No existe CENTRO con este CODIGO");
				}
				// x = "ingreso al try";
				// if (result.contains("LA REFERENCIA")) {
				// if (result.contains("NO EXISTE")) {
				// logger.info("existe un error de referencia inexistente");
				//
				// this.crearProductoMekano(factura.getFacArticulos().get(j).getArticulo().getProducto().getId());
				// this.crearFacturaMekanoN(factura, j);
				// }
				// }
				// logger.info(result);
				String requestOk = "{\"cod_error\":0,\"execsql\":0}";
				logger.info(requestOk);
				if (result.contains(requestOk)) {
					contable.setEnviadoMekano(true);
					ContableDao.save(contable);
				}
			}
			logger.info("Termina la ejecución del procedimiento para crear los informes	diarios por sede");
			// ContableDao.save(informeDiario);
			// } catch (Exception e) {
			// logger.error("Error ejecutando procedimiento almacenado para crear los
			// períodos contables: " + e);
			// x = "ingreso al catch";
			// }
			// return x;
		}

		// private void callProcedure() {
		// Integer idSede;
		// Authentication autenticacion =
		// SecurityContextHolder.getContext().getAuthentication();
		// Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		// InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
		// .buscarPorIdUsuario(usuarioAutenticado.getId());
		// idSede = usuarioInformacion.getIdOrganizacion().getId();
		// logger.info("ingresa a callprocedures muestro el valor: ");
		// try {
		// logger.info("ingresa a la creacion de informe desde la sede: " + idSede);

		// String uri = "http://190.145.10.42:8080/api/v1/TApoloRestInterface/execute";

		// // logger.info(factura.getFechaVenta().toString());
		// // logger.info(factura.getFechaVencimientoCr().toString());
		// // Date fechaEntradaVenta = factura.getFechaVenta();
		// // Date fechaEntradaVence = factura.getFechaVencimientoCr();
		// // String fechaVenta = fechaEntradaVenta.getDay() + "." +
		// // fechaEntradaVenta.getMonth() + "."
		// // + fechaEntradaVenta.getYear();
		// // logger.info(fechaVenta);
		// // String fechaVence = fechaEntradaVence.getDay() + "." +
		// // fechaEntradaVence.getMonth() + "."
		// // + fechaEntradaVence.getYear();
		// // logger.info(fechaVence);

		// ContableM facturaMekano = new ContableM();
		// facturaMekano.setCLAVE("Set_Contable_Primario");
		// facturaMekano.setTIPO("FV1");
		// facturaMekano.setPREFIJO("SETT");
		// // logger.info(factura.getNroFactura().toString());
		// facturaMekano.setNUMERO("1072");
		// facturaMekano.setFECHA("14.11.2022");
		// facturaMekano.setCUENTA("11050501");
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
		// logger.info("Termina la ejecución del procedimiento para crear los informes
		// diarios por sede");
		// } catch (Exception e) {
		// logger.error("Error ejecutando procedimiento almacenado para crear los
		// períodos contables: " + e);
		// }
		// }
	}
}
