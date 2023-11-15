package com.softlond.base.entity;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@Entity
@Table(name = "Traslado")
public class Traslado {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_traslado")
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

	@ManyToOne
	@JoinColumn(name = "id_local_inicial")
	private InveLocal localInicial;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "nid_movimiento")
	private List<ArticuloMovimientos> movimientos;

	// @JsonIgnoreProperties({ "Traslado", "hibernateLazyInitializer", "handler" })
	// @OneToMany(mappedBy = "Traslado")
	// private List<ArticuloMovimientos> movimientos;

	public Traslado() {
		movimientos = new ArrayList<ArticuloMovimientos>();
	}
}
