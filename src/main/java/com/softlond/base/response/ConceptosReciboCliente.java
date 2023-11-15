package com.softlond.base.response;

import java.util.Date;

import com.softlond.base.entity.Factura;
import com.softlond.base.entity.NotaCreditoCliente;
import com.softlond.base.entity.NotaDebitoCliente;

import lombok.Data;

@Data
public class ConceptosReciboCliente {

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

    public ConceptosReciboCliente() {
    }

    public static ConceptosReciboCliente convertirNotaCred(NotaCreditoCliente nc) {
        ConceptosReciboCliente mv = new ConceptosReciboCliente();
        mv.tipo = "Nota Crédito";
        mv.empresa = nc.getIdSede().getIdUnico();
        mv.numero = nc.getPrefijo().getPrefijo() + "-" + nc.getNumeroDocumento();
        mv.total = nc.getTotal();
        mv.saldo = nc.getTotal();
        mv.diasVencidos = 0;
        mv.fecha = nc.getFechaDocumento();
        mv.descuento = 0.0;
        return mv;
    }

    public static ConceptosReciboCliente convertirNotaDeb(NotaDebitoCliente nd) {
        ConceptosReciboCliente mv = new ConceptosReciboCliente();
        mv.tipo = "Nota Débito";
        mv.empresa = nd.getIdSede().getIdUnico();
        mv.numero = nd.getPrefijo().getPrefijo() + "-" + nd.getNumeroDocumento();
        mv.total = nd.getTotal();
        mv.saldo = nd.getTotal();
        mv.diasVencidos = 0;
        mv.fecha = nd.getFechaDocumento();
        mv.descuento = 0.0;
        return mv;
    }

    public static ConceptosReciboCliente convertirFactura(Factura fc) {
        ConceptosReciboCliente mv = new ConceptosReciboCliente();
        mv.tipo = "Factura";
        mv.empresa = fc.getIdSede().getIdUnico();
        mv.numero = fc.getPrefijo().getPrefijo() + fc.getNroFactura();
        mv.total = fc.getTotal();
        mv.saldo = fc.getTotal();
        mv.diasVencidos = diasTranscurridos(fc.getFechaVenta());
        mv.fecha = new Date(fc.getFechaVenta().getTime());
        mv.concepto = fc.getId().toString();
        mv.vence = fc.getFechaVencimientoCr();
        mv.descuento = 0.0;
        return mv;
    }

    private static int diasTranscurridos(Date fecha) {
        long tiempoActual = new java.util.Date().getTime();
        long tiempoFecha = fecha.getTime();
        long diff = tiempoActual - tiempoFecha;
        return (int) diff / (1000 * 60 * 60 * 24);
    }

}
