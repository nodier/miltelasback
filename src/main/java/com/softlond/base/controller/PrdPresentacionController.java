package com.softlond.base.controller;

import com.softlond.base.constant.ApiConstant;
import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.PrdPresentacion;
import com.softlond.base.response.PresentacionCantidadResponse;
import com.softlond.base.service.PrdPresentacionService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(ApiConstant.PRESENTACION_CONTROL_API)
public class PrdPresentacionController {
    private final Logger logger = Logger.getLogger(getClass());

    @Autowired
    private PrdPresentacionService presentacionService;

    @GetMapping(value = ApiConstant.PRESENTACION_CONTROL_API_LISTAR_TODOS, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity listarPresentacion() {
        RespuestaDto respuestaDto;
        try {
            List<PresentacionCantidadResponse> presentaciones = presentacionService.listarTodos();
            respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
            respuestaDto.setObjetoRespuesta(presentaciones);
            return new ResponseEntity(respuestaDto, HttpStatus.OK);
        } catch (Exception ex) {
            String msj = "Error listando presentaciones... " + ex.getMessage();
            logger.error(ex);
            respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
            return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = ApiConstant.PRESENTACION_CONTROL_API_CREAR, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity crearPresentacion(@RequestBody PrdPresentacion body) {
        RespuestaDto respuestaDto;
        try {
            PrdPresentacion presentacion = presentacionService.crearPresentacion(body);
            respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
            respuestaDto.setObjetoRespuesta(presentacion);
            return new ResponseEntity(respuestaDto, HttpStatus.OK);
        } catch (Exception ex) {
            String msj = "Error creando presentacion... " + ex.getMessage();
            logger.error(ex);
            respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
            return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = ApiConstant.PRESENTACION_CONTROL_API_EDITAR, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity editarPresentacion(@RequestBody PrdPresentacion body) {
        RespuestaDto respuestaDto;
        try {
            presentacionService.editarPresentacion(body);
            respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
            return new ResponseEntity(respuestaDto, HttpStatus.OK);
        } catch (Exception ex) {
            String msj = "Error editando presentacion... " + ex.getMessage();
            logger.error(ex);
            respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
            return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = ApiConstant.PRESENTACION_CONTROL_API_ELIMINAR, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity eliminarPresentacion(@RequestParam Integer presentacion) {
        RespuestaDto respuestaDto;
        try {
            presentacionService.eliminarPresentacion(presentacion);
            respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
            return new ResponseEntity(respuestaDto, HttpStatus.OK);
        } catch (Exception ex) {
            String msj = "Error eliminando presentacion... " + ex.getMessage();
            logger.error(ex);
            respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
            return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
        }
    }
    
    /* Listar todos las presentaciones */
	@GetMapping(value = ApiConstant.PRESENTACION_CONTROL_API_LISTAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> listarPresentaciones() {
		return presentacionService.listarPresentaciones();
	}
	
	@GetMapping(value = ApiConstant.CLASIFICACION_CONTROL_API_LISTAR_PRESENTACION, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerPresentacion(Integer idPresentacion ) {
		return presentacionService.obtenerPresentacion(idPresentacion);
	}
    @GetMapping(value = ApiConstant.PRESENTACION_CONTROL_API_LISTAR_TODOS_SELECTOR, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity listarPresentacionSelector() {
        RespuestaDto respuestaDto;
        try {
            List<PrdPresentacion> presentaciones = presentacionService.obtenerPresentacionesSelector();
            respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
            respuestaDto.setObjetoRespuesta(presentaciones);
            return new ResponseEntity(respuestaDto, HttpStatus.OK);
        } catch (Exception ex) {
            String msj = "Error listando presentaciones... " + ex.getMessage();
            logger.error(ex);
            respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
            return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping(value = ApiConstant.PRESENTACION_CONTROL_API_LISTAR_TODOS_TEXTO, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity listarPresentacionTexto(String texto) {
        RespuestaDto respuestaDto;
        try {
            List<PrdPresentacion> presentaciones = presentacionService.obtenerPresentacionesTexto(texto);
            respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
            respuestaDto.setObjetoRespuesta(presentaciones);
            return new ResponseEntity(respuestaDto, HttpStatus.OK);
        } catch (Exception ex) {
            String msj = "Error listando presentaciones... " + ex.getMessage();
            logger.error(ex);
            respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
            return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
        }
    }
}
