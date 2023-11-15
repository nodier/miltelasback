package com.softlond.base.entity;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@Entity
@Table(name ="organizacion")
public class Organizacion implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "nombre", length = 50)
	private String nombre;
	
	@Column(name = "id_unico", length = 50)
	private String idUnico;
	
	@ManyToOne
	@JoinColumn(name = "id_creador")
	private Usuario idCreador;
	
	@ManyToOne
	@JoinColumn(name = "id_ultimo_usuario_modifico")
	private Usuario idUltimoUsuarioModifico;
	
	@Column(name = "fecha_creacion")
	private Date fechaCreacion;
	
	@Column(name = "fecha_actualizacion")
	private Date fechaActualizacion;
	
	@ManyToOne
	@JoinColumn(name = "id_ciudad")
	private Ciudad idCiudad;
	
	@ManyToOne
	@JoinColumn(name = "id_departamento")
	private Departamento idDepartamento;
	
	@Column(name = "documento", length = 50)
	private String documento;
	
	@Column(name = "direccion", length = 50)
	private String direccion;
	
	@Column(name = "telefono", length = 10)
	private String telefono;
	
	@ManyToOne
	@JoinColumn(name = "id_tipo_documento")
	private MaestroValor idTipoDocumento;
	
	@JsonIgnoreProperties({"sede","hibernateLazyInitializer","handler"})
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "sede")
	private List<Caja> cajas;
	
	@JsonIgnoreProperties(value = {"sede"}, allowSetters = true)
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "sede", cascade = CascadeType.ALL)
	private List<Vendedor> vendedores;
	
	@JsonIgnoreProperties({"sede","hibernateLazyInitializer","handler"})
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "sede", cascade = CascadeType.ALL)
	private List<Articulo> articulos;
	
	public Organizacion() {
		cajas = new ArrayList<Caja>();
		vendedores = new ArrayList<Vendedor>();
		articulos = new ArrayList<Articulo>();
	}
	
		
}