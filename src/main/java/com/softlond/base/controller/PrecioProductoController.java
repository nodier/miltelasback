package com.softlond.base.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.softlond.base.constant.ApiConstant;
import com.softlond.base.entity.Precio;
import com.softlond.base.entity.Producto;
import com.softlond.base.service.PrecioProductoService;

@Controller
@RequestMapping(ApiConstant.PRECIO_PRODUCTO_CONTROL_API)
public class PrecioProductoController {
    private final Logger logger = Logger.getLogger(getClass());

    @Autowired
    private PrecioProductoService precioProductoService;
    
    @GetMapping(value = ApiConstant.PRECIO_PRODUCTO_CONTROL_API_LISTAR_PRECIO, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerPrecioProducto(Integer idProducto ) {
		return precioProductoService.obtenerPrecioProducto(idProducto);
	}
    
    @PostMapping(value = ApiConstant.PRECIO_PRODUCTO_CONTROL_API_CREAR_PRECIO, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> CrearPrecios(@RequestBody Precio precio) {
		return precioProductoService.CrearPrecios(precio);
	}

    @PutMapping(value = ApiConstant.PRECIO_PRODUCTO_CONTROL_API_EDITAR_PRECIO, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> ActualizarPrecios(@RequestBody Precio precio) {
		return precioProductoService.ActualizarPrecios(precio);
	}
}
