package com.softlond.base.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Comparator;
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
import com.softlond.base.entity.FacturaCompra;
import com.softlond.base.entity.FacturaImpuestos;
import com.softlond.base.entity.FacturaRetenciones;
import com.softlond.base.entity.InformacionUsuario;
import com.softlond.base.entity.NotaCredito;
import com.softlond.base.entity.NotaDebito;
import com.softlond.base.entity.Usuario;
import com.softlond.base.repository.AsignacionComprobanteDao;
import com.softlond.base.repository.ComprobanteEgresoDao;
import com.softlond.base.repository.ConceptoReciboEgresoDao;
import com.softlond.base.repository.InformeFacturasVencidasDao;
import com.softlond.base.repository.UsuarioInformacionDao;
import com.softlond.base.response.InfromeFacturasVencidas;
import com.softlond.base.response.MovimientoProveedor;
import com.softlond.base.response.Paginacion;

@Service
public class InformeFacturasVencidasService {

	private static final Logger logger = Logger.getLogger(InformeFacturasVencidasService.class);

	final int ITEMS_POR_PAGINA = 10;

	@Autowired
	private InformeFacturasVencidasDao informeFacturasVencidasDao;

	@Autowired
	UsuarioInformacionDao usuarioInformacionDao;

	@Autowired
	private FacturaCompraService servicioFactura;

	@Autowired
	private NotaDebitoService servicioND;

	@Autowired
	private NotaCreditoService servicioNC;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private ConceptoReciboEgresoDao conceptoReciboEgresoDao;

	@Autowired
	private ComprobanteEgresoDao comprobanteEgresoDao;

	@Autowired
	private AsignacionComprobanteDao asignacionComprobanteDao;

	private Integer totalSuma;

	public Integer getTotalSuma() {
		return totalSuma;
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> listarinformeFacturasVencidas(Integer page) {
		ResponseEntity<Object> respuesta;
		try {
			Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
			Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
			InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
					.buscarPorIdUsuario(usuarioAutenticado.getId());

			Pageable pageConfig = PageRequest.of(page, 10);
			int idSede = usuarioInformacion.getIdOrganizacion().getId();
			Page<FacturaCompra> numerofacturas = informeFacturasVencidasDao.listarinformeFacturasVencidas(idSede,
					pageConfig);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de facturas vencidas exitosa",
					numerofacturas.getContent(), 0, numerofacturas.getTotalElements() + "");
			respuestaDto.setObjetoRespuesta(numerofacturas);
			Integer total = informeFacturasVencidasDao.TotalInformeFacturasVencidas(idSede)
					- comprobanteEgresoDao.obtenerValorAbonosSede(idSede)
					- comprobanteEgresoDao.obtenerValorDescuentosSede(idSede)
					- comprobanteEgresoDao.obtenerValorAsignacionSede(idSede);
			respuestaDto.setVariable(String.valueOf(total));
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (RuntimeException e) {
			logger.error("Error al obtener las facturas vencidas" + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al obtener las facturas vencidas " + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	public List<InfromeFacturasVencidas> listarinformeFacturasVencidasCompra(int idSede) {
		List<InfromeFacturasVencidas> vecom = new ArrayList<>();
		this.totalSuma = 0;
		// Obtener facturas
		List<FacturaCompra> facturas = servicioFactura.listarinformeFacturasVencidasCompra(idSede);
		for (FacturaCompra f : facturas) {
			InfromeFacturasVencidas informe = InfromeFacturasVencidas.convertirFactura(f);
			Integer abonos = conceptoReciboEgresoDao.obtenerTotalAbonos(informe.getNroFactura(), idSede);
			Integer asignacion = asignacionComprobanteDao.obtenerTotal(f.getNroFactura(), idSede);
			Integer descuentos = conceptoReciboEgresoDao.obtenerTotalDescuentos(f.getNroFactura(), idSede);
			if (abonos != null) {
				informe.setSaldo(informe.getTotal() - abonos - asignacion - descuentos);
				informe.setAbono(abonos + asignacion + descuentos);
				this.totalSuma += (informe.getTotal() - abonos - asignacion - descuentos);
			} else {
				this.totalSuma += informe.getTotal();
			}
			vecom.add(informe);
		}

		// Obtener Notas débito
		List<NotaDebito> notasDebito = servicioND.listarnotadebitopendiente(idSede);
		for (NotaDebito nd : notasDebito) {
			InfromeFacturasVencidas informe = InfromeFacturasVencidas.convertirNotaDeb(nd);
			Integer abonos = conceptoReciboEgresoDao
					.obtenerTotalAbonos(informe.getDocumento() + "-" + informe.getNroFactura(), idSede);
			Integer asignacion = asignacionComprobanteDao
					.obtenerTotal(informe.getDocumento() + "-" + informe.getNroFactura(), idSede);
			Integer descuentos = conceptoReciboEgresoDao
					.obtenerTotalDescuentos(informe.getDocumento() + "-" + informe.getNroFactura(), idSede);
			if (abonos != null) {
				informe.setSaldo(informe.getTotal() - abonos - asignacion - descuentos);
				informe.setAbono(abonos + asignacion + descuentos);
				this.totalSuma += (informe.getTotal() - abonos - asignacion - descuentos);
			} else {
				this.totalSuma += informe.getTotal();
			}
			vecom.add(informe);
		}

		// Obtener Notas crédito
		List<NotaCredito> notasCredito = servicioNC.listarnotacreditopendiente(idSede);
		for (NotaCredito nc : notasCredito) {
			InfromeFacturasVencidas informe = InfromeFacturasVencidas.convertirNotaCred(nc);
			Integer abonos = conceptoReciboEgresoDao
					.obtenerTotalAbonos(informe.getDocumento() + "-" + informe.getNroFactura(), idSede);
			if (abonos != null) {
				informe.setSaldo(informe.getTotal() - abonos);
				informe.setAbono(abonos);
				this.totalSuma -= (informe.getTotal() + abonos);
			} else {
				this.totalSuma -= informe.getTotal();
			}
			vecom.add(informe);
		}

		vecom.sort(new OrdenPorFecha1());
		return vecom;
	}

	public Paginacion listarinformeFacturasVencidasCompraPaginado(int idSede, int pagina) {
		List<InfromeFacturasVencidas> vecom = listarinformeFacturasVencidasCompra(idSede);
		return Paginacion.paginar(vecom, ITEMS_POR_PAGINA, pagina);
	}

	public List<InfromeFacturasVencidas> listarinformeFacturasVencidasCompraExcel(int idSede) {
		List<InfromeFacturasVencidas> vecom = listarinformeFacturasVencidasCompra(idSede);
		return vecom;
	}

	public List<InfromeFacturasVencidas> listarinformeFacturasVencidasCompraFiltros(int idSede, int idProveedor,
			String fechaInicial, String fechaFinal) {
		List<InfromeFacturasVencidas> vecom = new ArrayList<>();
		this.totalSuma = 0;

		// Obtener facturas
		List<FacturaCompra> facturas = servicioFactura.listarinformeFacturasVencidasCompraFiltros(idSede, idProveedor,
				fechaInicial, fechaFinal);
		for (FacturaCompra f : facturas) {
			InfromeFacturasVencidas informe = InfromeFacturasVencidas.convertirFactura(f);
			Integer abonos = conceptoReciboEgresoDao.obtenerTotalAbonos(informe.getNroFactura(), idSede);
			Integer asignacion = asignacionComprobanteDao.obtenerTotal(f.getNroFactura(), idSede);
			Integer descuentos = conceptoReciboEgresoDao.obtenerTotalDescuentos(f.getNroFactura(), idSede);
			if (abonos != null) {
				informe.setSaldo(informe.getTotal() - abonos - asignacion - descuentos);
				informe.setAbono(abonos + asignacion + descuentos);
				this.totalSuma += (informe.getTotal() - abonos - asignacion - descuentos);
			} else {
				this.totalSuma += informe.getTotal();
			}
			if (informe.getSaldo() > 0) {
				vecom.add(informe);
			}
		}

		// Obtener Notas débito
		List<NotaDebito> notasDebito = servicioND.listarinformeNotasDebitoVencidasCompraFiltros(idSede, idProveedor,
				fechaInicial, fechaFinal);
		for (NotaDebito nd : notasDebito) {
			InfromeFacturasVencidas informe = InfromeFacturasVencidas.convertirNotaDeb(nd);
			Integer abonos = conceptoReciboEgresoDao
					.obtenerTotalAbonos(informe.getDocumento() + "-" + informe.getNroFactura(), idSede);
			Integer asignacion = asignacionComprobanteDao
					.obtenerTotal(informe.getDocumento() + "-" + informe.getNroFactura(), idSede);
			Integer descuentos = conceptoReciboEgresoDao
					.obtenerTotalDescuentos(informe.getDocumento() + "-" + informe.getNroFactura(), idSede);
			if (abonos != null) {
				informe.setSaldo(informe.getTotal() - abonos - asignacion - descuentos);
				informe.setAbono(abonos + asignacion + descuentos);
				this.totalSuma += (informe.getTotal() - abonos - asignacion - descuentos);
			} else {
				this.totalSuma += informe.getTotal();
			}
			if (informe.getSaldo() > 0) {
				vecom.add(informe);
			}
		}

		// Obtener Notas crédito
		List<NotaCredito> notasCredito = servicioNC.listarinformeNotasCreditoVencidasCompraFiltros(idSede, idProveedor,
				fechaInicial, fechaFinal);
		for (NotaCredito nc : notasCredito) {
			InfromeFacturasVencidas informe = InfromeFacturasVencidas.convertirNotaCred(nc);
			Integer abonos = conceptoReciboEgresoDao
					.obtenerTotalAbonos(informe.getDocumento() + "-" + informe.getNroFactura(), idSede);
			if (abonos != null) {
				informe.setSaldo(informe.getTotal() - abonos);
				informe.setAbono(abonos);
				this.totalSuma -= informe.getTotal() - abonos;
			} else {
				this.totalSuma -= informe.getTotal();
			}
			vecom.add(informe);
		}

		vecom.sort(new OrdenPorFecha1());
		return vecom;
	}

	public Paginacion listarinformeFacturasVencidasCompraConsultaPaginado(int idSede, int idProveedor,
			String fechaInicial, String fechaFinal, int pagina) {
		List<InfromeFacturasVencidas> vecom = listarinformeFacturasVencidasCompraFiltros(idSede, idProveedor,
				fechaInicial, fechaFinal);
		return Paginacion.paginar(vecom, ITEMS_POR_PAGINA, pagina);
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> listarFacturasVencidasConsulta(String fechaInicial, String fechaFinal,
			Integer proveedor, Integer page) {
		ResponseEntity<Object> respuesta;
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		Pageable paging = PageRequest.of(page, 10);
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		try {
			List<FacturaCompra> facturas;
			StringBuilder query = new StringBuilder();
			StringBuilder queryCantidad = new StringBuilder();
			StringBuilder querySuma = new StringBuilder();
			StringBuilder queryAbonos = new StringBuilder();
			StringBuilder queryDescuentos = new StringBuilder();
			StringBuilder queryAsignacion = new StringBuilder();
			generarQueryInformeFacturasVencida(query, usuarioInformacion.getIdOrganizacion().getId(), fechaInicial,fechaFinal, proveedor);		
			TypedQuery<FacturaCompra> facturasInfoQuery = (TypedQuery<FacturaCompra>) entityManager.createNativeQuery(query.toString(), FacturaCompra.class);
			int pageNumber = paging.getPageNumber();
			int pageSize = paging.getPageSize();
			facturasInfoQuery.setFirstResult(pageNumber * pageSize);
			facturasInfoQuery.setMaxResults(pageSize);
			facturas = facturasInfoQuery.getResultList();
			generarQueryCantidadInformeFacturasVencida(queryCantidad, usuarioInformacion.getIdOrganizacion().getId(),fechaInicial, fechaFinal, proveedor);
			Query cantidad = entityManager.createNativeQuery(queryCantidad.toString());
			BigInteger countResult = (BigInteger) cantidad.getSingleResult();
			Integer cantidadTotal = countResult.intValue();
			Page<FacturaCompra> result = new PageImpl<FacturaCompra>(facturas, paging, cantidadTotal);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de facturas exitosa");
			respuestaDto.setObjetoRespuesta(result);
			generarQueryTotalInformeFacturasVencida(querySuma, usuarioInformacion.getIdOrganizacion().getId(),
					fechaInicial, fechaFinal, proveedor);
			generarQueryTotalAbonos(queryAbonos, usuarioInformacion.getIdOrganizacion().getId(), fechaInicial,
					fechaFinal, proveedor);
			generarQueryTotalDescuentos(queryDescuentos, usuarioInformacion.getIdOrganizacion().getId(), fechaInicial,
					fechaFinal, proveedor);
			generarQueryTotalAsignacion(queryAsignacion, usuarioInformacion.getIdOrganizacion().getId(), fechaInicial,
					fechaFinal, proveedor);
			Query SumaQuery = entityManager.createNativeQuery(querySuma.toString());
			Query abonosQuery = entityManager.createNativeQuery(queryAbonos.toString());
			Query descuentosQuery = entityManager.createNativeQuery(queryDescuentos.toString());
			Query asignacionQuery = entityManager.createNativeQuery(queryAsignacion.toString());
			BigDecimal sumaResult = (BigDecimal) SumaQuery.getSingleResult();
			Double abonosResult = (Double) abonosQuery.getSingleResult();
			Double descuentosResult = (Double) descuentosQuery.getSingleResult();
			BigDecimal asignacionResult = (BigDecimal) asignacionQuery.getSingleResult();
			Integer suma = sumaResult.intValue();
			Integer abonos = abonosResult.intValue();
			Integer descuentos = descuentosResult.intValue();
			Integer asignacion = asignacionResult.intValue();
			respuestaDto.setVariable(String.valueOf(suma - abonos - descuentos - asignacion));
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error("Error obteniendo facturas " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error obteniendo facturas");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	private void generarQueryInformeFacturasVencida(StringBuilder query, Integer idSede, String fechaInicio,
			String fechaFin, Integer proveedor) {

		query.append("select * FROM fc_factura_compra where cod_estado_con =5 and id_sede=");

		query.append("" + idSede);
		if (!fechaInicio.equals("null") && fechaFin.equals("null")) {
			query.append(" and date_format(date(d_fecha_factura),'%Y-%m-%d')" + "= date('" + fechaInicio + "')");
		} else if (fechaInicio.equals("null") && !fechaFin.equals("null")) {
			query.append(" and date_format(date(d_fecha_factura),'%Y-%m-%d')" + "<= date('" + fechaFin + "')");
		} else if (!fechaInicio.equals("null") && !fechaFin.equals("null")) {
			query.append(" and date_format(date(d_fecha_factura),'%Y-%m-%d')" + "between " + "date('" + fechaInicio
					+ "') and " + "date('" + fechaFin + "')");
		}
		if (proveedor != 0) {
			query.append(" and nid_proveedor=" + proveedor);
		}
		query.append(
				" and DATEDIFF(d_fecha_vencimiento,CURDATE()) < 0 order by DATEDIFF(d_fecha_vencimiento,CURDATE())");
	}

	private void generarQueryCantidadInformeFacturasVencida(StringBuilder query, Integer idSede, String fechaInicio,
			String fechaFin, Integer proveedor) {

		query.append("select count(*) FROM fc_factura_compra where cod_estado_con =5 and id_sede=");

		query.append("" + idSede);
		if (!fechaInicio.equals("null") && fechaFin.equals("null")) {
			query.append(" and date_format(date(d_fecha_factura),'%Y-%m-%d')" + "= date('" + fechaInicio + "')");
		} else if (fechaInicio.equals("null") && !fechaFin.equals("null")) {
			query.append(" and date_format(date(d_fecha_factura),'%Y-%m-%d')" + "<= date('" + fechaFin + "')");
		} else if (!fechaInicio.equals("null") && !fechaFin.equals("null")) {
			query.append(" and date_format(date(d_fecha_factura),'%Y-%m-%d')" + "between " + "date('" + fechaInicio
					+ "') and " + "date('" + fechaFin + "')");
		}
		if (proveedor != 0) {
			query.append(" and nid_proveedor=" + proveedor);
		}
		query.append(
				" and DATEDIFF(d_fecha_vencimiento,CURDATE()) < 0 order by DATEDIFF(d_fecha_vencimiento,CURDATE())");
	}

	private void generarQueryTotalInformeFacturasVencida(StringBuilder query, Integer idSede, String fechaInicio,
			String fechaFin, Integer proveedor) {

		query.append("select ifnull(sum(m_total),0) FROM fc_factura_compra where cod_estado_con =5 and id_sede=");

		query.append("" + idSede);
		if (!fechaInicio.equals("null") && fechaFin.equals("null")) {
			query.append(" and date_format(date(d_fecha_factura),'%Y-%m-%d')" + "= date('" + fechaInicio + "')");
		} else if (fechaInicio.equals("null") && !fechaFin.equals("null")) {
			query.append(" and date_format(date(d_fecha_factura),'%Y-%m-%d')" + "<= date('" + fechaFin + "')");
		} else if (!fechaInicio.equals("null") && !fechaFin.equals("null")) {
			query.append(" and date_format(date(d_fecha_factura),'%Y-%m-%d')" + "between " + "date('" + fechaInicio
					+ "') and " + "date('" + fechaFin + "')");
		}
		if (proveedor != 0) {
			query.append(" and nid_proveedor=" + proveedor);
		}
		query.append(
				" and DATEDIFF(d_fecha_vencimiento,CURDATE()) < 0 order by DATEDIFF(d_fecha_vencimiento,CURDATE())");
	}

	private void generarQueryTotalComprobante(StringBuilder query, Integer idSede, String fechaInicio, String fechaFin,
			Integer proveedor) {

		query.append("select sum(m_total) FROM fc_factura_compra where cod_estado_con =5 and id_sede=");

		query.append("" + idSede);
		if (!fechaInicio.equals("null") && fechaFin.equals("null")) {
			query.append(" and date_format(date(d_fecha_factura),'%Y-%m-%d')" + "= date('" + fechaInicio + "')");
		} else if (fechaInicio.equals("null") && !fechaFin.equals("null")) {
			query.append(" and date_format(date(d_fecha_factura),'%Y-%m-%d')" + "<= date('" + fechaFin + "')");
		} else if (!fechaInicio.equals("null") && !fechaFin.equals("null")) {
			query.append(" and date_format(date(d_fecha_factura),'%Y-%m-%d')" + "between " + "date('" + fechaInicio
					+ "') and " + "date('" + fechaFin + "')");
		}
		if (proveedor != 0) {
			query.append(" and nid_proveedor=" + proveedor);
		}
		query.append(
				" and DATEDIFF(d_fecha_vencimiento,CURDATE()) < 0 order by DATEDIFF(d_fecha_vencimiento,CURDATE())");
	}

	private void generarQueryTotalAbonos(StringBuilder query, Integer idSede, String fechaInicio, String fechaFin,
			Integer proveedor) {

		query.append(
				"select ifnull(sum(rbe.t_valor_abono),0) from rbe_recibo_egreso_conceptos rbe join fc_factura_compra fc on fc.t_nro_factura = numero_documento where fc.cod_estado_con =5 and fc.id_sede=");

		query.append("" + idSede);
		if (!fechaInicio.equals("null") && fechaFin.equals("null")) {
			query.append(" and date_format(date(fc.d_fecha_factura),'%Y-%m-%d')" + "= date('" + fechaInicio + "')");
		} else if (fechaInicio.equals("null") && !fechaFin.equals("null")) {
			query.append(" and date_format(date(fc.d_fecha_factura),'%Y-%m-%d')" + "<= date('" + fechaFin + "')");
		} else if (!fechaInicio.equals("null") && !fechaFin.equals("null")) {
			query.append(" and date_format(date(fc.d_fecha_factura),'%Y-%m-%d')" + "between " + "date('" + fechaInicio
					+ "') and " + "date('" + fechaFin + "')");
		}
		if (proveedor != 0) {
			query.append(" and fc.nid_proveedor=" + proveedor);
		}
		query.append(" and DATEDIFF(d_fecha_vencimiento,CURDATE()) < 0");
	}

	private void generarQueryTotalDescuentos(StringBuilder query, Integer idSede, String fechaInicio, String fechaFin,
			Integer proveedor) {

		query.append(
				"select ifnull(sum(rbe.descuento),0) from rbe_recibo_egreso_conceptos rbe join fc_factura_compra fc on fc.t_nro_factura = numero_documento where fc.cod_estado_con =5 and fc.id_sede=");

		query.append("" + idSede);
		if (!fechaInicio.equals("null") && fechaFin.equals("null")) {
			query.append(" and date_format(date(fc.d_fecha_factura),'%Y-%m-%d')" + "= date('" + fechaInicio + "')");
		} else if (fechaInicio.equals("null") && !fechaFin.equals("null")) {
			query.append(" and date_format(date(fc.d_fecha_factura),'%Y-%m-%d')" + "<= date('" + fechaFin + "')");
		} else if (!fechaInicio.equals("null") && !fechaFin.equals("null")) {
			query.append(" and date_format(date(fc.d_fecha_factura),'%Y-%m-%d')" + "between " + "date('" + fechaInicio
					+ "') and " + "date('" + fechaFin + "')");
		}
		if (proveedor != 0) {
			query.append(" and fc.nid_proveedor=" + proveedor);
		}
		query.append(" and DATEDIFF(d_fecha_vencimiento,CURDATE()) < 0");
	}

	private void generarQueryTotalAsignacion(StringBuilder query, Integer idSede, String fechaInicio, String fechaFin,
			Integer proveedor) {

		query.append(
				"select ifnull(sum(nc.total),0) from rbe_recibo_egreso_conceptos rbe join fc_factura_compra fc on fc.t_nro_factura = rbe.numero_documento join asignaciones_comprobante asig\r\n"
						+ "on rbe.numero_documento = asig.numero_fact join nota_credito nc on asig.nota_credito_id = nc.id where fc.cod_estado_con =5 and fc.id_sede=");
		query.append("" + idSede);
		if (!fechaInicio.equals("null") && fechaFin.equals("null")) {
			query.append(" and date_format(date(fc.d_fecha_factura),'%Y-%m-%d')" + "= date('" + fechaInicio + "')");
		} else if (fechaInicio.equals("null") && !fechaFin.equals("null")) {
			query.append(" and date_format(date(fc.d_fecha_factura),'%Y-%m-%d')" + "<= date('" + fechaFin + "')");
		} else if (!fechaInicio.equals("null") && !fechaFin.equals("null")) {
			query.append(" and date_format(date(fc.d_fecha_factura),'%Y-%m-%d')" + "between " + "date('" + fechaInicio
					+ "') and " + "date('" + fechaFin + "')");
		}
		if (proveedor != 0) {
			query.append(" and fc.nid_proveedor=" + proveedor);
		}
		query.append(" and DATEDIFF(d_fecha_vencimiento,CURDATE()) < 0");
	}

}

class OrdenPorFecha1 implements Comparator<InfromeFacturasVencidas> {
	public int compare(InfromeFacturasVencidas a, InfromeFacturasVencidas b) {
		return (int) (a.getDiasVencidos() - b.getDiasVencidos());
	}
}
