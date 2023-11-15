package com.softlond.base.service;

import java.math.BigDecimal;
import java.math.BigInteger;
// import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.softlond.base.controller.ProductoController;
import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.Clientes;
import com.softlond.base.entity.EnlaceFactura;
import com.softlond.base.entity.EstadoArticulo;
import com.softlond.base.entity.EstadoDocumento;
import com.softlond.base.entity.Factura;
import com.softlond.base.entity.FacturaArticulos;
import com.softlond.base.entity.FacturaCompra;
import com.softlond.base.entity.FacturaRetenciones;
import com.softlond.base.entity.InformacionUsuario;
import com.softlond.base.entity.MaestroValor;
import com.softlond.base.entity.Prefijo;
import com.softlond.base.entity.Producto;
import com.softlond.base.entity.ReciboCajaVenta;
import com.softlond.base.entity.ReferenciaProd;
import com.softlond.base.entity.RemisionVenta;
import com.softlond.base.entity.RetencionFactura;
import com.softlond.base.entity.Sequence;
import com.softlond.base.entity.Tercero;
import com.softlond.base.entity.Usuario;
import com.softlond.base.repository.ArticuloDao;
import com.softlond.base.repository.AsignacionReciboDao;
import com.softlond.base.repository.ConceptosReciboCajaDao;
import com.softlond.base.repository.EstadoDocumentoDao;
import com.softlond.base.repository.FacturaArticuloDao;
import com.softlond.base.repository.FacturaDao;
import com.softlond.base.repository.MaestroValorDao;
import com.softlond.base.repository.PrefijoDao;
import com.softlond.base.repository.RemisionVentaDao;
import com.softlond.base.repository.RetencionFacturaVentaDao;
import com.softlond.base.repository.SequenceDao;
import com.softlond.base.repository.UsuarioInformacionDao;

import net.bytebuddy.asm.AsmVisitorWrapper.ForDeclaredFields;
import net.bytebuddy.build.Plugin.Factory.UsingReflection.ArgumentResolver.ForIndex;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
// import java.sql.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import com.softlond.base.entity.FacturaM;
import com.google.gson.Gson;
// import com.google.api.client.http.HttpHeaders;
import org.springframework.http.HttpHeaders;
import com.google.api.client.util.DateTime;

import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.MediaType;

@Service
public class FacturaService {
	private static final Logger logger = Logger.getLogger(FacturaService.class);

	@Autowired
	private FacturaDao facturaDao;

	@Autowired
	private FacturaArticuloDao facturaArticuloDao;

	@Autowired
	UsuarioInformacionDao usuarioInformacionDao;

	@Autowired
	private SequenceDao sequenceDao;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private MaestroValorDao maestroValorDao;

	@Autowired
	private RemisionVentaDao remisionDao;

	@Autowired
	private EstadoDocumentoDao estadoDao;

	@Autowired
	private RetencionFacturaVentaDao retencionFacturaDao;

	@Autowired
	private PrefijoDao prefijoDao;

	@Autowired
	private ConceptosReciboCajaDao conceptosReciboCajaDao;

	@Autowired
	private AsignacionReciboDao asignacionReciboDao;

	@Autowired
	private ArticuloDao articulo;

	@Autowired
	private EstadoDocumentoDao estadoDocumentoDao;

	@Autowired
	private ProductoService productoService;

	@Autowired
	private PrdDescuentosService descuentoService;

	@Autowired
	private PromocionService promocionService;

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN')")
	public ResponseEntity<Object> listarFacturasSede(Integer page) {
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Integer suma = 0;
		try {
			Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
			Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
			InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
					.buscarPorIdUsuario(usuarioAutenticado.getId());

			Pageable pageConfig = PageRequest.of(page, 10);

			int idSede = usuarioInformacion.getIdOrganizacion().getId();
			Page<Factura> facturas = facturaDao.findAllBySede(idSede, pageConfig);

			for (Factura factura : facturas) {
				List<FacturaArticulos> articulos = facturaArticuloDao.findAllBySede(factura.getId());
				factura.setFacArticulos(articulos);
				suma += factura.getTotal();
			}

			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de facturas exitosa",
					facturas.getContent(), suma, facturas.getTotalElements() + "");

			respuestaDto.setObjetoRespuesta(facturas);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error al obtener las facturas" + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al obtener las facturas " + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	// listar facturas del dia por sede
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN')")
	public ResponseEntity<Object> listarFacturasFechaSede(Integer page) {
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		String fechab = "" + LocalDate.now();
		Integer suma = 0;
		try {
			Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
			Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
			InformacionUsuario usuarioInformacion = this.usuarioInformacionDao.buscarPorIdUsuario(usuarioAutenticado.getId());

			int idSede = usuarioInformacion.getIdOrganizacion().getId();

			suma = sumaFactDia(idSede, fechab);

			Pageable pageConfig = PageRequest.of(page, 10);

			Page<Factura> facturas = facturaDao.findByFechaSede(idSede, fechab, pageConfig);

			for (Factura factura : facturas) {
				List<FacturaArticulos> articulos = facturaArticuloDao.findAllBySede(factura.getId());
				factura.setFacArticulos(articulos);
			}

			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de facturas exitosa",
					facturas.getContent(), suma.toString(), facturas.getTotalElements() + "");

			respuestaDto.setObjetoRespuesta(facturas);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error al obtener las facturas" + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al obtener las facturas " + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	// Listar facturas segun filtros
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN')")
	public ResponseEntity<Object> obtenerFacturaFiltros(Integer idCliente, Integer numeroFactura, String fechaInicial,
			String fechaFinal, Integer estado, String idSedeRequest, Integer page, String orden) throws Exception {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Integer suma = 0;
		String sumas = "";
		BigInteger sumaB = new BigInteger("0");
		Page<Factura> facturas = null;
		ArrayList<Factura> facturass = null;
		int idSede;
		String order = orden;
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

			Pageable pageConfig = PageRequest.of(page, 30);

			if (idCliente != 0 && numeroFactura == 0 && fechaInicial.equals("") && fechaFinal.equals("") && estado == 0) {
				facturas = facturaDao.findByClienteSede(idSede, idCliente, pageConfig);
				suma = sumaFactCliente(idSede, idCliente);
				sumas = suma.toString();
			}

			if (idCliente != 0 && numeroFactura != 0 && fechaInicial.equals("") && fechaFinal.equals("")
					&& estado == 0) {// se selecciona cliente y numero de factura se hace join con
				// conceptos_recibo_caja
				facturas = facturaDao.findNumeroFact(idSede, numeroFactura, pageConfig, orden);
				suma = sumaFactNum(idSede, numeroFactura);
				sumas = suma.toString();
			}

			if (idCliente != 0 && numeroFactura == 0 && !fechaInicial.equals("") && !fechaFinal.equals("")
					&& estado == 0) {// se selecciona cliente fecha inicial y fecha final se hace join con
				// conceptos_recibo_caja
				facturas = facturaDao.findByClienteFechaInicial(idSede, idCliente, fechaInicial, pageConfig, orden);
				suma = sumaFactClienteFechaInicial(idSede, idCliente, fechaInicial);
				sumas = suma.toString();
			}

			if (idCliente != 0 && numeroFactura == 0 && !fechaInicial.equals("") && !fechaFinal.equals("")
					&& estado == 0) {// se selecciona cliente, fecha inicial, fecha final se hace join con
				// conceptos_recibo_caja
				facturas = facturaDao.findByClienteFechas(idSede, idCliente, fechaInicial, fechaFinal, pageConfig,
						orden);
				suma = sumaFactClienteFechasTotal(idSede, idCliente, fechaInicial, fechaFinal);
				sumas = suma.toString();
			}

			if (idCliente != 0 && numeroFactura == 0 && fechaInicial.equals("") && fechaFinal.equals("")
					&& estado == 1) {// se selecciona cliente y estado 1 se hace join con conceptos_recibo_caja
				facturas = facturaDao.findByClienteEstadoCredito(idSede, idCliente, pageConfig, orden);
				suma = sumaFactClienteEstadoTotal(idSede, idCliente);
				sumas = suma.toString();
			}

			if (idCliente != 0 && numeroFactura == 0 && fechaInicial.equals("") && fechaFinal.equals("")
					&& estado == 2) {// se selecciona cliente y estado 2 se hace join con conceptos_recibo_caja
				facturas = facturaDao.findByClienteEstadoContado(idSede, idCliente, pageConfig, orden);
				suma = sumaFactClienteEstadoContadoTotal(idSede, idCliente);
				sumas = suma.toString();
			}

			if (idCliente == 0 && numeroFactura != 0 && fechaInicial.equals("") && fechaFinal.equals("")
					&& estado == 0) {// se selecciona solo numero de factura se hace join con conceptos_recibo_caja
				logger.info("ingresa a la busqueda solo por numero de factura");
				facturas = facturaDao.findNumeroFact(idSede, numeroFactura, pageConfig, orden);
				if (facturas.getContent().size() <= 0) {
					logger.info("no se encontraron facturas - revisar los conceptos por facturas");
					facturas = facturaDao.findNumeroFactA(idSede, numeroFactura, pageConfig, orden);
				} else {
					suma = sumaFactNum(idSede, numeroFactura);
					sumas = suma.toString();
					logger.info(suma);
				}
			}

			if (idCliente == 0 && numeroFactura == 0 && !fechaInicial.equals("") && fechaFinal.equals("")
					&& estado == 0) {// se selecciona solo fecha inicial no se hace join
				facturas = facturaDao.findByFechaSede(idSede, fechaInicial, pageConfig);
				suma = sumaFactDia(idSede, fechaInicial);
				sumas = suma.toString();
			}

			if (idCliente == 0 && numeroFactura == 0 && !fechaInicial.equals("") && fechaFinal.equals("")
					&& estado == 1) {// se selecciona fecha inicial y estado 1 se hace join con conceptos_recibo_caja
				facturas = facturaDao.findByFechaEstadoCredito(idSede, fechaInicial, pageConfig, orden);
				suma = sumaFactFechaCredito(idSede, fechaInicial);
				sumas = suma.toString();
			}

			if (idCliente == 0 && numeroFactura == 0 && !fechaInicial.equals("") && fechaFinal.equals("")
					&& estado == 2) {// se selecciona fecha inicial y estado 2 se hace join con conceptos_recibo_caja
				facturas = facturaDao.findByFechaEstadoContado(idSede, fechaInicial, pageConfig, orden);
				suma = sumaFactFechaContado(idSede, fechaInicial);
				sumas = suma.toString();
			}

			if (idCliente == 0 && numeroFactura == 0 && !fechaInicial.equals("") && !fechaFinal.equals("")
					&& estado == 0) {// se selecciona fecha inicial y fecha final solamente no se hace join
				facturas = facturaDao.findByFechas(idSede, fechaInicial, fechaFinal, pageConfig);
				suma = sumaFactFechas(idSede, fechaInicial, fechaFinal);
				sumas = suma.toString();
			}

			if (idCliente == 0 && numeroFactura == 0 && !fechaInicial.equals("") && !fechaFinal.equals("")
					&& estado == 1) {// se selecciona fecha inicial, fecha final y estado 1 se hace join con
				// conceptos_recibo_caja
				facturas = facturaDao.findByFechasEstadoCredito(idSede, fechaInicial, fechaFinal, pageConfig, orden);
				suma = sumaFactFechasEstadoCredito(idSede, fechaInicial, fechaFinal);
				sumas = suma.toString();
			}

			if (idCliente == 0 && numeroFactura == 0 && !fechaInicial.equals("") && !fechaFinal.equals("")
					&& estado == 2) {// se selecciona fecha inicial, fecha final y estado 2 se hace join con
				// conceptos_recibo_caja
				facturas = facturaDao.findByFechasEstadoContado(idSede, fechaInicial, fechaFinal, pageConfig, orden);
				suma = sumaFactFechasEstadoContado(idSede, fechaInicial, fechaFinal);
				sumas = suma.toString();
			}

			if (idCliente == 0 && numeroFactura == 0 && fechaInicial.equals("") && fechaFinal.equals("") && estado == 1) {
				facturas = facturaDao.findByVentasCredito(idSede, pageConfig);
				suma = sumaFactCredito(idSede);
				sumas = suma.toString();
			}

			if (idCliente == 0 && numeroFactura == 0 && fechaInicial.equals("") && fechaFinal.equals("") && estado == 2) {
				facturas = facturaDao.findByVentasPago(idSede, pageConfig);
				suma = sumaFactPago(idSede);
				sumas = suma.toString();
			}

			// ----------------------------------------------------------

			if (idCliente == 0 && numeroFactura == 0 && fechaInicial.equals("") && fechaFinal.equals("") && estado == 0) {
				logger.info("ingresa a busqueda por todas las remisiones");
				facturas = facturaDao.findByTodoRemisiones(idSede, pageConfig);

				sumas = facturaDao.findByPagoTotalRemisiones(idSede).toString();

			}
			for (Factura factura : facturas) {
				List<FacturaArticulos> articulos = facturaArticuloDao.findAllBySede(factura.getId());
				factura.setFacArticulos(articulos);
			}

			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de facturas exitosa",
					facturas.getContent(), sumas, facturas.getTotalElements() + "");

			respuestaDto.setObjetoRespuesta(facturas);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);

		} catch (RuntimeException e) {
			logger.error("Error al obtener las facturas" + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al obtener las facturas " + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	// Listar facturas segun cliente por sede
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN')")
	public ResponseEntity<Object> listarFacturasClienteSede(Integer idCliente, Integer page) {
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Integer suma = 0;

		try {
			Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
			Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
			InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
					.buscarPorIdUsuario(usuarioAutenticado.getId());

			Pageable pageConfig = PageRequest.of(page, 10);

			int idSede = usuarioInformacion.getIdOrganizacion().getId();
			suma = sumaFactCliente(idSede, idCliente);
			Page<Factura> facturas = facturaDao.findByClienteSede(idSede, idCliente, pageConfig);

			for (Factura factura : facturas) {
				List<FacturaArticulos> articulos = facturaArticuloDao.findAllBySede(factura.getId());
				factura.setFacArticulos(articulos);
			}

			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de facturas exitosa",
					facturas.getContent(), suma, facturas.getTotalElements() + "");

			respuestaDto.setObjetoRespuesta(facturas);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error al obtener las facturas" + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al obtener las facturas " + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	// Listar facturas credito
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN')")
	public ResponseEntity<Object> listarFacturasVentaCredito(Integer page) {
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Integer suma = 0;

		try {
			Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
			Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
			InformacionUsuario usuarioInformacion = this.usuarioInformacionDao.buscarPorIdUsuario(usuarioAutenticado.getId());

			Pageable pageConfig = PageRequest.of(page, 10);

			int idSede = usuarioInformacion.getIdOrganizacion().getId();
			suma = sumaFactCredito(idSede);
			Page<Factura> facturas = facturaDao.findByVentasCredito(idSede, pageConfig);

			for (Factura factura : facturas) {
				List<FacturaArticulos> articulos = facturaArticuloDao.findAllBySede(factura.getId());
				factura.setFacArticulos(articulos);
			}

			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de facturas exitosa",
					facturas.getContent(), suma, facturas.getTotalElements() + "");

			respuestaDto.setObjetoRespuesta(facturas);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error al obtener las facturas" + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al obtener las facturas " + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	// Listar facturas pagadas
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN')")
	public ResponseEntity<Object> listarFacturasVentaPago(Integer page) {
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Integer suma = 0;

		try {
			Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
			Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
			InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
					.buscarPorIdUsuario(usuarioAutenticado.getId());

			Pageable pageConfig = PageRequest.of(page, 10);

			int idSede = usuarioInformacion.getIdOrganizacion().getId();

			Page<Factura> facturas = facturaDao.findByVentasPago(idSede, pageConfig);

			for (Factura factura : facturas) {

				List<FacturaArticulos> articulos = facturaArticuloDao.findAllBySede(factura.getId());
				factura.setFacArticulos(articulos);
			}

			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de facturas exitosa",
					facturas.getContent(), suma, facturas.getTotalElements() + "");

			respuestaDto.setObjetoRespuesta(facturas);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error al obtener las facturas" + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al obtener las facturas " + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	// Buscar factura por numero de factura
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN')")
	public ResponseEntity<Object> obtenerFacturaNumero(Integer numeroFactura) {
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao.buscarPorIdUsuario(usuarioAutenticado.getId());
		Integer idSede = usuarioInformacion.getIdOrganizacion().getId();
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		try {
			ArrayList<Factura> facturas = this.facturaDao.findByNumAnular(numeroFactura, idSede);
			// ArrayList<Factura> facturas = this.facturaDao.findByNumAnular(numeroFactura);
			if (facturas.size() > 0) {
				logger.info(facturas.size());
			}
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de facturas exitosa");

			respuestaDto.setObjetoRespuesta(facturas);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error al obtener las facturas" + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al obtener las facturas " + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	// Buscar factura por fecha
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER')")
	public ResponseEntity<Object> obtenerFacturaFecha(String fechaInicial, String fechaFinal, Integer page) {
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Integer suma = 0;

		try {
			Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
			Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
			InformacionUsuario usuarioInformacion = this.usuarioInformacionDao.buscarPorIdUsuario(usuarioAutenticado.getId());

			Pageable pageConfig = PageRequest.of(page, 10);

			int idSede = usuarioInformacion.getIdOrganizacion().getId();

			if (fechaFinal != "") {
				Page<Factura> facturas = facturaDao.findByFechas(idSede, fechaInicial, fechaFinal, pageConfig);
				suma = sumaFactFechas(idSede, fechaInicial, fechaFinal);

				for (Factura factura : facturas) {
					List<FacturaArticulos> articulos = facturaArticuloDao.findAllBySede(factura.getId());
					factura.setFacArticulos(articulos);
				}
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de facturas exitosa",
						facturas.getContent(), suma, facturas.getTotalElements() + "");

				respuestaDto.setObjetoRespuesta(facturas);
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
			}

			else {
				Page<Factura> facturas = facturaDao.findByFechaSede(idSede, fechaInicial, pageConfig);
				suma = sumaFactDia(idSede, fechaInicial);
				for (Factura factura : facturas) {
					List<FacturaArticulos> articulos = facturaArticuloDao.findAllBySede(factura.getId());
					factura.setFacArticulos(articulos);
				}
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de facturas exitosa",
						facturas.getContent(), suma, facturas.getTotalElements() + "");

				respuestaDto.setObjetoRespuesta(facturas);
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
			}
		} catch (Exception e) {
			logger.error("Error al obtener las facturas" + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al obtener las facturas " + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN')")
	public ResponseEntity<Object> crearFactura(@RequestBody Factura factura) throws Exception {

		factura.setIva(Math.round(factura.getIva() * 100000) / 100000d);
		factura.setSubTotalVenta(Math.round(factura.getSubTotalVenta() * 100000) / 100000d);
		ResponseEntity<Object> respuesta;
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		Sequence secuenciaSave;

		logger.info(factura.getPrefijo().getPrefijo() + " - " + factura.getIdSede().getId() + " - " + factura.getPrefijo()
				.getId());
		Integer sequencia = sequenceDao.sequenciaNueva(factura.getPrefijo().getPrefijo(), factura.getIdSede().getId(),
				factura.getPrefijo().getId());

		try {
			factura.setUsuarioMod(usuarioAutenticado);
			factura.setIdUsuarioAutorizacion(usuarioAutenticado);
			if (factura.getIdReciboCaja() != null) {
				factura.getIdReciboCaja().setIdCreador(usuarioAutenticado);
				factura.getIdReciboCaja().setIdMoneda(null);
				factura.getIdReciboCaja().setCodRbocajaventa("" + factura.getPrefijo().getPrefijo() + factura.getNroFactura());
			}
			if (factura.getFechaVenta() == null) {
				factura.setFechaVenta(new java.util.Date());
				factura.setFechaMod(new java.util.Date());
				factura.setFechaVencimientoCr(new java.util.Date());
				if (factura.getIdReciboCaja() != null) {
					factura.getIdReciboCaja().setFechaPago(new java.sql.Date(factura.getFechaVenta().getTime()));
					factura.getIdReciboCaja().setFechaMod(new java.sql.Date(factura.getFechaVenta().getTime()));
				}
			}
			factura.setNroFactura(sequencia);

			gestionarRemision(factura);
			Factura facturaGuardada = facturaDao.save(factura);

			for (int j = 0; j < factura.getFacArticulos().size(); j++) {
				Float cantidadDisponibleModificada = articulo
						.obtenerCantDispArticuloFac(factura.getFacArticulos().get(j).getArticulo().getId().intValue())
						- factura.getFacArticulos().get(j).getCantidad().floatValue();
				articulo.cambiarCantidadDisponibleFac(cantidadDisponibleModificada,
						factura.getFacArticulos().get(j).getArticulo().getId().intValue());
				if (cantidadDisponibleModificada <= 0) {
					logger.info("ingresa a cambiar estado del articulo en la factura");
					EstadoArticulo estadoA = factura.getFacArticulos().get(j).getArticulo().getEstadoArticulo();
					estadoA.setId(4);
					logger.info(estadoA.getId());
					articulo.cambiarEstadoArticuloFinalizado(estadoA.getId(),
							factura.getFacArticulos().get(j).getArticulo().getId().intValue());
					logger.info(factura.getFacArticulos().get(j).getArticulo().getEstadoArticulo());
				}
				// ! api creacion primario (factura electronica)
				// ! se condiciona la ejecucion del consumo mekano para poder hacer pruebas
				// if (factura.getIdTipoVenta().getId() == 15
				// && (factura.getIdSede().getId() == 7 || factura.getIdSede().getId() == 15) &&
				// 1 != 1) {
				if (factura.getIdTipoVenta().getId() == 15
						&& (factura.getIdSede().getId() == 7 || factura.getIdSede().getId() == 15)) {
					logger.info("ingresa a la creacion de factura electronica desde la sede: " + factura.getIdSede().getId());

					String uri = "http://190.145.10.42:8080/api/v1/TApoloRestInterface/execute";

					// logger.info(factura.getFechaVenta().toString());
					// logger.info(factura.getFechaVencimientoCr().toString());
					Date fechaEntradaVenta = factura.getFechaVenta();
					Calendar calVent = Calendar.getInstance();
					calVent.setTime(fechaEntradaVenta);

					int yearVent = calVent.get(Calendar.YEAR);
					int monthVent = calVent.get(Calendar.MONTH) + 1;
					int dayVent = calVent.get(Calendar.DAY_OF_MONTH);

					Date fechaEntradaVence = factura.getFechaVencimientoCr();
					Calendar calVenc = Calendar.getInstance();
					calVenc.setTime(fechaEntradaVence);

					int yearVenc = calVenc.get(Calendar.YEAR);
					int monthVenc = calVenc.get(Calendar.MONTH) + 1;
					int dayVenc = calVenc.get(Calendar.DAY_OF_MONTH);

					String fechaVenta = dayVent + "." + monthVent + "." + yearVent;
					logger.info(fechaVenta);
					String fechaVence = dayVenc + "." + monthVenc + "." + yearVenc;
					logger.info(fechaVence);

					FacturaM facturaMekano = new FacturaM();
					facturaMekano.setCLAVE("Set_Gestion_Primario");
					facturaMekano.setTIPO("FE1");
					facturaMekano.setPREFIJO(factura.getPrefijo().getPrefijo().toUpperCase());
					// facturaMekano.setPREFIJO("MT5");
					logger.info(factura.getNroFactura().toString());
					facturaMekano.setNUMERO(factura.getNroFactura().toString());
					facturaMekano.setFECHA(fechaVenta);
					facturaMekano.setVENCE(fechaVence);
					facturaMekano.setTERCERO(factura.getIdCliente().getNitocc().toString().trim());
					// ! facturaMekano.setVENDEDOR(factura.getIdVendedor().getNumeroDocumento());
					// ! se envia un valor por defecto para vendedores porque la api e integracion
					// de
					// ! usuarios no estaba en el alcance de la integracion con mekano
					facturaMekano.setVENDEDOR("24347052");
					facturaMekano.setLISTA("NA");
					facturaMekano.setBANCO("CG");
					// ! se envia un valor por defecto para usuario en sesion porque la api e
					// ! integracion de
					// ! usuarios no estaba en el alcance de la integracion con mekano
					facturaMekano.setUSUARIO("SUPERVISOR");
					switch (factura.getIdSede().getId()) {
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
					facturaMekano.setBODEGA("NA");
					facturaMekano.setREFERENCIA(factura.getFacArticulos().get(j).getArticulo().getProducto().getCodigo());
					facturaMekano.setENTRADA(0);
					facturaMekano.setSALIDA(factura.getFacArticulos().get(j).getCantidad());
					// facturaMekano.setSALIDA(factura.getFacArticulos().get(j).getCantidad().intValue());
					logger.info(factura.getFacArticulos().get(j).getPrecioUnitario());
					// if (factura.getFacArticulos().get(j).getPrecioUnitario() > 0.0) {
					// facturaMekano
					// .setUNITARIO(factura.getFacArticulos().get(j).getPrecioUnitario()
					// - (factura.getFacArticulos().get(j).getPrecioUnitario()
					// *
					// ((factura.getFacArticulos().get(j).getPorcentajeDescuento()).floatValue())));
					// logger.info(((factura.getFacArticulos().get(j).getPorcentajeDescuento()).floatValue())
					// * 100);
					// // facturaMekano
					// //
					// .setUNITARIO(factura.getFacArticulos().get(j).getPrecioUnitario().intValue());
					// }
					if (factura.getFacArticulos().get(j).getPrecioUnitario() > 0.0) {
						// facturaMekano
						// .setUNITARIO(factura.getFacArticulos().get(index).getPrecioUnitario());
						logger.info(factura.getFacArticulos().get(j).getPorcentajeIva());
						facturaMekano
								.setUNITARIO((factura.getFacArticulos().get(j).getSubtotal()
										* (1 + (factura.getFacArticulos().get(j).getPorcentajeIva() / 100)))
										/ factura.getFacArticulos().get(j).getCantidad());

					} else {
						facturaMekano.setUNITARIO(0.0);
						// facturaMekano.setUNITARIO(0);
					}
					// Float descuento = (Float) ((RespuestaDto) descuentoService
					// .obtenerPorClienteClasificacion(factura.getIdCliente().getIdClasificacion().getId(),
					// factura.getFacArticulos().get(j).getArticulo().getProducto().getTipo().getId(),
					// factura.getFacArticulos().get(j).getArticulo().getProducto().getReferencia().getId(),
					// factura.getFacArticulos().get(j).getArticulo().getProducto().getPresentacion().getId())
					// .getBody()).getObjetoRespuesta();

					// logger.info(descuento);

					// Float promocion = (Float) ((RespuestaDto) promocionService
					// .obtenerPromocionDia(factura.getFacArticulos().get(j).getArticulo().getProducto().getTipo().getId(),
					// factura.getFacArticulos().get(j).getArticulo().getProducto().getReferencia().getId(),
					// factura.getFacArticulos().get(j).getArticulo().getProducto().getPresentacion().getId(),
					// "jueves")
					// .getBody()).getObjetoRespuesta();

					// logger.info(promocion);
					// if (descuento > promocion) {
					// facturaMekano.setPORC_DESCUENTO(descuento);
					// } else {
					// if (promocion > 0) {
					// facturaMekano.setPORC_DESCUENTO(promocion);
					// } else {
					// facturaMekano.setPORC_DESCUENTO(1.0f);
					// }
					// }
					facturaMekano
							.setPORC_DESCUENTO(0f);
					// facturaMekano
					// .setPORC_DESCUENTO(((factura.getFacArticulos().get(j).getPorcentajeDescuento()).floatValue())
					// * 100);
					// !revisar el campo de observaciones porque con factura.getObservaciones() sale
					// !error se envia valor por defecto
					// if (!factura.getObservaciones().equals(null)) {
					// facturaMekano.setNOTA(factura.getObservaciones());
					// } else {
					// facturaMekano.setNOTA("-");
					// }
					facturaMekano.setNOTA("-");
					// facturaMekano.setNOTA("-");

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
								// this.crearClienteMekano(factura.getIdCliente());
								// this.crearFacturaMekanoN(factura, j,
								// (factura.getFacArticulos().get(j).getPorcentajeDescuento()).floatValue());
								logger.info("existe un error de tercero inexistente");
								throw new Exception("tercero inexistente");
							}
						}
						logger.info(result);

						if (result.contains("LA REFERENCIA")) {
							if (result.contains("NO EXISTE")) {
								logger.info("existe un error de referencia inexistente");
								this.crearProductoMekano(factura.getFacArticulos().get(j).getArticulo().getProducto().getId());
								this.crearFacturaMekanoN(factura, j,
										(factura.getFacArticulos().get(j).getPorcentajeDescuento()).floatValue());
							}
						}

						logger.info(result);
						if (result.contains("EL DOCUMENTO")) {
							if (result.contains("YA EXISTE")) {
								logger.info("existe un error de DOCUMENTO existente");
								throw new Exception("Ya existe un DOCUMENTO con este CODIGO");
							}
						}

						if (result.contains("EL EMPLEADO")) {
							if (result.contains("NO EXISTE")) {
								logger.info("existe un error de EMPLEADO inexistente");
								throw new Exception("No existe un EMPLEADO con este CODIGO");
							}
						}

						if (result.contains("LA CAJA o BANCO")) {
							if (result.contains("NO EXISTE")) {
								logger.info("existe un error de CAJA o BANCO inexistente");
								throw new Exception("No existe CAJA o BANCO con este CODIGO");
							}
						}

						if (result.contains("LA BODEGA")) {
							if (result.contains("NO EXISTE")) {
								logger.info("existe un error de BODEGA inexistente");
								throw new Exception("No existe BODEGA con este CODIGO");
							}
						}

						if (result.contains("EL USUARIO")) {
							if (result.contains("NO EXISTE")) {
								logger.info("existe un error de USUARIO inexistente");
								throw new Exception("No existe USUARIO con este CODIGO");
							}
						}

						if (result.contains("LA LISTA DE PRECIOS")) {
							if (result.contains("NO EXISTE")) {
								logger.info("existe un error de LISTA DE PRECIOS inexistente");
								throw new Exception("No existe LISTA DE PRECIOS con este CODIGO");
							}
						}

						if (result.contains("EL CENTRO")) {
							if (result.contains("NO EXISTE")) {
								logger.info("existe un error de CENTRO inexistente");
								throw new Exception("No existe CENTRO con este CODIGO");
							}
						}
					}
				}
			}
			this.pasoFacturaMekano(factura.getPrefijo().getPrefijo().toUpperCase(), factura.getNroFactura().toString());
			logger.info("peticion factura electronica exitosa");

			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "factura creada exitosamente");
			respuestaDto.setObjetoRespuesta(facturaGuardada);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error al guardar la factura " + e.getMessage() + " " + e.getStackTrace()[0].getLineNumber());
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al guardar la factura" + e.getMessage());
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	private void pasoFacturaMekano(String prefijoFactura, String numeroFactura) {
		String clave = "Set_Termina_Gestion_Primario";
		String tipo = "FE1";
		String prefijo = prefijoFactura;
		String numero = numeroFactura;
		String uri = "http://190.145.10.42:8080/api/v1/TApoloRestInterface/execute";

		EnlaceFactura enlace = new EnlaceFactura();
		enlace.setCLAVE(clave);
		enlace.setTIPO(tipo);
		enlace.setPREFIJO(prefijo);
		enlace.setNUMERO(numero);

		Gson gson = new Gson();
		String rjson = gson.toJson(enlace);
		logger.info(rjson);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		HttpEntity<String> entity = new HttpEntity<String>(rjson, headers);
		RestTemplate rest = new RestTemplate();
		// String result = rest.postForObject(uri, entity, String.class);
		// logger.info(result);
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
				// case 13:
				// clasificacionLegal = "N";
				// codSociedad = "02";
				// break;
				// case 14:
				// clasificacionLegal = "J";
				// codSociedad = "03";
				// break;
				// case 15:
				// clasificacionLegal = "N";
				// codSociedad = "04";
				// break;
				// case 16:
				// clasificacionLegal = "J";
				// codSociedad = "05";
				// break;
				// case 17:
				// clasificacionLegal = "J";
				// codSociedad = "06";
				// break;
				// case 18:
				// clasificacionLegal = "J";
				// codSociedad = "07";
				// break;
				// case 19:
				// clasificacionLegal = "J";
				// codSociedad = "08";
				// break;
				// case 20:
				// clasificacionLegal = "J";
				// codSociedad = "09";
				// break;
				// case 21:
				// clasificacionLegal = "J";
				// codSociedad = "10";
				// break;
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

	private void crearProductoMekano(Integer idI) {
		String codigo = "";
		String codigoD = "";
		String nombre = "";
		String nombreD = "";
		String costo = "0.0";
		String precio = "0.0";
		String iva = "E19";
		String ivaV = "";
		String medida = "MTR";
		// Double costo = ArticuloDao.obtenerUltimoPrecioCosto(body.getId(), idSede);

		try {
			List<Producto> productos = productoService.listarTodos(idI);
			logger.info("ingresa a productos listar-todos");
			logger.info(productos.size());
			if (productos.size() > 0 && productos.get(0) != null) {
				logger.info(productos.size());
				codigo = productos.get(0).getCodigo() != null ? productos.get(0).getCodigo() : "";
				nombre = productos.get(0).getTipo().getTTipo() + " " + productos.get(0).getReferencia().getTreferencia() + " "
						+ productos.get(0).getPresentacion().getTPresentacion() + " " + productos.get(0).getColor().getTColor();
				if (productos.get(0).getPrecios().size() > 0) {
					costo = productos.get(0).getPrecios().get(0).getPrecioCosto() != null
							? productos.get(0).getPrecios().get(0).getPrecioCosto().toString()
							: "0.0";
					precio = productos.get(0).getPrecios().get(0).getPrecioVenta() != null
							? productos.get(0).getPrecios().get(0).getPrecioVenta().toString()
							: "0.0";
				}
				ivaV = productos.get(0).getIva() + "";
				logger.info("el iva es de:");
				logger.info(ivaV);

				if (ivaV.equals("0.0")) {
					iva = "E0";
				} else if (ivaV.equals("19.0")) {
					iva = "E19";
				} else if (ivaV.equals("5.0")) {
					iva = "E5";
				}

				if (productos.get(0).getUnidad().getId() == 1) {
					logger.info("la unidad es el metro");
					medida = "MTR";
				} else if (productos.get(0).getUnidad().getId() == 2) {
					logger.info("la unidad es el unidad");
					medida = "UND";
				} else if (productos.get(0).getUnidad().getId() == 3) {
					logger.info("la unidad es el paquete");
					medida = "PAQ";
				}

				logger.info(iva);
				logger.info(medida);

				String uri = "http://190.145.10.42:8080/api/v1/TApoloRestInterface/execute";

				ReferenciaProd referenciaMekano = new ReferenciaProd();
				referenciaMekano.setCLAVE("Set_Referencias");
				referenciaMekano.setCODIGO(codigo);
				referenciaMekano.setCODIGO2(codigoD);
				referenciaMekano.setNOMBRE(nombre.toUpperCase());
				referenciaMekano.setNOMBRE2(nombreD.toUpperCase());
				referenciaMekano.setCOSTO(costo);
				referenciaMekano.setPRECIO(precio);
				referenciaMekano.setCODLINEA("PROD");
				referenciaMekano.setCODMEDIDA(medida);
				referenciaMekano.setCOD_ESQIMPUESTO(iva);
				referenciaMekano.setCOD_ESQRETENCION("ECOM1");
				referenciaMekano.setCOD_ESQCONTABLE("ESQ");

				logger.info(referenciaMekano);

				Gson gson = new Gson();
				String rjson = gson.toJson(referenciaMekano);
				logger.info(rjson);
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
				HttpEntity<String> entity = new HttpEntity<String>(rjson, headers);
				logger.info(entity);
				RestTemplate rest = new RestTemplate();
				// if (entity != null && 1 != 1) {
				if (entity != null) {
					String result = rest.postForObject(uri, entity, String.class);
					logger.info(result);
				}
			}
		} catch (Exception ex) {
			String msj = "Error creando producto...";
			logger.error(msj + "\n");
			logger.error(ex);
		}
	}

	// !se debe corregir porque solo estaba tomando 1 factura y debe volver
	// !a recorrer todos los conceptos
	private void crearFacturaMekanoN(Factura factura, Integer index, float descuento) throws InterruptedException {
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		Integer idSede = usuarioInformacion.getIdOrganizacion().getId();

		// ! se condiciona la ejecucion del consumo mekano para poder hacer pruebas
		// if (factura.getIdTipoVenta().getId() == 15 && (idSede == 7 || idSede == 15)
		// && 1 != 1) {
		if (factura.getIdTipoVenta().getId() == 15 && (idSede == 7 || idSede == 15)) {
			String uri = "http://190.145.10.42:8080/api/v1/TApoloRestInterface/execute";
			logger.info(factura.getFechaVenta().toString());
			logger.info(factura.getFechaVencimientoCr().toString());
			Date fechaEntradaVenta = factura.getFechaVenta();
			Calendar calVent = Calendar.getInstance();
			calVent.setTime(fechaEntradaVenta);

			int yearVent = calVent.get(Calendar.YEAR);
			int monthVent = calVent.get(Calendar.MONTH) + 1;
			int dayVent = calVent.get(Calendar.DAY_OF_MONTH);

			Date fechaEntradaVence = factura.getFechaVencimientoCr();
			Calendar calVenc = Calendar.getInstance();
			calVenc.setTime(fechaEntradaVence);

			int yearVenc = calVenc.get(Calendar.YEAR);
			int monthVenc = calVenc.get(Calendar.MONTH) + 1;
			int dayVenc = calVenc.get(Calendar.DAY_OF_MONTH);

			String fechaVenta = dayVent + "." + monthVent + "." + yearVent;
			logger.info(fechaVenta);
			String fechaVence = dayVenc + "." + monthVenc + "." + yearVenc;
			logger.info(fechaVence);

			FacturaM facturaMekano = new FacturaM();
			facturaMekano.setCLAVE("Set_Gestion_Primario");
			facturaMekano.setTIPO("FE1");
			facturaMekano.setPREFIJO(factura.getPrefijo().getPrefijo().toUpperCase());
			logger.info(factura.getNroFactura().toString());
			facturaMekano.setNUMERO(factura.getNroFactura().toString());
			facturaMekano.setFECHA(fechaVenta);
			facturaMekano.setVENCE(fechaVence);
			facturaMekano.setTERCERO(factura.getIdCliente().getNitocc().toString());
			facturaMekano.setVENDEDOR("24347052");
			facturaMekano.setLISTA("NA");
			facturaMekano.setBANCO("CG");
			facturaMekano.setUSUARIO("SUPERVISOR");
			switch (factura.getIdSede().getId()) {
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
			facturaMekano.setBODEGA("NA");
			facturaMekano.setREFERENCIA(factura.getFacArticulos().get(index).getArticulo().getProducto().getCodigo());
			facturaMekano.setENTRADA(0);
			facturaMekano.setSALIDA(factura.getFacArticulos().get(index).getCantidad());
			logger.info(factura.getFacArticulos().get(index).getPrecioUnitario());
			// if (factura.getFacArticulos().get(index).getPrecioUnitario() > 0.0) {
			// // facturaMekano
			// // .setUNITARIO(factura.getFacArticulos().get(index).getPrecioUnitario());
			// facturaMekano
			// .setUNITARIO(factura.getFacArticulos().get(index).getPrecioUnitario()
			// - (factura.getFacArticulos().get(index).getPrecioUnitario()
			// * ((descuento))));
			// } else {
			if (factura.getFacArticulos().get(index).getPrecioUnitario() > 0.0) {
				// facturaMekano
				// .setUNITARIO(factura.getFacArticulos().get(index).getPrecioUnitario());
				logger.info(factura.getFacArticulos().get(index).getPorcentajeIva());
				facturaMekano
						.setUNITARIO((factura.getFacArticulos().get(index).getSubtotal()
								* (1 + (factura.getFacArticulos().get(index).getPorcentajeIva() / 100)))
								/ factura.getFacArticulos().get(index).getCantidad());

			} else {
				facturaMekano.setUNITARIO(0.0);
			}
			// facturaMekano.setPORC_DESCUENTO(descuento * 100);
			facturaMekano.setPORC_DESCUENTO(0f);
			facturaMekano.setNOTA("-");
			// facturaMekano.setNOTA("-");

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
				if (result.contains("LA REFERENCIA")) {
					if (result.contains("NO EXISTE")) {
						logger.info("existe un error de referencia inexistente");
						this.crearProductoMekano(factura.getFacArticulos().get(index).getArticulo().getProducto().getId());
						// this.crearFacturaMekanoN(factura, index);
					}
				}
				logger.info(result);
			}
		}
	}

	private void gestionarRemision(Factura factura) {
		RemisionVenta remision = remisionDao.obtenerRemisionSede(factura.getRemision(), factura.getIdSede().getId());

		if (remision != null) {
			facturaArticuloDao.eliminarArticulosRemision(remision.getId());
			remision.setFacArticulos(factura.getFacArticulos());
			EstadoDocumento estado = estadoDao.findById(1).orElse(null);
			remision.setTotal(factura.getTotal());
			remision.setSubTotal(factura.getSubTotalVenta());
			remision.setIva(factura.getIva());
			remision.setCodEstadoCon(estado);
			remisionDao.save(remision);
		} else {
			RemisionVenta nuevaRemision = new RemisionVenta();
			nuevaRemision.setTotal(factura.getTotal());
			nuevaRemision.setSubTotal(factura.getSubTotalVenta());
			nuevaRemision.setFacArticulos(factura.getFacArticulos());
			nuevaRemision.setSede(factura.getIdSede());
			nuevaRemision.setIdCliente(factura.getIdCliente());
			nuevaRemision.setIva(factura.getIva());
			EstadoDocumento estado = estadoDao.findById(1).orElse(null);
			nuevaRemision.setCodEstadoCon(estado);
			nuevaRemision.setIdVendedor(factura.getIdVendedor());
			Date fechaActualizar = new Date(factura.getFechaVenta().getTime());
			nuevaRemision.setFecha(new java.sql.Date(fechaActualizar.getTime()));
			// nuevaRemision.setFecha(fechaActualizar);
			Sequence sequence = sequenceDao.SequenciaRemision(factura.getIdSede().getId()).orElse(null);
			Prefijo prefijo = prefijoDao.obtenerPrefijoRemision(factura.getIdSede().getId());
			Integer sequence2 = sequenceDao.sequenciaNueva(prefijo.getPrefijo(), prefijo.getIdSede().getId(),
					prefijo.getId());
			Sequence sequen = new Sequence();
			sequen.setIdPrefijo(prefijo.getId());
			sequen.setIdSede(prefijo.getIdSede().getId());
			sequen.setValorSequencia(sequence2);
			Integer valorSecuencia2 = sequen.getValorSequencia();

			Integer valorSecuencia;
			if (sequence == null) {
				Sequence nuevaSecuencia = new Sequence();
				nuevaSecuencia.setIdSede(factura.getIdSede().getId());
				nuevaSecuencia.setIdPrefijo(prefijo.getId());
				nuevaSecuencia.setValorSequencia(prefijo.getValorInicial() + 1);
				sequence = nuevaSecuencia;
				valorSecuencia = nuevaSecuencia.getValorSequencia();
			} else {
				sequence.setValorSequencia(sequence.getValorSequencia() + 1);
				valorSecuencia = sequence.getValorSequencia();
			}
			nuevaRemision.setNumeroRemision(prefijo.getPrefijo() + sequence2);
			factura.setRemision(prefijo.getPrefijo() + sequence2);
			remisionDao.save(nuevaRemision);
			sequenceDao.save(sequen);
		}
	}

	// Obtener las facturas segun cliente
	@PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('SUPER')")
	public ResponseEntity<Object> obtenerConceptoFactura(@RequestParam Integer idUsuario) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		try {

			ArrayList<Factura> factura = this.facturaDao.obtenerConceptos(idUsuario);

			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de facturas exitosa");
			respuestaDto.setObjetoRespuesta(factura);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo facturas " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error obteniendo facturas");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	public ResponseEntity<Object> obtenerFacturasParaInformeVendedores(String fechaInicio, String fechaFin, Boolean sort,
			String tipo, String estado, Integer numeroFactura, Integer vendedor, Integer page) {
		ResponseEntity<Object> respuesta;
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		Pageable paging = PageRequest.of(page, 8);
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao.buscarPorIdUsuario(usuarioAutenticado.getId());
		try {
			List<Factura> facturas;
			StringBuilder query = new StringBuilder();
			StringBuilder queryCantidad = new StringBuilder();
			generarQuery(query, usuarioInformacion.getIdOrganizacion().getId(), fechaInicio, fechaFin, estado, numeroFactura,
					vendedor, sort, tipo);
			TypedQuery<Factura> vendedoresInfoQuery = (TypedQuery<Factura>) entityManager.createNativeQuery(query.toString(),
					Factura.class);
			int pageNumber = paging.getPageNumber();
			int pageSize = paging.getPageSize();
			vendedoresInfoQuery.setFirstResult(pageNumber * pageSize);
			vendedoresInfoQuery.setMaxResults(pageSize);
			facturas = vendedoresInfoQuery.getResultList();
			for (Factura factura : facturas) {
				List<FacturaArticulos> articulos = facturaArticuloDao.findAllBySede(factura.getId());
				factura.setFacArticulos(articulos);
			}
			generarQueryCantidadDatos(queryCantidad, usuarioInformacion.getIdOrganizacion().getId(), fechaInicio, fechaFin,
					estado, numeroFactura, vendedor, sort, tipo);
			Query cantidad = entityManager.createNativeQuery(queryCantidad.toString());
			BigInteger countResult = (BigInteger) cantidad.getSingleResult();
			Integer cantidadTotal = countResult.intValue();
			Page<Factura> result = new PageImpl<Factura>(facturas, paging, cantidadTotal);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de facturas exitosa");
			respuestaDto.setObjetoRespuesta(result);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo facturas " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error obteniendo facturas");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	private Integer sumaFactCliente(int idSede, int idCliente) {
		Integer suma = 0;
		Double sumaTotal = (double) 0;

		try {
			ArrayList<Factura> facturas = facturaDao.findBySedeClienteTotal(idSede, idCliente);
			for (Factura factura : facturas) {

				sumaTotal = sumaTotal + factura.getSubTotalVenta() + factura.getIva();
				suma = suma + factura.getTotal();

			}
		} catch (Exception e) {
		}

		return suma;
	}

	private Integer sumaFactDia(int idSede, String fecha) {
		Integer suma = 0;
		Double sumaTotal = 0.0000;

		try {
			ArrayList<Factura> facturas = facturaDao.findByFechaSedeTotal(idSede, fecha);
			for (Factura factura : facturas) {

				sumaTotal = sumaTotal + factura.getSubTotalVenta() + factura.getIva();
				sumaTotal = Math.round(sumaTotal * 10000) / 10000d;

				suma = suma + factura.getTotal();
			}
		} catch (Exception e) {
		}

		return suma;
	}

	// se hace join con conceptos_recibo_caja
	private Integer sumaFactCredito(int idSede) {
		Integer suma = 0;

		try {
			suma = facturaDao.findByCreditoTotal(idSede);
		} catch (Exception e) {
		}

		return suma;
	}

	private Integer sumaFactPago(Integer idSede) {
		Integer suma = 0;

		try {
			suma = facturaDao.findByPagoTotal(idSede);

		} catch (Exception e) {
		}

		return suma;
	}

	private Integer sumaFactFechas(int idSede, String fechaInicial, String fechaFinal) {
		Integer suma = 0;

		try {
			ArrayList<Factura> facturas = facturaDao.findByFechasTotal(idSede, fechaInicial, fechaFinal);
			for (Factura factura : facturas) {

				suma = suma + factura.getTotal();
			}
		} catch (Exception e) {
		}

		return suma;
	}

	private Integer sumaFactNum(int idSede, int num) {
		Integer suma = 0;

		try {
			suma = facturaDao.findByNumFact(idSede, num);

			// suma = suma + facturas.getTotal();

		} catch (Exception e) {
		}

		return suma;
	}

	private Integer sumaFactClienteEstadoTotal(int idSede, int idCliente) {
		Integer suma = 0;

		try {
			ArrayList<Factura> facturas = facturaDao.findByClienteEstadoCreditoTotal(idSede, idCliente);
			for (Factura factura : facturas) {

				suma = suma + factura.getTotal();
			}

		} catch (Exception e) {
		}

		return suma;
	}

	private Integer sumaFactClienteEstadoContadoTotal(int idSede, int idCliente) {
		Integer suma = 0;

		try {
			ArrayList<Factura> facturas = facturaDao.findByClienteEstadoContadoTotal(idSede, idCliente);
			for (Factura factura : facturas) {

				suma = suma + factura.getTotal();
			}

		} catch (Exception e) {
		}

		return suma;
	}

	private Integer sumaFactClienteFechaInicial(int idSede, int idCliente, String fechaInicial) {
		Integer suma = 0;

		try {
			ArrayList<Factura> facturas = facturaDao.findByClienteFechaInicialTotal(idSede, idCliente, fechaInicial);
			for (Factura factura : facturas) {

				suma = suma + factura.getTotal();
			}

		} catch (Exception e) {
		}

		return suma;
	}

	private Integer sumaFactClienteFechasTotal(int idSede, int idCliente, String fechaInicial, String fechaFinal) {
		Integer suma = 0;

		try {
			ArrayList<Factura> facturas = facturaDao.findByClienteFechasTotal(idSede, idCliente, fechaInicial,
					fechaFinal);
			for (Factura factura : facturas) {

				suma = suma + factura.getTotal();
			}

		} catch (Exception e) {
		}

		return suma;
	}

	private Integer sumaFactFechaCredito(int idSede, String fechaInicial) {
		Integer suma = 0;

		try {
			ArrayList<Factura> facturas = facturaDao.findByFechaEstadoCreditoTotal(idSede, fechaInicial);
			for (Factura factura : facturas) {

				suma = suma + factura.getTotal();
			}

		} catch (Exception e) {
		}

		return suma;
	}

	private Integer sumaFactFechaContado(int idSede, String fechaInicial) {
		Integer suma = 0;

		try {
			ArrayList<Factura> facturas = facturaDao.findByFechaEstadoContadoTotal(idSede, fechaInicial);
			for (Factura factura : facturas) {

				suma = suma + factura.getTotal();
			}

		} catch (Exception e) {
		}

		return suma;
	}

	private Integer sumaFactFechasEstadoCredito(int idSede, String fechaInicial, String fechaFinal) {
		Integer suma = 0;

		try {
			ArrayList<Factura> facturas = facturaDao.findByFechasEstadoCreditoTotal(idSede, fechaInicial, fechaFinal);
			for (Factura factura : facturas) {

				suma = suma + factura.getTotal();
			}

		} catch (Exception e) {
		}

		return suma;
	}

	private Integer sumaFactFechasEstadoContado(int idSede, String fechaInicial, String fechaFinal) {
		Integer suma = 0;

		try {
			ArrayList<Factura> facturas = facturaDao.findByFechasEstadoContadoTotal(idSede, fechaInicial, fechaFinal);
			for (Factura factura : facturas) {

				suma = suma + factura.getTotal();
			}

		} catch (Exception e) {
		}

		return suma;
	}

	public ResponseEntity<Object> obtenerTotalDeFacturasParaInformeVendedores(String fechaInicio, String fechaFin,
			String estado, Integer numeroFactura, Integer vendedor) {
		ResponseEntity<Object> respuesta;
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao.buscarPorIdUsuario(usuarioAutenticado.getId());
		try {
			Integer total;
			total = busquedaConsultaTotal(usuarioInformacion.getIdOrganizacion().getId(), fechaInicio, fechaFin, estado,
					numeroFactura, vendedor);

			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de facturas exitosa");
			respuestaDto.setObjetoRespuesta(total);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo facturas " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error obteniendo facturas");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	private Integer busquedaConsultaTotal(Integer idSede, String fechaInicio, String fechaFin, String estado,
			Integer numeroFactura, Integer vendedor) {
		StringBuilder query = new StringBuilder();
		Integer total = 0;
		generarTotalQuery(query, idSede, fechaInicio, fechaFin, estado, numeroFactura, vendedor);

		Query cantidad = entityManager.createNativeQuery(query.toString());

		BigDecimal totalResult = (BigDecimal) cantidad.getSingleResult();

		if (totalResult != null) {
			total = totalResult.intValue();
		}

		return total;
	}

	private void generarQuery(StringBuilder query, Integer idSede, String fechaInicio, String fechaFin, String estado,
			Integer numeroFactura, Integer vendedor, Boolean sort, String tipo) {
		if (tipo.equals("cantidad")) {
			query.append(
					// "select *,sum(a.n_cantidad) as cantidad from fac_facturas f join
					// informacion_usuario v on f.nid_vendedor = v.id"
					// + " join fac_articulos a on f.nid_factura = a.nid_factura join
					// conceptos_recibo_caja crc on crc.id = f.id_concepto where f.id_sede=");
					"select *,sum(a.n_cantidad) as cantidad from fac_facturas f join informacion_usuario v on f.nid_vendedor = v.id"
							+ " join fac_articulos a on f.nid_factura = a.nid_factura where f.id_sede=");
		} else {
			// query.append("select * from fac_facturas f join informacion_usuario v on
			// f.nid_vendedor = v.id"
			// + " join conceptos_recibo_caja crc on crc.id = f.id_concepto where
			// f.id_sede=");
			query.append("select * from fac_facturas f join informacion_usuario v on f.nid_vendedor = v.id"
					+ " where f.id_sede=");
		}
		query.append("" + idSede);
		if (!fechaInicio.equals("null") && fechaFin.equals("null")) {
			query.append(" and date_format(date(d_fecha_venta),'%Y-%m-%d')" + "= date('" + fechaInicio + "')");
		} else if (fechaInicio.equals("null") && !fechaFin.equals("null")) {
			query.append(" and date_format(date(d_fecha_venta),'%Y-%m-%d')" + "<= date('" + fechaFin + "')");
		} else if (!fechaInicio.equals("null") && !fechaFin.equals("null")) {
			query.append(" and date_format(date(d_fecha_venta),'%Y-%m-%d')" + "between " + "date('" + fechaInicio
					+ "') and " + "date('" + fechaFin + "')");
		}
		if (numeroFactura != 0) {
			query.append(" and n_nro_factura=" + numeroFactura);
		}
		if (vendedor != 0) {
			query.append(" and nid_vendedor=" + vendedor);
		}
		if (estado.equals("Pagado")) {
			query.append(" and crc.saldo_final = 0");
		} else if (estado.equals("Credito")) {
			query.append(" and crc.saldo_final > 0");
		}
		if (sort) {
			QueryOrdenar(query, tipo);
		}
		logger.info(query);
	}

	private void generarQueryCantidadDatos(StringBuilder query, Integer idSede, String fechaInicio, String fechaFin,
			String estado, Integer numeroFactura, Integer vendedor, Boolean sort, String tipo) {
		query.append(
				// "select count(*) from fac_facturas f join informacion_usuario v on
				// f.nid_vendedor = v.id join prefijo p on f.t_prefijo = p.id"
				// + " join conceptos_recibo_caja crc on crc.id = f.id_concepto where
				// f.id_sede=");
				"select count(*) from fac_facturas f join informacion_usuario v on f.nid_vendedor = v.id join prefijo p on f.t_prefijo = p.id"
						+ " where f.id_sede=");
		query.append("" + idSede);
		if (!fechaInicio.equals("null") && fechaFin.equals("null")) {
			query.append(" and date_format(date(d_fecha_venta),'%Y-%m-%d')" + "= date('" + fechaInicio + "')");
		} else if (fechaInicio.equals("null") && !fechaFin.equals("null")) {
			query.append(" and date_format(date(d_fecha_venta),'%Y-%m-%d')" + "<= date('" + fechaFin + "')");
		} else if (!fechaInicio.equals("null") && !fechaFin.equals("null")) {
			query.append(" and date_format(date(d_fecha_venta),'%Y-%m-%d')" + "between " + "date('" + fechaInicio
					+ "') and " + "date('" + fechaFin + "')");
		}
		if (numeroFactura != 0) {
			query.append(" and n_nro_factura=" + numeroFactura);
		}
		if (vendedor != 0) {
			query.append(" and nid_vendedor=" + vendedor);
		}
		if (estado.equals("Pagado")) {
			query.append(" and crc.saldo_final = 0");
		} else if (estado.equals("Credito")) {
			query.append(" and crc.saldo_final > 0");
		}
		logger.info(query);
	}

	private void QueryOrdenar(StringBuilder query, String tipo) {
		switch (tipo) {
			case "nit":
				query.append(" order by nid_vendedor desc");
				break;
			case "nombreVendedor":
				query.append(" order by v.nombre_completo desc");
				break;
			case "FechaVenta":
				query.append(" order by order by d_fecha_venta desc");
				break;
			case "prefijo":
				query.append(" order by p.prefijo desc");
				break;
			case "numeroFactura":
				query.append(" order by n_nro_factura desc");
				break;
			case "TipoVenta":
				query.append(" order nid_pyme_tipo_venta desc");
				break;
			case "documento":
				query.append(" order by c.nitocc desc");
				break;
			case "cliente":
				query.append(" order by c.nombres desc,c.apellidos desc");
				break;
			case "cantidad":
				query.append(" group by a.nid_factura order by cantidad desc");
				break;
			case "vence":
				query.append(" order by d_fecha_vencimiento_cr desc");
				break;
			case "total":
				query.append(" order by f.m_total desc");
				break;
			default:
				break;
		}
	}

	private void generarTotalQuery(StringBuilder query, Integer idSede, String fechaInicio, String fechaFin,
			String estado, Integer numeroFactura, Integer vendedor) {
		query.append("select sum(m_total) from fac_facturas f join informacion_usuario v on f.nid_vendedor = v.id"
				+ " join conceptos_recibo_caja crc on crc.id = f.id_concepto where f.id_sede=" + idSede);
		if (!fechaInicio.equals("null") && fechaFin.equals("null")) {
			query.append(" and date_format(date(d_fecha_venta),'%Y-%m-%d')" + "= date('" + fechaInicio + "')");
		} else if (fechaInicio.equals("null") && !fechaFin.equals("null")) {
			query.append(" and date_format(date(d_fecha_venta),'%Y-%m-%d')" + "<= date('" + fechaFin + "')");
		} else if (!fechaInicio.equals("null") && !fechaFin.equals("null")) {
			query.append(" and date_format(date(d_fecha_venta),'%Y-%m-%d')" + "between " + "date('" + fechaInicio
					+ "') and " + "date('" + fechaFin + "')");
		}
		if (numeroFactura != 0) {
			query.append(" and n_nro_factura=" + numeroFactura);
		}
		if (vendedor != 0) {
			query.append(" and nid_vendedor=" + vendedor);
		}
		if (estado.equals("Pagado")) {
			query.append(" and crc.saldo_final = 0");
		} else if (estado.equals("Credito")) {
			query.append(" and crc.saldo_final > 0");
		}
		logger.info(query);
	}

	// Anular Factura
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN')")
	public ResponseEntity<Object> anularFactura(@RequestBody Factura factura) throws Exception {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		try {
			Factura facturaAnular = this.facturaDao.findById(factura.getId()).get();
			if (facturaAnular != null) {
				logger.info("existe factura para anular");
			}
			if (facturaAnular.getId().equals(factura.getId())) {
				logger.info("validacion de facturass exittosa");
				facturaAnular.setAjustes(null);
				facturaAnular.setCajaId(null);
				// aqui va setCodEstadoCon "Anu"
				EstadoDocumento estado = estadoDao.findById(3).orElse(null);
				facturaAnular.setCodEstadoCon(estado);
				facturaAnular.setCodEstadoMon(null);
				facturaAnular.setDetalleCliente(null);
				facturaAnular.setFacArticulos(null);
				facturaAnular.setFechaVencimientoCr(null);
				facturaAnular.setFechaMod(new Date(new java.util.Date().getTime()));
				facturaAnular.setFechaVenta(null);
				facturaAnular.setIdCliente(null);
				facturaAnular.setIdConcepto(null);
				facturaAnular.setIdMoneda(null);
				facturaAnular.setIdPlazo(null);
				facturaAnular.setIva(null);
				facturaAnular.setNpuntos(null);
				facturaAnular.setObservaciones(null);
				facturaAnular.setOtrosImpuestos(null);
				facturaAnular.setPrefijo(null);
				facturaAnular.setRetenciones(null);
				facturaAnular.setSeparado(null);
				facturaAnular.setSubTotalVenta((double) 0);
				facturaAnular.setTotal(0);
				facturaAnular.setValoresAdicionales(null);
				facturaAnular.setIdTipoVenta(factura.getIdTipoVenta());
				Factura anulado = this.facturaDao.save(facturaAnular);
				// if (anulado != null) {
				// logger.info(anulado);
				// } else {
				// logger.info("no hay datos de la factura anular");
				// }
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Factura Anulada");
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
			}

		} catch (Exception e) {
			logger.error("Error obteniendo factura " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error obteniendo factura");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER')")
	public ResponseEntity<Object> obtenerNumeroFactura(Integer idSede, Integer idCliente) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		try {
			List<Factura> facturaC = this.facturaDao.findByIdSedeCliente(idSede, idCliente);
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

	public List<Factura> facturasPorCliente(Integer idCliente, Date fechaInicial, Date fechaFinal) {
		List<Factura> lista = facturaDao.busarPorClienteYFecha(idCliente, fechaInicial, fechaFinal);
		return lista;
	}

	public List<Factura> facturasPorCliente(Integer idCliente) {
		List<Factura> lista = facturaDao.busarPorCliente(idCliente);
		return lista;
	}

	// facturas vencidas por estados
	public List<Factura> listarmovimientosCliente(Integer idSede) {
		List<Factura> lista = facturaDao.listarlistarmovimientosClientePaginado(idSede);
		return lista;
	}

	public List<Factura> listarmovimientosClienteFiltros(Integer idSede, String fechaInicial, String fechaFinal,
			String numeroDocumento, Integer idCliente) {
		List<Factura> facturas;
		StringBuilder query = new StringBuilder();
		generarQuery(query, idSede, fechaInicial, fechaFinal, numeroDocumento, idCliente);
		TypedQuery<Factura> facturasInfoQuery = (TypedQuery<Factura>) entityManager.createNativeQuery(query.toString(),
				Factura.class);
		facturas = facturasInfoQuery.getResultList();
		return facturas;
	}

	private void generarQuery(StringBuilder query, Integer idSede, String fechaInicial, String fechaFinal,
			String numeroDocumento, Integer cliente) {

		query.append("select * FROM fac_facturas where id_sede=");

		query.append("" + idSede);
		if (!fechaInicial.equals("null") && fechaFinal.equals("null")) {
			query.append(" and date_format(date(d_fecha_venta),'%Y-%m-%d')" + "= date('" + fechaInicial + "')");
		} else if (fechaInicial.equals("null") && !fechaFinal.equals("null")) {
			query.append(" and date_format(date(d_fecha_venta),'%Y-%m-%d')" + "= date('" + fechaFinal + "')");
		} else if (!fechaInicial.equals("null") && !fechaFinal.equals("null")) {
			query.append(" and date_format(date(d_fecha_venta),'%Y-%m-%d')" + " between " + "date_format(date('"
					+ fechaInicial + "'),'%Y-%m-%d')" + " and " + "date_format(date('" + fechaFinal + "'),'%Y-%m-%d')");
		}

		if (!numeroDocumento.equals("null")) {
			query.append(" and n_nro_factura='" + numeroDocumento + "'");
		}

		if (cliente != 0) {
			query.append(" and nid_cliente=" + cliente);
		}

	}

	// public List<Factura> obtenerFacturasCreditoPendientesCliente(Integer
	// idCliente) {
	// return facturaDao.obtenerFacturasCredito(idCliente);
	// }

	public List<Factura> obtenerFacturasCreditoPendientesCliente(Integer idCliente) {
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());

		int idSede = usuarioInformacion.getIdOrganizacion().getId();
		return facturaDao.obtenerFacturasCredito2(idCliente, idSede);
	}

	public List<Factura> facturasPorClienteActualizar(Integer idCliente, Integer idRecibo) {
		List<Factura> lista = facturaDao.FacturasCreditoActualizar(idCliente, idRecibo);
		return lista;
	}

	// listar Clientes Especiales
	@PreAuthorize("hasAuthority('USER') or hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerClientesEspeciales() {
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		try {
			ArrayList<Factura> cliente = this.facturaDao.obtenerClientesEspeciales();
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de Clientes Especiales");
			respuestaDto.setObjetoRespuesta(cliente);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo usuarios " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error obteniendo Clientes Especiales");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return respuesta;
	}

	// listar Clientes Frecuentes
	@PreAuthorize("hasAuthority('USER') or hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerClientesFrecuentes() {
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		try {
			ArrayList<Factura> cliente = this.facturaDao.obtenerClientesFrecuentes();
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de Clientes Frecuentes");
			respuestaDto.setObjetoRespuesta(cliente);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo usuarios " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error obteniendo Clientes Frecuentes");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	// facturas vencidas por estados
	public List<Factura> listarinformeFacturasVencidasCliente(Integer idSede) {
		List<Factura> lista = facturaDao.listarinformeFacturasVencidasClientePaginado(idSede);
		return lista;
	}

	public List<Factura> listarinformeFacturasVencidasClienteFiltros(Integer idSede, Integer idCliente,
			String fechaInicial, String fechaFinal) {
		List<Factura> facturas;
		StringBuilder query = new StringBuilder();
		generarQueryInformeFacturasClienteVencida(query, idSede, fechaInicial, fechaFinal, idCliente);
		TypedQuery<Factura> facturasInfoQuery = (TypedQuery<Factura>) entityManager.createNativeQuery(query.toString(),
				Factura.class);
		facturas = facturasInfoQuery.getResultList();
		return facturas;
	}

	private void generarQueryInformeFacturasClienteVencida(StringBuilder query, Integer idSede, String fechaInicio,
			String fechaFin, Integer cliente) {

		query.append("select * FROM fac_facturas where id_sede=");

		query.append("" + idSede);
		if (!fechaInicio.equals("null") && fechaFin.equals("null")) {
			query.append(" and date_format(date(d_fecha_venta),'%Y-%m-%d')" + "= date('" + fechaInicio + "')");
		} else if (fechaInicio.equals("null") && !fechaFin.equals("null")) {
			query.append(" and date_format(date(d_fecha_venta),'%Y-%m-%d')" + "<= date('" + fechaFin + "')");
		} else if (!fechaInicio.equals("null") && !fechaFin.equals("null")) {
			query.append(" and date_format(date(d_fecha_venta),'%Y-%m-%d')" + "between " + "date('" + fechaInicio
					+ "') and " + "date('" + fechaFin + "')");
		}
		if (cliente != 0) {
			query.append(" and nid_cliente=" + cliente);
		}
		query.append(" and DATEDIFF(d_fecha_vencimiento_cr,CURDATE()) < 0 and cod_estado_con in (2,5)");
	}

	// facturas vencidas por estados
	public List<Factura> listarCarteraCliente(int idSede) {
		List<Factura> lista = facturaDao.listarCarteraCliente(idSede);
		return lista;
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER')")
	public ResponseEntity<Object> obtenerFacturasCliente(Integer idCliente) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		try {
			List<Factura> facturas = this.facturaDao.obtenerFacturasCliente(idCliente);
			for (Factura factura : facturas) {
				List<FacturaArticulos> articulos = facturaArticuloDao.findAllBySede(factura.getId());
				factura.setFacArticulos(articulos);
			}
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de factura de compra exitosa");
			respuestaDto.setObjetoRespuesta(facturas);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo factura de compra " + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error obteniendo factura de compra");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return respuesta;

	}

	public Integer abonoFacturas(Integer idSede, String fechaInicial, String fechaFinal, String numeroDocumento,
			Integer cliente) {
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
				"select IFNULL(sum(crc.valor_abono),0) from fac_facturas f join prefijo p on f.t_prefijo = p.id join conceptos_recibo_caja crc on \r\n"
						+ "crc.nro_documento = concat(p.prefijo,f.n_nro_factura) where f.id_sede=");

		query.append("" + idSede);
		if (!fechaInicial.equals("null") && fechaFinal.equals("null")) {
			query.append(" and date_format(date(f.d_fecha_venta),'%Y-%m-%d')" + "= date('" + fechaInicial + "')");
		} else if (fechaInicial.equals("null") && !fechaFinal.equals("null")) {
			query.append(" and date_format(date(f.d_fecha_venta),'%Y-%m-%d')" + "= date('" + fechaFinal + "')");
		} else if (!fechaInicial.equals("null") && !fechaFinal.equals("null")) {
			query.append(" and date_format(date(f.d_fecha_venta),'%Y-%m-%d')" + " between " + "date_format(date('"
					+ fechaInicial + "'),'%Y-%m-%d')" + " and " + "date_format(date('" + fechaFinal + "'),'%Y-%m-%d')");
		}
		if (!numeroDocumento.equals("null") && (!fechaInicial.equals("null") && !fechaFinal.equals("null"))) {
			query.append(" and n_nro_factura='" + numeroDocumento + "'");
		}

		if (!numeroDocumento.equals("null"))

		{
			query.append(" and f.n_nro_factura='" + numeroDocumento + "'");
		}

		if (cliente != 0
				&& (!fechaInicial.equals("null") && !fechaFinal.equals("null") || !numeroDocumento.equals("null"))) {
			query.append(" and nid_cliente=" + cliente);
		}

		if (cliente != 0) {
			query.append(" and f.nid_cliente=" + cliente);
		}

	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER')")
	public ResponseEntity<Object> obtenerFacturasPendientesCliente(Integer idCliente, Integer page) {
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Pageable pageConfig = PageRequest.of(page, 10);
		try {
			Page<Factura> facturas = this.facturaDao.obtenerFacturasClientePendientes(idCliente, pageConfig);
			for (Factura factura : facturas) {
				List<RetencionFactura> retenciones = retencionFacturaDao.obtenerRetencionesFactura(factura.getId());
				factura.setListRetenciones(retenciones);
			}
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de factura de compra exitosa");
			respuestaDto.setObjetoRespuesta(facturas);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo factura de compra " + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error obteniendo factura de compra");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN')")
	public ResponseEntity<Object> ActualizarRetencion(@RequestBody Factura factura) {
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		try {

			retencionFacturaDao.borrarRetencionesFactura(factura.getId());
			List<FacturaArticulos> articulos = facturaArticuloDao.findAllBySede(factura.getId());
			factura.setFacArticulos(articulos);
			facturaDao.buscarporIdFactura(factura.getId());
			facturaDao.save(factura);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Factura de venta actualizada exitosamente");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error al actualizar registro" + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al actualizar la factura" + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER')")
	public ResponseEntity<Object> obtenerSaldo(Factura f) {
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Integer total = 0;
		try {
			logger.info(f.getPrefijo().getPrefijo() + f.getNroFactura());
			Integer saldo = conceptosReciboCajaDao.obtenerSaldo(f.getPrefijo().getPrefijo() + f.getNroFactura());
			if (saldo != null) {
				logger.info(saldo);
			}
			Integer asignacion = asignacionReciboDao.obtenerTotal(f.getPrefijo().getPrefijo() + f.getNroFactura());
			if (asignacion != null) {
				logger.info(asignacion);
			}
			Integer descuentos = conceptosReciboCajaDao
					.obtenerTotalDescuentoConcepto(f.getPrefijo().getPrefijo() + f.getNroFactura());
			if (descuentos != null) {
				logger.info(descuentos);
			}

			if (f.getTotal() != null) {
				logger.info(f.getTotal());
			}

			if (f.getRetenciones() != null) {
				logger.info(f.getRetenciones());
				total = f.getTotal() - asignacion - saldo - descuentos - f.getRetenciones();
			} else {
				total = f.getTotal() - asignacion - saldo - descuentos;
			}
			// Integer total = f.getTotal() - asignacion - saldo - descuentos;

			if (total != null) {
				logger.info(total);
			}
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de factura de compra exitosa");
			respuestaDto.setObjetoRespuesta(total);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo factura de compra " + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error obteniendo factura de compra");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

}
