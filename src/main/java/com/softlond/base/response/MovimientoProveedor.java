package com.softlond.base.response;

import com.softlond.base.entity.ComprobanteEgreso;
import com.softlond.base.entity.FacturaCompra;
import com.softlond.base.entity.NotaCredito;
import com.softlond.base.entity.NotaDebito;

import java.util.Date;

public class MovimientoProveedor {
    String empresa;
    String tipo;
    String numero;
    Integer total;
    Integer saldo;
    Integer diasVencidos;
    double descuento;
    Date fecha;
    Date vence;
    String concepto;

    public MovimientoProveedor() {}
    // Factura, Recibo, Nota crédito y débito, egresos, recibos

    public static MovimientoProveedor convertirNotaCred(NotaCredito nc) {
        MovimientoProveedor mv = new MovimientoProveedor();
        mv.tipo = "Nota Crédito";
        mv.empresa = nc.getIdSede().getIdUnico();
        mv.numero = nc.getPrefijo().getPrefijo() + "-" + nc.getNumeroDocumento();
        mv.total = nc.getTotal();
        mv.saldo = nc.getTotal();
        mv.diasVencidos = 0;
        mv.fecha = new Date(nc.getFechaDocumento().getTime());
        mv.vence = new Date(nc.getFechaDocumento().getTime());
        mv.descuento = 0.0;
        return mv;
    }

    public static MovimientoProveedor convertirNotaDeb(NotaDebito nd) {
        MovimientoProveedor mv = new MovimientoProveedor();
        mv.tipo = "Nota Débito";
        mv.empresa = nd.getIdSede().getIdUnico();
        mv.numero = nd.getPrefijo().getPrefijo() + "-" + nd.getNumeroDocumento();
        mv.total = nd.getTotal();
        mv.saldo = nd.getTotal();
        mv.diasVencidos = 0;
        mv.fecha = new Date(nd.getFechaDocumento().getTime());
        mv.vence = new Date(nd.getFechaDocumento().getTime());
        mv.descuento = 0.0;
        return mv;
    }

    public static MovimientoProveedor convertirFactura(FacturaCompra fc) {
        MovimientoProveedor mv = new MovimientoProveedor();
        mv.tipo = "Factura";
        mv.empresa = fc.getIdSede().getIdUnico();
        mv.numero = fc.getNroFactura();
        mv.total = fc.getTotal();
        mv.saldo = fc.getTotal();
        mv.diasVencidos = diasTranscurridos(fc.getFechaFactura());
        mv.fecha = new Date(fc.getFechaFactura().getTime());
        mv.vence = new Date(fc.getFechaVencimiento().getTime());
        mv.concepto = fc.getConcepto();
        mv.descuento = 0.0;
        return mv;
    }
    
    public static MovimientoProveedor convertirComprobanteEgreso(ComprobanteEgreso comprobante) {
        MovimientoProveedor mv = new MovimientoProveedor();
        mv.tipo = "Comprobante";
        mv.empresa = comprobante.getSede().getIdUnico();
        mv.numero = comprobante.getPrefijo() + comprobante.getNumeroDocumento();
        mv.total = Math.round(comprobante.getTotal());
        mv.saldo = Math.round(comprobante.getTotal());
        mv.diasVencidos = 0;
        mv.fecha = comprobante.getFechaDocumento();
        mv.concepto = comprobante.getObservaciones();
        mv.vence = comprobante.getFechaDocumento();
        mv.descuento = 0.0;
        return mv;
    }

    private static int diasTranscurridos(Date fecha) {
        long tiempoActual = new java.util.Date().getTime();
        long tiempoFecha = fecha.getTime();
        long diff = tiempoActual - tiempoFecha;
        return (int) diff / (1000 * 60 * 60 * 24);
    }

    public String getEmpresa() {
        return empresa;
    }

    public String getTipo() {
        return tipo;
    }

    public String getNumero() {
        return numero;
    }

    public Integer getTotal() {
        return total;
    }

    public Integer getSaldo() {
        return saldo;
    }

    public Integer getDiasVencidos() {
        return diasVencidos;
    }

    public Date getFecha() {
        return fecha;
    }

	public void setTotal(Integer total) {
		this.total = total;
	}

	public void setSaldo(Integer saldo) {
		this.saldo = saldo;
	}

	public double getDescuento() {
		return descuento;
	}

	public void setDescuento(double descuento) {
		this.descuento = descuento;
	}

	public Date getVence() {
		return vence;
	}

	public void setVence(Date vence) {
		this.vence = vence;
	}

	public String getConcepto() {
		return concepto;
	}

	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}
}


