package com.softlond.base.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name ="Fac_Articulos")
public class FacturaArticulos implements Serializable{
	 
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "nID_FacArticulo")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(columnDefinition="integer", name = "nID_Articulo")
	private Articulo articulo;
	
	@Column(name = "nCantidad")
	private Double cantidad;
	
	@Column(name = "mPrecioUnitario")
	private Double precioUnitario;  //cambio
	
	@Column(name = "mPrecio") //cambio
	private Double precio;
	
	@Column(name = "nPorcentajeIVA")
	private Float porcentajeIva;
	
	@Column(name = "nPorcentajeDescuento")
	private Double porcentajeDescuento;
	
	@Column(name = "mSubTotal")
	private Double subtotal;  //cambio
}
