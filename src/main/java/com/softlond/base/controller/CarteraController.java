package com.softlond.base.controller;

import org.apache.log4j.Logger;
// import org.hibernate.annotations.common.util.impl.Log_.logger;
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
import com.softlond.base.service.CarteraService;
import com.softlond.base.service.InformeFacturasVencidasClienteService;
// import com.softlond.base.service.InformeFacturasVencidasService;

@Controller
@RequestMapping(ApiConstant.CARTERA_CLIENTE_API)

public class CarteraController {

	private final Logger logger = Logger.getLogger(getClass());

	@Autowired
	UsuarioInformacionDao usuarioInformacionDao;

	@Autowired
	private CarteraService carteraService;

	@Autowired
	private InformeFacturasVencidasClienteService informeFacturasVencidasClienteService;

	@PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	@GetMapping(value = ApiConstant.CARTERA_CLIENTE_API_LISTAR_CARTERA_CLIENTE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity listarinformeFacturasVencidasClientePaginado(@RequestParam int pagina) {
		RespuestaDto respuestaDto;
		logger.info("ingresa a cartera controller");
		try {
			Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
			Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
			InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
					.buscarPorIdUsuario(usuarioAutenticado.getId());

			int idSede = usuarioInformacion.getIdOrganizacion().getId();
			logger.info(idSede + "-" + pagina);
			Paginacion pag = carteraService.listarCarteraPaginado(idSede, pagina);
			// if (pag != null) {
			// logger.info("existen paginas");
			// logger.info(pag);
			// }
			respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
			respuestaDto.setObjetoRespuesta(pag);
			return new ResponseEntity(respuestaDto, HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
			String msj = "Error listando cartera de cliente... " + ex.getMessage();
			logger.error(ex);
			respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
			return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
		}
	}

}
