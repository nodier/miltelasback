package com.softlond.base.entity;

import java.io.Serializable;
import java.sql.Date;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity
@Table(name = "rcb_rbocajaventa")
public class ReciboCajaVenta implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "id_sede")
	private Organizacion idSede;

	@ManyToOne
	@JoinColumn(name = "id_caja")
	private Caja idCaja;

	@Column(name = "prefijo", length = 5)
	private String prefijo;

	@Column(name = "cod_rbocajaventa", length = 20)
	private String codRbocajaventa;

	@ManyToOne
	@JoinColumn(name = "id_periodo_contable")
	private PeriodosContables idPeriodoContable;

	@Column(name = "fecha_pago")
	private Date fechaPago;

	@ManyToOne
	@JoinColumn(name = "id_cliente")
	private Clientes idCliente;

	@ManyToOne
	@JoinColumn(name = "id_moneda")
	private ConfigMonedas idMoneda;

	@Column(name = "total_recibo")
	private Integer totalRecibo;

	@Column(name = "saldo")
	private Integer saldo;

	@Column(name = "nro_facPendientes")
	private Integer nroFacPendientes;

	@Column(name = "observaciones", length = 500)
	private String observaciones;

	@Column(name = "cod_estadoMon", length = 3)
	private String codEstadoMon;

	@Column(name = "cambio")
	private Double cambio;

	@Column(name = "fechaMod")
	private Date fechaMod;

	@ManyToOne
	@JoinColumn(name = "id_creador")
	private Usuario idCreador;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_recibo_caja")
	private List<PagosReciboCaja> idPagos;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "id_recibo_caja")
	private List<ConceptosReciboCaja> conceptos;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "nID_ReciboCaja")
	private List<RetencionReciboCaja> listRetenciones;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_recibo")
	private AsignacionRecibo asignacion;

}
