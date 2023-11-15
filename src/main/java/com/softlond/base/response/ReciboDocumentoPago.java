package com.softlond.base.response;


import java.util.Date;

import com.softlond.base.entity.AsignacionRecibo;
import com.softlond.base.entity.ComprobanteEgreso;
import com.softlond.base.entity.ReciboCajaVenta;
import com.softlond.base.entity.ConceptoReciboEgreso;


import lombok.Data;

@Data
public class ReciboDocumentoPago {

	String tipo;
	String numero;
	Date fecha;
	Double total;
	String Idcomprobante;
	ConceptoReciboEgreso concepto;
	
	public ReciboDocumentoPago() {
		super();
	}
	
	public static ReciboDocumentoPago convertirReciboDocumento(ReciboCajaVenta recibo) {
		ReciboDocumentoPago documento = new ReciboDocumentoPago();
		documento.setTipo("RCV");
		documento.setNumero(recibo.getConceptos().get(0).getNroDocumento());
		documento.setFecha(recibo.getFechaPago());
		documento.setTotal(recibo.getConceptos().get(0).getValorAbono());
		return documento;
	}
	
	public static ReciboDocumentoPago convertirAsignacionDocumento(ReciboCajaVenta recibo) {
		ReciboDocumentoPago documento = new ReciboDocumentoPago();
		documento.setTipo("RCV");
		documento.setNumero(recibo.getAsignacion().getNotaCredito().getNumeroDocumento());
		documento.setFecha(recibo.getFechaPago());
		documento.setTotal(Double.valueOf(String.valueOf(recibo.getAsignacion().getNotaCredito().getTotal())));
		return documento;
	}
	
	public static ReciboDocumentoPago convertirReciboComprobante(ComprobanteEgreso recibo, ConceptoReciboEgreso concepto) {
		ReciboDocumentoPago documento = new ReciboDocumentoPago();
		documento.setTipo(recibo.getTipoDocumento());
		documento.setNumero(recibo.getConceptos().get(0).getNumeroDocumento());
		documento.setFecha(recibo.getFechaDocumento());
		documento.setTotal(recibo.getConceptos().get(0).getValorAbono());
		documento.setConcepto(concepto);
		return documento;
	}
	
	public static ReciboDocumentoPago convertirAsignacionComprobante(ComprobanteEgreso recibo) {
		ReciboDocumentoPago documento = new ReciboDocumentoPago();
		documento.setTipo("NCP");
		documento.setNumero(recibo.getAsignacion().getNotaCredito().getNumeroDocumento());
		documento.setFecha(recibo.getFechaDocumento());
		documento.setTotal(Double.valueOf(String.valueOf(recibo.getAsignacion().getNotaCredito().getTotal())));
		return documento;
	}
	
	public static ReciboDocumentoPago convertirReciboComprobanteGastosServicio(ComprobanteEgreso recibo) {
		ReciboDocumentoPago documento = new ReciboDocumentoPago();
		documento.setTipo(recibo.getTipoDocumento());
		documento.setNumero(recibo.getPrefijo() + recibo.getNumeroDocumento());
		documento.setFecha(recibo.getFechaDocumento());
		documento.setTotal(Double.valueOf(String.valueOf(recibo.getTotal())));
		return documento;
	}
	
	
}

