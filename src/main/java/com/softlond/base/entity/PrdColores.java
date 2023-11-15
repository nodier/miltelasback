package com.softlond.base.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name ="PrdColores")
public class PrdColores implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "tColor", length =25)
	private String tColor;
	
	@Column(name = "tRGB", length =11)
	private String tRGB;
	
	@Column(name = "tid_color", length =11)
	private String tidColor;
	
	public PrdColores() {
	}

}
