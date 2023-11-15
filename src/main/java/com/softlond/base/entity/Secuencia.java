package com.softlond.base.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "_secuencias")
public class Secuencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "id_relacion")
    private Integer idRelacion;

    @Column(name = "tipo_relacion")
    private String tipoRelacion;

    @Column(name = "secuencia")
    private Integer secuencia;
}
