package com.softlond.base.entity;

import java.io.Serializable;

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
@Table(name ="DevVentas_Ventas_Articulos")
public class DevolucionArticulosVenta implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "nID_ArtDevolucion")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "nID_facturaArticulo")
	private FacturaArticulos articuloRemision;
	
	@Column(name = "nCantidadDevuelta")
	private Double cantidad;
	
	@Column(name = "mValorDevuelto")
	private Double valorDevuelto;
	
	@Column(name = "mIVADevuelto")
	private Double ivaDevuelto;
	
}
