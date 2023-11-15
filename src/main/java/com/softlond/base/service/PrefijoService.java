package com.softlond.base.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.Organizacion;
import com.softlond.base.entity.Prefijo;
import com.softlond.base.entity.Usuario;
import com.softlond.base.repository.OrganizacionDao;
import com.softlond.base.repository.PrefijoDao;
import com.softlond.base.repository.SequenceDao;

@Service
public class PrefijoService {

	private static final Logger logger = Logger.getLogger(PrefijoService.class);

	@Autowired
	private PrefijoDao prefijoDao;

	@Autowired
	OrganizacionDao organizacionDao;

	@Autowired
	private DatosSesionService datosSesion;

	@Autowired
	private SequenceDao sequenceDao;

	// Listar todos los prefijos
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerTodos() {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		try {
			ArrayList<Prefijo> prefijo = this.prefijoDao.findAllByOrderByFechaCreacionDesc();

			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito listando los prefijos");
			respuestaDto.setObjetoRespuesta(prefijo);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error en el listar los prefijos");
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error en el listar los prefijos");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return respuesta;
	}

	// Crear prefijos en el sistema
	// @PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or
	// hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	// public ResponseEntity<Object> crearPrefijo(@RequestBody Prefijo prefijoNuevo)
	// {

	// ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

	// Authentication autenticacion =
	// SecurityContextHolder.getContext().getAuthentication();
	// Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();

	// try {

	// prefijoNuevo.setFechaCreacion(new Date(new java.util.Date().getTime()));
	// prefijoNuevo.setIdCreador(usuarioAutenticado);

	// Prefijo guardado = this.prefijoDao.save(prefijoNuevo);
	// Integer numeroSequencia =
	// sequenceDao.sequenciaNueva(prefijoNuevo.getPrefijo(),
	// prefijoNuevo.getIdSede().getId(), prefijoNuevo.getId());

	// RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito creando el
	// prefijo");
	// respuestaDto.setObjetoRespuesta(guardado);
	// respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);

	// } catch (Exception e) {
	// logger.error("Error en la creación del prefijo");
	// RespuestaDto respuestaDto = new
	// RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
	// "Error en el listar de prefijos");
	// respuesta = new ResponseEntity<>(respuestaDto,
	// HttpStatus.INTERNAL_SERVER_ERROR);
	// }

	// return respuesta;
	// }

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> crearPrefijo(@RequestBody Prefijo prefijoNuevo) {
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();

		try {

			prefijoNuevo.setFechaCreacion(new Date(new java.util.Date().getTime()));
			prefijoNuevo.setIdCreador(usuarioAutenticado);

			Prefijo guardado = this.prefijoDao.save(prefijoNuevo);
			if (prefijoNuevo != null) {
				logger.info("prefijoNuevo.getPrefijo()");
				logger.info(prefijoNuevo.getPrefijo());
				logger.info("prefijoNuevo.getIdSede().getId()");
				logger.info(prefijoNuevo.getIdSede().getId());
				logger.info("prefijoNuevo.getId()");
				logger.info(prefijoNuevo.getId());
			}
			Integer numeroSequencia = sequenceDao.sequenciaNueva(prefijoNuevo.getPrefijo(), prefijoNuevo.getIdSede().getId(),
					prefijoNuevo.getId());
			if (numeroSequencia == -1) {
				// logger.info("no se encuentra la secuencia");
				// numeroSequencia = prefijoNuevo.getValorInicial();
				logger.info("nLast_val es null");// !la secuencia para el prefijo nuevo no existe en la base de datos
			}
			// if (prefijoNuevo != null) {
			// logger.info("prefijoNuevo.getPrefijo()");
			// logger.info(prefijoNuevo.getPrefijo());
			// logger.info("prefijoNuevo.getIdSede().getId()");
			// logger.info(prefijoNuevo.getIdSede().getId());
			// logger.info("prefijoNuevo.getId()");
			// logger.info(prefijoNuevo.getId());
			// }

			Integer sequenciaG = sequenceDao.ObtenerSequencia(prefijoNuevo.getIdSede().getId(), prefijoNuevo.getPrefijo(),
					prefijoNuevo.getId());
			if (sequenciaG != null) {
				logger.info(sequenciaG);
			}
			Integer idSecuencia = sequenceDao.ObtenerSequenciaId(prefijoNuevo.getIdSede().getId(), prefijoNuevo.getPrefijo(),
					prefijoNuevo.getId());
			if (idSecuencia != null) {
				logger.info("existe la secuencia por id: ");
				logger.info(idSecuencia);
			}
			if (sequenciaG != 0) {// ! si existe la secuencia
				logger.info("ya existe la secuencia");
				logger.info(sequenciaG);
				logger.info(idSecuencia);
				sequenceDao.EliminarSequencia(idSecuencia);
				// sequenceDao.ObtenerSequencia2(prefijoNuevo.getPrefijo(), numeroSequencia,
				// guardado.getId(),
				// prefijoNuevo.getIdSede().getId());
				if (numeroSequencia == 1) {
					sequenceDao.ObtenerSequencia2(prefijoNuevo.getPrefijo(), prefijoNuevo.getValorInicial() - 1, guardado.getId(),
							prefijoNuevo.getIdSede().getId());
				} else {
					sequenceDao.ObtenerSequencia2(prefijoNuevo.getPrefijo(), sequenciaG - 1, guardado.getId(),
							prefijoNuevo.getIdSede().getId());
				}
				// sequenceDao.ObtenerSequencia2(prefijoNuevo.getPrefijo(), sequenciaG - 1,
				// guardado.getId(),
				// prefijoNuevo.getIdSede().getId());
				logger.info("ingreso a crear la secuencia");
			}
			// if (sequenciaG != null) {// ! si existe la secuencia
			// logger.info(sequenciaG);
			// sequenceDao.EliminarSequencia(idSecuencia);
			// sequenceDao.ObtenerSequencia2(prefijoNuevo.getPrefijo(), sequenciaG - 1,
			// guardado.getId(),
			// prefijoNuevo.getIdSede().getId());
			// logger.info("ingreso a crear la secuencia");
			// }
			// if (sequenciaG != 0) {
			// logger.info(sequenciaG);
			// sequenceDao.EliminarSequencia(idSecuencia);
			// sequenceDao.ObtenerSequencia2(prefijoNuevo.getPrefijo(), sequenciaG - 1,
			// guardado.getId(),
			// prefijoNuevo.getIdSede().getId());
			// logger.info("ingreso a crear la secuencia");
			// }
			if (guardado != null) {
				// logger.info(guardado);
				logger.info(guardado.getActual());
				logger.info(guardado.getValorInicial());
			}
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito creando el prefijo");
			respuestaDto.setObjetoRespuesta(guardado);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);

		} catch (Exception e) {
			logger.error("Error en la creación del prefijo");
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error en el listar de prefijos");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	// Actualizar
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> actualizarPrefijo(@RequestBody Prefijo prefijoActualizado) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		Prefijo prefijoEnBd = this.prefijoDao.findById(prefijoActualizado.getId()).get();

		try {

			if (!prefijoEnBd.getId().equals(prefijoActualizado.getId())) {

				if (!this.existePrefijo(prefijoActualizado)) {

					Prefijo actualizada = this.prefijoDao.save(prefijoActualizado);

					RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito actualizando el prefijo");
					respuestaDto.setObjetoRespuesta(actualizada);
					respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
				} else {
					RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, "Error, el prefijo ya existe");
					respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.BAD_REQUEST);
				}
			} else {

				Prefijo actualizada = this.prefijoDao.save(prefijoActualizado);

				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito actualizando el prefijo");
				respuestaDto.setObjetoRespuesta(actualizada);
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
			}
		} catch (Exception e) {
			logger.error("Error en la actualización del prefijo");
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error en la actualización del prefijo");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return respuesta;
	}

	// Borrar prefijo en el sistema sistema
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> borrarPrefijo(@RequestParam Integer idPrefijo) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		try {
			this.prefijoDao.delete(this.prefijoDao.findById(idPrefijo).get());

			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			Throwable t = e.getCause();

			if (t instanceof ConstraintViolationException) {
				logger.error("se encuentra asociada en el sistema " + e.getCause());
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST,
						"Error,  se encuentra asociada en el sistema");
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.BAD_REQUEST);
			} else {
				logger.error("Error en el borrado del prefijo");
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
						"Error en el borrado del prefijo");
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

		return respuesta;
	}

	// Obtener los prefijos por sede
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerPrefijoSede() {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();

		try {
			Organizacion sede = organizacionDao.obtenerSede(usuarioAutenticado.getId());
			ArrayList<Prefijo> prefijo = this.prefijoDao.obtenerPrefijoSede(sede.getId());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de prefijos exitosa");
			respuestaDto.setObjetoRespuesta(prefijo);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo usuarios " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error obteniendo usuarios");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	// Obtener los prefijos de contado por sede
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerPrefijoSedeContado() {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();

		try {
			Organizacion sede = organizacionDao.obtenerSede(usuarioAutenticado.getId());
			ArrayList<Prefijo> prefijo = this.prefijoDao.obtenerPrefijoSedeContado(sede.getId());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de prefijos exitosa");
			respuestaDto.setObjetoRespuesta(prefijo);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo usuarios " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error obteniendo usuarios");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	// Obtener los prefijos de egresos por sede
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerPrefijoSedeEgresos() {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();

		try {
			Organizacion sede = organizacionDao.obtenerSede(usuarioAutenticado.getId());
			ArrayList<Prefijo> prefijo = this.prefijoDao.obtenerPrefijoSedeContado(sede.getId());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de prefijos exitosa");
			respuestaDto.setObjetoRespuesta(prefijo);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo usuarios " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error obteniendo usuarios");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	private boolean existePrefijo(Prefijo prefijoBuscar) {

		boolean flag = false;

		if (this.prefijoDao.findByPrefijo(prefijoBuscar.getPrefijo()) != null) {
			flag = true;
		}

		return flag;
	}

	public List<Prefijo> prefijosEgresoTipo(Boolean tipoGastos, Boolean tipoProveedores, Boolean tipoNomina) {
		List<Prefijo> prefijos = prefijoDao.obtenerPrefijosEgresoTipo(tipoGastos, tipoProveedores, tipoNomina);
		return prefijos;
	}

	public List<Prefijo> prefijosEgresoTipoPorSede(Boolean tipoGastos, Boolean tipoProveedores, Boolean tipoNomina) {
		Organizacion org = datosSesion.sedeUsuarioSesion();
		List<Prefijo> prefijos = prefijoDao.obtenerPrefijosEgresoTipoYSede(tipoGastos, tipoProveedores, tipoNomina,
				org.getId());
		return prefijos;
	}

	public List<Prefijo> prefijosEgreso() {
		List<Prefijo> prefijos = prefijoDao.obtenerPrefijosEgreso();
		return prefijos;
	}

	public List<Prefijo> prefijosEgresoPorSede() {
		Organizacion org = datosSesion.sedeUsuarioSesion();
		List<Prefijo> prefijos = prefijoDao.obtenerPrefijosEgresoSede(org.getId());
		return prefijos;
	}

	// Obtener los prefijos devoluciones por sede
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerPrefijoDevolucion() {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();

		try {
			Organizacion sede = organizacionDao.obtenerSede(usuarioAutenticado.getId());
			ArrayList<Prefijo> prefijo = this.prefijoDao.obtenerPrefijoDevolucion(sede.getId());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de prefijos exitosa");
			respuestaDto.setObjetoRespuesta(prefijo);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo usuarios " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error obteniendo usuarios");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	// Obtener los prefijos de notas credito por sede
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerPrefijoNotaCredito() {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();

		try {
			Organizacion sede = organizacionDao.obtenerSede(usuarioAutenticado.getId());
			ArrayList<Prefijo> prefijo = this.prefijoDao.obtenerPrefijoNotaCredito(sede.getId());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de prefijos exitosa");
			respuestaDto.setObjetoRespuesta(prefijo);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo usuarios " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error obteniendo usuarios");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	// Obtener los prefijos de notas debito por sede
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerPrefijoNotaDebito() {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();

		try {
			Organizacion sede = organizacionDao.obtenerSede(usuarioAutenticado.getId());
			ArrayList<Prefijo> prefijo = this.prefijoDao.obtenerPrefijoNotaDebito(sede.getId());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de prefijos exitosa");
			respuestaDto.setObjetoRespuesta(prefijo);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo usuarios " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error obteniendo usuarios");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	// Obtener los prefijos remisiones de venta por sede
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerPrefijoRemisionVenta() {
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();

		try {
			Organizacion sede = organizacionDao.obtenerSede(usuarioAutenticado.getId());
			List<Prefijo> prefijo = this.prefijoDao.obtenerPrefijoRemisionVenta(sede.getId());

			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de prefijos exitosa");
			respuestaDto.setObjetoRespuesta(prefijo);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo prefijos " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error obteniendo los prefijos");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	// Obtener los prefijos de notas credito cliente por sede
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerPrefijoNotaCreditoCliente() {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();

		try {
			Organizacion sede = organizacionDao.obtenerSede(usuarioAutenticado.getId());
			ArrayList<Prefijo> prefijo = this.prefijoDao.obtenerPrefijoNotaCreditoCliente(sede.getId());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de prefijos exitosa");
			respuestaDto.setObjetoRespuesta(prefijo);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo usuarios " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error obteniendo usuarios");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	// Obtener los prefijos de notas debito cliente por sede
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerPrefijoNotaDebitoCliente() {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();

		try {
			Organizacion sede = organizacionDao.obtenerSede(usuarioAutenticado.getId());
			ArrayList<Prefijo> prefijo = this.prefijoDao.obtenerPrefijoNotaDebitoCliente(sede.getId());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de prefijos exitosa");
			respuestaDto.setObjetoRespuesta(prefijo);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo usuarios " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error obteniendo usuarios");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	// Obtener los prefijos de facturas credito o electronica
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerPrefijoFacturaElectronica() {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();

		try {
			Organizacion sede = organizacionDao.obtenerSede(usuarioAutenticado.getId());
			List<Prefijo> prefijo = this.prefijoDao.obtenerPrefijoFacturaElectronica(sede.getId());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de prefijos exitosa");
			respuestaDto.setObjetoRespuesta(prefijo);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo usuarios " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error obteniendo usuarios");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	// Obtener los prefijos de recibos de cajaa
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerPrefijoReciboCaja() {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();

		try {
			Organizacion sede = organizacionDao.obtenerSede(usuarioAutenticado.getId());
			List<Prefijo> prefijo = this.prefijoDao.obtenerPrefijoReciboCaja(sede.getId());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de prefijos exitosa");
			respuestaDto.setObjetoRespuesta(prefijo);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo usuarios " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error obteniendo usuarios");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	// Obtener los prefijos de recibos de cajaa
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerPrefijoInventario() {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();

		try {
			Organizacion sede = organizacionDao.obtenerSede(usuarioAutenticado.getId());
			List<Prefijo> prefijo = this.prefijoDao.obtenerPrefijoInventario(sede.getId());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de prefijos exitosa");
			respuestaDto.setObjetoRespuesta(prefijo);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo usuarios " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error obteniendo usuarios");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerPrefijoDevolucionCliente() {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		logger.info("entro aqui");
		try {
			Organizacion sede = organizacionDao.obtenerSede(usuarioAutenticado.getId());
			ArrayList<Prefijo> prefijo = this.prefijoDao.obtenerPrefijoDevolucionCliente(sede.getId());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de prefijos exitosa");
			respuestaDto.setObjetoRespuesta(prefijo);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo usuarios " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error obteniendo usuarios");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}
}
