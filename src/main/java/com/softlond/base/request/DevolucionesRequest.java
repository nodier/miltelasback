package com.softlond.base.request;

import java.util.List;

import com.softlond.base.entity.DevolucionCompras;
import com.softlond.base.entity.DevolucionVentasCliente;
import com.softlond.base.entity.FacturaCompra;
import com.softlond.base.entity.FacturaRemision;
import com.softlond.base.entity.Organizacion;
import com.softlond.base.entity.Prefijo;

import lombok.Data;

@Data
public class DevolucionesRequest {

	private DevolucionVentasCliente devolucion;
	private Prefijo prefijo;
	
	
	public DevolucionesRequest(DevolucionVentasCliente devolucion, Prefijo prefijo) {
		this.devolucion = devolucion;
		this.prefijo = prefijo;
	}
	
	
}
