package com.softlond.base.entity;

import java.io.Serializable;
import java.sql.Date;
// import java.util.Date;
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
@Table(name = "remision_venta")
public class RemisionVenta implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "numero_remision")
	private String numeroRemision;

	@ManyToOne()
	@JoinColumn(name = "nID_Cliente")
	private Clientes idCliente;

	/*
	 * @ManyToOne
	 * 
	 * @JoinColumn(name = "nID_Vendedor")
	 * private Vendedor idVendedor;
	 */

	@ManyToOne
	@JoinColumn(name = "nID_Vendedor")
	private InformacionUsuario idVendedor;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "nID_Remision")
	private List<FacturaArticulos> facArticulos;

	@ManyToOne
	@JoinColumn(name = "id_sede")
	private Organizacion sede;

	@ManyToOne
	@JoinColumn(name = "Cod_EstadoCon")
	private EstadoDocumento codEstadoCon;

	@Column(name = "total")
	private Integer total;

	@Column(name = "fecha")
	private Date fecha;

	@Column(name = "mSubTotal")
	private Double subTotal;

	@Column(name = "mIVA")
	private Double iva;
}
