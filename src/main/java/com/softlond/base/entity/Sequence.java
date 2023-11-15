package com.softlond.base.entity;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "_sequence")
public class Sequence implements Serializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	Integer id;
	
	@Column(name = "seq_val")
	private Integer valorSequencia;
	
	@Column(name = "seq_Sede")
	private Integer idSede;
	
	@Column(name = "seq_prefijo")
	private Integer idPrefijo;

}
