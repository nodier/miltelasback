package com.softlond.base.entity;

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
@Table(name ="documentos_cuenta")
public class DocumentosCuentas {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "id_sede")
	private Organizacion idSede;
	
	@Column(name = "codigo_documento", length = 5)
	private String codigoDocumento;
	
	@Column(name = "tipo", length = 5)
	private String tipo;
	
	@Column(name = "cuenta", length = 20)
	private String cuenta;
	
	@Column(name = "nombre_cuenta", length = 100)
	private String nombreCuenta;
	
	@Column(name = "descripcion", length = 500)
	private String descripcion;
	
	@Column(name = "activo", length = 2)
	private Integer activo;
		

}
