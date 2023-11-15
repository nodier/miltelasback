package com.softlond.base.service;

import java.math.BigInteger;
import java.sql.Date;
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
import com.softlond.base.entity.ConceptoNotaCredito;
import com.softlond.base.entity.ConceptoNotaCreditoCliente;
import com.softlond.base.entity.DevolucionCompras;
import com.softlond.base.entity.DevolucionVentasCliente;
import com.softlond.base.entity.Factura;
import com.softlond.base.entity.FacturaCompra;
import com.softlond.base.entity.InformacionUsuario;
import com.softlond.base.entity.NotaCredito;
import com.softlond.base.entity.NotaCreditoCliente;
import com.softlond.base.entity.NotaDebitoCliente;
import com.softlond.base.entity.Secundario;
import com.softlond.base.entity.Sequence;
import com.softlond.base.entity.Tercero;
import com.softlond.base.entity.Usuario;
import com.softlond.base.repository.ConceptoNotaCreditoClienteDao;
import com.softlond.base.repository.ConceptoNotaCreditoDao;
import com.softlond.base.repository.DevolucionComprasDao;
import com.softlond.base.repository.DevolucionVentasClienteDao;
import com.softlond.base.repository.EstadoDocumentoDao;
import com.softlond.base.repository.FacturaCompraDao;
import com.softlond.base.repository.FacturaDao;
import com.softlond.base.repository.NotaCreditoClienteDao;
import com.softlond.base.repository.NotaCreditoDao;
import com.softlond.base.repository.NotaDebitoClienteDao;
import com.softlond.base.repository.SequenceDao;
import com.softlond.base.repository.UsuarioInformacionDao;
import java.util.ArrayList;
import java.util.Calendar;

import com.google.gson.Gson;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import com.softlond.base.entity.Clientes;

@Service
public class NotaCreditoClienteService {

	private static final Logger logger = Logger.getLogger(NotaCreditoClienteService.class);

	@Autowired
	public NotaCreditoClienteDao notaCreditoDao;

	@Autowired
	public NotaDebitoClienteDao notaDebitoClienteDao;

	@Autowired
	UsuarioInformacionDao usuarioInformacionDao;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private SequenceDao sequenceDao;

	@Autowired
	private ConceptoNotaCreditoClienteDao conceptoNotaCredito;

	@Autowired
	private FacturaDao facturaDao;

	@Autowired
	private EstadoDocumentoDao estadoDocumentoDao;

	@Autowired
	private DevolucionVentasClienteDao devolucionDao;

	// Crear
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> crearNotaCredito(@RequestBody NotaCreditoCliente nuevoNotaCredito) throws Exception {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		Integer idSede = usuarioInformacion.getIdOrganizacion().getId();
		Sequence secuenciaSave = new Sequence();
		secuenciaSave.setValorSequencia(Integer.parseInt(nuevoNotaCredito.getNumeroDocumento()));
		secuenciaSave.setIdPrefijo(nuevoNotaCredito.getPrefijo().getId());
		secuenciaSave.setIdSede(nuevoNotaCredito.getIdSede().getId());
		try {
			Sequence seqActualizada = sequenceDao.findByIdSedeAndIdPrefijo(nuevoNotaCredito.getIdSede().getId(),
					nuevoNotaCredito.getPrefijo().getId()).orElse(null);
			if (seqActualizada != null) {
				secuenciaSave.setId(seqActualizada.getId());
			}
			nuevoNotaCredito.setIdCreador(usuarioAutenticado);

			NotaCreditoCliente guardado = this.notaCreditoDao.save(nuevoNotaCredito);

			sequenceDao.save(secuenciaSave);
			logger.info(guardado.getId());
			ArrayList<ConceptoNotaCreditoCliente> conceptos = this.conceptoNotaCredito
					.obtenerConceptoIdNotaCredito(guardado.getId());
			for (ConceptoNotaCreditoCliente concepto : conceptos) {

				// ! se condiciona la ejecucion del consumo mekano para poder hacer pruebas
				// if (guardado != null && (idSede == 7 || idSede == 15) && 1 != 1) {
				if (guardado != null && (idSede == 7 || idSede == 15)) {
					// String uri = "http://190.145.10.42:8080/api/v1/TApoloRestInterface/execute";
					String uri = "http://190.145.10.42:8080/api/v1/TApoloRestInterface/execute";
					logger.info(uri);

					logger.info(guardado.getFechaDocumento().toString());
					logger.info(guardado.getFechaDocumento().toString());
					Date fechaEntradaVenta = guardado.getFechaDocumento();
					Calendar calVent = Calendar.getInstance();
					calVent.setTime(fechaEntradaVenta);

					int yearVent = calVent.get(Calendar.YEAR);
					int monthVent = calVent.get(Calendar.MONTH) + 1;
					int dayVent = calVent.get(Calendar.DAY_OF_MONTH);

					Date fechaEntradaVence = guardado.getFechaDocumento();
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
					reciboCajaMekano.setTIPO("NCE");
					String prefijo = "";
					switch (guardado.getIdSede().getId()) {
						case 1:
							prefijo = "NCE4";
							break;
						case 6:
							prefijo = "NCE2";
							break;
						case 7:
							prefijo = "NCE3";
							break;
						case 11:
							prefijo = "NCE1";
							break;
						case 12:
							prefijo = "NCE6";
							break;
						case 13:
							prefijo = "NCE10";
							break;
						case 14:
							prefijo = "NCE9";
							break;
						case 15:
							prefijo = "NCE5";
							break;

						default:
							break;
					}
					reciboCajaMekano.setPREFIJO(prefijo);
					reciboCajaMekano.setNUMERO(guardado.getNumeroDocumento().toString());
					reciboCajaMekano.setFECHA(fechaVenta);
					reciboCajaMekano.setVENCE(fechaVence);
					// !revisar si no falla con observaciones osino enviar por dedecto "-"
					// reciboCajaMekano.setNOTA(guardado.getObservaciones().toString());
					reciboCajaMekano.setNOTA(guardado.getObservaciones());
					// reciboCajaMekano.setNOTA("-");
					reciboCajaMekano.setTERCERO(guardado.getIdCliente().getNitocc().toString());
					// reciboCajaMekano.setTERCERO("1234567899");
					reciboCajaMekano.setVENDEDOR("24347052");
					reciboCajaMekano.setBANCO("CG");
					reciboCajaMekano.setUSUARIO("SUPERVISOR");
					// ! el prefijo debe cambiar cuando se pase a produccion
					// !se usa prefijo por defecto "NDE1"

					reciboCajaMekano.setTIPO_REF("FE1");
					// reciboCajaMekano.setPREFIJO_REF(factura.getPrefijo().getPrefijo());//
					// !valores facturas asociadas
					// reciboCajaMekano.setNUMERO_REF(factura.getNroFactura().toString());//
					// !valores facturas asociadas
					reciboCajaMekano.setPREFIJO_REF(concepto.getCodigoDocumento());
					// reciboCajaMekano.setPREFIJO_REF("MT5");
					reciboCajaMekano.setNUMERO_REF(concepto.getNumeroDocumento());
					// reciboCajaMekano.setNUMERO_REF("174");
					double Abono = concepto.getValor();
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
						if (result.contains("EL TERCERO")) {
							if (result.contains("NO EXISTE")) {
								logger.info("existe un error de tercero inexistente");
								// this.crearClienteMekano(guardado.getIdCliente());
								// this.crearFacturaMekanoN(guardado, concepto);
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
			logger.info("peticion nota credito cliente exitosa");
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito creando nota crédito cliente");
			respuestaDto.setObjetoRespuesta(guardado);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);

		} catch (Exception e) {
			logger.error("Error en la creación de la nota crédito cliente" + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error en la creación de la nota crédito cliente");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return respuesta;
	}

	private void crearFacturaMekanoN(NotaCreditoCliente guardado, ConceptoNotaCreditoCliente concepto)
			throws InterruptedException {
		// ArrayList<ConceptoNotaCreditoCliente> conceptos = this.conceptoNotaCredito
		// .obtenerConceptoIdNotaCredito(guardado.getId());
		// for (ConceptoNotaCreditoCliente concepto : conceptos) {
		if (guardado != null) {
			String uri = "http://190.145.10.42:8080/api/v1/TApoloRestInterface/execute";
			logger.info(uri);

			logger.info(guardado.getFechaDocumento().toString());
			logger.info(guardado.getFechaDocumento().toString());
			Date fechaEntradaVenta = guardado.getFechaDocumento();
			Calendar calVent = Calendar.getInstance();
			calVent.setTime(fechaEntradaVenta);

			int yearVent = calVent.get(Calendar.YEAR);
			int monthVent = calVent.get(Calendar.MONTH) + 1;
			int dayVent = calVent.get(Calendar.DAY_OF_MONTH);

			Date fechaEntradaVence = guardado.getFechaDocumento();
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
			reciboCajaMekano.setTIPO("NCE");
			String prefijo = "";
			switch (guardado.getIdSede().getId()) {
				case 1:
					prefijo = "NCE4";
					break;
				case 6:
					prefijo = "NCE2";
					break;
				case 7:
					prefijo = "NCE3";
					break;
				case 11:
					prefijo = "NCE1";
					break;
				case 12:
					prefijo = "NCE6";
					break;
				case 13:
					prefijo = "NCE10";
					break;
				case 14:
					prefijo = "NCE9";
					break;
				case 15:
					prefijo = "NCE5";
					break;

				default:
					break;
			}
			reciboCajaMekano.setPREFIJO(prefijo);
			reciboCajaMekano.setNUMERO(guardado.getNumeroDocumento().toString());
			reciboCajaMekano.setFECHA(fechaVenta);
			reciboCajaMekano.setVENCE(fechaVence);
			// !revisar si no falla con observaciones osino enviar por dedecto "-"
			// reciboCajaMekano.setNOTA(guardado.getObservaciones().toString());
			reciboCajaMekano.setNOTA(guardado.getObservaciones());
			// reciboCajaMekano.setNOTA("-");
			reciboCajaMekano.setTERCERO(guardado.getIdCliente().getNitocc().toString());// !saldo tercero
			// reciboCajaMekano.setTERCERO("1234567899");
			reciboCajaMekano.setVENDEDOR("24347052");
			reciboCajaMekano.setBANCO("CG");
			reciboCajaMekano.setUSUARIO("SUPERVISOR");

			reciboCajaMekano.setTIPO_REF("FE1");
			// reciboCajaMekano.setPREFIJO_REF(factura.getPrefijo().getPrefijo());//
			// !valores facturas asociadas
			// reciboCajaMekano.setNUMERO_REF(factura.getNroFactura().toString());//
			// !valores facturas asociadas
			reciboCajaMekano.setPREFIJO_REF(concepto.getCodigoDocumento());
			// reciboCajaMekano.setPREFIJO_REF("MT5");
			reciboCajaMekano.setNUMERO_REF(concepto.getNumeroDocumento());
			double Abono = concepto.getValor();
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
		// }
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
			codigoProveedor = clientes.getNitocc() != null ? clientes.getNitocc() : "";
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

	// Obtener numero documento por sede
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerNumero(Integer idSede) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		try {
			List<NotaCreditoCliente> notaDatos = this.notaCreditoDao.findByIdSede(idSede);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de nota credito exitosa");
			respuestaDto.setObjetoRespuesta(notaDatos);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo nota credito " + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error obteniendo nota credito");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return respuesta;

	}

	// Listar Notas credito segun filtros
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerNotaCreditoClienteConsulta(String fechaInicial, String fechaFinal,
			String numeroDocumento, String estadoDocumento, Integer page) {
		ResponseEntity<Object> respuesta;
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		Pageable paging = PageRequest.of(page, 10);
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		try {
			List<NotaCreditoCliente> notaDebitoCliente;
			StringBuilder query = new StringBuilder();
			StringBuilder queryCantidad = new StringBuilder();
			generarQuery(query, usuarioInformacion.getIdOrganizacion().getId(), fechaInicial, fechaFinal, numeroDocumento,
					estadoDocumento);
			TypedQuery<NotaCreditoCliente> notaDebitoClienteInfoQuery = (TypedQuery<NotaCreditoCliente>) entityManager
					.createNativeQuery(query.toString(), NotaCreditoCliente.class);
			int pageNumber = paging.getPageNumber();
			int pageSize = paging.getPageSize();
			notaDebitoClienteInfoQuery.setFirstResult(pageNumber * pageSize);
			notaDebitoClienteInfoQuery.setMaxResults(pageSize);
			notaDebitoCliente = notaDebitoClienteInfoQuery.getResultList();
			generarQueryCantidadDatos(queryCantidad, usuarioInformacion.getIdOrganizacion().getId(), fechaInicial, fechaFinal,
					numeroDocumento, estadoDocumento);
			Query cantidad = entityManager.createNativeQuery(queryCantidad.toString());
			BigInteger countResult = (BigInteger) cantidad.getSingleResult();
			Integer cantidadTotal = countResult.intValue();
			Page<NotaCreditoCliente> result = new PageImpl<NotaCreditoCliente>(notaDebitoCliente, paging, cantidadTotal);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de notas credito cliente exitosa");
			respuestaDto.setObjetoRespuesta(result);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error(
					"Error obteniendo notas credito cliente " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error obteniendo notas credito cliente");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	private void generarQuery(StringBuilder query, Integer idSede, String fechaInicial, String fechaFinal,
			String numeroDocumento, String estadoDocumento) {

		query.append("select * FROM nota_credito_cliente where id_sede= ");

		query.append("" + idSede);
		if (!fechaInicial.equals("null") && fechaFinal.equals("null")) {
			query.append(" and date_format(date(fecha_documento),'%Y-%m-%d')" + "= date('" + fechaInicial + "')");
		} else if (fechaInicial.equals("null") && !fechaFinal.equals("null")) {
			query.append(" and date_format(date(fecha_documento),'%Y-%m-%d')" + "= date('" + fechaFinal + "')");
		} else if (!fechaInicial.equals("null") && !fechaFinal.equals("null")) {
			query.append(" and date_format(date(fecha_documento),'%Y-%m-%d')" + " between " + "date('" + fechaInicial
					+ "') and " + "date('" + fechaFinal + "')");
		}
		if (!numeroDocumento.equals("null") && (!fechaInicial.equals("null") && !fechaFinal.equals("null"))) {
			query.append(" and numero_documento= '" + numeroDocumento + "'");
		} else if (!numeroDocumento.equals("null")) {
			query.append(" and numero_documento= '" + numeroDocumento + "'");
		}

		if (!estadoDocumento.equals("null")
				&& (!fechaInicial.equals("null") && !fechaFinal.equals("null") || !numeroDocumento.equals("null"))) {
			query.append(" and estado_documento= '" + estadoDocumento + "'");
		} else if (!estadoDocumento.equals("null")) {
			query.append(" and estado_documento= '" + estadoDocumento +
					"'");
		}
		query.append(" order by fecha_documento desc");
	}

	private void generarQueryCantidadDatos(StringBuilder query, Integer idSede, String fechaInicial, String fechaFinal,
			String numeroDocumento, String estadoDocumento) {
		query.append("select count(*) FROM nota_credito_cliente where id_sede= ");
		query.append("" + idSede);
		if (!fechaInicial.equals("null") && fechaFinal.equals("null")) {
			query.append(" and date_format(date(fecha_documento),'%Y-%m-%d')" + "= date('" + fechaInicial + "')");
		} else if (fechaInicial.equals("null") && !fechaFinal.equals("null")) {
			query.append(" and date_format(date(fecha_documento),'%Y-%m-%d')" + "= date('" + fechaFinal + "')");
		} else if (!fechaInicial.equals("null") && !fechaFinal.equals("null")) {
			query.append(" and date_format(date(fecha_documento),'%Y-%m-%d')" + " between " + "date('" + fechaInicial
					+ "') and " + "date('" + fechaFinal + "')");
		}
		if (!numeroDocumento.equals("null") && (!fechaInicial.equals("null") && !fechaFinal.equals("null"))) {
			query.append(" and numero_documento= '" + numeroDocumento + "'");
		} else if (!numeroDocumento.equals("null")) {
			query.append(" and numero_documento= '" + numeroDocumento + "'");
		}

		if (!estadoDocumento.equals("null")
				&& (!fechaInicial.equals("null") && !fechaFinal.equals("null") || !numeroDocumento.equals("null"))) {
			query.append(" and estado_documento= '" + estadoDocumento + "'");
		} else if (!estadoDocumento.equals("null")) {
			query.append(" and estado_documento= '" + estadoDocumento + "'");
		}
	}

	// Obtener nota credito
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerNC(String numeroDocumento) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		try {
			Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
			Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
			InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
					.buscarPorIdUsuario(usuarioAutenticado.getId());

			int idSede = usuarioInformacion.getIdOrganizacion().getId();
			NotaCreditoCliente notaDatos = this.notaCreditoDao.findByIdSedeNumero(idSede, numeroDocumento);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de nota credito exitosa");
			respuestaDto.setObjetoRespuesta(notaDatos);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo nota credito " + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error obteniendo nota credito");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return respuesta;

	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> actualizarNotaCredito(@RequestBody NotaCreditoCliente notaCredito) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		NotaCreditoCliente notaEnBd = this.notaCreditoDao.findById(notaCredito.getId()).get();
		try {
			if (!notaEnBd.getId().equals(notaCredito.getId())) {

				NotaCreditoCliente actualizada = this.notaCreditoDao.save(notaCredito);

				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito actualizando la nota crédito");
				respuestaDto.setObjetoRespuesta(actualizada);
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);

			} else {
				NotaCreditoCliente actualizada = this.notaCreditoDao.save(notaCredito);
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito actualizando la nota credito");
				respuestaDto.setObjetoRespuesta(actualizada);
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
			}

		} catch (Exception e) {
			logger.error("Error en la actualización la Remisión");
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error en la actualización del prefijo");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return respuesta;
	}

	// Listar remisiones mes
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerNotasCreditoMes(Integer page) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Integer suma = 0;
		try {
			Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
			Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
			InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
					.buscarPorIdUsuario(usuarioAutenticado.getId());
			Pageable pageConfig = PageRequest.of(page, 10);
			int idSede = usuarioInformacion.getIdOrganizacion().getId();
			Page<NotaCreditoCliente> notasCredito = notaCreditoDao.obtenerNotasCreditoDelMes(idSede, pageConfig);
			suma = notaCreditoDao.sumaNotasCreditoDelMes(idSede);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de notas credito exitosa",
					notasCredito.getContent(), suma, notasCredito.getTotalElements() + "");
			respuestaDto.setObjetoRespuesta(notasCredito);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);

		} catch (RuntimeException e) {
			logger.error("Error al obtener las notas credito" + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al obtener las notas credito " + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	public List<NotaCreditoCliente> listarnotacreditopendiente(Integer idSede) {
		List<NotaCreditoCliente> lista = notaCreditoDao.listarnotacreditopendiente(idSede);
		return lista;
	}

	public List<NotaCreditoCliente> listarinformeNotasCreditoVencidasCompraFiltros(Integer idSede, Integer idCliente,
			String fechaInicial, String fechaFinal) {
		List<NotaCreditoCliente> notasCredito;
		StringBuilder query = new StringBuilder();
		generarQueryInformeNotasCreditoVencida(query, idSede, fechaInicial, fechaFinal, idCliente);

		TypedQuery<NotaCreditoCliente> notasCreditoInfoQuery = (TypedQuery<NotaCreditoCliente>) entityManager
				.createNativeQuery(query.toString(), NotaCreditoCliente.class);
		notasCredito = notasCreditoInfoQuery.getResultList();
		return notasCredito;
	}

	private void generarQueryInformeNotasCreditoVencida(StringBuilder query, Integer idSede, String fechaInicio,
			String fechaFin, Integer cliente) {

		query.append("select * FROM nota_credito_cliente where id_sede=");

		query.append("" + idSede);
		if (!fechaInicio.equals("null") && fechaFin.equals("null")) {
			query.append(" and date_format(date(fecha_documento),'%Y-%m-%d')" + "= date('" + fechaInicio + "')");
		} else if (fechaInicio.equals("null") && !fechaFin.equals("null")) {
			query.append(" and date_format(date(fecha_documento),'%Y-%m-%d')" + "<= date('" + fechaFin + "')");
		} else if (!fechaInicio.equals("null") && !fechaFin.equals("null")) {
			query.append(" and date_format(date(fecha_documento),'%Y-%m-%d')" + "between " + "date('" + fechaInicio
					+ "') and " + "date('" + fechaFin + "')");
		}
		if (cliente != 0) {
			query.append(" and id_cliente=" + cliente);
		}
	}
	/*
	 * hay que modificar
	 */

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> eliminarNotaCredito(Integer idNotaCredito) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		try {
			List<ConceptoNotaCreditoCliente> conceptos = conceptoNotaCredito.obtenerConceptoIdNotaCredito(idNotaCredito);
			for (ConceptoNotaCreditoCliente conceptoNotaCredito : conceptos) {
				if (conceptoNotaCredito.getCodigoDocumento().equals("FAC")) {

					Factura factura = this.facturaDao.findByNumeroDocumento(conceptoNotaCredito.getNumeroDocumento());
					factura.setCodEstadoCon(this.estadoDocumentoDao.findById(2).get());
					this.facturaDao.save(factura);
				} else {

					DevolucionVentasCliente devolucion = this.devolucionDao
							.findByNroDevolucion(conceptoNotaCredito.getNumeroDocumento());
					devolucion.setCodEstadoCon(this.estadoDocumentoDao.findById(2).get());
					this.devolucionDao.save(devolucion);
				}
			}
			conceptoNotaCredito.eliminarConceptosNotasCreditos(idNotaCredito);
			notaCreditoDao.eliminarNotaCredito(idNotaCredito);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "eliminacion de notas credito exitosa");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);

		} catch (RuntimeException e) {
			logger.error("Error eliminando las notas credito" + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al eliminar las notas credito " + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	/*
	 * Ojitooooo
	 */

	public List<NotaCreditoCliente> obtenerNotasCreditosPorPagar(Integer idCliente) {
		List<NotaCreditoCliente> notas = notaCreditoDao.obtenerNotasCreditosPorPagar(idCliente);
		return notas;
	}

	public List<NotaCreditoCliente> notasCreditoPorCliente(Integer idCliente, Date fechaInicial, Date fechaFinal) {
		List<NotaCreditoCliente> lista = notaCreditoDao.busarPorClienteYFecha(idCliente, fechaInicial, fechaFinal);
		return lista;
	}

	public List<NotaCreditoCliente> obtenerPendientesPorCliente(Integer idCliente) {
		List<NotaCreditoCliente> notas = notaCreditoDao.obtenerNotasCreditosPorPagar(idCliente);
		return notas;
	}

	public List<NotaCreditoCliente> listarnotacreditocartera(Integer idSede) {
		List<NotaCreditoCliente> lista = notaCreditoDao.listarnotacreditocartera(idSede);
		return lista;
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerNotasCreditoPendientes(Integer idCliente, Integer page) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		try {
			Pageable pageConfig = PageRequest.of(page, 10);
			Page<NotaCreditoCliente> notasCredito = notaCreditoDao.obtenerNotasCreditosPorPagarPaginado(idCliente,
					pageConfig);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de notas credito exitosa");
			respuestaDto.setObjetoRespuesta(notasCredito);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);

		} catch (RuntimeException e) {
			logger.error("Error al obtener las notas credito" + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al obtener las notas credito " + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerNotasCreditoPendientesActualizar(Integer idCliente, Integer idCredito,
			Integer page) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		try {
			Pageable pageConfig = PageRequest.of(page, 10);
			Page<NotaCreditoCliente> notasCredito = notaCreditoDao.obtenerNotasCreditosPorPagarPaginadoActualizar(idCliente,
					idCredito, pageConfig);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de notas credito exitosa");
			respuestaDto.setObjetoRespuesta(notasCredito);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);

		} catch (RuntimeException e) {
			logger.error("Error al obtener las notas credito" + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al obtener las notas credito " + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}
	// moviemiento por cliente

	public List<NotaCreditoCliente> listarNotasCreditoFiltros1(Integer idSede, String fechaInicial, String fechaFinal,
			String numeroDocumento, Integer idCliente) {
		List<NotaCreditoCliente> notasCredito;
		StringBuilder query = new StringBuilder();
		generarQueryNotasCreditoVencida(query, idSede, fechaInicial, fechaFinal, numeroDocumento, idCliente);
		TypedQuery<NotaCreditoCliente> notasCreditoInfoQuery = (TypedQuery<NotaCreditoCliente>) entityManager
				.createNativeQuery(query.toString(), NotaCreditoCliente.class);
		notasCredito = notasCreditoInfoQuery.getResultList();
		return notasCredito;
	}

	private void generarQueryNotasCreditoVencida(StringBuilder query, Integer idSede, String fechaInicial,
			String fechaFinal,
			String numeroDocumento, Integer cliente) {

		query.append("select * FROM nota_credito_cliente where id_sede=");

		query.append("" + idSede);
		if (!fechaInicial.equals("null") && fechaFinal.equals("null")) {
			query.append(" and date_format(date(fecha_documento),'%Y-%m-%d')" + "= date('" + fechaInicial + "')");
		} else if (fechaInicial.equals("null") && !fechaFinal.equals("null")) {
			query.append(" and date_format(date(fecha_documento),'%Y-%m-%d')" + "<= date('" + fechaFinal + "')");
		} else if (!fechaInicial.equals("null") && !fechaFinal.equals("null")) {
			query.append(" and date_format(date(fecha_documento),'%Y-%m-%d')" + "between " + "date_format(date('"
					+ fechaInicial + "'),'%Y-%m-%d')" + " and " + "date_format(date('" + fechaFinal + "'),'%Y-%m-%d')");
		}
		/*
		 * if(!numeroDocumento.equals("null") && (!fechaInicial.equals("null") &&
		 * !fechaFinal.equals("null"))) {
		 * query.append(" and numero_documento='" + numeroDocumento + "'");
		 * }
		 */
		if (!numeroDocumento.equals("null")) {
			query.append(" and numero_documento='" + numeroDocumento + "'");
		}
		/*
		 * if(cliente!=0 && (!fechaInicial.equals("null") && !fechaFinal.equals("null")
		 * || !numeroDocumento.equals("null"))) {
		 * query.append(" and id_cliente=" + cliente);
		 * }
		 */
		if (cliente != 0) {
			query.append(" and id_cliente=" + cliente);
		}

	}

	public List<NotaCreditoCliente> listarNotasCreditoPagoFiltros(Integer idSede, String fechaInicial, String fechaFinal,
			String numeroDocumento, Integer idCliente) {
		List<NotaCreditoCliente> notasCredito;
		StringBuilder query = new StringBuilder();
		generarQueryNotasCreditoPago(query, idSede, fechaInicial, fechaFinal, numeroDocumento, idCliente);
		TypedQuery<NotaCreditoCliente> notasCreditoInfoQuery = (TypedQuery<NotaCreditoCliente>) entityManager
				.createNativeQuery(query.toString(), NotaCreditoCliente.class);
		notasCredito = notasCreditoInfoQuery.getResultList();
		return notasCredito;
	}

	private void generarQueryNotasCreditoPago(StringBuilder query, Integer idSede, String fechaInicial, String fechaFinal,
			String numeroDocumento, Integer cliente) {

		query.append("select * FROM nota_credito_cliente where id_sede=");

		query.append("" + idSede);
		if (!fechaInicial.equals("null") && fechaFinal.equals("null")) {
			query.append(" and date_format(date(fecha_documento),'%Y-%m-%d')" + "= date('" + fechaInicial + "')");
		} else if (fechaInicial.equals("null") && !fechaFinal.equals("null")) {
			query.append(" and date_format(date(fecha_documento),'%Y-%m-%d')" + "<= date('" + fechaFinal + "')");
		} else if (!fechaInicial.equals("null") && !fechaFinal.equals("null")) {
			query.append(" and date_format(date(fecha_documento),'%Y-%m-%d')" + "between " + "date_format(date('"
					+ fechaInicial + "'),'%Y-%m-%d')" + " and " + "date_format(date('" + fechaFinal + "'),'%Y-%m-%d')");
		}
		/*
		 * if(!numeroDocumento.equals("null") && (!fechaInicial.equals("null") &&
		 * !fechaFinal.equals("null"))) {
		 * query.append(" and numero_documento='" + numeroDocumento + "'");
		 * }
		 */
		if (!numeroDocumento.equals("null")) {
			query.append(" and numero_documento='" + numeroDocumento + "'");
		}
		/*
		 * if(cliente!=0 && (!fechaInicial.equals("null") && !fechaFinal.equals("null")
		 * || !numeroDocumento.equals("null"))) {
		 * query.append(" and id_cliente=" + cliente);
		 * }
		 */
		if (cliente != 0) {
			query.append(" and id_cliente=" + cliente);
		}

	}

}
