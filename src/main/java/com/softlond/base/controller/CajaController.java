package com.softlond.base.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.softlond.base.constant.ApiConstant;
import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.Caja;
import com.softlond.base.service.CajaService;


@RestController
@RequestMapping(ApiConstant.CAJA_CONTROL_API)
public class CajaController {
	
	@Autowired
	private CajaService cajaService;

	@GetMapping(value = ApiConstant.CAJA_CONTROL_API_GET_ALL_SEDE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getSpecialties(@RequestParam Integer id){
		return this.cajaService.getMyCajas(id);
	}
	
	@GetMapping(value = ApiConstant.CAJA_CONTROL_API_GET_ALL, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getSpecialties(){
		return this.cajaService.getMyCajas();
	}
	
	@PostMapping(value = ApiConstant.CAJA_CONTROL_API_ADD, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> postSpecialties(@RequestBody ArrayList<Caja> specialties){
		return this.cajaService.postCajas(specialties);
	}
	
	@DeleteMapping(value = ApiConstant.CAJA_CONTROL_API_REMOVE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> deleteSpecialties(@RequestParam Integer id){
		return this.cajaService.deleteCajas(id);
	}
	
	@PutMapping(value = ApiConstant.CAJA_CONTROL_API_UPDATE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> updateSpecialties(@Valid @RequestBody Caja caja, BindingResult result, @RequestParam Integer id){
		if(result.hasErrors()) {
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err->"El campo '" + err.getField() + "' " + err.getDefaultMessage())
					.collect(Collectors.toList());
			 ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.INTERNAL_SERVER_ERROR);
			 RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error en los datos");
			respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
			return respuesta;
		}
		return this.cajaService.updateCajas(caja, id);
	}
	
	@GetMapping(value = ApiConstant.CAJA_CONTROL_API_CAJAS_SEDE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getCajasSede(){
		return this.cajaService.getCajasSede();
	}
	
	@GetMapping(value = ApiConstant.CAJA_CONTROL_API_CAJAS_SEDE_INFORME, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getCajasSedeInforme(String idSede){
		return this.cajaService.getCajasSedeInforme(idSede);
	}
	
}
