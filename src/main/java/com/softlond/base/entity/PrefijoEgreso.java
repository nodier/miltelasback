package com.softlond.base.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Date;

@Data
@Entity
@Table(name = "prefijo_egreso")
public class PrefijoEgreso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "prefijo")
    private String prefijo;

    @CreationTimestamp
    @Column(name = "fecha_creacion")
    private Date fechaCreacion;

    @Column(name = "observaciones")
    private String observaciones;

    @Column(name = "activo")
    private Boolean activo;

    @Column(name = "tipo_gastos")
    private Boolean tipoGastos;

    @Column(name = "tipo_nomina")
    private Boolean tipoNomina;

    @Column(name = "tipo_proveedores")
    private Boolean tipoProveedores;

    @ManyToOne
    @JoinColumn(name = "id_cuenta")
    private Cuenta cuenta;

    @ManyToOne
    @JoinColumn(name = "id_creador")
    private Usuario creador;
    
    private Integer inicio;
    
    private Integer fin;
    
    @ManyToOne
	@JoinColumn(name = "id_sede")
	private Organizacion idSede;
}
