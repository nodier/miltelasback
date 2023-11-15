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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "PrdDescuentos")
public class PrdDescuentos implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "id_clasificacion")
	private ConfigTerClasificaciones idClasificacion;

	@ManyToOne()
	@JoinColumn(name = "tipo")
	private PrdTipos tipo;

	@ManyToOne()
	@JoinColumn(name = "referencia")
	private PrdReferencia referencia;

	@ManyToOne()
	@JoinColumn(name = "id_presentacion")
	private PrdPresentacion idPresentacion;

	@Column(name = "descuento")
	private Float descuento;

	@Column(name = "estado")
	private Boolean estado;

	@Column(name = "fechaCreacion")
	private Date fechaCreacion;

	@Column(name = "fechaModificacion")
	private Date fechaModificacion;

	public PrdDescuentos() {
	}
}
