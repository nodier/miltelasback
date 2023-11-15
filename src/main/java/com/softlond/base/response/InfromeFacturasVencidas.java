package com.softlond.base.response;

import com.softlond.base.entity.FacturaCompra;
import com.softlond.base.entity.NotaCredito;
import com.softlond.base.entity.NotaDebito;

import java.util.Date;

public class InfromeFacturasVencidas {
    String proveedor;
    String empresa;
    String nit;
    String documento;
    String nroFactura;
    Date fechaFactura;
    Date fechaVencimiento;
    Integer diasVencidos;
    String concepto;
    Double subtotalCompras;
    Integer retenciones;
    Float ajustes;
    Double iva;
    Integer adicionales;
    Integer total;
    Integer saldo;
    Integer abono;
    

    public InfromeFacturasVencidas() {}
    // Factura, Recibo, Nota crédito y débito, egresos, recibos

    public static InfromeFacturasVencidas convertirNotaCred(NotaCredito nc) {
        InfromeFacturasVencidas ic = new InfromeFacturasVencidas();
        ic.proveedor = nc.getIdProveedor().getProveedor();
        ic.empresa = nc.getIdSede().getIdUnico();
        ic.nit = nc.getIdProveedor().getNit() + "-" + nc.getIdProveedor().getDigito();
        ic.documento = nc.getPrefijo().getPrefijo();
        ic.nroFactura = nc.getNumeroDocumento();
        ic.fechaFactura = nc.getFechaDocumento();
        ic.fechaVencimiento = nc.getFechaDocumento();
        ic.diasVencidos = 0;
        ic.concepto = nc.getObservaciones();
        ic.subtotalCompras = 0.0;
        ic.retenciones = 0;
        ic.ajustes = (float) 0;
        ic.iva = 0.0;
        ic.adicionales = 0;
        ic.total = nc.getTotal();
        ic.saldo = nc.getTotal();
        return ic;
    }

    public static InfromeFacturasVencidas convertirNotaDeb(NotaDebito nd) {
        InfromeFacturasVencidas ic = new InfromeFacturasVencidas();
        ic.proveedor = nd.getIdProveedor().getProveedor();
        ic.empresa = nd.getIdSede().getIdUnico();
        ic.nit = nd.getIdProveedor().getNit() + "-" + nd.getIdProveedor().getDigito();
        ic.documento = nd.getPrefijo().getPrefijo();
        ic.nroFactura = nd.getNumeroDocumento();
        ic.fechaFactura = nd.getFechaDocumento();
        ic.fechaVencimiento = nd.getFechaDocumento();
        ic.diasVencidos = 0;
        ic.concepto = nd.getObservaciones();
        ic.subtotalCompras = 0.0;
        ic.retenciones = 0;
        ic.ajustes = (float) 0;
        ic.iva = 0.0;
        ic.adicionales = 0;
        ic.total = nd.getTotal();
        ic.saldo = nd.getTotal();
        return ic;
    }

    public static InfromeFacturasVencidas convertirFactura(FacturaCompra fc) {
        InfromeFacturasVencidas ic = new InfromeFacturasVencidas();
        ic.proveedor = fc.getProveedor()!=null?fc.getProveedor().getProveedor():"";
        ic.empresa = fc.getIdSede().getIdUnico();
        ic.nit = fc.getProveedor()!=null?fc.getProveedor().getNit():"" + "-" + (fc.getProveedor()!=null?fc.getProveedor().getDigito():"");
        ic.documento = "Fact";
        ic.nroFactura = fc.getNroFactura();
        ic.fechaFactura = fc.getFechaFactura();
        ic.fechaVencimiento = fc.getFechaVencimiento();
        ic.diasVencidos = diasTranscurridos(fc.getFechaVencimiento());
        ic.concepto = fc.getConcepto();
        ic.subtotalCompras = fc.getSubTotalCompra();
        ic.retenciones = fc.getRetenciones();
        ic.ajustes = fc.getAjustes();
        ic.iva = fc.getIva();
        ic.adicionales = fc.getValoresAdicionales();
        ic.total = fc.getTotal();
        ic.saldo = fc.getTotal();
        return ic;
    }

    private static int diasTranscurridos(Date fecha) {
        long tiempoActual = new java.util.Date().getTime();
        long tiempoFecha = fecha.getTime();
        long diff = tiempoFecha - tiempoActual;
        return (int) (diff / (1000 * 60 * 60 * 24));
    }

    public String getProveedor() {
        return proveedor;
    }
    
    public String getEmpresa() {
        return empresa;
    }
    
    public String getNit() {
        return nit;
    }
    
    public String getDocumento() {
        return documento;
    }
    
    public String getNroFactura() {
        return nroFactura;
    }

    public Date getFechaFactura() {
        return fechaFactura;
    }
    
    public Integer getDiasVencidos() {
        return diasVencidos;
    }
    
    public String getConcepto() {
        return concepto;
    }
    
    public Double getSubtotalCompras() {
        return subtotalCompras;
    }

    public Integer getRetenciones() {
        return retenciones;
    }
    
    public Float getAjustes() {
        return ajustes;
    }
    
    public Double getIva() {
        return iva;
    }
    
    public Integer getAdicionales() {
        return adicionales;
    }

    public Integer getTotal() {
        return total;
    }

    public Integer getSaldo() {
        return saldo;
    }

	public Integer getAbono() {
		return abono;
	}

	public void setAbono(Integer abono) {
		this.abono = abono;
	}

	public void setSaldo(Integer saldo) {
		this.saldo = saldo;
	}

	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}
}


