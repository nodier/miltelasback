package com.softlond.base.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "articulo_movimientos")
public class ArticuloMovimientos {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_movimiento")
	Integer id;

	@ManyToOne
	@JoinColumn(name = "id_articulo")
	private Articulo articulo;

	@ManyToOne
	@JoinColumn(name = "id_sector_origen")
	private InvSector sectorOrigen;

	@ManyToOne
	@JoinColumn(name = "id_sector_destino")
	private InvSector sectorDestino;

	@Column(name = "nCantidad")
	private Float cantidad;

	@Column(name = "dFechaMovimiento")
	private Date fechaMovimiento;

	// @ManyToOne()
	// @JoinColumn(name = "id_traslado")
	// private Traslado traslado;
}
