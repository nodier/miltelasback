package com.softlond.base.response;

import com.softlond.base.entity.InformacionUsuario;
import com.softlond.base.entity.InveLocal;

import lombok.Data;

@Data
public class Locales {
	Integer id;
	String empresa;
	String tlocal;
	String descripcion;
	Boolean activo;
	InformacionUsuario responsable;
	Boolean permiteVenta;
	Integer sectores;
	
	public Locales() {
	}
	
	public static Locales convertirLocales(InveLocal lo) {
		Locales lc = new Locales();
		lc.id = lo.getId();
		lc.empresa = lo.getSede().getIdUnico();
		lc.tlocal = lo.getTLocal();
		lc.descripcion = lo.getDescripcion();
		lc.activo = lo.isActivo();
		lc.responsable = lo.getResponsable();
		lc.permiteVenta = lo.isPermiteVenta();
		lc.sectores = 0;
		
		return lc;
	}

}
