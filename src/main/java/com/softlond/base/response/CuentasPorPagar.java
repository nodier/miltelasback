package com.softlond.base.response;

import java.util.Date;

import com.softlond.base.entity.FacturaCompra;
import com.softlond.base.entity.NotaCredito;
import com.softlond.base.entity.NotaDebito;
import com.softlond.base.entity.Proveedor;

import lombok.Data;

@Data
public class CuentasPorPagar {

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
    
    public CuentasPorPagar() {}
    // Factura, Recibo, Nota crédito y débito, egresos, recibos

    public static CuentasPorPagar convertirNotaCred(NotaCredito nc) {
    	CuentasPorPagar cp = new CuentasPorPagar();
        cp.tipo = "Nota Crédito";
        cp.empresa = nc.getIdSede().getIdUnico();
        cp.numero = nc.getPrefijo().getPrefijo() + "-" + nc.getNumeroDocumento();
        cp.total = nc.getTotal();
        cp.saldo = nc.getTotal();
        cp.abonos = 0;
        cp.diasVencidos = 0;
        cp.fecha = nc.getFechaDocumento();
        cp.fechaVencimiento = nc.getFechaDocumento();
        cp.concepto = "nota credito "+nc.getNumeroDocumento();
        cp.documento = nc.getPrefijo().getPrefijo();
        cp.proveedor = nc.getIdProveedor();
        return cp;
    }

    public static CuentasPorPagar convertirNotaDeb(NotaDebito nd) {
    	CuentasPorPagar cp = new CuentasPorPagar();
        cp.tipo = "Nota Débito";
        cp.empresa = nd.getIdSede().getIdUnico();
        cp.numero = nd.getPrefijo().getPrefijo() + "-" + nd.getNumeroDocumento();
        cp.total = nd.getTotal();
        cp.saldo = nd.getTotal();
        cp.abonos = 0;
        cp.diasVencidos = 0;
        cp.fecha = nd.getFechaDocumento();
        cp.fechaVencimiento = nd.getFechaDocumento();
        cp.concepto = "nota dedito "+nd.getNumeroDocumento();
        cp.documento = nd.getPrefijo().getPrefijo();
        cp.proveedor = nd.getIdProveedor();
        return cp;
    }

    public static CuentasPorPagar convertirFactura(FacturaCompra fc) {
    	CuentasPorPagar cp = new CuentasPorPagar();
        cp.tipo = "Factura";
        cp.empresa = fc.getIdSede().getIdUnico();
        cp.numero = fc.getNroFactura();
        cp.total = fc.getTotal();
        cp.saldo = 0;
        cp.abonos = 0;
        cp.diasVencidos = diasVencidos(fc.getFechaVencimiento());
        cp.fecha = fc.getFechaFactura();
        cp.fechaVencimiento = fc.getFechaVencimiento();
        cp.concepto = "factura de compra "+fc.getNroFactura();
        cp.documento = "FC";
        cp.proveedor = fc.getProveedor();
        return cp;
    }

    private static int diasVencidos(Date fechaVencimiento) {
    	long tiempoActual = new Date().getTime();
        long tiempoFechaVencimiento = fechaVencimiento.getTime();
        long diff = tiempoFechaVencimiento - tiempoActual;
        return (int) (diff / (1000 * 60 * 60 * 24));
    }
}
