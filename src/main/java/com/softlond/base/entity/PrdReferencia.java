package com.softlond.base.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.ManyToAny;

import lombok.Data;

@Data
@Entity
@Table(name ="Prd_Referencia")
public class PrdReferencia implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "tReferencia", length = 25)
	private String treferencia;
	
	@Column(name = "tDescripcion", length = 255)
	private String tDescripcion;
	
	@Column(name = "tid_referencia")
	private String tIdReferencia;
	
	@ManyToOne(cascade=CascadeType.MERGE)
	@JoinColumn(name = "id_tipo")
	private PrdTipos tipo;
	
	public PrdReferencia() {
	}

}
