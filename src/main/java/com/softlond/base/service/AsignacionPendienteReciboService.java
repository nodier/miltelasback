package com.softlond.base.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.AsignacionComprobante;
import com.softlond.base.entity.EstadoDocumento;
import com.softlond.base.entity.Factura;
import com.softlond.base.entity.FacturaCompra;
import com.softlond.base.entity.InformacionUsuario;
import com.softlond.base.entity.Organizacion;
import com.softlond.base.entity.Usuario;
import com.softlond.base.repository.AsignacionComprobanteDao;
import com.softlond.base.repository.ConceptoReciboEgresoDao;
import com.softlond.base.repository.FacturaCompraDao;
import com.softlond.base.repository.FacturaDao;
import com.softlond.base.repository.FacturaDevolucionDao;
import com.softlond.base.repository.UsuarioInformacionDao;
import com.softlond.base.response.AsignacionesPendientes;
import com.softlond.base.response.AsignacionesPendientesRecibo;
import com.softlond.base.response.Paginacion;

@Service
public class AsignacionPendienteReciboService {

	@Autowired
	private FacturaDao facturaDao;

	@Autowired
	private FacturaDevolucionDao devolucionDao;

	@Autowired
	private ConceptoReciboEgresoDao conceptoReciboEgresoDao;

	@Autowired
	private AsignacionComprobanteDao asignacionComprobanteDao;

	@Autowired
	UsuarioInformacionDao usuarioInformacionDao;

	final int ITEMS_POR_PAGINA = 10;

	private static final Logger logger = Logger.getLogger(AsignacionesPendientesService.class);

	public List<AsignacionesPendientesRecibo> obtenerAsignacionesPendientes(int idCliente) {
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		Organizacion sede = usuarioInformacion.getIdOrganizacion();
		List<AsignacionesPendientesRecibo> ap = new ArrayList<AsignacionesPendientesRecibo>();
		// List<Factura> facturas =
		// facturaDao.FacturasDisponiblesAsignarCliente(idCliente);
		List<Factura> facturas = facturaDao.FacturasDisponiblesAsignarCliente2(idCliente, sede.getId());

		for (Factura f : facturas) {
			AsignacionesPendientesRecibo asignacion = AsignacionesPendientesRecibo.convertirFactura(f);
			Integer abonos = conceptoReciboEgresoDao.obtenerTotalAbonos(asignacion.getNumero(), sede.getId());
			if (abonos != null) {
				asignacion.setSaldo(asignacion.getTotal() - abonos);
				asignacion.setAbonos(abonos);
			}
			ap.add(asignacion);
		}
		return ap;
	}

	public Paginacion obtenerAsignacionesPorProveedorPaginado(Integer idProveedor, int pagina) {
		List<AsignacionesPendientesRecibo> cp = obtenerAsignacionesPendientes(idProveedor);
		return Paginacion.paginar(cp, ITEMS_POR_PAGINA, pagina);
	}

	public ResponseEntity<Object> crearAsignacion(AsignacionComprobante asignacion) {
		ResponseEntity<Object> respuesta;
		try {
			Factura factura = this.facturaDao.facturaPorNumero(asignacion.getNumero());
			if (factura.getTotal() - asignacion.getNotaCredito().getTotal() <= 0) {
				EstadoDocumento estado = new EstadoDocumento();
				estado.setId(4);
				factura.setCodEstadoCon(estado);
				this.facturaDao.save(factura);
			}
			this.asignacionComprobanteDao.save(asignacion);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de barrios exitosa");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error("Error al crear asignacion" + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error crear asignacion" + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	public List<AsignacionesPendientesRecibo> obtenerAsignacionesPendientesActualizar(int idCliente) {
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		Organizacion sede = usuarioInformacion.getIdOrganizacion();
		List<AsignacionesPendientesRecibo> ap = new ArrayList<AsignacionesPendientesRecibo>();
		List<Factura> facturas = facturaDao.FacturasDisponiblesAsignarClienteActualizar(idCliente);
		for (Factura f : facturas) {
			AsignacionesPendientesRecibo asignacion = AsignacionesPendientesRecibo.convertirFactura(f);
			Integer abonos = conceptoReciboEgresoDao.obtenerTotalAbonos(asignacion.getNumero(), sede.getId());
			if (abonos != null) {
				asignacion.setSaldo(asignacion.getTotal() - abonos);
				asignacion.setAbonos(abonos);
			}
			ap.add(asignacion);
		}
		return ap;
	}

	public Paginacion obtenerAsignacionesPorProveedorPaginadoActualizar(Integer idProveedor, int pagina) {
		List<AsignacionesPendientesRecibo> cp = obtenerAsignacionesPendientesActualizar(idProveedor);
		return Paginacion.paginar(cp, ITEMS_POR_PAGINA, pagina);
	}

}
