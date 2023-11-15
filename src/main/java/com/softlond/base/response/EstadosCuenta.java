package com.softlond.base.response;

import java.util.Date;

import com.softlond.base.entity.FacturaCompra;
import com.softlond.base.entity.NotaCredito;
import com.softlond.base.entity.NotaDebito;
import com.softlond.base.entity.Proveedor;

import lombok.Data;

@Data
public class EstadosCuenta {

	String empresa;
    String tipo;
    String numero;
    Integer total;
    Integer saldo;
    Integer abonos;
    Integer diasVencidos;
    Date fecha;
    Date fechaVencimiento;
    String concepto;
    Double descuento;
    String documento;
    Proveedor proveedor;
    
    public EstadosCuenta() {
    	
    }
    
    public static EstadosCuenta convertirNotaCred(NotaCredito nc) {
    	EstadosCuenta ec = new EstadosCuenta();
        ec.tipo = "Nota Crédito";
        ec.empresa = nc.getIdSede().getIdUnico();
        ec.numero = nc.getPrefijo().getPrefijo() + "-" + nc.getNumeroDocumento();
        ec.total = nc.getTotal();
        ec.saldo = nc.getTotal();
        ec.abonos = 0;
        ec.diasVencidos = 0;
        ec.fecha = nc.getFechaDocumento();
        ec.fechaVencimiento = null;
        ec.concepto = "nota credito "+nc.getNumeroDocumento();
        ec.documento = nc.getPrefijo().getPrefijo();
        ec.proveedor = nc.getIdProveedor();
        return ec;
    }

    public static EstadosCuenta convertirNotaDeb(NotaDebito nd) {
    	EstadosCuenta ec = new EstadosCuenta();
        ec.tipo = "Nota Débito";
        ec.empresa = nd.getIdSede().getIdUnico();
        ec.numero = nd.getPrefijo().getPrefijo() + "-" + nd.getNumeroDocumento();
        ec.total = nd.getTotal();
        ec.saldo = nd.getTotal();
        ec.abonos = 0;
        ec.diasVencidos = 0;
        ec.fecha = nd.getFechaDocumento();
        ec.fechaVencimiento = null;
        ec.concepto = "nota debito "+nd.getNumeroDocumento();
        ec.documento = nd.getPrefijo().getPrefijo();
        ec.proveedor = nd.getIdProveedor();
        return ec;
    }

    public static EstadosCuenta convertirFactura(FacturaCompra fc) {
    	EstadosCuenta ec = new EstadosCuenta();
        ec.tipo = "Factura";
        ec.empresa = fc.getIdSede().getIdUnico();
        ec.numero = fc.getNroFactura();
        ec.total = fc.getTotal();
        ec.saldo = fc.getTotal();
        ec.abonos = 0;
        ec.diasVencidos = diasVencidos(fc.getFechaVencimiento());
        ec.fecha = fc.getFechaFactura();
        ec.fechaVencimiento = fc.getFechaVencimiento();
        ec.concepto = "factura de compra "+fc.getNroFactura();
        ec.documento = "FC";
        ec.proveedor = fc.getProveedor();
        return ec;
    }

    private static int diasVencidos(Date fechaVencimiento) {
    	long tiempoActual = new Date().getTime();
        long tiempoFechaVencimiento =  (new Date(fechaVencimiento.getTime())).getTime();
        long diff = tiempoFechaVencimiento - tiempoActual;
        return (int) (diff / (1000 * 60 * 60 * 24));
    }
}
