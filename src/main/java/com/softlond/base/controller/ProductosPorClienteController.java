package com.softlond.base.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.softlond.base.constant.ApiConstant;
import com.softlond.base.service.ProductosPorClienteService;

@RestController
@RequestMapping(ApiConstant.PRODUCTO_POR_CLIENTE_API)
public class ProductosPorClienteController {
	
		@Autowired
		public ProductosPorClienteService productosPorClienteService;
	
	
	@GetMapping(value = ApiConstant.PRODUCTO_POR_CLIENTE_API_LISTAR_TODOS_CONSULTA, produces = MediaType.APPLICATION_JSON_VALUE)
	public  @ResponseBody ResponseEntity<Object> obtenerProductoPorClienteConsulta(String fechaInicial, String fechaFinal,  
			Integer cliente, Integer page){
		return this.productosPorClienteService.obtenerProductoPorClienteConsulta(fechaInicial, fechaFinal, cliente, page);
	}

}
