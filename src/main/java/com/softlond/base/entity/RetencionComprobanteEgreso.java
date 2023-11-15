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
@Table(name ="com_comprobante_retenciones")
public class RetencionComprobanteEgreso {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "nid_com_retencion")
	private Integer id;
	
	@Column(name = "m_valor")
	private Integer valor;
	
	@ManyToOne
	@JoinColumn(name = "nid_pyme_impuesto_retenido")
	private Impuesto impuesto;
}
