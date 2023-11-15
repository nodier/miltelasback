package com.softlond.base.entity;

import java.io.Serializable;
import java.sql.Date;

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
@Table(name = "nota_credito_cliente")
public class NotaCreditoCliente implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "id_sede")
	private Organizacion idSede;
	
	@ManyToOne
	@JoinColumn(name = "id_cliente")
	private Clientes idCliente;
	
	@Column(name = "numero_documento")
	private String numeroDocumento;
	
	@Column(name = "fecha_documento")
	private Date fechaDocumento;
	
	@ManyToOne
	@JoinColumn(name = "id_periodo_contable")
	private PeriodosContables idPeriodoContable;
	
	@Column(name = "observaciones")
	private String observaciones;
	
	@Column(name = "total")
	private Integer total;
	
	@Column(name = "asignado")
	private Integer asignado;
	
	@Column(name = "estado_documento")
	private String estadoDocumento;
	
	@Column(name = "fecha_modificacion")
	private Date fechaMod;
	
	@ManyToOne
	@JoinColumn(name = "id_creador")
	private Usuario idCreador;
	
	/*@ManyToOne
	@JoinColumn(name = "prefijo")
	private Prefijo prefijo;*/
	
	@ManyToOne
	@JoinColumn(name = "prefijo")
	private Prefijo prefijo;
	
}

