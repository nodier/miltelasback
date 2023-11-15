package com.softlond.base.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;

@Data
@Entity
@Table(name ="inv_sector")
public class InvSector implements Serializable{

	 /**
		 * 
		 */
		 private static final long serialVersionUID = 1L;

		 @Id
		 @GeneratedValue(strategy = GenerationType.IDENTITY)
		 @Column(name = "id")
		 private Integer id;
		 
		 @ManyToOne
		 @JoinColumn(name = "id_local")
		 private InveLocal idLocal;
		
		 @Column(name = "t_sector")
		 private String tSector;
		 
		 @Column(name = "nCapacidad")
		 private Integer capacidad;
		 
		 @Column(name = "bActivo")
		 private boolean activo;
		 
	}

