package com.softlond.base.request;

import java.sql.Date;

public class BusquedaMovCliente {
    Integer idCliente;
    Date fechaInicial;
    Date fechaFinal;

    public Integer getIdCliente() {
        return idCliente;
    }

    public Date getFechaInicial() {
        return fechaInicial;
    }

    public Date getFechaFinal() {
        return fechaFinal;
    }
}
