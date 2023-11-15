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
@Table(name = "tercero")
public class Tercero {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "CLAVE", length = 50)
  private String CLAVE;

  @Column(name = "codigo", length = 15)
  private String CODIGO;

  @Column(name = "dv", length = 1)
  private String DV;

  @Column(name = "naturaleza", length = 1)
  private String NATURALEZA;

  @Column(name = "nom1", length = 20)
  private String NOM1;

  @Column(name = "nom2", length = 20)
  private String NOM2;

  @Column(name = "apl1", length = 20)
  private String APL1;

  @Column(name = "apl2", length = 20)
  private String APL2;

  @Column(name = "empresa", length = 80)
  private String EMPRESA;

  @Column(name = "razon_comercial", length = 50)
  private String RAZON_COMERCIAL;

  @Column(name = "direccion", length = 80)
  private String DIRECCION;

  @Column(name = "telefono", length = 50)
  private String TELEFONO;

  @Column(name = "movil", length = 50)
  private String MOVIL;

  @Column(name = "email", length = 50)
  private String EMAIL;

  @Column(name = "codigo_postal", length = 6)
  private String CODIGO_POSTAL;

  @Column(name = "gerente", length = 50)
  private String GERENTE;

  @Column(name = "codidentidad", length = 5)
  private String CODIDENTIDAD;

  @Column(name = "codsociedad", length = 5)
  private String CODSOCIEDAD;

  @Column(name = "codpais", length = 5)
  private String CODPAIS;

  @Column(name = "codactividad", length = 5)
  private String CODACTIVIDAD;

  @Column(name = "codzona", length = 5)
  private String CODZONA;

  @Column(name = "codmunicipio", length = 5)
  private String CODMUNICIPIO;
}
