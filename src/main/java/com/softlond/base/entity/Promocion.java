package com.softlond.base.entity;

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
@Table(name = "prd_promociones")
public class Promocion {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "nid_promocion")
	private Integer id;

	private String nombre;

	@ManyToOne
	private PrdTipos tipo;

	@ManyToOne
	private PrdReferencia referencia;

	@ManyToOne
	private PrdPresentacion presentacion;

	private Float descuento;

	private boolean lunes;
	private boolean martes;
	private boolean miercoles;
	private boolean jueves;
	private boolean viernes;
	private boolean sabado;
	private boolean domingo;

	private Date fechaInicio;
	private Date fechaFin;

	private boolean activo;

	@Column(name = "id_sede")
	private Integer sede;

}
