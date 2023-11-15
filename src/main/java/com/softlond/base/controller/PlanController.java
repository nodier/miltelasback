package com.softlond.base.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.softlond.base.constant.ApiConstant;
import com.softlond.base.entity.ModulosPorPlan;
import com.softlond.base.entity.Plan;
import com.softlond.base.service.PlanService;

@Controller
@RequestMapping(ApiConstant.PLAN_CONTROLADOR_API)
public class PlanController {

	@Autowired
	private PlanService planService;
	
	
	
	// Listar todos los planes siendo un Super user//
	@GetMapping(value = ApiConstant.PLAN_CONTROLAR_API_SUPER_LISTADO, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerTodos() {
		return planService.obtenerTodos();
	}
		
	/* SE RECIBE UN PLAN PARA LA CREACION DEL MISMO */
	@PostMapping(value = ApiConstant.PLAN_CONTROLADOR_API_CREAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> crearPlan(@RequestBody Plan nuevoPlan, HttpServletRequest request, HttpServletResponse response) {
		return this.planService.crearPlan(nuevoPlan);
	}
	
	/* SE RECIBE UN PLAN PARA LA ACTUALIZACIÖN DEL MISMO */
	@PostMapping(value = ApiConstant.PLAN_CONTROLAR_API_ACTUALIZAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> actualizarPlan(@RequestBody Plan newPlan, HttpServletRequest request, HttpServletResponse response) {
		return this.planService.actualizarPlan(newPlan);
	}

	/* SE RECIBE EL ID DE UN PLAN PARA EL BORRADO DEL MISMO */
	@DeleteMapping(value = ApiConstant.PLAN_CONTROLADOR_API_BORRAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> borrarPlan(Integer planId, HttpServletRequest request, HttpServletResponse response) {
		return this.planService.borrarPlan(planId);
	}
	
	/* METODO PARA EL EMPAREJADO DE UN PLAN Y SUS MODULOS */
	@PostMapping(value = ApiConstant.PLAN_CONTROLADOR_API_SUPER_ENLAZAR_MODULO, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> enlazarModulo(@RequestBody ModulosPorPlan planModulos, HttpServletRequest request, HttpServletResponse response) {
		return this.planService.enlazarModulo(planModulos);
	}

	/* METODO PARA EL DESEMPAREJADO DE UN MODULO Y LOS MENU */
	@PostMapping(value = ApiConstant.PLAN_CONTROLADOR_API_SUPER_DESENLAZAR_PLAN_MODULO, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> desenlazarModulos(@RequestBody ModulosPorPlan planModule, HttpServletRequest request, HttpServletResponse response) {
		return this.planService.desenlazarModulos(planModule);
	}

	// Listar todos modulos de un plan//
	@GetMapping(value = ApiConstant.PLAN_CONTROLADOR_API_LIST_MODULOS_POR_PLAN, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> listarModulosPlan(Long planId) {
		return this.planService.listarModulosPlan(planId);
	}

}
