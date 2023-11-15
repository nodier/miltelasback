package com.softlond.base.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.DevolucionCompras;
import com.softlond.base.entity.EstadoDocumento;
import com.softlond.base.entity.FacturaCompra;
import com.softlond.base.entity.InformacionUsuario;
import com.softlond.base.entity.Sequence;
import com.softlond.base.entity.Usuario;
import com.softlond.base.repository.EstadoDocumentoDao;
import com.softlond.base.repository.FacturaCompraDao;
import com.softlond.base.repository.FacturaDevolucionDao;
import com.softlond.base.repository.SequenceDao;
import com.softlond.base.repository.UsuarioInformacionDao;
import com.softlond.base.request.DevolucionRequest;

@Service
public class DevolucionCompraService {

	private static final Logger logger = Logger.getLogger(DevolucionCompraService.class);

	@Autowired
	private FacturaDevolucionDao devolucionDao;

	@Autowired
	UsuarioInformacionDao usuarioInformacionDao;

	@Autowired
	FacturaCompraDao facturaCompraDao;

	@Autowired
	private SequenceDao sequenceDao;

	@Autowired
	private EstadoDocumentoDao estadoDocumento;

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> crearFactura(@RequestBody DevolucionRequest request) {
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
			request.getDevolucion().setUsuarioMod(usuarioInformacion);
			request.getDevolucion().setIdSede(usuarioInformacion.getIdOrganizacion());
			double iva = request.getDevolucion().getIva();
			double sub = request.getDevolucion().getSubTotal();
			double suma = iva + sub;
			int i_suma = (int) Math.round(suma);
			request.getDevolucion().setTotal(i_suma);
			if (request.getDevolucion().getId() != null) {
				devolucionDao.findById(request.getDevolucion().getId());
				devolucionDao.save(request.getDevolucion());
			} else {
				devolucionDao.save(request.getDevolucion());
			}
			sequenceDao.save(secuenciaSave);

			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Factura de compra creada exitosamente");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error al guardar la devolucion" + e + " linea " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error al guardar la factura" + e);
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
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error al obtener numero" + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerDevolucion(Integer idSede) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		try {
			List<DevolucionCompras> devolucionC = this.devolucionDao.findByIdSede(idSede);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de devoluciones exitosa");
			respuestaDto.setObjetoRespuesta(devolucionC);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo factura de compra " + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error obteniendo devolucion");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return respuesta;

	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerDevolucionSedeProveedor(Integer idSede, Integer idProveedor) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		try {
			List<DevolucionCompras> devolucionC = this.devolucionDao.findByIdSedeProveedor(idSede, idProveedor);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de devoluciones exitosa");
			respuestaDto.setObjetoRespuesta(devolucionC);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo factura de compra " + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error obteniendo devolucion");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return respuesta;

	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> ObtenerDevolucionPorNumero(String numero) {
		logger.info(numero);
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		try {
			List<DevolucionCompras> devolucionC = this.devolucionDao.DevolucionesAnular(numero);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de devoluciones exitosa");
			respuestaDto.setObjetoRespuesta(devolucionC);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo factura de compra " + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error obteniendo devolucion");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return respuesta;

	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> anularFactura(@RequestBody DevolucionCompras request) {
		logger.info("ingresa anular factura exitosamente");
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		try {
			EstadoDocumento estado = estadoDocumento.findById(3).orElse(null);
			FacturaCompra facturaDevolucion = facturaCompraDao.findByIdFactura(request.getFactura().getId());
			facturaDevolucion.setCodEstadoCon(estado);
			request.setCodEstadoCon(estado);
			request.setCantidadTotal(0);
			request.setFechaFactura(null);
			request.setIva(null);
			request.setMotivo(null);
			request.setRetencion(null);
			request.setTotal(null);
			request.setSubTotal(null);

			devolucionDao.save(request);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Anulacion de la devolucion exitosa");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error al anular la devolucion " + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al anular la devolucion" + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

}
