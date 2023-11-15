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

@Controller
@RequestMapping(ApiConstant.INFORME_FACTURAS_VENCIDAS_CLIENTE_API)

public class InformeFacturasVencidasClienteController {

	private final Logger logger = Logger.getLogger(getClass());

	@Autowired
	UsuarioInformacionDao usuarioInformacionDao;

	@Autowired
	private InformeFacturasVencidasClienteService informeFacturasVencidasClienteService;

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	@GetMapping(value = ApiConstant.INFORME_FACTURAS_VENCIDAS_CLIENTE_API_LISTAR_VENCIDAS_CLIENTE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity listarinformeFacturasVencidasClientePaginado(@RequestParam int pagina) {
		RespuestaDto respuestaDto;
		try {
			Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
			Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
			InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
					.buscarPorIdUsuario(usuarioAutenticado.getId());

			int idSede = usuarioInformacion.getIdOrganizacion().getId();
			Paginacion pag = informeFacturasVencidasClienteService.listarinformeFacturasVencidasClientePaginado(idSede,
					pagina);
			respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
			respuestaDto.setObjetoRespuesta(pag);
			respuestaDto.setVariable(String.valueOf(informeFacturasVencidasClienteService.getTotalSaldo()));
			logger.info(respuestaDto.getVariable());
			return new ResponseEntity(respuestaDto, HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
			String msj = "Error listando informes de facturas vencidas de cliente... " + ex.getMessage();
			logger.error(ex);
			respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
			return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
		}
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	@GetMapping(value = ApiConstant.INFORME_FACTURAS_VENCIDAS_CLIENTE_API_LISTAR_VENCIDAS_CLIENTE_CONSULTA, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity listarinformeFacturasVencidasClienteConsultaPaginado(
			@RequestParam int idCliente, @RequestParam String fechaInicial, @RequestParam String fechaFinal,
			@RequestParam int pagina) {
		RespuestaDto respuestaDto;
		try {
			Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
			Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
			InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
					.buscarPorIdUsuario(usuarioAutenticado.getId());

			int idSede = usuarioInformacion.getIdOrganizacion().getId();
			Paginacion pag = informeFacturasVencidasClienteService.listarinformeFacturasVencidasClienteConsultaPaginado(
					idSede,
					idCliente, fechaInicial, fechaFinal, pagina);
			respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
			respuestaDto.setObjetoRespuesta(pag);
			respuestaDto.setVariable(String.valueOf(informeFacturasVencidasClienteService.getTotalSaldo()));
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
