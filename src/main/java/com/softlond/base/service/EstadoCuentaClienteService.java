package com.softlond.base.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.softlond.base.entity.Factura;
import com.softlond.base.entity.InformacionUsuario;
import com.softlond.base.entity.NotaCredito;
import com.softlond.base.entity.NotaCreditoCliente;
import com.softlond.base.entity.NotaDebitoCliente;
import com.softlond.base.entity.Usuario;
import com.softlond.base.repository.AsignacionReciboDao;
import com.softlond.base.repository.ConceptosReciboCajaDao;
import com.softlond.base.repository.FacturaDao;
import com.softlond.base.repository.NotaCreditoClienteDao;
import com.softlond.base.repository.NotaCreditoDao;
import com.softlond.base.repository.NotaDebitoClienteDao;
import com.softlond.base.repository.NotaDebitoDao;
import com.softlond.base.repository.UsuarioInformacionDao;
import com.softlond.base.response.EstadoCuentaCliente;
import com.softlond.base.response.EstadosCuenta;
import com.softlond.base.response.InfromeFacturasVencidasCliente;
import com.softlond.base.response.Paginacion;
import java.util.Date;

@Service
public class EstadoCuentaClienteService {

	private static final Logger logger = Logger.getLogger(EstadoCuentaClienteService.class);

	final int ITEMS_POR_PAGINA = 10;

	@Autowired
	private FacturaDao facturaDao;
	@Autowired
	private NotaCreditoClienteDao ncDao;
	@Autowired
	private NotaDebitoClienteDao ndDao;

	@Autowired
	private ConceptosReciboCajaDao conceptosReciboCajaDao;

	@Autowired
	private AsignacionReciboDao asignacionReciboDao;
	
	@Autowired
	UsuarioInformacionDao usuarioInformacionDao;

	public List<EstadoCuentaCliente> obtenerEstadosCuenta(int idCliente) {
		List<EstadoCuentaCliente> ec = new ArrayList<>();
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		int idSede = usuarioInformacion.getIdOrganizacion().getId();
		// Obtener facturas
		List<Factura> facturas = facturaDao.obtenerFacturasPorPagar(idCliente, idSede);
		for (Factura f : facturas) {
			EstadoCuentaCliente vencida = EstadoCuentaCliente.convertirFactura(f);
			Integer saldo = conceptosReciboCajaDao.obtenerSaldo(f.getPrefijo().getPrefijo() + vencida.getNumero());
			Integer asignacion = asignacionReciboDao.obtenerTotal(f.getPrefijo().getPrefijo() + vencida.getNumero());
			Integer descuentos = conceptosReciboCajaDao
					.obtenerTotalDescuentoConcepto(f.getPrefijo().getPrefijo() + vencida.getNumero());
			vencida.setSaldo(vencida.getTotal() - asignacion - saldo - descuentos);
			if(vencida.getSaldo() == 0) {
				//logger.info(vencida.getSaldo());
			}
			else {
			ec.add(vencida);
			}
		}

		// Obtener Notas débito
		List<NotaDebitoCliente> notasDebito = ndDao.obtenerNotasDebitosPorPagar2(idCliente, idSede);
		for (NotaDebitoCliente nd : notasDebito) {
			EstadoCuentaCliente vencida = EstadoCuentaCliente.convertirNotaDeb(nd);
			Integer saldo = conceptosReciboCajaDao.obtenerSaldo(vencida.getNumero());
			Integer asignacion = asignacionReciboDao.obtenerTotal(vencida.getNumero());
			Integer descuentos = conceptosReciboCajaDao.obtenerTotalDescuentoConcepto(vencida.getNumero());
			vencida.setSaldo(vencida.getTotal() - asignacion - saldo - descuentos);
			ec.add(vencida);
		}

		// Obtener Notas crédito
		List<NotaCreditoCliente> notasCredito = ncDao.obtenerNotasCreditosPorPagarSede(idCliente, idSede);
		for (NotaCreditoCliente nc : notasCredito) {
			EstadoCuentaCliente vencida = EstadoCuentaCliente.convertirNotaCred(nc);
			Integer asignacion = asignacionReciboDao.obtenerTotal(vencida.getNumero());
			vencida.setSaldo(vencida.getTotal() - asignacion);
			ec.add(vencida);
		}

		ec.sort(new OrdenPorFechaEstadosCuentaC());
		return ec;
	}

	public Paginacion obtenerEstadosCuentaPaginado(int idCliente, int pagina) {
		List<EstadoCuentaCliente> ec = obtenerEstadosCuenta(idCliente);
		return Paginacion.paginar(ec, ITEMS_POR_PAGINA, pagina);
	}

	public double sumar(int idCliente) {
		List<EstadoCuentaCliente> ec = new ArrayList<>();
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		Integer idSede = usuarioInformacion.getIdOrganizacion().getId();
		double totalFacturas = (facturaDao.obtenerTotalFacturasPorPagar(idCliente, idSede) != null)
				? facturaDao.obtenerTotalFacturasPorPagar(idCliente,idSede)
				: 0;
		double totalNotasCredito = (ncDao.obtenerTotalNotasCreditosClientePorPagar(idCliente, idSede) != null)
				? ncDao.obtenerTotalNotasCreditosClientePorPagar(idCliente, idSede)
				: 0;
		double totalNotasDebito = (ndDao.obtenerTotalNotasDebitosPorPagar(idCliente, idSede) != null)
				? ndDao.obtenerTotalNotasDebitosPorPagar(idCliente, idSede)
				: 0;
		double total = totalFacturas - totalNotasCredito + totalNotasDebito;
		Integer abonos = conceptosReciboCajaDao.totalConceptosClienteFacturas(idCliente, idSede) + conceptosReciboCajaDao.totalConceptosClienteNotaDebito(idCliente, idSede);
		Integer descuentos = conceptosReciboCajaDao.obtenerTotalDescuentoClienteFacturas(idCliente, idSede) + conceptosReciboCajaDao.obtenerTotalDescuentoClienteNotaDebito(idCliente, idSede);
		Integer asignacion = asignacionReciboDao.asignacionFacturaCliente(idCliente, idSede);
		total = total - abonos - descuentos - asignacion;
		return total;
	}
}

class OrdenPorFechaEstadosCuentaC implements Comparator<EstadoCuentaCliente> {
	public int compare(EstadoCuentaCliente a, EstadoCuentaCliente b) {
		return (int) (b.getFecha().getTime() - a.getFecha().getTime());
	}

}
