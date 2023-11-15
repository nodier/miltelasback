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
@Table(name = "enlacefac")
public class EnlaceFactura {

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
}
