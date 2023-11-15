package com.softlond.base.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.softlond.base.constant.ApiConstant;
import com.softlond.base.repository.UsuarioInformacionDao;
import com.softlond.base.service.VentaProductoService;

@Controller
@RequestMapping(ApiConstant.VENTA_PRODUCTO_CONTROL_API)
public class VentaProductoController {
    //private final Logger logger = Logger.getLogger(getClass());
    
	@Autowired
	UsuarioInformacionDao usuarioInformacionDao;

    @Autowired
    private VentaProductoService ventaProductoService;
    
    
	 @GetMapping(value = ApiConstant.VENTA_PRODUCTO_CONTROL_API_LISTAR_CONSULTA, produces = MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody ResponseEntity obtenerVentaProducto(@PathVariable Integer page, String fechaInicial, String fechaFinal, String order, boolean sort){
			return this.ventaProductoService.obtenerVentaProducto(fechaInicial, fechaFinal, order, sort, page);
		}
}
