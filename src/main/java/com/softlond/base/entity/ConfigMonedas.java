package com.softlond.base.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@Entity
@Table(name ="config_monedas")
public class ConfigMonedas {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "moneda", length = 20)
	private String moneda;
	
	@Column(name = "descripcion", length = 255)
	private String descripcion;
	
	@Column(name = "simbolo", length = 3)
	private String simbolo;
	
	@Column(name = "defecto")
	private Integer defecto;
	
	@Column(name = "visible")
	private Integer visible;
	
	
	
	
	
}
