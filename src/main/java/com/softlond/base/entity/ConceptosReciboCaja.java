package com.softlond.base.entity;

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
@Table(name = "conceptos_recibo_caja")
public class ConceptosReciboCaja {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "conceptos", length = 255)
	private String conceptos;

	@Column(name = "valor_descuento")
	private Double valorDescuento;

	@Column(name = "valor_abono")
	private Double valorAbono;

	@Column(name = "saldo_inicial")
	private Double saldoInicial;

	@Column(name = "saldo_final")
	private Double saldoFinal;

	@Column(name = "cod_documento", length = 30)
	private String codDocumento;

	@Column(name = "nro_documento", length = 20)
	private String nroDocumento;

	@Column(name = "retencion")
	private Integer retencion;

}
