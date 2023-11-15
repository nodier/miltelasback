package com.softlond.base.dto;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class RespuestaDto {

	private String categoria;
	private String codigo;
	private String descripcion;
	private int estado;
	private String tiempoRespuesta;
	private Integer suma;
	private String sumas;
	private Object objetoRespuesta;
	private String variable;
	
	public RespuestaDto(){
	}

	public RespuestaDto(HttpStatus estado, String mensaje) {
		super();
		this.tiempoRespuesta = OffsetDateTime.now().toString();
		this.estado = estado.value();
		this.codigo = estado.name();
		this.descripcion = mensaje;
		this.variable = "";
		this.categoria = "DEFAULT_CATEGORY";
	}
	
	public RespuestaDto(HttpStatus estado, String mensaje, String variable) {
		super();
		this.tiempoRespuesta = OffsetDateTime.now().toString();
		this.estado = estado.value();
		this.codigo = estado.name();
		this.descripcion = mensaje;
		this.variable = variable;
		this.categoria = "DEFAULT_CATEGORY";
	}
	
	public RespuestaDto(HttpStatus estado, String mensaje, Object objetoRespuesta, Integer suma, String variable) {
		super();
		this.tiempoRespuesta = OffsetDateTime.now().toString();
		this.estado = estado.value();
		this.descripcion = mensaje;
		this.objetoRespuesta = objetoRespuesta;
		this.variable = variable;
		this.categoria = "DEFAULT_CATEGORY";
		this.suma = suma;
	}
	
	public RespuestaDto(HttpStatus estado, String mensaje, Object objetoRespuesta, String sumas, String variable) {
		super();
		this.tiempoRespuesta = OffsetDateTime.now().toString();
		this.estado = estado.value();
		this.descripcion = mensaje;
		this.objetoRespuesta = objetoRespuesta;
		this.variable = variable;
		this.categoria = "DEFAULT_CATEGORY";
		this.sumas = sumas;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public int getEstado() {
		return estado;
	}

	public void setEstado(int estado) {
		this.estado = estado;
	}

	public String getTiempoRespuesta() {
		return tiempoRespuesta;
	}

	public void setTiempoRespuesta(String tiempoRespuesta) {
		this.tiempoRespuesta = tiempoRespuesta;
	}

	public Object getObjetoRespuesta() {
		return objetoRespuesta;
	}

	public void setObjetoRespuesta(Object objetoRespuesta) {
		this.objetoRespuesta = objetoRespuesta;
	}

	public String getVariable() {
		return variable;
	}

	public void setVariable(String variable) {
		this.variable = variable;
	}
	
	public Integer getSuma() {
		return suma;
	}
	
	public String getSumas() {
		return sumas;
	}

	public void setSuma(Integer suma) {
		this.suma = suma;
	}
	
	public void setSumas(String suma) {
		this.sumas = suma;
	}
				
}
