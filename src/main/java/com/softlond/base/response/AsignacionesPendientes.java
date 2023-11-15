package com.softlond.base.response;

import java.util.Date;

import com.softlond.base.entity.DevolucionCompras;
import com.softlond.base.entity.FacturaCompra;

import lombok.Data;

@Data
public class AsignacionesPendientes {

	String empresa;
    String tipo;
    String numero;
    Integer total;
    Integer saldo;
    Integer abonos;
    Date fecha;
    String concepto;
    
    public static AsignacionesPendientes convertirFactura(FacturaCompra fc) {
    	AsignacionesPendientes ap = new AsignacionesPendientes();
        ap.tipo = "Factura";
        ap.empresa = fc.getIdSede().getIdUnico();
        ap.numero = fc.getNroFactura();
        ap.total = fc.getTotal();
        ap.saldo = fc.getTotal();
        ap.fecha = new Date(fc.getFechaFactura().getTime());
        ap.abonos = 0;
        ap.concepto = fc.getConcepto();
        return ap;
    }
    
    public static AsignacionesPendientes convertirDevoluciones(DevolucionCompras dc) {
    	AsignacionesPendientes ap = new AsignacionesPendientes();
        ap.tipo = "Devolucion";
        ap.empresa = dc.getIdSede().getIdUnico();
        ap.numero = dc.getNroDevolucion();
        ap.total = dc.getTotal();
        ap.saldo = dc.getTotal();
        ap.abonos = 0;
        ap.fecha = new Date(dc.getFechaFactura().getTime());
        ap.concepto = "";
        return ap;
    }
}
