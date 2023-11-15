package com.softlond.base.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
// import java.sql.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softlond.base.repository.UsuarioInformacionDao;

import lombok.Data;

@Data
@Entity
@Table(name = "Fac_Facturas")
public class Factura implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "nID_Factura")
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "id_sede")
	private Organizacion idSede;

	@ManyToOne
	@JoinColumn(name = "nID_Caja")
	private Caja cajaId;

	@ManyToOne
	@JoinColumn(name = "nID_PymeTipoVenta")
	private MaestroValor idTipoVenta;

	@ManyToOne
	@JoinColumn(name = "tPrefijo")
	private Prefijo prefijo;

	@Column(name = "nNroFactura", unique = true)
	private Integer nroFactura;

	@Column(name = "dFechaVenta")
	private Date fechaVenta;

	@ManyToOne
	@JoinColumn(name = "nID_PeriodoContable")
	private PeriodosContables idPeriodoContable;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "nID_Cliente")
	private Clientes idCliente;

	@Column(name = "tDetalleCliente")
	private String detalleCliente;

	@ManyToOne
	@JoinColumn(name = "ID_Moneda")
	private ConfigMonedas idMoneda;

	/*
	 * @ManyToOne
	 * 
	 * @JoinColumn(name = "nID_Vendedor")
	 * private Vendedor idVendedor;
	 */

	@ManyToOne
	@JoinColumn(name = "nID_Vendedor")
	private InformacionUsuario idVendedor;

	@Column(name = "mSubtotalVentas")
	private Double subTotalVenta;

	@Column(name = "mAjustes")
	private Integer ajustes;

	@Column(name = "mIVA")
	private Double iva;

	@Column(name = "bValoresAdicionales")
	private Integer valoresAdicionales;

	@Column(name = "bOtrosImpuestos")
	private Integer otrosImpuestos;

	@Column(name = "bRetenciones")
	private Integer retenciones;

	@Column(name = "mTotal")
	private Integer total;

	@Column(name = "tObservaciones")
	private String observaciones;

	@ManyToOne
	@JoinColumn(name = "nID_Plazo")
	private PlazoCredito idPlazo;

	@Column(name = "dFechaVencimientoCr")
	private Date fechaVencimientoCr;

	@Column(name = "nPuntos")
	private Integer npuntos;

	@Column(name = "bSeparado")
	private Boolean separado;

	@Column(name = "bFechaMod")
	private Date fechaMod;

	@ManyToOne
	@JoinColumn(name = "UsuarioMod")
	private Usuario usuarioMod;

	@ManyToOne
	@JoinColumn(name = "IDUsuarioAutorizacion")
	private Usuario idUsuarioAutorizacion;

	@Column(name = "Cod_EstadoMon")
	private String codEstadoMon;

	@ManyToOne
	@JoinColumn(name = "Cod_EstadoCon")
	private EstadoDocumento codEstadoCon;

	@OneToOne
	@JoinColumn(name = "id_Concepto")
	private ConceptosReciboCaja idConcepto;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
	@JoinColumn(name = "nID_Factura")
	private List<FacturaArticulos> facArticulos;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_ReciboCaja")
	private ReciboCajaVenta idReciboCaja;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_asignacion")
	private AsignacionRecibo asignacion;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "nID_Factura")
	private List<RetencionFactura> listRetenciones;

	private String remision;
}
