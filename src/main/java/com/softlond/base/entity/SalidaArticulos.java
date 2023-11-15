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
@Table(name = "Salida_articulos")
public class SalidaArticulos {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_salida_articulo")
	Integer id;

	@ManyToOne
	@JoinColumn(name = "id_articulo")
	private Articulo articulo;

	@Column(name = "nCantidad")
	private float cantidad;

	@Column(name = "costo")
	private Integer costo;

	@Column(name = "costoUnitario")
	private Integer costoUnitario;
}
