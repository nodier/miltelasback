package com.softlond.base.response;

import java.util.Date;

import com.softlond.base.entity.DevolucionCompras;
import com.softlond.base.entity.Factura;
import com.softlond.base.entity.FacturaCompra;

import lombok.Data;

@Data
public class AsignacionesPendientesRecibo {

	String empresa;
    String tipo;
    String numero;
    Integer total;
    Integer saldo;
    Integer abonos;
    Date fecha;
    String concepto;
    
    public static AsignacionesPendientesRecibo convertirFactura(Factura fc) {
    	AsignacionesPendientesRecibo ap = new AsignacionesPendientesRecibo();
        ap.tipo = "Factura";
        ap.empresa = fc.getIdSede().getIdUnico();
        ap.numero = fc.getPrefijo().getPrefijo() + fc.getNroFactura();
        ap.total = fc.getTotal();
        ap.saldo = fc.getTotal();
        ap.fecha = new Date(fc.getFechaVenta().getTime());
        ap.abonos = 0;
        ap.concepto = "";
        return ap;
    }
}
