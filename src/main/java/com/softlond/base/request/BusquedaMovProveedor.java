package com.softlond.base.request;

import java.sql.Date;

public class BusquedaMovProveedor {
    Integer idProveedor;
    Date fechaInicial;
    Date fechaFinal;

    public Integer getIdProveedor() {
        return idProveedor;
    }

    public Date getFechaInicial() {
        return fechaInicial;
    }

    public Date getFechaFinal() {
        return fechaFinal;
    }
}
