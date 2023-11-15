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
@Table(name = "facturam")
public class FacturaM {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "CLAVE", length = 50)
  private String CLAVE;

  @Column(name = "tipo", length = 5)
  private String TIPO;

  @Column(name = "prefijo", length = 5)
  private String PREFIJO;

  @Column(name = "numero", length = 10)
  private String NUMERO;

  @Column(name = "fecha", length = 0)
  private String FECHA;

  @Column(name = "vence", length = 0)
  private String VENCE;

  @Column(name = "tercero", length = 15)
  private String TERCERO;

  @Column(name = "vendedor", length = 15)
  private String VENDEDOR;

  @Column(name = "lista", length = 5)
  private String LISTA;

  @Column(name = "banco", length = 5)
  private String BANCO;

  @Column(name = "usuario", length = 10)
  private String USUARIO;

  @Column(name = "centro", length = 5)
  private String CENTRO;

  @Column(name = "bodega", length = 5)
  private String BODEGA;

  @Column(name = "referencia", length = 20)
  private String REFERENCIA;

  @Column(name = "entrada", length = 20)
  private Integer ENTRADA;

  @Column(name = "salida", length = 20)
  private Double SALIDA;

  // @Column(name = "salida", length = 20)
  // private Integer SALIDA;

  @Column(name = "unitario", length = 20)
  private Double UNITARIO;

  // @Column(name = "unitario", length = 20)
  // private Integer UNITARIO;

  @Column(name = "descuento", length = 20)
  private Float PORC_DESCUENTO;

  @Column(name = "nota", length = 200)
  private String NOTA;

}
