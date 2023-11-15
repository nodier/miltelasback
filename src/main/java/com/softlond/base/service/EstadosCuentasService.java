package com.softlond.base.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

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
import com.softlond.base.response.EstadosCuenta;
import com.softlond.base.response.Paginacion;

@Service
public class EstadosCuentasService {

final int ITEMS_POR_PAGINA = 10;
	
	@Autowired
    private FacturaCompraDao facturaDao;
    @Autowired
    private NotaDebitoDao ndDao;
    @Autowired
    private NotaCreditoDao ncDao;
    
    @Autowired
    private ProveedorCondicionesComercialesDao condicionesComercialesDao;
    
    @Autowired
    private ConceptoReciboEgresoDao conceptoReciboEgresoDao;
    
    @Autowired
	private AsignacionComprobanteDao asignacionComprobanteDao;
    
    @Autowired
    private ComprobanteEgresoDao comprobanteEgresoDao;
    
    @Autowired
	UsuarioInformacionDao usuarioInformacionDao;
    
	public List<EstadosCuenta> obtenerEstadosCuenta(int idProveedor) {
        List<EstadosCuenta> ec = new ArrayList<>();
        Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		int idSede = usuarioInformacion.getIdOrganizacion().getId();
        // Obtener facturas
        List<FacturaCompra> facturas = facturaDao.obtenerFacturasPorPagar(idProveedor, idSede);
        for (FacturaCompra f: facturas) {
        	EstadosCuenta cuenta = EstadosCuenta.convertirFactura(f);
        	cuenta.setDescuento(this.calcularDescuento(idProveedor, cuenta));
        	Integer abonos = conceptoReciboEgresoDao.obtenerTotalAbonos(cuenta.getNumero(), idSede);
        	Integer asignacion = asignacionComprobanteDao.obtenerTotal(f.getNroFactura(), idSede);
        	Integer descuentos = conceptoReciboEgresoDao.obtenerTotalDescuentos(cuenta.getNumero(), idSede);
        	if (asignacion != null)
        		cuenta.setSaldo(cuenta.getTotal() - asignacion);
        	if(abonos!=null) {
        		cuenta.setSaldo(cuenta.getSaldo()-abonos - descuentos);
        		cuenta.setAbonos(abonos + asignacion);
        	}
        	ec.add(cuenta);
        }

        // Obtener Notas débito
        List<NotaDebito> notasDebito = ndDao.obtenerNotasDebitosPorPagar(idProveedor, idSede);
        for (NotaDebito nd: notasDebito) {
        	EstadosCuenta cuenta = EstadosCuenta.convertirNotaDeb(nd);
        	cuenta.setDescuento(this.calcularDescuento(idProveedor, cuenta));
        	Integer abonos = conceptoReciboEgresoDao.obtenerTotalAbonos(cuenta.getNumero(), idSede);
        	Integer asignacion = asignacionComprobanteDao.obtenerTotal(cuenta.getNumero(), idSede);
        	Integer descuentos = conceptoReciboEgresoDao.obtenerTotalDescuentos(cuenta.getNumero(), idSede);
        	if (asignacion != null)
        		cuenta.setSaldo(cuenta.getTotal() - asignacion);
        	if(abonos!=null) {
        		cuenta.setSaldo(cuenta.getSaldo()-abonos-descuentos);
        		cuenta.setAbonos(abonos + asignacion);
        	}
        	ec.add(cuenta);
        }

        // Obtener Notas crédito
        List<NotaCredito> notasCredito = ncDao.obtenerNotasCreditosPorPagar(idProveedor, idSede);
        for (NotaCredito nc: notasCredito) {
        	EstadosCuenta cuenta = EstadosCuenta.convertirNotaCred(nc);
        	cuenta.setDescuento(this.calcularDescuento(idProveedor, cuenta));
        	Integer abonos = conceptoReciboEgresoDao.obtenerTotalAbonos(cuenta.getNumero(), idSede);
        	if(abonos!=null) {
        		cuenta.setSaldo(cuenta.getTotal()-abonos);
        		cuenta.setAbonos(abonos);
        	}
        	ec.add(cuenta);
        }

        ec.sort(new OrdenPorFechaEstadosCuenta());
        return ec;
    }
	
	public Paginacion obtenerEstadosCuentaPaginado(int idProveedor, int pagina) {
        List<EstadosCuenta> ec = obtenerEstadosCuenta(
                idProveedor
        );
        return Paginacion.paginar(ec, ITEMS_POR_PAGINA, pagina);
    }
	
	public double sumar(int idProveedor) {
		List<EstadosCuenta> ec = new ArrayList<>();
        Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		int idSede = usuarioInformacion.getIdOrganizacion().getId();
		double totalFacturas = (facturaDao.obtenerTotalFacturasPorPagar(idProveedor, idSede)!=null)?facturaDao.obtenerTotalFacturasPorPagar(idProveedor, idSede):0;
		double totalNotasCredito = (ncDao.obtenerTotalNotasCreditosPorPagar(idProveedor, idSede)!=null)?ncDao.obtenerTotalNotasCreditosPorPagar(idProveedor, idSede):0;
		double totalNotasDebito = (ndDao.obtenerTotalNotasDebitosPorPagar(idProveedor, idSede)!=null)?ndDao.obtenerTotalNotasDebitosPorPagar(idProveedor, idSede):0;
		Integer totalAbonos = comprobanteEgresoDao.obtenerValorAbonosProveedor(idProveedor, idSede) - comprobanteEgresoDao.obtenerValorAbonosProveedorFacturasPagadas(idProveedor, idSede);
		Integer totalDescuentos = comprobanteEgresoDao.obtenerValorDescuentosProveedor(idProveedor, idSede) - comprobanteEgresoDao.obtenerValorDescuentosProveedorFacturasPagadas(idProveedor, idSede);
		Integer totalAsignacion = asignacionComprobanteDao.obtenerTotalProveedor(idProveedor, idSede);
		double total = totalFacturas - totalNotasCredito + totalNotasDebito - totalAbonos - totalDescuentos - totalAsignacion;
		return total;
	}
	
	private double calcularDescuento(Integer idProveedor, EstadosCuenta cuenta) {
		List<ProveedorCondicionesComerciales>condiciones = condicionesComercialesDao.obtenerProductosProveedoresbyProveedor(idProveedor);
		long tiempoFecha = cuenta.getFecha().getTime();
        long tiempoActual = new Date().getTime();
        long diff = tiempoActual - tiempoFecha;
        int dias = (int) diff / (1000 * 60 * 60 * 24);
        int i = 0;
        double descuento = 0;
        while(i<condiciones.size() && dias<=condiciones.get(i).getDias() && dias > 0) {
        	descuento = condiciones.get(i).getDescuento();
        	i++;
        }
        double valor = Math.round((cuenta.getTotal() * descuento)/100);
        return valor;
	}
}

class OrdenPorFechaEstadosCuenta implements Comparator<EstadosCuenta> {
	public int compare(EstadosCuenta a, EstadosCuenta b) {
		return (int)(b.getFecha().getTime() - a.getFecha().getTime());
	}
}
