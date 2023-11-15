package com.softlond.base.entity;

import java.sql.Date;

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
@Table(name ="caja_cierre")
public class CajaCierre {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "id_caja")
	private Caja idCaja;
	
	@ManyToOne
	@JoinColumn(name = "id_periodo_contable")
	private PeriodosContables idPeriodoContable;
	
	@Column(name = "tipo_cierre", length = 1)
	private String tipoCierre;
	
	@Column(name = "fecha_cierre")
	private Date fechaCierre;
	
	@Column(name = "fechaMod")
	private Date fechaMod;
	
	@ManyToOne
	@JoinColumn(name = "id_creador")
	private Usuario idCreador;

}
