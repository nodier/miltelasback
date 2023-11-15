package com.softlond.base.service;

import java.math.BigInteger;
import java.util.List;

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

import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.Articulo;
import com.softlond.base.entity.ArticuloMovimientos;
import com.softlond.base.entity.EstadoArticulo;
import com.softlond.base.entity.InformacionUsuario;
import com.softlond.base.entity.SalidaArticulos;
import com.softlond.base.entity.SalidaMercancia;
import com.softlond.base.entity.Traslado;
import com.softlond.base.entity.Usuario;
import com.softlond.base.repository.ArticuloDao;
import com.softlond.base.repository.ArticuloMovimientoDao;
import com.softlond.base.repository.EstadoArticuloDao;
import com.softlond.base.repository.SalidaArticulosDao;
import com.softlond.base.repository.SalidaMercanciaDao;
import com.softlond.base.repository.UsuarioInformacionDao;

@Service
public class SalidaService {

	@Autowired
	private UsuarioInformacionDao usuarioInformacionDao;

	@Autowired
	private ArticuloDao articuloDao;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private SalidaArticulosDao salidaArticuloDao;

	@Autowired
	private SalidaMercanciaDao salidaMercanciaDao;

	@Autowired
	EstadoArticuloDao estadoArticuloDao;

	private static final Logger logger = Logger.getLogger(SalidaService.class);

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> crearSalidaMercancia(SalidaMercancia salida) {
		logger.info("crearSalidaMercancia");
		logger.info(salida.getSalidas().get(0).getCantidad());
		ResponseEntity<Object> respuesta;
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		try {
			salida.setUsuario(usuarioInformacion);
			salidaMercanciaDao.save(salida);
			EstadoArticulo estado;
			for (SalidaArticulos salidaArticulo : salida.getSalidas()) {
				Articulo articulo = salidaArticulo.getArticulo();
				// if (articulo.getCantidadDisponible() == 0) {
				// estado = estadoArticuloDao.findById(4).orElse(null);
				// articulo.setEstadoArticulo(estado);
				// } else {
				// logger.info(articulo.getCantidadDisponible());
				// }
				logger.info(articulo.getCantidadDisponible() - salidaArticulo.getCantidad());
				articulo.setCantidadDisponible(articulo.getCantidadDisponible() - salidaArticulo.getCantidad());
				if (articulo.getCantidadDisponible() == 0) {
					estado = estadoArticuloDao.findById(4).orElse(null);
					articulo.setEstadoArticulo(estado);
				} else {
					logger.info(articulo.getCantidadDisponible());
				}
				logger.info(articulo.getCantidadDisponible());
				// articuloDao.cambiarCantidadDisponible(articulo.getCantidadDisponible() -
				// salidaArticulo.getCantidad(),
				// articulo.getId());
				logger.info(articulo.getEstadoArticulo());
				articuloDao.save(articulo);
			}
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Salida mercancia creado exitosamente");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error(
					"Error al guardar la salida mercancia " + e + " linea " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al guardar la salida mercancia " + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerSalidaConsulta(String fechaInicial, String fechaFinal,
			String numeroTraslado, Integer estado, Integer page) {
		logger.info(estado);
		ResponseEntity<Object> respuesta;
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		Pageable paging = PageRequest.of(page, 10);
		Integer idSede = usuarioInformacion.getIdOrganizacion().getId();
		try {
			List<SalidaMercancia> salidas;
			StringBuilder query = new StringBuilder();
			StringBuilder queryCantidad = new StringBuilder();
			generarQuerySalidas(query, fechaInicial, fechaFinal, numeroTraslado, estado, idSede);
			generarQuerySalidasCantidad(queryCantidad, fechaInicial, fechaFinal, numeroTraslado, estado, idSede);
			TypedQuery<SalidaMercancia> salidasInfoQuery = (TypedQuery<SalidaMercancia>) entityManager
					.createNativeQuery(query.toString(), SalidaMercancia.class);
			int pageNumber = paging.getPageNumber();
			int pageSize = paging.getPageSize();
			salidasInfoQuery.setFirstResult(pageNumber * pageSize);
			salidasInfoQuery.setMaxResults(pageSize);
			salidas = salidasInfoQuery.getResultList();
			for (SalidaMercancia salida : salidas) {
				List<SalidaArticulos> salidasArticulos = salidaArticuloDao
						.salidasArticulos(salida.getId());
				salida.setSalidas(salidasArticulos);
			}
			Query cantidad = entityManager.createNativeQuery(queryCantidad.toString());
			BigInteger countResult = (BigInteger) cantidad.getSingleResult();
			Integer cantidadTotal = countResult.intValue();
			Page<SalidaMercancia> result = new PageImpl<SalidaMercancia>(salidas, paging, cantidadTotal);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "traslado creado exitosamente");
			respuestaDto.setObjetoRespuesta(result);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error al obtener los traslados " + e + " linea " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al obtener los traslados " + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	private void generarQuerySalidas(StringBuilder query, String fechaInicial, String fechaFinal,
			String numeroTraslado, Integer estado, Integer idSede) {
		query.append("select * from salida_mercancia where id_sede= " + idSede);
		if (!fechaInicial.equals("null") && fechaFinal.equals("null")) {
			query.append(" and date_format(date(fecha_documento),'%Y-%m-%d')" + "= date('" + fechaInicial + "')");
		} else if (fechaInicial.equals("null") && !fechaFinal.equals("null")) {
			query.append(" and date_format(date(fecha_documento),'%Y-%m-%d')" + "<= date('" + fechaFinal + "')");
		} else if (!fechaInicial.equals("null") && !fechaFinal.equals("null")) {
			query.append(" and date_format(date(fecha_documento),'%Y-%m-%d')" + "between " + "date('" + fechaInicial
					+ "') and " + "date('" + fechaFinal + "')");
		}
		if (!numeroTraslado.equals("null")) {
			query.append(" and numero_documento = " + numeroTraslado);
		}
		if (estado != 0) {
			query.append(" and cod_estado = " + estado);
		}
	}

	private void generarQuerySalidasCantidad(StringBuilder query, String fechaInicial, String fechaFinal,
			String numeroTraslado, Integer estado, Integer idSede) {
		query.append("select count(*) from salida_mercancia where id_sede= " + idSede);
		if (!fechaInicial.equals("null") && fechaFinal.equals("null")) {
			query.append(" and date_format(date(fecha_documento),'%Y-%m-%d')" + "= date('" + fechaInicial + "')");
		} else if (fechaInicial.equals("null") && !fechaFinal.equals("null")) {
			query.append(" and date_format(date(fecha_documento),'%Y-%m-%d')" + "<= date('" + fechaFinal + "')");
		} else if (!fechaInicial.equals("null") && !fechaFinal.equals("null")) {
			query.append(" and date_format(date(fecha_documento),'%Y-%m-%d')" + "between " + "date('" + fechaInicial
					+ "') and " + "date('" + fechaFinal + "')");
		}
		if (!numeroTraslado.equals("null")) {
			query.append(" and numero_documento = " + numeroTraslado);
		}
		if (estado != 0) {
			query.append(" and cod_estado = " + estado);
		}
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> actualizarSalidaMercancia(SalidaMercancia salida) {

		ResponseEntity<Object> respuesta;
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		try {
			salida.setUsuario(usuarioInformacion);
			EstadoArticulo estado;
			List<SalidaArticulos> articulosAntiguos = salidaArticuloDao.salidasArticulos(salida.getId());
			salidaArticuloDao.eliminarArticulosSalida(salida.getId());
			for (SalidaArticulos salidaAntigua : articulosAntiguos) {
				Articulo articulo = salidaAntigua.getArticulo();
				articulo.setCantidadDisponible(articulo.getCantidadDisponible() + salidaAntigua.getCantidad());
				if (articulo.getCantidadDisponible() > 0) {
					estado = estadoArticuloDao.findById(1).orElse(null);
					articulo.setEstadoArticulo(estado);
				}
				articuloDao.save(articulo);
			}
			for (SalidaArticulos salidaArticulos : salida.getSalidas()) {
				salidaArticulos.setId(null);
			}
			salidaMercanciaDao.save(salida);
			for (SalidaArticulos salidaArticulo : salida.getSalidas()) {
				Articulo articulo = salidaArticulo.getArticulo();
				articulo.setCantidadDisponible(articulo.getCantidadDisponible() - salidaArticulo.getCantidad());
				if (articulo.getCantidadDisponible() == 0) {
					estado = estadoArticuloDao.findById(4).orElse(null);
					articulo.setEstadoArticulo(estado);
				}
				articuloDao.save(articulo);
			}
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Salida mercancia editada exitosamente");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error(
					"Error al guardar la salida mercancia " + e + " linea " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al guardar la salida mercancia " + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> salidaMercanciaMes(Integer page) {
		ResponseEntity<Object> respuesta;
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		Pageable pageConfig = PageRequest.of(page, 10);
		try {
			Page<SalidaMercancia> salidas = salidaMercanciaDao
					.salidaMercanciaMes(usuarioInformacion.getIdOrganizacion().getId(), pageConfig);
			for (SalidaMercancia salida : salidas) {
				List<SalidaArticulos> salidasArticulos = salidaArticuloDao
						.salidasArticulos(salida.getId());
				salida.setSalidas(salidasArticulos);
			}
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Salida mercancia creado exitosamente");
			respuestaDto.setObjetoRespuesta(salidas);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error al obtener los traslados " + e + " linea " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al obtener los traslados " + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> salidaMercanciaNumero(String numeroSalida) {
		ResponseEntity<Object> respuesta;
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		try {
			List<SalidaMercancia> salidas = salidaMercanciaDao.salidaMercanciaNumero(numeroSalida);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Salida mercancia creado exitosamente");
			respuestaDto.setObjetoRespuesta(salidas);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error al obtener los traslados " + e + " linea " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al obtener los traslados " + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

}
