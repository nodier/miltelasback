package com.softlond.base.request;

import java.util.ArrayList;
import java.util.List;

import com.softlond.base.entity.FacturaCompra;
import com.softlond.base.entity.FacturaRemision;

import lombok.Data;

@Data
public class FacturaCompraRemisionRequest {

	private FacturaCompra facturaCompra;
	private List<FacturaRemision> facturaRemision;
	private ArrayList<String[]> listContable;

	public FacturaCompraRemisionRequest(FacturaCompra facturaCompra, List<FacturaRemision> facturaRemision) {
		super();
		this.facturaCompra = facturaCompra;
		this.facturaRemision = facturaRemision;
	}

}
