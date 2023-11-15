package com.softlond.base.entity;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "documento_movimiento")
public class DocumentoMovimiento implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "tipo_documento")
	private String tipoDocumento;

	@Column(name = "cod_articulo")
	private String codArticulo;

	@Column(name = "ubicacion_inicial")
	private String ubInicial;

	@Column(name = "ubicacion_final")
	private String ubFinal;

	@Column(name = "cliente")
	private String cliente;

	@Column(name = "fecha_creacion")
	private String fechaCreacion;

	@Column(name = "id_documento")
	private String idDocumento;

	@Column(name = "cantidad")
	private String cantidad;

	@Column(name = "subtotal")
	private String costo;

	@Column(name = "total")
	private String total;

	@Column(name = "usuario_creador")
	private String usuarioCreador;

	@Column(name = "estado_documento")
	private String estadoDocumento;
}
