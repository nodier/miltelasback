package com.softlond.base.response;

import java.util.Date;

import org.joda.time.DateTime;

import com.softlond.base.entity.ArticuloMovimientos;
import com.softlond.base.entity.ArticulosRemisionCompra;
import com.softlond.base.entity.Factura;
import com.softlond.base.entity.FacturaArticulos;
import com.softlond.base.entity.ReciboCajaVenta;
import com.softlond.base.entity.Traslado;

import lombok.Data;

@Data
public class MovimientosArticulos {
    String tipo;
    String prefijo;
    String numero;
    String articulo;
    Date fecha;
    String concepto;
    float cantidad;
    float Ingresa;
    Double salida;
    double costo;
    Double costoTotal;
    String producto;
    String codProducto;

    public MovimientosArticulos() {}
    // Factura, Recibo, Nota crédito y débito, egresos, recibos

    public static MovimientosArticulos convertirRemCompra(ArticulosRemisionCompra rc) {
        MovimientosArticulos mv = new MovimientosArticulos();
        mv.tipo = "REMC";
        mv.prefijo = rc.getIdRemisionCompra().getNumeroRemision();
        mv.numero = rc.getIdRemisionCompra().getNumeroRemision();
        mv.articulo = rc.getIdArticulo().getCodigo();
        mv.fecha = rc.getIdRemisionCompra().getFechaRemision();
        mv.concepto = "Remisión de compra";
        mv.cantidad = 0;
        mv.Ingresa = rc.getIdArticulo().getCantidadCompra();
        mv.salida= 0.0;
        mv.costo = rc.getIdArticulo().getPrecioCosto();
        mv.costoTotal = 0.0;
        mv.producto = rc.getIdArticulo().getProducto().getProducto();
        mv.codProducto = rc.getIdArticulo().getProducto().getCodigo();
        return mv;
    }

   public static MovimientosArticulos convertirRemVenta(ReciboCajaVenta rv, FacturaArticulos fa) {
        MovimientosArticulos mv = new MovimientosArticulos();
        mv.tipo = "REMV";
        mv.prefijo = rv.getPrefijo();
        mv.numero = rv.getCodRbocajaventa();
        mv.articulo = fa.getArticulo().getCodigo();
        mv.fecha = rv.getFechaPago();
        mv.concepto = "Remisión de venta";
        mv.cantidad = 0;
        mv.Ingresa = 0;
        mv.salida= fa.getCantidad();
        mv.costo = fa.getPrecioUnitario();
        mv.costoTotal = 0.0;
        mv.producto = fa.getArticulo().getProducto().getProducto();
        mv.codProducto = fa.getArticulo().getProducto().getCodigo();
        return mv;
    }

    public static MovimientosArticulos convertirFactura(FacturaArticulos fc, Factura fac) {
        MovimientosArticulos mv = new MovimientosArticulos();
        mv.tipo = "Venta";
        mv.prefijo = fac.getPrefijo().getPrefijo();
        mv.numero = String.valueOf(fac.getNroFactura());
        mv.articulo = fc.getArticulo().getCodigo();
        mv.fecha = fc.getArticulo().getFechaIngreso();
        mv.concepto = "Venta";
        mv.cantidad = fc.getArticulo().getCantidadCompra();
        mv.Ingresa = 0;
        mv.salida= fc.getCantidad();
        mv.costo = fc.getPrecioUnitario();
        mv.costoTotal = fc.getCantidad() * fc.getPrecioUnitario();
        mv.producto = fc.getArticulo().getProducto().getProducto();
        mv.codProducto = fc.getArticulo().getProducto().getCodigo();
        return mv;
    }
    
    public static MovimientosArticulos convertirTraslado(Traslado tr, ArticuloMovimientos am) {
        MovimientosArticulos mv = new MovimientosArticulos();
        String desde = "";
        String hasta = "";
        mv.tipo = "TRF";
        mv.prefijo = tr.getPrefijo().getPrefijo();
        mv.numero = String.valueOf(tr.getNumeroDocumento());
        mv.articulo = am.getArticulo().getCodigo();
        mv.fecha = am.getArticulo().getFechaIngreso();
        if(am.getSectorOrigen()!=null) {
        	desde = am.getSectorOrigen().getTSector();
        }
        if(am.getSectorDestino()!=null) {
        	hasta = am.getSectorDestino().getTSector();
        }
        mv.concepto = "Desde" + desde + "-" + hasta;
        mv.cantidad = am.getCantidad();
        mv.Ingresa = 0;
        mv.salida= 0.0;
        mv.costo = am.getArticulo().getPrecioCosto();
        mv.costoTotal = (double) (am.getCantidad() * am.getArticulo().getPrecioCosto());
        mv.producto = am.getArticulo().getProducto().getProducto();
        mv.codProducto = am.getArticulo().getProducto().getCodigo();
        return mv;
    }

}
