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
@Table(name = "Config_Doc_Motivos")
public class Motivos {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nID_Motivo")
    private Integer id;
	
	@Column(name = "tCod_Documento")
    private String documento;

    @Column(name = "tMotivo")
    private String motivo;
}
