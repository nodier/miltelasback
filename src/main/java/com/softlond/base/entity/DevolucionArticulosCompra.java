package com.softlond.base.entity;

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
@Table(name ="dev_compras_articulos")
public class DevolucionArticulosCompra {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "nID_ArtDevolucion")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "nID_FCRemArticulo")
	private ArticulosRemisionCompra articuloRemision;
	
	@Column(name = "nCantidadDevuelta")
	private Double cantidad;
	
	@Column(name = "mValorDevuelto")
	private Double valorDevuelto;
	
	@Column(name = "mIVADevuelto")
	private Double ivaDevuelto;
	
	@ManyToOne
	@JoinColumn(name = "nID_DevCompra")
	private DevolucionCompras devolucionCompras;
}
