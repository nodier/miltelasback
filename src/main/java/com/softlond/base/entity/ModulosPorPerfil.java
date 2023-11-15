package com.softlond.base.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "modulos_por_perfil")
public class ModulosPorPerfil implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@OneToOne
	@JoinColumn(name = "id_perfil")
	private Perfil idPerfil;

	@OneToOne
	@JoinColumn(name = "id_modulo")
	private Modulo idModulo;

	@Column(name = "fecha_creacion")
	private Date fechaCreacion;
	
	@Column(name = "indice")
	private Integer indice;

}
