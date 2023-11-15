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
@Table(name ="Pyme_Cfg_Impuestos")
public class Impuesto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "nID_FacRetencion")
	private Integer id;
	
	@Column(name = "tCod_Impuesto")
	private String codImpuesto;
	
	@Column(name = "tImpuesto")
	private String impuesto;
	
	@Column(name = "nPorcentaje")
	private Double porcentaje;
	
	@Column(name = "nValor")
	private String valor;
	
	@Column(name = "nBase")
	private Double base;
	
	private String cuenta;
	
	
	private Integer enlace;
}



