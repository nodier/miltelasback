package com.softlond.base.request;

import java.util.ArrayList;
import java.util.List;

import com.softlond.base.entity.ArticulosRemisionCompra;
import com.softlond.base.entity.Entrada;
import com.softlond.base.entity.RemisionCompra;

import lombok.Data;

@Data
public class EntradaRequest {

	Entrada entrada;
	List<ArticulosRemisionCompra> articulosRemision = new ArrayList<ArticulosRemisionCompra>();
}
