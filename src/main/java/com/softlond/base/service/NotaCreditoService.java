package com.softlond.base.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import com.softlond.base.entity.ContableM;
import com.softlond.base.entity.DevolucionCompras;
import com.softlond.base.entity.FacturaCompra;
import com.softlond.base.entity.FacturaM;
import com.softlond.base.entity.InformacionUsuario;
import com.softlond.base.entity.NotaCredito;
import com.softlond.base.entity.NotaDebito;
import com.softlond.base.entity.Organizacion;
import com.softlond.base.entity.Proveedor;
import com.softlond.base.entity.RemisionCompra;
import com.softlond.base.entity.Sequence;
import com.softlond.base.entity.Tercero;
import com.softlond.base.entity.Usuario;
import com.softlond.base.repository.ConceptoNotaCreditoDao;
import com.softlond.base.repository.DevolucionComprasDao;
import com.softlond.base.repository.EstadoDocumentoDao;
import com.softlond.base.repository.FacturaCompraDao;
import com.softlond.base.repository.NotaCreditoDao;
import com.softlond.base.repository.SequenceDao;
import com.softlond.base.repository.UsuarioInformacionDao;
import com.google.gson.Gson;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.MediaType;

@Service
public class NotaCreditoService {

	private static final Logger logger = Logger.getLogger(NotaCreditoService.class);

	@Autowired
	public NotaCreditoDao notaCreditoDao;

	@Autowired
	UsuarioInformacionDao usuarioInformacionDao;

	@Autowired
	private SequenceDao sequenceDao;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private ConceptoNotaCreditoDao conceptoNotaCredito;

	@Autowired
	private FacturaCompraDao facturaCompraDao;

	@Autowired
	private EstadoDocumentoDao estadoDocumentoDao;

	@Autowired
	private DevolucionComprasDao devolucionDao;

	// Crear
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> crearNotaCredito(@RequestBody NotaCredito nuevoNotaCredito) throws Exception {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
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

			NotaCredito guardado = this.notaCreditoDao.save(nuevoNotaCredito);
			sequenceDao.save(secuenciaSave);

			List<ConceptoNotaCredito> conceptos = conceptoNotaCredito.obtenerConceptoIdNotaCredito(guardado.getId());
			if (guardado != null && (guardado.getIdSede().getId() == 7 ||
					guardado.getIdSede().getId() == 15)) {
				for (ConceptoNotaCredito conceptoNC : conceptos) {
					// Integer idSede = sede.getId();
					Integer tipoPago = 1;
					String uri = "http://190.145.10.42:8080/api/v1/TApoloRestInterface/execute";

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

					ContableM facturaMekano = new ContableM();
					String tipoDocumento = "E1";
					facturaMekano.setCLAVE("Set_Contable_Primario");
					switch (guardado.getIdSede().getId()) {
						case 1:
							tipoDocumento = "D4";
							break;
						case 6:
							tipoDocumento = "D2";
							break;
						case 7:
							tipoDocumento = "D3";
							break;
						case 11:
							tipoDocumento = "D1";
							break;
						case 12:
							tipoDocumento = "D6";
							break;
						case 13:
							tipoDocumento = "D10";
							break;
						case 14:
							tipoDocumento = "D9";
							break;
						case 15:
							tipoDocumento = "D5";
							break;

						default:
							break;
					}
					facturaMekano.setTIPO(tipoDocumento);
					facturaMekano.setPREFIJO("_");
					facturaMekano.setNUMERO(guardado.getNumeroDocumento().toString());
					facturaMekano.setFECHA(fechaVenta);
					logger.info(conceptoNC.getCuenta().getCta());
					facturaMekano.setCUENTA(conceptoNC.getCuenta().getCta());
					facturaMekano.setTERCERO(guardado.getIdProveedor().getNit());
					switch (guardado.getIdSede().getId()) {
						case 1:
							facturaMekano.setCENTRO("04");
							break;
						case 6:
							facturaMekano.setCENTRO("02");
							break;
						case 7:
							facturaMekano.setCENTRO("03");
							break;
						case 11:
							facturaMekano.setCENTRO("01");
							break;
						case 12:
							facturaMekano.setCENTRO("06");
							break;
						case 13:
							facturaMekano.setCENTRO("10");
							break;
						case 14:
							facturaMekano.setCENTRO("09");
							break;
						case 15:
							facturaMekano.setCENTRO("05");
							break;

						default:
							break;
					}
					facturaMekano.setACTIVO("NA");
					facturaMekano.setEMPLEADO("24347052");
					tipoPago = conceptoNC.getCuenta().getId();
					if (tipoPago == 102) {
						facturaMekano.setDEBITO(conceptoNC.getValor().toString());
						facturaMekano.setCREDITO("0");
						facturaMekano.setBASE("0");
					} else if (tipoPago == 3) {
						facturaMekano.setDEBITO("0");
						facturaMekano.setCREDITO("0");
						facturaMekano.setBASE("0");
					} else {
						facturaMekano.setDEBITO("0");
						facturaMekano.setCREDITO("0");
						facturaMekano.setBASE("0");
					}
					facturaMekano.setNOTA(conceptoNC.getDetalleCuenta());
					// facturaMekano.setNOTA("-");
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

						if (result.contains("EL TERCERO")) {
							if (result.contains("NO EXISTE")) {
								logger.info("existe un error de tercero inexistente");
								this.crearProveedorMekano(guardado.getIdProveedor(),
										guardado.getIdSede().getId());
								this.crearFacturaMekanoN(guardado, conceptoNC);
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
					}
				}
			}

			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito creando nota crédito");
			respuestaDto.setObjetoRespuesta(guardado);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);

		} catch (Exception e) {
			logger.error("Error en la creación de la nota crédito");
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error en la creación de la nota crédito");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}

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

			// !si el tipo de proveedor es persona natural
			if (proveedor.getClasificacionLegal().getId() == 1 || proveedor.getClasificacionLegal().getId() == 12 || proveedor
					.getClasificacionLegal().getId() == 13 ||
					proveedor.getClasificacionLegal().getId() == 15) {
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

	public void crearFacturaMekanoN(NotaCredito guardado, ConceptoNotaCredito conceptoNC) throws InterruptedException {
		Integer tipoPago = 1;
		String uri = "http://190.145.10.42:8080/api/v1/TApoloRestInterface/execute";

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

		ContableM facturaMekano = new ContableM();
		String tipoDocumento = "D1";
		facturaMekano.setCLAVE("Set_Contable_Primario");
		switch (guardado.getIdSede().getId()) {
			case 1:
				tipoDocumento = "D4";
				break;
			case 6:
				tipoDocumento = "D2";
				break;
			case 7:
				tipoDocumento = "D3";
				break;
			case 11:
				tipoDocumento = "D1";
				break;
			case 12:
				tipoDocumento = "D6";
				break;
			case 13:
				tipoDocumento = "D10";
				break;
			case 14:
				tipoDocumento = "D9";
				break;
			case 15:
				tipoDocumento = "D5";
				break;

			default:
				break;
		}
		facturaMekano.setTIPO(tipoDocumento);
		facturaMekano.setPREFIJO("_");
		// logger.info(factura.getNroFactura().toString());
		facturaMekano.setNUMERO(guardado.getNumeroDocumento().toString());
		facturaMekano.setFECHA(fechaVenta);
		logger.info(conceptoNC.getCuenta().getCta());
		facturaMekano.setCUENTA(conceptoNC.getCuenta().getCta());
		facturaMekano.setTERCERO(guardado.getIdProveedor().getNit());
		switch (guardado.getIdSede().getId()) {
			case 1:
				facturaMekano.setCENTRO("04");
				break;
			case 6:
				facturaMekano.setCENTRO("02");
				break;
			case 7:
				facturaMekano.setCENTRO("03");
				break;
			case 11:
				facturaMekano.setCENTRO("01");
				break;
			case 12:
				facturaMekano.setCENTRO("06");
				break;
			case 13:
				facturaMekano.setCENTRO("10");
				break;
			case 14:
				facturaMekano.setCENTRO("09");
				break;
			case 15:
				facturaMekano.setCENTRO("05");
				break;

			default:
				break;
		}
		facturaMekano.setACTIVO("NA");
		facturaMekano.setEMPLEADO("24347052");
		tipoPago = conceptoNC.getCuenta().getId();
		if (tipoPago == 102) {
			facturaMekano.setDEBITO(conceptoNC.getValor().toString());
			facturaMekano.setCREDITO("0");
			facturaMekano.setBASE("0");
		} else if (tipoPago == 3) {
			facturaMekano.setDEBITO("0");
			facturaMekano.setCREDITO("0");
			facturaMekano.setBASE("0");
		} else {
			facturaMekano.setDEBITO("0");
			facturaMekano.setCREDITO("0");
			facturaMekano.setBASE("0");
		}
		facturaMekano.setNOTA(conceptoNC.getDetalleCuenta());
		// facturaMekano.setNOTA("-");
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
			logger.info(result);
		}
	}

	// Obtener numero documento por sede
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerNumero(Integer idSede) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		try {
			List<NotaCredito> notaDatos = this.notaCreditoDao.findByIdSede(idSede);
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
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerNcFiltros(String numeroDocumento, String fechaInicial, String fechaFinal,
			String estadoDocumento, Integer page) throws Exception {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Integer suma = 0;
		try {
			Page<NotaCredito> notaCredito = null;
			Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
			Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
			InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
					.buscarPorIdUsuario(usuarioAutenticado.getId());

			Pageable pageConfig = PageRequest.of(page, 10);

			int idSede = usuarioInformacion.getIdOrganizacion().getId();

			if (numeroDocumento != "" && fechaInicial == "" && fechaFinal == "" && estadoDocumento == "") {
				notaCredito = this.notaCreditoDao.obtenerNumeroDocumento(idSede, numeroDocumento, pageConfig);
				suma = this.notaCreditoDao.suma1(idSede, numeroDocumento);
			}

			if (numeroDocumento != "" && fechaInicial == "" && fechaFinal == "" && estadoDocumento != "") {
				notaCredito = notaCreditoDao.findByNumeroDocumentoEstado(idSede, numeroDocumento, estadoDocumento,
						pageConfig);
				suma = this.notaCreditoDao.suma2(idSede, numeroDocumento, estadoDocumento);
			}

			if (numeroDocumento != "" && fechaInicial != "" && fechaFinal == "" && estadoDocumento == "") {
				notaCredito = notaCreditoDao.findByNumeroDocumentoFechaI(idSede, numeroDocumento, fechaInicial,
						pageConfig);
				suma = this.notaCreditoDao.suma3(idSede, numeroDocumento, fechaInicial);
			}

			if (numeroDocumento != "" && fechaInicial != "" && fechaFinal != "" && estadoDocumento == "") {
				notaCredito = notaCreditoDao.findByNumeroFechas(idSede, numeroDocumento, fechaInicial, fechaFinal,
						pageConfig);
				suma = this.notaCreditoDao.suma4(idSede, numeroDocumento, fechaInicial, fechaFinal);
			}

			if (numeroDocumento == "" && fechaInicial != "" && fechaFinal != "" && estadoDocumento == "") {
				notaCredito = notaCreditoDao.findByFechas(idSede, fechaInicial, fechaFinal, pageConfig);
				suma = this.notaCreditoDao.suma5(idSede, fechaInicial, fechaFinal);
			}

			if (numeroDocumento == "" && fechaInicial != "" && fechaFinal != "" && estadoDocumento != "") {
				notaCredito = notaCreditoDao.findByFechasEstado(idSede, estadoDocumento, fechaInicial, fechaFinal,
						pageConfig);
				suma = this.notaCreditoDao.suma6(idSede, estadoDocumento, fechaInicial, fechaFinal);
			}

			if (numeroDocumento == "" && fechaInicial != "" && fechaFinal == "" && estadoDocumento == "") {
				notaCredito = notaCreditoDao.findByFechaInicial(idSede, fechaInicial, pageConfig);
				suma = this.notaCreditoDao.suma7(idSede, fechaInicial);
			}

			if (numeroDocumento == "" && fechaInicial != "" && fechaFinal == "" && estadoDocumento != "") {
				notaCredito = notaCreditoDao.findByFechaInicialEstado(idSede, estadoDocumento, fechaInicial,
						pageConfig);
				suma = this.notaCreditoDao.suma8(idSede, estadoDocumento, fechaInicial);
			}

			if (numeroDocumento == "" && fechaInicial == "" && fechaFinal == "" && estadoDocumento != "") {
				notaCredito = this.notaCreditoDao.obtenerEstadoDocumento(idSede, estadoDocumento, pageConfig);
				suma = this.notaCreditoDao.suma9(idSede, estadoDocumento);
			}

			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de remisiones exitosa",
					notaCredito.getContent(), suma, notaCredito.getTotalElements() + "");

			respuestaDto.setObjetoRespuesta(notaCredito);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);

		} catch (RuntimeException e) {
			logger.error("Error al obtener las notas credito" + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al obtener las notas credito " + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
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
			NotaCredito notaDatos = this.notaCreditoDao.findByIdSedeNumero(idSede, numeroDocumento);
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
	public ResponseEntity<Object> actualizarNotaCredito(@RequestBody NotaCredito notaCredito) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		NotaCredito notaEnBd = this.notaCreditoDao.findById(notaCredito.getId()).get();
		try {
			if (!notaEnBd.getId().equals(notaCredito.getId())) {

				NotaCredito actualizada = this.notaCreditoDao.save(notaCredito);

				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito actualizando la nota crédito");
				respuestaDto.setObjetoRespuesta(actualizada);
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);

			} else {
				NotaCredito actualizada = this.notaCreditoDao.save(notaCredito);
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

	public List<NotaCredito> notasCreditoPorProveedor(Integer idProveedor, Date fechaInicial, Date fechaFinal,
			Integer idSede) {
		List<NotaCredito> lista = notaCreditoDao.busarPorProveedorYFecha(idProveedor, fechaInicial, fechaFinal, idSede);
		return lista;
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
			Page<NotaCredito> notasCredito = notaCreditoDao.obtenerNotasCreditoDelMes(idSede, pageConfig);
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

	public List<NotaCredito> listarnotacreditopendiente(Integer idSede) {
		List<NotaCredito> lista = notaCreditoDao.listarnotacreditopendiente(idSede);
		return lista;
	}

	public List<NotaCredito> listarinformeNotasCreditoVencidasCompraFiltros(Integer idSede, Integer idProveedor,
			String fechaInicial, String fechaFinal) {
		List<NotaCredito> notasCredito;
		StringBuilder query = new StringBuilder();
		generarQueryInformeNotasCreditoVencida(query, idSede, fechaInicial, fechaFinal, idProveedor);
		TypedQuery<NotaCredito> notasCreditoInfoQuery = (TypedQuery<NotaCredito>) entityManager
				.createNativeQuery(query.toString(), NotaCredito.class);
		notasCredito = notasCreditoInfoQuery.getResultList();
		return notasCredito;
	}

	private void generarQueryInformeNotasCreditoVencida(StringBuilder query, Integer idSede, String fechaInicio,
			String fechaFin, Integer proveedor) {

		query.append("select * FROM nota_credito where id_sede=");

		query.append("" + idSede);
		if (!fechaInicio.equals("null") && fechaFin.equals("null")) {
			query.append(" and date_format(date(fecha_documento),'%Y-%m-%d')" + "= date('" + fechaInicio + "')");
		} else if (fechaInicio.equals("null") && !fechaFin.equals("null")) {
			query.append(" and date_format(date(fecha_documento),'%Y-%m-%d')" + "<= date('" + fechaFin + "')");
		} else if (!fechaInicio.equals("null") && !fechaFin.equals("null")) {
			query.append(" and date_format(date(fecha_documento),'%Y-%m-%d')" + "between " + "date('" + fechaInicio
					+ "') and " + "date('" + fechaFin + "')");
		}
		if (proveedor != 0) {
			query.append(" and id_proveedor=" + proveedor);
		}
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> eliminarNotaCredito(Integer idNotaCredito) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		try {
			List<ConceptoNotaCredito> conceptos = conceptoNotaCredito.obtenerConceptoIdNotaCredito(idNotaCredito);
			for (ConceptoNotaCredito conceptoNotaCredito : conceptos) {
				if (conceptoNotaCredito.getCodigoDocumento().equals("FAC")) {

					FacturaCompra factura = this.facturaCompraDao.findByNumeroDocumento(conceptoNotaCredito.getNumeroDocumento());
					factura.setCodEstadoCon(this.estadoDocumentoDao.findById(2).get());
					this.facturaCompraDao.save(factura);
				} else {

					DevolucionCompras devolucion = this.devolucionDao
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

	public List<NotaCredito> obtenerPendientesPorProveedor(Integer idProveedor, Integer idSede) {
		List<NotaCredito> notas = notaCreditoDao.obtenerNotasCreditosPorPagar(idProveedor, idSede);
		return notas;
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerNotasCreditoAnular(String numero) {
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		try {
			List<NotaCredito> notasCredito = notaCreditoDao.obtenerNotasCreditoAnular(numero);
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

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN')")
	public ResponseEntity<Object> AnularNotaCredito(NotaCredito notaCredito) {
		logger.info(notaCredito);
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		try {
			logger.info(notaCredito);
			List<ConceptoNotaCredito> conceptos = conceptoNotaCredito.obtenerConceptoIdNotaCredito(notaCredito.getId());
			for (ConceptoNotaCredito conceptoNotaCredito : conceptos) {
				if (conceptoNotaCredito.getCodigoDocumento().equals("FAC")) {

					FacturaCompra factura = this.facturaCompraDao.findByNumeroDocumento(conceptoNotaCredito.getNumeroDocumento());
					factura.setCodEstadoCon(this.estadoDocumentoDao.findById(2).get());
					this.facturaCompraDao.save(factura);
				} else {
					DevolucionCompras devolucion = this.devolucionDao
							.findByNroDevolucion(conceptoNotaCredito.getNumeroDocumento());
					devolucion.setCodEstadoCon(this.estadoDocumentoDao.findById(2).get());
					this.devolucionDao.save(devolucion);
				}
			}
			conceptoNotaCredito.eliminarConceptosNotasCreditos(notaCredito.getId());
			notaCredito.setEstadoDocumento("Anulado");
			notaCreditoDao.save(notaCredito);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "anulacion de nota credito exitosa");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);

		} catch (RuntimeException e) {
			logger.error("Error anulandi la nota credito" + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al anular la nota credito " + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerNotasCreditoPendientes(Integer idProveedor, Integer page) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		try {
			Pageable pageConfig = PageRequest.of(page, 10);
			Page<NotaCredito> notasCredito = notaCreditoDao.obtenerNotasCreditosPorPagarPaginado(idProveedor, pageConfig);
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
	public ResponseEntity<Object> obtenerNotasCreditoPendientesActualizar(Integer idProveedor, Integer idCredito,
			Integer page) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		try {
			Pageable pageConfig = PageRequest.of(page, 10);
			Page<NotaCredito> notasCredito = notaCreditoDao.obtenerNotasCreditosPorPagarPaginadoActualizar(idProveedor,
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

}
