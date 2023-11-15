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
@Table(name ="concepto_nota_debito_cliente")
public class ConceptoNotaDebitoCliente {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "id_nota_debito")
	private NotaDebitoCliente idNotaDebito;
	
	@Column(name = "detalle_cuenta", length = 255)
	private String detalleCuenta;
	
	@Column(name = "valor")
	private Integer valor;
	
	@Column(name = "codigo_documento", length = 5)
	private String codigoDocumento;
	
	@Column(name = "numero_documento", length = 20)
	private String numeroDocumento;
	
}
