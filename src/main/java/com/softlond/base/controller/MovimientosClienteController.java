package com.softlond.base.controller;

import com.softlond.base.constant.ApiConstant;
import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.InformacionUsuario;
import com.softlond.base.entity.Usuario;
import com.softlond.base.repository.UsuarioInformacionDao;
import com.softlond.base.request.BusquedaMovCliente;
import com.softlond.base.request.BusquedaMovProveedor;
import com.softlond.base.response.MovimientoCliente;
import com.softlond.base.response.MovimientoProveedor;
import com.softlond.base.response.Paginacion;
import com.softlond.base.service.MovimientosClienteService;
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
@RequestMapping(ApiConstant.MOVIMIENTOS_CLIENTES_API)
public class MovimientosClienteController {

	private final Logger logger = Logger.getLogger(getClass());

	@Autowired
	UsuarioInformacionDao usuarioInformacionDao;

	@Autowired
	MovimientosClienteService servicioMovimientos;

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	@GetMapping(value = ApiConstant.MOVIMIENTOS_CLIENTES_API_LISTAR_MOVIMIENTOS_CONSULTA, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity listarmovimientosClienteConsultaPaginado(@RequestParam int idCliente,
			@RequestParam String fechaInicial, @RequestParam String fechaFinal, @RequestParam String numeroDocumento,
			@RequestParam int pagina) {
		RespuestaDto respuestaDto;
		try {
			Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
			Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
			InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
					.buscarPorIdUsuario(usuarioAutenticado.getId());

			int idSede = usuarioInformacion.getIdOrganizacion().getId();
			Paginacion pag = servicioMovimientos.listarmovimientosClienteConsultaPaginado(idSede, idCliente,
					fechaInicial, fechaFinal, numeroDocumento, pagina);
			Integer descuentos = servicioMovimientos.getTotalDescuentos();
			respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
			respuestaDto.setObjetoRespuesta(pag);
			respuestaDto.setVariable(String.valueOf(
					servicioMovimientos.getTotalAbono()));
			respuestaDto.setSuma(descuentos);
			return new ResponseEntity(respuestaDto, HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
			String msj = "Error listando informes de movimientos de clientes... " + ex.getMessage();
			logger.error(ex + "linea " + ex.getStackTrace()[0].getLineNumber());
			respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
			return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
		}
	}
}
