package com.softlond.base.response;

import java.util.Date;
import com.softlond.base.entity.Clientes;
import lombok.Data;

@Data
public class Cartera {

    String empresa;
    String tipo;
    String concepto;
    String documento;
    Clientes cliente;
    String direccion;
    String telefono;
    String email;
    Integer diasVencidos;
    Integer saldo;
    Date ultimoPago;

    public Cartera() {
    }
    // Factura, Recibo, Nota crédito y débito, egresos, recibos

    public static Cartera convertirCartera(Clientes c, Date fecha) {
        Cartera ca = new Cartera();
        ca.tipo = "informe cartera";
        ca.concepto = "Total cartera";
        ca.documento = c.getNitocc();
        ca.cliente = c;
        ca.direccion = c.getDireccion();
        ca.telefono = c.getTelefono();
        ca.email = c.getEmail();
        // ca.diasVencidos = 0;
        ca.diasVencidos = diasTranscurridos(fecha);
        ca.saldo = 0;
        ca.ultimoPago = fecha;
        return ca;
    }

    private static int diasTranscurridos(Date fecha) {
        long tiempoActual = new java.util.Date().getTime();
        long tiempoFecha = fecha != null ? fecha.getTime() : new Date().getTime();
        long diff = tiempoFecha - tiempoActual;
        return (int) (diff / (1000 * 60 * 60 * 24));
    }

    public String getEmpresa() {
        return empresa;
    }

    public String getTipo() {
        return tipo;
    }

    public String getConepto() {
        return concepto;
    }

    public String getDocumento() {
        return documento;
    }

    public Clientes getCliente() {
        return cliente;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getTeleofno() {
        return telefono;
    }

    public String getEmail() {
        return email;
    }

    public Integer getDiasVencidos() {
        return diasVencidos;
    }

    public Integer getSaldo() {
        return saldo;
    }

    public Date getUltimoPago() {
        return ultimoPago;
    }

}
