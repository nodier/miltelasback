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
import com.softlond.base.entity.Barrio;
import com.softlond.base.entity.DevolucionCompras;
import com.softlond.base.entity.EstadoDocumento;
import com.softlond.base.entity.FacturaCompra;
import com.softlond.base.entity.InformacionUsuario;
import com.softlond.base.entity.Organizacion;
import com.softlond.base.entity.Usuario;
import com.softlond.base.repository.AsignacionComprobanteDao;
import com.softlond.base.repository.ConceptoReciboEgresoDao;
import com.softlond.base.repository.FacturaCompraDao;
import com.softlond.base.repository.FacturaDevolucionDao;
import com.softlond.base.repository.UsuarioInformacionDao;
import com.softlond.base.response.AsignacionesPendientes;
import com.softlond.base.response.CuentasPorPagar;
import com.softlond.base.response.Paginacion;

@Service
public class AsignacionesPendientesService {

	@Autowired
	private FacturaCompraDao facturaDao;

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

	public List<AsignacionesPendientes> obtenerAsignacionesPendientes(int idProveedor) {
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		int idSede = usuarioInformacion.getIdOrganizacion().getId();
		List<AsignacionesPendientes> ap = new ArrayList<AsignacionesPendientes>();
		List<FacturaCompra> facturas = facturaDao.obtenerFacturasSinAsignarComprobante(idProveedor, idSede);
		for (FacturaCompra f : facturas) {
			AsignacionesPendientes asignacion = AsignacionesPendientes.convertirFactura(f);
			Integer abonos = conceptoReciboEgresoDao.obtenerTotalAbonos(asignacion.getNumero(), idSede);
			if (abonos != null) {
				asignacion.setSaldo(asignacion.getTotal() - abonos);
				asignacion.setAbonos(abonos);
			}
			ap.add(asignacion);
		}
		return ap;
	}

	public Paginacion obtenerAsignacionesPorProveedorPaginado(Integer idProveedor, int pagina) {
		List<AsignacionesPendientes> cp = obtenerAsignacionesPendientes(idProveedor);
		return Paginacion.paginar(cp, ITEMS_POR_PAGINA, pagina);
	}

	public ResponseEntity<Object> crearAsignacion(AsignacionComprobante asignacion) {
		ResponseEntity<Object> respuesta;
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		Organizacion sede = usuarioInformacion.getIdOrganizacion();
		try {
			FacturaCompra factura = this.facturaDao.obtenerFacturaNumero(asignacion.getNumero(), sede.getId());
			if(factura.getTotal()-asignacion.getNotaCredito().getTotal() <= 0) {
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
	
	public List<AsignacionesPendientes> obtenerAsignacionesPendientesActualizar(int idProveedor) {
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		int idSede = usuarioInformacion.getIdOrganizacion().getId();
		List<AsignacionesPendientes> ap = new ArrayList<AsignacionesPendientes>();
		List<FacturaCompra> facturas = facturaDao.obtenerFacturasSinAsignarComprobanteActualizar(idProveedor, idSede);
		for (FacturaCompra f : facturas) {
			AsignacionesPendientes asignacion = AsignacionesPendientes.convertirFactura(f);
			Integer abonos = conceptoReciboEgresoDao.obtenerTotalAbonos(asignacion.getNumero(), idSede);
			if (abonos != null) {
				asignacion.setSaldo(asignacion.getTotal() - abonos);
				asignacion.setAbonos(abonos);
			}
			ap.add(asignacion);
		}
		return ap;
	}

	public Paginacion obtenerAsignacionesPorProveedorPaginadoActualizar(Integer idProveedor, int pagina) {
		List<AsignacionesPendientes> cp = obtenerAsignacionesPendientesActualizar(idProveedor);
		return Paginacion.paginar(cp, ITEMS_POR_PAGINA, pagina);
	}
	
	

}



