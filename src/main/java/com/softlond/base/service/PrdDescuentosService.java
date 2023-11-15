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
import com.softlond.base.entity.Clientes;
import com.softlond.base.entity.PrdDescuentos;
import com.softlond.base.entity.PrdPresentacion;
import com.softlond.base.entity.Producto;
import com.softlond.base.repository.PrdDescuentosDao;
import com.softlond.base.repository.PrdPresentacionDao;
import com.softlond.base.repository.PrdReferenciaDao;
import com.softlond.base.repository.PrdTipoDao;
import com.softlond.base.repository.ProductoDao;
import com.softlond.base.entity.PrdReferencia;
import com.softlond.base.entity.PrdTipos;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.softlond.base.entity.InformacionUsuario;
import com.softlond.base.entity.Usuario;
import com.softlond.base.repository.UsuarioInformacionDao;

@Service
public class PrdDescuentosService {

	private static final Logger logger = Logger.getLogger(PrdDescuentosService.class);

	@Autowired
	private PrdDescuentosDao prdDescuentosDao;

	@Autowired
	private ProductoDao productoDao;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private PrdReferenciaDao refDao;

	@Autowired
	UsuarioInformacionDao usuarioInformacionDao;

	@Autowired
	private PrdTipoDao tipoDao;
	@Autowired
	private PrdPresentacionDao presentacionDao;

	// listar Descuentos
	@PreAuthorize("hasAuthority('USER') or hasAuthority('SUPER')  or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerDescuentos() {

		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		int idSede = usuarioInformacion.getIdOrganizacion().getId();

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		try {

			ArrayList<PrdDescuentos> descuento = this.prdDescuentosDao.obtenerDescuentos();

			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de descuentos exitosa");
			respuestaDto.setObjetoRespuesta(descuento);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo descuento " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error obteniendo descuento");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	// Listar todos los descuentos por paginado
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerTodosDescuentos(Integer page) {
		ResponseEntity<Object> respuesta;
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		int idSede = usuarioInformacion.getIdOrganizacion().getId();
		try {
			Pageable pageConfig = PageRequest.of(page, 10);
			Page<PrdDescuentos> descuentos = prdDescuentosDao.obtenerTodosDescuentos(pageConfig);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de descuentos exitosa");
			respuestaDto.setObjetoRespuesta(descuentos);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error("Error al listar los descuentos" + e.getMessage());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"No se pudo obtener los descuentos");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	// Crear descuento
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> crearDescuento(PrdDescuentos descuentos) {
		ResponseEntity<Object> respuesta = null;
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		int idSede = usuarioInformacion.getIdOrganizacion().getId();

		// descuentos.setSede();
		try {
			if (descuentos.getTipo().getId() != null && descuentos.getReferencia().getId() != null
					&& descuentos.getIdPresentacion().getId() != null) {
				if (prdDescuentosDao.existeDescuentoTipoReferenciaPresentacionCreacion(descuentos.getIdClasificacion().getId(),
						descuentos.getTipo().getId(), descuentos.getReferencia().getId(),
						descuentos.getIdPresentacion().getId()) <= 0) {
					Float valorDescuentoMayor = 100.0f;
					Float valorDescuentoActual = 0.0f;
					Float valorDescuentoMenor = 0.0f;

					valorDescuentoMenor = prdDescuentosDao.existeDescuentoPresentacionCreacionValor(
							descuentos.getIdClasificacion().getId(),
							descuentos.getTipo().getId(), descuentos.getReferencia().getId(),
							null);
					if (valorDescuentoMenor == null) {
						valorDescuentoMenor = 0.0f;
					}
					valorDescuentoActual = descuentos.getDescuento();
					if (valorDescuentoActual == null) {
						valorDescuentoActual = 0.0f;
					}

					if (valorDescuentoActual > valorDescuentoMenor) {

						descuentos.setFechaCreacion(new Date());
						descuentos.setFechaModificacion(new Date());
						descuentos.setTipo(descuentos.getTipo());
						descuentos.setReferencia(descuentos.getReferencia());
						descuentos.setIdPresentacion(descuentos.getIdPresentacion());

						prdDescuentosDao.save(descuentos);

						respuesta = ResponseEntity.ok(HttpStatus.OK);
						RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "creación del descuento exitoso");
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
				} // 1)ya

			} else if (descuentos.getTipo().getId() == null && descuentos.getReferencia().getId() == null
					&& descuentos.getIdPresentacion().getId() == null) {
				if (prdDescuentosDao.existeDescuentoTodosCreacion(descuentos.getIdClasificacion().getId(),
						descuentos.getTipo().getId(), descuentos.getReferencia().getId(),
						descuentos.getIdPresentacion().getId()) <= 0) {
					Float valorDescuentoMayor = 0.0f;
					Float valorDescuentoActual = 0.0f;
					Float valorDescuentoMenor = 0.0f;

					valorDescuentoMayor = prdDescuentosDao.descuentoMenorCreacionValor(descuentos.getIdClasificacion().getId());
					if (valorDescuentoMayor == null) {
						valorDescuentoMayor = 100.0f;
					}
					valorDescuentoActual = descuentos.getDescuento();
					if (valorDescuentoActual == null) {
						valorDescuentoActual = 0.0f;
					}

					if (valorDescuentoActual < valorDescuentoMayor) {

						descuentos.setFechaCreacion(new Date());
						descuentos.setFechaModificacion(new Date());
						descuentos.setTipo(null);
						descuentos.setReferencia(null);
						descuentos.setIdPresentacion(null);

						prdDescuentosDao.save(descuentos);

						respuesta = ResponseEntity.ok(HttpStatus.OK);
						RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "creación del descuento exitoso");
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
				} // 2)ya

			} else if (descuentos.getReferencia().getId() == null && descuentos.getIdPresentacion().getId() == null) {
				if (prdDescuentosDao.existeDescuentoReferenciaPresentacionCreacion(descuentos.getIdClasificacion().getId(),
						descuentos.getTipo().getId(), descuentos.getReferencia().getId(),
						descuentos.getIdPresentacion().getId()) <= 0) {
					Float valorDescuentoMayor = 0.0f;
					Float valorDescuentoActual = 0.0f;
					Float valorDescuentoMenor = 0.0f;

					valorDescuentoActual = descuentos.getDescuento();
					if (valorDescuentoActual == null) {
						valorDescuentoActual = 0.0f;
					}
					valorDescuentoMenor = prdDescuentosDao
							.existeDescuentoTodosCreacionValor(descuentos.getIdClasificacion().getId(), null, null, null);
					if (valorDescuentoMenor == null) {
						valorDescuentoMenor = 0.0f;
					}
					valorDescuentoMayor = prdDescuentosDao.existeDescuentoNPresentacionCreacionValor(
							descuentos.getIdClasificacion().getId(),
							descuentos.getTipo().getId(), null);
					if (valorDescuentoMayor == null) {
						valorDescuentoMayor = 100.0f;
					}

					if (valorDescuentoMenor < valorDescuentoActual && valorDescuentoActual < valorDescuentoMayor) {

						descuentos.setFechaCreacion(new Date());
						descuentos.setFechaModificacion(new Date());
						descuentos.setReferencia(null);
						descuentos.setIdPresentacion(null);

						prdDescuentosDao.save(descuentos);

						respuesta = ResponseEntity.ok(HttpStatus.OK);
						RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "creación del descuento exitoso");
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

			} else if (descuentos.getIdPresentacion().getId() == null) {
				if (prdDescuentosDao.existeDescuentoPresentacionCreacion(descuentos.getIdClasificacion().getId(),
						descuentos.getTipo().getId(), descuentos.getReferencia().getId(),
						descuentos.getIdPresentacion().getId()) <= 0) {
					Float valorDescuentoMayor = 0.0f;
					Float valorDescuentoActual = 0.0f;
					Float valorDescuentoMenor = 0.0f;

					valorDescuentoMayor = prdDescuentosDao.existeDescuentoTipoReferenciaPresentacionCreacionValor(
							descuentos.getIdClasificacion().getId(),
							descuentos.getTipo().getId(), descuentos.getReferencia().getId());
					if (valorDescuentoMayor == null) {
						valorDescuentoMayor = 100.0f;
					}
					valorDescuentoActual = descuentos.getDescuento();
					if (valorDescuentoActual == null) {
						valorDescuentoActual = 0.0f;
					}
					valorDescuentoMenor = prdDescuentosDao.existeDescuentoReferenciaPresentacionCreacionValor(
							descuentos.getIdClasificacion().getId(),
							descuentos.getTipo().getId(), null,
							null);
					if (valorDescuentoMenor == null) {
						valorDescuentoMenor = 0.0f;
					}

					if (valorDescuentoMenor < valorDescuentoActual && valorDescuentoActual < valorDescuentoMayor) {

						descuentos.setFechaCreacion(new Date());
						descuentos.setFechaModificacion(new Date());
						descuentos.setIdPresentacion(null);

						prdDescuentosDao.save(descuentos);

						respuesta = ResponseEntity.ok(HttpStatus.OK);
						RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "creación del descuento exitoso");
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
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error("Error creando el descuento " + e.getMessage() + " linea # "
					+ e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"No se pudo crear el descuento");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	// --------------------------------------------------------------------------------------

	// Editar descuento
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> actualizarDescuento(PrdDescuentos descuentos) {
		ResponseEntity<Object> respuesta = null;
		PrdTipos tipo = null;
		PrdReferencia referencia = null;
		PrdPresentacion presentacion = null;
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		int idSede = usuarioInformacion.getIdOrganizacion().getId();
		// descuentos.setSede(idSede);
		if (descuentos.getTipo().getId() != null) {
			tipo = tipoDao.findById(descuentos.getTipo().getId()).get();
		} else {

		}
		if (descuentos.getReferencia().getId() != null) {
			referencia = refDao.findById(descuentos.getReferencia().getId()).get();
		} else {

		}
		if (descuentos.getIdPresentacion().getId() != null) {
			presentacion = presentacionDao.findById(descuentos.getIdPresentacion().getId()).get();
		} else {

		}

		try {
			if (descuentos.getTipo().getId() != null && descuentos.getReferencia().getId() != null
					&& descuentos.getIdPresentacion().getId() != null) {
				if (prdDescuentosDao.existeDescuentoTipoReferenciaPresentacion(descuentos.getId(),
						descuentos.getIdClasificacion().getId(),
						descuentos.getTipo().getId(), descuentos.getReferencia().getId(),
						descuentos.getIdPresentacion().getId()) <= 0) {
					Float valorDescuentoMayor = 100.0f;
					Float valorDescuentoActual = 0.0f;
					Float valorDescuentoMenor = 0.0f;

					valorDescuentoMenor = prdDescuentosDao.existeDescuentoPresentacionEdicionValor(descuentos.getId(),
							descuentos.getIdClasificacion().getId(),
							descuentos.getTipo().getId(), descuentos.getReferencia().getId(),
							null);// ? se podria implementar para que se obtenga con el mismo metodo que verifica
					// la existencia del descuento
					if (valorDescuentoMenor == null) {
						valorDescuentoMenor = 0.0f;
					}
					valorDescuentoActual = descuentos.getDescuento();
					if (valorDescuentoActual == null) {
						valorDescuentoActual = 0.0f;
					}

					if (valorDescuentoActual > valorDescuentoMenor) {

						descuentos.setFechaCreacion(new Date());
						descuentos.setFechaModificacion(new Date());
						descuentos.setTipo(descuentos.getTipo());
						descuentos.setReferencia(descuentos.getReferencia());
						descuentos.setIdPresentacion(descuentos.getIdPresentacion());

						prdDescuentosDao.save(descuentos);

						respuesta = ResponseEntity.ok(HttpStatus.OK);
						RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "creación del descuento exitoso");
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
			} else if (descuentos.getTipo().getId() == null && descuentos.getReferencia().getId() == null
					&& descuentos.getIdPresentacion().getId() == null) {
				if (prdDescuentosDao.existeDescuentoTodos(descuentos.getId(), descuentos.getIdClasificacion().getId(),
						descuentos.getTipo().getId(), descuentos.getReferencia().getId(),
						descuentos.getIdPresentacion().getId()) <= 0) {

					Float valorDescuentoMayor = 0.0f;
					Float valorDescuentoActual = 0.0f;
					Float valorDescuentoMenor = 0.0f;

					valorDescuentoMayor = prdDescuentosDao.descuentoMenorEdicionValor(descuentos.getId(),
							descuentos.getIdClasificacion().getId());
					if (valorDescuentoMayor == null) {
						valorDescuentoMayor = 100.0f;
					}
					valorDescuentoActual = descuentos.getDescuento();
					if (valorDescuentoActual == null) {
						valorDescuentoActual = 0.0f;
					}

					if (valorDescuentoActual < valorDescuentoMayor) {

						descuentos.setFechaCreacion(new Date());
						descuentos.setFechaModificacion(new Date());
						descuentos.setTipo(null);
						descuentos.setReferencia(null);
						descuentos.setIdPresentacion(null);

						prdDescuentosDao.save(descuentos);

						respuesta = ResponseEntity.ok(HttpStatus.OK);
						RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "creación del descuento exitoso");
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
				} // 2)ya
			} else if (descuentos.getReferencia().getId() == null && descuentos.getIdPresentacion().getId() == null) {
				if (prdDescuentosDao.existeDescuentoReferenciaPresentacion(descuentos.getId(),
						descuentos.getIdClasificacion().getId(),
						descuentos.getTipo().getId(), descuentos.getReferencia().getId(),
						descuentos.getIdPresentacion().getId()) <= 0) {

					Float valorDescuentoMayor = 0.0f;
					Float valorDescuentoActual = 0.0f;
					Float valorDescuentoMenor = 0.0f;

					valorDescuentoActual = descuentos.getDescuento();
					if (valorDescuentoActual == null) {
						valorDescuentoActual = 0.0f;
					}
					valorDescuentoMenor = prdDescuentosDao.existeDescuentoTodosEdicionValor(descuentos.getId(),
							descuentos.getIdClasificacion().getId(),
							null, null,
							null);
					if (valorDescuentoMenor == null) {
						valorDescuentoMenor = 0.0f;
					}
					valorDescuentoMayor = prdDescuentosDao.existeDescuentoNPresentacionEdicionValor(descuentos.getId(),
							descuentos.getIdClasificacion().getId(),
							descuentos.getTipo().getId(), null);
					if (valorDescuentoMayor == null) {
						valorDescuentoMayor = 100.0f;
					}

					if (valorDescuentoMenor < valorDescuentoActual && valorDescuentoActual < valorDescuentoMayor) {

						descuentos.setFechaCreacion(new Date());
						descuentos.setFechaModificacion(new Date());
						descuentos.setReferencia(null);
						descuentos.setIdPresentacion(null);

						prdDescuentosDao.save(descuentos);

						respuesta = ResponseEntity.ok(HttpStatus.OK);
						RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "creación del descuento exitoso");
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
				} // 3)ya
			} else if (descuentos.getIdPresentacion().getId() == null) {
				if (prdDescuentosDao.existeDescuentoPresentacion(descuentos.getId(), descuentos.getIdClasificacion().getId(),
						descuentos.getTipo().getId(), descuentos.getReferencia().getId(),
						descuentos.getIdPresentacion().getId()) <= 0) {

					Float valorDescuentoMayor = 0.0f;
					Float valorDescuentoActual = 0.0f;
					Float valorDescuentoMenor = 0.0f;
					// Integer valorDescuentoMayor = 0;
					// Integer valorDescuentoActual = 0;
					// Integer valorDescuentoMenor = 0;

					valorDescuentoMayor = prdDescuentosDao.existeDescuentoTipoReferenciaPresentacionEdicionValor(
							descuentos.getId(), descuentos.getIdClasificacion().getId(), descuentos.getTipo().getId(),
							descuentos.getReferencia().getId());
					if (valorDescuentoMayor == null) {
						valorDescuentoMayor = 100.0f;
					}
					valorDescuentoActual = descuentos.getDescuento();
					if (valorDescuentoActual == null) {
						valorDescuentoActual = 0.0f;
					}
					valorDescuentoMenor = prdDescuentosDao.existeDescuentoReferenciaPresentacionEdicionValor(descuentos.getId(),
							descuentos.getIdClasificacion().getId(), descuentos.getTipo().getId(), null,
							null);
					if (valorDescuentoMenor == null) {
						valorDescuentoMenor = 0.0f;
					}

					if (valorDescuentoMenor < valorDescuentoActual && valorDescuentoActual < valorDescuentoMayor) {

						descuentos.setFechaCreacion(new Date());
						descuentos.setFechaModificacion(new Date());
						descuentos.setIdPresentacion(null);

						prdDescuentosDao.save(descuentos);

						respuesta = ResponseEntity.ok(HttpStatus.OK);
						RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "creación del descuento exitoso");
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
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error("Error actualizando cliente" + e.getMessage());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"No se pudo actualizar el cliente");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	// Eliminar descuento
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> eliminarDescuento(Integer idDescuento) {
		ResponseEntity<Object> respuesta;

		try {
			prdDescuentosDao.deleteById(idDescuento);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "eliminación de descuento exitosa");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);

		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error("Error al eliminar el cliente" + e.getMessage());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"No se pudo eliminar un descuento");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	// Consulta avanzada de descuentos
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerDescuentoConsulta(Integer cliente, String activo, Integer page) {
		ResponseEntity<Object> respuesta;
		Pageable paging = PageRequest.of(page, 10);
		try {
			List<PrdDescuentos> descuento;
			StringBuilder query = new StringBuilder();
			StringBuilder queryCantidad = new StringBuilder();
			generarQuery(query, cliente, activo);
			logger.warn(query);
			TypedQuery<PrdDescuentos> descuentosInfoQuery = (TypedQuery<PrdDescuentos>) entityManager
					.createNativeQuery(query.toString(), PrdDescuentos.class);
			int pageNumber = paging.getPageNumber();
			int pageSize = paging.getPageSize();
			descuentosInfoQuery.setFirstResult(pageNumber * pageSize);
			descuentosInfoQuery.setMaxResults(pageSize);
			descuento = descuentosInfoQuery.getResultList();
			generarQueryCantidad(queryCantidad, cliente, activo);
			Query cantidad = entityManager.createNativeQuery(queryCantidad.toString());
			BigInteger countResult = (BigInteger) cantidad.getSingleResult();
			Integer cantidadTotal = countResult.intValue();
			Page<PrdDescuentos> result = new PageImpl<PrdDescuentos>(descuento, paging, cantidadTotal);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de descuento exitosa");
			respuestaDto.setObjetoRespuesta(result);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo descuento " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error obteniendo descuento");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	private void generarQuery(StringBuilder query, Integer cliente, String activo) {
		query.append(
				"select * from prd_descuentos pd inner join config_ter_clasificaciones co on co.id  = pd.id_clasificacion inner join clientes cl on cl.id_clasificacion = co.id  where");
		if (cliente != 0) {
			query.append(" cl.id=" + "'" + cliente + "'");
		}

		if (!activo.equals("null") && cliente != 0) {
			query.append(" and activo=" + activo);
		} else if (!activo.equals("null")) {
			query.append(" activo=" + activo);
		}

	}

	private void generarQueryCantidad(StringBuilder query, Integer cliente, String activo) {
		query.append(
				"SELECT count(*) from prd_descuentos pd inner join config_ter_clasificaciones co on co.id  = pd.id_clasificacion inner join clientes cl on cl.id_clasificacion = co.id  where");
		if (cliente != 0) {
			query.append(" cl.id=" + "'" + cliente + "'");
		}

		if (!activo.equals("null") && cliente != 0) {
			query.append(" and activo=" + activo);
		} else if (!activo.equals("null")) {
			query.append(" activo=" + activo);
		}
	}

	// Consulta avanzada de descuentos
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerDescuentosConsultas(Integer page, Integer clasificacion, String activo,
			Integer tipo, Integer referencia) {
		ResponseEntity<Object> respuesta;
		Pageable paging = PageRequest.of(page, 10);
		try {
			List<PrdDescuentos> descuento;
			StringBuilder query = new StringBuilder();
			StringBuilder queryCantidad = new StringBuilder();
			generarQueryFiltros(query, clasificacion, activo, tipo, referencia);
			TypedQuery<PrdDescuentos> descuentosInfoQuery = (TypedQuery<PrdDescuentos>) entityManager
					.createNativeQuery(query.toString(), PrdDescuentos.class);
			int pageNumber = paging.getPageNumber();
			int pageSize = paging.getPageSize();
			descuentosInfoQuery.setFirstResult(pageNumber * pageSize);
			descuentosInfoQuery.setMaxResults(pageSize);
			descuento = descuentosInfoQuery.getResultList();
			generarQueryFiltrosCantidad(queryCantidad, clasificacion, activo, tipo, referencia);
			Query cantidad = entityManager.createNativeQuery(queryCantidad.toString());
			BigInteger countResult = (BigInteger) cantidad.getSingleResult();
			Integer cantidadTotal = countResult.intValue();
			Page<PrdDescuentos> result = new PageImpl<PrdDescuentos>(descuento, paging, cantidadTotal);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de descuento exitosa");
			respuestaDto.setObjetoRespuesta(result);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo descuento " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error obteniendo descuento");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	private void generarQueryFiltros(StringBuilder query, Integer clasificacion, String activo, Integer tipo,
			Integer referencia) {
		query.append("select * from prd_descuentos");
		if (clasificacion != 0) {
			query.append(" where id_clasificacion=" + clasificacion);
		}
		if (!activo.equals("null") && clasificacion != 0) {
			query.append(" and estado=" + activo);
		} else if (!activo.equals("null")) {
			query.append(" where estado=" + activo);
		}
		if (tipo != 0 && (clasificacion != 0 || !activo.equals("null"))) {
			query.append(" and tipo=" + tipo);
		} else if (tipo != 0) {
			query.append(" where tipo=" + tipo);
		}
		if (referencia != 0 && (clasificacion != 0 || !activo.equals("null") || tipo != 0)) {
			query.append(" and tipo=" + tipo);
		} else if (referencia != 0) {
			query.append(" where tipo=" + tipo);
		}

	}

	private void generarQueryFiltrosCantidad(StringBuilder query, Integer clasificacion, String activo, Integer tipo,
			Integer referencia) {
		query.append("select count(*) from prd_descuentos where");
		if (clasificacion != 0) {
			query.append(" id_clasificacion=" + clasificacion);
		}
		if (!activo.equals("null") && clasificacion != 0) {
			query.append(" and estado=" + activo);
		} else if (!activo.equals("null")) {
			query.append(" estado=" + activo);
		}
		if (tipo != 0 && (clasificacion != 0 || !activo.equals("null"))) {
			query.append(" and tipo=" + activo);
		} else if (tipo != 0) {
			query.append(" tipo=" + activo);
		}
		if (referencia != 0 && (clasificacion != 0 || !activo.equals("null") || tipo != 0)) {
			query.append(" and tipo=" + activo);
		} else if (referencia != 0) {
			query.append(" tipo=" + activo);
		}
	}

	// Listar todos los descuentos por paginado
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerTodosDescuentosProducto(Integer page, Integer idProducto) {
		ResponseEntity<Object> respuesta;
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		int idSede = usuarioInformacion.getIdOrganizacion().getId();

		try {
			Pageable pageConfig = PageRequest.of(page, 10);
			Page<PrdDescuentos> descuentos = prdDescuentosDao.obtenerTodosDescuentosProducto(pageConfig, idProducto);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de descuentos exitosa");
			respuestaDto.setObjetoRespuesta(descuentos);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error("Error al listar los descuentos" + e.getMessage());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"No se pudo obtener los descuentos");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	// Consulta avanzada de descuentos
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerDescuentosConsultasProducto(Integer page, Integer clasificacion, String activo,
			Integer tipo, Integer referencia, Integer producto) {
		ResponseEntity<Object> respuesta;
		Pageable paging = PageRequest.of(page, 10);

		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		int idSede = usuarioInformacion.getIdOrganizacion().getId();

		try {
			List<PrdDescuentos> descuento;
			StringBuilder query = new StringBuilder();
			StringBuilder queryCantidad = new StringBuilder();
			generarQueryFiltrosProducto(query, clasificacion, activo, tipo, referencia, producto);

			TypedQuery<PrdDescuentos> descuentosInfoQuery = (TypedQuery<PrdDescuentos>) entityManager
					.createNativeQuery(query.toString(), PrdDescuentos.class);
			int pageNumber = paging.getPageNumber();
			int pageSize = paging.getPageSize();
			descuentosInfoQuery.setFirstResult(pageNumber * pageSize);
			descuentosInfoQuery.setMaxResults(pageSize);
			descuento = descuentosInfoQuery.getResultList();
			generarQueryFiltrosProductoCantidad(queryCantidad, clasificacion, activo, tipo, referencia, producto);
			Query cantidad = entityManager.createNativeQuery(queryCantidad.toString());
			BigInteger countResult = (BigInteger) cantidad.getSingleResult();
			Integer cantidadTotal = countResult.intValue();
			Page<PrdDescuentos> result = new PageImpl<PrdDescuentos>(descuento, paging, cantidadTotal);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de descuento exitosa");
			respuestaDto.setObjetoRespuesta(result);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo descuento " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error obteniendo descuento");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return respuesta;
	}

	private void generarQueryFiltrosProducto(StringBuilder query, Integer clasificacion, String activo, Integer tipo,
			Integer referencia, Integer producto) {

		query.append("select * from prd_descuentos d");
		if (clasificacion != 0) {
			query.append(" where d.id_clasificacion=" + clasificacion);
		}
		if (!activo.equals("null") && clasificacion != 0) {
			query.append(" and d.estado=" + activo);
		} else if (!activo.equals("null")) {
			query.append(" where d.estado=" + activo);
		}
		if (tipo != 0 && (clasificacion != 0 || !activo.equals("null"))) {
			query.append(" and d.tipo=" + tipo);
		} else if (tipo != 0) {
			query.append(" where d.tipo=" + tipo);
		}
		if (referencia != 0 && (clasificacion != 0 || !activo.equals("null") || tipo != 0)) {
			query.append(" and d.referencia=" + referencia);
		} else if (referencia != 0) {
			query.append(" where d.referencia=" + referencia);
		}
		// if (idSede != 0 && (referencia != 0 || clasificacion != 0 ||
		// !activo.equals("null") || tipo != 0)) {
		// query.append(" and id_sede =" + idSede);
		// } else if (idSede != 0) {
		// query.append(" where id_sede =" + idSede);
		// }
		query.append(" group by d.id");
	}

	private void generarQueryFiltrosProductoCantidad(StringBuilder query, Integer clasificacion, String activo,
			Integer tipo, Integer referencia, Integer producto) {
		query.append("select count(*) from prd_descuentos d");
		if (clasificacion != 0) {
			query.append(" where d.id_clasificacion=" + clasificacion);
		}
		if (!activo.equals("null") && clasificacion != 0) {
			query.append(" and d.estado=" + activo);
		} else if (!activo.equals("null")) {
			query.append(" where d.estado=" + activo);
		}
		if (tipo != 0 && (clasificacion != 0 || !activo.equals("null"))) {
			query.append(" and d.tipo=" + tipo);
		} else if (tipo != 0) {
			query.append(" where d.tipo=" + tipo);
		}
		if (referencia != 0 && (clasificacion != 0 || !activo.equals("null") || tipo != 0)) {
			query.append(" and d.referencia=" + referencia);
		} else if (referencia != 0) {
			query.append(" where d.referencia=" + referencia);
		}
		// if (idSede != 0 && (referencia != 0 || clasificacion != 0 ||
		// !activo.equals("null") || tipo != 0)) {
		// query.append(" and id_sede =" + idSede);
		// } else if (idSede != 0) {
		// query.append(" where id_sede =" + idSede);
		// }
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerPorClienteClasificacion(Integer idClasificacion, Integer idTipo,
			Integer idReferencia, Integer idPresentacion) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		try {
			Float resultado = obtenerDescuento(idClasificacion, idTipo, idReferencia, idPresentacion);
			logger.info(resultado);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de descuento exitosa");
			respuestaDto.setObjetoRespuesta(resultado);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error("Error al listar los descuentos " + e.getMessage());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"No se pudo obtener el descuento");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	private Float obtenerDescuento(Integer idClasificacion, Integer idTipo, Integer idReferencia,
			Integer idPresentacion) {
		Float descuento = 0.0f;
		Float descuentoAux = 0.0f;
		Query descuentoInfoQuery;

		if (idTipo != null && idReferencia != null && idPresentacion != null) { // ingreso de tipo, presentacion y
			// referencia validos??
			StringBuilder query = new StringBuilder();
			query.append("select max(d.descuento) from prd_descuentos d where id_clasificacion=" + idClasificacion
					+ " and tipo=" + idTipo + " and referencia=" + idReferencia + " and id_presentacion="
					+ idPresentacion);
			descuentoInfoQuery = entityManager.createNativeQuery(query.toString());

			if (descuentoInfoQuery.getResultStream().count() == 1) {
				descuento = (Float) descuentoInfoQuery.getSingleResult();
			}
			if (descuento != null) {
				descuentoAux = descuento;
				return descuentoAux;
			} else {
				StringBuilder query1 = new StringBuilder();
				query1.append("select max(d.descuento) from prd_descuentos d where id_clasificacion=" + idClasificacion
						+ " and tipo=" + idTipo + " and referencia=" + idReferencia + " and id_presentacion is "
						+ null);

				descuentoInfoQuery = entityManager.createNativeQuery(query1.toString());
				if (descuentoInfoQuery.getResultStream().count() == 1) {
					descuento = (Float) descuentoInfoQuery.getSingleResult();
				}
				if (descuento != null) {
					descuentoAux = descuento;
					return descuentoAux;
				} else {
					StringBuilder query2 = new StringBuilder();
					query2.append("select max(d.descuento) from prd_descuentos d where id_clasificacion="
							+ idClasificacion + " and tipo=" + idTipo + " and referencia is " + null
							+ " and id_presentacion is " + null);
					descuentoInfoQuery = entityManager.createNativeQuery(query2.toString());

					if (descuentoInfoQuery.getResultStream().count() == 1) {
						descuento = (Float) descuentoInfoQuery.getSingleResult();
					}
					if (descuento != null) {
						descuentoAux = descuento;
						return descuentoAux;
					} else {
						StringBuilder query3 = new StringBuilder();
						query3.append("select max(d.descuento) from prd_descuentos d where id_clasificacion="
								+ idClasificacion + " and tipo is " + null + " and referencia is " + null
								+ " and id_presentacion is " + null);
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

		}
		return descuentoAux;

	}

}
