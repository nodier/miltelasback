package com.softlond.base.controller;

import com.softlond.base.constant.ApiConstant;
import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.PrdTipos;
import com.softlond.base.response.TiposCantidadResponse;
import com.softlond.base.service.PrdTipoService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(ApiConstant.TIPO_CONTROL_API)
public class PrdTipoController {
    private final Logger logger = Logger.getLogger(getClass());

    @Autowired
    private PrdTipoService tipoService;

    @GetMapping(value = ApiConstant.TIPO_CONTROL_API_LISTAR_TODOS, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity listarTipos() {
        RespuestaDto respuestaDto;
        try {
            List<TiposCantidadResponse> tipos = tipoService.listarTodos();
            respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
            respuestaDto.setObjetoRespuesta(tipos);
            return new ResponseEntity(respuestaDto, HttpStatus.OK);
        } catch (Exception ex) {
            String msj = "Error listando tipos... " + ex.getMessage();
            logger.error(ex);
            respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
            return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = ApiConstant.TIPO_CONTROL_API_CREAR, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity crearTipo(@RequestBody PrdTipos body) {
        RespuestaDto respuestaDto;
        try {
            PrdTipos tipo = tipoService.crearTipo(body);
            respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
            respuestaDto.setObjetoRespuesta(tipo);
            return new ResponseEntity(respuestaDto, HttpStatus.OK);
        } catch (Exception ex) {
            String msj = "Error creando tipo... " + ex.getMessage();
            logger.error(ex);
            respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
            return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = ApiConstant.TIPO_CONTROL_API_EDITAR, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity editarTipo(@RequestBody PrdTipos body) {
        RespuestaDto respuestaDto;
        try {
            tipoService.editarTipo(body);
            respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
            return new ResponseEntity(respuestaDto, HttpStatus.OK);
        } catch (Exception ex) {
            String msj = "Error editando tipo... " + ex.getMessage();
            logger.error(ex);
            respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
            return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = ApiConstant.TIPO_CONTROL_API_ELIMINAR, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity eliminarTipo(@RequestParam Integer tipo) {
        RespuestaDto respuestaDto;
        try {
            tipoService.eliminarTipo(tipo);
            respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
            return new ResponseEntity(respuestaDto, HttpStatus.OK);
        } catch (Exception ex) {
            String msj = "Error eliminando tipo... " + ex.getMessage();
            logger.error(ex);
            respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
            return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping(value = ApiConstant.TIPO_CONTROL_API_LISTAR, produces = MediaType.APPLICATION_JSON_VALUE)
   	public @ResponseBody ResponseEntity<Object> listarTipo() {
   		return tipoService.listarTipo();
   	}
    @GetMapping(value = ApiConstant.TIPO_CONTROL_API_LISTAR_SELECTOR, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity listarTiposSelector() {
        RespuestaDto respuestaDto;
        try {
            List<PrdTipos> tipos = tipoService.listarTodosSelector();
            respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
            respuestaDto.setObjetoRespuesta(tipos);
            return new ResponseEntity(respuestaDto, HttpStatus.OK);
        } catch (Exception ex) {
            String msj = "Error listando tipos... " + ex.getMessage();
            logger.error(ex);
            respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
            return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping(value = ApiConstant.TIPO_CONTROL_API_LISTAR_FILTRO_TEXTO, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity listarTodosFiltroTexto(String texto) {
        RespuestaDto respuestaDto;
        try {
            List<PrdTipos> tipos = tipoService.listarTodosFiltroTexto(texto);
            respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
            respuestaDto.setObjetoRespuesta(tipos);
            return new ResponseEntity(respuestaDto, HttpStatus.OK);
        } catch (Exception ex) {
            String msj = "Error listando tipos... " + ex.getMessage();
            logger.error(ex);
            respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
            return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
        }
    }
}
