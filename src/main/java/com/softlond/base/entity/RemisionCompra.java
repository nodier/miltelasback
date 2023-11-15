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
@Table(name ="remision_compra")
public class RemisionCompra implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	

	@ManyToOne
	@JoinColumn(name = "id_sede")
	private Organizacion idSede;
	
	@ManyToOne
	@JoinColumn(name = "id_proveedor")
	private Proveedor idProveedor;
	
	@Column(name = "fecha_creacion")
	private Date fechaRemision;
	
	@Column(name = "numero_remision")
	private String numeroRemision;
	
	@Column(name = "observaciones")
	private String observaciones;
	
	@Column(name = "fecha_modificacion")
	private Date fechaMod;
	
	@ManyToOne
	@JoinColumn(name = "id_creador")
	private Usuario idCreador;
	
	@ManyToOne
	@JoinColumn(name = "id_periodo_contable")
	private PeriodosContables idPeriodoContable;
	
	@Column(name = "cantidad_articulos")
	private Integer cantidadArticulos;
	
	@Column(name = "total")
	private Double total;
	
	@Column(name= "asignado")
	private Integer asignado;
	
	@Column(name="estado_documento")
	private String estadoDocumento;
	
}


