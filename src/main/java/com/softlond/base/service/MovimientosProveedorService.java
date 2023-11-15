package com.softlond.base.service;

import com.softlond.base.entity.ComprobanteEgreso;
import com.softlond.base.entity.FacturaCompra;
import com.softlond.base.entity.InformacionUsuario;
import com.softlond.base.entity.NotaCredito;
import com.softlond.base.entity.NotaDebito;
import com.softlond.base.entity.ProveedorCondicionesComerciales;
import com.softlond.base.entity.Usuario;
import com.softlond.base.repository.AsignacionComprobanteDao;
import com.softlond.base.repository.ComprobanteEgresoDao;
import com.softlond.base.repository.ConceptoReciboEgresoDao;
import com.softlond.base.repository.FacturaCompraDao;
import com.softlond.base.repository.ProveedorCondicionesComercialesDao;
import com.softlond.base.repository.UsuarioInformacionDao;
import com.softlond.base.response.EstadosCuenta;
import com.softlond.base.response.MovimientoProveedor;
import com.softlond.base.response.Paginacion;
import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service
public class MovimientosProveedorService {

	final int ITEMS_POR_PAGINA = 10;

	@Autowired
	private FacturaCompraService servicioFactura;
	@Autowired
	private NotaDebitoService servicioND;
	@Autowired
	private NotaCreditoService servicioNC;
	@Autowired
	private ConceptoReciboEgresoDao conceptoReciboEgresoDao;

	@Autowired
	private AsignacionComprobanteDao asignacionComprobanteDao;

	@Autowired
	private ProveedorCondicionesComercialesDao condicionesComercialesDao;

	@Autowired
	private ComprobanteEgresoDao comprobanteEgresoDao;

	@Autowired
	UsuarioInformacionDao usuarioInformacionDao;

	@Autowired
	private FacturaCompraDao facturaCompraDao;

	private static final Logger logger = Logger.getLogger(MovimientosProveedorService.class);

	public List<MovimientoProveedor> obtenerMovimientosPorProveedor(int idProveedor, Date fechaInicial,
			Date fechaFinal) {
		List<MovimientoProveedor> movs = new ArrayList<>();
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		int idSede = usuarioInformacion.getIdOrganizacion().getId();
		// Obtener facturas
		List<FacturaCompra> facturas = servicioFactura.facturasPorProveedor(idProveedor, fechaInicial, fechaFinal,
				idSede);
		for (FacturaCompra f : facturas) {
			Integer abonos = conceptoReciboEgresoDao.obtenerTotalAbonos(f.getNroFactura(), idSede);
			MovimientoProveedor movimiento = MovimientoProveedor.convertirFactura(f);
			Integer asignacion = asignacionComprobanteDao.obtenerTotal(f.getNroFactura(), idSede);
			Integer descuentos = conceptoReciboEgresoDao.obtenerTotalDescuentos(f.getNroFactura(), idSede);
			if (f.getCodEstadoCon().getId() == 4) {
				movimiento.setSaldo(0);
			} else {
				if (asignacion != null)
					movimiento.setSaldo(movimiento.getTotal() - asignacion - descuentos);
				if (abonos != null)
					movimiento.setSaldo(movimiento.getSaldo() - abonos);
			}
			movs.add(movimiento);
		}

		// Obtener Notas débito
		List<NotaDebito> notasDebito = servicioND.notasDebitoPorProveedor(idProveedor, fechaInicial, fechaFinal,
				idSede);
		for (NotaDebito nd : notasDebito) {
			MovimientoProveedor movimiento = MovimientoProveedor.convertirNotaDeb(nd);
			Integer abonos = conceptoReciboEgresoDao.obtenerTotalAbonos(movimiento.getNumero(), idSede);
			Integer asignacion = asignacionComprobanteDao.obtenerTotal(movimiento.getNumero(), idSede);
			Integer descuentos = conceptoReciboEgresoDao.obtenerTotalDescuentos(movimiento.getNumero(), idSede);
			if (nd.getEstadoDocumento().equals("Pagado")) {
				movimiento.setSaldo(0);
				movimiento.setTotal(0);
			} else {
				if (asignacion != null)
					movimiento.setSaldo(movimiento.getTotal() - asignacion - descuentos);
				if (abonos != null)
					movimiento.setSaldo(movimiento.getSaldo() - abonos);
			}
			movs.add(movimiento);
		}

		// Obtener Notas crédito
		List<NotaCredito> notasCredito = servicioNC.notasCreditoPorProveedor(idProveedor, fechaInicial, fechaFinal,
				idSede);
		for (NotaCredito nc : notasCredito) {
			MovimientoProveedor movimiento = MovimientoProveedor.convertirNotaCred(nc);
			Integer abonos = conceptoReciboEgresoDao.obtenerTotalAbonos(movimiento.getNumero(), idSede);
			if (abonos != null)
				movimiento.setSaldo(movimiento.getTotal() - abonos);
			movs.add(movimiento);
		}

		// Obtener comprobantes
		List<ComprobanteEgreso> comprobantes = comprobanteEgresoDao.obtenerComprobantesFiltros(idProveedor,
				fechaInicial, fechaFinal, idSede);
		for (ComprobanteEgreso c : comprobantes) {
			MovimientoProveedor movimiento = MovimientoProveedor.convertirComprobanteEgreso(c);
			Integer abonos = conceptoReciboEgresoDao.obtenerTotalAbonos(movimiento.getNumero(), idSede);
			movs.add(movimiento);
		}
		movs.sort(new OrdenPorFecha());
		return movs;
	}

	public Paginacion obtenerMovimientosPorProveedorPaginado(int idProveedor, Date fechaInicial, Date fechaFinal,
			int pagina) {
		List<MovimientoProveedor> movs = obtenerMovimientosPorProveedor(idProveedor, fechaInicial, fechaFinal);
		return Paginacion.paginar(movs, ITEMS_POR_PAGINA, pagina);
	}

	public List<MovimientoProveedor> obtenerPendientes(Integer idProveedor) {
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao.buscarPorIdUsuario(usuarioAutenticado.getId());
		int idSede = usuarioInformacion.getIdOrganizacion().getId();
		List<MovimientoProveedor> movs = new ArrayList<>();
		List<NotaCredito> notas = servicioNC.obtenerPendientesPorProveedor(idProveedor, idSede);
		for (NotaCredito nc : notas) {
			MovimientoProveedor movimiento = MovimientoProveedor.convertirNotaCred(nc);
			Integer abonos = conceptoReciboEgresoDao.obtenerTotalAbonos(movimiento.getNumero(), idSede);
			if (abonos != null)
				movimiento.setSaldo(movimiento.getTotal() - abonos);
			movs.add(movimiento);
		}
		List<NotaDebito> notasD = servicioND.obtenerPendientesPorProveedor(idProveedor, idSede);
		for (NotaDebito nd : notasD) {
			MovimientoProveedor movimiento = MovimientoProveedor.convertirNotaDeb(nd);
			Integer abonos = conceptoReciboEgresoDao.obtenerTotalAbonos(movimiento.getNumero(), idSede);
			Integer asignacion = asignacionComprobanteDao.obtenerTotal(movimiento.getNumero(), idSede);
			Integer descuentos = conceptoReciboEgresoDao.obtenerTotalDescuentos(movimiento.getNumero(), idSede);
			movimiento.setDescuento(calcularDescuento(nd.getIdProveedor().getId(), movimiento));
			movimiento.setSaldo(movimiento.getTotal() - abonos - asignacion - descuentos);
			movs.add(movimiento);
		}

		List<FacturaCompra> facturas = servicioFactura.facturasPorProveedor(idProveedor, idSede);
		for (FacturaCompra f : facturas) {
			Integer abonos = conceptoReciboEgresoDao.obtenerTotalAbonos(f.getNroFactura(), idSede);
			MovimientoProveedor movimiento = MovimientoProveedor.convertirFactura(f);
			Integer asignacion = asignacionComprobanteDao.obtenerTotal(f.getNroFactura(), idSede);
			Integer descuentos = conceptoReciboEgresoDao.obtenerTotalDescuentos(f.getNroFactura(), idSede);
			movimiento.setDescuento(calcularDescuento(f.getProveedor().getId(), movimiento));
			// if (f.getCodEstadoCon().getId() == 4) {
			// movimiento.setSaldo(0);
			// } else {
			movimiento.setSaldo(movimiento.getTotal() - abonos - descuentos);
			movimiento.setSaldo(movimiento.getSaldo() - asignacion);
			// }
			movs.add(movimiento);
		}
		movs.sort(new OrdenPorFechaDescendente());
		return movs;
	}

	public List<MovimientoProveedor> obtenerPendientesActualizar(Integer idProveedor, Integer idComprobante) {

		List<MovimientoProveedor> movs = new ArrayList<>();
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao.buscarPorIdUsuario(usuarioAutenticado.getId());
		int idSede = usuarioInformacion.getIdOrganizacion().getId();
		List<NotaCredito> notas = servicioNC.obtenerPendientesPorProveedor(idProveedor, idSede);
		for (NotaCredito nc : notas) {
			MovimientoProveedor movimiento = MovimientoProveedor.convertirNotaCred(nc);
			Integer abonos = conceptoReciboEgresoDao.obtenerTotalAbonos(movimiento.getNumero(), idSede);
			if (abonos != null)
				movimiento.setSaldo(movimiento.getTotal() - abonos);
			movs.add(movimiento);
		}

		List<FacturaCompra> facturasPendientes = servicioFactura.facturasPorProveedor(idProveedor, idSede);
		List<FacturaCompra> facturas = servicioFactura.facturasPorProveedorActualizar(idProveedor, idComprobante,
				idSede);
		for (FacturaCompra f : facturas) {
			Integer abonos = conceptoReciboEgresoDao.obtenerTotalAbonos(f.getNroFactura(), idSede);

			MovimientoProveedor movimiento = MovimientoProveedor.convertirFactura(f);
			movimiento.setDescuento(conceptoReciboEgresoDao.obtenerDescuento(f.getNroFactura(), idSede, idComprobante));
			Integer asignacion = asignacionComprobanteDao.obtenerTotal(f.getNroFactura(), idSede);
			if (f.getCodEstadoCon().getId() == 4) {
				Integer abono = conceptoReciboEgresoDao.obtenerTotalAbono(movimiento.getNumero(), idComprobante);
				double d_val = movimiento.getDescuento();
				int i_val = (int) d_val;
				movimiento.setSaldo(abono + i_val);
			} else {
				if (abonos != null)
					movimiento.setSaldo(movimiento.getTotal() - abonos);
				if (asignacion != null)
					movimiento.setSaldo(movimiento.getSaldo() - asignacion);
			}
			movs.add(movimiento);
		}
		for (FacturaCompra f : facturasPendientes) {
			boolean existeMovimiento = false;
			for (MovimientoProveedor mov : movs) {
				if (mov.getNumero().equals(f.getNroFactura())) {
					existeMovimiento = true;
					Integer abono = conceptoReciboEgresoDao.obtenerTotalAbono(mov.getNumero(), idComprobante);
					mov.setSaldo(mov.getSaldo() + abono);
					break;
				}
			}
			if (!existeMovimiento) {
				Integer abonos = conceptoReciboEgresoDao.obtenerTotalAbonos(f.getNroFactura(), idSede);
				MovimientoProveedor movimiento = MovimientoProveedor.convertirFactura(f);
				Integer asignacion = asignacionComprobanteDao.obtenerTotal(f.getNroFactura(), idSede);
				Integer descuentos = conceptoReciboEgresoDao.obtenerTotalDescuentos(f.getNroFactura(), idSede);
				movimiento.setDescuento(calcularDescuento(f.getProveedor().getId(), movimiento));
				if (f.getCodEstadoCon().getId() == 4) {
					movimiento.setSaldo(0);
				} else {
					movimiento.setSaldo(movimiento.getTotal() - abonos - descuentos);
					movimiento.setSaldo(movimiento.getSaldo() - asignacion);
				}
				movs.add(movimiento);
			}
		}
		List<NotaDebito> notasD = servicioND.obtenerPendientesPorProveedor(idProveedor, idSede);
		List<NotaDebito> notasDActualizar = servicioND.obtenerPendientesPorProveedorActualizar(idProveedor, idSede,
				idComprobante);
		for (NotaDebito nd : notasDActualizar) {
			MovimientoProveedor movimiento = MovimientoProveedor.convertirNotaDeb(nd);
			Integer abonos = conceptoReciboEgresoDao.obtenerTotalAbonos(movimiento.getNumero(), idSede);
			Integer asignacion = asignacionComprobanteDao.obtenerTotal(movimiento.getNumero(), idSede);
			Integer descuentos = conceptoReciboEgresoDao.obtenerTotalDescuentos(movimiento.getNumero(), idSede);
			movimiento.setDescuento(conceptoReciboEgresoDao
					.obtenerDescuento(nd.getPrefijo().getPrefijo() + "-" + nd.getNumeroDocumento(), idSede, idComprobante));
			movimiento.setSaldo(movimiento.getTotal() - abonos - asignacion - descuentos);
			movs.add(movimiento);
		}
		for (NotaDebito nd : notasD) {
			boolean existeMovimiento = false;
			for (MovimientoProveedor mov : movs) {
				if (mov.getNumero().equals(nd.getPrefijo().getPrefijo() + "-" + nd.getNumeroDocumento())) {
					existeMovimiento = true;
					Integer abono = conceptoReciboEgresoDao.obtenerTotalAbono(mov.getNumero(), idComprobante);
					mov.setSaldo(mov.getSaldo() + abono);
					break;
				}
			}
			if (!existeMovimiento) {
				MovimientoProveedor movimiento = MovimientoProveedor.convertirNotaDeb(nd);
				Integer abonos = conceptoReciboEgresoDao.obtenerTotalAbonos(movimiento.getNumero(), idSede);
				Integer asignacion = asignacionComprobanteDao.obtenerTotal(movimiento.getNumero(), idSede);
				Integer descuentos = conceptoReciboEgresoDao.obtenerTotalDescuentos(movimiento.getNumero(), idSede);
				movimiento.setDescuento(calcularDescuento(nd.getIdProveedor().getId(), movimiento));
				movimiento.setSaldo(movimiento.getTotal() - abonos - asignacion - descuentos);
				movs.add(movimiento);
			}
		}
		movs.sort(new OrdenPorFechaDescendente());

		return movs;
	}

	public Integer obtenerSaldoTotal(List<MovimientoProveedor> movimientos) {
		Integer totalSaldo = 0;
		for (MovimientoProveedor movimiento : movimientos) {
			if (!movimiento.getTipo().equals("Nota Crédito")) {
				totalSaldo += movimiento.getSaldo();
			}
		}
		return totalSaldo;
	}

	private double calcularDescuento(Integer idProveedor, MovimientoProveedor mov) {
		List<ProveedorCondicionesComerciales> condiciones = condicionesComercialesDao
				.obtenerProductosProveedoresbyProveedor(idProveedor);
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		int idSede = usuarioInformacion.getIdOrganizacion().getId();
		long tiempoFecha = mov.getFecha().getTime();
		long tiempoActual = new Date().getTime();
		long diff = tiempoActual - tiempoFecha;
		int dias = (int) (diff / (1000 * 60 * 60 * 24));
		int i = 0;
		double descuento = 0;
		while (i < condiciones.size() && dias <= condiciones.get(i).getDias() && dias > 0) {
			descuento = condiciones.get(i).getDescuento();
			i++;
		}

		int i_val = 0;
		logger.info(mov.getNumero());
		if (facturaCompraDao.obtenerFacturaSubtotal(mov.getNumero(), idSede) != null) {
			double SubtotalFacturaCompra = facturaCompraDao.obtenerFacturaSubtotal(mov.getNumero(), idSede);
			double valor = ((SubtotalFacturaCompra * descuento) / 100);
			i_val = (int) valor;
		}
		return i_val;
	}
}

class OrdenPorFecha implements Comparator<MovimientoProveedor> {
	public int compare(MovimientoProveedor a, MovimientoProveedor b) {
		return (int) (b.getFecha().getTime() - a.getFecha().getTime());
	}
}

class OrdenPorFechaDescendente implements Comparator<MovimientoProveedor> {
	public int compare(MovimientoProveedor a, MovimientoProveedor b) {
		// return (int) (((b.getFecha().getTime()) - (a.getFecha().getTime())));
		return a.getFecha().compareTo(b.getFecha());
	}
}
