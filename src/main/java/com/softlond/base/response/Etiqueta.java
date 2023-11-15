package com.softlond.base.response;

import com.softlond.base.entity.Articulo;
import com.softlond.base.entity.Precio;
import com.softlond.base.entity.RemisionCompra;
import lombok.Data;

import java.util.List;

@Data
public class Etiqueta {
    String producto;
    String remision;
    Integer proveedor;
    String articulo;
    float cantidad;
    double precio;
    double costo;

    public static Etiqueta articuloRemision(Articulo a, RemisionCompra r) {
        Etiqueta e = new Etiqueta();
        e.producto = a.getProducto().getProducto();// ! se debe cambiar esta asignacion puesto que getProducto no posee
                                                   // el nombre real (cambiar por articulo.tipo,
                                                   // articulo.referencia....)
        e.remision = r != null ? r.getNumeroRemision() : "";
        e.proveedor = r != null ? r.getIdProveedor().getId() : 0;
        e.articulo = a.getCodigo();
        e.cantidad = a.getCantidadDisponible();
        e.costo = a.getPrecioCosto();
        List<Precio> precios = a.getProducto().getPrecios();
        if (precios.size() <= 0) {
            e.precio = 0;
        } else {
            for (int i = 0; i < a.getProducto().getPrecios().size(); i++) {
                if (a.getProducto().getPrecios().get(i).getSede().equals(a.getSede())) {
                    e.precio = a.getProducto().getPrecios().get(i).getPrecioVenta();
                }
            }
            // e.precio =
            // a.getProducto().getPrecios().get(a.getProducto().getPrecios().size() -
            // 1).getPrecioVenta()
            // .floatValue();
        }
        return e;
    }
}
