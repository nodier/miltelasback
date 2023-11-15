package com.softlond.base.entity;

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

import org.springframework.data.annotation.Transient;

import lombok.Data;

@Data
@Entity
@Table(name ="dev_compras")
public class DevolucionCompras {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "nID_DevCompra")
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
	
	@ManyToOne
	@JoinColumn(name = "nID_Factura")
	private FacturaCompra factura;
	
	@Column(name = "dFechaFactura")
	private Date fechaFactura;
	
	@Column(name = "nNroDocumento")
	private String nroDevolucion;
	
	@Column(name = "mSubTotal")
	private Double subTotal;
	
	@Column(name = "mIVA")
	private Double iva;
	
	@Column(name = "mReteFuente")
	private Double retencion;
	
	@Column(name = "mTotal")
	private Integer total;
	
	@Column(name = "tObservaciones")
	private String observaciones;
	
	@ManyToOne
	@JoinColumn(name = "Cod_EstadoCon")
	private EstadoDocumento codEstadoCon;
	
	@Column(name = "bFechaMod")
	private Date fechaMod;
	
	@Transient
	private double cantidadTotal;
	
	@ManyToOne
	@JoinColumn(name = "UsuarioMod")
	private InformacionUsuario usuarioMod;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "nID_DevCompra")
	private List<DevolucionArticulosCompra> devolucionArticulos;
	
	@Column(name = "Motivo")
	private String motivo;

	public DevolucionCompras() {
		this.devolucionArticulos = new ArrayList<DevolucionArticulosCompra>();
		
	
	}
}


