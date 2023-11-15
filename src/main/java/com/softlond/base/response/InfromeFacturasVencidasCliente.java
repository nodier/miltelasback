package com.softlond.base.response;

import java.util.Date;

import com.softlond.base.entity.Factura;
import com.softlond.base.entity.NotaCreditoCliente;
import com.softlond.base.entity.NotaDebitoCliente;

import lombok.Data;

@Data
public class InfromeFacturasVencidasCliente {
    String cliente;
    String empresa;
    String documento;
    String prefijo;
    String numero;
    Date fechaVenta;
    Integer diasVencidos;
    Integer total;
    Integer saldo;
    

    public InfromeFacturasVencidasCliente() {}
    // Factura, Recibo, Nota crédito y débito, egresos, recibos

    /*public static InfromeFacturasVencidasCliente convertirNotaCred(NotaCreditoCliente nc) {
        InfromeFacturasVencidasCliente ic = new InfromeFacturasVencidasCliente();
        ic.cliente = nc.getIdCliente().getNombres() + " " + nc.getIdCliente().getApellidos();
        ic.empresa = nc.getIdSede().getIdUnico(); 
        ic.documento = nc.getIdCliente().getNitocc();
        ic.prefijo = nc.getPrefijo().getPrefijo();
        ic.numero = nc.getNumeroDocumento();
        ic.fechaVenta = nc.getFechaDocumento();
        ic.diasVencidos = 0;
        ic.total = nc.getTotal();
        ic.saldo = nc.getTotal();
        return ic;
    }*/

    /*
    public static InfromeFacturasVencidasCliente convertirNotaDeb(NotaDebitoCliente nd) {
        InfromeFacturasVencidasCliente ic = new InfromeFacturasVencidasCliente();
        ic.cliente = nd.getIdCliente().getNombres() + " " + nd.getIdCliente().getApellidos();
        ic.empresa = nd.getIdSede().getIdUnico(); 
        ic.documento = nd.getIdCliente().getNitocc();
        ic.prefijo = nd.getPrefijo().getPrefijo();
        ic.numero = nd.getNumeroDocumento();
        ic.fechaVenta = nd.getFechaDocumento();
        ic.diasVencidos = 0;
        ic.total = nd.getTotal();
        ic.saldo = nd.getTotal();
        return ic;
    }*/

    public static InfromeFacturasVencidasCliente convertirFactura(Factura fc) {
        InfromeFacturasVencidasCliente ic = new InfromeFacturasVencidasCliente();
        ic.cliente = fc.getIdCliente()!=null?fc.getIdCliente().getNombres() + " " + fc.getIdCliente().getApellidos():"";
        ic.empresa = fc.getIdSede().getIdUnico();
        ic.documento = fc.getIdCliente()!=null?fc.getIdCliente().getNitocc():"";
        ic.prefijo = "Fact";
        ic.numero = String.valueOf(fc.getNroFactura());
        ic.fechaVenta = fc.getFechaVenta();
        ic.diasVencidos = diasTranscurridos(fc.getFechaVencimientoCr());
        ic.total = fc.getTotal();
        ic.saldo = fc.getTotal();
        return ic;
    }

    private static int diasTranscurridos(Date fecha) {
        long tiempoActual = new java.util.Date().getTime();
        long tiempoFecha = fecha.getTime();
        long diff = tiempoFecha - tiempoActual;
        return (int) diff / (1000 * 60 * 60 * 24);
    }

    public String getCliente() {
        return cliente;
    }
    
    public String getEmpresa() {
        return empresa;
    }
    
    public String getPrefijo() {
        return prefijo;
    }
    
    public String getNumero() {
        return numero;
    }
    
    public Date getFechaVenta() {
        return fechaVenta;
    }
    
    public Integer getDiasVencidos() {
        return diasVencidos;
    }

    public Integer getTotal() {
        return total;
    }

    public Integer getSaldo() {
        return saldo;
    }
}
