package com.softlond.base.controller;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.softlond.base.constant.ApiConstant;
import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.InformacionUsuario;
import com.softlond.base.entity.Usuario;
import com.softlond.base.repository.UsuarioInformacionDao;
import com.softlond.base.response.Paginacion;
import com.softlond.base.service.InformeFacturasVencidasClienteService;
import com.softlond.base.service.InformeFacturasVencidasService;
import com.softlond.base.service.ProductosExistentesService;

@Controller
@RequestMapping(ApiConstant.PRODUCTOS_EXISTENTES_API)

public class ProductosExistentesController {

	private final Logger logger = Logger.getLogger(getClass());

	@Autowired
	UsuarioInformacionDao usuarioInformacionDao;

	@Autowired
	private ProductosExistentesService productosExistentesService;
	
	
	 @GetMapping(value = ApiConstant.PRODUCTOS_EXISTENTES_API_LISTAR_CONSULTA, produces = MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody ResponseEntity obtenerProductosExistentesFiltros(@PathVariable Integer page, String texto, Integer idClasificacion){
			return this.productosExistentesService.obtenerProductosExistentesFiltros(texto, idClasificacion,
					page);
		}
		
		@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
		@GetMapping(value = ApiConstant.PRODUCTOS_EXISTENTES_API_LISTAR_PRODUCTO_EXISTENTE, produces = MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody ResponseEntity listarProductosExistentesPaginado(@RequestParam int pagina, @RequestParam int idProducto) {
			RespuestaDto respuestaDto;
			try {
				
				Paginacion pag = productosExistentesService.listarProductosExistentesPaginado(idProducto, pagina);
				respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
				respuestaDto.setObjetoRespuesta(pag);
				return new ResponseEntity(respuestaDto, HttpStatus.OK);
			} catch (Exception ex) {
				ex.printStackTrace();
				String msj = "Error listando informes de facturas vencidas de cliente... " + ex.getMessage();
				logger.error(ex);
				respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
				return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
			}
		}
}
