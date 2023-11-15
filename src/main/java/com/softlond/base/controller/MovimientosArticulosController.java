package com.softlond.base.controller;

import com.softlond.base.constant.ApiConstant;
import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.InformacionUsuario;
import com.softlond.base.entity.Usuario;
import com.softlond.base.repository.UsuarioInformacionDao;
import com.softlond.base.request.BusquedaMovProveedor;
import com.softlond.base.response.MovimientoProveedor;
import com.softlond.base.response.Paginacion;
import com.softlond.base.service.MovimientosArticulosService;
import com.softlond.base.service.MovimientosProveedorService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(ApiConstant.MOVIMIENTOS_ARTICULOS_API)
public class MovimientosArticulosController {

	private final Logger logger = Logger.getLogger(getClass());

	@Autowired
	UsuarioInformacionDao usuarioInformacionDao;

    @Autowired
    private MovimientosArticulosService movimientosArticulosService;

    @PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	@GetMapping(value = ApiConstant.MOVIMIENTOS_ARTICULOS_API_LISTAR_MOVIMIENTOS_ARTICULOS_CONSULTA, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity obtenerMovimientosPorArticuloPaginado(
			@RequestParam int idArticulo, @RequestParam int pagina) {
		RespuestaDto respuestaDto;
		try {
			Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
			Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
			InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
					.buscarPorIdUsuario(usuarioAutenticado.getId());

			int idSede = usuarioInformacion.getIdOrganizacion().getId();
			Paginacion pag = movimientosArticulosService.obtenerMovimientosPorArticuloPaginado(idSede,
					idArticulo, pagina);
			respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
			respuestaDto.setObjetoRespuesta(pag);
			return new ResponseEntity(respuestaDto, HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
			String msj = "Error listando informes de facturas vencidas de compra... " + ex.getMessage();
			logger.error(ex.getStackTrace()[0].getLineNumber() + ex.getMessage());
			respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
			return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
		}
	}

}
