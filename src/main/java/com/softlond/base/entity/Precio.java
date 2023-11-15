package com.softlond.base.entity;

import java.io.Serializable;
import java.sql.Date;

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
@Table(name = "prd_precios")
public class Precio implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nID_Precio")
    private Integer id;

    @JoinColumn(name = "ID_Pyme")
    @ManyToOne()
    private Organizacion sede;
    
    @ManyToOne()
    //@Transactional(readOnly = true)
	@JoinColumn(name = "nid_producto")
    private Producto producto;

    @Column(name = "mPrecioCostoCalc")
    private Double precioCosto;
    
    @Column(name = "mPrecioVenta")
    private Double precioVenta;
    
    @Column(name = "bActivo")
    private Integer activo;
    
    @Column(name = "dFechaMod")
    private Date fechaMod;

}
