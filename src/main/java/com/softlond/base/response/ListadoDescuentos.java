package com.softlond.base.response;

import com.softlond.base.entity.Clientes;
import com.softlond.base.entity.FacturaCompra;
import com.softlond.base.entity.NotaCredito;
import com.softlond.base.entity.NotaDebito;
import com.softlond.base.entity.PrdDescuentos;
import com.softlond.base.entity.Producto;

import java.util.Date;

public class ListadoDescuentos {
    String empresa;
    String documento;
    String cliente;
    String referencia;
    String tipo;
    String producto;
    Float descuento;
    
    public ListadoDescuentos() {}


    public static ListadoDescuentos convertirProducto(Producto pr) {
        ListadoDescuentos ic = new ListadoDescuentos();
    
        ic.referencia = pr.getReferencia().getTreferencia();
        ic.tipo = pr.getTipo().getTTipo();
        ic.producto = pr.getProducto();
        ic.descuento = pr.getDescuento().getDescuento();
      
        return ic;
    }

    public static ListadoDescuentos convertirCliente(Clientes cl) {
        ListadoDescuentos ic = new ListadoDescuentos();
        
        ic.cliente = cl.getNombres() + "-" + cl.getApellidos();
        ic.documento = cl.getNitocc() + "-" + cl.getDigito();
        
        return ic;
    }

    
    public String getCliente() {
        return cliente;
    }
    
    public String getEmpresa() {
        return empresa;
    }
    
    public String getDocumento() {
        return documento;
    }
    
    public String getReferencia() {
        return referencia;
    }
    
    public String getTipo() {
        return tipo;
    }

    public String getproducto() {
        return producto;
    }
    
    public Float getDescuento() {
        return descuento;
    }

}
