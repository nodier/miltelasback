package com.softlond.base.entity;

import java.io.Serializable;
import java.util.Date;

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
@Table(name = "perfil")
public class Perfil implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
		
	@Column(name = "nombre", length = 50)
	private String nombre;
	
	@Column (name= "descripcion", length = 100)
	private String descripcion;
	
	@Column(name = "fecha_creacion")
	private Date fechaCreacion;

	@Column(name = "fecha_actualizacion")
	private Date fechaActualizacion;
	
	@ManyToOne
	@JoinColumn(name = "id_creador")
	private Usuario idCreador;
		
	@ManyToOne
	@JoinColumn(name = "id_ultimo_usuario_modifico")
	private Usuario idUltimoUsuarioModifico;

	
	public Perfil(Integer id, String nombre, String descripcion, Date fechaCreacion, Date fechaActualizacion, Usuario idCreador, Usuario idUltimoUsuarioModifico) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.fechaCreacion = fechaCreacion;
		this.fechaActualizacion = fechaActualizacion;
		this.idCreador = idCreador;
		this.idUltimoUsuarioModifico = idUltimoUsuarioModifico;
	}

	public Perfil() {
		super();
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
	