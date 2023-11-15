package com.softlond.base.service;

import java.math.BigInteger;
import java.util.ArrayList;
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
import com.softlond.base.entity.Entrada;
import com.softlond.base.entity.EntradaArticulos;
// import com.softlond.base.entity.EntradaArticulos;
import com.softlond.base.entity.EstadoArticulo;
import com.softlond.base.entity.InformacionUsuario;
import com.softlond.base.entity.SalidaArticulos;
import com.softlond.base.entity.SalidaMercancia;
import com.softlond.base.entity.Traslado;
import com.softlond.base.entity.Usuario;
import com.softlond.base.repository.ArticuloDao;
import com.softlond.base.repository.ArticulosRemisionCompraDao;
import com.softlond.base.repository.EntradaArticuloDao;
import com.softlond.base.repository.EntradaDao;
import com.softlond.base.repository.EstadoArticuloDao;
import com.softlond.base.repository.RemisionCompraDao;
import com.softlond.base.repository.UsuarioInformacionDao;
import com.softlond.base.request.EntradaRequest;

@Service
public class EntradaService {

	@Autowired
	private UsuarioInformacionDao usuarioInformacionDao;

	@Autowired
	private EntradaDao entradaDao;

	@Autowired
	private EntradaArticuloDao EntradaArticuloDao;

	@Autowired
	private ArticuloDao articuloDao;

	@Autowired
	EstadoArticuloDao estadoArticuloDao;

	@Autowired
	public ArticulosRemisionCompraDao articulosRemisionCompraDao;

	@Autowired
	private RemisionCompraDao remisionCompraDao;

	@PersistenceContext
	private EntityManager entityManager;

	private static final Logger logger = Logger.getLogger(EntradaService.class);

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> crearEntradaMercancia(Entrada entradaRequest) {
		logger.info("entradaRequest");
		logger.info(entradaRequest.getEntradas());
		ResponseEntity<Object> respuesta;
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		try {
			entradaRequest.setUsuario(usuarioInformacion);
			// articulosRemisionCompraDao.eliminarArticulosRemision(entradaRequest.getEntrada().getRemision().getId());
			// remisionCompraDao.save(entradaRequest.getEntrada().getRemision());
			logger.info("ENTRADA Request GetEntradas");
			// logger.info(entradaRequest);
			List<EntradaArticulos> ent = new ArrayList<EntradaArticulos>();
			for (int i = 0; i < entradaRequest.getEntradas().size(); i++) {
				// Integer id = entradaRequest.getArticulosRemision().get(i).;
				EntradaArticulos e = new EntradaArticulos();
				logger.info(entradaRequest.getEntradas().get(i));
				e.setArticulo(entradaRequest.getEntradas().get(i).getArticulo());
				logger.info(e.getArticulo());
				ent.add(e);
				logger.info(ent);
			}

			if (entradaRequest.getEntradas() != null) {
				for (int i = 0; i < entradaRequest.getEntradas().size(); i++) {
					logger.info(entradaRequest.getEntradas().get(i).getArticulo().getCodigo());
				}
			} else {
				logger.info("ingresa al else");
				entradaRequest.setEntradas(ent);
				if (entradaRequest.getEntradas() != null)
					logger.info(entradaRequest.getEntradas().size());
				// // for (int i = 0; i < entradaRequest.getEntrada().getEntradas().size(); i++)
				// {
				// //
				// logger.info(entradaRequest.getEntrada().getEntradas().get(i).getArticulo().getCodigo());
				// // }
			}

			entradaDao.save(entradaRequest);
			EstadoArticulo estado;
			if (entradaRequest.getEntradas() != null) {
				for (EntradaArticulos entradaArticulo : entradaRequest.getEntradas()) {
					Articulo articulo = entradaArticulo.getArticulo();
					if (articulo.getCantidadDisponible() == 0) {
						estado = estadoArticuloDao.findById(4).orElse(null);
						articulo.setEstadoArticulo(estado);
					} else {
						logger.info(articulo.getCantidadDisponible());
					}
					logger.info(articulo.getCantidadDisponible() + entradaArticulo.getCantidad());
					articulo.setCantidadDisponible(articulo.getCantidadDisponible() + entradaArticulo.getCantidad());
					logger.info(articulo.getCantidadDisponible());
					// articuloDao.cambiarCantidadDisponible(articulo.getCantidadDisponible() -
					// salidaArticulo.getCantidad(),
					// articulo.getId());
					articuloDao.save(articulo);
				}
			}
			// articulosRemisionCompraDao.saveAll(entradaRequest.getArticulosRemision());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Entrada mercancia creado exitosamente");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error(
					"Error al guardar la entrada mercancia " + e + " linea " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al guardar la entrada mercancia " + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerEntradasConsulta(String fechaInicial, String fechaFinal,
			String numeroTraslado, Integer estado, Integer page) {
		ResponseEntity<Object> respuesta;
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		Pageable paging = PageRequest.of(page, 10);
		Integer idSede = usuarioInformacion.getIdOrganizacion().getId();
		try {
			List<Entrada> entradas;
			StringBuilder query = new StringBuilder();
			StringBuilder queryCantidad = new StringBuilder();
			generarQueryEntradas(query, fechaInicial, fechaFinal, numeroTraslado, estado, idSede);
			generarQueryEntradasCantidad(queryCantidad, fechaInicial, fechaFinal, numeroTraslado, estado, idSede);
			TypedQuery<Entrada> entradasInfoQuery = (TypedQuery<Entrada>) entityManager
					.createNativeQuery(query.toString(), Entrada.class);
			int pageNumber = paging.getPageNumber();
			int pageSize = paging.getPageSize();
			entradasInfoQuery.setFirstResult(pageNumber * pageSize);
			entradasInfoQuery.setMaxResults(pageSize);
			entradas = entradasInfoQuery.getResultList();
			for (Entrada entrada : entradas) {
				List<EntradaArticulos> entradasArticulos = EntradaArticuloDao.entradasArticulos(entrada.getId());
				entrada.setEntradas(entradasArticulos);
			}
			Query cantidad = entityManager.createNativeQuery(queryCantidad.toString());
			BigInteger countResult = (BigInteger) cantidad.getSingleResult();
			Integer cantidadTotal = countResult.intValue();
			Page<Entrada> result = new PageImpl<Entrada>(entradas, paging, cantidadTotal);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "entradas obtenidas exitosamente");
			respuestaDto.setObjetoRespuesta(result);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error al obtener las entradas " + e + " linea " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al obtener las entradas " + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	private void generarQueryEntradas(StringBuilder query, String fechaInicial, String fechaFinal,
			String numeroTraslado, Integer estado, Integer idSede) {
		query.append("select * from entrada where id_sede= " + idSede);
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

	private void generarQueryEntradasCantidad(StringBuilder query, String fechaInicial, String fechaFinal,
			String numeroTraslado, Integer estado, Integer idSede) {
		query.append("select count(*) from entrada where id_sede= " + idSede);
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
	public ResponseEntity<Object> actualizarEntradaMercancia(Entrada entradaRequest) {
		logger.info("ingresa a actualizarEntradaMercancia");
		ResponseEntity<Object> respuesta;
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		// logger.info(entradaRequest.getEntradas());
		try {
			entradaRequest.setUsuario(usuarioInformacion);
			EstadoArticulo estado;
			List<EntradaArticulos> articulosAntiguos = EntradaArticuloDao.entradasArticulos(entradaRequest.getId());
			logger.info(entradaRequest.getId());
			EntradaArticuloDao.eliminarArticulosEntrada(entradaRequest.getId());
			for (EntradaArticulos entradaAntigua : articulosAntiguos) {
				Articulo articulo = entradaAntigua.getArticulo();
				articulo.setCantidadDisponible(articulo.getCantidadDisponible() - entradaAntigua.getCantidad());
				if (articulo.getCantidadDisponible() > 0) {
					estado = estadoArticuloDao.findById(1).orElse(null);
					articulo.setEstadoArticulo(estado);
				}
				articuloDao.save(articulo);
			}
			for (EntradaArticulos entradaArticulos : entradaRequest.getEntradas()) {
				entradaArticulos.setId(null);
			}
			entradaDao.save(entradaRequest);
			for (EntradaArticulos entradaArticulo : entradaRequest.getEntradas()) {
				Articulo articulo = entradaArticulo.getArticulo();
				articulo.setCantidadDisponible(articulo.getCantidadDisponible() + entradaArticulo.getCantidad());
				if (articulo.getCantidadDisponible() == 0) {
					estado = estadoArticuloDao.findById(4).orElse(null);
					articulo.setEstadoArticulo(estado);
				}
				articuloDao.save(articulo);
			}
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Entrada mercancia editada exitosamente");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error(
					"Error al guardar la entrada mercancia " + e + " linea " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al guardar la entrada mercancia " + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> entradaMes(Integer page) {
		ResponseEntity<Object> respuesta;
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		Pageable pageConfig = PageRequest.of(page, 10);
		try {
			Page<Entrada> entradas = entradaDao.entradasMes(usuarioInformacion.getIdOrganizacion().getId(), pageConfig);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Entrada mercancia creado exitosamente");
			respuestaDto.setObjetoRespuesta(entradas);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error(
					"Error al guardar la entrada mercancia " + e + " linea " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al guardar la entrada mercancia " + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> entradasNumero(String numero) {
		ResponseEntity<Object> respuesta;
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		try {
			List<Entrada> entradas = entradaDao.entradasNumero(numero);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Entrada mercancia creado exitosamente");
			respuestaDto.setObjetoRespuesta(entradas);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error(
					"Error al guardar la entrada mercancia " + e + " linea " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al guardar la entrada mercancia " + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}
}
