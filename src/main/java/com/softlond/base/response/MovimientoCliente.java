package com.softlond.base.response;

import java.util.Date;

import com.softlond.base.entity.Factura;
import com.softlond.base.entity.NotaCreditoCliente;
import com.softlond.base.entity.NotaDebitoCliente;
import com.softlond.base.entity.Proveedor;

import lombok.Data;

@Data
public class MovimientoCliente {
    String empresa;
    String prefijo;
    String reciboCajaVenta;
    Date fechaPago;
    Integer totalRecibo;
    Integer saldo;
    Integer diasCorrientes;
    String concepto;
    String numeroDocumento;
    Integer valorAbono;
    Integer descuento;

    public MovimientoCliente() {}
    // Factura, Recibo, Nota crédito y débito, egresos, recibos

    public static MovimientoCliente convertirNotaCred(NotaCreditoCliente nc) {
        MovimientoCliente mv = new MovimientoCliente();
       
        mv.empresa = nc.getIdSede().getIdUnico();
        mv.prefijo = nc.getPrefijo().getPrefijo();
        //mv.reciboCajaVenta = "";
        mv.fechaPago = nc.getFechaDocumento();
        mv.totalRecibo = nc.getTotal();
        mv.saldo = nc.getTotal();
        mv.diasCorrientes = 0;
        mv.numeroDocumento = nc.getNumeroDocumento();
        mv.valorAbono = 0;
        mv.descuento = 0;
        mv.concepto = "NC" + " " + nc.getObservaciones();
        return mv;
    }

    public static MovimientoCliente convertirNotaDeb(NotaDebitoCliente nd) {
        MovimientoCliente mv = new MovimientoCliente();
        mv.empresa = nd.getIdSede().getIdUnico();
        mv.prefijo = nd.getPrefijo().getPrefijo();
        mv.reciboCajaVenta = "";
        mv.fechaPago = nd.getFechaDocumento();
        mv.totalRecibo = nd.getTotal();
        mv.saldo = nd.getTotal();
        mv.diasCorrientes = 0;
        mv.numeroDocumento = nd.getNumeroDocumento();
        mv.valorAbono = nd.getTotal();
        mv.descuento = 0;
        return mv;
    }

    public static MovimientoCliente convertirFactura(Factura fc) {
        MovimientoCliente mv = new MovimientoCliente();
        mv.prefijo = "Factura";
        mv.empresa = fc.getIdSede().getIdUnico();
        mv.reciboCajaVenta = fc.getIdReciboCaja()!=null?fc.getIdReciboCaja().getCodRbocajaventa():"";
        mv.fechaPago = fc.getFechaVencimientoCr();
        mv.totalRecibo = fc.getTotal();
        mv.saldo = fc.getTotal();
        mv.diasCorrientes = diasVencidos(fc.getFechaVencimientoCr()!=null?fc.getFechaVencimientoCr():fc.getFechaVenta());
        mv.numeroDocumento = String.valueOf(fc.getNroFactura());
        mv.valorAbono = fc.getTotal();
        mv.descuento = 0;
        mv.concepto = fc.getIdConcepto()!=null?fc.getIdConcepto().getCodDocumento():"";
        
        return mv;
    }

    private static int diasVencidos(Date fecha) {
        long tiempoActual = new java.util.Date().getTime();
        long tiempoFecha = fecha!=null?fecha.getTime():tiempoActual;
        long diff = tiempoActual - tiempoFecha;
        return (int) diff / (1000 * 60 * 60 * 24);
    }
    

    public String getEmpresa() {
        return empresa;
    }

    public String getPrefijo() {
        return prefijo;
    }

    public String getReciboCajaVenta() {
        return reciboCajaVenta;
    }
    
    public Date getFechaPago() {
        return fechaPago;
    }

    public Integer getTotalRecibo() {
        return totalRecibo;
    }

    public Integer getSaldo() {
        return saldo;
    }

    public Integer getDiasCorrientes() {
        return diasCorrientes;
    }
    
    public String getConcepto() {
        return concepto;
    }
    
    public String getNumeroDocumento() {
        return numeroDocumento;
    }
    
    public Integer getValorAbono() {
        return valorAbono;
    }
    
    public Integer getDescuento() {
        return descuento;
    }

  
}
