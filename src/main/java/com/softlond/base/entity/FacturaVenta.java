package com.softlond.base.entity;

import java.util.Date;
import java.util.List;

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
@Table(name ="FC_FacturaVentasRemisionesVenta")
public class FacturaVenta {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "nID_FCFacturaRemision")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "nID_Factura")
	private Factura facturaVenta;
	
	@ManyToOne
	@JoinColumn(name = "nID_FCRemision")
	private RemisionVenta Remision;
	
}
