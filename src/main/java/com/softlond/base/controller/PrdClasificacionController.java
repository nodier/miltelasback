package com.softlond.base.controller;

import com.softlond.base.constant.ApiConstant;
import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.PrdClasificaciones;
import com.softlond.base.response.ClasificacionCantidadResponse;
import com.softlond.base.service.PrdClasificacionService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(ApiConstant.CLASIFICACION_CONTROL_API)
public class PrdClasificacionController {
    private final Logger logger = Logger.getLogger(getClass());

    @Autowired
    private PrdClasificacionService clasificacionService;

    @GetMapping(value = ApiConstant.CLASIFICACION_CONTROL_API_LISTAR_TODOS, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity listarClasificaciones() {
        RespuestaDto respuestaDto;
        try {
            List<ClasificacionCantidadResponse> clasificaciones = clasificacionService.listarTodos();
            respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
            respuestaDto.setObjetoRespuesta(clasificaciones);
            return new ResponseEntity(respuestaDto, HttpStatus.OK);
        } catch (Exception ex) {
            String msj = "Error listando clasificaciones... " + ex.getMessage();
            logger.error(ex);
            respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
            return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = ApiConstant.CLASIFICACION_CONTROL_API_CREAR, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity crearClasificacion(@RequestBody PrdClasificaciones body) {
        RespuestaDto respuestaDto;
        try {
            PrdClasificaciones clasificacion = clasificacionService.crearClasificacion(body);
            respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
            respuestaDto.setObjetoRespuesta(clasificacion);
            return new ResponseEntity(respuestaDto, HttpStatus.OK);
        } catch (Exception ex) {
            String msj = "Error creando clasificación... " + ex.getMessage();
            logger.error(ex);
            respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
            return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = ApiConstant.CLASIFICACION_CONTROL_API_EDITAR, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity editarClasificacion(@RequestBody PrdClasificaciones body) {
        RespuestaDto respuestaDto;
        try {
            clasificacionService.editarClasificacion(body);
            respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
            return new ResponseEntity(respuestaDto, HttpStatus.OK);
        } catch (Exception ex) {
            String msj = "Error editando clasificación... " + ex.getMessage();
            logger.error(ex);
            respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
            return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = ApiConstant.CLASIFICACION_CONTROL_API_ELIMINAR, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity elminarClasificacion(@RequestParam Integer clasificacion) {
        RespuestaDto respuestaDto;
        try {
            clasificacionService.eliminarClasificacion(clasificacion);
            respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
            return new ResponseEntity(respuestaDto, HttpStatus.OK);
        } catch (Exception ex) {
            String msj = "Error eliminando clasificación... " + ex.getMessage();
            logger.error(ex);
            respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
            return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
        }
    }
    
	/* Listar todos las clasificaciones */
	@GetMapping(value = ApiConstant.CLASIFICACION_CONTROL_API_LISTAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> listarClasificacion() {
		return clasificacionService.listarClasificacion();
	}
	
}
