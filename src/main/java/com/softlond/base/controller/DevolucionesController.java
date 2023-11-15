package com.softlond.base.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.softlond.base.constant.ApiConstant;
import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.Producto;
import com.softlond.base.service.DevolucionesService;

@RestController
@RequestMapping(ApiConstant.FACTURA_DEVOLUCION_API)
public class DevolucionesController {
			
		@Autowired
		public DevolucionesService devolucionesService;

		@GetMapping(value = ApiConstant.FACTURA_DEVOLUCION_API_LISTAR, produces = MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody ResponseEntity<Object> reporteMovimientos( String nDocumento, String fechaInicial, String fechaFinal, Integer estado, Integer page)
			 throws Exception {
			return devolucionesService.devoluciones(nDocumento, fechaInicial, fechaFinal, estado, page);
		}
		
		/* Borrar Devolucion */
		@DeleteMapping(value = ApiConstant.FACTURA_DEVOLUCION_API_ELIMINAR, produces = MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody ResponseEntity<Object> borrarDevolucion(Integer id) {
			return devolucionesService.borrarDevolucion(id);
		}
		
		@GetMapping(value = ApiConstant.FACTURA_DEVOLUCION_API_LISTAR_POR_MES, produces = MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody ResponseEntity<Object> devolucionesMes(Integer page)
			 throws Exception {
			return devolucionesService.devolucionesMes(page);
		}
		
		
	
}