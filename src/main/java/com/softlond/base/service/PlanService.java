package com.softlond.base.service;

import java.util.Date;

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
import com.softlond.base.entity.ModulosPorPlan;
import com.softlond.base.entity.Plan;
import com.softlond.base.entity.Usuario;
import com.softlond.base.repository.ModulesByPlanDao;
import com.softlond.base.repository.ModuloDao;
import com.softlond.base.repository.PlanDao;

@Service
public class PlanService {

	private static final Logger logger = Logger.getLogger(PlanService.class);

	@Autowired
	PlanDao planDao;

	@Autowired
	private ModulesByPlanDao planModuloDao;

	@Autowired
	private ModuloDao moduloDao;

	
	// Listar todos los Planes
	@PreAuthorize("hasAuthority('SUPER')")
	public ResponseEntity<Object> obtenerTodos() {
		
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		
		try {
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito listando los planes");
			respuestaDto.setObjetoRespuesta(this.planDao.findAllByOrderByFechaCreacionDesc());
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} 
		catch (Exception e) {
			logger.error("Error en el listar de planes");
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error al listar los planes");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return respuesta;
	}
		
	/* SE RECIBE UN PLAN PARA LA CREACION DEL MISMO */
	@PreAuthorize("hasAuthority('SUPER')")
	public ResponseEntity<Object> crearPlan(Plan nuevoPlan) {
		
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		
		try {
			if (!existePorNombre(nuevoPlan.getNombre())) {
				
				nuevoPlan.setFechaCreacion(new Date());
				nuevoPlan.setIdCreador(usuarioAutenticado);
				
				this.planDao.save(nuevoPlan);
				
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito creando el plan");
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
			} 
			else {
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, "Error, el nombre del plan ya existe");
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.BAD_REQUEST);
			}
		} 
		catch (Exception e) {
			logger.error("Error en la creacion del plan");
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error en la creacion del plan");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return respuesta;
	}


	/* SE RECIBE UN PLAN PARA LA ACTUALIZACION DEL MISMO */
	@PreAuthorize("hasAuthority('SUPER')")
	public ResponseEntity<Object> actualizarPlan(Plan nuevoPlan) {
		
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		
		try {
			Plan planBD = this.planDao.findById(nuevoPlan.getId()).get();
			
			if (!planBD.getNombre().equals(nuevoPlan.getNombre())) {
				
				if (existePorNombre(nuevoPlan.getNombre())) {
					RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, "Error nombre del plan ya existe");
					respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.BAD_REQUEST);
				}
				else {
					nuevoPlan.setFechaActualizacion(new Date());
					nuevoPlan.setIdUltimoUsuarioModifico(usuarioAutenticado);
					this.planDao.save(nuevoPlan);
					
					RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito actualizar el plan");
					respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
				}
			}
			else {
				nuevoPlan.setFechaActualizacion(new Date());
				nuevoPlan.setIdUltimoUsuarioModifico(usuarioAutenticado);
				this.planDao.save(nuevoPlan);
				
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito actualizar el plan");
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
			}
		} 
		catch (Exception e) {
			logger.error("Error en la creacion del plan");
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error en la creacion del plan");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return respuesta;
	}

	
	/* SE RECIBE EL ID DE UN Plan PARA EL BORRADO DEL MISMO */
	@PreAuthorize("hasAuthority('SUPER')")
	public @ResponseBody ResponseEntity<Object> borrarPlan(Integer id) {
		
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		
		try {
			Plan auxPlan = this.planDao.findById(id).get();
			this.planDao.delete(auxPlan);
			
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} 
		catch (Exception e) {
			Throwable t = e.getCause();
			
			if (t instanceof ConstraintViolationException) {
				logger.error("El plan se encuentra asociado en el sistema");
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, "El Plan se encuentra asociado en el sistema");
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.BAD_REQUEST);
			}
			else {
				logger.error("Error en el borrado del plan");
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error en el borrado del plan");
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		return respuesta;
	}
	

	/* METODO PARA EL EMPAREJADO DE UN PLAN Y SUS MODULOS */
	@PreAuthorize("hasAuthority('SUPER')")
	public @ResponseBody ResponseEntity<Object> enlazarModulo(@RequestBody ModulosPorPlan planModulos) {
		
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		
		if (!existeRelacion(planModulos.getIdPlan().getId(), planModulos.getIdModulo().getId())) {
			
			try {
				planModulos.setFechaCreacion(new Date());
				this.planModuloDao.save(planModulos);
				
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito");
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
			} 
			catch (Exception e) {
				logger.error("Error en la creacion del enlaze modulo - plan", e);
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error en la creacion del enlaze modulo - plan");
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		else {
			logger.error("Error en la creacion del emparejamiento del modulo y el plan");
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, "Error en la creacion del emparejamiento del modulo y el plan");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.BAD_REQUEST);
		}

		return respuesta;
	}

	
	// Listar todos modulos de un plan//
	@PreAuthorize("hasAuthority('SUPER')")
	public @ResponseBody ResponseEntity<Object> listarModulosPlan(Long planId) {
		
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		
		try {		
			
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito");
			respuestaDto.setObjetoRespuesta(this.moduloDao.buscarModulosPorPlan(planId));
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} 
		catch (Exception e) {
			logger.error("Error se presentan problemas al cargar los modules por planes " + e);
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error se presentan problemas al cargar los modulos del plan");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return respuesta;
	}
		
	
	/* METODO PARA EL DESEMPAREJADO DE UN MODULO Y LOS MENU */
	@PreAuthorize("hasAuthority('SUPER')")
	public @ResponseBody ResponseEntity<Object> desenlazarModulos(@RequestBody ModulosPorPlan planModulo) {
		
		ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
		
		try {
			ModulosPorPlan auxPlanModulo = getNewPlanModules();
			auxPlanModulo = this.planModuloDao.findByIdPlanAndIdModulo(this.planDao.findById(planModulo.getIdPlan().getId()).get(),
			this.moduloDao.findById(planModulo.getIdModulo().getId()).get());
			
			this.planModuloDao.delete(auxPlanModulo);
			
			RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
		} 
		catch (Exception e) {
			
			Throwable t = e.getCause();
			if (t instanceof ConstraintViolationException) {
				logger.error("La relación se encuentra asociada en el sistema" + e.getCause());
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, "La relación se encuentra asociada en el sistema");
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.BAD_REQUEST);
			}
			else {
				logger.error("Error en el desemparejado del plan y el modulo." + e);
				RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error en el desemparejado del plan y el modulo");
				respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		return respuesta;
	}



	

	// Adicionado para retornar instancia Module By Plan
	public ModulosPorPlan getNewPlanModules() {
		return new ModulosPorPlan();
	}

	/* Verificación de si hay una relación entre un plan y un módulo */
	public boolean existeRelacion(Integer idPlan, Integer idModulo) {
		
		boolean existe = false;
		
		if (planModuloDao.buscarModuloPorPlan(idPlan, idModulo) != null) {
			existe = true;
		}
		
		return existe;
	}

	// Adicionado para retornar instancia Plan
	public Plan getNewPlan() {
		return new Plan();
	}

	// Metodo que recibe un nombre para el Plan y responde con un boolean si el plan ya esta almacenado en la BD
	public boolean existePorNombre(String nombrePlan) {
		
		boolean existe = false;
		
		if (planDao.findByNombre(nombrePlan) != null) {
			existe = true;
		} 
		
		return existe;
	}

}