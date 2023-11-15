package com.softlond.base.entity;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@Entity
@Table(name ="Fac_FacturaRetenciones")
public class FacturaRetenciones implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "nID_FacRetencion")
	private Integer id;
	
	@Column(name = "mValor")
	private Integer valor;
	
	@ManyToOne
	@JoinColumn(name = "nID_PymeImpuestoRetenido")
	private Impuesto impuesto;
	
	private String cuenta;
	
	private boolean realizadaEnComprobante;
	
	
}
