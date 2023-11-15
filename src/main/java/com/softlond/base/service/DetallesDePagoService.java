package com.softlond.base.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softlond.base.entity.ConceptosReciboCaja;
import com.softlond.base.entity.Factura;
import com.softlond.base.entity.FacturaCompra;
import com.softlond.base.entity.NotaCreditoCliente;
import com.softlond.base.entity.ReciboCajaVenta;
import com.softlond.base.repository.ReciboCajaVentaDao;
import com.softlond.base.repository.UsuarioInformacionDao;
import com.softlond.base.response.DetallesDePago;
import com.softlond.base.response.InfromeFacturasVencidas;
import com.softlond.base.response.MovimientoCliente;
import com.softlond.base.response.Paginacion;

@Service
public class DetallesDePagoService {

	final int ITEMS_POR_PAGINA = 10;

	@Autowired
	ReciboCajaVentaService reciboCajaVentaService;

	/*
	 * @Autowired private NotaDebitoClienteService servicioND;
	 */
	@Autowired
	private NotaCreditoClienteService servicioNC;

	@Autowired
	UsuarioInformacionDao usuarioInformacionDao;

	@Autowired
	ConceptosReciboCajaService conceptosReciboCajaService;

	@PersistenceContext
	private EntityManager entityManager;

	public List<DetallesDePago> listardetallesDePagoFiltros(int idSede, String fechaInicial, String fechaFinal,
			String numeroDocumento, int cliente) {
		List<DetallesDePago> vecom = new ArrayList<>();
		// Obtener facturas
		List<ReciboCajaVenta> recibos = reciboCajaVentaService.listardetallesDePagoFiltros(idSede, fechaInicial,
				fechaFinal, numeroDocumento, cliente);
		for (ReciboCajaVenta recibo : recibos) {
			List<ConceptosReciboCaja> conceptos;
			StringBuilder queryNuevo = new StringBuilder();
			reciboCajaVentaService.generarQueryRecibos(queryNuevo, idSede, fechaInicial, fechaFinal, numeroDocumento, cliente, recibo.getId());
			TypedQuery<ConceptosReciboCaja> recibosDocumentosInfoQuery = (TypedQuery<ConceptosReciboCaja>) entityManager.createNativeQuery(queryNuevo.toString(), ConceptosReciboCaja.class);
			conceptos = recibosDocumentosInfoQuery.getResultList();

			vecom.add(DetallesDePago.convertirRecibo(recibo, conceptos));
		}

		/*
		 * // Obtener Notas débito List<NotaDebitoCliente> notasDebito =
		 * servicioND.listarinformeNotasDebitoVencidasCompraFiltros(idSede, idCliente,
		 * fechaInicial, fechaFinal); for (NotaDebitoCliente nd : notasDebito) {
		 * vecom.add(MovimientoCliente.convertirNotaDeb(nd)); }
		 */
		/*
		 * // Obtener Notas crédito List<NotaCreditoCliente> notasCredito =
		 * servicioNC.listarNotasCreditoPagoFiltros(idSede, fechaInicial, fechaFinal,
		 * numeroDocumento, idCliente); for (NotaCreditoCliente nc : notasCredito) {
		 * vecom.add(DetallesDePago.convertirNotaCreditoCliente(nc)); }
		 */

		vecom.sort(new OrdenPorFechalistardetallesPago());
		return vecom;
	}
	
	public Integer obtenerTotalabonos(int idSede, int cliente, String fechaInicial,
			String fechaFinal, String numeroDocumento) {
		return reciboCajaVentaService.obtenerTotalAbonos(idSede, fechaInicial, fechaFinal, numeroDocumento, cliente);
	}
	
	public Integer obtenerTotalDescuentos(int idSede, int cliente, String fechaInicial,
			String fechaFinal, String numeroDocumento) {
		return reciboCajaVentaService.obtenerTotalDescuentos(idSede, fechaInicial, fechaFinal, numeroDocumento, cliente);
	}

	public Paginacion listarmovimientosClienteConsultaPaginado(int idSede, int cliente, String fechaInicial,
			String fechaFinal, String numeroDocumento, int pagina) {
		List<DetallesDePago> vecom = listardetallesDePagoFiltros(idSede, fechaInicial, fechaFinal, numeroDocumento,
				cliente);
		return Paginacion.paginar(vecom, ITEMS_POR_PAGINA, pagina);
	}

}

class OrdenPorFechalistardetallesPago implements Comparator<DetallesDePago> {
	public int compare(DetallesDePago a, DetallesDePago b) {
		return (int) (a.getFechaPago().getTime() - b.getFechaPago().getTime());
	}
}
