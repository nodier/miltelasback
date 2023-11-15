package com.softlond.base.service;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;
import org.joda.time.LocalDate;
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
import com.softlond.base.entity.FormasPago;
import com.softlond.base.entity.PagosReciboCaja;
import com.softlond.base.entity.Usuario;
import com.softlond.base.repository.PagosReciboCajaDao;

@Service
public class PagosReciboCajaService {

	private static final Logger logger = Logger.getLogger(PagosReciboCajaService.class);

	@Autowired
	private PagosReciboCajaDao pagosReciboCajaDao;

	// Crear pagos en recibo de caja
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> crearPagoReciboCaja(@RequestBody PagosReciboCaja pagoNuevo) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();

		try {

			pagoNuevo.setFecha(new Date(new java.util.Date().getTime()));

			pagoNuevo.setIdCreador(usuarioAutenticado);

			PagosReciboCaja guardado = this.pagosReciboCajaDao.save(pagoNuevo);

			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito registrando el pago");
			respuestaDto.setObjetoRespuesta(guardado);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);

		} catch (Exception e) {
			logger.error("Error al guardar el pago");
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error en el listar pagos");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return respuesta;
	}

	// Listar pagos
	@PreAuthorize("hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerTodos(@RequestParam Integer idReciboCaja) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		try {

			ArrayList<PagosReciboCaja> pagos = this.pagosReciboCajaDao.findPagos(idReciboCaja);

			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito listando los pagos");
			respuestaDto.setObjetoRespuesta(pagos);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error en el listar los pagos");
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error en el listar los pagos");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return respuesta;
	}

	// Borrar ultimo pago resgitrado
	@PreAuthorize("hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> borrarPago(@RequestParam Integer idPago) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		try {

			this.pagosReciboCajaDao.delete(this.pagosReciboCajaDao.findById(idPago).get());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);

		} catch (Exception e) {
			Throwable t = e.getCause();

			if (t instanceof ConstraintViolationException) {
				logger.error("Se encuentra asociado en el sistema " + e.getCause());
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST,
						"Error,  se encuentra asociada en el sistema");
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.BAD_REQUEST);
			} else {
				logger.error("Error en el borrado del pago");
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
						"Error en el borrado del pago");
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

		return respuesta;
	}

	// Obtener pagos por cliente
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerPagosCliente(@RequestParam Integer idFactura) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		try {
		
			ArrayList<PagosReciboCaja> pagos = this.pagosReciboCajaDao.pagosCliente(idFactura);
			
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito listando los pagos");
			respuestaDto.setObjetoRespuesta(pagos);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error en el listar los pagos");
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error en el listar los pagos");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return respuesta;
	}

	// Obtener las formas de pago de la caja de acuerdo a la fecha
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerPagosCaja(Integer idCaja, String fecha) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		try {

			List<PagosReciboCaja> formasPagos = this.pagosReciboCajaDao.obtenerPagosInformeCaja(idCaja, fecha);

			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de pagos exitosa");
			respuestaDto.setObjetoRespuesta(formasPagos);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo  pagos " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error obteniendo formas de pago");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	// Obtener las formas de pago de la sede de acuerdo a la fecha
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerPagosSede(Integer idSede, String fecha) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		try {

			List<PagosReciboCaja> formasPagos = this.pagosReciboCajaDao.obtenerPagosInformeSede(idSede, fecha);

			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de pagos exitosa");
			respuestaDto.setObjetoRespuesta(formasPagos);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo  pagos " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error obteniendo formas de pago");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}
}
