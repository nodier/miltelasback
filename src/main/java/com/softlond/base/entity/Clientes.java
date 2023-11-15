package com.softlond.base.entity;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "clientes")
public class Clientes implements Serializable{

private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@OneToOne
	@JoinColumn(name = "cod_clasificacion_legal")
	private ClasificacionLegal codClasificacionLegal;
	
	
	@Column(name = "nitocc", length = 20)
	private String nitocc;
	
	@Column(name = "digito", length = 2)
	private String digito;
	
	@ManyToOne
	@JoinColumn(name = "id_tipo_documento")
	private MaestroValor idTipoDocumento;
	
	@Column(name = "nombres", length = 100)
	private String nombres;
	
	@Column(name = "apellidos", length = 100)
	private String apellidos;
	
	@Column(name = "cupo_credito")
	private Float cupoCredito;
	
	@Column(name = "direccion", length = 255)
	private String direccion;
	
	@ManyToOne
	@JoinColumn(name = "id_ciudad")
	private Ciudad idCiudad;
	
	@ManyToOne
	@JoinColumn(name = "id_departamento")
	private Departamento idDepartamento;
	
	@Column(name = "contacto", length = 100)
	private String contacto;
	
	@Column(name = "cargo_contacto", length = 100)
	private String cargoContacto;
	
	@Column(name = "telefono", length = 100)
	private String telefono;
	
	@Column(name = "fax", length = 100)
	private String fax;
	
	@Column(name = "email", length = 100)
	private String email;
	
	@Column(name = "web_site", length = 100)
	private String webSite;
	
	@Column(name = "observaciones", length = 500)
	private String observaciones;
	
	@ManyToOne
	@JoinColumn(name = "id_clasificacion")
	private ConfigTerClasificaciones idClasificacion;
	
	@Column(name = "fecha_nacimiento")
	private Date fechaNacimiento;
	
	@Column(name = "activo")
	private Boolean activo;
	
	@Column(name = "fecha_actualizacion")
	private Date fechaActualizacion;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_creador")
	private Usuario idCreador;
	
	
	@OneToOne
	@JoinColumn(name = "barrio")
	private Barrio barrio;
	
	@ManyToOne
	@JoinColumn(name = "id_retenion")
	private Retencion idRetencion;
	
}
