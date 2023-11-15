package com.softlond.base.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "departamento")
public class Departamento {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "indicativo")
	private String indicativo;
	
	@Column(name = "nombre")
	private String nombre;
	
	public Departamento() {}
	
	public Departamento(Integer id, String indicativo, String nombre ) {
		
		this.id = id;
		this.indicativo = indicativo;
		this.nombre = nombre;
	}
	
}
