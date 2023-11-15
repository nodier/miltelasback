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
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;


@Data
@Entity
@Table(name = "menu")
public class Menu implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@NotNull
	@Column(name = "nombre")
	private String nombre;

	@Column(name = "ruta")
	private String ruta;

	@Column(name = "titulo", length = 50)
	private String titulo;

	@Column(name = "icono")
	private String icono;

	@Column(name = "clase")
	private String clase;

	@Column(name = "insignia")
	private String insignia;

	@Column(name = "clase_insignia", length = 100)
	private String claseInsignia;

	@JsonIgnore
	@Column(name = "fecha_creacion")
	private Date fechaCreacion;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "id_creador")
	private Usuario idCreador;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "id_ultimo_usuario_modifico")
	private Usuario idUltimoUsuarioModifico;
	
	@JsonIgnore
	@Column(name = "fecha_actualizacion")
	private Date fechaActualizacion;

}
