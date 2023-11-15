package com.softlond.base.entity;

import java.sql.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@Entity
@Table(name = "pagos_recibo_caja")
public class PagosReciboCaja {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;	
	
	@Column(name = "valor")
	private Integer valor;
	
	@ManyToOne
	@JoinColumn(name = "id_formas_pago")
	private FormasPago idFormasPago;
	
	@Column(name = "cuenta",length = 20)
	private String cuenta;
	
	@ManyToOne
	@JoinColumn(name = "id_tipo_tarjeta", nullable = true)
	private RbcTipoTarjetas idTipoTarjeta;
	
	@Column(name = "numero_aprobacion",length = 20)
	private String numeroAprobacion;
	
	@Column(name = "banco_cheque",length = 25)
	private String bancoCheque;
	
	@Column(name = "fecha")
	private Date fecha;
	
	@ManyToOne
	@JoinColumn(name = "id_creador")
	private Usuario idCreador;
	
	@Column(name = "cta")
	private String cta;
}
