package com.softlond.base.response;

import java.util.Date;

import com.softlond.base.entity.Articulo;

import lombok.Data;

@Data
public class ProductosPreciosArticulos {
    
    String empresa;
    String codigo;
    String producto;
    String articulo;
    Date fechaIngreso;
    Double cantCompra;
    Double cantMedida;
    Double cantDisponible;
    Double precioCosto;
    Double cantVendida;
    Double precioVendida;
    Double cantDevCompra;
    Double precioDeVCompra;
    Double cantDevVenta;
    float vVsC;
    
    public ProductosPreciosArticulos() {}
    

    public static ProductosPreciosArticulos convertirArticulo(Articulo ar) {
        ProductosPreciosArticulos ic = new ProductosPreciosArticulos();
        ic.empresa = ar.getSede().getIdUnico();
        ic.codigo = ar.getProducto().getCodigo();
        ic.producto = ar.getProducto().getProducto();
        ic.articulo = ar.getCodigo();
        ic.fechaIngreso = ar.getFechaIngreso();
        ic.cantCompra = ar.getCantidadCompra() * ar.getPrecioCosto();
        ic.cantMedida = ar.getCantidadMedida() * ar.getPrecioCosto();
        ic.cantDisponible = ar.getCantidadDisponible() * ar.getPrecioCosto();
        ic.precioCosto = ar.getPrecioCosto();
        ic.cantVendida = 0.0;
        ic.cantDevCompra = 0.0;
        ic.cantDevVenta = 0.0;
        ic.vVsC = 0;
        ic.precioVendida = 0.0;
        ic.precioDeVCompra = 0.0;
        return ic;
    }
}
