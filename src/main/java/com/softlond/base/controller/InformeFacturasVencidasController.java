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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.softlond.base.constant.ApiConstant;
import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.InformacionUsuario;
import com.softlond.base.entity.Usuario;
import com.softlond.base.repository.UsuarioInformacionDao;
import com.softlond.base.response.InfromeFacturasVencidas;
import com.softlond.base.response.Paginacion;
import com.softlond.base.service.InformeFacturasVencidasService;

@Controller
@RequestMapping(ApiConstant.INFORME_FACTURAS_VENCIDAS_API)

public class InformeFacturasVencidasController {

	private final Logger logger = Logger.getLogger(getClass());

	@Autowired
	UsuarioInformacionDao usuarioInformacionDao;

	@Autowired
	private InformeFacturasVencidasService informeFacturasVencidasService;

	@GetMapping(value = ApiConstant.INFORME_FACTURAS_VENCIDAS_API_LISTAR_VENCIDAS, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> listarinformeFacturasVencidas(Integer page) {
		return informeFacturasVencidasService.listarinformeFacturasVencidas(page);
	}

	/*
	 * @GetMapping(value =
	 * ApiConstant.INFORME_FACTURAS_VENCIDAS_API_LISTAR_VENCIDAS_COMPRA, produces =
	 * MediaType.APPLICATION_JSON_VALUE) public @ResponseBody ResponseEntity<Object>
	 * listarinformeFacturasVencidasCompra(Integer page) { return
	 * informeFacturasVencidasService.listarinformeFacturasVencidasCompra(page); }
	 */

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	@GetMapping(value = ApiConstant.INFORME_FACTURAS_VENCIDAS_API_LISTAR_VENCIDAS_COMPRA, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity listarinformeFacturasVencidasCompraPaginado(@RequestParam int pagina) {
		RespuestaDto respuestaDto;
		try {
			Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
			Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
			InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
					.buscarPorIdUsuario(usuarioAutenticado.getId());

			int idSede = usuarioInformacion.getIdOrganizacion().getId();
			Paginacion pag = informeFacturasVencidasService.listarinformeFacturasVencidasCompraPaginado(idSede, pagina);
			respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
			respuestaDto.setObjetoRespuesta(pag);
			respuestaDto.setVariable(String.valueOf(informeFacturasVencidasService.getTotalSuma()));
			return new ResponseEntity(respuestaDto, HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
			String msj = "Error listando informes de facturas vencidas de compra... " + ex.getMessage();
			logger.error(ex.getMessage() + " linea " + ex.getStackTrace()[0].getLineNumber() + " clase " + ex.getStackTrace()[0].getMethodName());
			respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
			return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
		}
	}

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	@GetMapping(value = ApiConstant.INFORME_FACTURAS_VENCIDAS_API_LISTAR_VENCIDAS_COMPRA_CONSULTA, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity listarinformeFacturasVencidasCompraConsultaPaginado(
			@RequestParam int idProveedor, @RequestParam String fechaInicial, @RequestParam String fechaFinal,
			@RequestParam int pagina) {
		RespuestaDto respuestaDto;
		try {
			Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
			Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
			InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
					.buscarPorIdUsuario(usuarioAutenticado.getId());

			int idSede = usuarioInformacion.getIdOrganizacion().getId();
			Paginacion pag = informeFacturasVencidasService.listarinformeFacturasVencidasCompraConsultaPaginado(idSede,
					idProveedor, fechaInicial, fechaFinal, pagina);
			respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
			respuestaDto.setObjetoRespuesta(pag);
			respuestaDto.setVariable(String.valueOf(informeFacturasVencidasService.getTotalSuma()));
			return new ResponseEntity(respuestaDto, HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
			String msj = "Error listando informes de facturas vencidas de compra... " + ex.getMessage();
			logger.error(ex);
			respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
			return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping(value = ApiConstant.INFORME_FACTURAS_VENCIDAS_API_LISTAR_VENCIDAS_CONSULTA, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> listarinformeFacturasVencidasConsulta(String fechaInicial, String fechaFinal,
			Integer idProveedor, Integer pagina) {
		return informeFacturasVencidasService.listarFacturasVencidasConsulta(fechaInicial, fechaFinal, idProveedor, pagina);
	}
	
	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	@GetMapping(value = ApiConstant.INFORME_FACTURAS_VENCIDAS_API_LISTAR_VENCIDAS_COMPRA_EXCEL, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity listarinformeFacturasVencidasCompraPaginado() {
		RespuestaDto respuestaDto;
		try {
			Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
			Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
			InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
					.buscarPorIdUsuario(usuarioAutenticado.getId());

			int idSede = usuarioInformacion.getIdOrganizacion().getId();
			List<InfromeFacturasVencidas> pag = informeFacturasVencidasService.listarinformeFacturasVencidasCompraExcel(idSede);
			respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
			respuestaDto.setObjetoRespuesta(pag);
			respuestaDto.setVariable(String.valueOf(informeFacturasVencidasService.getTotalSuma()));
			return new ResponseEntity(respuestaDto, HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
			String msj = "Error listando informes de facturas vencidas de compra... " + ex.getMessage();
			logger.error(ex.getMessage() + " linea " + ex.getStackTrace()[0].getLineNumber() + " clase " + ex.getStackTrace()[0].getMethodName());
			respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
			return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
		}
	}

}
