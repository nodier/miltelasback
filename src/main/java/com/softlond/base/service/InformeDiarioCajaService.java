package com.softlond.base.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.InformacionUsuario;
import com.softlond.base.entity.Usuario;
import com.softlond.base.repository.UsuarioInformacionDao;
import com.softlond.base.response.recaudoPagosReciboResponse;

@Service
public class InformeDiarioCajaService {

	private static final Logger logger = Logger.getLogger(InformeDiarioCajaService.class);
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	UsuarioInformacionDao usuarioInformacionDao;

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerInformeDiario(Integer idCaja, String fecha) {
		logger.info("ingresa a obtener informe diario caja");
		ResponseEntity<Object> respuesta;
		try {
			StringBuilder query = new StringBuilder();
			generarQuery(query, idCaja, fecha);
			TypedQuery<Object> reporte = (TypedQuery<Object>) entityManager.createNativeQuery(query.toString());
			List<Object> reportes = reporte.getResultList();
			RespuestaDto respuestaDto;
			if (reportes.size() == 0) {
				respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, "no hay informacion para las bsuqueda actual");
			} else {
				respuestaDto = new RespuestaDto(HttpStatus.OK, "obtención de informe exitosa");
			}
			respuestaDto.setObjetoRespuesta(reportes);

			// logger.info(reportes.get(0));

			respuesta = ResponseEntity.ok(HttpStatus.OK);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error al obtener informe" + e);
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al obtener informe" + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;

	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerInformeDiarioIva(Integer idCaja, String fecha) {
		ResponseEntity<Object> respuesta;
		try {
			StringBuilder query = new StringBuilder();
			generarQueryIva(query, idCaja, fecha);
			TypedQuery<Object> reporte = (TypedQuery<Object>) entityManager.createNativeQuery(query.toString());
			List<Object> reportes = reporte.getResultList();
			RespuestaDto respuestaDto;
			if (reportes.size() == 0) {
				respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST,
						"no hay informacion de iva para las busqueda actual");
			} else {
				respuestaDto = new RespuestaDto(HttpStatus.OK, "obtención de informe de iva exitosa");
			}
			respuestaDto.setObjetoRespuesta(reportes);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error al obtener informe" + e);
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al obtener informe" + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;

	}

	private void generarQuery(StringBuilder query, Integer idCaja, String fecha) {
		query.append(
				"select m.nombre, nid_pyme_tipo_venta,count(*),sum(m_subtotal_ventas),sum(miva),sum(m_total),sum(m_ajustes), concat(p.prefijo,min(n_nro_factura)),concat(p.prefijo,max(n_nro_factura)),sum(f.m_total),m.id\r\n"
						+ "FROM fac_facturas f join maestro_valor m on f.nid_pyme_tipo_venta = m.id join prefijo p on f.t_prefijo= p.id\r\n"
						+ "where nid_caja=");
		query.append("" + idCaja + " and date_format(date(d_fecha_venta),'%Y-%m-%d') = date('");
		query.append("" + fecha + "') group by f.nid_pyme_tipo_venta");
	}

	private void generarQueryIva(StringBuilder query, Integer idCaja, String fecha) {
		query.append(
				"select (sum(m_sub_total)) as total, nid_pyme_tipo_venta as tipo,fa.n_porcentajeiva as iva,\r\n"
						+ "Sum(((fa.n_cantidad * fa.m_precio_unitario)*(fa.n_porcentaje_descuento))) as descuento,\r\n"
						+ "Sum(((fa.n_cantidad * fa.m_precio_unitario)-(((fa.n_cantidad * fa.m_precio_unitario)*(fa.n_porcentaje_descuento))))-(((fa.n_cantidad * fa.m_precio_unitario)-(((fa.n_cantidad * fa.m_precio_unitario)*(fa.n_porcentaje_descuento))))/(1+(fa.n_porcentajeiva/100)))) as totalIva "
						+ "FROM fac_facturas f join maestro_valor m on f.nid_pyme_tipo_venta = m.id\r\n"
						+ "join fac_articulos fa on fa.nid_factura = f.nid_factura\r\n" + "where nid_caja=");
		query.append("" + idCaja + " and date_format(date(d_fecha_venta),'%Y-%m-%d') = date('");
		query.append("" + fecha + "') group by nid_pyme_tipo_venta,fa.n_porcentajeiva");
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerRecaudos(String fecha, String idCaja) {

		ResponseEntity<Object> respuesta;
		Integer idSede;
		if (idCaja.equals("null")) {
			Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
			Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
			InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
					.buscarPorIdUsuario(usuarioAutenticado.getId());
			idSede = usuarioInformacion.getIdOrganizacion().getId();
		} else {
			idSede = Integer.parseInt(idCaja);
		}
		try {
			StringBuilder query = new StringBuilder();
			generarQueryRecaudo(query, idSede, fecha);
			Query resultado = (Query) entityManager.createNativeQuery(query.toString());
			List<recaudoPagosReciboResponse> reportes = resultado
					.setResultTransformer(Transformers.aliasToBean(recaudoPagosReciboResponse.class)).getResultList();
			RespuestaDto respuestaDto;
			respuestaDto = new RespuestaDto(HttpStatus.OK, "obtención de informe de iva exitosa");
			respuestaDto.setObjetoRespuesta(reportes);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error al obtener informe" + e);
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al obtener informe" + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;

	}

	private void generarQueryRecaudo(StringBuilder query, Integer idCaja, String fecha) {
		query.append("select r.prefijo,ifnull(sum(r.total_recibo),0) as valorRecaudo from rcb_rbocajaventa r left join \r\n"
				+ "fac_facturas f on r.id=f.id_recibo_caja where r.id_caja =");
		query.append("" + idCaja + " and date_format(date(r.fecha_pago),'%Y-%m-%d') = date('");
		query.append("" + fecha + "') and f.nid_factura is null group by r.prefijo");
	}
}
