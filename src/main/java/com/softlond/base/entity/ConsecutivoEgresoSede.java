package com.softlond.base.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "consecutivo_egreso_por_sede")
public class ConsecutivoEgresoSede {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_sede")
    private Organizacion sede;

    @Column(name = "valor_actual")
    private int valorActual;
    
    @ManyToOne
    @JoinColumn(name = "id_prefijo")
    private PrefijoEgreso prefijo;
}
