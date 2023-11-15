package com.softlond.base.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.Impuesto;
import com.softlond.base.entity.Menu;
import com.softlond.base.entity.Usuario;
import com.softlond.base.repository.FacturaImpuestosDao;
import com.softlond.base.repository.ImpuestosDao;
import com.softlond.base.entity.Factura;
import com.softlond.base.entity.FacturaImpuestos;

@Service
public class ImpuestoService {
	
	private static final Logger logger = Logger.getLogger(ImpuestoService.class);

	@Autowired
	private ImpuestosDao impuestoDao;
	
	@Autowired
	private FacturaImpuestosDao facturaImpuestoDao;
	
	
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER')")
	public ResponseEntity<Object> listarTodos(){
		ResponseEntity<Object> respuesta;
		try {
			List<Impuesto> impuestos = (List<Impuesto>) impuestoDao.findAll();
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de impuestos exitosa");
			respuestaDto.setObjetoRespuesta(impuestos);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error("Error obteniendo impuestos" + e.getMessage());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo obteneer los impuestos");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}
	
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER')")
	public ResponseEntity<Object> listarTodosRecibo(){
		ResponseEntity<Object> respuesta;
		try {
			List<Impuesto> impuestos = (List<Impuesto>) impuestoDao.obtenerImpuestosRecibo();
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de impuestos exitosa");
			respuestaDto.setObjetoRespuesta(impuestos);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error("Error obteniendo impuestos" + e.getMessage());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo obteneer los impuestos");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}
	
	
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER')")
	public ResponseEntity<Object> ObtenerImpuestoFactura(Integer factura){
		ResponseEntity<Object> respuesta;
		try {		
		
			
			ArrayList<FacturaImpuestos> impuestoFactura = facturaImpuestoDao.obtenerImpuestosFactura(factura);			 
			List<Impuesto> impuestos =  (List<Impuesto>) impuestoDao.obtenerImpuestosClientes();
             
             for (int i =0; i<impuestoFactura.size(); i++) {
            	       
            	 for (int j=0; j<impuestos.size(); j++) {
            
                     if (impuestoFactura.get(i).getImpuesto().getEnlace() == impuestos.get(j).getId()) {
                    	  impuestos.remove(j);                    	
                     }
                     else {
                    	 //logger.info("no entro a la validacion");
                     }
            	 }
             }         
						
			respuesta = ResponseEntity.ok(HttpStatus.OK);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de impuestos exitosa");
			respuestaDto.setObjetoRespuesta(impuestos);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			respuesta = ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			logger.error("Error obteniendo impuestos" + e.getMessage());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo obteneer los impuestos");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}
	
	
	@PreAuthorize("hasAuthority('SUPER')")
	public ResponseEntity<Object> crearImpuesto(Impuesto nuevoImpuesto) {
		
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		
				try {
			if (nuevoImpuesto.getPorcentaje()!= null) {
				
				nuevoImpuesto.setEnlace(0);
				
				this.impuestoDao.save(nuevoImpuesto);
				
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito");
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
			}
			else {
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, "Error nombre de menú ya existe");
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.BAD_REQUEST);
			}
		} 
		catch (Exception e) {
			logger.error("Error en la creacion del impuesto");
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error en la creacion del menu");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return respuesta;
	}
	
	@PreAuthorize("hasAuthority('SUPER')")
	public ResponseEntity<Object> enlazarImpuesto(Impuesto nuevoImpuesto) {
		
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		
	
		Impuesto ImpuestoEnlace = impuestoDao.insertarEnlace(nuevoImpuesto.getEnlace());
		ImpuestoEnlace.setEnlace(nuevoImpuesto.getId());		
		
		try {
			if (nuevoImpuesto.getEnlace()!= null) {
				
				this.impuestoDao.save(nuevoImpuesto);
				
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito");
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
			}
			else {
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, "Error nombre de menú ya existe");
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.BAD_REQUEST);
			}
		} 
		catch (Exception e) {
			logger.error("Error en la creacion del impuesto");
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error en la creacion del menu");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return respuesta;
	}
	
	@PreAuthorize("hasAuthority('SUPER')")
	public ResponseEntity<Object> eliminarEnlace(Impuesto nuevoImpuesto) {
		
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		
		
		Impuesto ImpuestoEnlace = impuestoDao.insertarEnlace(nuevoImpuesto.getEnlace());
		ImpuestoEnlace.setEnlace(0);		
		
		try {
			if (nuevoImpuesto.getEnlace()!= null) {
				nuevoImpuesto.setEnlace(0);
				this.impuestoDao.save(nuevoImpuesto);
				
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito");
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
			}
			else {
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, "Error nombre de menú ya existe");
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.BAD_REQUEST);
			}
		} 
		catch (Exception e) {
			logger.error("Error en la creacion del impuesto");
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error en la creacion del menu");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return respuesta;
	}
	@PreAuthorize("hasAuthority('SUPER')")
	public ResponseEntity<Object> ObtenerImpuestosConexion(String nuevoImpuesto) {
		
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		
		 List<Impuesto> impuestos = null;
		
		try {
			
			  if(nuevoImpuesto.equals("factura compra")) {
				 impuestos =  (List<Impuesto>) impuestoDao.obtenerImpuestosClientesEnlace();
			  }else
				  if(nuevoImpuesto.equals("comprobante egreso"))
			  {
					impuestos =  (List<Impuesto>) impuestoDao.obtenerImpuestosProveedoresEnlace();
			  }
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
			respuestaDto.setObjetoRespuesta(impuestos);
				
		} 
		catch (Exception e) {
			logger.error("Error en la creacion del impuesto");
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error en la creacion del menu");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return respuesta;
	}
	
	/* Metodo para elminar los menus en el sistema */
	@PreAuthorize("hasAuthority('SUPER')")
	public @ResponseBody ResponseEntity<Object> borrarImpuesto(Integer id) {
		
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
			
		try {
			Impuesto auxMenu = this.impuestoDao.findById(id).get();
			this.impuestoDao.delete(auxMenu);
			
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} 
		catch (Exception e) {
			Throwable t = e.getCause();
			
			if (t instanceof ConstraintViolationException) {
				logger.error("El menu se encuentra asociado en el sistema" + e.getCause());
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, "Error, el menu se encuentra asociado en el sistema");
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.BAD_REQUEST);
			} 
			else {
				logger.error("Error en el borrado del Menú");
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error en el borrado del Menú");
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		return respuesta;
	}
	
	@PreAuthorize("hasAuthority('SUPER')")
	public ResponseEntity<Object> actualizarImpuesto(@RequestBody Impuesto impuestoNuevo) {
		
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		
		Authentication autentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autentication.getPrincipal();
		
		try {
			
			Impuesto menuActualizar = impuestoDao.findById(impuestoNuevo.getId()).get();
			
				
					this.impuestoDao.save(impuestoNuevo);
					impuestoDao.save(impuestoNuevo);
				
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito");
				respuestaDto.setObjetoRespuesta(impuestoNuevo);
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
			
		} 
		catch (Exception e) {
			logger.error("Error en el actualizado del Menu");
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error en el actualizado del Menu");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return respuesta;
	}
}


