package com.softlond.base.entity;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@Entity
@Table(name ="Fac_SedeVendedores")
public class Vendedor implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@JsonIgnoreProperties(value = {"vendedores"}, allowSetters = true)
	@ManyToOne(fetch = FetchType.LAZY)
	private Organizacion sede;
	
	@Column(name = "NIToCC")
	private String nit;
	
	@Column(name = "NombreCompleto")
	private String nombre;
	
	@Column(name = "Activo")
	private boolean activo;
	
	@Column(name = "FechaMod")
	private Date fechaModificacion;
}
