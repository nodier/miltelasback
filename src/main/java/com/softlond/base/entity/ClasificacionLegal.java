package com.softlond.base.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "Config_Cfg_ClasificacionLegal")
public class ClasificacionLegal {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "tCod_ClasificacionLegal")
	private Integer id;
	
	@Column(name = "tClasificacionLegal")
	private String clasificacionLegal;
	
	@Column(name = "tObservaciones")
	private String observaciones;
	
	@Column(name = "xDefecto")
	private Boolean xDefecto;
	
	@Column(name = "bVisible")
	private Boolean visible;
}
