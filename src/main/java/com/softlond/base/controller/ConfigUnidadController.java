package com.softlond.base.controller;

import com.softlond.base.constant.ApiConstant;
import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.ConfigUnidades;
import com.softlond.base.service.ConfigUnidadService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(ApiConstant.UNIDAD_CONTROL_API)
public class ConfigUnidadController {
    private final Logger logger = Logger.getLogger(getClass());

    @Autowired
    private ConfigUnidadService unidadService;

    @GetMapping(value = ApiConstant.UNIDAD_CONTROL_API_LISTAR_TODOS, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity listarUnidades() {
        RespuestaDto respuestaDto;
        try {
            List<ConfigUnidades> unidades = unidadService.listarTodos();
            respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
            respuestaDto.setObjetoRespuesta(unidades);
            return new ResponseEntity(respuestaDto, HttpStatus.OK);
        } catch (Exception ex) {
            String msj = "Error listando unidades... " + ex.getMessage();
            logger.error(ex);
            respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
            return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = ApiConstant.UNIDAD_CONTROL_API_CREAR, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity crearUnidad(@RequestBody ConfigUnidades body) {
        RespuestaDto respuestaDto;
        try {
            ConfigUnidades unidad = unidadService.crearUnidad(body);
            respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
            respuestaDto.setObjetoRespuesta(unidad);
            return new ResponseEntity(respuestaDto, HttpStatus.OK);
        } catch (Exception ex) {
            String msj = "Error creando unidad... " + ex.getMessage();
            logger.error(ex);
            respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
            return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = ApiConstant.UNIDAD_CONTROL_API_EDITAR, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity editarUnidad(@RequestBody ConfigUnidades body) {
        RespuestaDto respuestaDto;
        try {
            unidadService.crearUnidad(body);
            respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
            return new ResponseEntity(respuestaDto, HttpStatus.OK);
        } catch (Exception ex) {
            String msj = "Error editando unidad... " + ex.getMessage();
            logger.error(ex);
            respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
            return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = ApiConstant.UNIDAD_CONTROL_API_ELIMINAR, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity eliminarUnidad(@RequestParam Integer unidad) {
        RespuestaDto respuestaDto;
        try {
            unidadService.eliminarUnidad(unidad);
            respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
            return new ResponseEntity(respuestaDto, HttpStatus.OK);
        } catch (Exception ex) {
            String msj = "Error eliminando unidad... " + ex.getMessage();
            logger.error(ex);
            respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
            return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
        }
    }
}
