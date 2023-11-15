package com.softlond.base.request;

import java.util.ArrayList;

import com.softlond.base.entity.ComprobanteEgreso;

import lombok.Data;

@Data
public class ComprobanteEgresoRequest {

	private Integer idPrefijo;
	private ComprobanteEgreso comprobante;
	private ArrayList<String[]> listContable;

	public ComprobanteEgresoRequest() {
	}

}
