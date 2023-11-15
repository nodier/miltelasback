package com.softlond.base.entity;

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
@Table(name = "config_rcb_formas_pago")
public class FormasPago {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "formaPago", length = 50)
	private String formaPago;
	
	@Column(name = "defecto")
	private Integer defecto;
	
	@Column(name = "cuentaAux", length = 20)
	private String cuentaAux;

}
