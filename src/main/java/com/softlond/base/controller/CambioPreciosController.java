package com.softlond.base.controller;

import com.softlond.base.constant.ApiConstant;
import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.Producto;
import com.softlond.base.entity.RemisionVenta;
import com.softlond.base.service.CambioPreciosService;
import com.softlond.base.service.ProductoService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(ApiConstant.CAMBIO_PRECIOS_CONTROL_API)
public class CambioPreciosController {

	private final Logger logger = Logger.getLogger(getClass());

	@Autowired
	private CambioPreciosService cambioPreciosService;
		
		//Listar productos por conuslta avanzada
		@GetMapping(value = ApiConstant.CAMBIO_PRECIOS_CONTROL_API_LISTAR_TODOS_CONSULTA, produces = MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody ResponseEntity obtenerProductosConsulta(@PathVariable Integer page, Integer tipo, Integer referencia, 
				Integer presentacion, Integer color){
			return this.cambioPreciosService.obtenerProductosConsulta(tipo, referencia, presentacion, color, page);
		}
		
		@PostMapping(value = ApiConstant.CAMBIO_PRECIOS_CONTROL_API_GUARDAR_CONSULTA, produces = MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody ResponseEntity GuardarPreciosConsulta(Integer tipo, Integer referencia, 
				Integer presentacion, Integer color, @RequestBody Double precioVenta){
			return this.cambioPreciosService.modificarPreciosConsulta(tipo, referencia, presentacion, color, precioVenta);
		}
}
