package com.softlond.base.service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.ArticulosRemisionCompra;
import com.softlond.base.entity.Factura;
import com.softlond.base.entity.FacturaArticulos;
import com.softlond.base.entity.InformacionUsuario;
import com.softlond.base.entity.Organizacion;
import com.softlond.base.entity.Prefijo;
import com.softlond.base.entity.RemisionCompra;
import com.softlond.base.entity.Usuario;
import com.softlond.base.repository.ArticulosRemisionCompraDao;
import com.softlond.base.repository.RemisionCompraDao;
import com.softlond.base.repository.UsuarioInformacionDao;

@Service
public class RemisionCompraService {

	private static final Logger logger = Logger.getLogger(RemisionCompraService.class);

	@Autowired
	private RemisionCompraDao remisionCompraDao;

	public RemisionCompraDao getRemisionCompraDao() {
		return remisionCompraDao;
	}

	public void setRemisionCompraDao(RemisionCompraDao remisionCompraDao) {
		this.remisionCompraDao = remisionCompraDao;
	}

	@Autowired
	UsuarioInformacionDao usuarioInformacionDao;

	@Autowired
	public ArticulosRemisionCompraDao articulosRemisionCompraDao;

	// Crear
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> crearRemision(@RequestBody RemisionCompra remision) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();

		try {
			remision.setIdCreador(usuarioAutenticado);
			if (remisionCompraDao.buscarNumeroRemisionYProveedor(remision.getNumeroRemision(), remision.getIdProveedor().getId()) != null) {
				logger.error("Error en la creación de la remision, remision existente");
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST,
						"Error en la creación de la remisión, remision existente");
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
				return respuesta;
			}
			RemisionCompra guardado = this.remisionCompraDao.save(remision);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito creando remision");
			respuestaDto.setObjetoRespuesta(guardado);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);

		} catch (Exception e) {
			logger.error("Error en la creación de la remision" + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error en la creación de la remisión "+e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return respuesta;
	}

	// Obtener por numero de remision
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerRemisionCodigo(String idRemision, Integer proveedor) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		try {
			Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
			Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
			InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
					.buscarPorIdUsuario(usuarioAutenticado.getId());

			int idSede = usuarioInformacion.getIdOrganizacion().getId();

			RemisionCompra remisionDatos = this.remisionCompraDao.findByIdNumeroRemision(proveedor, idRemision);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de remision exitosa");
			respuestaDto.setObjetoRespuesta(remisionDatos);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo remision " + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error obteniendo remision");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return respuesta;

	}

	// Listar remisiones segun filtros
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerRemisionesFiltros(Integer idProveedor, String numeroRemision,
			String fechaInicial, String fechaFinal, String estado, Integer page) throws Exception {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Integer suma = 0;	
		
		try {
			Page<RemisionCompra> remisiones = null;
			Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
			Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
			InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
					.buscarPorIdUsuario(usuarioAutenticado.getId());

			Pageable pageConfig = PageRequest.of(page, 10);

			int idSede = usuarioInformacion.getIdOrganizacion().getId();

			if (idProveedor != 0 && numeroRemision.equals("") && fechaInicial.equals("") && fechaFinal.equals("") && estado.equals("")) {
								remisiones = remisionCompraDao.findByProveedorSede(idSede, idProveedor, pageConfig);
				Double sumaprueba1 = this.remisionCompraDao.suma1(idSede, idProveedor);
				double d_val = sumaprueba1;
		        int i_val = (int) Math.round(d_val);		      
		        suma = i_val;
			}

			if (idProveedor != 0 && !numeroRemision.equals("") && fechaInicial.equals("") && fechaFinal.equals("") && estado.equals("")) {
				remisiones = remisionCompraDao.findByProveedorNumeroRemision(idSede, idProveedor, numeroRemision,pageConfig);
				Double sumaprueba = this.remisionCompraDao.suma2(idSede, idProveedor, numeroRemision);
				double d_val = sumaprueba;
		        int i_val = (int) Math.round(d_val);		       
		        suma = i_val;
			}

			if (idProveedor != 0 && !numeroRemision.equals("") && fechaInicial.equals("") && fechaFinal.equals("") && !estado.equals("")) {
				remisiones = remisionCompraDao.findByProveedorNumeroRemisionEstado(idSede, idProveedor, numeroRemision,
						estado, pageConfig);
				Double sumaprueba = this.remisionCompraDao.suma10(idSede, idProveedor, numeroRemision, estado);
				double d_val = sumaprueba;
		        int i_val = (int) Math.round(d_val);		       
		        suma = i_val;
			}

			if (idProveedor != 0 && numeroRemision.equals("") && !fechaInicial.equals("") && fechaFinal.equals("") && estado.equals("")) {
				remisiones = remisionCompraDao.findByProveedorFechaInicial(idSede, idProveedor, fechaInicial,
						pageConfig);
				Double sumaprueba = this.remisionCompraDao.suma3(idSede, idProveedor, fechaInicial);
				double d_val = sumaprueba;
		        int i_val = (int) Math.round(d_val);		       
		        suma = i_val;
			}

			if (idProveedor != 0 && numeroRemision.equals("") && !fechaInicial.equals("") && !fechaFinal.equals("") && estado.equals("")) {
				remisiones = remisionCompraDao.findByProveedorFechas(idSede, idProveedor, fechaInicial, fechaFinal,pageConfig);
				Double sumaprueba = this.remisionCompraDao.suma4(idSede, idProveedor, fechaInicial, fechaFinal);
				double d_val = sumaprueba;
		        int i_val = (int) Math.round(d_val);		       
		        suma = i_val;
			}

			if (idProveedor != 0 && numeroRemision.equals("") && fechaInicial.equals("") && fechaFinal.equals("") && !estado.equals("")) {
				remisiones = remisionCompraDao.findByProveedorEstado(idSede, idProveedor, estado, pageConfig);
				Double sumaprueba = this.remisionCompraDao.suma5(idSede, idProveedor, estado);
				double d_val = sumaprueba;
		        int i_val = (int) Math.round(d_val);		       
		        suma = i_val;
			}

			if (idProveedor != 0 && !numeroRemision.equals("") && !fechaInicial.equals("") && !fechaFinal.equals("") && !estado.equals("")) {
				remisiones = remisionCompraDao.findByProveedorNumeroRemisionEstadoFechas(idSede, idProveedor,numeroRemision, estado, fechaInicial, fechaFinal, pageConfig);
				Double sumaprueba = this.remisionCompraDao.suma11(idSede, idProveedor, numeroRemision, estado, fechaInicial,fechaFinal);
				double d_val = sumaprueba;
		        int i_val = (int) Math.round(d_val);		       
		        suma = i_val;
			}

			if (idProveedor == 0 && !numeroRemision.equals("") && fechaInicial.equals("") && fechaFinal.equals("") && estado.equals("")) {
				remisiones = remisionCompraDao.findNumeroRemision(idSede, numeroRemision, pageConfig);
				Double sumaprueba = this.remisionCompraDao.suma6(idSede, numeroRemision);
				double d_val = sumaprueba;
		        int i_val = (int) Math.round(d_val);		      
		        suma = i_val;
			}

			if (idProveedor == 0 && !numeroRemision.equals("") && fechaInicial.equals("") && fechaFinal.equals("") && !estado.equals("")) {
				remisiones = remisionCompraDao.findNumeroRemisionEstado(idSede, numeroRemision, estado, pageConfig);
				Double sumaprueba = this.remisionCompraDao.suma7(idSede, numeroRemision, estado);
				double d_val = sumaprueba;
		        int i_val = (int) Math.round(d_val);		       
		        suma = i_val;
			}

			if (idProveedor == 0 && numeroRemision.equals("") && !fechaInicial.equals("") && fechaFinal.equals("") && estado.equals("")) {
				remisiones = remisionCompraDao.findByFechaInicial(idSede, fechaInicial, pageConfig);
				Double sumaprueba = this.remisionCompraDao.suma8(idSede, fechaInicial);
				double d_val = sumaprueba;
		        int i_val = (int) Math.round(d_val);		      
		        suma = i_val;
			}

			if (idProveedor == 0 && numeroRemision.equals("") && !fechaInicial.equals("") && !fechaFinal.equals("") && estado.equals("")) {
				remisiones = remisionCompraDao.findByFechas(idSede, fechaInicial, fechaFinal, pageConfig);
				Double sumaprueba = this.remisionCompraDao.suma9(idSede, fechaInicial, fechaFinal);			
				double d_val = sumaprueba;
		        int i_val = (int) Math.round(d_val);		       
		        suma = i_val;
			}

			if (idProveedor == 0 && numeroRemision.equals("") && !fechaInicial.equals("") && !fechaFinal.equals("") && !estado.equals("")) {
				remisiones = remisionCompraDao.findByFechaInicialEstado(idSede, fechaInicial, estado, pageConfig);
				Double sumaprueba = this.remisionCompraDao.suma10(idSede, fechaInicial, estado);
				double d_val = sumaprueba;
		        int i_val = (int) Math.round(d_val);		       
		        suma = i_val;
			}

			if (idProveedor == 0 && numeroRemision.equals("") && fechaInicial.equals("") && fechaFinal.equals("") && !estado.equals("")) {				
				remisiones = remisionCompraDao.findByEstado(idSede, estado, pageConfig);
				Double sumaprueba = this.remisionCompraDao.suma14(idSede, estado);
				double d_val = sumaprueba;
		        int i_val = (int) Math.round(d_val);		      
		        suma = i_val;
			}

			if (idProveedor == 0 && numeroRemision.equals("") && !fechaInicial.equals("") && !fechaFinal.equals("") && !estado.equals("")) {				
				remisiones = remisionCompraDao.findByFechasEstado(idSede, fechaInicial, fechaFinal, estado, pageConfig);
				Double sumaprueba = this.remisionCompraDao.suma12(idSede, fechaInicial, fechaFinal, estado);
				double d_val = sumaprueba;
		        int i_val = (int) Math.round(d_val);		       
		        suma = i_val;
			}
			
			if (idProveedor == 0 && numeroRemision.equals("") && fechaInicial.equals("") && fechaFinal.equals("") && estado.equals("")) {				
				remisiones = remisionCompraDao.findByEstadoTodos(idSede, pageConfig);
				Double sumaprueba = this.remisionCompraDao.suma13(idSede);						
				double d_val = sumaprueba;
		        int i_val = (int) Math.round(d_val);		       
		        suma = i_val;
			}		
		
			if(remisiones != null) {				
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de remisiones exitosa",remisiones.getContent(), suma, remisiones.getTotalElements() + "");
				respuestaDto.setObjetoRespuesta(remisiones);
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
			}else {
								RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, "sin_remision");
				respuestaDto.setObjetoRespuesta(remisiones);
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.BAD_REQUEST);
			}
			

			

		} catch (RuntimeException e) {
			logger.error("Error al obtener las remisiones" + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al obtener las remisiones " + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	// Listar remisiones mes
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerRemisionesMes(Integer page) throws Exception {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		Integer suma = 0;
		try {
			Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
			Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
			InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
					.buscarPorIdUsuario(usuarioAutenticado.getId());
			Pageable pageConfig = PageRequest.of(page, 10);
			int idSede = usuarioInformacion.getIdOrganizacion().getId();
			Page<RemisionCompra> remisiones = remisionCompraDao.obtenerRemisionesDelMes(idSede, pageConfig);
			Double sumaprueba = remisionCompraDao.totalMes(idSede);
			if(sumaprueba!=null) {
				double d_val = sumaprueba;
		        int i_val = (int) Math.round(d_val);		      
		        suma = i_val;
			}
			
	        
	        if(remisiones != null) {	        	
	        	RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de remisiones exitosa",remisiones.getContent(), suma, remisiones.getTotalElements() + "");
				respuestaDto.setObjetoRespuesta(remisiones);
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
	        }else {	        	
	        	
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, "sin_remision");
				respuestaDto.setObjetoRespuesta(remisiones);
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.BAD_REQUEST);
	        }
			

		} catch (RuntimeException e) {
			logger.error("Error al obtener las remisiones" + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,"Error al obtener las remisiones " + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> actualizarRemision(@RequestBody RemisionCompra remision) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		RemisionCompra remisionEnBd = this.remisionCompraDao.findById(remision.getId()).get();
		try {
			remision.setId(remisionEnBd.getId());
			articulosRemisionCompraDao.eliminarArticulosRemision(remision.getId());
			RemisionCompra actualizada = this.remisionCompraDao.save(remision);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito actualizando la Remisión");
			respuestaDto.setObjetoRespuesta(actualizada);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);

		} catch (Exception e) {
			logger.error("Error en la actualización la Remisión");
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error en la actualización del prefijo");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return respuesta;
	}

	// Eliminar
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> borrarRemision(@RequestParam Integer idRemision) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		try {
			List<ArticulosRemisionCompra> articulosRemision = this.articulosRemisionCompraDao
					.findByRemision(idRemision);

			for (ArticulosRemisionCompra articulos : articulosRemision) {
				this.articulosRemisionCompraDao
						.delete(this.articulosRemisionCompraDao.findById(articulos.getId()).get());
			}
			this.remisionCompraDao.delete(this.remisionCompraDao.findById(idRemision).get());

			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			Throwable t = e.getCause();

			if (t instanceof ConstraintViolationException) {
				logger.error("se encuentra asociada en el sistema " + e.getCause());
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST,
						"Error,  se encuentra asociada en el sistema");
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.BAD_REQUEST);
			} else {
				logger.error("Error en el borrado del prefijo");
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
						"Error en el borrado del prefijo");
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

		return respuesta;
	}

	private boolean existeRemision(RemisionCompra remision) {

		boolean flag = false;

		if (this.remisionCompraDao.findByNumeroRemision(remision.getNumeroRemision()) != null) {
			flag = true;
		}

		return flag;
	}

	// Obtener todas las remision
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerRemisiones(Integer idProveedor) {		
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao.buscarPorIdUsuario(usuarioAutenticado.getId());
		int idSede = usuarioInformacion.getIdOrganizacion().getId();
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);		
		try {            
			List<RemisionCompra> remisiones = this.remisionCompraDao.obtenerRemisionesPendientes(idProveedor, idSede);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de remisiones exitosa");
			respuestaDto.setObjetoRespuesta(remisiones);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo remision " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,"Error obteniendo remisiones");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	// Obtener todas las remision de uan factura
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerRemisionesFactura(Integer idFactura) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

		try {

			List<RemisionCompra> remisiones = this.remisionCompraDao.obtenerRemisionesFactura(idFactura);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de remisiones exitosa");
			respuestaDto.setObjetoRespuesta(remisiones);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo remision " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error obteniendo remisiones");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}

	// Obtener por fecha actual
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> remisionFechaActual(Integer page) {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		String fechab = "" + LocalDate.now();
		Integer suma = 0;
		try {
			Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
			Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
			InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
					.buscarPorIdUsuario(usuarioAutenticado.getId());

			int idSede = usuarioInformacion.getIdOrganizacion().getId();
			Pageable pageConfig = PageRequest.of(page, 10);

			Page<RemisionCompra> remisiones = this.remisionCompraDao.findByFechaInicial(idSede, fechab, pageConfig);
			Double sumaprueba = this.remisionCompraDao.suma8(idSede, fechab);
			double d_val = sumaprueba;
	        int i_val = (int) Math.round(d_val);	       
	        suma = i_val;

			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de facturas exitosa",
					remisiones.getContent(), suma, remisiones.getTotalElements() + "");

			respuestaDto.setObjetoRespuesta(remisiones);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error obteniendo remision " + e + " Linea error: " + e.getStackTrace()[0].getLineNumber());
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,"Error obteniendo remisiones");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}
	
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
	public ResponseEntity<Object> obtenerRemisionesEntrada(Integer idProveedor, String numeroRemision, Integer page) throws Exception {

		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		try {
			Page<RemisionCompra> remisiones = null;
			Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
			Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
			InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
					.buscarPorIdUsuario(usuarioAutenticado.getId());

			Pageable pageConfig = PageRequest.of(page, 10);
			int idSede = usuarioInformacion.getIdOrganizacion().getId();
			if(idProveedor != 0 && numeroRemision.equals("null")) {
				remisiones = remisionCompraDao.findByProveedorSedeNoAsignada(idSede, idProveedor, pageConfig);
			}
			else if(idProveedor == 0 && !numeroRemision.equals("null")){
				remisiones = remisionCompraDao.findByNRemisionSedeNoAsignada(idSede, numeroRemision, pageConfig);
			}
			else if(idProveedor != 0 && !numeroRemision.equals("null")){
				remisiones = remisionCompraDao.findByNRemisionSedeAndProveedorNoAsignada(idSede, numeroRemision, idProveedor, pageConfig);
			}
			else{
				remisiones = remisionCompraDao.findByRemisionSedeNoAsignada(idSede, pageConfig);
			}
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de remisiones exitosa");
			respuestaDto.setObjetoRespuesta(remisiones);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);

		} catch (RuntimeException e) {
			logger.error("Error al obtener las remisiones" + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error al obtener las remisiones " + e);
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
	}
}

