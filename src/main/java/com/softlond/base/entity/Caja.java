package com.softlond.base.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
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
@Table(name ="cajas")
public class Caja implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "nombre", length = 30)
	private String nombre;
	
	@Column(name = "descripcion",length = 100)
	private String descripcion;
	
	@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
	@ManyToOne
	private Organizacion sede;
	
	public Caja() {
		
	}
	
}
