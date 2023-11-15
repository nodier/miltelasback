package com.softlond.base.entity;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.joda.time.DateTime;

import lombok.Data;

@Data
@Entity
@Table(name = "articulo")
public class Articulo implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @JoinColumn(name = "id_sede")
    @ManyToOne()
    private Organizacion sede;

    @ManyToOne()
    @JoinColumn(name = "id_producto")
    private Producto producto;

    @Column(name = "codigo")
    private String codigo;

    @ManyToOne()
    @JoinColumn(name = "local")
    private InveLocal local;

    @ManyToOne()
    @JoinColumn(name = "sector")
    private InvSector sector;

    @Column(name = "fecha_ingreso")
    private Date fechaIngreso;

    @Column(name = "fecha_actualizacion")
    private Date fechaActualizacion;

    @Column(name = "cantidad_compra")
    private Float cantidadCompra;

    @Column(name = "cantidad_disponible")
    private Float cantidadDisponible;

    @Column(name = "cantidad_medida")
    private Float cantidadMedida;

    @Column(name = "iva_costo")
    private Float ivaCosto;

    @Column(name = "precio_costo")
    private Double precioCosto;

    @Column(name = "en_promocion")
    private Integer promocion;

    @JoinColumn(name = "nID_EstadoArticulo")
    @OneToOne()
    private EstadoArticulo estadoArticulo;

    public Articulo() {

    }
}
