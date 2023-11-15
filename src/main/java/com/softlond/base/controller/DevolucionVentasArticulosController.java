package com.softlond.base.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;

import com.softlond.base.constant.ApiConstant;
import com.softlond.base.service.DevolucionComprasArticulosService;
import com.softlond.base.service.DevolucionVentaService;
import com.softlond.base.service.DevolucionVentasArticulosService;

@RestController
@RequestMapping(ApiConstant.ARTICULOS_DEVOLUCION_VENTA_API)
public class DevolucionVentasArticulosController {
	
	private static final Logger logger = Logger.getLogger(DevolucionVentaService.class);
	
	@Autowired
	public DevolucionVentasArticulosService devolucionVentasArticulosService;
	

		@GetMapping(value = ApiConstant.ARTICULOS_DEVOLUCION_VENTA_API_LISTAR, produces = MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody ResponseEntity<Object> findBynDevolucion(Integer idDev){
			return devolucionVentasArticulosService.findBynDevolucion(idDev);
		}

}


