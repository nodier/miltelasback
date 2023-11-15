package com.softlond.base.response;


import java.util.Date;
import java.util.List;

import com.softlond.base.entity.ComprobanteEgreso;
import com.softlond.base.entity.ConceptoReciboEgreso;
import com.softlond.base.entity.Factura;
import com.softlond.base.entity.FacturaCompra;
import com.softlond.base.entity.NotaCredito;
import com.softlond.base.entity.NotaDebito;

import lombok.Data;

@Data
public class PagoDocumento {

	String tipo;
	String numero;
	Date fecha;
	Integer total;
	
	public PagoDocumento() {
	}
	
	
	public static  PagoDocumento convertirFactura(FacturaCompra factura){
		PagoDocumento pago = new PagoDocumento();
		pago.tipo = "FCP";
		pago.numero = factura.getNroFactura();
		pago.fecha = factura.getFechaFactura();
		pago.total = factura.getTotal();
		return pago;
	}
	
	public static  PagoDocumento convertirNotaCredito(NotaCredito nc){
		PagoDocumento pago = new PagoDocumento();
		pago.tipo = "NCP";
		pago.numero = nc.getNumeroDocumento();
		pago.fecha = nc.getFechaDocumento();
		pago.total = nc.getTotal();
		return pago;
	}
	
	public static  PagoDocumento convertirNotaDebito(NotaDebito nd){
		PagoDocumento pago = new PagoDocumento();
		pago.tipo = "NDP";
		pago.numero = nd.getNumeroDocumento();
		pago.fecha = nd.getFechaDocumento();
		pago.total = nd.getTotal();
		return pago;
	}
	
	public static  PagoDocumento convertirFacturaCliente(Factura f){
		PagoDocumento pago = new PagoDocumento();
		pago.tipo = "FVC";
		pago.numero = f.getPrefijo().getPrefijo() + f.getNroFactura();
		pago.fecha = f.getFechaVenta();
		pago.total = f.getTotal();
		return pago;
	}
	
}
