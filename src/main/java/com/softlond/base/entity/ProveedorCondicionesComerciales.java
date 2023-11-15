package com.softlond.base.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "Ter_ProveedorCondicionesPago")
public class ProveedorCondicionesComerciales implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "nID_CondicionPago")
	private Integer id;
	
	@OneToOne
	@JoinColumn(name = "nID_Provedor")
	private Proveedor proveedor;
	
	@Column(name = "nNroDias")
	private Integer dias;
	
	@Column(name = "nDescuento")
	private Double descuento;
	
	@Column(name = "tObservaciones")
	private String observaciones;
}
