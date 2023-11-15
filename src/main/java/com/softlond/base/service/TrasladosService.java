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
import com.softlond.base.entity.Entrada;
import com.softlond.base.entity.EstadoArticulo;
import com.softlond.base.entity.InformacionUsuario;
import com.softlond.base.repository.EstadoArticuloDao;
import com.softlond.base.entity.InveLocal;
import com.softlond.base.entity.SalidaArticulos;
import com.softlond.base.entity.SalidaMercancia;
import com.softlond.base.entity.Traslado;
import com.softlond.base.entity.Usuario;
import com.softlond.base.repository.ArticuloDao;
import com.softlond.base.repository.ArticuloMovimientoDao;
import com.softlond.base.repository.EntradaDao;
import com.softlond.base.repository.SalidaArticulosDao;
import com.softlond.base.repository.SalidaMercanciaDao;
import com.softlond.base.repository.TrasladosDao;
import com.softlond.base.repository.UsuarioInformacionDao;

@Service
public class TrasladosService {

	@Autowired
	private TrasladosDao trasladosDao;

	@Autowired
	EstadoArticuloDao estadoArticuloDao;

	@Autowired
	private UsuarioInformacionDao usuarioInformacionDao;

	@Autowired
	private ArticuloDao articuloDao;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private ArticuloMovimientoDao articuloMovimientoDao;

	@Autowired
	private EntradaDao entradaDao;

	@Autowired
	private SalidaArticulosDao salidaArticuloDao;

	@Autowired
	private SalidaMercanciaDao salidaMercanciaDao;

	private static final Logger logger = Logger.getLogger(TrasladosService.class);

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> crearTrasladosArticulos(Traslado traslado) {
		logger.info("ingresa a crear traslado");
		// logger.info(traslado.getMovimientos().size());
		ResponseEntity<Object> respuesta;
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		try {
			logger.info("ingresa al try de crear traslado");
			traslado.setUsuario(usuarioInformacion);
			trasladosDao.save(traslado);
			logger.info("llega hasta for de movimientos");
			EstadoArticulo estado;
			for (ArticuloMovimientos movimiento : traslado.getMovimientos()) {
				Articulo articulo = movimiento.getArticulo();
				articulo.setLocal(movimiento.getSectorDestino().getIdLocal());
				if (articulo.getLocal().getTLocal().equals("BODEGA")) {
					estado = estadoArticuloDao.findById(2).orElse(null);
					articulo.setEstadoArticulo(estado);
				} else {
					estado = estadoArticuloDao.findById(1).orElse(null);
					articulo.setEstadoArticulo(estado);
				}
				articulo.setSector(movimiento.getSectorDestino());
				// articulo.setSector.t_sector(movimiento.getSectorDestino().getTSector());
				// InveLocal localActualizado = articulo.getLocal();
				// localActualizado.setTLocal(movimiento.getSectorDestino().getTSector());
				// articulo.setLocal(localActualizado);
				articuloDao.save(articulo);
			}

			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "traslado creado exitosamente");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error al guardar el traslado " + e + " linea " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al guardar el traslado " + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerTrasladosConsulta(String fechaInicial, String fechaFinal,
			String numeroTraslado, Integer estado, Integer page) {
		ResponseEntity<Object> respuesta;
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		Pageable paging = PageRequest.of(page, 10);
		Integer idSede = usuarioInformacion.getIdOrganizacion().getId();
		try {
			List<Traslado> traslados;
			StringBuilder query = new StringBuilder();
			StringBuilder queryCantidad = new StringBuilder();
			logger.info(estado);

			generarQueryTraslados(query, fechaInicial, fechaFinal, numeroTraslado, estado, idSede);
			generarQueryTrasladosCantidad(queryCantidad, fechaInicial, fechaFinal, numeroTraslado, estado, idSede);
			logger.info(query);
			TypedQuery<Traslado> trasladosInfoQuery = (TypedQuery<Traslado>) entityManager
					.createNativeQuery(query.toString(), Traslado.class);
			int pageNumber = paging.getPageNumber();
			int pageSize = paging.getPageSize();
			trasladosInfoQuery.setFirstResult(pageNumber * pageSize);
			trasladosInfoQuery.setMaxResults(pageSize);
			traslados = trasladosInfoQuery.getResultList();
			// for (Traslado traslado : traslados) {
			// List<ArticuloMovimientos> movimientos = articuloMovimientoDao
			// .obtenerMovimientosTraslado(traslado.getId());
			// traslado.setMovimientos(movimientos);
			// }
			Query cantidad = entityManager.createNativeQuery(queryCantidad.toString());
			BigInteger countResult = (BigInteger) cantidad.getSingleResult();
			Integer cantidadTotal = countResult.intValue();
			for (Traslado traslado : traslados) {
				traslado.setMovimientos(articuloMovimientoDao.obtenerMovimientosTraslado(traslado.getId()));
			}
			Page<Traslado> result = new PageImpl<Traslado>(traslados, paging, cantidadTotal);
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

	private void generarQueryTraslados(StringBuilder query, String fechaInicial, String fechaFinal,
			String numeroTraslado, Integer estado, Integer idSede) {
		query.append("select * from traslado where id_sede= " + idSede);
		// query.append("select * from traslado t join entrada e on t.id_sede =
		// e.id_sede where t.id_sede= " + idSede);
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
		logger.info(query);
	}

	private void generarQueryTrasladosCantidad(StringBuilder query, String fechaInicial, String fechaFinal,
			String numeroTraslado, Integer estado, Integer idSede) {
		query.append("select count(*) from traslado where id_sede= " + idSede);
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
		logger.info(query);
	}

	// ! listar entradas de mercancia

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

	// ! fin listar entrada mercancia -----

	// ! listar salida de mercancia

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerSalidaConsulta(String fechaInicial, String fechaFinal,
			String numeroTraslado, Integer estado, Integer page) {
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
				List<SalidaArticulos> salidasArticulos = salidaArticuloDao.salidasArticulos(salida.getId());
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

	// ! fin listar salida mercancia ----

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> actualizarTraslados(Traslado traslado) {
		logger.info("ingresa a actualizar traslado");
		ResponseEntity<Object> respuesta;
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao.buscarPorIdUsuario(usuarioAutenticado.getId());
		try {
			traslado.setUsuario(usuarioInformacion);
			articuloMovimientoDao.eliminarMovimientosTraslado(traslado.getId());
			trasladosDao.save(traslado);
			List<ArticuloMovimientos> movimientosAntiguos = articuloMovimientoDao
					.obtenerMovimientosTraslado(traslado.getId());
			logger.info(movimientosAntiguos.size());

			for (ArticuloMovimientos movimiento : movimientosAntiguos) {
				Articulo articulo = movimiento.getArticulo();
				// articulo.setSector(movimiento.getSectorOrigen().getTSector());
				articulo.setLocal(movimiento.getSectorOrigen().getIdLocal());
				articulo.setSector(movimiento.getSectorOrigen());
				articuloDao.save(articulo);
			}
			// articuloMovimientoDao.eliminarMovimientosTraslado(traslado.getId());
			// trasladosDao.save(traslado);
			for (ArticuloMovimientos movimiento : traslado.getMovimientos()) {
				Articulo articulo = movimiento.getArticulo();
				// articulo.setSector(movimiento.getSectorDestino().getTSector());
				articulo.setLocal(movimiento.getSectorDestino().getIdLocal());
				articulo.setSector(movimiento.getSectorDestino());
				articuloDao.save(articulo);
			}
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "traslado creado exitosamente");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error al guardar el traslado " + e + " linea " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al guardar el traslado " + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> trasladosMes(Integer page) {
		ResponseEntity<Object> respuesta;
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		Pageable pageConfig = PageRequest.of(page, 10);
		try {
			Page<Traslado> traslados = trasladosDao.trasladoMes(usuarioInformacion.getIdOrganizacion().getId(),
					pageConfig);
			for (Traslado traslado : traslados) {
				List<ArticuloMovimientos> movimientos = articuloMovimientoDao
						.obtenerMovimientosTraslado(traslado.getId());
				traslado.setMovimientos(movimientos);
			}
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Entrada mercancia creado exitosamente");
			respuestaDto.setObjetoRespuesta(traslados);
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
	public ResponseEntity<Object> obtenerTrasladoNumero(String numeroTraslado) {
		ResponseEntity<Object> respuesta;
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		try {
			List<Traslado> traslados = trasladosDao.obtenerTrasladoNumero(numeroTraslado);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Entrada mercancia creado exitosamente");
			respuestaDto.setObjetoRespuesta(traslados);
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
