package com.softlond.base.response;

import java.util.ArrayList;
import java.util.List;

public class Paginacion {
    List<Object> content;
    int totalElements;

    public Paginacion() {
        super();
    }

    public Paginacion(List content, int totalElements) {
        this.content = content;
        this.totalElements = totalElements;
    }

    public static Paginacion paginar(List lista, int itemsPorPagina, int pagina) {
        int offset = pagina * itemsPorPagina;
        int limite = Math.min(lista.size(), offset + itemsPorPagina);
        if (lista.size() < offset) {
            return new Paginacion(new ArrayList<>(), lista.size());
        }
        return new Paginacion(lista.subList(offset, limite), lista.size());
    }

    public List<Object> getContent() {
        return content;
    }

    public int getTotalElements() {
        return totalElements;
    }
}
