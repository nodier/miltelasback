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
@Table(name = "referencia")
public class ReferenciaProd {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "clave", length = 50)
  private String CLAVE;

  @Column(name = "codigo", length = 20)
  private String CODIGO;

  @Column(name = "codigoD", length = 20)
  private String CODIGO2;

  @Column(name = "nombre", length = 80)
  private String NOMBRE;

  @Column(name = "nombreD", length = 20)
  private String NOMBRE2;

  @Column(name = "costo", length = 0)
  private String COSTO;

  @Column(name = "precio", length = 0)
  private String PRECIO;

  @Column(name = "codlinea", length = 5)
  private String CODLINEA;

  @Column(name = "codmedida", length = 5)
  private String CODMEDIDA;

  @Column(name = "codesqimpuesto", length = 5)
  private String COD_ESQIMPUESTO;

  @Column(name = "codesqretencion", length = 5)
  private String COD_ESQRETENCION;

  @Column(name = "codesqcontable", length = 5)
  private String COD_ESQCONTABLE;
}
