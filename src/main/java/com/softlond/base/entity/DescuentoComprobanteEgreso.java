package com.softlond.base.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Date;

@Data
@Entity
@Table(name = "dcto_comprobante_egreso")
public class DescuentoComprobanteEgreso {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "nid_com_descuento")
	private Integer id;
	
	@Column(name = "m_valor")
	private Integer valor;
	
	@ManyToOne
	@JoinColumn(name = "nid_pyme_impuesto")
	private Impuesto impuesto;
}
