package com.softlond.base.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;

import com.softlond.base.constant.ApiConstant;
import com.softlond.base.service.DevolucionComprasArticulosService;

@RestController
@RequestMapping(ApiConstant.ARTICULOS_DEVOLUCION_COMPRA_API)
public class DevolucionComprasArticulosController {
	
	@Autowired
	public DevolucionComprasArticulosService devolucionComprasArticulosService;
	

		@GetMapping(value = ApiConstant.ARTICULOS_DEVOLUCION_COMPRA_API_LISTAR, produces = MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody ResponseEntity<Object> findBynDevolucion(Integer idDev){
			return devolucionComprasArticulosService.findBynDevolucion(idDev);
		}

}


