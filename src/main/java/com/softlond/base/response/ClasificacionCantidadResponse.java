package com.softlond.base.response;

import com.softlond.base.entity.PrdClasificaciones;

import lombok.Data;

@Data
public class ClasificacionCantidadResponse {

	PrdClasificaciones clasificacion;
	Integer cantidad;
}
