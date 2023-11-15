package com.softlond.base.service;

import java.util.ArrayList;

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
import com.softlond.base.entity.Clientes;
import com.softlond.base.entity.ConceptoNotaCredito;
import com.softlond.base.entity.ContableM;
import com.softlond.base.entity.DevolucionCompras;
import com.softlond.base.entity.FacturaCompra;
import com.softlond.base.entity.InformacionUsuario;
import com.softlond.base.entity.NotaCredito;
import com.softlond.base.entity.Proveedor;
import com.softlond.base.entity.Tercero;
import com.softlond.base.entity.Usuario;
import com.softlond.base.repository.ConceptoNotaCreditoDao;
import com.softlond.base.repository.DevolucionComprasDao;
import com.softlond.base.repository.EstadoDocumentoDao;
import com.softlond.base.repository.FacturaCompraDao;
import com.softlond.base.repository.NotaCreditoDao;
import com.softlond.base.repository.UsuarioInformacionDao;
import java.sql.Date;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@Service
public class ConceptoNotaCreditoService {

	private static final Logger logger = Logger.getLogger(ConceptoNotaCredito.class);

	@Autowired
	public ConceptoNotaCreditoDao conceptoNotaCreditoDao;

	@Autowired
	UsuarioInformacionDao usuarioInformacionDao;

	@Autowired
	private FacturaCompraDao facturaCompraDao;

	@Autowired
	private EstadoDocumentoDao estadoDocumentoDao;

	@Autowired
	private DevolucionComprasDao devolucionDao;
	@Autowired
	private NotaCreditoDao NotaCreditoDao;

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> crearConceptoNC(@RequestBody ConceptoNotaCredito conceptoNC) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		try {

			if (conceptoNC.getCodigoDocumento().equals("FAC")) {

				FacturaCompra factura = this.facturaCompraDao.findByNumeroDocumento(conceptoNC.getNumeroDocumento());
				factura.setCodEstadoCon(this.estadoDocumentoDao.findById(1).get());
				this.facturaCompraDao.save(factura);
				NotaCredito guardado = NotaCreditoDao.obtenerNotaCreditoPorId(conceptoNC.getIdNotaCredito().getId());

				// ! se condiciona la ejecucion del consumo mekano para poder hacer pruebas
				// if (guardado != null && (guardado.getIdSede().getId() == 7 ||
				// guardado.getIdSede().getId() == 15) && 1 != 1) {
				if (guardado != null && (guardado.getIdSede().getId() == 7 ||
						guardado.getIdSede().getId() == 15)) {
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
								this.crearProveedorMekano(guardado.getIdProveedor(), guardado.getIdSede().getId());
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
			} else {
				logger.info("ingresa a NC Dev");
				DevolucionCompras devolucion = this.devolucionDao.findByNroDevolucion(conceptoNC.getNumeroDocumento());
				devolucion.setCodEstadoCon(this.estadoDocumentoDao.findById(1).get());
				this.devolucionDao.save(devolucion);
			}

			ConceptoNotaCredito guardado = this.conceptoNotaCreditoDao.save(conceptoNC);

			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito creando conceptos NC");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);

		} catch (Exception e) {
			logger.error("Error en la creación de conceptos NC" + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error en la creación de conceptos NC" + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return respuesta;
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

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> listarNC() {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		try {
			Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
			Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
			InformacionUsuario usuarioInformacion = this.usuarioInformacionDao.buscarPorIdUsuario(usuarioAutenticado.getId());

			int idSede = usuarioInformacion.getIdOrganizacion().getId();
			ArrayList<ConceptoNotaCredito> concepto = this.conceptoNotaCreditoDao.obtenerConceptosNotaCredito(idSede);

			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de conceptos exitosa");
			respuestaDto.setObjetoRespuesta(concepto);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo conceptos " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error obteniendo conceptos");
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
			Page<ConceptoNotaCredito> notaCredito = null;
			Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
			Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
			InformacionUsuario usuarioInformacion = this.usuarioInformacionDao.buscarPorIdUsuario(usuarioAutenticado.getId());

			Pageable pageConfig = PageRequest.of(page, 10);

			int idSede = usuarioInformacion.getIdOrganizacion().getId();

			if (numeroDocumento != "" && fechaInicial == "" && fechaFinal == "" && estadoDocumento == "") {
				notaCredito = this.conceptoNotaCreditoDao.obtenerNumeroDocumento(idSede, numeroDocumento, pageConfig);
				suma = this.conceptoNotaCreditoDao.suma1(idSede, numeroDocumento);
			}

			if (numeroDocumento != "" && fechaInicial == "" && fechaFinal == "" && estadoDocumento != "") {
				notaCredito = conceptoNotaCreditoDao.findByNumeroDocumentoEstado(idSede, numeroDocumento, estadoDocumento,
						pageConfig);
				suma = this.conceptoNotaCreditoDao.suma2(idSede, numeroDocumento, estadoDocumento);
			}

			if (numeroDocumento != "" && fechaInicial != "" && fechaFinal == "" && estadoDocumento == "") {
				notaCredito = conceptoNotaCreditoDao.findByNumeroDocumentoFechaI(idSede, numeroDocumento, fechaInicial,
						pageConfig);
				suma = this.conceptoNotaCreditoDao.suma3(idSede, numeroDocumento, fechaInicial);
			}

			if (numeroDocumento != "" && fechaInicial != "" && fechaFinal != "" && estadoDocumento == "") {
				notaCredito = conceptoNotaCreditoDao.findByNumeroFechas(idSede, numeroDocumento, fechaInicial, fechaFinal,
						pageConfig);
				suma = this.conceptoNotaCreditoDao.suma4(idSede, numeroDocumento, fechaInicial, fechaFinal);
			}

			if (numeroDocumento == "" && fechaInicial != "" && fechaFinal != "" && estadoDocumento == "") {
				notaCredito = conceptoNotaCreditoDao.findByFechas(idSede, fechaInicial, fechaFinal, pageConfig);
				suma = this.conceptoNotaCreditoDao.suma5(idSede, fechaInicial, fechaFinal);
			}

			if (numeroDocumento == "" && fechaInicial != "" && fechaFinal != "" && estadoDocumento != "") {
				notaCredito = conceptoNotaCreditoDao.findByFechasEstado(idSede, estadoDocumento, fechaInicial, fechaFinal,
						pageConfig);
				suma = this.conceptoNotaCreditoDao.suma6(idSede, fechaInicial, fechaFinal, estadoDocumento);
			}

			if (numeroDocumento == "" && fechaInicial != "" && fechaFinal == "" && estadoDocumento == "") {
				notaCredito = conceptoNotaCreditoDao.findByFechaInicial(idSede, fechaInicial, pageConfig);
				suma = this.conceptoNotaCreditoDao.suma7(idSede, fechaInicial);
			}

			if (numeroDocumento == "" && fechaInicial != "" && fechaFinal == "" && estadoDocumento != "") {
				notaCredito = conceptoNotaCreditoDao.findByFechaInicialEstado(idSede, estadoDocumento, fechaInicial,
						pageConfig);
				suma = this.conceptoNotaCreditoDao.suma8(idSede, fechaInicial, estadoDocumento);
			}

			if (numeroDocumento == "" && fechaInicial == "" && fechaFinal == "" && estadoDocumento != "") {
				notaCredito = this.conceptoNotaCreditoDao.obtenerEstadoDocumento(idSede, estadoDocumento, pageConfig);
				suma = this.conceptoNotaCreditoDao.suma9(idSede, estadoDocumento);
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

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerNcConceptos(Integer idNotaCredito) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		try {

			ArrayList<ConceptoNotaCredito> concepto = this.conceptoNotaCreditoDao.obtenerConceptoIdNotaCredito(idNotaCredito);

			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de conceptos exitosa");
			respuestaDto.setObjetoRespuesta(concepto);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo conceptos " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error obteniendo conceptos");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}
}
