package com.softlond.base.response;

import com.softlond.base.entity.Articulo;
import com.softlond.base.entity.PrdColores;
import com.softlond.base.entity.PrdPresentacion;
import com.softlond.base.entity.PrdReferencia;
import com.softlond.base.entity.PrdTipos;
import com.softlond.base.entity.Producto;

import lombok.Data;

@Data
public class ProductosExistentes {
    String codigo;
    String producto;
    String clasificacion;
    PrdTipos tipo;
    PrdReferencia referencia;
    PrdPresentacion presentacion;
    PrdColores color;
    Integer articulos;
    Double disponible;
    String unidad;
    Integer stMin;
    Integer stMax;
    

    public ProductosExistentes() {}

    public static ProductosExistentes convertirProductos(Producto pr) {
        ProductosExistentes ic = new ProductosExistentes();
        ic.codigo = pr.getCodigo();
        ic.producto = pr.getProducto();
        ic.clasificacion = pr.getClasificacion()!=null?pr.getClasificacion().getTClasificacion():"";
        ic.articulos = 0;
        ic.disponible = 0.0;
        ic.unidad = pr.getUnidad()!=null?pr.getUnidad().getTUnidad():"";
        ic.stMin = pr.getStMin();
        ic.stMax = pr.getStMax();
        ic.tipo = pr.getTipo();
        ic.referencia = pr.getReferencia();
        ic.presentacion = pr.getPresentacion();
        ic.color = pr.getColor();
        
     
        return ic;
    }

}
