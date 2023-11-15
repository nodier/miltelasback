package com.softlond.base.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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

import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@Entity
@Table(name = "producto")
public class Producto implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "codigo", unique = true, nullable = false)
    private String codigo;

    @ManyToOne()
   	@JoinColumn(name = "tipo")
    private PrdTipos tipo;

    @ManyToOne()
   	@JoinColumn(name = "referencia")
    private PrdReferencia referencia;

    @ManyToOne()
   	@JoinColumn(name = "id_presentacion")
    private PrdPresentacion presentacion;
    
    @ManyToOne()
   	@JoinColumn(name = "id_descuento")
    private PrdDescuentos descuento;

    @ManyToOne()
   	@JoinColumn(name = "id_color")
    private PrdColores color;

    @ManyToOne()
   	@JoinColumn(name = "id_clasificacion")
    private PrdClasificaciones clasificacion;

    @Column(name = "producto")
    private String producto;

    @Column(name = "observaciones")
    private String observaciones;

    @Column(name = "iva")
    private float iva;

    @ManyToOne
    @JoinColumn(name = "unidad")
    private ConfigUnidades unidad;

    @Column(name = "requiere_precio")
    private Boolean requierePrecio;
    
    @Column(name = "nStock_Min")
    private Integer stMin;
    
    @Column(name = "nStock_Max")
    private Integer stMax;
    
    private Date fechaCreacion;
    
    private Date fechaModificacion;
    
    @JsonIgnoreProperties({"producto","hibernateLazyInitializer","handler"})
	@OneToMany(mappedBy = "producto")
	private List<Articulo> articulos;
    
    @Transient
    @JsonIgnoreProperties(value = {"producto","hibernateLazyInitializer","handler"})
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "nID_Producto")
	private List<Precio> precios;
    
    
    public Producto() {
    	articulos = new ArrayList<Articulo>();
    	precios = new ArrayList<Precio>();
    }
}
