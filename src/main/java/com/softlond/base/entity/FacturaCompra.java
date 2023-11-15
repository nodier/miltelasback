package com.softlond.base.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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

import lombok.Data;

@Data
@Entity
@Table(name ="FC_FacturaCompra")
public class FacturaCompra implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "nID_FacturaCompra")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "id_sede")
	private Organizacion idSede;
	
	@ManyToOne
	@JoinColumn(name = "nID_PeriodoContable")
	private PeriodosContables idPeriodoContable;
	
	@ManyToOne
	@JoinColumn(name = "nID_PymeTipoVenta")
	private MaestroValor idTipoVenta;
	
	@ManyToOne
	@JoinColumn(name = "nID_Proveedor")
	private Proveedor proveedor;
	
	@Column(name = "dFechaFactura")
	private Date fechaFactura;
	
	@Column(name = "tNroFactura")
	private String nroFactura;
	
	@ManyToOne
	@JoinColumn(name = "ID_Moneda")
	private ConfigMonedas idMoneda;
	
	@Column(name = "mSubtotalCompras")
	private Double subTotalCompra;
	
	@Column(name = "mAjustes")
	private Float ajustes;
	
	@Column(name = "mIVA")
	private Double iva;
	
	@Column(name = "mAdicionales")
	private Integer valoresAdicionales;
	
	@Column(name = "bOtrosImpuestos")
	private Integer otrosImpuestos;
	
	@Column(name = "bRetenciones")
	private Integer retenciones;
	
	@Column(name = "mTotal")
	private Integer total;
	
	@Column(name = "Concepto")
	private String concepto;
	
	@Column(name = "dFechaVencimiento")
	private Date fechaVencimiento;
	
	@ManyToOne
	@JoinColumn(name = "nPlazo")
	private PlazoCredito idPlazo;
	
	@Column(name = "tObservaciones")
	private String observaciones;
	
	@Column(name = "Cod_EstadoMon")
	private String codEstadoMon;
	
	@ManyToOne
	@JoinColumn(name = "Cod_EstadoCon")
	private EstadoDocumento codEstadoCon;
	
	@Column(name = "bFechaMod")
	private Date fechaMod;
	
	@ManyToOne
	@JoinColumn(name = "UsuarioMod")
	private InformacionUsuario usuarioMod;
	
	@ManyToOne
	@JoinColumn(name = "IDUsuarioAutorizacion")
	private InformacionUsuario idUsuarioAutorizacion;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "nID_FacturaCompra")
	private List<FacturaImpuestos> listOtrosImpuestos;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "nID_FacturaCompra")
	private List<FacturaRetenciones> listRetenciones;

	public FacturaCompra() {
		this.listOtrosImpuestos = new ArrayList<FacturaImpuestos>();
		this.listRetenciones = new ArrayList<FacturaRetenciones>();
	}
	
	
}


