package com.softlond.base.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

import com.amazonaws.services.simpleworkflow.model.RequestCancelActivityTaskDecisionAttributes;
import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.ConceptoNotaCredito;
import com.softlond.base.entity.DevolucionArticulosVenta;
import com.softlond.base.entity.DevolucionCompras;
import com.softlond.base.entity.DevolucionVentasCliente;
import com.softlond.base.entity.EstadoDocumento;
import com.softlond.base.entity.Factura;
import com.softlond.base.entity.FacturaArticulos;
import com.softlond.base.entity.FacturaCompra;
import com.softlond.base.entity.FacturaImpuestos;
import com.softlond.base.entity.FacturaRetenciones;
import com.softlond.base.entity.InformacionUsuario;
import com.softlond.base.entity.Sequence;
import com.softlond.base.entity.Usuario;
import com.softlond.base.repository.ConceptoNotaCreditoClienteDao;
import com.softlond.base.repository.ConceptoNotaCreditoDao;
import com.softlond.base.repository.DevolucionVentasArticulosDao;
import com.softlond.base.repository.FacturaArticuloDao;
import com.softlond.base.repository.FacturaCompraDao;
import com.softlond.base.repository.FacturaDao;
import com.softlond.base.repository.FacturaDevolucionDao;
import com.softlond.base.repository.FacturasDevolucionDao;
import com.softlond.base.repository.NotaCreditoClienteDao;
import com.softlond.base.repository.SequenceDao;
import com.softlond.base.repository.UsuarioInformacionDao;
import com.softlond.base.request.DevolucionRequest;
import com.softlond.base.request.DevolucionesRequest;

@Service
public class DevolucionVentaService {

	private static final Logger logger = Logger.getLogger(DevolucionVentaService.class);

	@Autowired
	private FacturasDevolucionDao devolucionDao;

	@Autowired
	UsuarioInformacionDao usuarioInformacionDao;

	@Autowired
	FacturaDao facturaDao;

	@Autowired
	DevolucionVentasArticulosDao DevolucionVentasArticulosDao;

	@Autowired
	ConceptoNotaCreditoClienteDao conceptonotaCreditoDao;

	@Autowired
	NotaCreditoClienteDao notaCreditoDao;

	@Autowired
	private SequenceDao sequenceDao;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private DevolucionVentasArticulosDao articulosDevolucionDao;

	@Autowired
	private FacturaArticuloDao facturaArticuloDao;

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> crearFactura(@RequestBody DevolucionesRequest request) {
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		Sequence secuenciaSave = new Sequence();
		secuenciaSave.setValorSequencia(Integer.parseInt(request.getDevolucion().getNroDevolucion().split(" ")[1]));
		secuenciaSave.setIdPrefijo(request.getPrefijo().getId());
		secuenciaSave.setIdSede(request.getDevolucion().getIdSede().getId());
		try {
			Sequence seqActualizada = sequenceDao
					.findByIdSedeAndIdPrefijo(request.getDevolucion().getIdSede().getId(), request.getPrefijo().getId())
					.orElse(null);
			if (seqActualizada != null) {
				secuenciaSave.setId(seqActualizada.getId());
			}

			Integer numerofactura = request.getDevolucion().getFactura().getId();
			Factura facDevolucion = facturaDao.buscarporIdFactura(numerofactura);

			for (FacturaArticulos fac : facDevolucion.getFacArticulos()) {

				for (FacturaArticulos facDevo : request.getDevolucion().getFactura().getFacArticulos()) {
					if (facDevo.getId().equals(fac.getId())) {

						// fac.setCantidad(facDevo.getCantidad());
					}
				}

			}
			// facDevolucion.setFacArticulos(request.getDevolucion().getFactura().getFacArticulos());
			facturaDao.save(facDevolucion);
			request.getDevolucion().setUsuarioMod(usuarioInformacion);
			request.getDevolucion().setIdSede(usuarioInformacion.getIdOrganizacion());
			devolucionDao.save(request.getDevolucion());
			sequenceDao.save(secuenciaSave);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Factura de compra creada exitosamente");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error al guardar la factura" + e + " linea " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al guardar la factura" + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerNumero() {
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		try {
			String numero = devolucionDao.Consecutivo();
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Factura de compra creada exitosamente");
			respuestaDto.setObjetoRespuesta(numero);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error al obtener numero" + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al obtener numero" + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerDevolucion(Integer idSede) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		try {
			List<DevolucionVentasCliente> devolucionC = this.devolucionDao.findByIdSede(idSede);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de devoluciones exitosa");
			respuestaDto.setObjetoRespuesta(devolucionC);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo factura de compra " + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error obteniendo devolucion");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return respuesta;

	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerDevolucionSedeCliente(Integer idSede, Integer idCliente) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		try {
			List<DevolucionVentasCliente> devolucionC = this.devolucionDao.findByIdSedeCliente(idSede, idCliente);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de devoluciones exitosa");
			respuestaDto.setObjetoRespuesta(devolucionC);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo factura de compra " + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error obteniendo devolucion");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return respuesta;

	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerListadoDevoluciones(Integer idSede, Integer idCliente) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		try {
			List<DevolucionVentasCliente> devolucionC = this.devolucionDao.findByIdSedeCliente(idSede, idCliente);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de devoluciones exitosa");
			respuestaDto.setObjetoRespuesta(devolucionC);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo factura de compra " + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error obteniendo devolucion");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return respuesta;

	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerDevolucionFactura(Integer id) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		logger.info(id);

		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		try {
			int i = 0;
			List<DevolucionVentasCliente> devolucionC = this.devolucionDao.findByIdFactura(id,
					usuarioInformacion.getIdOrganizacion().getId());
			for (DevolucionVentasCliente devCl : devolucionC) {
				List<DevolucionArticulosVenta> devArti = new ArrayList<DevolucionArticulosVenta>();
				devArti = DevolucionVentasArticulosDao.findBynDevolucion(devCl.getId());
				devolucionC.get(i).setDevolucionArticulos(devArti);
				i++;
			}

			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de devoluciones exitosa");
			respuestaDto.setObjetoRespuesta(devolucionC);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo factura de compra " + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error obteniendo devolucion");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return respuesta;

	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerListadoDevolucionesFiltros(String numeroDocumento, String fechaInicial,
			String fechaFinal, Integer estado, Integer page) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Pageable paging = PageRequest.of(page, 10);
		try {
			List<DevolucionVentasCliente> devolucionC;
			StringBuilder query = new StringBuilder();
			StringBuilder queryCantidad = new StringBuilder();
			logger.info("numeroDoucmento" + numeroDocumento);
			logger.info("fecha inicial" + fechaInicial);
			logger.info("fecha final" + fechaFinal);
			logger.info("estado" + estado);
			generarQuery(query, numeroDocumento, fechaInicial, fechaFinal, estado);
			logger.warn(query);
			TypedQuery<DevolucionVentasCliente> devolucionesInfoQuery = (TypedQuery<DevolucionVentasCliente>) entityManager
					.createNativeQuery(query.toString(), DevolucionVentasCliente.class);
			int pageNumber = paging.getPageNumber();
			int pageSize = paging.getPageSize();
			devolucionesInfoQuery.setFirstResult(pageNumber * pageSize);
			devolucionesInfoQuery.setMaxResults(pageSize);
			devolucionC = devolucionesInfoQuery.getResultList();
			for (DevolucionVentasCliente devolucion : devolucionC) {
				List<DevolucionArticulosVenta> articulos = articulosDevolucionDao.findBynDevolucion(devolucion.getId());
				devolucion.setDevolucionArticulos(articulos);
				List<FacturaArticulos> articulosFactura = facturaArticuloDao.findAllBySede(devolucion.getFactura().getId());
				devolucion.getFactura().setFacArticulos(articulosFactura);
			}
			generarQueryCantidad(queryCantidad, numeroDocumento, fechaInicial, fechaFinal, estado);
			Query cantidad = entityManager.createNativeQuery(queryCantidad.toString());
			BigInteger countResult = (BigInteger) cantidad.getSingleResult();
			Integer cantidadTotal = countResult.intValue();
			Page<DevolucionVentasCliente> result = new PageImpl<DevolucionVentasCliente>(devolucionC, paging,
					cantidadTotal);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de devoluciones exitosa");
			respuestaDto.setObjetoRespuesta(result);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo devoluciones " + e + "linea " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error obteniendo devolucion");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return respuesta;

	}

	private void generarQuery(StringBuilder query, String numeroDocumento, String fechaInicio, String fechaFin,
			Integer estado) {

		query.append("select * FROM dev_ventas_cliente ");

		if (!fechaInicio.equals("null") && fechaFin.equals("null")) {
			query.append("where date_format(date(d_fecha_factura),'%Y-%m-%d')" + ">= date('" + fechaInicio + "')");
		} else if (fechaInicio.equals("null") && !fechaFin.equals("null")) {
			query.append("where date_format(date(d_fecha_factura),'%Y-%m-%d')" + "<= date('" + fechaFin + "')");
		} else if (!fechaInicio.equals("null") && !fechaFin.equals("null")) {
			query.append("where date_format(date(d_fecha_factura),'%Y-%m-%d')" + "between " + "date('" + fechaInicio
					+ "') and " + "date('" + fechaFin + "')");
		}
		if (!numeroDocumento.equals("null") && (!fechaInicio.equals("null") || !fechaFin.equals("null"))) {
			query.append(" and n_nro_documento='" + numeroDocumento + "'");
		} else if (!numeroDocumento.equals("null")) {
			query.append("where n_nro_documento='" + numeroDocumento + "'");
		}

		if (estado != 0
				&& (!fechaInicio.equals("null") || !fechaFin.equals("null") || !numeroDocumento.equals("null"))) {
			query.append(" and cod_estado_con = " + estado);
		} else if (estado != 0) {
			query.append("where cod_estado_con = " + estado);
		}
	}

	private void generarQueryCantidad(StringBuilder query, String numeroDocumento, String fechaInicio, String fechaFin,
			Integer estado) {

		query.append("select count(*) FROM dev_ventas_cliente ");

		if (!fechaInicio.equals("null") && fechaFin.equals("null")) {
			query.append("where date_format(date(d_fecha_factura),'%Y-%m-%d')" + "= date('" + fechaInicio + "')");
		} else if (fechaInicio.equals("null") && !fechaFin.equals("null")) {
			query.append("where date_format(date(d_fecha_factura),'%Y-%m-%d')" + "<= date('" + fechaFin + "')");
		} else if (!fechaInicio.equals("null") && !fechaFin.equals("null")) {
			query.append("where date_format(date(d_fecha_factura),'%Y-%m-%d')" + "between " + "date('" + fechaInicio
					+ "') and " + "date('" + fechaFin + "')");
		}
		if (!numeroDocumento.equals("null") && (!fechaInicio.equals("null") || !fechaFin.equals("null"))) {
			query.append(" and n_nro_documento='" + numeroDocumento + "'");
		} else if (!numeroDocumento.equals("null")) {
			query.append("where n_nro_documento='" + numeroDocumento + "'");
		}

		if (estado != 0
				&& (!fechaInicio.equals("null") || !fechaFin.equals("null") || !numeroDocumento.equals("null"))) {
			query.append(" and cod_estado_con = " + estado);
		} else if (estado != 0) {
			query.append("where cod_estado_con = " + estado);
		}
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> eliminarDevolucion(Integer idDEvolucion) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		try {
			articulosDevolucionDao.eliminarArticulosDevolucionIdDevolucion(idDEvolucion);

			devolucionDao.eliminarDevolucion(idDEvolucion);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de devoluciones exitosa");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo factura de compra " + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error obteniendo devolucion");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return respuesta;

	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> actualizarFactura(@RequestBody DevolucionesRequest request) {
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		try {
			articulosDevolucionDao.eliminarArticulosDevolucionIdDevolucion(request.getDevolucion().getId());
			request.getDevolucion().setUsuarioMod(usuarioInformacion);
			request.getDevolucion().setIdSede(usuarioInformacion.getIdOrganizacion());
			devolucionDao.save(request.getDevolucion());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Factura de compra creada exitosamente");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error al guardar la factura" + e + " linea " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al guardar la factura" + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> devolucionesMes(Integer page) {
		ResponseEntity<Object> respuesta;
		try {
			Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
			Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
			InformacionUsuario usuarioInformacion = this.usuarioInformacionDao.buscarPorIdUsuario(usuarioAutenticado.getId());
			Pageable pageConfig = PageRequest.of(page, 10);
			int idSede = usuarioInformacion.getIdOrganizacion().getId();

			Page<DevolucionVentasCliente> devolucionesMes = devolucionDao.obtenerDevolucionesDelMes(idSede, pageConfig);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de devoluciones exitosa",
					devolucionesMes.getContent(), 0, devolucionesMes.getTotalElements() + "");

			respuestaDto.setObjetoRespuesta(devolucionesMes);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);

		} catch (RuntimeException e) {
			logger.error("Error al obtener las devoluciones" + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al obtener las facturas vencidas " + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> estadoNota(String request) {
		ResponseEntity<Object> respuesta;
		try {
			logger.info("entro aqui");
			Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
			Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
			InformacionUsuario usuarioInformacion = this.usuarioInformacionDao.buscarPorIdUsuario(usuarioAutenticado.getId());
			int idSede = usuarioInformacion.getIdOrganizacion().getId();

			logger.info(request);

			Integer idNotaCredito = conceptonotaCreditoDao.idNotaCredito(request);
			logger.info(idNotaCredito);

			String estadoDocumento = notaCreditoDao.estadoDocumento(idNotaCredito, idSede);
			logger.info(estadoDocumento);

			if (estadoDocumento.equals("Asignado")) {
				DevolucionVentasCliente devolucion = devolucionDao.findBynumeroDocumento(idSede, request);
				devolucion.setRetencion(1.0);
				devolucionDao.save(devolucion);
			}

			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de devoluciones exitosa");

			respuestaDto.setObjetoRespuesta(null);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);

		} catch (RuntimeException e) {
			logger.error("Error al obtener las devoluciones" + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al obtener las facturas vencidas " + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

}
