package com.softlond.base.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softlond.base.entity.Factura;
import com.softlond.base.repository.AsignacionReciboDao;
import com.softlond.base.repository.ConceptosReciboCajaDao;
import com.softlond.base.repository.UsuarioInformacionDao;
import com.softlond.base.response.InfromeFacturasVencidasCliente;
import com.softlond.base.response.Paginacion;

@Service
public class InformeFacturasVencidasClienteService {

	private static final Logger logger = Logger.getLogger(InformeFacturasVencidasClienteService.class);

	final int ITEMS_POR_PAGINA = 10;

	/*
	 * @Autowired private InformeFacturasVencidasDao informeFacturasVencidasDao;
	 */

	@Autowired
	UsuarioInformacionDao usuarioInformacionDao;

	@Autowired
	private FacturaService servicioFactura;

	@Autowired
	private NotaDebitoClienteService servicioND;

	@Autowired
	private ConceptosReciboCajaDao conceptosReciboCajaDao;

	@Autowired
	private NotaCreditoClienteService servicioNC;

	@Autowired
	private AsignacionReciboDao asignacionReciboDao;

	@PersistenceContext
	private EntityManager entityManager;

	private Integer totalSaldo;

	public List<InfromeFacturasVencidasCliente> listarinformeFacturasVencidasCliente(int idSede) {
		List<InfromeFacturasVencidasCliente> vecom = new ArrayList<>();
		this.totalSaldo = 0;
		// Obtener facturas
		List<Factura> facturas = servicioFactura.listarinformeFacturasVencidasCliente(idSede);
		for (Factura f : facturas) {
			InfromeFacturasVencidasCliente vencida = InfromeFacturasVencidasCliente.convertirFactura(f);
			// if (f.getCodEstadoCon().getId() == 15) {//! esta opcion de documento no
			// existe
			if (vencida != null) {
				// logger.info("existe vencida");

				Integer saldo = conceptosReciboCajaDao.obtenerSaldo(f.getPrefijo().getPrefijo() + f.getNroFactura());
				Integer asignacion = asignacionReciboDao.obtenerTotal(f.getPrefijo().getPrefijo() + f.getNroFactura());
				Integer descuentos = conceptosReciboCajaDao
						.obtenerTotalDescuentoConcepto(f.getPrefijo().getPrefijo() + f.getNroFactura());
				// logger.info(saldo + "-" + asignacion + "-" + descuentos);
				// vencida.setSaldo(vencida.getTotal() - asignacion - saldo - descuentos);
				if (saldo != null && asignacion != null && descuentos != null) {
					vencida.setSaldo(vencida.getTotal() - asignacion - saldo - descuentos);
				}
				// } else {
				// vencida.setSaldo(0); //! verificar si esta informacion es necesaria
				// }
				if (vencida.getSaldo() != null) {
					// logger.info("el saldo es diferente de nulo");
					this.totalSaldo += vencida.getSaldo();
					// logger.info(this.totalSaldo);
				}
			}

			vecom.add(vencida);
		}
		/*
		 * // Obtener Notas débito List<NotaDebitoCliente> notasDebito =
		 * servicioND.listarnotadebitopendiente(idSede); for (NotaDebitoCliente nd :
		 * notasDebito) { InfromeFacturasVencidasCliente vencida =
		 * InfromeFacturasVencidasCliente.convertirNotaDeb(nd); Integer saldo =
		 * conceptosReciboCajaDao.obtenerSaldo(vencida.getNumero()); Integer asignacion
		 * = asignacionReciboDao.obtenerTotal(vencida.getNumero()); if (asignacion !=
		 * null) vencida.setSaldo(vencida.getTotal() - asignacion); if(saldo!=null) {
		 * vencida.setSaldo(vencida.getTotal()-saldo); //vencida.setAbonos(saldo); }
		 * vecom.add(vencida); }
		 */
		/*
		 * // Obtener Notas crédito List<NotaCreditoCliente> notasCredito =
		 * servicioNC.listarnotacreditopendiente(idSede); for (NotaCreditoCliente nc :
		 * notasCredito) { InfromeFacturasVencidasCliente vencida =
		 * InfromeFacturasVencidasCliente.convertirNotaCred(nc); Integer asignacion =
		 * asignacionReciboDao.obtenerTotal(vencida.getNumero()); if (asignacion !=
		 * null) { vencida.setSaldo(vencida.getTotal() - asignacion); }
		 * vecom.add(vencida); }
		 */

		vecom.sort(new OrdenPorFechaFacturasVencidasCliente());
		return vecom;
	}

	public Paginacion listarinformeFacturasVencidasClientePaginado(int idSede, int pagina) {
		List<InfromeFacturasVencidasCliente> vecom = listarinformeFacturasVencidasCliente(idSede);
		return Paginacion.paginar(vecom, ITEMS_POR_PAGINA, pagina);
	}

	public List<InfromeFacturasVencidasCliente> listarinformeFacturasVencidasClienteFiltros(int idSede, int idCliente,
			String fechaInicial, String fechaFinal) {
		List<InfromeFacturasVencidasCliente> vecom = new ArrayList<>();
		this.totalSaldo = 0;
		// Obtener facturas
		List<Factura> facturas = servicioFactura.listarinformeFacturasVencidasClienteFiltros(idSede, idCliente,
				fechaInicial, fechaFinal);
		for (Factura f : facturas) {
			InfromeFacturasVencidasCliente vencida = InfromeFacturasVencidasCliente.convertirFactura(f);
			// if (f.getCodEstadoCon().getId() == 15) {
			Integer saldo = conceptosReciboCajaDao.obtenerSaldo(f.getPrefijo().getPrefijo() + f.getNroFactura());
			Integer asignacion = asignacionReciboDao.obtenerTotal(f.getPrefijo().getPrefijo() + f.getNroFactura());
			Integer descuentos = conceptosReciboCajaDao
					.obtenerTotalDescuentoConcepto(f.getPrefijo().getPrefijo() + f.getNroFactura());
			vencida.setSaldo(vencida.getTotal() - asignacion - saldo - descuentos);
			// } else {
			// vencida.setSaldo(0);
			// }
			this.totalSaldo += vencida.getSaldo();
			vecom.add(vencida);
		}

		/*
		 * // Obtener Notas débito List<NotaDebitoCliente> notasDebito =
		 * servicioND.listarinformeNotasDebitoVencidasClienteFiltros(idSede, idCliente,
		 * fechaInicial, fechaFinal); for (NotaDebitoCliente nd : notasDebito) {
		 * InfromeFacturasVencidasCliente vencida =
		 * InfromeFacturasVencidasCliente.convertirNotaDeb(nd); Integer saldo =
		 * conceptosReciboCajaDao.obtenerSaldo(vencida.getNumero()); Integer asignacion
		 * = asignacionReciboDao.obtenerTotal(vencida.getNumero()); if (asignacion !=
		 * null) vencida.setSaldo(vencida.getTotal() - asignacion); if(saldo!=null) {
		 * vencida.setSaldo(vencida.getTotal()-saldo); //vencida.setAbonos(saldo); }
		 * vecom.add(vencida); }
		 */

		/*
		 * // Obtener Notas crédito List<NotaCreditoCliente> notasCredito =
		 * servicioNC.listarinformeNotasCreditoVencidasCompraFiltros(idSede, idCliente,
		 * fechaInicial, fechaFinal); for (NotaCreditoCliente nc : notasCredito) {
		 * InfromeFacturasVencidasCliente vencida =
		 * InfromeFacturasVencidasCliente.convertirNotaCred(nc); Integer asignacion =
		 * asignacionReciboDao.obtenerTotal(vencida.getNumero()); if (asignacion !=
		 * null) { vencida.setSaldo(vencida.getTotal() - asignacion); }
		 * vecom.add(vencida); }
		 */

		vecom.sort(new OrdenPorFechaFacturasVencidasCliente());
		return vecom;
	}

	public Paginacion listarinformeFacturasVencidasClienteConsultaPaginado(int idSede, int idCliente,
			String fechaInicial, String fechaFinal, int pagina) {
		List<InfromeFacturasVencidasCliente> vecom = listarinformeFacturasVencidasClienteFiltros(idSede, idCliente,
				fechaInicial, fechaFinal);
		return Paginacion.paginar(vecom, ITEMS_POR_PAGINA, pagina);
	}

	public Integer getTotalSaldo() {
		logger.info(totalSaldo);
		return totalSaldo;
	}

}

class OrdenPorFechaFacturasVencidasCliente implements Comparator<InfromeFacturasVencidasCliente> {
	public int compare(InfromeFacturasVencidasCliente a, InfromeFacturasVencidasCliente b) {
		return (int) (a.getDiasVencidos() - b.getDiasVencidos());
	}
}
