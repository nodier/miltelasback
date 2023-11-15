package com.softlond.base.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@Entity
@Table(name = "row_inventario")
public class RowInventario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "empresa")
	private Organizacion empresa;

	@ManyToOne()
	@JoinColumn(name = "producto")
	private Producto producto;

	Double cantidadCompra;

	String Unidad;

	Double costoUnidad;

	Double cantidadDisponible;

	@ManyToOne()
	@JoinColumn(name = "estado")
	private EstadoArticulo estado;

	@ManyToOne()
	@JoinColumn(name = "local")
	private InveLocal local;

	@ManyToOne()
	@JoinColumn(name = "sector")
	private InvSector sector;

	Date ingreso;
	
	String codigoArticulo;

	@ManyToOne()
	@JoinColumn(name = "usuario")
	private InformacionUsuario usuario;

	@ManyToOne()
	@JoinColumn(name = "proveedor")
	private Proveedor proveedor;
	
	@JsonIgnoreProperties({"rows","hibernateLazyInitializer","handler"})
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_inventario")
	private Inventario inventario;
}
