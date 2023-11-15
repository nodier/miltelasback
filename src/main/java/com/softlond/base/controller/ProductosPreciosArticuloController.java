package com.softlond.base.controller;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.softlond.base.constant.ApiConstant;
import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.InformacionUsuario;
import com.softlond.base.entity.Usuario;
import com.softlond.base.repository.UsuarioInformacionDao;
import com.softlond.base.request.BusquedaMovProveedor;
import com.softlond.base.request.BusquedaProdNoExistente;
import com.softlond.base.response.MovimientoProveedor;
import com.softlond.base.response.Paginacion;
import com.softlond.base.response.ProductosNoExistentes;
import com.softlond.base.service.InformeFacturasVencidasClienteService;
import com.softlond.base.service.InformeFacturasVencidasService;
import com.softlond.base.service.ProductosCantidadArticuloService;
import com.softlond.base.service.ProductosExistentesService;
import com.softlond.base.service.ProductosNoExistentesService;
import com.softlond.base.service.ProductosPreciosArticuloService;

@Controller
@RequestMapping(ApiConstant.PRODUCTOS_PRECIOS_ARTICULO_API)

public class ProductosPreciosArticuloController {

	private final Logger logger = Logger.getLogger(getClass());

	@Autowired
	UsuarioInformacionDao usuarioInformacionDao;

	@Autowired
	private ProductosPreciosArticuloService productosPreciosArticuloService;
	
	
    @GetMapping(value = ApiConstant.PRODUCTOS_PRECIOS_ARTICULO_API_LISTAR_CONSULTA, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity obtenerProductosPreciosArticuloFiltros
			(@PathVariable Integer page, String fechaInicial, String fechaFinal, Integer tipo,
		    		Integer referencia, Integer presentacion, Integer clasificacion, Integer color, 
		    		Integer estado, String order, boolean sort){
		return this.productosPreciosArticuloService.obtenerProductosPreciosArticuloFiltros(fechaInicial, fechaFinal, tipo, referencia, 
				presentacion, clasificacion,  color, estado, order, sort, page);
	}
    
    @GetMapping(value = ApiConstant.PRODUCTOS_PRECIOS_ARTICULO_API_LISTAR_CONSULTA_PAGINADO, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity obtenerProductosPreciosArticuloFiltrosPaginado
			(@PathVariable Integer page){
		return this.productosPreciosArticuloService.obtenerProductosPreciosArticuloFiltrosPaginado(page);
	}
		
}
