package com.softlond.base.controller;

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
import com.softlond.base.service.ProductosExistentesService;
import com.softlond.base.service.ProductosNoExistentesService;

@Controller
@RequestMapping(ApiConstant.PRODUCTOS_NO_EXISTENTES_API)

public class ProductosNoExistentesController {

	private final Logger logger = Logger.getLogger(getClass());

	@Autowired
	UsuarioInformacionDao usuarioInformacionDao;

	@Autowired
	private ProductosNoExistentesService productosNoExistentesService;
	
	
    @GetMapping(value = ApiConstant.PRODUCTOS_NO_EXISTENTES_API_LISTAR_CONSULTA, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity obtenerProductosNoExistentesFiltros
			(@PathVariable Integer page, String idProducto, Integer idClasificacion){
		return this.productosNoExistentesService.obtenerProductosNoExistentesFiltros(idProducto, idClasificacion,
				page);
	}
		
}
