package com.softlond.base.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name ="Com_ComprobanteRetencionesCaja")
public class RetencionReciboCaja {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "nID_ComRetencion")
	private Integer id;
	
	@Column(name = "mValor")
	private Integer valor;
	
	@ManyToOne
	@JoinColumn(name = "nID_PymeImpuestoRetenido")
	private Impuesto impuesto;
}
