package com.softlond.base.entity;

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
@Table(name = "rbe_recibo_egreso_conceptos")
public class ConceptoReciboEgreso {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "nid_concepto")
	Integer id;

	@Column(name = "t_concepto")
	String concepto;

	@Column(name = "t_valor_descuento")
	Double valorDescuento;

	@Column(name = "t_valor_abono")
	Double valorAbono;

	@ManyToOne
	@JoinColumn(name = "cod_estado_con")
	private EstadoDocumento codEstadoCon;

	@Column(name = "numero_Documento")
	String numeroDocumento;

	@Column(name = "nombre")
	String nombre;

	@Column(name = "descuento")
	private Float descuento;

}



