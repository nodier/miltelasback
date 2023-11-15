package com.softlond.base.entity;


import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Data
@Entity
@Table(name = "prefijo")
public class Prefijo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "prefijo")
	private String prefijo;
	
	@Column(name = "tipo_prefijo")
	private String tipoPrefijo;
	
	@ManyToOne
	@JoinColumn(name = "id_creador")
	private Usuario idCreador;
	
	@Column(name = "fecha_resolucion")
	@Temporal(TemporalType.DATE)
	private Date fechaResolucion;
	
	@Column(name = "resolucion")
	private String resolucion;
		
	@Column(name = "fecha_inicial")
	@Temporal(TemporalType.DATE)
	private Date fechaInicial;
	
	@Column(name = "fecha_final")
	@Temporal(TemporalType.DATE)
	private Date fechaFinal;
	
	@Column(name = "valor_inicial")
	private Integer valorInicial;
	
	@Column(name = "valor_final")
	private Integer valorFinal;
	
	@Column(name = "actual")
	private Integer actual;
	
	@ManyToOne
	@JoinColumn(name = "id_sede")
	private Organizacion idSede;
	
	@Column(name = "estado")
	private String estado;
	
	@Column(name = "fecha_creacion")
	private Date fechaCreacion;
	
	@Column(name = "tipo_documento")
	private String tipoDocumento;
	
	@ManyToOne
	@JoinColumn(name = "id_tipo_venta")
	private MaestroValor idTipoVenta;

	@Column(name = "observacion")
	private String observacion;
}
