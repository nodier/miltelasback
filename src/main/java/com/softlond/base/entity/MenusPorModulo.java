package com.softlond.base.entity;

import java.io.Serializable;
import java.sql.Date;

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
@Table(name = "menus_por_modulo")
public class MenusPorModulo implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@OneToOne
	@JoinColumn(name = "id_modulo")
	private Modulo idModulo;

	@OneToOne
	@JoinColumn(name = "id_menu")
	private Menu idMenu;

	@Column(name = "fecha_creacion")
	private Date fechaCreacion;

	@Column(name = "indice")
	private Integer indice;

}
