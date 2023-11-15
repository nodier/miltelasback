package com.softlond.base.entity;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "Cuenta")
public class Cuenta implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	
	@ManyToOne(optional = true, cascade = CascadeType.ALL)
	private Organizacion sede;
		
	private String nombre;
	
	private String numeroCuenta;
	
	private String tipoCuenta;
	
	private String tipoDocumento;
	
	private String observaciones;
	
	private String cta;
}
