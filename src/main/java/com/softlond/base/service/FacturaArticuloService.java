package com.softlond.base.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.ArticulosRemisionCompra;
import com.softlond.base.entity.Factura;
import com.softlond.base.entity.FacturaArticulos;
import com.softlond.base.repository.FacturaArticuloDao;

@Service
public class FacturaArticuloService {
	
private static final Logger logger = Logger.getLogger(FacturaArticuloService.class);
	
	@Autowired
	private FacturaArticuloDao facturaArticuloDao;
	
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> listarFacturasArticuloSede(@RequestParam Integer idSede){
		
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		try {
			List<FacturaArticulos> facturas = facturaArticuloDao.findAllBySede(idSede);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de facturas exitosa");
			respuestaDto.setObjetoRespuesta(facturas);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error al obtener las facturas");
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error al obtener las facturas");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}
	
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> crearFacturaArticulo(@RequestBody FacturaArticulos factura){
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		try {
			FacturaArticulos facturaArticuloGuardada = facturaArticuloDao.save(factura);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "factura creada exitosamente");
			respuestaDto.setObjetoRespuesta(facturaArticuloGuardada);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error al guardar la factura");
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error al guardar la factura"+e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}
	
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> editarFacturaArticulo(@RequestBody FacturaArticulos factura){
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		try {
			//Optional<FacturaArticulos> fac = facturaArticuloDao.findById(factura.getId());		
			FacturaArticulos facturaArticuloGuardada = facturaArticuloDao.save(factura);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "factura creada exitosamente");
			respuestaDto.setObjetoRespuesta(facturaArticuloGuardada);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error al guardar la factura");
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error al guardar la factura"+e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}
	
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> actualizarCantidad(@RequestBody FacturaArticulos factura){
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		try {
			FacturaArticulos facturaArticuloGuardada = facturaArticuloDao.save(factura);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "factura creada exitosamente");
			respuestaDto.setObjetoRespuesta(facturaArticuloGuardada);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error al guardar la factura");
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error al guardar la factura"+e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}
	

}
