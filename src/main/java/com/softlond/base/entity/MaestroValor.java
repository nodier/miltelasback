package com.softlond.base.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name ="maestro_valor")
public class MaestroValor {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "nombre", length = 50)
	private String nombre;
	
	@Column(name = "objetivo", length = 50)
	private String objetivo;
	
	@Column(name = "descripcion", length = 200)
	private String descripcion;
	
	@Column(name = "fecha_creacion")
	private Date fechaCreacion;
	
}
