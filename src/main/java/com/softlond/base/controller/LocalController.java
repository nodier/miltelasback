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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.softlond.base.constant.ApiConstant;
import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.InformacionUsuario;
import com.softlond.base.entity.InveLocal;
import com.softlond.base.entity.Usuario;
import com.softlond.base.repository.UsuarioInformacionDao;
import com.softlond.base.response.Paginacion;
import com.softlond.base.service.LocalService;

@Controller
@RequestMapping(ApiConstant.LOCALES_CONTROL_API)
public class LocalController {
    private final Logger logger = Logger.getLogger(getClass());
    
	@Autowired
	UsuarioInformacionDao usuarioInformacionDao;

    @Autowired
    private LocalService localService;
    
    
    /* Listar todos los locales */
	@GetMapping(value = ApiConstant.LOCALES_CONTROL_API_LISTAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> listarLocaless() {
		return localService.listarLocaless();
	}
    
    @PreAuthorize("hasAuthority('SUPER') or hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('VENDEDOR')")
	@GetMapping(value = ApiConstant.LOCALES_CONTROL_API_LISTAR_LOCAL, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity listarLocalesPaginado(@RequestParam int pagina) {
		RespuestaDto respuestaDto;
		try {
			Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
			Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
			InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
					.buscarPorIdUsuario(usuarioAutenticado.getId());

			int idSede = usuarioInformacion.getIdOrganizacion().getId();
			Paginacion pag = localService.listarLocalesPaginado(idSede, pagina);
			respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
			respuestaDto.setObjetoRespuesta(pag);
			return new ResponseEntity(respuestaDto, HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
			String msj = "Error listando informes de facturas vencidas de compra... " + ex.getMessage();
			logger.error(ex);
			respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
			return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
		}
	}
    
    @GetMapping(value = ApiConstant.LOCALES_CONTROL_API_LISTAR_TODOS, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity listarLocales(Integer page) {
        RespuestaDto respuestaDto;
        return localService.listarLocales(page);
    }
    
    @PostMapping(value = ApiConstant.LOCALES_CONTROL_API_CREAR_LOCAL, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> crearLocal(@RequestBody InveLocal local) {
		return localService.crearLocal(local);
	}
    
    @PutMapping(value = ApiConstant.LOCALES_CONTROL_API_EDITAR_LOCAL, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> actualizarLocal(@RequestBody InveLocal local) {
		return localService.actualizarLocal(local);
	}
    
	//Eliminar Local
	@DeleteMapping(value = ApiConstant.LOCALES_CONTROL_API_ELIMINAR_LOCAL, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> eliminarLocal(Integer idLocal){
		return this.localService.eliminarLocal(idLocal);
	}
	
	 @GetMapping(value = ApiConstant.LOCALES_CONTROL_API_LISTAR_CONSULTA, produces = MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody ResponseEntity obtenerLocal(@PathVariable Integer page, Integer idLocal){
			return this.localService.obtenerLocal(idLocal, page);
		}
	 
	 @GetMapping(value = ApiConstant.LOCALES_CONTROL_API_LISTAR_CONSULTA2, produces = MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody ResponseEntity obtenerLocal2(@PathVariable Integer page){
			return this.localService.obtenerLocal2( page);
		}
	 
	 
		//Eliminar Actividad
		@DeleteMapping(value = ApiConstant.LOCALES_CONTROL_API_ELIMINAR, produces = MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<Object> eliminarLocalSi(Integer id){
			return this.localService.eliminarLocalSi(id);
		}
		
		@GetMapping(value = ApiConstant.LOCALES_CONTROL_API_LISTAR_TODOS_SEDE, produces = MediaType.APPLICATION_JSON_VALUE)
	    public @ResponseBody ResponseEntity<Object> listarLocalesSede() {
	        return localService.listarLocalesSede();
	    }
}
