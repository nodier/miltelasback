package com.softlond.base.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "Entrada")
public class Entrada {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_entrada_mercancia")
	Integer id;

	@ManyToOne()
	@JoinColumn(name = "id_sede")
	private Organizacion sede;

	@ManyToOne()
	@JoinColumn(name = "id_prefijo")
	private Prefijo prefijo;

	@ManyToOne()
	@JoinColumn(name = "id_periodo_contable")
	private PeriodosContables periodosContables;

	private Date fechaDocumento;

	private Integer numeroDocumento;

	private String concepto;

	@ManyToOne
	@JoinColumn(name = "Cod_Estado")
	private EstadoDocumento estado;

	@ManyToOne
	@JoinColumn(name = "IDUsuarioAutorizacion")
	private InformacionUsuario usuario;

	@Column(name = "mTotal")
	private Float total;

	// @ManyToOne()
	// @JoinColumn(name = "id_remision")
	// private RemisionCompra remision;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "nid_entrada")
	private List<EntradaArticulos> entradas;

}
