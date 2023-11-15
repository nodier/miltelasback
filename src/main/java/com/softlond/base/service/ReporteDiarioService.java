package com.softlond.base.service;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.InformacionUsuario;
import com.softlond.base.entity.Organizacion;
import com.softlond.base.entity.Usuario;
import com.softlond.base.repository.OrganizacionDao;
import com.softlond.base.repository.UsuarioInformacionDao;

@Service
public class ReporteDiarioService {

	private static final Logger logger = Logger.getLogger(ReporteDiarioService.class);
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	OrganizacionDao organizacionDao;

	@Autowired
	UsuarioInformacionDao usuarioInformacionDao;

	public ResponseEntity<Object> obtenerInformeDiario(Integer idSede, String fecha) {

		logger.info("ingresa a obtener informe");

		ResponseEntity<Object> respuesta;
		try {
			StringBuilder query = new StringBuilder();
			generarQuery(query, idSede, fecha);
			TypedQuery<Object> reporte = (TypedQuery<Object>) entityManager.createNativeQuery(query.toString());
			List<Object> reportes = reporte.getResultList();
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtención de reportes exitosa");
			respuestaDto.setObjetoRespuesta(reportes);

			logger.info(reportes.get(0));

			respuesta = ResponseEntity.ok(HttpStatus.OK);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error al obtener informe" + e);
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error al obtener informe" + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;

	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerInformeRecaudoDiario(Integer idCaja, String fecha) {
		ResponseEntity<Object> respuesta;
		try {
			StringBuilder query = new StringBuilder();
			generarQueryRecaudos(query, idCaja, fecha);
			TypedQuery<Object> reporte = (TypedQuery<Object>) entityManager.createNativeQuery(query.toString());
			List<Object> reportes = reporte.getResultList();
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtención de informe exitosa");
			respuestaDto.setObjetoRespuesta(reportes);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error al obtener informe" + e);
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error al obtener informe" + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;

	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerInformeRecaudoDiarioSede(String fecha, String idSedeRequest) {
		ResponseEntity<Object> respuesta;
		Integer idSede;
		if (idSedeRequest.equals("null")) {
			Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
			Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
			InformacionUsuario usuarioInformacion = this.usuarioInformacionDao.buscarPorIdUsuario(usuarioAutenticado.getId());
			idSede = usuarioInformacion.getIdOrganizacion().getId();
		} else {
			idSede = Integer.parseInt(idSedeRequest);
		}
		try {
			StringBuilder query = new StringBuilder();
			generarQueryRecaudosSede(query, idSede, fecha);
			TypedQuery<Object> reporte = (TypedQuery<Object>) entityManager.createNativeQuery(query.toString());
			List<Object> reportes = reporte.getResultList();
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtención de informe exitosa");
			respuestaDto.setObjetoRespuesta(reportes);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error al obtener informe" + e);
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error al obtener informe" + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;

	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerInformeRecaudoDiarioMediosPagosNoEfectivo(Integer idCaja, String fecha) {
		ResponseEntity<Object> respuesta;
		try {
			StringBuilder query = new StringBuilder();
			generarQueryMediosPagoNoEfectivo(query, idCaja, fecha);
			TypedQuery<Object> reporte = (TypedQuery<Object>) entityManager.createNativeQuery(query.toString());
			List<Object> reportes = reporte.getResultList();
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtención de informe exitosa");
			respuestaDto.setObjetoRespuesta(reportes);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error al obtener informe" + e);
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error al obtener informe" + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;

	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerInformeRecaudoDiarioMediosPagosNoEfectivoRecaudos(Integer idCaja, String fecha) {
		ResponseEntity<Object> respuesta;
		try {
			StringBuilder query = new StringBuilder();
			generarQueryMediosPagoNoEfectivoRecaudos(query, idCaja, fecha);
			TypedQuery<Object> reporte = (TypedQuery<Object>) entityManager.createNativeQuery(query.toString());
			List<Object> reportes = reporte.getResultList();
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtención de informe exitosa");
			respuestaDto.setObjetoRespuesta(reportes);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error al obtener informe" + e);
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error al obtener informe" + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;

	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerInformeRecaudoDiarioMediosPagosNoEfectivoSede(String fecha,
			String idSedeRequest) {
		ResponseEntity<Object> respuesta;
		Integer idSede;
		if (idSedeRequest.equals("null")) {
			Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
			Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
			InformacionUsuario usuarioInformacion = this.usuarioInformacionDao.buscarPorIdUsuario(usuarioAutenticado.getId());
			idSede = usuarioInformacion.getIdOrganizacion().getId();
		} else {
			idSede = Integer.parseInt(idSedeRequest);
		}
		try {
			StringBuilder query = new StringBuilder();

			generarQueryMediosPagoNoEfectivoSede(query, idSede, fecha);
			TypedQuery<Object> reporte = (TypedQuery<Object>) entityManager.createNativeQuery(query.toString());
			List<Object> reportes = reporte.getResultList();
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtención de informe exitosa");
			respuestaDto.setObjetoRespuesta(reportes);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error al obtener informe" + e);
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error al obtener informe" + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;

	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerInformeRecaudoDiarioMediosPagosNoEfectivoSedeRecaudos(String fecha,
			String idSedeRequest) {
		ResponseEntity<Object> respuesta;
		Integer idSede;
		if (idSedeRequest.equals("null")) {
			Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
			Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
			InformacionUsuario usuarioInformacion = this.usuarioInformacionDao.buscarPorIdUsuario(usuarioAutenticado.getId());
			idSede = usuarioInformacion.getIdOrganizacion().getId();
		} else {
			idSede = Integer.parseInt(idSedeRequest);
		}
		try {
			StringBuilder query = new StringBuilder();

			generarQueryMediosPagoNoEfectivoSedeRecaudos(query, idSede, fecha);
			TypedQuery<Object> reporte = (TypedQuery<Object>) entityManager.createNativeQuery(query.toString());
			List<Object> reportes = reporte.getResultList();
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtención de informe exitosa");
			respuestaDto.setObjetoRespuesta(reportes);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error al obtener informe" + e);
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error al obtener informe" + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;

	}

	private void generarQuery(StringBuilder query, Integer idSede, String fecha) {
		query.append(
				"select rbo.prefijo, p.id_formas_pago, sum(valor) from rcb_rbocajaventa rbo join pagos_recibo_caja p on rbo.id=p.id_recibo_caja\r\n"
						+ "where rbo.id_sede=");
		query.append("" + idSede + " and rbo.fecha_pago='");
		query.append("" + fecha + "' group by rbo.prefijo, p.id_formas_pago");
	}

	private void generarQueryRecaudos(StringBuilder query, Integer idCaja, String fecha) {
		query.append(
				"select rbo.cod_rbocajaventa, p.id_formas_pago, sum(p.valor),count(distinct(cod_rbocajaventa)), fp.forma_pago, cambio from rcb_rbocajaventa rbo join pagos_recibo_caja p on rbo.id=p.id_recibo_caja\r\n"
						+ "join config_rcb_formas_pago fp on fp.id=p.id_formas_pago\r\n"
						+ "where rbo.id_caja=");
		query.append("" + idCaja + " and rbo.fecha_pago='");
		query.append("" + fecha + "' and fp.id = 1 GROUP  by rbo.cod_rbocajaventa");
	}

	private void generarQueryRecaudosSede(StringBuilder query, Integer idSede, String fecha) {
		query.append(
				"select rbo.cod_rbocajaventa, p.id_formas_pago, sum(p.valor),count(distinct(cod_rbocajaventa)), fp.forma_pago, cambio from rcb_rbocajaventa rbo join pagos_recibo_caja p on rbo.id=p.id_recibo_caja\r\n"
						+ "join config_rcb_formas_pago fp on fp.id=p.id_formas_pago\r\n"
						+ "where rbo.id_sede=");
		query.append("" + idSede + " and rbo.fecha_pago='");
		query.append("" + fecha + "' and fp.id = 1 GROUP  by rbo.cod_rbocajaventa");
	}

	private void generarQueryMediosPagoNoEfectivo(StringBuilder query, Integer idCaja, String fecha) {
		query.append(
				"select rbo.prefijo, rbo.cod_rbocajaventa, p.id_formas_pago, sum(p.valor), fp.forma_pago, cambio, rt.banco, p.numero_aprobacion, p.cuenta\r\n"
						+ "from rcb_rbocajaventa rbo join pagos_recibo_caja p on rbo.id=p.id_recibo_caja\r\n"
						+ "join config_rcb_formas_pago fp on fp.id=p.id_formas_pago left join rbc_tipo_tarjeta rt on rt.id = p.id_tipo_tarjeta\r\n"
						+ "where rbo.id_caja=");
		query.append("" + idCaja + " and rbo.fecha_pago='");
		query.append("" + fecha + "' and id_formas_pago != 1 GROUP  by p.id_recibo_caja, p.id_formas_pago");
	}

	// informe recuados
	private void generarQueryMediosPagoNoEfectivoRecaudos(StringBuilder query, Integer idCaja, String fecha) {
		query.append(
				"select rbo.prefijo, rbo.cod_rbocajaventa, p.id_formas_pago, sum(p.valor + cambio), fp.forma_pago, cambio, rt.banco, p.numero_aprobacion, p.cuenta\r\n"
						+ "from rcb_rbocajaventa rbo join pagos_recibo_caja p on rbo.id=p.id_recibo_caja\r\n"
						+ "join config_rcb_formas_pago fp on fp.id=p.id_formas_pago left join rbc_tipo_tarjeta rt on rt.id = p.id_tipo_tarjeta\r\n"
						+ "where rbo.id_caja=");
		query.append("" + idCaja + " and rbo.fecha_pago='");
		query.append("" + fecha + "' and id_formas_pago != 1 GROUP  by p.id_recibo_caja, p.id_formas_pago");
	}

	private void generarQueryMediosPagoNoEfectivoSede(StringBuilder query, Integer idSede, String fecha) {
		query.append(
				"select rbo.prefijo, rbo.cod_rbocajaventa, p.id_formas_pago, sum(p.valor), fp.forma_pago, cambio, rt.banco, p.numero_aprobacion, p.cuenta\r\n"
						+ "from rcb_rbocajaventa rbo join pagos_recibo_caja p on rbo.id=p.id_recibo_caja\r\n"
						+ "join config_rcb_formas_pago fp on fp.id=p.id_formas_pago left join rbc_tipo_tarjeta rt on rt.id = p.id_tipo_tarjeta\r\n"
						+ "where rbo.id_sede=");
		query.append("" + idSede + " and rbo.fecha_pago='");
		query.append("" + fecha + "' and id_formas_pago != 1 GROUP  by p.id_recibo_caja, p.id_formas_pago");
	}

	// informe de recaudos
	private void generarQueryMediosPagoNoEfectivoSedeRecaudos(StringBuilder query, Integer idSede, String fecha) {
		query.append(
				"select rbo.prefijo, rbo.cod_rbocajaventa, p.id_formas_pago, sum(p.valor + cambio), fp.forma_pago, cambio, rt.banco, p.numero_aprobacion, p.cuenta\r\n"
						+ "from rcb_rbocajaventa rbo join pagos_recibo_caja p on rbo.id=p.id_recibo_caja\r\n"
						+ "join config_rcb_formas_pago fp on fp.id=p.id_formas_pago left join rbc_tipo_tarjeta rt on rt.id = p.id_tipo_tarjeta\r\n"
						+ "where rbo.id_sede=");
		query.append("" + idSede + " and rbo.fecha_pago='");
		query.append("" + fecha + "' and id_formas_pago != 1 GROUP  by p.id_recibo_caja, p.id_formas_pago");
	}

	// Recibos de caja

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerInformeRecaudoDiarioRecibo(Integer idCaja, String fecha) {
		ResponseEntity<Object> respuesta;
		try {
			StringBuilder query = new StringBuilder();
			generarQueryRecaudosRecibo(query, idCaja, fecha);
			TypedQuery<Object> reporte = (TypedQuery<Object>) entityManager.createNativeQuery(query.toString());
			List<Object> reportes = reporte.getResultList();
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtención de informe exitosa");
			respuestaDto.setObjetoRespuesta(reportes);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error al obtener informe" + e);
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error al obtener informe" + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;

	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerInformeRecaudoDiarioSedeRecibo(String fecha, String idSedeRequest) {
		ResponseEntity<Object> respuesta;
		Integer idSede;
		if (idSedeRequest.equals("null")) {
			Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
			Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
			InformacionUsuario usuarioInformacion = this.usuarioInformacionDao.buscarPorIdUsuario(usuarioAutenticado.getId());
			idSede = usuarioInformacion.getIdOrganizacion().getId();
		} else {
			idSede = Integer.parseInt(idSedeRequest);
		}
		try {
			StringBuilder query = new StringBuilder();
			generarQueryRecaudosSedeRecibo(query, idSede, fecha);
			TypedQuery<Object> reporte = (TypedQuery<Object>) entityManager.createNativeQuery(query.toString());
			List<Object> reportes = reporte.getResultList();
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtención de informe exitosa");
			respuestaDto.setObjetoRespuesta(reportes);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error al obtener informe" + e);
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error al obtener informe" + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;

	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerInformeRecaudoDiarioMediosPagosNoEfectivoRecibo(Integer idCaja, String fecha) {
		ResponseEntity<Object> respuesta;
		try {
			StringBuilder query = new StringBuilder();
			generarQueryMediosPagoNoEfectivoRecibo(query, idCaja, fecha);
			TypedQuery<Object> reporte = (TypedQuery<Object>) entityManager.createNativeQuery(query.toString());
			List<Object> reportes = reporte.getResultList();
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtención de informe exitosa");
			respuestaDto.setObjetoRespuesta(reportes);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error al obtener informe" + e);
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error al obtener informe" + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;

	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerInformeRecaudoDiarioMediosPagosNoEfectivoSedeRecibo(String fecha,
			String idSedeRequest) {
		ResponseEntity<Object> respuesta;
		Integer idSede;
		if (idSedeRequest.equals("null")) {
			Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
			Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
			InformacionUsuario usuarioInformacion = this.usuarioInformacionDao.buscarPorIdUsuario(usuarioAutenticado.getId());
			idSede = usuarioInformacion.getIdOrganizacion().getId();
		} else {
			idSede = Integer.parseInt(idSedeRequest);
		}
		try {
			StringBuilder query = new StringBuilder();
			generarQueryMediosPagoNoEfectivoSedeRecibo(query, idSede, fecha);
			TypedQuery<Object> reporte = (TypedQuery<Object>) entityManager.createNativeQuery(query.toString());
			List<Object> reportes = reporte.getResultList();
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtención de informe exitosa");
			respuestaDto.setObjetoRespuesta(reportes);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error al obtener informe" + e);
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error al obtener informe" + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;

	}

	private void generarQueryRecaudosRecibo(StringBuilder query, Integer idCaja, String fecha) {
		query.append(
				"select rbo.cod_rbocajaventa, p.id_formas_pago, sum(p.valor),count(distinct(cod_rbocajaventa)), fp.forma_pago, cambio from rcb_rbocajaventa rbo join pagos_recibo_caja p on rbo.id=p.id_recibo_caja\r\n"
						+ "join config_rcb_formas_pago fp on fp.id=p.id_formas_pago\r\n"
						+ "where rbo.id_caja=");
		query.append("" + idCaja + " and rbo.fecha_pago='");
		query.append("" + fecha + "' and fp.id = 1 GROUP  by rbo.cod_rbocajaventa");
	}

	private void generarQueryRecaudosSedeRecibo(StringBuilder query, Integer idSede, String fecha) {
		query.append(
				"select rbo.cod_rbocajaventa, p.id_formas_pago, sum(p.valor),count(distinct(cod_rbocajaventa)), fp.forma_pago, cambio from rcb_rbocajaventa rbo join pagos_recibo_caja p on rbo.id=p.id_recibo_caja\r\n"
						+ "join config_rcb_formas_pago fp on fp.id=p.id_formas_pago\r\n"
						+ "where rbo.id_sede=");
		query.append("" + idSede + " and rbo.fecha_pago='");
		query.append("" + fecha + "' and fp.id = 1 GROUP  by rbo.cod_rbocajaventa");
	}

	private void generarQueryMediosPagoNoEfectivoRecibo(StringBuilder query, Integer idCaja, String fecha) {
		query.append(
				"select rbo.prefijo, rbo.cod_rbocajaventa, p.id_formas_pago, sum(p.valor), fp.forma_pago, cambio, rt.banco, p.numero_aprobacion, p.cuenta\r\n"
						+ "from rcb_rbocajaventa rbo join pagos_recibo_caja p on rbo.id=p.id_recibo_caja\r\n"
						+ "join config_rcb_formas_pago fp on fp.id=p.id_formas_pago left join rbc_tipo_tarjeta rt on rt.id = p.id_tipo_tarjeta\r\n"
						+ "where rbo.id_caja=");
		query.append("" + idCaja + " and rbo.fecha_pago='");
		query.append("" + fecha + "' and id_formas_pago != 1 GROUP  by p.id_recibo_caja, p.id_formas_pago");
	}

	private void generarQueryMediosPagoNoEfectivoSedeRecibo(StringBuilder query, Integer idSede, String fecha) {
		query.append(
				"select rbo.prefijo, rbo.cod_rbocajaventa, p.id_formas_pago, sum(p.valor), fp.forma_pago, cambio, rt.banco, p.numero_aprobacion, p.cuenta\r\n"
						+ "from rcb_rbocajaventa rbo join pagos_recibo_caja p on rbo.id=p.id_recibo_caja\r\n"
						+ "join config_rcb_formas_pago fp on fp.id=p.id_formas_pago left join rbc_tipo_tarjeta rt on rt.id = p.id_tipo_tarjeta\r\n"
						+ "where rbo.id_sede=");
		query.append("" + idSede + " and rbo.fecha_pago='");
		query.append("" + fecha + "' and id_formas_pago != 1 GROUP  by p.id_recibo_caja, p.id_formas_pago");
	}

}
