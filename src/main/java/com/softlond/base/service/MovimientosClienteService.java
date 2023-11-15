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
import com.softlond.base.entity.NotaCreditoCliente;
import com.softlond.base.entity.NotaDebitoCliente;
import com.softlond.base.repository.AsignacionReciboDao;
import com.softlond.base.repository.ConceptosReciboCajaDao;
import com.softlond.base.repository.UsuarioInformacionDao;
import com.softlond.base.response.InfromeFacturasVencidasCliente;
import com.softlond.base.response.MovimientoCliente;
import com.softlond.base.response.Paginacion;

@Service
public class MovimientosClienteService {

	private static final Logger logger = Logger.getLogger(MovimientosClienteService.class);

	final int ITEMS_POR_PAGINA = 10;

	@Autowired
	private FacturaService servicioFactura;
	@Autowired
	private NotaDebitoClienteService servicioND;
	@Autowired
	private NotaCreditoClienteService servicioNC;

	@Autowired
	UsuarioInformacionDao usuarioInformacionDao;

	@Autowired
	private ConceptosReciboCajaDao conceptosReciboCajaDao;

	@Autowired
	private AsignacionReciboDao asignacionReciboDao;

	@PersistenceContext
	private EntityManager entityManager;

	private Integer totalDescuentos;

	private Integer totalAbono;

	public List<MovimientoCliente> listarmovimientosClienteFiltros(int idSede, String fechaInicial, String fechaFinal,
			String numeroDocumento, int idCliente) {
		List<MovimientoCliente> vecom = new ArrayList<>();
		this.totalDescuentos = 0;
		this.totalAbono = 0;
		// Obtener facturas
		List<Factura> facturas = servicioFactura.listarmovimientosClienteFiltros(idSede, fechaInicial, fechaFinal,
				numeroDocumento, idCliente);
		for (Factura f : facturas) {
			MovimientoCliente vencida = MovimientoCliente.convertirFactura(f);
			Integer saldo = 0;
			Integer asignacion = 0;
			Integer descuentos = 0;
			if (f.getPrefijo() != null && f.getNroFactura() != null) {
				saldo = conceptosReciboCajaDao.obtenerSaldo(f.getPrefijo().getPrefijo() + f.getNroFactura());
				asignacion = asignacionReciboDao.obtenerTotal(f.getPrefijo().getPrefijo() + f.getNroFactura());
				descuentos = conceptosReciboCajaDao
						.obtenerTotalDescuentoConcepto(f.getPrefijo().getPrefijo() + f.getNroFactura());
			}
			vencida.setSaldo(vencida.getTotalRecibo() - asignacion - saldo - descuentos);
			vencida.setValorAbono(asignacion + saldo);
			this.totalAbono += (asignacion + saldo);
			this.totalDescuentos += descuentos;
			vencida.setDescuento(descuentos);
			vecom.add(vencida);
		}

		// Obtener Notas débito
		List<NotaDebitoCliente> notasDebito = servicioND.listarNotasdebitoFiltros(idSede, fechaInicial, fechaFinal,
				numeroDocumento, idCliente);
		for (NotaDebitoCliente nd : notasDebito) {
			MovimientoCliente vencida = MovimientoCliente.convertirNotaDeb(nd);
			Integer saldo = conceptosReciboCajaDao.obtenerSaldo(vencida.getNumeroDocumento());
			Integer asignacion = asignacionReciboDao.obtenerTotal(vencida.getNumeroDocumento());
			Integer descuentos = conceptosReciboCajaDao.obtenerTotalDescuentoConcepto(vencida.getNumeroDocumento());
			vencida.setSaldo(vencida.getTotalRecibo() - asignacion - saldo - descuentos);
			vencida.setValorAbono(asignacion + saldo);
			vencida.setDescuento(descuentos);
			this.totalAbono += (asignacion + saldo);
			this.totalDescuentos += descuentos;
			vecom.add(vencida);
		}

		// Obtener Notas crédito
		List<NotaCreditoCliente> notasCredito = servicioNC.listarNotasCreditoFiltros1(idSede, fechaInicial, fechaFinal,
				numeroDocumento, idCliente);
		for (NotaCreditoCliente nc : notasCredito) {
			MovimientoCliente vencida = MovimientoCliente.convertirNotaCred(nc);
			Integer asignacion = asignacionReciboDao.obtenerTotal(vencida.getNumeroDocumento());
			if (asignacion != null) {
				vencida.setSaldo(vencida.getTotalRecibo() - asignacion);
				vencida.setValorAbono(asignacion);
			}
			vecom.add(vencida);
		}

		vecom.sort(new OrdenPorFechalistarmovimientosCliente());
		return vecom;
	}

	public Paginacion listarmovimientosClienteConsultaPaginado(int idSede, int idCliente, String fechaInicial,
			String fechaFinal, String numeroDocumento, int pagina) {
		List<MovimientoCliente> vecom = listarmovimientosClienteFiltros(idSede, fechaInicial, fechaFinal,
				numeroDocumento, idCliente);
		return Paginacion.paginar(vecom, ITEMS_POR_PAGINA, pagina);
	}

	public Integer calcularAbonos(int idSede, int idCliente, String fechaInicial, String fechaFinal,
			String numeroDocumento) {
		Integer abono = 0;
		abono += servicioFactura.abonoFacturas(idSede, fechaInicial, fechaFinal, numeroDocumento, idCliente)
				+ servicioND.abonoNotasDebito(idSede, fechaInicial, fechaFinal, numeroDocumento, idCliente);
		return abono;
	}

	public Integer getTotalDescuentos() {
		return totalDescuentos;
	}

	public void setTotalDescuentos(Integer totalDescuentos) {
		this.totalDescuentos = totalDescuentos;
	}

	public Integer getTotalAbono() {
		return totalAbono;
	}

}

class OrdenPorFechalistarmovimientosCliente implements Comparator<MovimientoCliente> {
	public int compare(MovimientoCliente a, MovimientoCliente b) {
		return (int) ((a.getFechaPago()!=null?a.getFechaPago().getTime():0) - (b.getFechaPago()!=null?b.getFechaPago().getTime():0));
	}
}
