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
@Table(name ="ConfigUnidades")
public class ConfigUnidades implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "tUnidad", length =20)
	private String tUnidad;
	
	@Column(name = "tSimbolo", length =3)
	private String tSimbolo;
	
	@Column(name = "xDefecto")
	private boolean xDefecto;
	
	@Column(name = "xVisible")
	private boolean xVisible;
	
	public ConfigUnidades() {
	}

}
