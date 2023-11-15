package com.softlond.base.entity;

import java.io.Serializable;
import java.sql.Date;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Data;

@Data
@Entity
@Table(name = "informacion_usuario")
public class InformacionUsuario  implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "nombre_completo")
	private String nombreCompleto;

	@Column(name = "correo")
	private String correo;

	@Column(name = "numero_documento", unique= true)
	private String numeroDocumento;

	@Column(name = "fecha_cumpleanos")
	private Date fechaCumpleanos;

	@Column(name = "direccion")
	private String direccion;

	@Column(name = "telefono")
	private String telefono;

	@Column(name = "celular")
	private String celular;
	
	@ManyToOne
	@JoinColumn(name = "id_rol")
	private MaestroValor idRol;

	@ManyToOne
	@JoinColumn(name = "id_genero")
	private MaestroValor idGenero;

	@ManyToOne
	@JoinColumn(name = "tipo_documento")
	private MaestroValor tipoDocumento;
	
	@ManyToOne
	@JoinColumn(name = "id_usuario")
	private Usuario idUsuario;

	@ManyToOne
	@JoinColumn(name = "id_plan_activado")
	private Plan idPlanActivado;
	
	@ManyToOne
	@JoinColumn(name = "id_perfil_activado")
	private Perfil idPerfilActivado;
	
	@ManyToOne
	@JoinColumn(name = "id_creador")
	private Usuario idCreador;
	
	@ManyToOne
	@JoinColumn(name = "id_ultimo_usuario_modifico")
	private Usuario idUltimoUsuarioModifico;

	@Column(name = "fecha_creacion")
	private Date fechaCreacion;

	@Column(name = "fecha_actualizacion")
	private Date fechaActualizacion;
	
	@ManyToOne
	@JoinColumn(name = "id_organizacion")
	private Organizacion idOrganizacion;
	
	@ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "usuario_sedes", joinColumns = @JoinColumn(name = "id_usuario", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name="id_organizacion",referencedColumnName = "id"),uniqueConstraints = @UniqueConstraint(columnNames = {"id_organizacion","id_usuario"}))
    private Collection<Organizacion> sedes;
	
}
