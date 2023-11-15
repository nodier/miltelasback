package com.softlond.base.entity;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;
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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

//@Data
@Setter
@Getter
@Entity
@Table(name ="inve_local")
public class InveLocal implements Serializable{

	 private static final long serialVersionUID = 1L;

	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 @Column(name = "id")
	 private Integer id;

	 @ManyToOne
	 @JoinColumn(name = "id_sede")
	 private Organizacion sede;
	
	 @Column(name = "t_local")
	 private String tLocal;
	 
	 @Column(name = "tDescripcion")
	 private String descripcion;
	 
	 @Column(name = "bActivo")
	 private boolean activo;
	 
	 @ManyToOne
	 @JoinColumn(name = "nID_Responsable")
	 private InformacionUsuario responsable;
	 
	 @Column(name = "nCtaActivoInv")
	 private String ctaActivoInv;
	 
	 @Column(name = "nCtaIngresoAjustesInv")
	 private String ctaIngresoAjustesInv;
	 
	 @Column(name = "nCtaPerdidasAjustesInv")
	 private String ctaPerdidasAjustesInv;
	 
	 @Column(name = "nCtaDevComprasCr")
	 private String ctaDevComprasCr;
	 
	 @Column(name = "nCtaDevVentasDb")
	 private String ctaDevVentasDb;
	 
	 @Column(name = "bPermiteVenta")
	 private boolean permiteVenta;
	 
	 @JsonIgnoreProperties(value = {"idLocal","hibernateLazyInitializer","handler"}, allowSetters = true)
	 @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	 @JoinColumn(name = "id_local")
	 private List<InvSector> sectores;

	
}
