package com.softlond.base.service;

import java.io.Console;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

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
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.Articulo;
import com.softlond.base.entity.Clientes;
import com.softlond.base.entity.EstadoArticulo;
import com.softlond.base.entity.Factura;
import com.softlond.base.entity.FacturaArticulos;
import com.softlond.base.entity.InformacionUsuario;
import com.softlond.base.entity.NotaDebito;
import com.softlond.base.entity.Prefijo;
import com.softlond.base.entity.RemisionCompra;
import com.softlond.base.entity.RemisionVenta;
import com.softlond.base.entity.Sequence;
import com.softlond.base.entity.Usuario;
import com.softlond.base.entity.Vendedor;
import com.softlond.base.repository.ArticuloDao;
import com.softlond.base.repository.FacturaArticuloDao;
import com.softlond.base.repository.FacturaDao;
import com.softlond.base.repository.PrefijoDao;
import com.softlond.base.repository.RemisionCompraDao;
import com.softlond.base.repository.RemisionVentaDao;
import com.softlond.base.repository.SequenceDao;
import com.softlond.base.repository.UsuarioInformacionDao;

@Service
public class RemisionVentaService {
	private static final Logger logger = Logger.getLogger(RemisionVentaService.class);

	@Autowired
	private RemisionVentaDao remisionVentaDao;

	public RemisionVentaDao getRemisionVentaDao() {
		return remisionVentaDao;
	}

	public void setRemisionVentaDao(RemisionVentaDao remisionVentaDao) {
		this.remisionVentaDao = remisionVentaDao;
	}

	@Autowired
	private FacturaDao facturaDao;

	@Autowired
	private FacturaArticuloDao facturaArticuloDao;

	@Autowired
	UsuarioInformacionDao usuarioInformacionDao;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private SequenceDao sequenceDao;

	@Autowired
	private PrefijoDao prefijoDao;

	@Autowired
	private ArticuloDao articulo;

	// Crear Remision venta ,Number[] listaIdsArticulos,Number[] listaCantArticulos
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> crearRemision(@RequestBody RemisionVenta remision) {
		boolean disponibilidadArticulos = true;
		int i = 0;
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		try {
			/*
			 * Prefijo prefijo =
			 * prefijoDao.obtenerPrefijoRemisionSede(remision.getSede().getId());
			 * String nombrePrefijo = prefijo.getPrefijo();
			 * Integer numeroRecibo =
			 * Integer.parseInt(extraerNumeros(remision.getNumeroRemision(),
			 * nombrePrefijo));
			 * String remisionString = (nombrePrefijo + (numeroRecibo));
			 * RemisionVenta rem = new RemisionVenta();
			 */

			RemisionVenta guardado = this.remisionVentaDao.save(remision);

			for (int j = 0; j < guardado.getFacArticulos().size(); j++) {
				Float cantidadDisponibleModificada = articulo
						.obtenerCantDispArticuloFac(guardado.getFacArticulos().get(j).getArticulo().getId().intValue())
						- guardado.getFacArticulos().get(j).getCantidad().floatValue();
				articulo.cambiarCantidadDisponibleFac(cantidadDisponibleModificada,
						guardado.getFacArticulos().get(j).getArticulo().getId().intValue());
				if (cantidadDisponibleModificada <= 0) {
					logger.info("ingresa a cambiar estado del articulo en la remision de venta");
					logger.info(guardado.getFacArticulos().get(j).getArticulo().getId().intValue());
					// EstadoArticulo estadoA =
					// guardado.getFacArticulos().get(j).getArticulo().getEstadoArticulo();
					// estadoA.setId(4);
					// logger.info(estadoA.getId());
					articulo.cambiarEstadoArticuloFinalizado(4,
							guardado.getFacArticulos().get(j).getArticulo().getId().intValue());

					logger.info(guardado.getFacArticulos().get(j).getArticulo().getEstadoArticulo());
					logger.info(guardado.getFacArticulos().get(j).getArticulo());
				} else {
					logger.info("no ingresa a cantidadDisponibleModificada");
				}
			}

			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito creando remision de venta");
			respuestaDto.setObjetoRespuesta(guardado);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error en la creación de la remision de venta" + e + " linea "
					+ e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error en la creación de la remisión " + e + " linea " + e.getStackTrace()[0].getLineNumber());
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return respuesta;
	}

	// Actualizar remision venta
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> actualizarRemisionVenta(@RequestBody RemisionVenta remision) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		RemisionVenta remisionEnBd = this.remisionVentaDao.findById(remision.getId()).get();
		for (int j = 0; j < remisionEnBd.getFacArticulos().size(); j++) {
			Float cantidadDisponibleModificada = articulo
					.obtenerCantDispArticuloFac(remisionEnBd.getFacArticulos().get(j).getArticulo().getId().intValue())
					+ remisionEnBd.getFacArticulos().get(j).getCantidad().floatValue();
			articulo.cambiarCantidadDisponibleFac(cantidadDisponibleModificada,
					remisionEnBd.getFacArticulos().get(j).getArticulo().getId().intValue());
		}
		try {
			logger.info("ingreso a actualizar remision de venta sin problema");
			remision.setId(remisionEnBd.getId());
			logger.info(remision.getFacArticulos().size());
			RemisionVenta guardado = this.remisionVentaDao.save(remision);
			for (int j = 0; j < guardado.getFacArticulos().size(); j++) {
				Float cantidadDisponibleModificada = articulo
						.obtenerCantDispArticuloFac(guardado.getFacArticulos().get(j).getArticulo().getId().intValue())
						- guardado.getFacArticulos().get(j).getCantidad().floatValue();
				articulo.cambiarCantidadDisponibleFac(cantidadDisponibleModificada,
						guardado.getFacArticulos().get(j).getArticulo().getId().intValue());
			}
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito al actualizar la Remisión de venta");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);

		} catch (Exception e) {
			logger.error("Error en la actualización de la Remisión de venta");
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error en la actualización de la remisión de venta");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return respuesta;
	}

	// Ajustar remision venta
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> ajustarRemisionVenta(@RequestBody FacturaArticulos[] articulos) {
		logger.info("ingresa ajustar articulos");
		for (FacturaArticulos facturaArticulos : articulos) {
			Float cantidadDisponibleModificada = articulo.obtenerCantDispArticuloFac(
					facturaArticulos.getArticulo().getId().intValue()) + facturaArticulos.getCantidad().floatValue();
			articulo.cambiarCantidadDisponibleFac(cantidadDisponibleModificada,
					facturaArticulos.getArticulo().getId().intValue());
		}

		// logger.info("lista de articulos "+listaIdsArticulos);
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		try {
			/*
			 * for (int j = 0; j < listaIdsArticulos.length; j++) {
			 * Float cantidadDisponibleModificada =
			 * articulo.obtenerCantDispArticuloFac(listaIdsArticulos[j].getArticulo().getId(
			 * ).intValue())+ listaIdsArticulos[j].getCantidad().floatValue();
			 * articulo.cambiarCantidadDisponibleFac(cantidadDisponibleModificada,
			 * listaIdsArticulos[j].getArticulo().getId().intValue());
			 * }
			 */
			logger.info("llega hasta despues de recorrer articulos");
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito al actualizar la Remisión de venta");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);

		} catch (Exception e) {
			logger.error("Error en la actualización de la Remisión de venta");
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error en la actualización de la remisión de venta");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return respuesta;
	}

	// Listar Remision venta
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> listarRemisioVenta(Integer page, String ordenarPor) {
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		// Integer suma = 0;
		try {
			Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
			Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
			InformacionUsuario usuarioInformacion = this.usuarioInformacionDao.buscarPorIdUsuario(usuarioAutenticado.getId());

			Pageable pageConfig = PageRequest.of(page, 30);
			int idSede = usuarioInformacion.getIdOrganizacion().getId();
			Page<RemisionVenta> remisiones;
			Integer total = 0;
			if (!ordenarPor.equals("")) {
				pageConfig.getSort().and(Sort.by(Sort.Direction.DESC, ordenarPor));
				remisiones = remisionVentaDao.findAllBySedeOrder(idSede, pageConfig);
				for (RemisionVenta remisionVenta : remisiones) {
					logger.info("RemisionVentaService => ResponseEntity<Object>listarRemisioVenta => remisiones: "
							+ remisionVenta.getFecha());
				}
				total = remisionVentaDao.findAllBySedeOrderTotal(idSede);
			} else {
				remisiones = remisionVentaDao.findAllBySede(idSede, pageConfig);
				for (RemisionVenta remisionVenta : remisiones) {
					logger.info("RemisionVentaService => ResponseEntity<Object>listarRemisioVenta => remisiones: "
							+ remisionVenta.getFecha() + " " + remisionVenta.getNumeroRemision());
				}
				total = remisionVentaDao.findAllBySedeTotal(idSede);
			}
			for (RemisionVenta remision : remisiones) {
				List<FacturaArticulos> articulos = facturaArticuloDao.findAllBySedeRemision(remision.getId());
				remision.setFacArticulos(articulos);
				// suma += remision.getTotal();
			}

			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de remisiones exitosa",
					remisiones.getContent(), total, remisiones.getTotalElements() + "");

			respuestaDto.setObjetoRespuesta(remisiones);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error al obtener las remisiones" + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al obtener las remisiones " + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	// Consulta avanzada de clientes
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerRemisionVentaConsulta(String fechaInicial, String fechaFinal,
			String numeroRemision, Integer estado, Integer cliente, Integer vendedor, Integer page) {
		ResponseEntity<Object> respuesta;
		Pageable paging = PageRequest.of(page, 30);

		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao.buscarPorIdUsuario(usuarioAutenticado.getId());
		Integer idSede = usuarioInformacion.getIdOrganizacion().getId();
		try {
			List<RemisionVenta> remisiones;
			StringBuilder query = new StringBuilder();
			StringBuilder queryT = new StringBuilder();
			StringBuilder queryCantidad = new StringBuilder();
			generarQuery(query, fechaInicial, fechaFinal, numeroRemision, estado, cliente, vendedor, idSede);

			generarQueryT(queryT, fechaInicial, fechaFinal, numeroRemision, estado, cliente, vendedor, idSede);
			Query cantidadT = entityManager.createNativeQuery(queryT.toString());
			BigDecimal totalResult = (BigDecimal) cantidadT.getSingleResult();
			Integer total = totalResult.intValue();

			TypedQuery<RemisionVenta> remisionesInfoQuery = (TypedQuery<RemisionVenta>) entityManager
					.createNativeQuery(query.toString(), RemisionVenta.class);
			int pageNumber = paging.getPageNumber();
			int pageSize = paging.getPageSize();
			logger.info(pageSize);
			remisionesInfoQuery.setFirstResult(pageNumber * pageSize);
			remisionesInfoQuery.setMaxResults(pageSize);
			remisiones = remisionesInfoQuery.getResultList();
			// Integer suma = 0;

			for (RemisionVenta remision : remisiones) {
				List<FacturaArticulos> articulos = facturaArticuloDao.findAllBySedeRemision(remision.getId());
				remision.setFacArticulos(articulos);
				// suma = suma+remision.getTotal();
			}
			generarQueryCantidad(queryCantidad, fechaInicial, fechaFinal, numeroRemision, estado, cliente, vendedor, idSede);
			Query cantidad = entityManager.createNativeQuery(queryCantidad.toString());
			BigInteger countResult = (BigInteger) cantidad.getSingleResult();
			Integer cantidadTotal = countResult.intValue();
			logger.info(paging.getPageSize());
			Page<RemisionVenta> result = new PageImpl<RemisionVenta>(remisiones, paging, cantidadTotal);
			respuesta = ResponseEntity.ok(HttpStatus.OK);

			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de remisiones de ventas exitosa");
			respuestaDto.setObjetoRespuesta(result);
			respuestaDto.setSuma(total);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo Remisiones de venta " + e + " Linea error: "
					+ e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error obteniendo Remisiones de venta");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	private void generarQuery(StringBuilder query, String fechaInicial, String fechaFinal, String numeroRemision,
			Integer estado, Integer cliente, Integer vendedor, Integer idSede) {
		query.append("select * from remision_venta ");

		if (!fechaInicial.equals("null") && fechaFinal.equals("null")) {
			query.append("where date_format(date(fecha),'%Y-%m-%d')" + "= date('" + fechaInicial + "')");
		} else if (fechaInicial.equals("null") && !fechaFinal.equals("null")) {
			query.append("where date_format(date(fecha),'%Y-%m-%d')" + "= date('" + fechaFinal + "')");
		} else if (!fechaInicial.equals("null") && !fechaFinal.equals("null")) {
			query.append("where date_format(date(fecha),'%Y-%m-%d')" + "between " + "date_format(date('" + fechaInicial
					+ "'),'%Y-%m-%d')" + " and " + "date_format(date('" + fechaFinal + "'),'%Y-%m-%d')");
		}

		if (!numeroRemision.equals("null") && (!fechaInicial.equals("null") && !fechaFinal.equals("null"))) {
			query.append(" and numero_remision='" + numeroRemision + "'");
		}

		else if (!numeroRemision.equals("null")) {
			query.append("where numero_remision='" + numeroRemision + "'");
		}

		if (estado != 0
				&& (!fechaInicial.equals("null") || !fechaFinal.equals("null") || !numeroRemision.equals("null"))) {
			query.append(" and cod_estado_con=" + estado);
		}

		else if (estado != 0) {
			query.append("where cod_estado_con=" + estado);
		}

		if (cliente != 0 && (!fechaInicial.equals("null") || !fechaFinal.equals("null")
				|| !numeroRemision.equals("null") || estado != 0)) {
			query.append(" and nid_cliente=" + cliente);
		}

		else if (cliente != 0) {
			query.append("where nid_cliente=" + cliente);
		}

		if (vendedor != 0 && (!fechaInicial.equals("null") || !fechaFinal.equals("null")
				|| !numeroRemision.equals("null") || estado != 0 || cliente != 0)) {
			query.append(" and nid_vendedor=" + vendedor);
		}

		else if (vendedor != 0) {
			query.append("where nid_vendedor=" + vendedor);
		}

		if ((idSede != 0) && ((!fechaInicial.equals("null") || !fechaFinal.equals("null"))
				|| !numeroRemision.equals("null") || estado != 0 || cliente != 0 || vendedor != 0)) {
			query.append(" and id_sede =" + idSede);
		} else if ((idSede != 0)
				&& ((fechaInicial.equals("null") || fechaFinal.equals("null"))
						|| numeroRemision.equals("null") || estado != 0 || cliente == 0 || vendedor == 0)) {
			query.append("where id_sede =" + idSede);
		}
		// query.append(" order by id desc");
		logger.info(query);
	}

	private void generarQueryT(StringBuilder query, String fechaInicial, String fechaFinal, String numeroRemision,
			Integer estado, Integer cliente, Integer vendedor, Integer idSede) {
		query.append("select sum(total) from remision_venta ");

		if (!fechaInicial.equals("null") && fechaFinal.equals("null")) {
			query.append("where date_format(date(fecha),'%Y-%m-%d')" + "= date('" + fechaInicial + "')");
		} else if (fechaInicial.equals("null") && !fechaFinal.equals("null")) {
			query.append("where date_format(date(fecha),'%Y-%m-%d')" + "= date('" + fechaFinal + "')");
		} else if (!fechaInicial.equals("null") && !fechaFinal.equals("null")) {
			query.append("where date_format(date(fecha),'%Y-%m-%d')" + "between " + "date_format(date('" + fechaInicial
					+ "'),'%Y-%m-%d')" + " and " + "date_format(date('" + fechaFinal + "'),'%Y-%m-%d')");
		}

		if (!numeroRemision.equals("null") && (!fechaInicial.equals("null") && !fechaFinal.equals("null"))) {
			query.append(" and numero_remision='" + numeroRemision + "'");
		}

		else if (!numeroRemision.equals("null")) {
			query.append("where numero_remision='" + numeroRemision + "'");
		}

		if (estado != 0
				&& (!fechaInicial.equals("null") || !fechaFinal.equals("null") || !numeroRemision.equals("null"))) {
			query.append(" and cod_estado_con=" + estado);
		}

		else if (estado != 0) {
			query.append("where cod_estado_con=" + estado);
		}

		if (cliente != 0 && (!fechaInicial.equals("null") || !fechaFinal.equals("null")
				|| !numeroRemision.equals("null") || estado != 0)) {
			query.append(" and nid_cliente=" + cliente);
		}

		else if (cliente != 0) {
			query.append("where nid_cliente=" + cliente);
		}

		if (vendedor != 0 && (!fechaInicial.equals("null") || !fechaFinal.equals("null")
				|| !numeroRemision.equals("null") || estado != 0 || cliente != 0)) {
			query.append(" and nid_vendedor=" + vendedor);
		}

		else if (vendedor != 0) {
			query.append("where nid_vendedor=" + vendedor);
		}

		if ((idSede != 0) && ((!fechaInicial.equals("null") || !fechaFinal.equals("null"))
				|| !numeroRemision.equals("null") || estado != 0 || cliente != 0 || vendedor != 0)) {
			query.append(" and id_sede =" + idSede);
		} else if ((idSede != 0)
				&& ((fechaInicial.equals("null") || fechaFinal.equals("null"))
						|| numeroRemision.equals("null") || estado != 0 || cliente == 0 || vendedor == 0)) {
			query.append("where id_sede =" + idSede);
		}
		logger.info(query);
	}

	private void generarQueryCantidad(StringBuilder query, String fechaInicial, String fechaFinal, String numeroRemision,
			Integer estado, Integer cliente, Integer vendedor, Integer idSede) {
		query.append("SELECT count(*) FROM remision_venta");

		if (!fechaInicial.equals("null") && fechaFinal.equals("null")) {
			query.append(" where date_format(date(fecha),'%Y-%m-%d')" + "= date('" + fechaInicial + "')");
		} else if (fechaInicial.equals("null") && !fechaFinal.equals("null")) {
			query.append(" where date_format(date(fecha),'%Y-%m-%d')" + "= date('" + fechaFinal + "')");
		} else if (!fechaInicial.equals("null") && !fechaFinal.equals("null")) {
			query.append(" where date_format(date(fecha),'%Y-%m-%d')" + "between " + "date_format(date('" + fechaInicial
					+ "'),'%Y-%m-%d')" + " and " + "date_format(date('" + fechaFinal + "'),'%Y-%m-%d')");
		}

		if (!numeroRemision.equals("null") && (!fechaInicial.equals("null") && !fechaFinal.equals("null"))) {
			query.append(" and numero_remision='" + numeroRemision + "'");
		}

		else if (!numeroRemision.equals("null")) {
			query.append(" where numero_remision='" + numeroRemision + "'");
		}

		if (estado != 0
				&& (!fechaInicial.equals("null") && !fechaFinal.equals("null") || !numeroRemision.equals("null"))) {
			query.append(" and cod_estado_con=" + estado);
		}

		else if (estado != 0) {
			query.append(" where cod_estado_con=" + estado);
		}

		if (cliente != 0 && (!fechaInicial.equals("null") && !fechaFinal.equals("null")
				|| !numeroRemision.equals("null") || estado != 0)) {
			query.append(" and nid_cliente=" + cliente);
		}

		else if (cliente != 0) {
			query.append(" where nid_cliente=" + cliente);
		}

		if (vendedor != 0 && (!fechaInicial.equals("null") && !fechaFinal.equals("null")
				|| !numeroRemision.equals("null") || estado != 0 || cliente != 0)) {
			query.append(" and nid_vendedor=" + vendedor);
		}

		else if (vendedor != 0) {
			query.append(" where nid_vendedor=" + vendedor);
		}

		if ((idSede != 0) && ((!fechaInicial.equals("null") || !fechaFinal.equals("null"))
				|| !numeroRemision.equals("null") || estado != 0 || cliente != 0 || vendedor != 0)) {
			query.append(" and id_sede =" + idSede);
		} else if ((idSede != 0)
				&& ((fechaInicial.equals("null") || fechaFinal.equals("null"))
						|| numeroRemision.equals("null") || estado != 0 || cliente == 0 || vendedor == 0)) {
			query.append(" where id_sede =" + idSede);
		}
	}

	/* Borrar Remision venta */
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public @ResponseBody ResponseEntity<Object> borrarRemisionVenta(Integer id) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		try {
			this.remisionVentaDao.delete(remisionVentaDao.findById(id).get());

			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			Throwable t = e.getCause();
			if (t instanceof ConstraintViolationException) {
				logger.error("Error al elminar la remision venta " + e.getCause());
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST,
						"Error al elminar la remision venta");
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.BAD_REQUEST);
			} else {
				logger.error("Error al elminar la remision venta");
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
						"Error al elminar la remision venta");
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

		return respuesta;
	}

	// Obtener por numero
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerNumeroM() {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		try {
			List<RemisionVenta> notaDatos = this.remisionVentaDao.obtenerNumeroM();
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de numero de documento maximo exitosa");
			respuestaDto.setObjetoRespuesta(notaDatos);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo remision venta " + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error obteniendo numero de documento maximo");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return respuesta;

	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerRemision(String numero) {
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao.buscarPorIdUsuario(usuarioAutenticado.getId());
		int idSede = usuarioInformacion.getIdOrganizacion().getId();
		try {
			RemisionVenta remision = remisionVentaDao.obtenerRemisionSede(numero, idSede);

			if (remision != null) {

				List<FacturaArticulos> articulos = facturaArticuloDao.findAllBySedeRemision(remision.getId());
				remision.setFacArticulos(articulos);
			}
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito creando remision de venta");
			respuestaDto.setObjetoRespuesta(remision);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error en obtener remision de venta" + e + " linea " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error en obtener la remisión " + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerRemisionImpresion(String numero) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao.buscarPorIdUsuario(usuarioAutenticado.getId());
		int idSede = usuarioInformacion.getIdOrganizacion().getId();
		int idRemision;
		try {
			idRemision = remisionVentaDao.obtenerRemisionImpresion(numero, idSede);
			RemisionVenta remision = remisionVentaDao.buscarRemisionImpresion(idRemision);

			if (remision != null) {

				List<FacturaArticulos> articulos = facturaArticuloDao.findAllBySedeRemision(remision.getId());
				remision.setFacArticulos(articulos);
			}
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito creando remision de venta");
			respuestaDto.setObjetoRespuesta(remision);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error en obtener remision de venta" + e + " linea " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error en obtener la remisión " + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerRemisionesPendientes(Integer page) {
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao.buscarPorIdUsuario(usuarioAutenticado.getId());
		int idSede = usuarioInformacion.getIdOrganizacion().getId();
		Pageable pageConfig = PageRequest.of(page, 10);
		try {
			Page<RemisionVenta> remisiones = this.remisionVentaDao.obtenerRemisionesPendientesSede(idSede, pageConfig);
			for (RemisionVenta remision : remisiones) {
				List<FacturaArticulos> articulos = facturaArticuloDao.findAllBySedeRemision(remision.getId());
				remision.setFacArticulos(articulos);
			}
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito obteniendo las remisiones");
			respuestaDto.setObjetoRespuesta(remisiones);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error(
					"Error en obtener las remisiones de venta" + e + " linea " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error en obtener la remisión " + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	private String extraerNumeros(String cadena, String prefijo) {
		String numero = cadena.replaceAll(prefijo, "");
		return numero;
	}

	// Listar Remision venta
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> listarRemisioVenta2(Integer id, Integer page) {
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Integer suma = 0;
		try {
			Pageable pageConfig = PageRequest.of(page, 25);

			Page<RemisionVenta> remisiones = remisionVentaDao.buscarUltimosId(id, pageConfig);

			for (RemisionVenta remision : remisiones) {
				List<FacturaArticulos> articulos = facturaArticuloDao.findAllBySedeRemision(remision.getId());
				remision.setFacArticulos(articulos);
				suma += remision.getTotal();
			}

			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de remisiones exitosa");

			respuestaDto.setObjetoRespuesta(remisiones);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error al obtener las remisiones" + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al obtener las remisiones " + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	// Consulta avanzada de clientes
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerRemisionVentaConsulta2(Integer vendedor, String fechaInicial, String fechaFinal,
			Integer page) {
		ResponseEntity<Object> respuesta;
		Pageable paging = PageRequest.of(page, 25);
		try {
			List<RemisionVenta> remisiones;
			StringBuilder query = new StringBuilder();
			StringBuilder queryCantidad = new StringBuilder();
			generarQueryRemision(query, vendedor, fechaInicial, fechaFinal);
			TypedQuery<RemisionVenta> remisionesInfoQuery = (TypedQuery<RemisionVenta>) entityManager
					.createNativeQuery(query.toString(), RemisionVenta.class);
			int pageNumber = paging.getPageNumber();
			int pageSize = paging.getPageSize();
			remisionesInfoQuery.setFirstResult(pageNumber * pageSize);
			remisionesInfoQuery.setMaxResults(pageSize);
			remisiones = remisionesInfoQuery.getResultList();
			for (RemisionVenta remision : remisiones) {
				List<FacturaArticulos> articulos = facturaArticuloDao.findAllBySedeRemision(remision.getId());
				remision.setFacArticulos(articulos);
			}
			generarQueryCantidadRemision(queryCantidad, vendedor, fechaInicial, fechaFinal);
			Query cantidad = entityManager.createNativeQuery(queryCantidad.toString());
			BigInteger countResult = (BigInteger) cantidad.getSingleResult();
			Integer cantidadTotal = countResult.intValue();
			Page<RemisionVenta> result = new PageImpl<RemisionVenta>(remisiones, paging, cantidadTotal);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de remisiones de ventas exitosa");
			respuestaDto.setObjetoRespuesta(result);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo Remisiones de venta " + e + " Linea error: "
					+ e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error obteniendo Remisiones de venta");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	private void generarQueryRemision(StringBuilder query, Integer vendedor, String fechaInicial, String fechaFinal) {
		query.append("select * from remision_venta ");

		if (vendedor != 0) {
			query.append(" where nid_vendedor = " + vendedor);
		}
		if (!fechaInicial.equals("null") && fechaFinal.equals("null")) {
			query.append(" and date_format(date(fecha),'%Y-%m-%d')" + "= date('" + fechaInicial + "')");
		} else if (fechaInicial.equals("null") && !fechaFinal.equals("null")) {
			query.append(" and date_format(date(fecha),'%Y-%m-%d')" + "= date('" + fechaFinal + "')");
		} else if (!fechaInicial.equals("null") && !fechaFinal.equals("null")) {
			query.append(" and date_format(date(fecha),'%Y-%m-%d')" + "between " + "date_format(date('" + fechaInicial
					+ "'),'%Y-%m-%d')" + " and " + "date_format(date('" + fechaFinal + "'),'%Y-%m-%d')");
		}
		query.append(" order by id desc");
	}

	private void generarQueryCantidadRemision(StringBuilder query, Integer vendedor, String fechaInicial,
			String fechaFinal) {
		query.append("SELECT count(*) FROM remision_venta where");

		if (vendedor != 0) {
			query.append(" nid_vendedor = " + vendedor);
		}
		if (!fechaInicial.equals("null") && fechaFinal.equals("null")) {
			query.append(" and date_format(date(fecha),'%Y-%m-%d')" + "= date('" + fechaInicial + "')");
		} else if (fechaInicial.equals("null") && !fechaFinal.equals("null")) {
			query.append(" and date_format(date(fecha),'%Y-%m-%d')" + "= date('" + fechaFinal + "')");
		} else if (!fechaInicial.equals("null") && !fechaFinal.equals("null")) {
			query.append(" and date_format(date(fecha),'%Y-%m-%d')" + "between " + "date_format(date('" + fechaInicial
					+ "'),'%Y-%m-%d')" + " and " + "date_format(date('" + fechaFinal + "'),'%Y-%m-%d')");
		}
	}

	// Listar Remision venta
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> listarPorCedulaVendedor(String nitocc) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		try {

			Integer remisiones = remisionVentaDao.buscarPorCedulaVendedor(nitocc);

			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de remisiones exitosa");

			respuestaDto.setObjetoRespuesta(remisiones);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error al obtener las remisiones" + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al obtener las remisiones " + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

}
