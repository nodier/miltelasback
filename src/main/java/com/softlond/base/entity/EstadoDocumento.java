package com.softlond.base.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "Config_Doc_Estados")
public class EstadoDocumento {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tMoC")
    private Integer id;
	
	@Column(name = "Cod_Estado")
	private String codigo;
	
	@Column(name = "tEstado")
    private String estado;

    @Column(name = "xDefecto")
    private Integer defecto;
}
