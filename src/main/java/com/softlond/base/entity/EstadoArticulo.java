package com.softlond.base.entity;

import java.io.Serializable;
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
@Table(name = "prd_estados_articulo")
public class EstadoArticulo{
	/**
	 * 
	 */	

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nID_EstadoArticulo")
    private Integer id;
	
	@Column(name = "tEstado")
    private String estado;

    @Column(name = "xDefecto")
    private Integer defecto;
}
