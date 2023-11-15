package com.softlond.base.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.ProveedorCondicionesComerciales;
import com.softlond.base.repository.ProveedorCondicionesComercialesDao;

@Service
public class ProveedorCondicionesComercialesService {

private static final Logger logger = Logger.getLogger(ProveedorCondicionesComercialesService.class);
	
	@Autowired
	private ProveedorCondicionesComercialesDao proveedorCondicionesComercialesDao;

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> crearCondicionComercial(ProveedorCondicionesComerciales condicionComercial){
		ResponseEntity<Object> respuesta;
		try {
			proveedorCondicionesComercialesDao.save(condicionComercial);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "creación de una accion comercial exitosa");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
			
		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error("Error creando proveedor" + e.getMessage());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo crear una accion comercial");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}
	
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> eliminarCondicionComercial(Integer idProveedorProducto){
		ResponseEntity<Object> respuesta;
		try {
			proveedorCondicionesComercialesDao.deleteById(idProveedorProducto);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "eliminación de accion comercial exitosa");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
			
		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error("Error creando proveedor" + e.getMessage());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo eliminar una accion comercial");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}
	
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> ObtenerCondicionesComerciales(){
		ResponseEntity<Object> respuesta;
		try {
			List<ProveedorCondicionesComerciales> condicionesComerciales = (List<ProveedorCondicionesComerciales>) proveedorCondicionesComercialesDao.findAll();
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de acciones comerciales exitosa");
			respuestaDto.setObjetoRespuesta(condicionesComerciales);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error("Error creando proveedor" + e.getMessage());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo obteneer las acciones comerciales");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}
	
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> ObtenerCondicionesComercialesbyProveedor(Integer idProveedor){
		ResponseEntity<Object> respuesta;
		try {
			List<ProveedorCondicionesComerciales> condicionesComerciales = proveedorCondicionesComercialesDao.obtenerProductosProveedoresbyProveedor(idProveedor);
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de acciones comerciales exitosa");
			respuestaDto.setObjetoRespuesta(condicionesComerciales);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error("Error creando proveedor" + e.getMessage());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo obteneer las acciones comerciales");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}
}
