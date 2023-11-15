package com.softlond.base.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softlond.base.entity.Factura;
import com.softlond.base.entity.NotaCreditoCliente;
import com.softlond.base.entity.NotaDebitoCliente;
import com.softlond.base.repository.AsignacionReciboDao;
import com.softlond.base.repository.ConceptosReciboCajaDao;
import com.softlond.base.response.ConceptosReciboCliente;
import com.softlond.base.response.MovimientoProveedor;
import com.softlond.base.response.Paginacion;

@Service
public class ConceptosClienteVisualService {

	@Autowired
	private FacturaService facturaService;

	@Autowired
	private NotaCreditoClienteService notasCreditoService;

	@Autowired
	private NotaDebitoClienteService notasDebitoService;

	@Autowired
	private ConceptosReciboCajaDao conceptosDao;

	@Autowired
	private AsignacionReciboDao asignacionDao;

	private final Logger logger = Logger.getLogger(getClass());

	final int ITEMS_POR_PAGINA = 10;

	Integer total = 0;

	public Integer getTotal() {
		return total;
	}

	private List<ConceptosReciboCliente> obtenerConceptosPendientes(Integer idCliente) {
		total = 0;
		List<ConceptosReciboCliente> conceptos = new ArrayList<ConceptosReciboCliente>();
		List<Factura> facturas = facturaService.obtenerFacturasCreditoPendientesCliente(idCliente);
		logger.info(facturas.size());
		List<NotaCreditoCliente> notasCredito = notasCreditoService.obtenerPendientesPorCliente(idCliente);
		List<NotaDebitoCliente> notasDebito = notasDebitoService.notasDebitoPendientes(idCliente);
		for (Factura factura : facturas) {
			ConceptosReciboCliente concepto = ConceptosReciboCliente.convertirFactura(factura);
			int asignacion = asignacionDao.asignacionFactura(concepto.getNumero());
			int retencion = 0;
			if (factura.getRetenciones() != null) {
				retencion = factura.getRetenciones();
			}
			// Integer totalAbonos = conceptosDao.obtenerSaldo(concepto.getNumero());
			Integer totalAbonos = conceptosDao.obtenerSaldo2(concepto.getConcepto());
			Integer descuentos = conceptosDao.obtenerTotalDescuentoConcepto(concepto.getNumero());
			if (totalAbonos == null) {
				totalAbonos = 0;
			}
			concepto.setConcepto(factura.getId().toString());
			concepto.setSaldo(concepto.getTotal() - totalAbonos - asignacion - descuentos - retencion);
			total += concepto.getSaldo();
			conceptos.add(concepto);

		}
		for (NotaCreditoCliente notaCredito : notasCredito) {
			ConceptosReciboCliente concepto = ConceptosReciboCliente.convertirNotaCred(notaCredito);
			int asignacion = asignacionDao.asignacionFactura(concepto.getNumero());
			concepto.setSaldo(concepto.getTotal() - asignacion);
			conceptos.add(concepto);
		}
		for (NotaDebitoCliente notaDebito : notasDebito) {
			ConceptosReciboCliente concepto = ConceptosReciboCliente.convertirNotaDeb(notaDebito);
			int asignacion = asignacionDao.asignacionFactura(concepto.getNumero());
			Integer totalAbonos = conceptosDao.obtenerSaldo(concepto.getNumero());
			Integer descuentos = conceptosDao.obtenerTotalDescuentoConcepto(concepto.getNumero());
			if (totalAbonos == null) {
				totalAbonos = 0;
			}
			concepto.setSaldo(concepto.getTotal() - totalAbonos - asignacion - descuentos);
			total += concepto.getSaldo();
			conceptos.add(concepto);
		}
		return conceptos;
	}

	public Paginacion obtenerConceptosPaginado(int cliente, int pagina) {
		List<ConceptosReciboCliente> conceptos = obtenerConceptosPendientes(cliente);
		return Paginacion.paginar(conceptos, ITEMS_POR_PAGINA, pagina);
	}

	private List<ConceptosReciboCliente> obtenerConceptosPendientesActualizar(Integer idCliente, Integer recibo) {
		total = 0;
		List<ConceptosReciboCliente> conceptos = new ArrayList<ConceptosReciboCliente>();
		List<Factura> facturas = facturaService.facturasPorClienteActualizar(idCliente, recibo);
		List<NotaCreditoCliente> notasCredito = notasCreditoService.obtenerPendientesPorCliente(idCliente);
		List<NotaDebitoCliente> notasDebito = notasDebitoService.notasDebitoPendientes(idCliente);
		for (Factura factura : facturas) {
			ConceptosReciboCliente concepto = ConceptosReciboCliente.convertirFactura(factura);
			int asignacion = asignacionDao.asignacionFactura(concepto.getNumero());
			Integer totalAbonos = conceptosDao.obtenerSaldo(concepto.getNumero());
			Integer descuentos = conceptosDao.obtenerTotalDescuentoConcepto(concepto.getNumero());
			if (totalAbonos == null) {
				totalAbonos = 0;
			}
			concepto.setSaldo(concepto.getTotal() - totalAbonos - asignacion - descuentos);
			total += concepto.getSaldo();
			conceptos.add(concepto);
		}
		for (NotaCreditoCliente notaCredito : notasCredito) {
			ConceptosReciboCliente concepto = ConceptosReciboCliente.convertirNotaCred(notaCredito);
			int asignacion = asignacionDao.asignacionFactura(concepto.getNumero());
			concepto.setSaldo(concepto.getTotal() - asignacion);
			conceptos.add(concepto);
		}
		for (NotaDebitoCliente notaDebito : notasDebito) {
			ConceptosReciboCliente concepto = ConceptosReciboCliente.convertirNotaDeb(notaDebito);
			int asignacion = asignacionDao.asignacionFactura(concepto.getNumero());
			Integer totalAbonos = conceptosDao.obtenerSaldo(concepto.getNumero());
			Integer descuentos = conceptosDao.obtenerTotalDescuentoConcepto(concepto.getNumero());
			if (totalAbonos == null) {
				totalAbonos = 0;
			}
			concepto.setSaldo(concepto.getTotal() - totalAbonos - asignacion - descuentos);
			total += concepto.getSaldo();
			conceptos.add(concepto);
		}
		return conceptos;
	}

	public Paginacion obtenerConceptosPaginadoActualizar(int cliente, int recibo, int pagina) {
		List<ConceptosReciboCliente> conceptos = obtenerConceptosPendientesActualizar(cliente, recibo);
		return Paginacion.paginar(conceptos, ITEMS_POR_PAGINA, pagina);
	}

}
