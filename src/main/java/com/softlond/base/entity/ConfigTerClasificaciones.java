package com.softlond.base.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "config_ter_clasificaciones")
public class ConfigTerClasificaciones implements Serializable{

private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "clasificacion", length = 50)
	private String clasificacion;
	
	@Column(name = "descripcion", length = 255)
	private String descripcion;
	
	@Column(name = "valor")
	private Float valor;
}
