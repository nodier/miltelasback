package com.softlond.base.response;

import java.util.Date;
import java.util.List;

import com.softlond.base.entity.ConceptosReciboCaja;
import com.softlond.base.entity.NotaCreditoCliente;
import com.softlond.base.entity.NotaDebitoCliente;
import com.softlond.base.entity.ReciboCajaVenta;

import lombok.Data;

@Data
public class DetallesDePago {
	String empresa;
    String prefijo;
    String reciboCajaVenta;
    Date fechaPago;
    Integer totalRecibo;
    Integer saldo;
    Integer diasCorrientes;
    String concepto;
    String numeroDocumento;
    Double valorAbono;
    Integer descuento;
    String codRbocajaventa;
    

    public DetallesDePago() {}
    // Factura, Recibo, Nota crédito y débito, egresos, recibos

   /* public static DetallesDePago convertirNotaCreditoCliente(NotaCreditoCliente nc) {
        DetallesDePago mv = new DetallesDePago();
        
        mv.empresa = nc.getIdSede().getIdUnico();
        mv.prefijo = nc.getPrefijo().getPrefijo();
        //mv.reciboCajaVenta = "";
        mv.fechaPago = nc.getFechaDocumento();
        mv.totalRecibo = nc.getTotal();
        mv.saldo = nc.getTotal();
        mv.diasCorrientes = 0;
        mv.numeroDocumento = nc.getNumeroDocumento();
        mv.valorAbono = nc.getTotal();
        mv.descuento = 0;
        mv.concepto = "NC" + " " + nc.getObservaciones();
        return mv;
        
    }*/

   /* public static DetallesDePago convertirNotaDebitoCliente(NotaDebitoCliente nd) {
        DetallesDePago mv = new DetallesDePago();
        
        mv.empresa = nd.getIdSede().getIdUnico();
        mv.prefijo = nd.getPrefijo().getPrefijo();
        //mv.reciboCajaVenta = "";
        mv.fechaPago = nd.getFechaDocumento();
        mv.totalRecibo = nd.getTotal();
        mv.saldo = nd.getTotal();
        mv.diasCorrientes = 0;
        mv.numeroDocumento = nd.getNumeroDocumento();
        mv.valorAbono = nd.getTotal();
        mv.descuento = 0;
        mv.concepto = "NC" + " " + nd.getObservaciones();
        return mv;
       
    }*/
    

    public static DetallesDePago convertirRecibo(ReciboCajaVenta rc, List<ConceptosReciboCaja> documentos) {
        DetallesDePago cv = new DetallesDePago();
        
        cv.empresa = rc.getIdSede().getIdUnico();
        cv.prefijo = rc.getPrefijo();
        cv.codRbocajaventa = rc.getCodRbocajaventa();
        cv.fechaPago = rc.getFechaPago()!=null?rc.getFechaPago():new java.util.Date();
        cv.totalRecibo = rc.getTotalRecibo();
        cv.saldo = rc.getSaldo();
        cv.diasCorrientes = diasTranscurridos(rc.getFechaPago());
        cv.concepto = "RC" + " " + rc.getObservaciones();
        cv.numeroDocumento = numeroDocumentos(documentos);
        cv.valorAbono = Double.parseDouble(String.valueOf(rc.getTotalRecibo()));
        return cv;
    }
    
    private static  String numeroDocumentos(List<ConceptosReciboCaja> documentos) {
    	String numero = "";
    	for (ConceptosReciboCaja concepto : documentos) {
			numero += concepto.getNroDocumento() +". ";
		}
    	return numero;
    }

   private static int diasTranscurridos(Date fecha) {
        long tiempoActual = new java.util.Date().getTime();
        long tiempoFecha = fecha!=null?fecha.getTime():tiempoActual;
        long diff = tiempoFecha - tiempoActual;
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
    
    public Double getValorAbono() {
        return valorAbono;
    }
    
    public Integer getDescuento() {
        return descuento;
    }
    
    public String getCodRbocajaventa() {
        return codRbocajaventa;
    }

}
