package com.softlond.base.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
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
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.PrdTipos;
import com.softlond.base.entity.Producto;
import com.softlond.base.entity.Promocion;
import com.softlond.base.repository.PromocionDao;
import com.softlond.base.response.ProductoResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.softlond.base.entity.InformacionUsuario;
import com.softlond.base.entity.Usuario;
import com.softlond.base.repository.UsuarioInformacionDao;

@Service
public class PromocionService {

	private static final Logger logger = Logger.getLogger(PromocionService.class);

	@Autowired
	private PromocionDao promocionDao;

	@Autowired
	UsuarioInformacionDao usuarioInformacionDao;

	@PersistenceContext
	private EntityManager entityManager;

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> crearPromocion(Promocion promocion) {
		ResponseEntity<Object> respuesta = null;
		Integer promocionExiste;

		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		int idSede = usuarioInformacion.getIdOrganizacion().getId();

		promocion.setSede(idSede);

		try {
			if (promocion.getTipo().getId() != null && promocion.getReferencia().getId() != null
					&& promocion.getPresentacion().getId() != null) {
				if (promocion.getId() != null) {
					promocionExiste = promocionDao.existeDescuentoTipoReferenciaPresentacion(promocion.getId(),
							promocion.getTipo().getId(), promocion.getReferencia().getId(),
							promocion.getPresentacion().getId(), idSede);
				} else {
					promocionExiste = promocionDao.existeDescuentoTipoReferenciaPresentacionCreacion(
							promocion.getTipo().getId(), promocion.getReferencia().getId(),
							promocion.getPresentacion().getId(), idSede);
				}

				if (promocionExiste <= 0) {
					Float valorDescuentoMayor = 100.0f;
					Float valorDescuentoActual = 0.0f;
					Float valorDescuentoMenor = 0.0f;

					if (promocion.getId() != null)
						valorDescuentoMenor = promocionDao.existeDescuentoPresentacionEdicionValor(promocion.getId(),
								promocion.getTipo().getId(), promocion.getReferencia().getId(), null, idSede);
					else
						valorDescuentoMenor = promocionDao.existeDescuentoPresentacionCreacionValor(promocion.getTipo().getId(),
								promocion.getReferencia().getId(), null, idSede);
					if (valorDescuentoMenor == null) {
						valorDescuentoMenor = 0.0f;
					}
					valorDescuentoActual = promocion.getDescuento();
					if (valorDescuentoActual == null) {
						valorDescuentoActual = 0.0f;
					}

					if (valorDescuentoActual >= valorDescuentoMenor) {

						promocion.setTipo(promocion.getTipo());
						promocion.setReferencia(promocion.getReferencia());
						promocion.setPresentacion(promocion.getPresentacion());

						promocionDao.save(promocion);

						respuesta = ResponseEntity.ok(HttpStatus.OK);
						RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "creación de la promocion exitosa");
						respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);

					} else {

						respuesta = ResponseEntity.ok(HttpStatus.OK);
						RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "descuento_invalido");
						respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
					}

				} else {

					respuesta = ResponseEntity.ok(HttpStatus.OK);
					RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "descuento_existe");
					respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
				}

			} else if (promocion.getTipo().getId() == null && promocion.getReferencia().getId() == null
					&& promocion.getPresentacion().getId() == null) {// aplica para el menor descuento (¡¡descuento para
				// todos los articulos de la tienda!!)
				if (promocion.getId() != null) {

					promocionExiste = promocionDao.existeDescuentoTodos(promocion.getId(), promocion.getTipo().getId(),
							promocion.getReferencia().getId(), promocion.getPresentacion().getId(), idSede);
				} else {

					promocionExiste = promocionDao.existeDescuentoTodosCreacion(promocion.getTipo().getId(),
							promocion.getReferencia().getId(), promocion.getPresentacion().getId(), idSede);
				}

				if (promocionExiste <= 0) {
					Float valorDescuentoMayor = 0.0f;
					Float valorDescuentoActual = 0.0f;
					Float valorDescuentoMenor = 0.0f;

					if (promocion.getId() != null)
						valorDescuentoMayor = promocionDao.descuentoMenorEdicionValor(promocion.getId(), idSede);
					else
						valorDescuentoMayor = promocionDao.descuentoMenorCreacionValor(idSede);
					if (valorDescuentoMayor == null) {
						valorDescuentoMayor = 100.0f;
					}
					valorDescuentoActual = promocion.getDescuento();
					if (valorDescuentoActual == null) {
						valorDescuentoActual = 0.0f;
					}

					if (valorDescuentoActual <= valorDescuentoMayor) {
						promocion.setTipo(null);
						promocion.setReferencia(null);
						promocion.setPresentacion(null);

						promocionDao.save(promocion);

						respuesta = ResponseEntity.ok(HttpStatus.OK);
						RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "creación de la promocion exitosa");
						respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
					} else {

						respuesta = ResponseEntity.ok(HttpStatus.OK);
						RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "descuento_invalido");
						respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
					}
				} else {

					respuesta = ResponseEntity.ok(HttpStatus.OK);
					RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "descuento_existe");
					respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
				}

			} else if (promocion.getReferencia().getId() == null && promocion.getPresentacion().getId() == null) {

				if (promocion.getId() != null) {
					promocionExiste = promocionDao.existeDescuentoReferenciaPresentacion(promocion.getId(),
							promocion.getTipo().getId(), promocion.getReferencia().getId(), promocion.getPresentacion().getId(),
							idSede);
				} else {
					promocionExiste = promocionDao.existeDescuentoReferenciaPresentacionCreacion(promocion.getTipo().getId(),
							promocion.getReferencia().getId(), promocion.getPresentacion().getId(), idSede);
				}

				if (promocionExiste <= 0) {
					Float valorDescuentoMayor = 0.0f;
					Float valorDescuentoActual = 0.0f;
					Float valorDescuentoMenor = 0.0f;

					valorDescuentoActual = promocion.getDescuento();
					if (valorDescuentoActual == null) {
						valorDescuentoActual = 0.0f;
					}
					if (promocion.getId() != null)
						valorDescuentoMenor = promocionDao.existeDescuentoTodosEdicionValor(promocion.getId(), null,
								null, null, idSede);
					else
						valorDescuentoMenor = promocionDao.existeDescuentoTodosCreacionValor(null, null, null, idSede);
					if (valorDescuentoMenor == null) {
						valorDescuentoMenor = 0.0f;
					}
					if (promocion.getId() != null)
						valorDescuentoMayor = promocionDao.existeDescuentoNPresentacionEdicionValor(promocion.getId(),
								promocion.getTipo().getId(), null, idSede);
					else
						valorDescuentoMayor = promocionDao.existeDescuentoNPresentacionCreacionValor(promocion.getTipo().getId(),
								null, idSede);

					if (valorDescuentoMayor == null) {
						valorDescuentoMayor = 100.0f;
					}

					if (valorDescuentoMenor <= valorDescuentoActual && valorDescuentoActual <= valorDescuentoMayor) {

						promocion.setReferencia(null);
						promocion.setPresentacion(null);

						promocionDao.save(promocion);

						respuesta = ResponseEntity.ok(HttpStatus.OK);
						RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "creación de la promocion exitosa");
						respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
					} else {

						respuesta = ResponseEntity.ok(HttpStatus.OK);
						RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "descuento_invalido");
						respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
					}
				} else {

					respuesta = ResponseEntity.ok(HttpStatus.OK);
					RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "descuento_existe");
					respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
				}

			} else if (promocion.getPresentacion().getId() == null) {

				if (promocion.getId() != null) {
					promocionExiste = promocionDao.existeDescuentoPresentacion(promocion.getId(),
							promocion.getTipo().getId(), promocion.getReferencia().getId(),
							promocion.getPresentacion().getId(), idSede);
				} else {
					promocionExiste = promocionDao.existeDescuentoPresentacionCreacion(promocion.getTipo().getId(),
							promocion.getReferencia().getId(), promocion.getPresentacion().getId(), idSede);
				}

				if (promocionExiste <= 0) {
					Float valorDescuentoMayor = 0.0f;
					Float valorDescuentoActual = 0.0f;
					Float valorDescuentoMenor = 0.0f;

					if (promocion.getId() != null)
						valorDescuentoMayor = promocionDao.existeDescuentoTipoReferenciaPresentacionEdicionValor(
								promocion.getId(), promocion.getTipo().getId(), promocion.getReferencia().getId(), idSede);
					else
						valorDescuentoMayor = promocionDao.existeDescuentoTipoReferenciaPresentacionCreacionValor(
								promocion.getTipo().getId(), promocion.getReferencia().getId(), idSede);

					if (valorDescuentoMayor == null) {
						valorDescuentoMayor = 100.0f;
					}
					valorDescuentoActual = promocion.getDescuento();
					if (valorDescuentoActual == null) {
						valorDescuentoActual = 0.0f;
					}
					if (promocion.getId() != null)
						valorDescuentoMenor = promocionDao.existeDescuentoReferenciaPresentacionEdicionValor(
								promocion.getId(), promocion.getTipo().getId(), null, null, idSede);
					else
						valorDescuentoMenor = promocionDao.existeDescuentoReferenciaPresentacionCreacionValor(
								promocion.getTipo().getId(), null, null, idSede);

					if (valorDescuentoMenor == null) {
						valorDescuentoMenor = 0.0f;
					}

					if (valorDescuentoMenor <= valorDescuentoActual && valorDescuentoActual <= valorDescuentoMayor) {

						promocion.setPresentacion(null);

						promocionDao.save(promocion);

						respuesta = ResponseEntity.ok(HttpStatus.OK);
						RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "creación de la promocion exitosa");
						respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
					} else {

						respuesta = ResponseEntity.ok(HttpStatus.OK);
						RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "descuento_invalido");
						respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
					}
				} else {

					respuesta = ResponseEntity.ok(HttpStatus.OK);
					RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "descuento_existe");
					respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
				}
			}

		} catch (Exception e) {

			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error guardando promocion");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerPromocionTextoBusqueda(String texto) {
		ResponseEntity<Object> respuesta;
		try {
			List<Promocion> promociones = promocionDao.obtenerPromocionesTextoBusqueda(texto);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito al obtener las promociones promocion");
			respuestaDto.setObjetoRespuesta(promociones);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {

			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al obtener las promociones");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerPromocionFiltros(String fechaInicial, String fechaFinal, String idPromocion,
			Integer page) {
		ResponseEntity<Object> respuesta;
		Pageable paging = PageRequest.of(page, 10);

		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		int idSede = usuarioInformacion.getIdOrganizacion().getId();

		try {
			List<Promocion> promociones;
			StringBuilder query = new StringBuilder();
			StringBuilder queryCantidad = new StringBuilder();
			generarQueryPromocionFiltros(fechaInicial, fechaFinal, idPromocion, query, idSede);

			TypedQuery<Promocion> promocionInfoQuery = (TypedQuery<Promocion>) entityManager
					.createNativeQuery(query.toString(), Promocion.class);
			int pageNumber = paging.getPageNumber();
			int pageSize = paging.getPageSize();
			promocionInfoQuery.setFirstResult(pageNumber * pageSize);
			promocionInfoQuery.setMaxResults(pageSize);
			promociones = promocionInfoQuery.getResultList();
			generarQueryCantidadPromocionFiltros(fechaInicial, fechaFinal, idPromocion, queryCantidad, idSede);

			Query cantidad = entityManager.createNativeQuery(queryCantidad.toString());
			BigInteger countResult = (BigInteger) cantidad.getSingleResult();
			Integer cantidadTotal = countResult.intValue();
			Page<Promocion> result = new PageImpl<Promocion>(promociones, paging, cantidadTotal);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de productos exitosa");
			respuestaDto.setObjetoRespuesta(result);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {

			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al obtener las promociones");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	public void generarQueryPromocionFiltros(String fechaInicial, String fechaFinal, String promocion,
			StringBuilder query, Integer idSede) {
		Promocion promo = null;

		query.append("select * from prd_promociones");
		if (!fechaInicial.equals("null") && fechaFinal.equals("null")) {
			query.append(" where date_format(date(fecha_inicio),'%Y-%m-%d')" + "= date('" + fechaInicial + "')");
		} else if (fechaInicial.equals("null") && !fechaFinal.equals("null")) {
			query.append(" where date_format(date(fecha_inicio),'%Y-%m-%d')" + "<= date('" + fechaFinal + "')");
		} else if (!fechaInicial.equals("null") && !fechaFinal.equals("null")) {
			query.append(" where date_format(date(fecha_inicio),'%Y-%m-%d')" + " between " + "date('" + fechaInicial
					+ "') and " + "date('" + fechaFinal + "')");
		}
		if (!promocion.equals("null") && (!fechaInicial.equals("null") || !fechaFinal.equals("null"))) {
			query.append(" and nombre like '%" + promocion + "%'");
		} else if (!promocion.equals("null")) {
			query.append(" where nombre like '%" + promocion + "%'");
		}
		if (idSede != 0 && (!promocion.equals("null") || !fechaInicial.equals("null") || !fechaFinal.equals("null"))) {
			query.append(" and id_sede =" + idSede);
		} else if (idSede != 0) {
			query.append(" where id_sede =" + idSede);
		}
		logger.info(query);
	}

	public void generarQueryCantidadPromocionFiltros(String fechaInicial, String fechaFinal, String promocion,
			StringBuilder query, Integer idSede) {
		query.append("select count(*) from prd_promociones");
		if (!fechaInicial.equals("null") && fechaFinal.equals("null")) {
			query.append(" where date_format(date(fecha_inicio),'%Y-%m-%d')" + "= date('" + fechaInicial + "')");
		} else if (fechaInicial.equals("null") && !fechaFinal.equals("null")) {
			query.append(" where date_format(date(fecha_inicio),'%Y-%m-%d')" + "<= date('" + fechaFinal + "')");
		} else if (!fechaInicial.equals("null") && !fechaFinal.equals("null")) {
			query.append(" where date_format(date(fecha_inicio),'%Y-%m-%d')" + " between " + "date('" + fechaInicial
					+ "') and " + "date('" + fechaFinal + "')");
		}
		if (!promocion.equals("null") && (!fechaInicial.equals("null") || !fechaFinal.equals("null"))) {
			query.append(" and nombre like '%" + promocion + "%'");
		} else if (!promocion.equals("null")) {
			query.append(" where nombre like '%" + promocion + "%'");
		}
		if (idSede != 0 && (!promocion.equals("null") || !fechaInicial.equals("null") || !fechaFinal.equals("null"))) {
			query.append(" and id_sede =" + idSede);
		} else if (idSede != 0) {
			query.append(" where id_sede =" + idSede);
		}
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> elimnarPromocion(Integer idPromocion) {
		ResponseEntity<Object> respuesta;
		try {
			promocionDao.deleteById(idPromocion);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito al eliminar la promocion");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {

			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Exito al eliminar la promocion");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerPromocionFiltrosExport(String fechaInicial, String fechaFinal,
			String idPromocion) {
		ResponseEntity<Object> respuesta;
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		int idSede = usuarioInformacion.getIdOrganizacion().getId();
		try {
			List<Promocion> promociones;
			StringBuilder query = new StringBuilder();
			generarQueryPromocionFiltros(fechaInicial, fechaFinal, idPromocion, query, idSede);

			TypedQuery<Promocion> promocionInfoQuery = (TypedQuery<Promocion>) entityManager
					.createNativeQuery(query.toString(), Promocion.class);
			promociones = promocionInfoQuery.getResultList();
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de productos exitosa");
			respuestaDto.setObjetoRespuesta(promociones);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al obtener las promociones");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerPromocionDia(Integer idTipo, Integer idReferencia, Integer idPresentacion,
			String diaActual) {

		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		int idSede = usuarioInformacion.getIdOrganizacion().getId();

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		try {
			Float resultado = obtenerPromocion(idTipo, idReferencia, idPresentacion, diaActual, idSede);
			logger.info(resultado);
			logger.info(diaActual);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de descuento exitosa");
			respuestaDto.setObjetoRespuesta(resultado);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error("Error al listar las promociones " + e.getMessage());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo obtener la promocion");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	private Float obtenerPromocion(Integer idTipo, Integer idReferencia, Integer idPresentacion, String diaActual,
			Integer idSede) {
		logger.info(diaActual);
		Float descuento = 0.0f;
		Float descuentoAux = 0.0f;
		Query descuentoInfoQuery;
		StringBuilder query = new StringBuilder();
		query.append("select max(p.descuento) from prd_promociones p where tipo_id=" + idTipo + " and referencia_id="
				+ idReferencia + " and presentacion_id=" + idPresentacion + " and activo=1 and " + diaActual + "=1 and id_sede="
				+ idSede);
		logger.info(query);
		descuentoInfoQuery = entityManager.createNativeQuery(query.toString());

		if (descuentoInfoQuery.getResultStream().count() == 1) {
			descuento = (Float) descuentoInfoQuery.getSingleResult();
		}
		if (descuento != null) {

			descuentoAux = descuento;
			return descuentoAux;
		} else {
			StringBuilder query1 = new StringBuilder();
			query1.append("select max(p.descuento) from prd_promociones p where tipo_id=" + idTipo
					+ " and referencia_id=" + idReferencia + " and presentacion_id is " + null + " and activo=1 and "
					+ diaActual + "=1 and id_sede=" + idSede);
			logger.info(query1);
			descuentoInfoQuery = entityManager.createNativeQuery(query1.toString());

			if (descuentoInfoQuery.getResultStream().count() == 1) {
				descuento = (Float) descuentoInfoQuery.getSingleResult();

			}
			if (descuento != null) {

				descuentoAux = descuento;
				return descuentoAux;
			} else {
				StringBuilder query2 = new StringBuilder();
				query2
						.append("select max(p.descuento) from prd_promociones p where tipo_id=" + idTipo + " and referencia_id is "
								+ null + " and presentacion_id is " + null + " and activo=1 and " + diaActual + "=1 and id_sede="
								+ idSede);
				logger.info(query2);
				descuentoInfoQuery = entityManager.createNativeQuery(query2.toString());

				if (descuentoInfoQuery.getResultStream().count() == 1) {
					descuento = (Float) descuentoInfoQuery.getSingleResult();

				}
				if (descuento != null) {

					descuentoAux = descuento;
					return descuentoAux;
				} else {
					StringBuilder query3 = new StringBuilder();
					query3.append("select max(p.descuento) from prd_promociones p where tipo_id is " + null
							+ " and referencia_id is " + null + " and presentacion_id is " + null + " and activo=1 and "
							+ diaActual + "=1 and id_sede=" + idSede);
					logger.info(query3);
					descuentoInfoQuery = entityManager.createNativeQuery(query3.toString());

					if (descuentoInfoQuery.getResultStream().count() == 1) {
						descuento = (Float) descuentoInfoQuery.getSingleResult();

					}
					if (descuento != null) {

						descuentoAux = descuento;
						return descuentoAux;
					}

				}

			}

		}

		return descuentoAux;
	}

}
