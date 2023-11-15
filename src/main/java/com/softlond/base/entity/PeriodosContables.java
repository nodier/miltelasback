package com.softlond.base.entity;

import java.io.Serializable;
import java.util.ArrayList;
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
@Table(name = "pyme_con_periodos_contables")
public class PeriodosContables {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "id_sede")
	private Organizacion idSede;
	
	@Column(name = "cod_periodoContable", length = 8)
	private String codPeriodoContable;
	
	@Column(name = "ano" , length = 4)
	private String ano;
	
	@Column(name = "mes" , length = 2)
	private String mes;
	
	@Column(name = "fecha_apertura")
	private Date fechaApertura;
	
	@Column(name = "fecha_cierre")
	private Date fechaCierre;
	
	@Column(name = "activo")
	private Integer activo;



}
