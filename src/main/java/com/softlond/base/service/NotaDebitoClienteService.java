package com.softlond.base.service;

import java.math.BigInteger;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;
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
import org.springframework.web.bind.annotation.RequestParam;

import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.ConceptoNotaDebito;
import com.softlond.base.entity.FacturaCompra;
import com.softlond.base.entity.FacturaImpuestos;
import com.softlond.base.entity.FacturaRetenciones;
import com.softlond.base.entity.InformacionUsuario;
import com.softlond.base.entity.NotaCredito;
import com.softlond.base.entity.NotaCreditoCliente;
import com.softlond.base.entity.NotaDebito;
import com.softlond.base.entity.NotaDebitoCliente;
import com.softlond.base.entity.RemisionCompra;
import com.softlond.base.entity.Secundario;
import com.softlond.base.entity.Sequence;
import com.softlond.base.entity.Tercero;
import com.softlond.base.entity.Usuario;
import com.softlond.base.repository.ConceptoNotaDebitoDao;
import com.softlond.base.repository.NotaDebitoClienteDao;
import com.softlond.base.repository.NotaDebitoDao;
import com.softlond.base.repository.SequenceDao;
import com.softlond.base.repository.UsuarioInformacionDao;
import com.google.gson.Gson;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import com.softlond.base.entity.Clientes;

@Service
public class NotaDebitoClienteService {

	private static final Logger logger = Logger.getLogger(NotaDebitoClienteService.class);

	@Autowired
	public NotaDebitoClienteDao notaDebitoClienteDao;

	@Autowired
	UsuarioInformacionDao usuarioInformacionDao;

	@Autowired
	private SequenceDao sequenceDao;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	public ConceptoNotaDebitoDao conceptoNdDao;

	// Crear
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> crearNotaDebitoCliente(@RequestBody NotaDebitoCliente nuevoNotaDebito)
			throws Exception {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		Integer idSede = usuarioInformacion.getIdOrganizacion().getId();
		Sequence secuenciaSave = new Sequence();
		secuenciaSave.setValorSequencia(Integer.parseInt(nuevoNotaDebito.getNumeroDocumento()));
		secuenciaSave.setIdPrefijo(nuevoNotaDebito.getPrefijo().getId());
		secuenciaSave.setIdSede(nuevoNotaDebito.getIdSede().getId());
		try {
			Sequence seqActualizada = sequenceDao
					.findByIdSedeAndIdPrefijo(nuevoNotaDebito.getIdSede().getId(), nuevoNotaDebito.getPrefijo().getId())
					.orElse(null);
			if (seqActualizada != null) {
				secuenciaSave.setId(seqActualizada.getId());
			}
			nuevoNotaDebito.setIdCreador(usuarioAutenticado);

			NotaDebitoCliente guardado = this.notaDebitoClienteDao.save(nuevoNotaDebito);
			guardado = this.notaDebitoClienteDao.findByNumeroDocumento(nuevoNotaDebito.getNumeroDocumento());
			sequenceDao.save(secuenciaSave);

			ArrayList<ConceptoNotaDebito> conceptos = this.conceptoNdDao.obtenerConceptoIdNotaDebito(guardado.getId());
			for (ConceptoNotaDebito concepto : conceptos) {

				// ! se condiciona la ejecucion del consumo mekano para poder hacer pruebas
				// if (guardado != null && (idSede == 7 || idSede == 15) && 1 != 1) {
				if (guardado != null && (idSede == 7 || idSede == 15)) {
					String uri = "http://190.145.10.42:8080/api/v1/TApoloRestInterface/execute";
					logger.info(uri);

					logger.info(guardado.getFechaDocumento().toString());
					logger.info(guardado.getFechaDocumento().toString());
					// String fechaEntrada = "2021-05-13 05:00:00";
					Date fechaEntradaVenta = guardado.getFechaDocumento();
					Date fechaEntradaVence = guardado.getFechaDocumento();
					String fechaVenta = fechaEntradaVenta.getDay() + "." + fechaEntradaVenta.getMonth() + "."
							+ fechaEntradaVenta.getYear();
					logger.info(fechaVenta);
					String fechaVence = fechaEntradaVence.getDay() + "." + fechaEntradaVence.getMonth() + "."
							+ fechaEntradaVence.getYear();
					logger.info(fechaVence);

					Secundario reciboCajaMekano = new Secundario();
					reciboCajaMekano.setCLAVE("Set_Gestion_Secundario");
					reciboCajaMekano.setTIPO("NDEC");
					// reciboCajaMekano.setPREFIJO(reciboNuevo.getPrefijo().toString());
					// reciboCajaMekano.setPREFIJO("_");
					// reciboCajaMekano.setPREFIJO(reciboNuevo.getPrefijo().toString());
					reciboCajaMekano.setPREFIJO("NDE1");
					reciboCajaMekano.setNUMERO(guardado.getNumeroDocumento().toString());
					reciboCajaMekano.setFECHA(fechaVenta);
					reciboCajaMekano.setVENCE(fechaVence);
					// !revisar si no falla con observaciones osino enviar por dedecto "-"
					reciboCajaMekano.setNOTA(guardado.getObservaciones());
					// reciboCajaMekano.setNOTA("-");
					//
					reciboCajaMekano.setTERCERO(guardado.getIdCliente().getNitocc().toString());// !saldo tercero
					// reciboCajaMekano.setTERCERO("1234567899");
					reciboCajaMekano.setVENDEDOR("24347052");
					reciboCajaMekano.setBANCO("CG");
					reciboCajaMekano.setUSUARIO("SUPERVISOR");
					// ! el prefijo debe cambiar cuando se pase a produccion
					// !se usa prefijo por defecto "NDE1"
					String prefijo = "";
					switch (guardado.getIdSede().getId()) {
						case 1:
							prefijo = "NDE4";
							break;
						case 6:
							prefijo = "NDE2";
							break;
						case 7:
							prefijo = "NDE3";
							break;
						case 11:
							prefijo = "NDE1";
							break;
						case 12:
							prefijo = "NDE6";
							break;
						case 13:
							prefijo = "NDE10";
							break;
						case 14:
							prefijo = "NDE9";
							break;
						case 15:
							prefijo = "NDE5";
							break;

						default:
							break;
					}
					reciboCajaMekano.setTIPO_REF("FE1");
					// reciboCajaMekano.setPREFIJO_REF(factura.getPrefijo().getPrefijo());//
					// !valores facturas asociadas
					// reciboCajaMekano.setNUMERO_REF(factura.getNroFactura().toString());//
					// !valores facturas asociadas
					reciboCajaMekano.setPREFIJO_REF(concepto.getCodigoDocumento());
					// reciboCajaMekano.setPREFIJO_REF("MT5");
					reciboCajaMekano.setNUMERO_REF(concepto.getNumeroDocumento());
					// reciboCajaMekano.setNUMERO_REF("174");
					// Integer Abono = reciboNuevo.getTotalRecibo() - reciboNuevo.getSaldo();
					// Integer Abono = Integer.parseInt(concepto.getValorAbono().toString());
					double Abono = concepto.getValor();
					logger.info(Abono);
					int Ab = (int) Abono;
					logger.info(Ab);
					// String As = Ab.toString;
					// logger.info(As);
					// reciboCajaMekano.setABONO(100000);
					reciboCajaMekano.setABONO(Ab);

					Gson gson = new Gson();
					String rjson = gson.toJson(reciboCajaMekano);

					logger.info(rjson);
					// HttpHeaders headers = new HttpHeaders();
					// headers.setContentType(MediaType.APPLICATION_JSON_VALUE);
					HttpHeaders headers = new HttpHeaders();
					headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
					// HttpEntity<String> entity = new HttpEntity<String>(rjson);
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
								// this.crearFacturaMekanoN(guardado);
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

			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito creando nota debito");
			respuestaDto.setObjetoRespuesta(guardado);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);

		} catch (

		Exception e) {
			logger.error("Error en la creación de la nota debito" + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error en la creación de la nota debito");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return respuesta;
	}

	private void crearFacturaMekanoN(NotaDebitoCliente guardado) throws InterruptedException {
		ArrayList<ConceptoNotaDebito> conceptos = this.conceptoNdDao.obtenerConceptoIdNotaDebito(guardado.getId());
		for (ConceptoNotaDebito concepto : conceptos) {
			if (guardado != null) {
				String uri = "http://190.145.10.42:8080/api/v1/TApoloRestInterface/execute";
				logger.info(uri);

				logger.info(guardado.getFechaDocumento().toString());
				logger.info(guardado.getFechaDocumento().toString());
				// String fechaEntrada = "2021-05-13 05:00:00";
				Date fechaEntradaVenta = guardado.getFechaDocumento();
				Date fechaEntradaVence = guardado.getFechaDocumento();
				String fechaVenta = fechaEntradaVenta.getDay() + "." + fechaEntradaVenta.getMonth() + "."
						+ fechaEntradaVenta.getYear();
				logger.info(fechaVenta);
				String fechaVence = fechaEntradaVence.getDay() + "." + fechaEntradaVence.getMonth() + "."
						+ fechaEntradaVence.getYear();
				logger.info(fechaVence);

				Secundario reciboCajaMekano = new Secundario();
				reciboCajaMekano.setCLAVE("Set_Gestion_Secundario");
				reciboCajaMekano.setTIPO("NDEC");
				// reciboCajaMekano.setPREFIJO(reciboNuevo.getPrefijo().toString());
				// reciboCajaMekano.setPREFIJO("_");
				// reciboCajaMekano.setPREFIJO(reciboNuevo.getPrefijo().toString());
				reciboCajaMekano.setPREFIJO("NDE1");
				reciboCajaMekano.setNUMERO(guardado.getNumeroDocumento().toString());
				reciboCajaMekano.setFECHA(fechaVenta);
				reciboCajaMekano.setVENCE(fechaVence);
				// !revisar si no falla con observaciones osino enviar por dedecto "-"
				reciboCajaMekano.setNOTA(guardado.getObservaciones());
				// reciboCajaMekano.setNOTA("-");
				//
				reciboCajaMekano.setTERCERO(guardado.getIdCliente().getNitocc().toString());// !saldo tercero
				// reciboCajaMekano.setTERCERO("1234567899");
				reciboCajaMekano.setVENDEDOR("24347052");
				reciboCajaMekano.setBANCO("CG");
				reciboCajaMekano.setUSUARIO("SUPERVISOR");
				// ! el prefijo debe cambiar cuando se pase a produccion
				// !se usa prefijo por defecto "NDE1"
				String prefijo = "";
				switch (guardado.getIdSede().getId()) {
					case 1:
						prefijo = "NDE4";
						break;
					case 6:
						prefijo = "NDE2";
						break;
					case 7:
						prefijo = "NDE3";
						break;
					case 11:
						prefijo = "NDE1";
						break;
					case 12:
						prefijo = "NDE6";
						break;
					case 13:
						prefijo = "NDE10";
						break;
					case 14:
						prefijo = "NDE9";
						break;
					case 15:
						prefijo = "NDE5";
						break;

					default:
						break;
				}
				reciboCajaMekano.setTIPO_REF("FE1");
				// reciboCajaMekano.setPREFIJO_REF(factura.getPrefijo().getPrefijo());//
				// !valores facturas asociadas
				// reciboCajaMekano.setNUMERO_REF(factura.getNroFactura().toString());//
				// !valores facturas asociadas
				reciboCajaMekano.setPREFIJO_REF(concepto.getCodigoDocumento());
				// reciboCajaMekano.setPREFIJO_REF("MT5");
				reciboCajaMekano.setNUMERO_REF(concepto.getNumeroDocumento());
				// reciboCajaMekano.setNUMERO_REF("174");
				// Integer Abono = reciboNuevo.getTotalRecibo() - reciboNuevo.getSaldo();
				// Integer Abono = Integer.parseInt(concepto.getValorAbono().toString());
				double Abono = concepto.getValor();
				logger.info(Abono);
				int Ab = (int) Abono;
				logger.info(Ab);
				// String As = Ab.toString;
				// logger.info(As);
				// reciboCajaMekano.setABONO(100000);
				reciboCajaMekano.setABONO(Ab);

				Gson gson = new Gson();
				String rjson = gson.toJson(reciboCajaMekano);

				logger.info(rjson);
				// HttpHeaders headers = new HttpHeaders();
				// headers.setContentType(MediaType.APPLICATION_JSON_VALUE);
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
				// HttpEntity<String> entity = new HttpEntity<String>(rjson);
				HttpEntity<String> entity = new HttpEntity<String>(rjson, headers);
				RestTemplate rest = new RestTemplate();

				// if (entity != null && 1 != 1) {
				if (entity != null) {
					TimeUnit.SECONDS.sleep(2);
					String result = rest.postForObject(uri, entity, String.class);
					// if (result.contains("EL TERCERO")) {
					// if (result.contains("NO EXISTE")) {
					// logger.info("existe un error de tercero inexistente");
					// this.crearClienteMekano(guardado.getIdCliente());
					// this.crearFacturaMekanoN(guardado, j);
					// }
					// }
					// logger.info(result);
					logger.info(result);
				}
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
		String nombreD = ""; // ! solo aplica para proveedor naturaleza (N)
		String apellido = ""; // ! solo aplica para proveedor naturaleza (N)
		String apellidoD = ""; // ! solo aplica para proveedor naturaleza (N)
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
					break;
				case 6:
					clasificacionLegal = "J";
					break;
				case 7:
					clasificacionLegal = "J";
					codSociedad = "09";
					break;
				case 8:
					clasificacionLegal = "J";
					break;
				case 9:
					clasificacionLegal = "J";
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

	// Obtener por numero
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerNumero(Integer idSede) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		try {
			List<NotaDebitoCliente> notaDatos = this.notaDebitoClienteDao.findByIdSede(idSede);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de nota debito exitosa");
			respuestaDto.setObjetoRespuesta(notaDatos);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo nota debito " + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error obteniendo nota debito");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return respuesta;

	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerNotaDebitoClienteConsulta(String fechaInicial, String fechaFinal,
			String numeroDocumento, String estadoDocumento, Integer page) {
		ResponseEntity<Object> respuesta;
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		Pageable paging = PageRequest.of(page, 10);
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		try {
			List<NotaDebitoCliente> notaDebitoCliente;
			StringBuilder query = new StringBuilder();
			StringBuilder queryCantidad = new StringBuilder();
			generarQuery(query, usuarioInformacion.getIdOrganizacion().getId(), fechaInicial, fechaFinal, numeroDocumento,
					estadoDocumento);
			TypedQuery<NotaDebitoCliente> notaDebitoClienteInfoQuery = (TypedQuery<NotaDebitoCliente>) entityManager
					.createNativeQuery(query.toString(), NotaDebitoCliente.class);
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
			Page<NotaDebitoCliente> result = new PageImpl<NotaDebitoCliente>(notaDebitoCliente, paging, cantidadTotal);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de notas debito cliente exitosa");
			respuestaDto.setObjetoRespuesta(result);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error(
					"Error obteniendo notas debito cliente " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error obteniendo notas debito cliente");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	private void generarQuery(StringBuilder query, Integer idSede, String fechaInicial, String fechaFinal,
			String numeroDocumento, String estadoDocumento) {

		query.append("select * FROM nota_debito_cliente where id_sede= ");

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
		query.append(" order by fecha_documento desc");
	}

	private void generarQueryCantidadDatos(StringBuilder query, Integer idSede, String fechaInicial, String fechaFinal,
			String numeroDocumento, String estadoDocumento) {
		query.append("select count(*) FROM nota_debito_cliente where id_sede= ");
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

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerND(String numeroDocumento) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		try {
			Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
			Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
			InformacionUsuario usuarioInformacion = this.usuarioInformacionDao.buscarPorIdUsuario(usuarioAutenticado.getId());

			int idSede = usuarioInformacion.getIdOrganizacion().getId();
			NotaDebitoCliente notaDatosCliente = this.notaDebitoClienteDao.findByIdSedeNumero(idSede, numeroDocumento);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de nota debito exitosa");
			respuestaDto.setObjetoRespuesta(notaDatosCliente);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo nota debito " + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error obteniendo nota debito");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return respuesta;

	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> actualizarNotaDebito(@RequestBody NotaDebitoCliente notaDebito) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		NotaDebitoCliente notaEnBd = this.notaDebitoClienteDao.findById(notaDebito.getId()).get();
		try {
			if (!notaEnBd.getId().equals(notaDebito.getId())) {

				/* sequenceDao.save(secuenciaSave); */

				NotaDebitoCliente actualizada = this.notaDebitoClienteDao.save(notaDebito);
				actualizada = this.notaDebitoClienteDao.findByNumeroDocumento(notaDebito.getNumeroDocumento());
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito actualizando la nota debito");
				respuestaDto.setObjetoRespuesta(actualizada);
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);

			} else {
				NotaDebitoCliente actualizada = this.notaDebitoClienteDao.save(notaDebito);
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito actualizando la nota debito");
				respuestaDto.setObjetoRespuesta(actualizada);
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
			}

		} catch (Exception e) {
			logger.error("Error en la actualización la nota debito");
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error en la actualización de nota debito");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return respuesta;
	}

	// Borrar
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> borrar(@RequestParam Integer idNotaDebito) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		try {
			conceptoNdDao.eliminarConceptosNotasDebito(idNotaDebito);
			this.notaDebitoClienteDao.delete(this.notaDebitoClienteDao.findById(idNotaDebito).get());

			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			Throwable t = e.getCause();

			if (t instanceof ConstraintViolationException) {
				logger.error("se encuentra asociada en el sistema " + e.getCause());
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST,
						"Error,  se encuentra asociada en el sistema");
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.BAD_REQUEST);
			} else {
				logger.error("Error en el borrado ");
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error en el borrado ");
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

		return respuesta;
	}

	public List<NotaDebitoCliente> notasDebitoPorCliente(Integer idCliente, Date fechaInicial, Date fechaFinal) {
		List<NotaDebitoCliente> lista = notaDebitoClienteDao.busarPorProveedorYFecha(idCliente, fechaInicial, fechaFinal);
		return lista;
	}

	public List<NotaDebitoCliente> listarnotadebitopendiente(Integer idSede) {
		List<NotaDebitoCliente> lista = notaDebitoClienteDao.listarnotadebitopendiente(idSede);
		return lista;
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerNotasDebitoMes(Integer page) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Integer suma = 0;
		try {
			Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
			Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
			InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
					.buscarPorIdUsuario(usuarioAutenticado.getId());
			Pageable pageConfig = PageRequest.of(page, 10);
			int idSede = usuarioInformacion.getIdOrganizacion().getId();
			Page<NotaDebitoCliente> notasDebito = notaDebitoClienteDao.obtenerNotasDebitoDelMes(idSede, pageConfig);
			suma = notaDebitoClienteDao.obtenerSumaDebitoDelMes(idSede);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de notas credito exitosa",
					notasDebito.getContent(), suma, notasDebito.getTotalElements() + "");
			respuestaDto.setObjetoRespuesta(notasDebito);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);

		} catch (RuntimeException e) {
			logger.error("Error al obtener las notas credito" + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al obtener las notas credito " + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	public List<NotaDebitoCliente> listarinformeNotasDebitoVencidasClienteFiltros(Integer idSede, Integer idCliente,
			String fechaInicial, String fechaFinal) {
		List<NotaDebitoCliente> notasDebitos;
		StringBuilder query = new StringBuilder();
		generarQueryInformeNotasDebitoVencidasCliente(query, idSede, fechaInicial, fechaFinal, idCliente);
		TypedQuery<NotaDebitoCliente> notasDebitoInfoQuery = (TypedQuery<NotaDebitoCliente>) entityManager
				.createNativeQuery(query.toString(), NotaDebitoCliente.class);
		notasDebitos = notasDebitoInfoQuery.getResultList();
		return notasDebitos;
	}

	private void generarQueryInformeNotasDebitoVencidasCliente(StringBuilder query, Integer idSede, String fechaInicio,
			String fechaFin, Integer cliente) {

		query.append("select * FROM nota_debito_cliente where id_sede=");

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

	public List<NotaDebitoCliente> notasDebitoPendientes(Integer idCliente) {
		return notaDebitoClienteDao.obtenerNotasDebitosPorPagar(idCliente);
	}

	public List<NotaDebitoCliente> listarnotadebitocartera(Integer idSede) {
		List<NotaDebitoCliente> lista = notaDebitoClienteDao.listarnotadebitocartera(idSede);
		return lista;
	}

	public List<NotaDebitoCliente> listarNotasdebitoFiltros(Integer idSede, String fechaInicial, String fechaFinal,
			String numeroDocumento, Integer idCliente) {
		List<NotaDebitoCliente> notasCredito;
		StringBuilder query = new StringBuilder();
		generarQueryNotasDebitoVencida(query, idSede, fechaInicial, fechaFinal, numeroDocumento, idCliente);
		TypedQuery<NotaDebitoCliente> notasCreditoInfoQuery = (TypedQuery<NotaDebitoCliente>) entityManager
				.createNativeQuery(query.toString(), NotaDebitoCliente.class);
		notasCredito = notasCreditoInfoQuery.getResultList();
		return notasCredito;
	}

	private void generarQueryNotasDebitoVencida(StringBuilder query, Integer idSede, String fechaInicial,
			String fechaFinal,
			String numeroDocumento, Integer cliente) {

		query.append("select * FROM nota_debito_cliente where id_sede=");

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

	public Integer abonoNotasDebito(Integer idSede, String fechaInicial, String fechaFinal,
			String numeroDocumento, Integer cliente) {
		StringBuilder query = new StringBuilder();
		generarQueryAbonos(query, idSede, fechaInicial, fechaFinal, numeroDocumento, cliente);
		Query resultado = entityManager.createNativeQuery(query.toString());
		Double obtenerResult = (Double) resultado.getSingleResult();
		Integer abonos = obtenerResult.intValue();
		return abonos;
	}

	private void generarQueryAbonos(StringBuilder query, Integer idSede, String fechaInicial, String fechaFinal,
			String numeroDocumento, Integer cliente) {

		query.append(
				"select IFNULL(sum(crc.valor_abono),0) from nota_debito_cliente ndc join conceptos_recibo_caja crc on \r\n" +
						"crc.nro_documento = ndc.numero_documento COLLATE utf8mb4_unicode_ci where ndc.id_sede=");

		query.append("" + idSede);
		if (!fechaInicial.equals("null") && fechaFinal.equals("null")) {
			query.append(" and date_format(date(ndc.fecha_documento),'%Y-%m-%d')" + "= date('" + fechaInicial + "')");
		} else if (fechaInicial.equals("null") && !fechaFinal.equals("null")) {
			query.append(" and date_format(date(ndc.fecha_documento),'%Y-%m-%d')" + "= date('" + fechaFinal + "')");
		} else if (!fechaInicial.equals("null") && !fechaFinal.equals("null")) {
			query.append(" and date_format(date(ndc.fecha_documento),'%Y-%m-%d')" + " between " + "date_format(date('"
					+ fechaInicial + "'),'%Y-%m-%d')" + " and " + "date_format(date('" + fechaFinal + "'),'%Y-%m-%d')");
		}
		/*
		 * if(!numeroDocumento.equals("null") && (!fechaInicial.equals("null") &&
		 * !fechaFinal.equals("null"))) { query.append(" and n_nro_factura='" +
		 * numeroDocumento + "'"); }
		 */
		if (!numeroDocumento.equals("null")) {
			query.append(" and ndc.numero_documento='" + numeroDocumento + "'");
		}
		/*
		 * if(cliente!=0 && (!fechaInicial.equals("null") && !fechaFinal.equals("null")
		 * || !numeroDocumento.equals("null"))) { query.append(" and nid_cliente=" +
		 * cliente); }
		 */
		if (cliente != 0) {
			query.append(" and ndc.id_cliente=" + cliente);
		}

	}
}
