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
@Table(name = "secundariom")
public class Secundario {

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

  @Column(name = "tipoRef", length = 5)
  private String TIPO_REF;

  @Column(name = "prefijoRef", length = 5)
  private String PREFIJO_REF;

  @Column(name = "numeroRef", length = 20)
  private String NUMERO_REF;

  @Column(name = "abono", length = 20)
  private Integer ABONO;

  @Column(name = "nota", length = 200)
  private String NOTA;

}
