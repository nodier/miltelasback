package com.softlond.base.response;

import java.util.Date;

import com.softlond.base.entity.Articulo;

import lombok.Data;

@Data
public class ProductosCantidadesArticulos {
    
    String empresa;
    String codigo;
    String producto;
    String articulo;
    Date fechaIngreso;
    Float cantCompra;
    Float cantMedida;
    Float cantDisponible;
    Double cantVendida;
    Double cantDevCompra;
    Double cantDevVenta;
    float vVsC;
    
    public ProductosCantidadesArticulos() {}
    

    public static ProductosCantidadesArticulos convertirArticulo(Articulo ar) {
        ProductosCantidadesArticulos ic = new ProductosCantidadesArticulos();
        ic.empresa = ar.getSede().getIdUnico();
        ic.codigo = ar.getProducto().getCodigo();
        ic.producto = ar.getProducto().getProducto();
        ic.articulo = ar.getCodigo();
        ic.fechaIngreso = ar.getFechaIngreso();
        ic.cantCompra = ar.getCantidadCompra();
        ic.cantMedida = ar.getCantidadMedida();
        ic.cantDisponible = ar.getCantidadDisponible();
        ic.cantVendida = 0.0;
        ic.cantDevCompra = 0.0;
        ic.cantDevVenta = 0.0;
        ic.vVsC = 0;
        return ic;
    }
}
