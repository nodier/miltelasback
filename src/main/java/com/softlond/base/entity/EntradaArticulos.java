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
@Table(name = "Entrada_articulos")
public class EntradaArticulos {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_entrada_articulo")
	Integer id;

	@ManyToOne
	@JoinColumn(name = "id_articulo")
	private Articulo articulo;

	@Column(name = "nCantidad")
	private Float cantidad;

	@Column(name = "costo")
	private Float costo;

	@Column(name = "costoUnitario")
	private Float costoUnitario;
}
