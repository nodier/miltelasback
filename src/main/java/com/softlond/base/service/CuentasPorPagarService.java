package com.softlond.base.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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
import com.softlond.base.repository.NotaCreditoDao;
import com.softlond.base.repository.NotaDebitoDao;
import com.softlond.base.repository.ProveedorCondicionesComercialesDao;
import com.softlond.base.repository.UsuarioInformacionDao;
import com.softlond.base.response.CuentasPorPagar;
import com.softlond.base.response.EstadosCuenta;
import com.softlond.base.response.MovimientoProveedor;
import com.softlond.base.response.Paginacion;

@Service
public class CuentasPorPagarService {

	final int ITEMS_POR_PAGINA = 10;

	private final Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private FacturaCompraDao facturaDao;
	@Autowired
	private NotaDebitoDao ndDao;
	@Autowired
	private NotaCreditoDao ncDao;

	@Autowired
	UsuarioInformacionDao usuarioInformacionDao;

	@Autowired
	private ProveedorCondicionesComercialesDao condicionesComercialesDao;

	@Autowired
	private ConceptoReciboEgresoDao conceptoReciboEgresoDao;

	@Autowired
	private AsignacionComprobanteDao asignacionComprobanteDao;

	@Autowired
	private ComprobanteEgresoDao comprobanteEgresoDao;

	public List<CuentasPorPagar> obtenerCuentasPorPagar(int idProveedor) {
		List<CuentasPorPagar> cp = new ArrayList<>();
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		int idSede = usuarioInformacion.getIdOrganizacion().getId();
		int saldototal = 0;
		// Obtener facturas
		List<FacturaCompra> facturas = facturaDao.obtenerFacturasPorPagar(idProveedor, idSede);
		for (FacturaCompra f : facturas) {
			CuentasPorPagar cuenta = CuentasPorPagar.convertirFactura(f);
			cuenta.setDescuento(this.calcularDescuento(f.getProveedor().getId(), cuenta));
			Integer abonos = conceptoReciboEgresoDao.obtenerTotalAbonos(cuenta.getNumero(), idSede);
			Integer asignacion = asignacionComprobanteDao.obtenerTotal(f.getNroFactura(), idSede);
			Integer descuentos = conceptoReciboEgresoDao.obtenerTotalDescuentos(cuenta.getNumero(), idSede);
				    
			cuenta.setSaldo(cuenta.getTotal() - asignacion - abonos - descuentos);
			
			saldototal = saldototal + cuenta.getSaldo();
			cuenta.setAbonos(abonos + asignacion);
			cp.add(cuenta);
		}
          
		// Obtener Notas débito
		List<NotaDebito> notasDebito = ndDao.obtenerNotasDebitosPorPagar(idProveedor,idSede);
		for (NotaDebito nd : notasDebito) {
			CuentasPorPagar cuenta = CuentasPorPagar.convertirNotaDeb(nd);
			cuenta.setDescuento(this.calcularDescuento(nd.getIdProveedor().getId(), cuenta));
			Integer abonos = conceptoReciboEgresoDao.obtenerTotalAbonos(cuenta.getNumero(), idSede);
			Integer asignacion = asignacionComprobanteDao.obtenerTotal(cuenta.getNumero(), idSede);
			Integer descuentos = conceptoReciboEgresoDao.obtenerTotalDescuentos(cuenta.getNumero(), idSede);
			cuenta.setSaldo(cuenta.getTotal() - asignacion - abonos - descuentos);
			cuenta.setAbonos(abonos + asignacion);
			cp.add(cuenta);
		}

		// Obtener Notas crédito
		List<NotaCredito> notasCredito = ncDao.obtenerNotasCreditosPorPagar(idProveedor, idSede);
		for (NotaCredito nc : notasCredito) {
			CuentasPorPagar cuenta = CuentasPorPagar.convertirNotaCred(nc);
			cuenta.setDescuento(this.calcularDescuento(nc.getIdProveedor().getId(), cuenta));
			Integer abonos = conceptoReciboEgresoDao.obtenerTotalAbonos(cuenta.getNumero(), idSede);
			cuenta.setSaldo(cuenta.getTotal() - abonos);
			cuenta.setAbonos(abonos);
			cp.add(cuenta);
		}
             
		cp.sort(new OrdenPorFechaCuentasPorPagar());
		return cp;
	}

	public Paginacion obtenerCuentasPorPagarPorProveedorPaginado(int idProveedor, int pagina) {
		List<CuentasPorPagar> cp = obtenerCuentasPorPagar(idProveedor);
		return Paginacion.paginar(cp, ITEMS_POR_PAGINA, pagina);
	}

	public List<CuentasPorPagar> obtenerCuentasPorPagarAlmacen() {
		List<CuentasPorPagar> cp = new ArrayList<>();
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		Integer idSede = usuarioInformacion.getIdOrganizacion().getId();
		// Obtener facturas
		List<FacturaCompra> facturas = facturaDao.obtenerFacturasPorPagarSede1(idSede);
		for (FacturaCompra f : facturas) {
			CuentasPorPagar cuenta = CuentasPorPagar.convertirFactura(f);
			cuenta.setDescuento(this.calcularDescuento(f.getProveedor().getId(), cuenta));
			Integer abonos = conceptoReciboEgresoDao.obtenerTotalAbonos(cuenta.getNumero(), idSede);
			Integer asignacion = asignacionComprobanteDao.obtenerTotal(cuenta.getNumero(), idSede);
			Integer descuentos = conceptoReciboEgresoDao.obtenerTotalDescuentos(cuenta.getNumero(), idSede);
			cuenta.setSaldo(cuenta.getTotal() - asignacion - abonos - descuentos);
			cuenta.setAbonos(abonos + asignacion);
			cp.add(cuenta);
		}

		// Obtener Notas débito
		List<NotaDebito> notasDebito = ndDao.obtenerNotasDebitosPorPagarAlmacen(idSede);
		for (NotaDebito nd : notasDebito) {
			CuentasPorPagar cuenta = CuentasPorPagar.convertirNotaDeb(nd);
			cuenta.setDescuento(this.calcularDescuento(nd.getIdProveedor().getId(), cuenta));
			Integer abonos = conceptoReciboEgresoDao.obtenerTotalAbonos(cuenta.getNumero(), idSede);
			Integer asignacion = asignacionComprobanteDao.obtenerTotal(cuenta.getNumero(), idSede);
			Integer descuentos = conceptoReciboEgresoDao.obtenerTotalDescuentos(cuenta.getNumero(), idSede);
			cuenta.setSaldo(cuenta.getTotal() - asignacion - abonos - descuentos);
			cuenta.setAbonos(abonos + asignacion);
			cp.add(cuenta);
		}

		// Obtener Notas crédito
		List<NotaCredito> notasCredito = ncDao.obtenerNotasCreditosPorPagarAlmacen(idSede);
		for (NotaCredito nc : notasCredito) {
			CuentasPorPagar cuenta = CuentasPorPagar.convertirNotaCred(nc);
			cuenta.setDescuento(this.calcularDescuento(nc.getIdProveedor().getId(), cuenta));
			Integer abonos = conceptoReciboEgresoDao.obtenerTotalAbonos(cuenta.getNumero(), idSede);
			cuenta.setSaldo(cuenta.getTotal() - abonos);
			cuenta.setAbonos(abonos);
			cp.add(cuenta);
		}

		cp.sort(new OrdenPorFechaCuentasPorPagar());
		return cp;
	}

	public Paginacion obtenerCuentasPorPagarPorProveedorPaginadoAlmacen(int pagina) {
		List<CuentasPorPagar> cp = obtenerCuentasPorPagarAlmacen();
		return Paginacion.paginar(cp, ITEMS_POR_PAGINA, pagina);
	}

	public Integer sumar1(int idProveedor) {
		
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		Integer idSede = usuarioInformacion.getIdOrganizacion().getId();
		Integer totalFacturas = (facturaDao.obtenerTotalFacturasPorPagar(idProveedor, idSede) != null)
				? facturaDao.obtenerTotalFacturasPorPagar(idProveedor, idSede)
				: 0;
		
		Integer totalNotasCredito = (ncDao.obtenerTotalNotasCreditosPorPagar(idProveedor, idSede) != null)
				? ncDao.obtenerTotalNotasCreditosPorPagar(idProveedor, idSede)
				: 0;
		Integer totalNotasDebito = (ndDao.obtenerTotalNotasDebitosPorPagar(idProveedor, idSede) != null)
				? ndDao.obtenerTotalNotasDebitosPorPagar(idProveedor, idSede)
				: 0;
		Integer totalAbonos = comprobanteEgresoDao.obtenerValorAbonosProveedor(idProveedor, idSede)- comprobanteEgresoDao.obtenerValorAbonosProveedorFacturasPagadas(idProveedor, idSede);
		Integer totalDescuentos = comprobanteEgresoDao.obtenerValorDescuentosProveedor(idProveedor, idSede)- comprobanteEgresoDao.obtenerValorDescuentosProveedorFacturasPagadas(idProveedor, idSede);
		Integer totalAsignacion = asignacionComprobanteDao.obtenerTotalProveedor(idProveedor, idSede);
		Integer total = totalFacturas - totalNotasCredito + totalNotasDebito - totalDescuentos - totalAbonos- totalAsignacion;				
		return total;
	}

	
public Integer sumar(int idProveedor) {
		
	List<CuentasPorPagar> cp = new ArrayList<>();
	Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
	Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
	InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
			.buscarPorIdUsuario(usuarioAutenticado.getId());
	int idSede = usuarioInformacion.getIdOrganizacion().getId();
	int saldototal = 0;
	// Obtener facturas
	List<FacturaCompra> facturas = facturaDao.obtenerFacturasPorPagar(idProveedor, idSede);
	for (FacturaCompra f : facturas) {
		CuentasPorPagar cuenta = CuentasPorPagar.convertirFactura(f);
		cuenta.setDescuento(this.calcularDescuento(f.getProveedor().getId(), cuenta));
		Integer abonos = conceptoReciboEgresoDao.obtenerTotalAbonos(cuenta.getNumero(), idSede);
		Integer asignacion = asignacionComprobanteDao.obtenerTotal(f.getNroFactura(), idSede);
		Integer descuentos = conceptoReciboEgresoDao.obtenerTotalDescuentos(cuenta.getNumero(), idSede);
		    
		cuenta.setSaldo(cuenta.getTotal() - asignacion - abonos - descuentos);
		
		saldototal = saldototal + cuenta.getSaldo();
		cuenta.setAbonos(abonos + asignacion);
		cp.add(cuenta);
	}
              
       return saldototal;
	}

	public double sumarAlmacen() {
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		Integer idSede = usuarioInformacion.getIdOrganizacion().getId();
		double totalFacturas = (facturaDao.obtenerTotalFacturasPorPagarSede(idSede) != null)
				? facturaDao.obtenerTotalFacturasPorPagarSede(idSede)
				: 0;
		double totalNotasCredito = (ncDao.obtenerTotalNotasCreditosPorPagarAlmacen(idSede) != null)
				? ncDao.obtenerTotalNotasCreditosPorPagarAlmacen(idSede)
				: 0;
		double totalNotasDebito = (ndDao.obtenerTotalNotasDebitosPorPagarAlmacenEstadoCuentas(idSede) != null)
				? ndDao.obtenerTotalNotasDebitosPorPagarAlmacen(idSede)
				: 0;
		Integer totalAbonos = comprobanteEgresoDao.obtenerValorAbonosSedeCuentasPorPagar(idSede)
				- comprobanteEgresoDao.obtenerValorAbonosSedeCuentasPorPagarPagados(idSede)
				- comprobanteEgresoDao.obtenerValorAbonosSedeNotasDebito(idSede);
		Integer totalDescuentos = comprobanteEgresoDao.obtenerValorDescuentosSede(idSede)
				//- comprobanteEgresoDao.obtenerValorDescuentosSedePagado(idSede)
				+ comprobanteEgresoDao.obtenerValorDescuentosSedeNotaDebito(idSede);
		Integer totalAsignacion = comprobanteEgresoDao.obtenerValorAsignacionSede(idSede);		
		double total = totalFacturas - totalNotasCredito + totalNotasDebito - totalAbonos - totalAsignacion- totalDescuentos;
		return total;
	}

	private double calcularDescuento(Integer idProveedor, CuentasPorPagar cuenta) {
		List<ProveedorCondicionesComerciales> condiciones = condicionesComercialesDao
				.obtenerProductosProveedoresbyProveedor(idProveedor);
		long tiempoFecha = cuenta.getFecha().getTime();
		long tiempoActual = new java.util.Date().getTime();
		long diff = tiempoActual - tiempoFecha;
		int dias = (int) (diff / (1000 * 60 * 60 * 24));
		int i = 0;
		double descuento = 0;
		while (i < condiciones.size() && dias <= condiciones.get(i).getDias() && dias > 0) {
			descuento = condiciones.get(i).getDescuento();
			i++;
		}
		double valor = Math.round((cuenta.getTotal() * descuento) / 100);
		return valor;
	}
}

class OrdenPorFechaCuentasPorPagar implements Comparator<CuentasPorPagar> {
	public int compare(CuentasPorPagar a, CuentasPorPagar b) {
		if(b.getFecha().getTime() > a.getFecha().getTime()) {
			return 1;
		}
		else if(b.getFecha().getTime() == a.getFecha().getTime()) {
			return 0;
		}
		else {
			return -1;
		}
	}
}


