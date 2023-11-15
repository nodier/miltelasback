package com.softlond.base.response;

import java.util.Date;

import com.softlond.base.entity.PrdColores;
import com.softlond.base.entity.PrdPresentacion;
import com.softlond.base.entity.PrdReferencia;
import com.softlond.base.entity.PrdTipos;
import com.softlond.base.entity.Producto;

import lombok.Data;

@Data
public class ProductosNoExistentes {
    String codigo;
    String producto;
    String clasificacion;
    String idUltArticulo;
    PrdTipos tipo;
    PrdReferencia referencia;
    PrdPresentacion presentacion;
    PrdColores color;
    Date ultFecha;
    Double ultCosto;
    Double ultPrecioVenta;
    Integer stMin;
    Integer stMax;
    
    public ProductosNoExistentes() {}

    public static ProductosNoExistentes convertirProductos(Producto pr) {
        ProductosNoExistentes ic = new ProductosNoExistentes();
        ic.codigo = pr.getCodigo();
        ic.producto = pr.getProducto();
        ic.clasificacion = pr.getClasificacion().getTClasificacion();
        ic.idUltArticulo = "";
        ic.ultFecha = null;
        ic.ultCosto = 0.0;
        ic.ultPrecioVenta = 0.0;
        ic.stMin = pr.getStMin();
        ic.stMax = pr.getStMax();
        ic.tipo =pr.getTipo();
        ic.referencia = pr.getReferencia();
        ic.presentacion = pr.getPresentacion();
        ic.color = pr.getColor();
     
        return ic;
    }

}
