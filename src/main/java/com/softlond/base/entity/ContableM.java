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
@Table(name = "contablem")
public class ContableM {

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

  @Column(name = "fecha", length = 20)
  private String FECHA;

  @Column(name = "cuenta", length = 30)
  private String CUENTA;

  @Column(name = "tercero", length = 15)
  private String TERCERO;

  @Column(name = "centro", length = 5)
  private String CENTRO;

  @Column(name = "activo", length = 15)
  private String ACTIVO;

  @Column(name = "empleado", length = 15)
  private String EMPLEADO;

  @Column(name = "debito", length = 18)
  private String DEBITO;

  @Column(name = "credito", length = 18)
  private String CREDITO;

  @Column(name = "base", length = 18)
  private String BASE;

  @Column(name = "nota", length = 80)
  private String NOTA;

  @Column(name = "usuario", length = 10)
  private String USUARIO;
}
