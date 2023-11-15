package com.softlond.base.entity;

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
@Table(name = "Ter_Proveedores")
public class Proveedor {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "nID_Proveedor")
	private Integer id;

	@OneToOne
	@JoinColumn(name = "Cod_ClasificacionLegal")
	private ClasificacionLegal clasificacionLegal;

	@Column(name = "tNIToCC", length = 50)
	private String nit;

	@Column(name = "Digito")
	private Integer digito;

	@OneToOne
	@JoinColumn(name = "tCod_TipoIdentificacion")
	private MaestroValor tipoIdentificacion;

	@OneToOne
	@JoinColumn(name = "tCod_TipoProveedor")
	private MaestroValor tipoProveedor;

	@Column(name = "tProveedor", length = 50)
	private String proveedor;

	@Column(name = "tDireccion")
	private String direccion;

	@OneToOne
	@JoinColumn(name = "nID_Depto")
	private Departamento depto;

	@OneToOne
	@JoinColumn(name = "nID_Ciudad")
	private Ciudad ciudad;

	@Column(name = "tContacto")
	private String contacto;

	@Column(name = "tCargoContacto")
	private String cargoContacto;

	@Column(name = "tTelefono")
	private String telefono;

	@Column(name = "tFax")
	private String fax;

	@Column(name = "tEmail")
	private String email;

	@Column(name = "tWebsite")
	private String webSite;

	@Column(name = "tObservaciones")
	private String observaciones;

	@Column(name = "bActivo")
	private Boolean activo;

	@Column(name = "dFechaMod")
	private Date fechaMod;

	@OneToOne
	@JoinColumn(name = "UsuarioMod")
	private InformacionUsuario usuarioMod;

	@OneToOne
	@JoinColumn(name = "barrio")
	private Barrio barrio;

}
