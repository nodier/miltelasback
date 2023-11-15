package com.softlond.base.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.DevolucionArticulosCompra;
import com.softlond.base.entity.DevolucionCompras;
import com.softlond.base.entity.InformacionUsuario;
import com.softlond.base.entity.MaestroValor;
import com.softlond.base.entity.Usuario;
import com.softlond.base.repository.DevolucionComprasArticulosDao;
import com.softlond.base.repository.DevolucionDao;
import com.softlond.base.repository.MaestroValorDao;
import com.softlond.base.repository.UsuarioInformacionDao;


@Service
public class DevolucionesService {
	
	private static final Logger logger = Logger.getLogger(DevolucionesService.class);
	
	@Autowired
	private MaestroValorDao maestrovalorDao;
	
	@Autowired
	UsuarioInformacionDao usuarioInformacionDao;
	
	@Autowired
	private DevolucionDao devolucionDao;
	
	@Autowired
	private DevolucionComprasArticulosDao devolucionComprasArticulosDao;
	
	@PersistenceContext
    private EntityManager entityManager;
	
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> devoluciones(String nDocumento, String fechaInicial, String fechaFinal, Integer estado, Integer page)
			throws Exception{
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		try {
			Page<DevolucionCompras> devoluciones = null;
			
			
			Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
			Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
			InformacionUsuario usuarioInformacion = this.usuarioInformacionDao.buscarPorIdUsuario(usuarioAutenticado.getId());

			Pageable pageConfig = PageRequest.of(page, 10);					

			int idSede = usuarioInformacion.getIdOrganizacion().getId();	
			
			
			if(nDocumento != ""  && fechaInicial == "" && fechaFinal == "" && estado == 0) {
				devoluciones = devolucionDao.findBynDocumentoSede(idSede, nDocumento, pageConfig);
				
			}
			if(nDocumento != ""  && fechaInicial != "" && fechaFinal == "" && estado == 0) {
				devoluciones = devolucionDao.findByDevolucionFechaInicial(idSede, nDocumento, fechaInicial, pageConfig);
				
			}
			if(nDocumento != ""   && fechaInicial != "" && fechaFinal != "" && estado == 0) {
				devoluciones = devolucionDao.findByDevolucionFechas(idSede, nDocumento, fechaInicial,fechaFinal, pageConfig);
				
			}
			if(nDocumento != ""  && fechaInicial == "" && fechaFinal == "" && estado == 1) {
				devoluciones = devolucionDao.findByDevolucionEstado5(idSede, nDocumento, pageConfig);
	
			}
			if(nDocumento != ""  && fechaInicial == "" && fechaFinal == "" && estado == 2) {
				devoluciones = devolucionDao.findByDevolucionEstado4(idSede, nDocumento, pageConfig);
	
			}
			if(nDocumento  !=  ""  && fechaInicial == "" && fechaFinal == "" && estado == 3) {
				devoluciones = devolucionDao.findByDevolucionEstado1(idSede, nDocumento, pageConfig);
		
			}
		
			if(nDocumento != ""  && fechaInicial == "" && fechaFinal == "" && estado == 4) {
				devoluciones = devolucionDao.findByDevolucionEstado2(idSede, nDocumento, pageConfig);
	
			}
			if(nDocumento != ""  && fechaInicial == "" && fechaFinal == "" && estado == 5) {
				devoluciones = devolucionDao.findByDevolucionEstado3(idSede, nDocumento, pageConfig);
	
			}

		
			if(nDocumento == ""  && fechaInicial != "" && fechaFinal == "" && estado == 0) {
				devoluciones = devolucionDao.findFechaInicial(idSede, fechaInicial, pageConfig);
			
			}
			
			if(nDocumento == ""   && fechaInicial == "" && fechaFinal != "" && estado == 0) {
				devoluciones = devolucionDao.findFechaFinal(idSede, fechaFinal, pageConfig);

			}
			if(nDocumento == "" &&   fechaInicial != "" && fechaFinal == "" && estado == 1) {
				devoluciones = devolucionDao.findByFechaEstado5(idSede, fechaInicial, pageConfig);

			}
			if(nDocumento == "" &&   fechaInicial != "" && fechaFinal == "" && estado == 2) {
				devoluciones = devolucionDao.findByFechaEstado4(idSede, fechaInicial, pageConfig);

			}
			
			if(nDocumento == ""  && fechaInicial != "" && fechaFinal == "" && estado == 3) {
				devoluciones = devolucionDao.findByFechaEstado1(idSede, fechaInicial, pageConfig);

			}
			
			if(nDocumento == "" &&   fechaInicial != "" && fechaFinal == "" && estado == 4) {
				devoluciones = devolucionDao.findByFechaEstado2(idSede, fechaInicial, pageConfig);

			}
			if(nDocumento == "" &&   fechaInicial != "" && fechaFinal == "" && estado == 5) {
				devoluciones = devolucionDao.findByFechaEstado3(idSede, fechaInicial, pageConfig);

			}

			if(nDocumento == ""  &&  fechaInicial != "" && fechaFinal != "" && estado == 0) {
				devoluciones = devolucionDao.findByFechas(idSede, fechaInicial, fechaFinal, pageConfig);
				
			}
			if(nDocumento == ""  && fechaInicial != "" && fechaFinal != "" && estado == 1) {
				devoluciones = devolucionDao.findByFechasEstado5(idSede, fechaInicial, fechaFinal, pageConfig);
				
			}
			if(nDocumento == ""  && fechaInicial != "" && fechaFinal != "" && estado == 2) {
				devoluciones = devolucionDao.findByFechasEstado4(idSede, fechaInicial, fechaFinal, pageConfig);
				
			}
			
			if(nDocumento == ""  &&   fechaInicial != "" && fechaFinal != "" && estado == 3) {
				devoluciones = devolucionDao.findByFechasEstado1(idSede, fechaInicial, fechaFinal, pageConfig);
			
			}
			
			if(nDocumento == ""  && fechaInicial != "" && fechaFinal != "" && estado == 4) {
				devoluciones = devolucionDao.findByFechasEstado2(idSede, fechaInicial, fechaFinal, pageConfig);
				
			}
			if(nDocumento == ""  && fechaInicial != "" && fechaFinal != "" && estado == 5) {
				devoluciones = devolucionDao.findByFechasEstado3(idSede, fechaInicial, fechaFinal, pageConfig);
				
			}
			if(nDocumento == ""  &&  fechaInicial == "" && fechaFinal == "" && estado == 1) {					
				devoluciones = devolucionDao.findByDevolucionEstado5(idSede, pageConfig);				
	
			}	
			if(nDocumento == ""  &&  fechaInicial == "" && fechaFinal == "" && estado == 2) {					
				devoluciones = devolucionDao.findByDevolucionEstado4(idSede, pageConfig);				
	
			}	
			
			if(nDocumento == ""  &&   fechaInicial == "" && fechaFinal == "" && estado == 3) {
				devoluciones = devolucionDao.findByDevolucionEstado1(idSede, pageConfig);
	
			}
			
			if(nDocumento == ""  &&  fechaInicial == "" && fechaFinal == "" && estado == 4) {					
				devoluciones = devolucionDao.findByDevolucionEstado2(idSede, pageConfig);				
	
			}	
			if(nDocumento == ""  &&  fechaInicial == "" && fechaFinal == "" && estado == 5) {					
				devoluciones = devolucionDao.findByDevolucionEstado3(idSede, pageConfig);				
	
			}		
			
			if(nDocumento == ""  &&  fechaInicial == "" && fechaFinal == "" && (estado == 6 || estado == 0)) {					
				devoluciones = devolucionDao.Todo(idSede, pageConfig);				
	
			}			

			for(DevolucionCompras d: devoluciones) {
				List<DevolucionArticulosCompra> articulos = devolucionComprasArticulosDao.findBynDevolucion(d.getId());
				double total = 0;
				for(DevolucionArticulosCompra a: articulos) {
					total += a.getCantidad();
				}
				d.setCantidadTotal(total);
			}
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de devoluciones exitosa");

			respuestaDto.setObjetoRespuesta(devoluciones);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
			
		} catch (RuntimeException e) {
			logger.error("Error al obtener las devoluciones" + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al obtener las devoluciones " + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}
	
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerCantidadDevolucion(){
					
	ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
    Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
	Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
	InformacionUsuario usuarioInformacion = this.usuarioInformacionDao.buscarPorIdUsuario(usuarioAutenticado.getId());
					
	int idSede = usuarioInformacion.getIdOrganizacion().getId(); 

	try {
	 ArrayList<DevolucionCompras> prefijo = this.devolucionDao.obtenerCantidadDevolucion(idSede);	
     RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de prefijos exitosa");
     respuestaDto.setObjetoRespuesta(prefijo);
	     respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
	} catch (Exception e) {
	 logger.error("Error obteniendo usuarios " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
	 RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error obteniendo usuarios");
	 respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	return respuesta;
}
	/* Borrar devolucion */
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public @ResponseBody ResponseEntity<Object> borrarDevolucion(Integer id) {
		
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		
		try {
			this.devolucionDao.delete(devolucionDao.findById(id).get());
			
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} 
		catch (Exception e) {
			Throwable t = e.getCause();
			if (t instanceof ConstraintViolationException) {
				logger.error("El modulo se encuentra asociado en el sistema " + e.getCause());
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, "Error al elminar la devolucion");
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.BAD_REQUEST);
			} 
			else {
				logger.error("Error en el borrado del modulo");
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error al elminar devolucion");
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		return respuesta;
	}
	
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> devolucionesMes(Integer page){
		ResponseEntity<Object> respuesta;
		try {
			Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
			Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
			InformacionUsuario usuarioInformacion = this.usuarioInformacionDao.buscarPorIdUsuario(usuarioAutenticado.getId());
			MaestroValor rol = maestrovalorDao.buscarRol(usuarioInformacion.getIdRol().getId());
			Pageable pageConfig = PageRequest.of(page, 10);			
			

			int idSede = usuarioInformacion.getIdOrganizacion().getId();	
			//int idSede = 11;	
			
			Page<DevolucionCompras> devolucionesMes = devolucionDao.obtenerDevolucionesDelMes(idSede, pageConfig);
			
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de facturas vencidas exitosa",
					devolucionesMes.getContent(),0, devolucionesMes.getTotalElements() + "");

			respuestaDto.setObjetoRespuesta(devolucionesMes);
			respuestaDto.setCategoria(rol.getNombre());
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
			
		} catch (RuntimeException e) {
			logger.error("Error al obtener las facturas vencidas" + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al obtener las facturas vencidas " + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}
	

}

