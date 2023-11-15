package com.softlond.base.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.softlond.base.constant.ApiConstant;
import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.ConfigTerClasificaciones;
import com.softlond.base.entity.Cuenta;
import com.softlond.base.service.ClasificacionService;

@Controller
@RequestMapping(ApiConstant.CLASIFICACION_CONTROLADOR_API)
public class ClasificacionController {
	
	 private final Logger logger = Logger.getLogger(getClass());


	@Autowired
	private ClasificacionService clasificacionService;
	
	@GetMapping(value = ApiConstant.CLASIFICACION_CONTROLADOR_API_LISTAR_TODOS, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity listarClasificaciones() {
        RespuestaDto respuestaDto;
        try {
            List<ConfigTerClasificaciones> clasificacion = clasificacionService.listarTodos();
            respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
            respuestaDto.setObjetoRespuesta(clasificacion);
            return new ResponseEntity(respuestaDto, HttpStatus.OK);
        } catch (Exception ex) {
            String msj = "Error listando clasificaciones... " + ex.getMessage();
            logger.error(ex);
            respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
            return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
        }
    }
	
	@PostMapping(value = ApiConstant.CLASIFICACION_CONTROLADOR_API_CREAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> crearClasificacion(@RequestBody ConfigTerClasificaciones clasificacion) {
		return clasificacionService.crearClasificacion(clasificacion);
	}
	
	@DeleteMapping(value = ApiConstant.CLASIFICACION_CONTROLADOR_API_REMOVE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> deleteClasificacion(@RequestParam Integer id){
		return this.clasificacionService.deleteClasificacion(id);
	}
}